package com.tokopedia.notifcenter.presentation.fragment.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

object BottomSheetFactory {
    fun showLongerContent(manager: FragmentManager, notification: NotificationUiModel) {
        NotificationLongerContentBottomSheet
                .create(notification)
                .show(manager, NotificationLongerContentBottomSheet::class.java.simpleName)
    }

    fun showProductBottomSheet(manager: FragmentManager, notification: NotificationUiModel) {
        NotificationProductLongerContentBottomSheet
                .create(notification)
                .show(manager, NotificationProductLongerContentBottomSheet::class.java.simpleName)
    }
}