package com.tokopedia.otp.qrcode.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class LoginByQrViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_login_by_qr

    var containerView: View? = null
    var toolbar: Toolbar? = null
    var mainIcon: IconUnify? = null
    var userName: Typography? = null
    var approveButton: UnifyButton? = null
    var rejectButton: UnifyButton? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container_login_by_qr)
                toolbar = findViewById(R.id.toolbar_otp)
                mainIcon = findViewById(R.id.main_icon)
                userName = findViewById(R.id.user_name)
                approveButton = findViewById(R.id.approve_button)
                rejectButton = findViewById(R.id.reject_button)
            }
}