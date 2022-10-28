package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.tokopedianow.common.bottomsheet.TokoNowOnBoard20mBottomSheet

class OnBoard20mBottomSheetCallback (
    private val onBackTo2hClicked: () -> Unit,
    private val onDismiss: () -> Unit
): TokoNowOnBoard20mBottomSheet.OnBoard20mBottomSheetListener {
    override fun onBackTo2hClicked() {
        onBackTo2hClicked.invoke()
    }

    override fun onDismiss() {
        onDismiss.invoke()
    }
}