import com.google.auto.service.AutoService
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


@AutoService(Processor::class)
class GqlQueryAnnotationProcessor : AbstractProcessor() {


    companion object {
        var messenger: Messager? = null
        var filer: Filer? = null
        val variableName : String = "GQL_QUERY"
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        messenger = processingEnv?.messager
        filer = processingEnv?.filer
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

        val elementsWithGqQuery = roundEnv?.getElementsAnnotatedWith(GqlQuery::class.java)
        elementsWithGqQuery?.forEach {
            if(it.kind == ElementKind.CLASS || it.kind == ElementKind.METHOD) {
                val elementAnnotated = it.getAnnotation(GqlQuery::class.java)
                val queryName = elementAnnotated.queryName
                val queryValue = elementAnnotated.queryValue

                val formattedQuery = formatToSingleLineString(queryValue)

                val fieldSpec = FieldSpec.builder(
                        ClassName.get("java.lang", "String"),
                        variableName,
                        Modifier.PUBLIC,
                        Modifier.STATIC,
                        Modifier.FINAL
                )
                        .initializer("\$S", formattedQuery)
                        .build()

                val typeSpec = TypeSpec.classBuilder(queryName.capitalize())
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addField(fieldSpec)
                        .build()

                val javaFile = JavaFile.builder(processingEnv.elementUtils.getPackageOf(it).toString(), typeSpec)
                        .build()
                javaFile.writeTo(filer)
            } else {
                messenger?.printMessage(Diagnostic.Kind.ERROR, "Annotation can only one applied on Method or class", it)
            }
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GqlQuery::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }


    private fun formatToSingleLineString(query: String): String {
        return query
                .replace("\\n", "")
                .replace("\\t", "")
                .replace("\\", "")
                .replace("\"", "")
                .replace("+", "")
                .replace("\\s+".toRegex(), " ")
                .replace(System.getProperty("line.separator"), "")
    }
} 