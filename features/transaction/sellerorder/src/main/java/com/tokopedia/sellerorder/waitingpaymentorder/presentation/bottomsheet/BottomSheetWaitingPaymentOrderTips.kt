package com.tokopedia.sellerorder.waitingpaymentorder.presentation.bottomsheet

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.R
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
            addItemDecoration(ItemDivider(context))
        }
    }

    class ItemDivider(context: Context) : RecyclerView.ItemDecoration() {

        private val divider = MethodChecker.getDrawable(context, R.drawable.waiting_payment_tips_divider)
        private val margin = context.resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl1).toInt()

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val isLastItem: Boolean = parent.getChildAdapterPosition(view) == parent.adapter?.itemCount.orZero() - 1
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            layoutParams.topMargin = margin
            if (!isLastItem) {
                layoutParams.bottomMargin = margin
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i)
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + layoutParams.bottomMargin
                val bottom = top + divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }
}