package com.tokopedia.feedcomponent.helper

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jegul on 2019-10-09.
 */
abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun onViewRecycled() {}

    protected fun getString(@StringRes stringRes: Int): String {
        return itemView.context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, value: String): String {
        return itemView.context.getString(stringRes, value)
    }
}