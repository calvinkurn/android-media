package com.tokopedia.feedcomponent.helper

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * Created by jegul on 2019-10-01.
 */
abstract class TypedAdapterDelegate<T: ST, ST: Any, VH : RecyclerView.ViewHolder>(@LayoutRes layoutRes: Int) : BaseAdapterDelegate<T, ST, VH>(layoutRes) {

    @Suppress("UNCHECKED_CAST")
    val itemClass: Class<T> = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first() as Class<T>)

    override fun isForViewType(itemList: List<ST>, position: Int): Boolean {
        return itemList[position]::class.java == itemClass
    }
}