package com.tokopedia.sellerpersona.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerpersona.view.adapter.viewholder.OptionMultipleViewHolder
import com.tokopedia.sellerpersona.view.adapter.viewholder.OptionSingleViewHolder
import com.tokopedia.sellerpersona.view.model.QuestionOptionMultipleUiModel
import com.tokopedia.sellerpersona.view.model.QuestionOptionSingleUiModel

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

class OptionAdapterFactoryImpl : BaseAdapterTypeFactory(), OptionAdapterFactory {

    override fun type(model: QuestionOptionSingleUiModel): Int {
        return OptionSingleViewHolder.RES_LAYOUT
    }

    override fun type(model: QuestionOptionMultipleUiModel): Int {
        return OptionMultipleViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OptionSingleViewHolder.RES_LAYOUT -> OptionSingleViewHolder(parent)
            OptionMultipleViewHolder.RES_LAYOUT -> OptionMultipleViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}