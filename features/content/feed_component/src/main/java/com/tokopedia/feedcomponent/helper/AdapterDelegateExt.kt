package com.tokopedia.feedcomponent.helper

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by jegul on 2019-10-09.
 */
fun <T> AdapterDelegate<T>.getView(parent: ViewGroup, @LayoutRes layoutRes: Int): View = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)