package com.tokopedia.settingnotif.usersetting.view.fragment.dialog

import androidx.fragment.app.Fragment
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.util.inflateView
import com.tokopedia.unifycomponents.BottomSheetUnify

object InformationDialog {

    fun Fragment.showInformationDialog(screenName: String) {
        val layoutRes = R.layout.dialog_push_notif_information
        val customDialogView = context.inflateView(layoutRes)
        val informationSheet = BottomSheetUnify().apply {
            setTitle(screenName)
            setChild(customDialogView)
            setCloseClickListener { dismiss() }
        }
        informationSheet.show(childFragmentManager, screenName)
    }

}