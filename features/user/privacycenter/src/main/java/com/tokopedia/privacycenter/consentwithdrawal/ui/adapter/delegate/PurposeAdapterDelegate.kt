package com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.privacycenter.consentwithdrawal.ui.ConsentWithdrawalListener
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.uimodel.PurposeUiModel
import com.tokopedia.privacycenter.consentwithdrawal.ui.adapter.viewholder.PurposeViewHolder

class PurposeAdapterDelegate(
    private val listener: ConsentWithdrawalListener.Mandatory
) : TypedAdapterDelegate<PurposeUiModel, ConsentWithdrawalUiModel, PurposeViewHolder>(
    PurposeViewHolder.LAYOUT
) {

    override fun onBindViewHolder(
        item: PurposeUiModel,
        holder: PurposeViewHolder
    ) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PurposeViewHolder {
        return PurposeViewHolder(basicView, listener)
    }

}
