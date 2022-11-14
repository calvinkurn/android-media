package com.tokopedia.privacycenter.main.section.consentwithdrawal

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupDataModel

class ConsentWithdrawalSectionAdapter constructor(
    listener: Listener
) : BaseAdapter<ConsentGroupDataModel>() {

    interface Listener {
        fun onItemClicked(data: ConsentGroupDataModel)
    }

    init {
        delegatesManager.addDelegate(ConsentWithdrawalSectionAdapterDelegate(listener))
    }
}
