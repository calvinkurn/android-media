package com.tokopedia.adapterdelegate

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

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