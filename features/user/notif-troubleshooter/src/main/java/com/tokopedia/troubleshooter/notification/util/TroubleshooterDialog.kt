package com.tokopedia.troubleshooter.notification.util

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tokopedia.settingnotif.usersetting.util.inflateView
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify

object TroubleshooterDialog {
    private const val URL_IMG_NOTIF_SAMPLE = "https://images.tokopedia.net/img/android/user/notif-troubleshooter/ic_ts_notif_sample.png"

    fun Fragment.showInformationDialog(screenName: String) {
        val layoutRes = R.layout.dialog_troubleshooter_info
        val customDialogView = context.inflateView(layoutRes)
        val ivNotifSample = customDialogView.findViewById<ImageView>(R.id.iv_notif_sample)
        ivNotifSample.loadImage(URL_IMG_NOTIF_SAMPLE)
        val informationSheet = BottomSheetUnify().apply {
            setTitle(screenName)
            setChild(customDialogView)
            setCloseClickListener { dismiss() }
        }
        informationSheet.show(childFragmentManager, screenName)
    }
}