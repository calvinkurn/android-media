package com.tokopedia.product.detail.postatc.base

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder

abstract class PostAtcViewHolder<T>(view: View) : BaseViewHolder(view) {
    abstract fun bind(element: T)
}
