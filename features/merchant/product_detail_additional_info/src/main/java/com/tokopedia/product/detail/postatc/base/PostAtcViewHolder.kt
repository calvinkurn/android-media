package com.tokopedia.product.detail.postatc.base

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder

abstract class PostAtcViewHolder<T : PostAtcUiModel>(val view: View) : BaseViewHolder(view) {

    abstract fun bind(element: T)

    protected fun getComponentTrackData(element: T): ComponentTrackData {
        return ComponentTrackData(
            element.name,
            element.type,
            (bindingAdapterPosition + 1).toString()
        )
    }
}
