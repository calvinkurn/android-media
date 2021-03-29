package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductInfoDataModel
import com.tokopedia.product.detail.view.adapter.ProductDetailInfoAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.info.util.ProductDetailInfoConstant
import kotlinx.android.synthetic.main.item_dynamic_product_detail_info.view.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDetailInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_detail_info
        private const val DESCRIPTION_LIMIT = 300
    }

    private var adapter = ProductDetailInfoAdapter(listener)
    private var rvProductDetail: RecyclerView? = itemView.findViewById(R.id.rv_product_detail_info)

    override fun bind(element: ProductDetailInfoDataModel) {
        renderListInfo(element)
        renderDescription(element)

        view.product_detail_info_seemore?.setOnClickListener {
            listener.onSeeMoreDescriptionClicked(element.dataContent, getComponentTrackData(element))
        }
    }

    private fun renderListInfo(element: ProductDetailInfoDataModel) = with(view) {
        rvProductDetail?.adapter = adapter
        adapter.updateData(element.getShowableData(), getComponentTrackData(element))
    }

    private fun renderDescription(element: ProductDetailInfoDataModel) = with(view) {
        val descFormatted = element.dataContent.find {
            it.title.toLowerCase() == ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
        }?.subtitle ?: ""

        if (descFormatted.isNotEmpty()) {
            (product_detail_info_seemore.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)

            val textDesc = if (descFormatted.length > DESCRIPTION_LIMIT) {
                MethodChecker.fromHtml(descFormatted.replace("(\r\n|\n)".toRegex(), " "))
            } else descFormatted

            product_detail_info_description.text = textDesc
            product_detail_info_description?.show()
        } else {
            (product_detail_info_seemore.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
            product_detail_info_description?.hide()
        }
    }

    private fun getComponentTrackData(element: ProductDetailInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}