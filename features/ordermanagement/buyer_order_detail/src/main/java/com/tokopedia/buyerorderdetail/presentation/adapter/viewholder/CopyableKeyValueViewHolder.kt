package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.BaseCopyableKeyValueUiModel
import com.tokopedia.unifyprinciples.Typography

open class CopyableKeyValueViewHolder<T : BaseCopyableKeyValueUiModel>(itemView: View?) : BaseToasterViewHolder<T>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_copyable_key_value
    }
    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val maskTriggerCopyArea = itemView?.findViewById<View>(R.id.maskTriggerCopyArea)
    private val tvBuyerOrderDetailCopyableValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCopyableValue)
    private val tvBuyerOrderDetailCopyableLabel = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailCopyableLabel)

    protected var element: T? = null

    init {
        setupClickListener()
    }

    override fun bind(element: T?) {
        element?.let {
            this.element = it
            setupLabel(it.label.getString(itemView.context))
            setupTriggerCopyArea(it.copyLabel.getString(itemView.context))
            setupTextToShow(it.copyableText)
        }
    }

    override fun bind(element: T?, payloads: MutableList<Any>) {
        container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        bind(element)
        container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setupClickListener() {
        maskTriggerCopyArea?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                it?.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            } else {
                it?.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            copyText()
        }
    }

    private fun setupLabel(label: String) {
        tvBuyerOrderDetailCopyableLabel?.text = label
    }

    private fun setupTriggerCopyArea(copyLabel: String) {
        maskTriggerCopyArea?.tag = copyLabel
    }

    private fun setupTextToShow(text: String) {
        tvBuyerOrderDetailCopyableValue?.let {
            it.text = MethodChecker.fromHtmlWithoutExtraSpace(text)
        }
    }

    protected open fun copyText() {
        element?.let {
            Utils.copyText(
                itemView.context,
                it.copyLabel.getString(itemView.context),
                MethodChecker.fromHtmlWithoutExtraSpace(it.copyableText)
            )
            showToaster(it.copyMessage.getString(itemView.context))
        }
    }
}
