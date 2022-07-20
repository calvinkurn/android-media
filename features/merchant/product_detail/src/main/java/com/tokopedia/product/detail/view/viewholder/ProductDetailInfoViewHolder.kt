package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicProductDetailInfoBinding
import com.tokopedia.product.detail.view.adapter.ProductDetailInfoAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDetailInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_detail_info
    }

    private val binding = ItemDynamicProductDetailInfoBinding.bind(view)

    private var adapter = ProductDetailInfoAdapter(listener)
    private var rvProductDetail: RecyclerView? = itemView.findViewById(R.id.rv_product_detail_info)

    override fun bind(element: ProductDetailInfoDataModel) {
        renderListInfo(element)
        renderDescription(element)

        binding.productDetailInfoSeemore.setOnClickListener {
            listener.onSeeMoreDescriptionClicked(element.dataContent, getComponentTrackData(element))
        }
        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun renderListInfo(element: ProductDetailInfoDataModel) = with(view) {
        rvProductDetail?.adapter = adapter
        adapter.updateData(element.getShowableData(), getComponentTrackData(element))
    }

    private fun renderDescription(element: ProductDetailInfoDataModel) = with(binding) {
        val descFormatted = element.getDescription()
        val resources = view.resources
        if (descFormatted.isNotEmpty()) {
            (productDetailInfoSeemore.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)

            val textDesc = descFormatted.parseAsHtml().toString().replace("(\r\n|\n)".toRegex(), " ")

            productDetailInfoDescription.text = textDesc
            productDetailInfoDescription.show()
            productDetailInfoDescription.setOnClickListener {
                listener.onSeeMoreDescriptionClicked(element.dataContent, getComponentTrackData(element))
            }
        } else {
            (productDetailInfoSeemore.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
            productDetailInfoDescription.hide()
        }
    }

    private fun getComponentTrackData(element: ProductDetailInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}