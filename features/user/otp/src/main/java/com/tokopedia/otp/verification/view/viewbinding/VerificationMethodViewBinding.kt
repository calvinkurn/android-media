package com.tokopedia.otp.verification.view.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.otp.R
import com.tokopedia.otp.common.abstraction.BaseOtpViewBinding
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/04/20.
 * ade.hadian@tokopedia.com
 */

class VerificationMethodViewBinding @Inject constructor() : BaseOtpViewBinding() {

    override val layoutResId: Int = R.layout.fragment_verification_method

    var parentContainerView: View? = null
    var containerView: View? = null
    var title: Typography? = null
    var subtitle: Typography? = null
    var methodList: RecyclerView? = null
    var phoneInactive: Typography? = null
    var phoneInactiveTicker: Ticker? = null
    var loader: LoaderUnify? = null
    var toolbar: Toolbar? = null
    var ticker: Ticker? = null

    override fun inflate(layoutInflater: LayoutInflater, container: ViewGroup?): View =
            layoutInflater.inflate(layoutResId, container, false).apply {
                parentContainerView = findViewById(R.id.parent_container)
                containerView = findViewById(R.id.container)
                title = findViewById(R.id.title)
                subtitle = findViewById(R.id.subtitle)
                methodList = findViewById(R.id.method_list)
                phoneInactive = findViewById(R.id.phone_inactive)
                phoneInactiveTicker = findViewById(R.id.phone_inactive_ticker)
                loader = findViewById(R.id.loader)
                toolbar = findViewById(R.id.toolbar_otp)
                ticker = findViewById(R.id.tickerVerificationMethod)
            }
}