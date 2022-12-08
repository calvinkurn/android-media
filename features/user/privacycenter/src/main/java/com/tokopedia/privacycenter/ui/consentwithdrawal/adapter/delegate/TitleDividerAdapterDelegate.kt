package com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.TitleDividerUiModel
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.viewholder.TitleDividerViewHolder

class TitleDividerAdapterDelegate : TypedAdapterDelegate<TitleDividerUiModel, ConsentWithdrawalUiModel, TitleDividerViewHolder>(
    TitleDividerViewHolder.LAYOUT
) {

    override fun onBindViewHolder(
        item: TitleDividerUiModel,
        holder: TitleDividerViewHolder
    ) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): TitleDividerViewHolder {
        return TitleDividerViewHolder(basicView)
    }
}
