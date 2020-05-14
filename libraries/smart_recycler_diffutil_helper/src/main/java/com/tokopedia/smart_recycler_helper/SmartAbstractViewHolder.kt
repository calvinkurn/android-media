package com.tokopedia.smart_recycler_helper

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

abstract class SmartAbstractViewHolder<T : SmartVisitable<*>>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(element: T, listener: SmartListener)

    /*
    This method is used to bind the view holder when only some parts of the model is changed.
    This way, the view holder won't be fully bind, you can control which part of it to re-bind.
    You can refer to https://medium.com/livefront/recyclerview-trick-selectively-bind-viewholders-with-payloads-4b28e3d2cce8
    on how to use it.

    Override this method to do the partial bind
     */
    open fun bind(element: T, listener: SmartListener, payloads: List<Any>) {}

    /*
    Override this to do recycle and clear image resources
     */
    fun onViewRecycled() {}

    protected fun getString(@StringRes stringRes: Int): String {
        return itemView.context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, value: String): String {
        return itemView.context.getString(stringRes, value)
    }
}