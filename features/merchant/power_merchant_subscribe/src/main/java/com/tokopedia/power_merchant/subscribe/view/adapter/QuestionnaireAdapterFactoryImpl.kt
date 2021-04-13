package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.QuestionnaireMultipleViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.QuestionnaireRatingViewHolder
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class QuestionnaireAdapterFactoryImpl : BaseAdapterTypeFactory(), QuestionnaireAdapterFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            QuestionnaireRatingViewHolder.RES_LAYOUT -> QuestionnaireRatingViewHolder(parent)
            QuestionnaireMultipleViewHolder.RES_LAYOUT -> QuestionnaireMultipleViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(model: QuestionnaireUiModel.QuestionnaireRatingUiModel): Int = QuestionnaireRatingViewHolder.RES_LAYOUT

    override fun type(model: QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel): Int = QuestionnaireMultipleViewHolder.RES_LAYOUT
}