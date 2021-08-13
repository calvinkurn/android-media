package com.tokopedia.otp.qrcode.view.viewbinding

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

class LoginByQrResultViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_login_by_qr_result

    var containerView: View? = null
    var mainImage: ImageUnify? = null
    var title: Typography? = null
    var subtitle: Typography? = null
    var btnMain: UnifyButton? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container_login_by_qr)
                mainImage = findViewById(R.id.main_image)
                title = findViewById(R.id.text_title)
                subtitle = findViewById(R.id.text_subtitle)
                btnMain = findViewById(R.id.button_main)
                toolbar = findViewById(R.id.toolbar_otp)
            }

}