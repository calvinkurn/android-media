package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
import com.tokopedia.product.detail.view.adapter.ProductDetailInfoAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_product_detail_info.view.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDetailInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_detail_info
        private const val DESCRIPTION_LIMIT = 300
    }

    private var listAdapter: ProductDetailInfoAdapter? = null

    override fun bind(element: ProductDetailInfoDataModel) {
        renderListInfo(element)
        renderDescription(element)

        view.product_detail_info_seemore?.setOnClickListener {
            listener.onSeeMoreDescriptionClicked()
        }
    }

    private fun renderListInfo(element: ProductDetailInfoDataModel) = with(view) {
        if (listAdapter == null) {
            listAdapter = ProductDetailInfoAdapter(listener, getComponentTrackData(element))
        }
        product_detail_info_rv.adapter = listAdapter
        listAdapter?.updateData(element.getShowableData())
    }

    private fun renderDescription(element: ProductDetailInfoDataModel) = with(view) {
        val descFormatted = MethodChecker.fromHtmlPreserveLineBreak(if (element.dataContent.lastOrNull()?.subtitle?.isNotBlank() == true) element.dataContent.last().subtitle else "")

        if (descFormatted.isNotEmpty()) {
            product_detail_info_description.text = if (descFormatted.length > DESCRIPTION_LIMIT) {
                val subDescr = descFormatted.toString().substring(0, DESCRIPTION_LIMIT)
                MethodChecker.fromHtml(subDescr.replace("(\r\n|\n)".toRegex(), "<br />") + "....")
            } else descFormatted
        } else {
            product_detail_info_description?.hide()
        }
    }

    private fun getComponentTrackData(element: ProductDetailInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}