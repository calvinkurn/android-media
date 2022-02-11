package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography


class ArmyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tvTitle: Typography = itemView.findViewById(R.id.tv_title)
    private var tvTitleExtra: Typography = itemView.findViewById(R.id.tv_title_extra)
    private var tvInfo: Typography = itemView.findViewById(R.id.tv_info)
    private var tvEta: Typography = itemView.findViewById(R.id.tv_eta)
    private var lblCodAvailableEta: Label = itemView.findViewById(R.id.lbl_cod_available_eta)
    private var imgLogo: ImageView = itemView.findViewById(R.id.img_logo)
    private var flImageContainer: FrameLayout = itemView.findViewById(R.id.fl_image_container)
    private var imgCheck: IconUnify = itemView.findViewById(R.id.img_check)

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_army
        const val ESTIMASI_TIDAK_TERSEDIA = "Estimasi tidak tersedia"
    }

    fun bindData(data: LogisticPromoUiModel, listener: ShippingDurationAdapterListener?, isOcc: Boolean = false) {
        // todo need to confirm if this impacting all or only bo eko
        val formattedTitle = HtmlLinkHelper(itemView.context, data.freeShippingItemTitle).spannedString
        
        tvTitle.text = formattedTitle
        tvTitle.visibility = View.VISIBLE
        tvTitleExtra.visibility = View.GONE

        if (!data.isBebasOngkirExtra || !isOcc) {
            if (data.etaData.errorCode == 0 && data.etaData.textEta.isNotEmpty()) {
                tvEta.visibility = View.VISIBLE
                tvEta.text = data.etaData.textEta
            } else if (data.etaData.errorCode == 0 && data.etaData.textEta.isEmpty()) {
                tvEta.visibility = View.VISIBLE
                tvEta.text = ESTIMASI_TIDAK_TERSEDIA
            } else {
                tvEta.visibility = View.GONE
            }
        } else {
            tvEta.visibility = View.GONE
        }

        if (data.codData.isCodAvailable == 1) {
            lblCodAvailableEta.apply {
                visibility = View.VISIBLE
                text = data.codData.codText
            }
        } else {
            lblCodAvailableEta.visibility = View.GONE
        }

        tvInfo.text = MethodChecker.fromHtml(data.bottomSheetDescription)
        if (data.bottomSheetDescription.isEmpty()) tvInfo.visibility = View.GONE

        if (data.imageUrl.isNotEmpty()) {
            imgLogo.contentDescription = itemView.context.getString(R.string.content_description_img_logo_rates_promo_prefix, data.title)
            ImageHandler.LoadImage(imgLogo, data.imageUrl)
        } else {
            imgLogo.visibility = View.GONE
        }

        val fontColor = if (data.disabled) {
            ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
        } else {
            ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        }
        val boldFontColor = if (data.disabled) {
            ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
        } else {
            ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        }

        tvInfo.setTextColor(fontColor)
        tvEta.setTextColor(fontColor)

        if (!data.disabled) {
            itemView.setOnClickListener {
                listener?.onLogisticPromoClicked(data)
            }
            flImageContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_enabled_item)
        } else {
            tvTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            flImageContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_disabled_item)
            itemView.setOnClickListener(null)
        }

        if (data.isApplied) {
            imgCheck.visibility = View.VISIBLE
        } else {
            imgCheck.visibility = View.GONE
        }
    }

}