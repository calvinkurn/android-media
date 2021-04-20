package com.tokopedia.troubleshooter.notification.util

import androidx.fragment.app.Fragment
import com.tokopedia.settingnotif.usersetting.util.inflateView
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.unifycomponents.BottomSheetUnify

object TroubleshooterDialog {

    fun Fragment.showInformationDialog(screenName: String) {
        val layoutRes = R.layout.dialog_troubleshooter_info
        val customDialogView = context.inflateView(layoutRes)
        val informationSheet = BottomSheetUnify().apply {
            setTitle(screenName)
            setChild(customDialogView)
            setCloseClickListener { dismiss() }
        }
        informationSheet.show(childFragmentManager, screenName)
    }

}