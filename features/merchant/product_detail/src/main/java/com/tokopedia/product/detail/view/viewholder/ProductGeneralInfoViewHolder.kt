package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicGeneralInfoBinding
import com.tokopedia.product.detail.databinding.ItemProtectionPartnerInfoDetailBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductGeneralInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    private val binding = ItemDynamicGeneralInfoBinding.bind(view)

    override fun bind(element: ProductGeneralInfoDataModel) {
        renderView(element.subtitle)

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }

        val pdpGeneralInfoDescDetail = binding.pdpGeneralInfoDescDetail

        if (element.additionalDesc.isEmpty() && element.additionalIcon.isEmpty()) {
            pdpGeneralInfoDescDetail.root.hide()
        } else {
            pdpGeneralInfoDescDetail.root.show()
        }

        binding.pdpInfoTitle.text = MethodChecker.fromHtml(element.title)
        binding.pdpInfoTitle.show()
        binding.pdpInfoDesc.text = MethodChecker.fromHtml(element.subtitle)

        val pdpAdditionalInfoDesc = pdpGeneralInfoDescDetail.pdpAdditionalInfoDesc
        pdpAdditionalInfoDesc.shouldShowWithAction(element.additionalDesc.isNotEmpty()) {
            pdpAdditionalInfoDesc.text = element.additionalDesc
        }

        view.setOnClickListener {
            listener.onInfoClicked(element.applink, element.name, getComponentTrackData(element))
        }
        renderIcon(element, pdpGeneralInfoDescDetail)
    }

    private fun renderIcon(
        element: ProductGeneralInfoDataModel,
        pdpGeneralInfoDescDetail: ItemProtectionPartnerInfoDetailBinding
    ) = with(binding) {
        pdpArrowRight.showWithCondition(element.isApplink)

        pdpIcon.shouldShowWithAction(element.parentIcon.isNotEmpty()) {
            pdpIcon.loadIcon(element.parentIcon)
        }

        val icPdpAdditionalInfo = pdpGeneralInfoDescDetail.icPdpAdditionalInfo
        icPdpAdditionalInfo.shouldShowWithAction(element.additionalIcon.isNotEmpty()) {
            icPdpAdditionalInfo.loadIcon(element.additionalIcon)
        }
    }

    private fun renderView(subtitle: String) = with(binding) {
        if (subtitle.isEmpty()) {
            infoSeparator.gone()
            generalInfoContainer.layoutParams.height = 0
        } else {
            infoSeparator.show()
            generalInfoContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun getComponentTrackData(element: ProductGeneralInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}