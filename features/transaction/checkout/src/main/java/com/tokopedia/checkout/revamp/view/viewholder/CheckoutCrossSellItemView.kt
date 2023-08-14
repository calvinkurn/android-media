package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
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
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.util.*

object CheckoutCrossSellItemView {

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
        itemBinding.cbCheckoutCrossSellItem.isChecked = crossSellModel.isChecked
        itemBinding.cbCheckoutCrossSellItem.skipAnimation()
        itemBinding.tvCheckoutCrossSellItem.text = MethodChecker.fromHtml(crossSellModel.crossSellModel.info.title)
    }

    private fun renderDonation(
        donationModel: CheckoutDonationModel,
        itemBinding: ItemCheckoutCrossSellItemBinding,
        listener: CheckoutAdapterListener
    ) {
        itemBinding.ivCheckoutCrossSellItem.setImageResource(R.drawable.ic_donasi_lingkungan)
        itemBinding.tvCheckoutCrossSellItem.text = donationModel.donation.title
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _, _ -> }
        itemBinding.cbCheckoutCrossSellItem.isChecked = donationModel.donation.isChecked
        itemBinding.cbCheckoutCrossSellItem.skipAnimation()
        itemBinding.cbCheckoutCrossSellItem.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            listener.onDonationChecked(isChecked)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderEgold(
        egoldModel: CheckoutEgoldModel,
        itemBinding: ItemCheckoutCrossSellItemBinding,
        listener: CheckoutAdapterListener
    ) {
        itemBinding.ivCheckoutCrossSellItem.setImageResource(R.drawable.ic_logam_mulia)
        val text = if (egoldModel.egoldAttributeModel.isShowHyperlink) {
            "${egoldModel.egoldAttributeModel.titleText ?: ""} ${egoldModel.egoldAttributeModel.hyperlinkText ?: ""} (${
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                egoldModel.egoldAttributeModel.buyEgoldValue,
                false
            ).removeDecimalSuffix()
            })"
        } else {
            "${egoldModel.egoldAttributeModel.titleText ?: ""} (${
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                egoldModel.egoldAttributeModel.buyEgoldValue,
                false
            ).removeDecimalSuffix()
            })"
        }
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
        itemBinding.tvCheckoutCrossSellItem.text = SpannableString(text).apply {
            if (egoldModel.egoldAttributeModel.isShowHyperlink) {
                val start = "${egoldModel.egoldAttributeModel.titleText ?: ""} ".length
                setSpan(
                    UnderlineSpan(),
                    start,
                    start + (egoldModel.egoldAttributeModel.hyperlinkText ?: "").length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
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
}
