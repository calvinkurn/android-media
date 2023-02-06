package com.tokopedia.sellerpersona.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerpersona.view.adapter.viewholder.BaseOptionViewHolder
import com.tokopedia.sellerpersona.view.adapter.viewholder.OptionMultipleViewHolder
import com.tokopedia.sellerpersona.view.adapter.viewholder.OptionSingleViewHolder
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

class OptionAdapterFactoryImpl(
    private val listener: BaseOptionViewHolder.Listener
) : BaseAdapterTypeFactory(), OptionAdapterFactory {

    override fun type(model: BaseOptionUiModel.QuestionOptionSingleUiModel): Int {
        return OptionSingleViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseOptionUiModel.QuestionOptionMultipleUiModel): Int {
        return OptionMultipleViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OptionSingleViewHolder.RES_LAYOUT -> OptionSingleViewHolder(listener, parent)
            OptionMultipleViewHolder.RES_LAYOUT -> OptionMultipleViewHolder(listener, parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}