package com.tokopedia.otp.verification.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Ade Fulki on 22/04/20.
 * ade.hadian@tokopedia.com
 */

class VerificationMethodViewBinding : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_verification_method

    var containerView: View? = null
    var title: Typography? = null
    var subtitle: Typography? = null
    var methodList: RecyclerView? = null
    var phoneInactive: Typography? = null
    var loader: LoaderUnify? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                containerView = findViewById(R.id.container)
                title = findViewById(R.id.title)
                subtitle = findViewById(R.id.subtitle)
                methodList = findViewById(R.id.method_list)
                phoneInactive = findViewById(R.id.phone_inactive)
                loader = findViewById(R.id.loader)
            }
}