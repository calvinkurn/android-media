package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.order.DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID
import com.tokopedia.applink.order.DeeplinkMapperOrder.Pof.INTENT_PARAM_POF_STATUS
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.domain.model.TickerInfo.Companion.CTA_ACTION_VALUE_POF
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_USER_MODE
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_AUTO_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_REJECTED
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.generateHapticFeedback
import com.tokopedia.sellerorder.common.util.Utils.toStringFormatted
import com.tokopedia.sellerorder.databinding.DetailHeaderItemBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailLabelAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailHeaderViewHolder(
    itemView: View?,
    private val actionListener: SomDetailAdapterFactoryImpl.ActionListener?
) : AbstractViewHolder<SomDetailData>(itemView) {

    private val adapter: SomDetailLabelAdapter = SomDetailLabelAdapter(emptyList())
    private val binding by viewBinding<DetailHeaderItemBinding>()

    override fun bind(item: SomDetailData) {
        if (item.dataObject is SomDetailHeader) {
            setupOrderStatus(item.dataObject.statusText, item.dataObject.statusCode)
            binding?.run {
                if (item.dataObject.isWarehouse) {
                    warehouseLabel.apply {
                        setLabel(getWarehouseLabelString(context))
                        show()
                        unlockFeature = true
                        setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950_68))
                    }
                } else {
                    warehouseLabel.hide()
                }

                if (item.dataObject.statusIndicatorColor.isNotBlank()) {
                    somOrderDetailIndicator.background = Utils.getColoredIndicator(root.context, item.dataObject.statusIndicatorColor)
                }

                headerSeeHistory.setOnClickListener {
                    binding?.root?.context?.startActivity(
                        RouteManager.getIntent(it.context, ApplinkConstInternalOrder.TRACK, "")
                            .putExtra(EXTRA_ORDER_ID, item.dataObject.orderId)
                            .putExtra(EXTRA_USER_MODE, 2)
                    )
                }

                if (item.dataObject.tickerInfo.text.isNotEmpty() || item.dataObject.awbUploadProofText.isNotEmpty()) {
                    // If ticker is POF ticker
                    if (item.dataObject.tickerInfo.ctaActionValue == CTA_ACTION_VALUE_POF) {
                        setupPofTicker(tickerDetailBuyerRequestCancel, item.dataObject.tickerInfo, item.dataObject.orderId, item.dataObject.pofStatus)
                    } else {
                        val tickerContent = item.dataObject.tickerInfo.text.ifBlank {
                            item.dataObject.awbUploadProofText
                        }
                        val isAwb = item.dataObject.tickerInfo.text.isEmpty()
                        val tickerUrl = item.dataObject.tickerInfo.actionUrl.ifBlank {
                            item.dataObject.awbUploadUrl
                        }

                        setupTicker(tickerDetailBuyerRequestCancel, item.dataObject.tickerInfo, tickerContent, tickerUrl, isAwb)
                    }

                    tickerDetailBuyerRequestCancel.show()
                } else {
                    tickerDetailBuyerRequestCancel.gone()
                }

                headerBuyerValue.text = item.dataObject.custName.toStringFormatted(MAX_BUYER_NAME)
                headerDateValue.text = item.dataObject.paymentDate

                if (item.dataObject.deadlineText.isNotEmpty()) {
                    if (item.dataObject.statusCode == STATUS_CODE_ORDER_DELIVERED || item.dataObject.statusCode == STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT) {
                        headerDeadlineLabel.text = root.context.getString(R.string.som_deadline_done)
                    } else {
                        headerDeadlineLabel.text = root.context.getString(R.string.som_deadline)
                    }
                    tvSomDetailDeadline.text = item.dataObject.deadlineText
                    setupDeadlineStyleFromRollence(item.dataObject)
                    headerDeadlineLabel.show()
                    layoutSomDetailDeadline.show()
                } else {
                    headerDeadlineLabel.hide()
                    layoutSomDetailDeadline.hide()
                }

                headerInvoice.text = item.dataObject.invoice

                maskTriggerCopyArea.setOnClickListener {
                    it.generateHapticFeedback()
                    actionListener?.onCopiedInvoice(it.context.getString(R.string.invoice_label), item.dataObject.invoice)
                }

                headerSeeInvoice.setOnClickListener {
                    actionListener?.onSeeInvoice(item.dataObject.invoiceUrl, item.dataObject.invoice)
                }

                // labels
                rvSomDetailLabels.isNestedScrollingEnabled = false
                rvSomDetailLabels.layoutManager = FlexboxLayoutManager(root.context).apply {
                    alignItems = AlignItems.FLEX_START
                }
                rvSomDetailLabels.adapter = adapter
                adapter.setLabels(item.dataObject.listLabelOrder)
            }
        }
    }

    private fun DetailHeaderItemBinding.setupDeadlineStyleFromRollence(element: SomDetailHeader) {
        if (Utils.isEnableOperationalGuideline()) {
            setupDeadlineStyle(element.deadlineStyle)
        } else {
            val deadlineBackground = Utils.getColoredDeadlineBackground(
                context = root.context,
                colorHex = element.deadlineColor,
                defaultColor = unifyprinciplesR.color.Unify_YN600
            )
            layoutSomDetailDeadline.background = deadlineBackground
        }
    }

    private fun DetailHeaderItemBinding.setupDeadlineStyle(deadlineStyle: Int) {
        when (deadlineStyle) {
            SomConsts.DEADLINE_MORE_THAN_24_HOURS -> setDeadlineMoreThan24Hours()
            SomConsts.DEADLINE_BETWEEN_12_TO_24_HOURS -> setDeadlineBetween12To24Hours()
            SomConsts.DEADLINE_LOWER_THAN_12_HOURS -> setDeadlineLowerThan12Hours()
            else -> setDeadlineMoreThan24Hours()
        }
    }

    private fun DetailHeaderItemBinding.setDeadlineLowerThan12Hours() {
        val bgDeadline = Utils.getDeadlineDrawable(root.context, unifyprinciplesR.color.Unify_RN600)
        val colorDeadline = MethodChecker.getColor(root.context, unifyprinciplesR.color.Unify_NN0)
        layoutSomDetailDeadline.background = bgDeadline
        icSomDetailDeadline.setImage(newIconId = IconUnify.CLOCK, newLightEnable = colorDeadline)
        tvSomDetailDeadline.setTextColor(colorDeadline)
    }

    private fun DetailHeaderItemBinding.setDeadlineBetween12To24Hours() {
        val bgDeadline = Utils.getDeadlineDrawable(root.context, unifyprinciplesR.color.Unify_RN50)
        val colorDeadline = MethodChecker.getColor(root.context, unifyprinciplesR.color.Unify_RN600)
        layoutSomDetailDeadline.background = bgDeadline
        icSomDetailDeadline.setImage(newIconId = IconUnify.CLOCK, newLightEnable = colorDeadline)
        tvSomDetailDeadline.setTextColor(colorDeadline)
    }

    private fun DetailHeaderItemBinding.setDeadlineMoreThan24Hours() {
        val bgDeadline = Utils.getDeadlineDrawable(root.context, R.color._dms_som_operational_more_than_24_hour_color)
        val colorDeadline = MethodChecker.getColor(root.context, unifyprinciplesR.color.Unify_NN0)
        layoutSomDetailDeadline.background = bgDeadline
        icSomDetailDeadline.setImage(newIconId = IconUnify.CLOCK, newLightEnable = colorDeadline)
        tvSomDetailDeadline.setTextColor(colorDeadline)
    }

    private fun setupOrderStatus(statusText: String, statusCode: Int) {
        binding?.headerTitle?.run {
            text = statusText
            val statusOrderColor = if (statusCode == STATUS_CODE_ORDER_CANCELLED ||
                statusCode == STATUS_CODE_ORDER_AUTO_CANCELLED ||
                statusCode == STATUS_CODE_ORDER_REJECTED) {
                unifyprinciplesR.color.Unify_RN500
            } else {
                unifyprinciplesR.color.Unify_NN950_96
            }
            setTextColor(MethodChecker.getColor(context, statusOrderColor))
        }
    }

    private fun setupPofTicker(
        tickerBuyerRequestCancel: Ticker,
        tickerInfo: TickerInfo,
        orderId: String,
        pofStatus: Int
    ) {
        tickerBuyerRequestCancel.run {
            val tickerUrl = UriUtil.buildUriAppendParams(
                ApplinkConst.SELLER_PARTIAL_ORDER_FULFILLMENT,
                mapOf(
                    INTENT_PARAM_ORDER_ID to orderId,
                    INTENT_PARAM_POF_STATUS to pofStatus
                )
            )
            val tickerCta = context
                .getString(R.string.som_link_formatted, tickerUrl, tickerInfo.ctaText)
            val tickerDescription = "${tickerInfo.text} $tickerCta"
            setHtmlDescription(tickerDescription)

            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    val intent = RouteManager.getIntentNoFallback(context, tickerUrl)
                    if (intent != null) {
                        SomAnalytics.trackClickPofTicker(pofStatus)
                        context.startActivity(intent)
                    }
                }

                override fun onDismiss() {}
            })
            tickerType = Utils.mapStringTickerTypeToUnifyTickerType(tickerInfo.type)
            closeButtonVisibility = View.GONE
        }
    }

    private fun setupTicker(tickerBuyerRequestCancel: Ticker?, tickerInfo: TickerInfo, tickerContent: String, tickerUrl: String, isAwb: Boolean) {
        tickerBuyerRequestCancel?.apply {
            val tickerDescription = makeTickerDescription(context, tickerInfo, tickerContent, isAwb)
            setHtmlDescription(tickerDescription)

            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    if (tickerUrl.isNotBlank()) {
                        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, tickerUrl)
                    }
                }

                override fun onDismiss() {}
            })
            tickerType = Utils.mapStringTickerTypeToUnifyTickerType(tickerInfo.type)
            closeButtonVisibility = View.GONE
        }
    }

    private fun makeTickerDescription(context: Context, tickerInfo: TickerInfo, tickerContent: String, isAwb: Boolean): String {
        return binding?.root?.run {
            val message = Utils.getL2CancellationReason(tickerInfo.text, context.getString(R.string.som_header_detail_ticker_cancellation))
            val additionalInvalidResi = context.getString(R.string.additional_invalid_resi)
            if (isAwb) {
                String.format(context.getString(R.string.som_detail_ticker_description), tickerContent, additionalInvalidResi)
            } else {
                String.format(context.getString(R.string.som_detail_ticker_description), message, tickerInfo.actionText)
            }
        }.orEmpty()
    }

    private fun getWarehouseLabelString(context: Context?): String {
        return context?.getString(R.string.warehouse_label).orEmpty()
    }

    companion object {
        const val MAX_BUYER_NAME = 35

        val LAYOUT = R.layout.detail_header_item
    }
}
