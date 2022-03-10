package com.tokopedia.home_account.explicitprofile.features.categories.sections.chips

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel

class ChipsAdapterDelegate(
    private val chipsListener: ChipsViewHolder.QuestionChipsListener
) : TypedAdapterDelegate<QuestionDataModel, QuestionDataModel, ChipsViewHolder>(
    ChipsViewHolder.LAYOUT
) {
    override fun onBindViewHolder(item: QuestionDataModel, holderChips: ChipsViewHolder) {
        holderChips.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ChipsViewHolder {
        return ChipsViewHolder(basicView, chipsListener)
    }

}