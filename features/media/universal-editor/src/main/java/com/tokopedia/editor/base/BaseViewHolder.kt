package com.tokopedia.editor.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder<T>(view: View) : ViewHolder(view) {

    abstract fun onBind(data: T)
}
