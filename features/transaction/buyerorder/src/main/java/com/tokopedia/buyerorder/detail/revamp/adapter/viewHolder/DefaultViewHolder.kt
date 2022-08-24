package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR
import com.tokopedia.buyerorder.common.util.BuyerUtils.clickActionButton
import com.tokopedia.buyerorder.databinding.VoucherItemDefaultBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDefault
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography

/**
 * created by @bayazidnasir on 23/8/2022
 */

class DefaultViewHolder(
    itemView: View,
    private val eventDetailsListener: EventDetailsListener
) : AbstractViewHolder<ItemsDefault>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_default

        private const val ZERO_QUANTITY = 0
        private const val ZERO_MARGIN = 0
        private const val STROKE_WIDTH = 1
        private const val KEY_BUTTON = "button"
        private const val KEY_REDIRECT = "redirect"
        private const val KEY_TEXT = "text"
        private const val ITEMS_DEALS = 1
    }

    private var hasViews = false

    override fun bind(element: ItemsDefault) {
        val binding = VoucherItemDefaultBinding.bind(itemView)
        val metadata = Gson().fromJson(element.item.metaData, MetaDataInfo::class.java)

        eventDetailsListener.sendThankYouEvent(metadata, ITEMS_DEALS, element.orderDetails)
        eventDetailsListener.sendOpenScreenDeals(false)
        eventDetailsListener.setDetailTitle(getString(R.string.purchase_detail))

        renderProductName(binding, metadata)
        renderEntityPackage(binding, metadata)
        renderQuantity(binding, metadata, element.item)
        renderDate(binding, metadata)
        requestTapActions(binding, element.item)
        setDivider(binding)
        setTapActions(binding, element.item)
        setButtonActions(binding, element.item)
    }

    private fun renderProductName(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
    ){
        if (metadata.entityProductName.isNotEmpty()) {
            hasViews = true
            binding.rightEvent.text = metadata.entityProductName
        } else {
            binding.llEvent.gone()
        }
    }

    private fun renderEntityPackage(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
    ){
        if (metadata.entityPackages.isNotEmpty()) {
            if (metadata.entityPackages.first().displayName.isEmpty()) {
                binding.llCategoryTicket.gone()
            } else {
                binding.rightCategoryTicket.text = metadata.entityPackages.first().displayName
                hasViews = true
            }
        } else {
            binding.llCategoryTicket.gone()
        }
    }

    private fun renderQuantity(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        if (item.quantity == ZERO_QUANTITY) {
            binding.llTotalTicket.gone()
        } else {
            hasViews = true
            binding.rightTotalTicket.text = metadata.totalTicketCount.toString()
        }
    }

    private fun renderDate(
        binding: VoucherItemDefaultBinding,
        metadata: MetaDataInfo,
    ){
        binding.llValid.shouldShowWithAction(metadata.endDate.isNotEmpty()){
            binding.tvValidTillDate.text = getString(R.string.order_detail_date_whitespace, metadata.endDate)
            hasViews = true
        }

        if (metadata.startDate.isNotEmpty()){
            hasViews = true
            binding.tvStartDate.text = getString(R.string.order_detail_date_whitespace, metadata.startDate)
            binding.llTanggalEvent.visible()
        } else {
            binding.llTanggalEvent.gone()
        }
    }

    private fun requestTapActions(
        binding: VoucherItemDefaultBinding,
        item: Items,
    ){
        if (item.tapActions.isNotEmpty() && !item.isTapActionsLoaded){
            binding.progBar.visible()
            binding.tapAction.gone()
            eventDetailsListener.setActionButtonGql(item.tapActions, adapterPosition, true)
        }
    }

    private fun setDivider(binding: VoucherItemDefaultBinding){
        if (!hasViews) {
            binding.customView1.gone()
            binding.divider1.gone()
        } else {
            binding.customView1.visible()
            binding.divider1.visible()
        }
    }

    private fun setTapActions(
        binding: VoucherItemDefaultBinding,
        item: Items,
    ){
        if (item.isTapActionsLoaded) {
            binding.progBar.gone()
            if (item.tapActions.size == ZERO_QUANTITY) {
                binding.tapAction.gone()
            } else {
                binding.tapAction.visible()
                binding.tapAction.removeAllViews()
                item.tapActions.forEachIndexed { i, actionButton ->
                    val actionTextView = renderActionButtons(i, actionButton, item)
                    if (actionButton.control.equals(KEY_BUTTON, true)){
                        eventDetailsListener.setActionButtonGql(item.actionButtons, adapterPosition, true)
                    } else {
                        setActionButtonClick(actionTextView, actionButton)
                    }
                    binding.tapAction.addView(actionTextView)
                }
            }
        } else if (item.tapActions.isEmpty()){
            binding.progBar.gone()
        }
    }

    private fun setButtonActions(
        binding: VoucherItemDefaultBinding,
        item: Items,
    ){
        if (item.actionButtons.isEmpty()) {
            binding.actionButton.gone()
        } else {
            binding.actionButton.visible()
            binding.actionButton.removeAllViews()

            item.actionButtons.forEachIndexed { index, actionButton ->
                val actionTextView = renderActionButtons(index, actionButton, item)

                if (actionButton.control.equals(KEY_TEXT, true)){
                    return@forEachIndexed
                }

                if (item.isActionButtonLoaded) {
                    setActionButtonClick(null, actionButton)
                } else {
                    actionTextView.setOnClickListener {
                        if (actionButton.control.equals(KEY_BUTTON, true)){
                            eventDetailsListener.setActionButtonGql(item.actionButtons, adapterPosition, true)
                        } else {
                            setActionButtonClick(actionTextView, actionButton)
                        }
                    }
                }

                binding.actionButton.addView(actionTextView)
            }
        }
    }

    private fun renderActionButtons(
        position: Int,
        actionButton: ActionButton,
        item: Items,
    ) : Typography {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(
                ZERO_MARGIN,
                getDimension(com.tokopedia.resources.common.R.dimen.dp_8),
                ZERO_MARGIN,
                ZERO_MARGIN,
            )
        }

        return Typography(itemView.context).apply {
            setPadding(
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
                getDimension(unifyPrinciplesR.dimen.unify_space_16),
            )
            setTextColor(MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_N0))
            layoutParams = params
            gravity = Gravity.CENTER_HORIZONTAL
            text = actionButton.label

            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE

            if (actionButton.actionColor.background.isNotEmpty()) {
                shape.setColor(Color.parseColor(actionButton.actionColor.background))
            } else {
                shape.setColor(MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_G400))
            }

            if (actionButton.actionColor.border.isNotEmpty()) {
                shape.setStroke(
                    STROKE_WIDTH,
                    Color.parseColor(actionButton.actionColor.border)
                )
            }

            if (actionButton.actionColor.textColor.isNotEmpty()) {
                setTextColor(Color.parseColor(actionButton.actionColor.textColor))
            } else {
                setTextColor(MethodChecker.getColor(context, unifyPrinciplesR.color.Unify_N0))
            }

            if (position == item.actionButtons.size - 1 &&  item.actionButtons.isEmpty()){
                val radius = context.resources.getDimension(unifyPrinciplesR.dimen.unify_space_4)
                shape.cornerRadii = floatArrayOf(0F, 0F, 0F, 0F, radius, radius, radius, radius)
            } else {
                shape.cornerRadius = context.resources.getDimension(unifyPrinciplesR.dimen.unify_space_4)
            }

            background = shape
        }
    }

    private fun setActionButtonClick(
        textView: TextView?,
        actionButton: ActionButton,
    ) {
        if (!actionButton.control.equals(KEY_REDIRECT, true)){
            return
        }

        if (actionButton.body.toString().isEmpty() && actionButton.body.appURL.isEmpty()){
            return
        }

        if (textView == null) {
            RouteManager.route(itemView.context, actionButton.body.appURL)
        } else {
            textView.setOnClickListener {
                clickActionButton(
                    itemView.context,
                    actionButton.body.appURL,
                    false
                ){ uri ->
                    eventDetailsListener.askPermission(uri, false, "")
                }
                //TODO : set uri pdp using @params uri
            }
        }
    }

    private fun getDimension(@DimenRes dimenId: Int): Int{
        return itemView.context.resources.getDimension(dimenId).toIntSafely()
    }

}