package com.tokopedia.feedcomponent.helper

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * Created by jegul on 2019-10-01.
 */
abstract class TypedAdapterDelegate<T: ST, ST: Any, VH : RecyclerView.ViewHolder>(@LayoutRes private val layoutRes: Int) : AdapterDelegate<ST> {

    @Suppress("UNCHECKED_CAST")
    val itemClass: Class<T> = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first() as Class<T>)

    abstract fun onBindViewHolder(item: T, holder: VH)

    abstract fun onCreateViewHolder(parent: ViewGroup, basicView: View): VH

    override fun isForViewType(itemList: List<ST>, position: Int): Boolean {
        return itemList[position]::class.java == itemClass
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(itemList: List<ST>, position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(itemList[position] as T, holder as VH)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolder(parent, getView(parent, layoutRes))
    }
}