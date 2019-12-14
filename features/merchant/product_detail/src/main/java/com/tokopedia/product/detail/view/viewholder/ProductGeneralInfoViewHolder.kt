package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductGeneralInfoDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_general_info.view.*

class ProductGeneralInfoViewHolder(val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductGeneralInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_general_info
    }

    override fun bind(element: ProductGeneralInfoDataModel) {
        showLoading()
        element.data?.run {
            view.pdp_info_title.text = MethodChecker.fromHtml(title)
            view.pdp_info_title.show()
            if (element.description.isNotEmpty()) {
                hideLoading()
                view.pdp_info_desc.show()
                view.pdp_info_desc.text = MethodChecker.fromHtml(element.description)
                view.setOnClickListener {
                    listener.onInfoClicked(element.name)
                }
            } else {
                view.pdp_info_desc.text = ""
                view.setOnClickListener {
                    listener.onInfoClicked(element.name)
                }
            }

            if (element.isApplink) {
                view.pdp_arrow_right.visible()
            } else {
                view.pdp_arrow_right.gone()
            }
        }
    }

    private fun hideLoading() {
        view.titleShimmering.hide()
        view.descShimmering.hide()
    }

    private fun showLoading() {
        view.titleShimmering.show()
        view.descShimmering.show()
    }

}