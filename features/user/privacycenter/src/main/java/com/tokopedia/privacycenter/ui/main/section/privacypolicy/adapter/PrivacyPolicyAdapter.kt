package com.tokopedia.privacycenter.ui.main.section.privacypolicy.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel

class PrivacyPolicyAdapter constructor(
    listener: Listener
) : BaseAdapter<PrivacyPolicyDataModel>() {

    interface Listener {
        val isFromBottomSheet: Boolean
        fun onItemClicked(item: PrivacyPolicyDataModel)
    }

    init {
        delegatesManager.addDelegate(PrivacyPolicyAdapterDelegate(listener))
    }
}
