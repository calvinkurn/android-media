package com.tokopedia.addongifting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.ProductUiModel

interface AddOnListTypeFactory {

    fun type(uiModel: AddOnUiModel): Int

    fun type(uiModel: ProductUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>

}