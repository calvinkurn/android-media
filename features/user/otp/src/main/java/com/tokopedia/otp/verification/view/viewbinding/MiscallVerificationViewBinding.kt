package com.tokopedia.otp.verification.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.otp.R
import com.tokopedia.pin.PinUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by Ade Fulki on 22/04/20.
 * ade.hadian@tokopedia.com
 */

class MiscallVerificationViewBinding : BaseVerificationViewBinding() {

    override val layoutResId: Int = R.layout.fragment_miscall_verification

    var containerView: View? = null
    var imgMiscall: ImageUnify? = null
    var pin: PinUnify? = null
    var loader: LoaderUnify? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                imgMiscall = findViewById(R.id.img_miscall_verification)
                pin = findViewById(R.id.pin)
                loader = findViewById(R.id.loader)
            }
}