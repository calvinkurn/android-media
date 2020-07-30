package com.tokopedia.home_wishlist.base

abstract class UseCase<in InputType, out OutputType> {
    abstract suspend fun getData(inputParameter: InputType): OutputType
}