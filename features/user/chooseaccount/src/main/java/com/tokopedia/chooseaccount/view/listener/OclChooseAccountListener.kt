package com.tokopedia.chooseaccount.view.listener

import com.tokopedia.chooseaccount.data.OclAccount

interface OclChooseAccountListener {
    fun onAccountSelected(account: OclAccount)
    fun onDeleteButtonClicked(account: OclAccount)
}
