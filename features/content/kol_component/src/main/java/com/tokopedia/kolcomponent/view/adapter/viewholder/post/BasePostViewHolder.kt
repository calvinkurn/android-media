package com.tokopedia.kolcomponent.view.adapter.viewholder.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author by milhamj on 04/12/18.
 */
abstract class BasePostViewHolder<T> {

    lateinit var itemView: View
    lateinit var context: Context
    var position = 0

    abstract var layoutRes: Int

    abstract fun bind(element: T)

    fun inflate(container: ViewGroup, element: T, position: Int): View {
        this.itemView = LayoutInflater.from(container.context).inflate(layoutRes, container, false)
        this.context = itemView.context
        this.position = position
        bind(element)
        return itemView
    }
}