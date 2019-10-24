package com.tokopedia.home_wishlist.base

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * <T> is Custom type factory.
 */
interface SmartVisitable<T> : Visitable<T>{
    fun equalsDataModel(dataModel: Visitable<*>): Boolean
    fun getUniqueIdentity() : Any
}