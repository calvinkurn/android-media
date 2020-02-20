package com.tokopedia.smart_recycler_helper

/**
 * <T> is Custom type factory.
 */
interface SmartVisitable<T>{
    fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean
    fun type(typeFactory: T): Int
    fun getUniqueIdentity() : Any
}