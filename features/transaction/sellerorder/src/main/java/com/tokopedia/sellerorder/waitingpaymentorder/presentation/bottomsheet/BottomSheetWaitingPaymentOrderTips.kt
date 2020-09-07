package com.tokopedia.sellerorder.waitingpaymentorder.presentation.bottomsheet

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderTipsTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.provider.WaitingPaymentOrderTipsDataProvider
import com.tokopedia.unifycomponents.BottomSheetUnify
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
        context?.let { context ->
            val headerText = getString(R.string.bottomsheet_waiting_payment_order_header)
            val ctaText = getString(R.string.bottomsheet_waiting_payment_order_header_cta)
            val spannedHeader = SpannableStringBuilder()
                    .append(headerText)
                    .append(" $ctaText")
            spannedHeader.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)),
                    headerText.length + 1,
                    headerText.length + ctaText.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannedHeader.setSpan(
                    StyleSpan(android.graphics.Typeface.BOLD),
                    headerText.length + 1,
                    headerText.length + ctaText.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvHeader?.text = spannedHeader
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

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i)
                val top = child.bottom
                val bottom = top + divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }
}