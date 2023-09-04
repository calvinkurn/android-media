package com.tokopedia.gqlquery_ksp_processor

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.tokopedia.gql_query_annotation.GqlQuery
import java.io.OutputStream
import java.util.*
import java.util.regex.Pattern

class GqlSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    companion object {
        private const val GROUP = 1
        private const val regexQueryList = "(?:\\{|[}]|\\W )(?: *?|\\t*?)*(\\w*?|_*?) *?\\("
        private const val regexOpName = """(\w*?|_*?) *?\("""
        private val REGEX_NO_SPECIAL_CHAR = "[^a-zA-Z0-9]+".toRegex()
        private val patternQueryList: Pattern = Pattern.compile(regexQueryList, Pattern.MULTILINE)
        private val patternOpName: Pattern = Pattern.compile(regexOpName, Pattern.MULTILINE)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GqlQuery::class.qualifiedName!!)
        val unableToProcess = symbols.filterNot { it.validate() }
        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        symbols.filter { (it is KSClassDeclaration || it is KSFunctionDeclaration) && it.validate() }
            .forEach { it.accept(GqlSymbolVisitor(dependencies), Unit) }

        return unableToProcess.toList()
    }

    private inner class GqlSymbolVisitor(val dependencies: Dependencies) : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            if (classDeclaration.isAbstract()) {
                logger.error("should not use abstract class")
            }

            if (classDeclaration.annotations.toList().isEmpty()) {
                logger.error("annotations not found")
            }

            val packageName = classDeclaration.packageName.asString()

            val annotation = classDeclaration.annotations.first { annotation ->
                annotation.shortName.asString() == GqlQuery::class.simpleName
            }
            generateFile(packageName, annotation, dependencies)
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            if (function.isAbstract) {
                logger.error("should not use abstract method")
            }
            if (function.annotations.toList().isEmpty()) {
                logger.error("annotations not found")
            }

            val packageName = function.packageName.asString()

            val annotation = function.annotations.first { annotation ->
                annotation.shortName.asString() == GqlQuery::class.simpleName
            }

            generateFile(packageName, annotation, dependencies)
        }
    }

    private fun generateFile(
        packageName: String,
        annotation: KSAnnotation,
        dependencies: Dependencies
    ) {
        val queryName = annotation.arguments.first {
            it.name?.asString() == GqlQuery::queryName.name
        }.value as String
        val queryValue = annotation.arguments.first {
            it.name?.asString() == GqlQuery::queryValue.name
        }.value as String

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = queryName.capitalize()
        )

        val singleLineQuery = queryValue.formatToSingleLineString()
        val topOperationName = singleLineQuery.getTopQueryName()

        val queryInside = queryValue.getQueryInside().map { "\"$it\"" }

        val operationName = if (queryInside.isEmpty()) {
            "emptyList()"
        } else {
            "listOf(${queryInside.joinToString(",")})"
        }
        outputStream.use {
            it.write(
                """
                |package $packageName
                
                |import com.tokopedia.gql_query_annotation.GqlQueryInterface
                |
                |class ${queryName.capitalize()} : GqlQueryInterface {
                
                |    companion object {
                |        const val GQL_QUERY = "$singleLineQuery"
                |    }
                
                |    override fun getQuery(): String = GQL_QUERY
                
                |    override fun getOperationNameList(): List<String> = $operationName
                
                |    override fun getTopOperationName(): String = "$topOperationName"
                |}
                """.trimMargin().toByteArray()
            )
        }
    }

    private fun String.formatToSingleLineString(): String {
        return this
            .replace("\\n", "")
            .replace("\\t", "")
            .replace("\"", "\\\"")
            .replace("+", "")
            .replace("\\s+".toRegex(), " ")
            .replace(System.getProperty("line.separator"), "")
            .replace("$", "\${\"$\"}")
            .trim()
    }

    private fun String.getTopQueryName(): String {
        val m = patternOpName.matcher(this)

        while (m.find()) {
            return m.group(GROUP).replace(REGEX_NO_SPECIAL_CHAR, "")
        }
        return ""
    }

    private fun String.getQueryInside(): ArrayList<String> {
        val m = patternQueryList.matcher(this)
        val list = arrayListOf<String>()

        while (m.find()) {
            list.add(m.group(GROUP))
        }
        return list
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}
