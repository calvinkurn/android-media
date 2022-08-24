package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR
import com.tokopedia.buyerorder.databinding.VoucherItemCardDealsBinding
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDealsOMP
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.buyerorder.detail.view.customview.BookingCodeView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifyprinciples.Typography
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * created by @bayazidnasir on 23/8/2022
 */

class DealsOMPViewHolder(
    itemView: View,
    private val eventDetailsListener: EventDetailsListener
) : AbstractViewHolder<ItemsDealsOMP>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_card_deals

        private const val KEY_VOUCHER_CODE = "vouchercodes"
        private const val KEY_REDIRECT = "redirect"
        private const val KEY_REDIRECT_EXTERNAL = "redirectexternal"
        private const val KEY_POPUP = "popup"
        private const val STROKE_WIDTH = 1
        private const val ZERO_MARGIN = 0
    }

    override fun bind(element: ItemsDealsOMP) {
        val binding = VoucherItemCardDealsBinding.bind(itemView)
        val metadata = Gson().fromJson(element.item.metaData, MetaDataInfo::class.java)

        renderProducts(binding, metadata, element.item)
        setActionButton(binding, element.item, element.orderDetails)
    }

    private fun renderProducts(
        binding: VoucherItemCardDealsBinding,
        metadata: MetaDataInfo,
        item: Items,
    ){
        binding.ivDeal.loadImageCircle(metadata.productImage.ifEmpty { item.imageUrl })
        binding.tvDealIntro.text = metadata.name.ifEmpty { item.title }
        binding.tvBrandName.text = metadata.productName

        eventDetailsListener.sendOpenScreenDeals(true)

        binding.llValid.shouldShowWithAction(metadata.endTime.isNotEmpty()){
            binding.tvValidTillDate.text = getTimeMillis(metadata.endDate)
        }

        eventDetailsListener.setDealsBanner(item)
        eventDetailsListener.setDetailTitle(getString(R.string.detail_label))

        binding.customView1.setOnClickListener {
            RouteManager.route(itemView.context, metadata.productAppUrl)
        }
    }

    private fun setActionButton(
        binding: VoucherItemCardDealsBinding,
        item: Items,
        orderDetails: OrderDetails,
    ){
        if (item.actionButtons.isEmpty()){
            with(binding){
                progBar.gone()
                customView2.gone()
                tapActionDeals.gone()
            }
            return
        }

        binding.voucerCodeLayout.visible()
        item.actionButtons.forEachIndexed { index, actionButton ->
            when (actionButton.control) {
                KEY_VOUCHER_CODE -> {
                    val codes = actionButton.body.body.split(",")

                    if (codes.isEmpty()){
                        return@forEachIndexed
                    }

                    codes.forEach { code ->
                        val bookingView = BookingCodeView(itemView.context, code, adapterPosition, actionButton.label, 0).apply {
                            background = null
                        }
                        binding.voucerCodeLayout.addView(bookingView)

                    }
                }
                KEY_REDIRECT, KEY_REDIRECT_EXTERNAL -> {
                    //TODO : set redeemVoucherView
                    //TODO : addview
                }
                KEY_POPUP -> {
                    val actionTextButton = renderActionButtons(index, actionButton, item)
                    binding.voucerCodeLayout.addView(actionTextButton)
                    actionTextButton.setOnClickListener {
                        eventDetailsListener.openQRFragment(actionButton, item)
                    }
                }
            }
        }

        if (orderDetails.actionButtons.isNotEmpty()){
            eventDetailsListener.setActionButtonEvent(orderDetails.actionButtons.first(), item, orderDetails)
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
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }

            if (position == item.actionButtons.size - 1 &&  item.actionButtons.isEmpty()){
                val radius = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
                shape.cornerRadii = floatArrayOf(0F, 0F, 0F, 0F, radius, radius, radius, radius)
            } else {
                shape.cornerRadius = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4)
            }

            background = shape
        }
    }

    private fun getDimension(@DimenRes dimenId: Int): Int{
        return itemView.context.resources.getDimension(dimenId).toIntSafely()
    }

    private fun getTimeMillis(date: String): String{
        return try {
            val dateFormat = SimpleDateFormat(" dd MMM yyyy hh:mm", Locale.ROOT)
            val dateMillis = Date(TimeUnit.SECONDS.toMillis(date.toLongOrZero()))
            dateFormat.format(dateMillis)
        } catch (e: Exception) {
            date
        }
    }
}