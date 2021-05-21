package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_general_info.view.*
import kotlinx.android.synthetic.main.item_protection_partner_info_detail.view.*

class ProductGeneralInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    override fun bind(element: ProductGeneralInfoDataModel) {
        renderView(element.subtitle)

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }

        if (element.additionalDesc.isEmpty() && element.additionalIcon.isEmpty()) {
            view.pdp_general_info_desc_detail?.hide()
        } else {
            view.pdp_general_info_desc_detail?.show()
        }

        view.pdp_info_title.text = MethodChecker.fromHtml(element.title)
        view.pdp_info_title.show()
        view.pdp_info_desc?.text = MethodChecker.fromHtml(element.subtitle)
        view.pdp_additional_info_desc.shouldShowWithAction(element.additionalDesc.isNotEmpty()) {
            view.pdp_additional_info_desc?.text = element.additionalDesc
        }

        view.setOnClickListener {
            if (element.name == ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO) {
                listener.goToApplink(element.applink)
                listener.sendTrackerInstallmentPayment(getComponentTrackData(element))
            } else {
                listener.onInfoClicked(element.name, getComponentTrackData(element))
            }
        }
        renderIcon(element)
    }

    private fun renderIcon(element: ProductGeneralInfoDataModel) = with(itemView) {
        pdp_arrow_right?.showWithCondition(element.isApplink)

        pdp_icon?.shouldShowWithAction(element.parentIcon.isNotEmpty()) {
            view.pdp_icon?.loadIcon(element.parentIcon)
        }

        view.ic_pdp_additional_info?.shouldShowWithAction(element.additionalIcon.isNotEmpty()) {
            view.ic_pdp_additional_info?.loadIcon(element.additionalIcon)
        }
    }

    private fun renderView(subtitle: String) = with(itemView) {
        if (subtitle.isEmpty()) {
            info_separator.gone()
            general_info_container.layoutParams.height = 0
        } else {
            info_separator.show()
            general_info_container.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun getComponentTrackData(element: ProductGeneralInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}