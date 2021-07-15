package com.tokopedia.universal_sharing.view.bottomsheet.listener

import com.tokopedia.universal_sharing.view.model.ShareModel

interface ShareBottomsheetListener {
    fun onItemBottomsheetShareClicked(shareModel: ShareModel)
    fun onCloseBottomSheet()
}