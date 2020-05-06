package com.tokopedia.home_wishlist.view.ext

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_wishlist.model.datamodel.EmptyWishlistDataModel
import com.tokopedia.home_wishlist.model.datamodel.ErrorWishlistDataModel

fun <T> List<T>.copy(): MutableList<T> = ArrayList(this)

fun List<Visitable<*>>.isErrorWishlist() = this.filterIsInstance<ErrorWishlistDataModel>().isNotEmpty()

fun List<Visitable<*>>.isEmptyWishlist() = this.filterIsInstance<EmptyWishlistDataModel>().isNotEmpty()

fun List<Any>.equalsList(list2: List<Any>): Boolean{
    if (this.size != list2.size)
        return false

    val pairList = this.zip(list2)

    return pairList.all { (elt1, elt2) ->
        elt1 == elt2
    }
}