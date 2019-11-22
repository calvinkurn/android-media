package com.tokopedia.feedcomponent.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Created by jegul on 2019-10-09.
 */
fun <T> AdapterDelegate<T>.getView(parent: ViewGroup, @LayoutRes layoutRes: Int): View = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)