package com.tokopedia.kolcomponent.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author by milhamj on 04/12/18.
 */
abstract class BaseViewHolder<T>(val container: ViewGroup) {

    lateinit var itemView: View

    abstract var layoutRes: Int

    abstract fun bind(element: T)

    fun inflate(element: T): View {
        itemView = LayoutInflater.from(container.context).inflate(layoutRes, container, false)
        bind(element)
        return itemView
    }
}