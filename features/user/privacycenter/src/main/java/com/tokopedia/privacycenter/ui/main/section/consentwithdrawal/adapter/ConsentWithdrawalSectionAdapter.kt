package com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.privacycenter.data.ConsentGroupDataModel

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
