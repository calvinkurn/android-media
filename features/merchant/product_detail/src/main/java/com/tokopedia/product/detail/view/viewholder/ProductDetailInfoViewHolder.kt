package com.tokopedia.product.detail.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.info.util.ProductDetailInfoConstant
import com.tokopedia.product.share.ekstensions.layoutInflater
import kotlinx.android.synthetic.main.item_dynamic_product_detail_info.view.*
import kotlinx.android.synthetic.main.item_info_product_detail.view.*

/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductDetailInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_product_detail_info
        private const val DESCRIPTION_LIMIT = 300
    }

    override fun bind(element: ProductDetailInfoDataModel) {
        renderListInfo(element)
        renderDescription(element)

        view.product_detail_info_seemore?.setOnClickListener {
            listener.onSeeMoreDescriptionClicked(element.dataContent)
        }
    }

    private fun renderListInfo(element: ProductDetailInfoDataModel) = with(view) {
        val rootView = findViewById<ViewGroup>(R.id.product_detail_info_container)
        val inflater: LayoutInflater = context.layoutInflater
        rootView.removeAllViews()

        element.getShowableData().forEach { data ->
            val socProofView: View = inflater.inflate(R.layout.item_info_product_detail, null)
            setupListItem(socProofView, data)
            rootView.addView(socProofView)
        }
    }

    private fun setupListItem(itemView: View, data: ProductDetailInfoContent) = with(view) {
        itemView.info_detail_title?.text = data.title
        itemView.info_detail_value?.text = data.subtitle
    }

    private fun renderDescription(element: ProductDetailInfoDataModel) = with(view) {
        val subtitleDescription = element.dataContent.firstOrNull {
            it.title.toLowerCase() == ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
        }?.subtitle ?: ""

        val descFormatted = MethodChecker.fromHtmlPreserveLineBreak(subtitleDescription)

        if (descFormatted.isNotEmpty()) {
            (product_detail_info_seemore.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_8)

            product_detail_info_description.text = if (descFormatted.length > DESCRIPTION_LIMIT) {
                val subDescr = descFormatted.toString().substring(0, DESCRIPTION_LIMIT)
                MethodChecker.fromHtml(subDescr.replace("(\r\n|\n)".toRegex(), "") + "....")
            } else descFormatted
            product_detail_info_description?.show()
        } else {
            (product_detail_info_seemore.layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
            product_detail_info_description?.hide()
        }
    }

    private fun getComponentTrackData(element: ProductDetailInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}