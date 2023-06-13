package com.tokopedia.linter.detectors.sourcescanner.method

object MethodClassRuleManager {
    val methodClassNameMapping = HashMap<String, ArrayList<MethodClassMapper>>()
    val expectedClassNameMapping = HashMap<MethodClassMapper, MethodClassMapper>()

    init {

        val messageForToInt = "Using this method can result in bad behaviour. " +
                "Prefer to use toIntOrZero() method or please make sure with the Business and Backend team, data on " +
                "which you are calling this method always lies in the range INT_MIN to INT_MAX"
        createMap("StringExt","toIntOrZero",
            "kotlin.text.StringsKt__StringNumberConversionsJVMKt","toInt",
            messageForToInt)
        createMap("StringExt","toIntOrZero",
            "java.lang.Integer","parseInt",messageForToInt)
    }
    fun createMap(expectedClassName: String, expectedMethodName: String,
                  classNames: String, methodName: String, message:String? = null) {
        mapMethodClasstoExpectedClass(
            getMethodClassMappingArray(methodName),
            expectedClassName,
            expectedMethodName,
            classNames,
            methodName,
            message
        )
    }

    fun getMethodClassMappingArray(methodName: String) = methodClassNameMapping[methodName]?: getNewArrayList(methodName)

    fun getNewArrayList(methodName: String):ArrayList<MethodClassMapper> {
        return ArrayList<MethodClassMapper>().apply {
            methodClassNameMapping[methodName]= this
        }

    }

    private fun mapMethodClasstoExpectedClass(list:ArrayList<MethodClassMapper>, expectedClassName: String,
                                              expectedMethodName: String, classNames: String, methodName: String,message: String?) {

        MethodClassMapper(classNames, methodName).apply {
            list.add(this)
            expectedClassNameMapping[this] = MethodClassMapper(expectedClassName, expectedMethodName,message)
        }
    }

    fun checkMethodContaingClass(classNames: String, methodName: String) = methodClassNameMapping[methodName]?.contains(MethodClassMapper(classNames, methodName))?:false

    fun getExpectedMethodClass(classNames: String,methodName: String) = expectedClassNameMapping[MethodClassMapper(classNames,methodName)]

}