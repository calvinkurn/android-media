package com.tokopedia.home_account.explicitprofile.features.popupinfo

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel

class SectionInfoAdapterDelegate : TypedAdapterDelegate<QuestionDataModel, QuestionDataModel, SectionInfoViewHolder>(SectionInfoViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: QuestionDataModel, holder: SectionInfoViewHolder) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SectionInfoViewHolder {
        return SectionInfoViewHolder(basicView)
    }
}