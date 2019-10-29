package com.tokopedia.shop.common.view

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BaseMembershipViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)
}