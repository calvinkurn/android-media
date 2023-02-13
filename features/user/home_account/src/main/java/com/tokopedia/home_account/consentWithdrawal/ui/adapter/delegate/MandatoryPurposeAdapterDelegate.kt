package com.tokopedia.home_account.consentWithdrawal.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.consentWithdrawal.ui.ConsentWithdrawalListener
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.MandatoryPurposeUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.viewholder.MandatoryPurposeViewHolder

class MandatoryPurposeAdapterDelegate(
    private val listener: ConsentWithdrawalListener.Mandatory
) : TypedAdapterDelegate<MandatoryPurposeUiModel, ConsentWithdrawalUiModel, MandatoryPurposeViewHolder>(
    MandatoryPurposeViewHolder.LAYOUT
) {

    override fun onBindViewHolder(
        item: MandatoryPurposeUiModel,
        holder: MandatoryPurposeViewHolder
    ) {
        holder.onBind(item.data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): MandatoryPurposeViewHolder {
        return MandatoryPurposeViewHolder(basicView, listener)
    }

}
