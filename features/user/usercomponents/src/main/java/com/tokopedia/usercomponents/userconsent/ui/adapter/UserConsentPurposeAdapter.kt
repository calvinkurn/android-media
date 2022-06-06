package com.tokopedia.usercomponents.userconsent.ui.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.usercomponents.userconsent.common.UserConsentPurposeUiModel

class UserConsentPurposeAdapter(
    listener: UserConsentPurposeViewHolder.UserConsentPurposeListener
): BaseAdapter<UserConsentPurposeUiModel>() {

    init {
        delegatesManager.addDelegate(UserConsentPurposeAdapterDelegate(listener))
    }
}