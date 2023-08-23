package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.util.*

object CheckoutCrossSellItemView {

    private const val CROSS_SELL_UNDERLINE_TEXT = "isi pulsa"

    fun renderCrossSellItem(crossSellItem: CheckoutCrossSellItem, binding: ItemCheckoutCrossSellItemBinding, listener: CheckoutAdapterListener) {
        when (crossSellItem) {
            is CheckoutCrossSellModel -> renderCrossSell(
                crossSellItem,
                binding,
                listener
            )

            is CheckoutDonationModel -> renderDonation(
                crossSellItem,
                binding,
                listener
            )

            is CheckoutEgoldModel -> renderEgold(crossSellItem, binding, listener)
        }
    }

    private fun renderCrossSell(
        crossSellModel: CheckoutCrossSellModel,
        itemBinding: ItemCheckoutCrossSellItemBinding,
        listener: CheckoutAdapterListener
    ) {
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, _ -> }
        itemBinding.cbCheckoutCrossSellItem.isChecked = crossSellModel.isChecked
        itemBinding.cbCheckoutCrossSellItem.skipAnimation()
        var title = crossSellModel.crossSellModel.info.title
        val startUnderline = title.indexOf(CROSS_SELL_UNDERLINE_TEXT, ignoreCase = true)
        val underlineText = title.substring(startUnderline, CROSS_SELL_UNDERLINE_TEXT.length)
        if (startUnderline >= 0) {
            title = title.replaceRange(startUnderline, CROSS_SELL_UNDERLINE_TEXT.length, "<u>$underlineText</u>")
        }
        val text = MethodChecker.fromHtml(title)
        itemBinding.ivCheckoutCrossSellItem.setImageUrl(crossSellModel.crossSellModel.info.iconUrl)
        itemBinding.tvCheckoutCrossSellItem.text = text
        itemBinding.tvCheckoutCrossSellItem.setOnClickListener {
            if (startUnderline >= 0) {
                showCrossSellBottomSheet(crossSellModel, itemBinding, listener)
            }
        }
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, isChecked ->
            listener.onCrossSellItemChecked(isChecked, crossSellModel)
        }
    }

    private fun showCrossSellBottomSheet(
        crossSellModel: CheckoutCrossSellModel,
        itemBinding: ItemCheckoutCrossSellItemBinding,
        listener: CheckoutAdapterListener
    ) {
        GeneralBottomSheet().apply {
            setTitle(
                MethodChecker.fromHtml(crossSellModel.crossSellModel.bottomSheet.title)
                    .toString()
            )
            setDesc(
                MethodChecker.fromHtml(crossSellModel.crossSellModel.bottomSheet.subtitle)
                    .toString()
            )
            setButtonText(itemBinding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setButtonOnClickListener { it.dismiss() }
        }.show(itemBinding.root.context, listener.getHostFragmentManager())
    }

    @SuppressLint("SetTextI18n")
    private fun renderDonation(
        donationModel: CheckoutDonationModel,
        itemBinding: ItemCheckoutCrossSellItemBinding,
        listener: CheckoutAdapterListener
    ) {
        itemBinding.tvCheckoutCrossSellItem.text = "${donationModel.donation.title} (${
        CurrencyFormatUtil.convertPriceValueToIdrFormat(
            donationModel.donation.nominal,
            false
        ).removeDecimalSuffix()
        })"
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, _ -> }
        itemBinding.cbCheckoutCrossSellItem.isChecked = donationModel.donation.isChecked
        itemBinding.cbCheckoutCrossSellItem.skipAnimation()
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            listener.onDonationChecked(isChecked)
        }
        itemBinding.tvCheckoutCrossSellItem.setOnClickListener {
            showDonationBottomSheet(itemBinding, donationModel, listener)
        }
    }

    private fun showDonationBottomSheet(itemBinding: ItemCheckoutCrossSellItemBinding, shipmentDonationModel: CheckoutDonationModel, listener: CheckoutAdapterListener) {
        GeneralBottomSheet().apply {
            setTitle(shipmentDonationModel.donation.title)
            setDesc(shipmentDonationModel.donation.description)
            setButtonText(itemBinding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setIcon(R.drawable.checkout_module_ic_donation)
            setButtonOnClickListener { it.dismiss() }
        }.show(itemBinding.root.context, listener.getHostFragmentManager())
    }

    @SuppressLint("SetTextI18n")
    private fun renderEgold(
        egoldModel: CheckoutEgoldModel,
        itemBinding: ItemCheckoutCrossSellItemBinding,
        listener: CheckoutAdapterListener
    ) {
        if (egoldModel.egoldAttributeModel.isEnabled) {
            itemBinding.cbCheckoutCrossSellItem.isEnabled = true
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                itemBinding.root.foreground = ContextCompat.getDrawable(
                    itemBinding.root.context,
                    com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item
                )
            }
        } else {
            itemBinding.cbCheckoutCrossSellItem.isEnabled = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                itemBinding.root.foreground = ContextCompat.getDrawable(
                    itemBinding.root.context,
                    com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item
                )
            }
        }
        itemBinding.tvCheckoutCrossSellItem.text = HtmlLinkHelper(
            itemBinding.root.context,
            "${egoldModel.egoldAttributeModel.titleText ?: ""} (${
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                egoldModel.egoldAttributeModel.buyEgoldValue,
                false
            ).removeDecimalSuffix()
            })"
        ).spannedString
        itemBinding.tvCheckoutCrossSellItem.setOnClickListener {
            if (egoldModel.egoldAttributeModel.isShowHyperlink) {
                RouteManager.route(
                    itemBinding.root.context,
                    String.format(
                        Locale.getDefault(),
                        "%s?url=%s",
                        ApplinkConst.WEBVIEW,
                        egoldModel.egoldAttributeModel.hyperlinkUrl
                    )
                )
            } else {
                showEgoldBottomSheet(itemBinding, egoldModel.egoldAttributeModel, listener)
            }
        }
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, _ -> }
        itemBinding.cbCheckoutCrossSellItem.isChecked = egoldModel.egoldAttributeModel.isChecked
        itemBinding.cbCheckoutCrossSellItem.skipAnimation()
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (egoldModel.egoldAttributeModel.isEnabled) {
                listener.onEgoldChecked(isChecked)
            }
        }
    }

    private fun showEgoldBottomSheet(
        itemBinding: ItemCheckoutCrossSellItemBinding,
        egoldAttributeModel: EgoldAttributeModel,
        listener: CheckoutAdapterListener
    ) {
        GeneralBottomSheet().apply {
            setTitle(egoldAttributeModel.titleText ?: "")
            setDesc(egoldAttributeModel.tooltipText ?: "")
            setButtonText(itemBinding.root.context.getString(com.tokopedia.purchase_platform.common.R.string.label_button_bottomsheet_close))
            setButtonOnClickListener { it.dismiss() }
        }.show(itemBinding.root.context, listener.getHostFragmentManager())
    }
}
