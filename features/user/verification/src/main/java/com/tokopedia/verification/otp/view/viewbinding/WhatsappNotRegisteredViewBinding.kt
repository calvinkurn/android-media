package com.tokopedia.verification.otp.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.verification.R
import com.tokopedia.verification.common.abstraction.BaseOtpViewBinding
import javax.inject.Inject

class WhatsappNotRegisteredViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_whatsapp_not_registered

    var emptyState: EmptyStateUnify? = null
    var toolbar: Toolbar? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                emptyState = findViewById(R.id.empty_state_wa)
                toolbar = findViewById(R.id.toolbar_otp)
            }
}
