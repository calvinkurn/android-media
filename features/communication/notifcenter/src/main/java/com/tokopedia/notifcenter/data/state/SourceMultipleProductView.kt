package com.tokopedia.notifcenter.data.state

sealed class SourceMultipleProductView {
    object NotificationCenter: SourceMultipleProductView()
    object BottomSheetDetail: SourceMultipleProductView()
}