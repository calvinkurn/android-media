package com.tokopedia.sellerorder.waitingpaymentorder.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.RecyclerViewItemDivider
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderTipsTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.provider.WaitingPaymentOrderTipsDataProvider
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class BottomSheetWaitingPaymentOrderTips : BottomSheetUnify() {
    private val adapter by lazy {
        BaseAdapter(WaitingPaymentOrderTipsTypeFactory())
    }

    private var tvHeader: Typography? = null
    private var rvTips: RecyclerView? = null
    private val headerTitle by lazy {
        context?.let { context ->
            HtmlLinkHelper(context, getString(R.string.bottomsheet_waiting_payment_order_header))
        }
    }

    private val items by lazy {
        context?.run {
            WaitingPaymentOrderTipsDataProvider().provideData(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildLayout()
        setupTitle()
        val views = super.onCreateView(inflater, container, savedInstanceState)
        setupHeader()
        setupRecyclerView()
        return views
    }

    override fun onResume() {
        super.onResume()
        updateShopActive()
    }

    private fun setChildLayout() {
        val views = View.inflate(context, R.layout.bottomsheet_waiting_payment_order_tips, null)
        tvHeader = views.findViewById(R.id.tv_waiting_payment_order_header_tips)
        rvTips = views.findViewById(R.id.rv_waiting_payment_order_tips)
        setChild(views)
    }

    private fun setupHeader() {
        tvHeader?.text = headerTitle?.spannedString
        tvHeader?.setOnClickListener {
            val link = headerTitle?.urlList?.firstOrNull()?.linkUrl.orEmpty()
            if (link.isNotEmpty()) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, link)
                context?.startActivity(intent)
            }
        }
    }

    private fun setupTitle() {
        setTitle(getString(R.string.bottomsheet_waiting_payment_order_tips_title))
    }

    private fun setupRecyclerView() {
        adapter.setElements(items)
        rvTips?.apply {
            adapter = this@BottomSheetWaitingPaymentOrderTips.adapter
            val margins = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.spacing_lvl3).toInt()
            addItemDecoration(
                RecyclerViewItemDivider(
                    divider = MethodChecker.getDrawable(context, R.drawable.waiting_payment_tips_divider),
                    topMargin = margins,
                    bottomMargin = margins
                )
            )
        }
    }
}