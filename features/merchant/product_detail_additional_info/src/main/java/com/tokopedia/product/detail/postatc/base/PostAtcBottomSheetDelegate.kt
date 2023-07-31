package com.tokopedia.product.detail.postatc.base

import android.content.Context
import android.content.Intent
import com.tokopedia.product.detail.databinding.PostAtcBottomSheetBinding
import com.tokopedia.product.detail.databinding.ViewPostAtcFooterBinding
import com.tokopedia.product.detail.postatc.view.PostAtcViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

interface PostAtcBottomSheetDelegate {
    val adapter: PostAtcAdapter
    val binding: PostAtcBottomSheetBinding?
    val footer: ViewPostAtcFooterBinding?
    val userSession: UserSessionInterface
    val trackingQueue: TrackingQueue
    val viewModel: PostAtcViewModel
    fun dismiss()
    fun dismissAllowingStateLoss()
    fun initData()
    fun getContext(): Context?
    fun startActivity(intent: Intent)
}
