package com.tokopedia.analytic.processor

import com.google.auto.service.AutoService
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.tokopedia.analytic.annotation.DefinedInCollections
import com.tokopedia.analytic.annotation.Key
import com.tokopedia.analytic.processor.utils.isList
import com.tokopedia.analytic.processor.utils.isMap
import com.tokopedia.analytic.processor.utils.isRawType
import com.tokopedia.analytic.processor.utils.isSet
import com.tokopedia.analyticparam.AnalyticParameter
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.AnalyticRules
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.defaultvalues.*
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

@AutoService(Processor::class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
class AnnotationProcessor : AbstractProcessor() {

    companion object {
        val foundParams = mutableSetOf<String>()
        val bundleableElements = mutableSetOf<Element>()
        val analyticEventElements = mutableSetOf<Element>()
        var messenger: Messager? = null
        var filer: Filer? = null
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        messenger = processingEnv?.messager
        filer = processingEnv?.filer
    }

    override fun process(
        elements: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        if (roundEnv != null) {
            bundleableElements.addAll(roundEnv.getElementsAnnotatedWith(BundleThis::class.java))
            analyticEventElements.addAll(roundEnv.getElementsAnnotatedWith(AnalyticEvent::class.java))
            if (bundleableElements.isNotEmpty()) {
                processAnnotatedModelClass()
            }
            if (analyticEventElements.isNotEmpty()) {
                processAnnotatedEventClass()
            }

            if (roundEnv.errorRaised()) return false
            generateFiles()
        }

        return true
    }

    private fun processAnnotatedModelClass() {
        AnnotatedModelClass.getAnnotatedClasses(processingEnv)
        AnnotatedModelClass.annotatedClasses.forEach {
            it.fields.putAll(ModelClassField.getClassFields(it))
        }
    }

    private fun processAnnotatedEventClass() {
        AnnotatedEventClass.getAnnotatedEventClasses(processingEnv)
        AnnotatedEventClass.annotatedEventClass.forEach {
            it.fields.putAll(ModelClassField.getClassFields(it))
        }

        AnnotatedEventClass.annotatedEventClass.forEach {
            validateRequired(it)
        }
    }

    private fun validateRequired(eventClass: AnnotatedEventClass) {
        val rulesClass = Class.forName(eventClass.rulesClass.toString()).kotlin
        val required = rulesClass.companionObject?.memberProperties?.find {
            it.name == "rules"
        }?.getter?.call(rulesClass.companionObjectInstance) as Map<*, *>
        val keys = mutableListOf<String>()
        eventClass.fields.forEach {
            if (it.value.element.getAnnotation(DefinedInCollections::class.java) == null) {
                keys.add(it.key)
            }
        }
        if (!keys.containsAll(required.keys)) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Some required bundle element is not present", eventClass.element
            )
            return
        }
        required.entries.forEach { rule ->
            if ((rule.value as AnalyticParameter).required != null) {
                val field = eventClass.fields[rule.key]!!
                val fieldTypeName = TypeName.get(field.element.asType())

                if (!isRawType(fieldTypeName)) {
                    field.element as Symbol
                    val type: Type = if (isList(fieldTypeName) || isSet(fieldTypeName)) {
                        (field.element.asType() as Type.ClassType).typarams_field[0]
                    } else if (isMap(fieldTypeName)) {
                        (field.element.asType() as Type.ClassType).typarams_field[1]
                    } else {
                        field.element.asType()
                    }

                    AnnotatedModelClass.annotatedClasses.find {
                        it.getFqName() == type.toString()
                    }?.run {
                        validateRequired(
                            this,
                            (rule.value as AnalyticParameter).required as Map<String, AnalyticParameter>
                        )
                    }
                }
            }
        }
    }

    private fun validateRequired(
            fieldClass: AnnotatedModelClass,
            required: Map<String, AnalyticParameter>
    ) {
        if (!fieldClass.fields.keys.containsAll(required.keys)) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Some required bundle element is not present", fieldClass.element
            )
            return
        }

        required.entries.forEach { entry ->
            if (entry.value.required != null) {
                val field = fieldClass.fields[entry.key]!!
                val fieldTypeName = TypeName.get(field.element.asType())

                if (!isRawType(fieldTypeName)) {
                    field.element as Symbol
                    val type: Type = if (isList(fieldTypeName) || isSet(fieldTypeName)) {
                        (field.element.asType() as Type.ClassType).typarams_field[0]
                    } else if (isMap(fieldTypeName)) {
                        (field.element.asType() as Type.ClassType).typarams_field[1]
                    } else {
                        field.element.asType()
                    }

                    AnnotatedModelClass.annotatedClasses.find {
                        it.getFqName() == type.toString()
                    }?.run {
                        validateRequired(
                            this,
                            entry.value.required as Map<String, AnalyticParameter>
                        )
                    }
                }
            }
        }
    }

    private fun generateFiles() {
        val checkerFiles = mutableListOf<JavaFile>()
        val modelFiles = mutableListOf<JavaFile>()
        AnnotatedModelClass.annotatedClasses.forEach {
            val modelClassGenerator = ModelClassGenerator(it)
            modelFiles.add(modelClassGenerator.generate())
            if (modelClassGenerator.hasCustomChecker()) {
                checkerFiles.add(modelClassGenerator.generateCheckerInterface())
            }
        }
        try {
            modelFiles.forEach {
                it.writeTo(processingEnv.filer)
            }
        } catch (ignored: FilerException) {
        }

        val eventFiles = mutableListOf<JavaFile>()
        AnnotatedEventClass.annotatedEventClass.forEach {
            val eventClassGenerator = EventClassGenerator(it)
            eventFiles.add(eventClassGenerator.generate())
            if (eventClassGenerator.hasCustomChecker()) {
                checkerFiles.add(eventClassGenerator.generateCheckerInterface())
            }
        }
        try {
            eventFiles.forEach {
                it.writeTo(processingEnv.filer)
            }
        } catch (ignored: FilerException) {
        }

        try {
            checkerFiles.forEach {
                it.writeTo(processingEnv.filer)
            }
        } catch (ignored: FilerException) {
        }
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            BundleThis::class.java.name,
            AnalyticEvent::class.java.name,
            Key::class.java.name,
            AnalyticRules::class.java.name,
            DefinedInCollections::class.java.name,
            Default::class.java.name,
            DefaultValueString::class.java.name,
            DefaultValueLong::class.java.name,
            DefaultValueChar::class.java.name,
            DefaultValueDouble::class.java.name,
            DefaultValueFloat::class.java.name,
            DefaultValueInt::class.java.name,
            DefaultValueShort::class.java.name,
            DefaultValueBoolean::class.java.name,
            DefaultValueByte::class.java.name
        )
    }
}