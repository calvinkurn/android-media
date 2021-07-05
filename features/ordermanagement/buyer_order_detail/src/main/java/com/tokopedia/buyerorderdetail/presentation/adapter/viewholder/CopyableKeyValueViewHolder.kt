package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.text.Spannable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.CopyableKeyValueUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifyprinciples.Typography

open class CopyableKeyValueViewHolder<T : CopyableKeyValueUiModel>(itemView: View?) : BaseToasterViewHolder<T>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_copyable_key_value
    }
    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val icBuyerOrderDetailCopy = itemView?.findViewById<IconUnify>(R.id.icBuyerOrderDetailCopy)
    private val tvBuyerOrderDetailCopyableValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCopyableValue)
    private val tvBuyerOrderDetailCopyableLabel = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCopyableLabel)

    protected var element: T? = null

    init {
        setupClickListener()
    }

    override fun bind(element: T?) {
        element?.let {
            this.element = it
            setupLabel(it.label)
            setupCopyIcon(it.copyLabel)
            setupTextToShow(it.copyableText)
        }
    }

    override fun bind(element: T?, payloads: MutableList<Any>) {
        container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        bind(element)
        container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setupClickListener() {
        icBuyerOrderDetailCopy?.setOnClickListener {
            copyText()
        }
    }

    private fun setupLabel(label: String) {
        tvBuyerOrderDetailCopyableLabel?.text = label
    }

    private fun setupCopyIcon(copyLabel: String) {
        icBuyerOrderDetailCopy?.apply {
            tag = copyLabel
            showWithCondition(copyLabel.isNotBlank())
        }
    }

    private fun setupTextToShow(text: Spannable) {
        tvBuyerOrderDetailCopyableValue?.text = text
    }

    protected open fun copyText() {
        element?.let {
            Utils.copyText(itemView.context, it.copyLabel, it.copyableText.toString())
            showToaster(it.copyMessage)
        }
    }
}