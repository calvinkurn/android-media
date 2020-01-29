package com.tokopedia.entertainment.adapter

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable

abstract class SmartAbstractViewHolder<T : Visitable<*>>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(element: T)

    protected fun getString(@StringRes stringRes: Int): String {
        return itemView.context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, value: String): String {
        return itemView.context.getString(stringRes, value)
    }
}