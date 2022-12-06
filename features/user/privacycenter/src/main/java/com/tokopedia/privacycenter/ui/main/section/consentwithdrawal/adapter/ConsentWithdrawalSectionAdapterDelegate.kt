package com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.ConsentGroupDataModel

class ConsentWithdrawalSectionAdapterDelegate(
    private val listener: ConsentWithdrawalSectionAdapter.Listener
) : TypedAdapterDelegate<ConsentGroupDataModel, ConsentGroupDataModel, ConsentWithdrawalSectionViewHolder>(
    R.layout.base_item_privacy_center
) {

    override fun onBindViewHolder(
        item: ConsentGroupDataModel,
        holder: ConsentWithdrawalSectionViewHolder
    ) {
        holder.onBind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): ConsentWithdrawalSectionViewHolder {
        return ConsentWithdrawalSectionViewHolder(basicView, listener)
    }
}
