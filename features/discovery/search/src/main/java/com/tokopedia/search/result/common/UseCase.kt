package com.tokopedia.search.result.common

abstract class UseCase<out OutputType> {

    abstract suspend fun execute(inputParameter: Map<String, Any>): OutputType
}