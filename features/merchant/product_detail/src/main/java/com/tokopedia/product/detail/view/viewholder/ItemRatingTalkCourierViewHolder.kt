package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialProductStatisticView
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.view.*

class ItemRatingTalkCourierViewHolder(private val view: View) : BaseSocialProofViewHolder<ProductSocialProofDataModel>(view) {

    override fun bind(element: ProductSocialProofDataModel, position: Int) {
//        val inflater = view.context.layoutInflater
//
//        element.dataLayout[position].row.forEachIndexed { index, c ->
//            val viewGroup = inflater.inflate(R.layout.item_attribute_icon, view.entry_point, false)
//            val textView = viewGroup.findViewById<TextView>(R.id.txt_attribute)
//            textView.text = view.context.getString(R.string.template_review, element.productInfo.stats.countReview)
//            view.entry_point.addView(view)
//        }

        val productStatsView = PartialProductStatisticView.build(view.base_rating_talk_courier)
        productStatsView.renderRating(element.productInfoP2.rating)
        productStatsView.renderData(element.productInfo)

    }
}