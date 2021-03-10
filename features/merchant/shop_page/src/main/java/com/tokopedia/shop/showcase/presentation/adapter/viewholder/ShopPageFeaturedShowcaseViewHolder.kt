package com.tokopedia.shop.showcase.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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

    private var ivShowcaseImg: ImageUnify? = null
    private var tvShowcaseName: Typography? = null
    private var tvShowcaseCount: Typography? = null

    init {
        ivShowcaseImg = itemView.findViewById(R.id.ivShowcaseImg)
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
    }

    fun bind(element: ShopEtalaseModel) {
        ivShowcaseImg?.setImageUrl(element.imageUrl ?: "")
        tvShowcaseName?.text = element.name
        tvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_featured_product_count_text,
                element.count.toString()
        )
    }

}