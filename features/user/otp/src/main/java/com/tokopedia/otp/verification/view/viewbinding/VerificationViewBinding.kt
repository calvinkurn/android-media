package com.tokopedia.otp.verification.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.pin.PinUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Ade Fulki on 21/04/20.
 * ade.hadian@tokopedia.com
 */

class VerificationViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_verification

    var containerView: View? = null
    var methodIcon: ImageUnify? = null
    var prefixTextMethodIcon: Typography? = null
    var pin: PinUnify? = null
    var loader: LoaderUnify? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                methodIcon = findViewById(R.id.method_icon)
                prefixTextMethodIcon = findViewById(R.id.prefix_text_method_icon)
                pin = findViewById(R.id.pin)
                loader = findViewById(R.id.loader)
                toolbar = findViewById(R.id.toolbar_otp)
            }
}