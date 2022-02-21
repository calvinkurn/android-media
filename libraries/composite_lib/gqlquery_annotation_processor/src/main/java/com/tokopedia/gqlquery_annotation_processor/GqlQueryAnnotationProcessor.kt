import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import java.util.*
import java.util.regex.Pattern
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import kotlin.collections.ArrayList
import com.squareup.javapoet.MethodSpec


@AutoService(Processor::class)
class GqlQueryAnnotationProcessor : AbstractProcessor() {

    companion object {
        var messenger: Messager? = null
        var filer: Filer? = null
        val variableGqlQuery: String = "GQL_QUERY"
        const val REGEX_QUERY_NAME =
            "\\w+[(][$]|\\w+[(]|\\w+[{]|\\w+\\s+[(][$]|\\w+\\s+[(]|\\w+\\s[{]";
        val REGEX_NO_SPECIAL_CHAR = "[^a-zA-Z0-9]+".toRegex()
        const val regexQueryList = "(?:\\{|[}]|\\W )(?: *?|\\t*?)*(\\w*?|_*?) *?\\("
        const val regexOpName = """(\w*?|_*?) *?\("""
        val patternQueryList = Pattern.compile(regexQueryList, Pattern.MULTILINE)
        val patternOpName = Pattern.compile(regexOpName, Pattern.MULTILINE)
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        messenger = processingEnv?.messager
        filer = processingEnv?.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {

        val elementsWithGqQuery = roundEnv?.getElementsAnnotatedWith(GqlQuery::class.java)
        elementsWithGqQuery?.forEach {
            if (it.kind == ElementKind.CLASS || it.kind == ElementKind.METHOD) {
                val elementAnnotated = it.getAnnotation(GqlQuery::class.java)
                val queryName = elementAnnotated.queryName
                val queryValue = elementAnnotated.queryValue

                val formattedQuery = formatToSingleLineString(queryValue)

                val stringClass = ClassName.get("java.lang", "String")
                val arrayListClass = ClassName.get("java.util", "ArrayList")

                val fieldSpecQuery = FieldSpec.builder(
                    stringClass,
                    variableGqlQuery,
                    Modifier.PUBLIC,
                    Modifier.STATIC,
                    Modifier.FINAL
                )
                    .initializer("\$S", formattedQuery)
                    .build()

                val list = ClassName.get("java.util", "List")
                val listType: TypeName =
                    ParameterizedTypeName.get(list, ClassName.get("java.lang", "String"))

                val value = getQueryInside(formattedQuery)
                val codeBlockList = mutableListOf<CodeBlock>()
                for (aValue in value) {
                    codeBlockList.add(CodeBlock.of("\$S", aValue))
                }
                val operationNameListMethodBuilder = MethodSpec
                    .methodBuilder("getOperationNameList")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .returns(listType)

                if (codeBlockList.isEmpty()) {
                    operationNameListMethodBuilder.addStatement("return new \$T()", arrayListClass)
                } else {
                    operationNameListMethodBuilder.addStatement(
                        "return \$T.asList(\$L)",
                        Arrays::class.java,
                        CodeBlock.join(codeBlockList, ", ")
                    )
                }
                val operationNameListMethod = operationNameListMethodBuilder.build()

                val queryNameMethod = MethodSpec
                    .methodBuilder("getQuery")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .returns(stringClass)
                    .addStatement("return $variableGqlQuery")
                    .build()

                val topOpNameMethod = MethodSpec
                    .methodBuilder("getTopOperationName")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .returns(stringClass)
                    .addStatement("return \"" + getTopQueryName(formattedQuery) +"\"")
                    .build()

                val typeSpecBuilder = TypeSpec.classBuilder(queryName.ccapitalize())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(GqlQueryInterface::class.java)
                    .addMethod(operationNameListMethod)
                    .addMethod(topOpNameMethod)
                    .addMethod(queryNameMethod)
                    .addField(fieldSpecQuery)

                val typeSpec = typeSpecBuilder.build()

                val javaFile = JavaFile.builder(
                    processingEnv.elementUtils.getPackageOf(it).toString(),
                    typeSpec
                )
                    .build()
                javaFile.writeTo(filer)
            } else {
                messenger?.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Annotation can only one applied on Method or class",
                    it
                )
            }
        }
        return true
    }

    fun String.ccapitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
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
            .replace("+", "")
            .replace("\\s+".toRegex(), " ")
            .replace(System.getProperty("line.separator"), "")
            .trim()
    }

    private fun getTopQueryName(input: String): String? {
        val m = patternOpName.matcher(input)

        while (m.find()) {
            return m.group(1).replace(REGEX_NO_SPECIAL_CHAR, "")
        }
        return ""
    }

    private fun getQueryInside(input: String): ArrayList<String> {
        val m = patternQueryList.matcher(input)
        val list = arrayListOf<String>()

        while (m.find()) {
            list.add(m.group(1))
        }
        return list
    }
}
