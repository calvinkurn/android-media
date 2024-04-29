package com.tokopedia.verification.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.header.HeaderUnify
import com.tokopedia.verification.R
import com.tokopedia.verification.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class SettingNotifViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_notif_setting

    var containerView: View? = null
    var mainImage: ImageUnify? = null
    var switch: SwitchUnify? = null
    var switchLayout: FrameLayout? = null
    var headerUnify: HeaderUnify? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                mainImage = findViewById(R.id.main_image)
                switch = findViewById(R.id.switch_button)
                switchLayout = findViewById(R.id.switch_layout)
                headerUnify = findViewById(R.id.toolbar_otp)
            }
}
