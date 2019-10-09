package com.tokopedia.feedcomponent.helper

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-01.
 */
interface AdapterDelegate<T> {

    fun isForViewType(itemList: List<T>, position: Int): Boolean

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun onBindViewHolder(itemList: List<T>, position: Int, holder: RecyclerView.ViewHolder)
}

fun AdapterDelegate<*>.getView(parent: ViewGroup, @LayoutRes layoutRes: Int): View = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)