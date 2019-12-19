package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.view.adapter.ProductGeneralItemAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_general_info.view.*

class ProductGeneralInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    override fun bind(element: ProductGeneralInfoDataModel) {
        hideLoading()
        element.data.run {
            view.rv_general_info.adapter = ProductGeneralItemAdapter(this)
        }

        view.pdp_info_title.text = MethodChecker.fromHtml(element.title)
        view.setOnClickListener {
            listener.onInfoClicked(element.name)
        }

        if (element.isApplink) {
            view.pdp_arrow_right.show()
        } else {
            view.pdp_arrow_right.hide()
        }

        if (element.parentIcon.isNotEmpty()) {
            view.pdp_icon.show()
            ImageHandler.loadImage(view.context, view.pdp_icon, element.parentIcon, R.drawable.ic_loading_image)
        } else {
            view.pdp_icon.hide()
        }
    }

    private fun hideLoading() {
        view.pdp_info_title.show()
        view.titleShimmering.hide()
        view.descShimmering.hide()
    }

    private fun showLoading() {
        view.pdp_info_title.hide()
        view.titleShimmering.show()
        view.descShimmering.show()
    }

}