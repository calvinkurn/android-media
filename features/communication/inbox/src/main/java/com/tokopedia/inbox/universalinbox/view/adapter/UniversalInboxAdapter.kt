package com.tokopedia.inbox.universalinbox.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuItemDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuSectionDelegate

class UniversalInboxAdapter: BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(UniversalInboxMenuSectionDelegate())
        delegatesManager.addDelegate(UniversalInboxMenuItemDelegate())
    }
}
