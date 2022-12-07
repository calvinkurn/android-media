package com.tokopedia.privacycenter.ui.main.section.privacypolicy.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel

class PrivacyPolicyAdapterDelegate(
    private val listener: PrivacyPolicyAdapter.Listener
) : TypedAdapterDelegate<PrivacyPolicyDataModel, PrivacyPolicyDataModel, PrivacyPolicyViewHolder>(
    R.layout.privacy_policy_item_view
) {
    override fun onBindViewHolder(item: PrivacyPolicyDataModel, holder: PrivacyPolicyViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PrivacyPolicyViewHolder {
        return PrivacyPolicyViewHolder(basicView, listener)
    }
}
