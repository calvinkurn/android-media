package com.tokopedia.linter.detectors.sourcescanner.method

object MethodClassRuleManager {
    val methodClassNameMapping = HashMap<String, ArrayList<MethodClassMapper>>()
    val expectedClassNameMapping = HashMap<MethodClassMapper, MethodClassMapper>()

    fun createMap(expectedClassName: String, expectedMethodName: String, classNames: String, methodName: String) {
        methodClassNameMapping[methodName]?.add(
                MethodClassMapper(classNames, methodName).apply {
                    expectedClassNameMapping[this] = MethodClassMapper(expectedClassName, expectedMethodName)
                })
    }

    fun checkMethodContaingClass(classNames: String, methodName: String) = methodClassNameMapping[methodName]?.contains(MethodClassMapper(classNames, methodName))?:false

    fun getExpectedMethodClass(classNames: String,methodName: String) = expectedClassNameMapping[MethodClassMapper(classNames,methodName)]

}