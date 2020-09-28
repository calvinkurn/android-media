package com.tokopedia.otp.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class SettingNotifViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_notif_setting

    var mainImage: ImageUnify? = null
    var switch: SwitchUnify? = null
    var switchLayout: FrameLayout? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                mainImage = findViewById(R.id.main_image)
                switch = findViewById(R.id.switch_button)
                switchLayout = findViewById(R.id.switch_layout)
            }
}