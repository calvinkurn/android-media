package com.tokopedia.adapterdelegate

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.ParameterizedType

/**
 * Created by jegul on 2019-10-01.
 */
abstract class TypedAdapterDelegate<T: ST, ST: Any, VH : RecyclerView.ViewHolder>(@LayoutRes layoutRes: Int) : BaseAdapterDelegate<T, ST, VH>(layoutRes) {

    @Suppress("UNCHECKED_CAST")
    val itemClass: Class<T> = ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first() as Class<T>)

    override fun isForViewType(itemList: List<ST>, position: Int, isFlexibleType: Boolean): Boolean {
        return if (isFlexibleType) itemClass.isAssignableFrom(itemList[position]::class.java)
        else itemList[position]::class.java == itemClass
    }
}