package com.tokopedia.otp.notif.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ReceiverNotifViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_notif_receiver

    var containerView: View? = null
    var btnYes: UnifyButton? = null
    var btnNo: UnifyButton? = null
    var textDevice: Typography? = null
    var textTime: Typography? = null
    var textLocation: Typography? = null
    var imagePhoneBell: ImageUnify? = null
    var imagePhone: ImageUnify? = null
    var imageTime: ImageUnify? = null
    var imageLocation: ImageUnify? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                btnYes = findViewById(R.id.btn_yes)
                btnNo = findViewById(R.id.btn_no)
                textDevice = findViewById(R.id.text_device)
                textTime = findViewById(R.id.text_time)
                textLocation = findViewById(R.id.text_location)
                imagePhoneBell = findViewById(R.id.image_phone_bell)
                imagePhone = findViewById(R.id.image_phone)
                imageTime = findViewById(R.id.image_time)
                imageLocation = findViewById(R.id.image_location)
                toolbar = findViewById(R.id.toolbar_otp)
            }
}