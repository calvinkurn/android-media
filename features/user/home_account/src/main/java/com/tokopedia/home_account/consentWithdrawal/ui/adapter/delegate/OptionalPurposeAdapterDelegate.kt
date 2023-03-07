package com.tokopedia.home_account.consentWithdrawal.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.home_account.consentWithdrawal.ui.ConsentWithdrawalListener
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.ConsentWithdrawalUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.OptionalPurposeUiModel
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.viewholder.OptionalPurposeViewHolder

class OptionalPurposeAdapterDelegate(
    private val listener: ConsentWithdrawalListener.Optional
) : TypedAdapterDelegate<OptionalPurposeUiModel, ConsentWithdrawalUiModel, OptionalPurposeViewHolder>(
    OptionalPurposeViewHolder.LAYOUT
) {

    override fun onBindViewHolder(
        item: OptionalPurposeUiModel,
        holder: OptionalPurposeViewHolder
    ) {
        holder.onBind(item.data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): OptionalPurposeViewHolder {
        return OptionalPurposeViewHolder(basicView, listener)
    }

}
