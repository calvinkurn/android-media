package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.databinding.ItemArmyBinding
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.logisticcart.R as logisticcartR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ArmyViewHolder(val binding: ItemArmyBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val LAYOUT = 1
        const val ESTIMASI_TIDAK_TERSEDIA = "Estimasi tidak tersedia"
    }

    fun bindData(data: LogisticPromoUiModel, listener: ShippingDurationAdapterListener?) {
        binding.run {
            val formattedTitle =
                HtmlLinkHelper(root.context, data.freeShippingItemTitle).spannedString

            if (formattedTitle?.isNotEmpty() == true) {
                tvTitle.text = formattedTitle
                tvTitle.visible()
            } else {
                tvTitle.gone()
            }

            if (data.bebasOngkirKuota.isNotEmpty()) {
                val kuotaTextTemplate = itemView.context.getString(
                    logisticcartR.string.checkout_bebas_ongkir_view_holder_description_template,
                    data.bebasOngkirKuota
                )
                tvKuotaInfo.text = HtmlLinkHelper(itemView.context, kuotaTextTemplate).spannedString
                tvKuotaInfo.visible()
            } else {
                tvKuotaInfo.gone()
            }

            if (data.codData.isCodAvailable == 1) {
                lblCodAvailableEta.apply {
                    visible()
                    text = data.codData.codText
                }
            } else {
                lblCodAvailableEta.gone()
            }

            if (data.bottomSheetDescription.isNotEmpty()) {
                tvInfo.text = MethodChecker.fromHtml(data.bottomSheetDescription)
                tvInfo.visible()
            } else if (data.promoMessage.isNotBlank()) {
                tvInfo.text = MethodChecker.fromHtml(data.promoMessage)
                tvInfo.visible()
            } else {
                tvInfo.gone()
            }

            if (data.imageUrl.isNotEmpty()) {
                imgLogo.contentDescription = itemView.context.getString(
                    logisticcartR.string.content_description_img_logo_rates_promo_prefix,
                    data.title
                )
                imgLogo.loadImage(data.imageUrl)
                flImageContainer.visible()
            } else {
                flImageContainer.gone()
            }

            val fontColor = if (data.disabled) {
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN950_32
                )
            } else {
                ContextCompat.getColor(
                    itemView.context,
                    unifyprinciplesR.color.Unify_NN950_68
                )
            }

            tvInfo.setTextColor(fontColor)
            tvKuotaInfo.setTextColor(fontColor)

            if (!data.disabled) {
                itemView.setOnClickListener {
                    listener?.onLogisticPromoClicked(data)
                }
                flImageContainer.foreground =
                    ContextCompat.getDrawable(itemView.context, logisticcartR.drawable.fg_enabled_item)
            } else {
                tvTitle.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_NN400
                    )
                )
                tvInfo.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        unifyprinciplesR.color.Unify_NN400
                    )
                )
                flImageContainer.foreground =
                    ContextCompat.getDrawable(itemView.context, logisticcartR.drawable.fg_disabled_item)
                itemView.setOnClickListener(null)
            }

            if (data.isApplied) {
                imgCheck.visible()
            } else {
                imgCheck.gone()
            }
        }
    }
}
