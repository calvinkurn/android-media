package com.tokopedia.verification.otp.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.verification.R
import com.tokopedia.verification.common.abstraction.BaseOtpViewBinding
import com.tokopedia.pin.PinUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import javax.inject.Inject

/**
 * Created by Ade Fulki on 21/04/20.
 * ade.hadian@tokopedia.com
 */

open class VerificationViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_verification

    var containerView: View? = null
    var methodIcon: ImageUnify? = null
    var lottieMiscallAnimation: LottieAnimationView? = null
    var pin: PinUnify? = null
    var loader: LoaderUnify? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                methodIcon = findViewById(R.id.method_icon)
                lottieMiscallAnimation = findViewById(R.id.lottieMiscallAnimation)
                pin = findViewById(R.id.pin)
                loader = findViewById(R.id.loader)
                toolbar = findViewById(R.id.toolbar_otp)
            }
}
