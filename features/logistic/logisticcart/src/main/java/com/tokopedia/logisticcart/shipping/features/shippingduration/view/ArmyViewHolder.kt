package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ArmyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tvTitle: Typography = itemView.findViewById(R.id.tv_title)
    private var tvKuota: Typography = itemView.findViewById(R.id.tv_kuota_info)
    private var tvInfo: Typography = itemView.findViewById(R.id.tv_info)
    private var lblCodAvailableEta: Label = itemView.findViewById(R.id.lbl_cod_available_eta)
    private var imgLogo: ImageView = itemView.findViewById(R.id.img_logo)
    private var flImageContainer: FrameLayout = itemView.findViewById(R.id.fl_image_container)
    private var imgCheck: IconUnify = itemView.findViewById(R.id.img_check)

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_army
        const val ESTIMASI_TIDAK_TERSEDIA = "Estimasi tidak tersedia"
    }

    fun bindData(data: LogisticPromoUiModel, listener: ShippingDurationAdapterListener?) {
        val formattedTitle = HtmlLinkHelper(itemView.context, data.freeShippingItemTitle).spannedString

        if (formattedTitle?.isNotEmpty() == true) {
            tvTitle.text = formattedTitle
            tvTitle.visible()
        } else {
            tvTitle.gone()
        }

        if (data.bebasOngkirKuota != 0L) {
            val kuotaTextTemplate = itemView.context.getString(R.string.checkout_bebas_ongkir_view_holder_description_template, data.bebasOngkirKuota)
            tvKuota.text = HtmlLinkHelper(itemView.context, kuotaTextTemplate).spannedString
            tvKuota.visible()
        } else {
            tvKuota.gone()
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
            imgLogo.contentDescription = itemView.context.getString(R.string.content_description_img_logo_rates_promo_prefix, data.title)
            imgLogo.loadImage(data.imageUrl)
            imgLogo.visible()
        } else {
            imgLogo.gone()
        }

        val fontColor = if (data.disabled) {
            ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32)
        } else {
            ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
        }

        tvInfo.setTextColor(fontColor)
        tvKuota.setTextColor(fontColor)

        if (!data.disabled) {
            itemView.setOnClickListener {
                listener?.onLogisticPromoClicked(data)
            }
            flImageContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_enabled_item)
        } else {
            tvTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
            flImageContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_disabled_item)
            itemView.setOnClickListener(null)
        }

        if (data.isApplied) {
            imgCheck.visible()
        } else {
            imgCheck.gone()
        }
    }
}
