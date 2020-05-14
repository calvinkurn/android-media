package com.tokopedia.recommendation_widget_common.domain.coroutines.base

abstract class UseCase<in InputType, out OutputType> {
    abstract suspend fun getData(inputParameter: InputType) : OutputType
}