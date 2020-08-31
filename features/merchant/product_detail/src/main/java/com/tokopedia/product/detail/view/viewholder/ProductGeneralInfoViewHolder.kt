package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.view.adapter.ProductGeneralItemAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_general_info.view.*

class ProductGeneralInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    override fun bind(element: ProductGeneralInfoDataModel) {
        if (element.data.first().subtitle.isEmpty()) {
            showLoading()
        } else {
            hideLoading()
        }

        element.data.run {
            if (element.data.firstOrNull()?.subtitle?.isEmpty() == true) {
                view.info_separator.gone()
                view.general_info_container.layoutParams.height = 0
            } else {
                view.info_separator.show()
                view.general_info_container.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }

            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }

            view.rv_general_info.adapter = ProductGeneralItemAdapter(this, element.name, listener, element.type, element.name, adapterPosition)

            if (element.isApplink) {
                view.pdp_arrow_right.show()
            } else {
                view.pdp_arrow_right.hide()
            }
        }

        view.pdp_info_title.text = MethodChecker.fromHtml(element.title)
        view.setOnClickListener {
            listener.onInfoClicked(element.name, getComponentTrackData(element))
        }

        if (element.parentIcon.isNotEmpty()) {
            view.pdp_icon.show()
            ImageHandler.LoadImage(view.pdp_icon, element.parentIcon)
        } else {
            view.pdp_icon.hide()
        }
    }

    private fun hideLoading() = with(view) {
        rv_general_info.show()
        pdp_info_title.show()
        titleShimmering.hide()
        descShimmering.hide()
    }

    private fun showLoading() = with(view) {
        pdp_arrow_right.hide()
        pdp_info_title.hide()
        rv_general_info.hide()
        titleShimmering.show()
        descShimmering.show()
    }


    private fun getComponentTrackData(element: ProductGeneralInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "",
            element?.name ?: "", adapterPosition + 1)
}