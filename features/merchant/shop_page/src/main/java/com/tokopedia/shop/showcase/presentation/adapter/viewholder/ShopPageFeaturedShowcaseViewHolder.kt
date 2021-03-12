package com.tokopedia.shop.showcase.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.shop.R
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseViewHolder(
        itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val MARGIN_DP_12 = 12.0f
    }

    private var ivShowcaseImg: ImageUnify? = null
    private var tvShowcaseName: Typography? = null
    private var tvShowcaseCount: Typography? = null

    init {
        ivShowcaseImg = itemView.findViewById(R.id.ivShowcaseImg)
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
    }

    fun bind(list: List<ShopEtalaseModel>) {

        val element = list[adapterPosition]

        // set card content
        ivShowcaseImg?.setImageUrl(element.imageUrl ?: "")
        tvShowcaseName?.text = element.name
        tvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_featured_product_count_text,
                element.count.toString()
        )

        setItemMargin(adapterPosition, list)
    }

    private fun setItemMargin(position: Int, list: List<ShopEtalaseModel>) {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (position == 0) {
            // set margin for first featured showcase item
            layoutParams.setMargins(MARGIN_DP_12.toPx().toInt(), 0, 0, 0)
        }
        else if (position == list.size - 1) {
            // set margin for last featured showcase item
            layoutParams.setMargins(0, 0, MARGIN_DP_12.toPx().toInt(), 0)
        }
        itemView.layoutParams = layoutParams
    }
}