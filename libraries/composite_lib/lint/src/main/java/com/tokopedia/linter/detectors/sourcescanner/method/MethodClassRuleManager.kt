package com.tokopedia.linter.detectors.sourcescanner.method

object MethodClassRuleManager {
    val methodClassNameMapping = HashMap<String, ArrayList<MethodClassMapper>>()
    val expectedClassNameMapping = HashMap<MethodClassMapper, MethodClassMapper>()

    init {
        createMap("StringExt","toIntRangeCheck","kotlin.text.StringsKt__StringNumberConversionsJVMKt","toInt")
        createMap("StringExt","toIntRangeCheck","java.lang.Integer","parseInt")
    }
    fun createMap(expectedClassName: String, expectedMethodName: String, classNames: String, methodName: String) {
        {methodClassNameMapping[methodName]?: getNewArrayList(methodName)}.let {
            addMethodNameAndExpectedClassName(
                it(),
                expectedClassName, expectedMethodName, classNames, methodName
            )
        }
    }

    fun getNewArrayList(methodName: String):ArrayList<MethodClassMapper> {
        return ArrayList<MethodClassMapper>().apply {
            methodClassNameMapping[methodName]= this
        }

    }

    fun addMethodNameAndExpectedClassName(list:ArrayList<MethodClassMapper>,expectedClassName: String, expectedMethodName: String, classNames: String, methodName: String) {
        list.add(MethodClassMapper(classNames, methodName).apply {
            expectedClassNameMapping[this] = MethodClassMapper(expectedClassName, expectedMethodName)
        })
    }

    fun checkMethodContaingClass(classNames: String, methodName: String) = methodClassNameMapping[methodName]?.contains(MethodClassMapper(classNames, methodName))?:false

    fun getExpectedMethodClass(classNames: String,methodName: String) = expectedClassNameMapping[MethodClassMapper(classNames,methodName)]

}