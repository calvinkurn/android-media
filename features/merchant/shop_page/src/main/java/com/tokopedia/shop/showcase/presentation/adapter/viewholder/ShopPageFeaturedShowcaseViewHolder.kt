package com.tokopedia.shop.showcase.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageListener
import com.tokopedia.shop.showcase.domain.model.ShopFeaturedShowcase
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseViewHolder(
        itemView: View,
        listener: ShopShowcaseListImageListener
): RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val MARGIN_DP_10 = 10.0f
    }

    private var ivShowcaseImg: ImageUnify? = null
    private var tvShowcaseName: Typography? = null
    private var tvShowcaseCount: Typography? = null
    private var showcaseId: String = ""

    init {
        ivShowcaseImg = itemView.findViewById(R.id.ivShowcaseImg)
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)

        // ini listeners
        itemView.setOnClickListener {
            listener.onShowcaseListItemSelected(showcaseId)
        }
    }

    fun bind(list: List<ShopFeaturedShowcase>) {

        val element = list[adapterPosition]

        // set card content
        showcaseId = element.id
        ivShowcaseImg?.setImageUrl(element.imageUrl)
        tvShowcaseName?.text = element.name
        tvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_featured_product_count_text,
                element.count.toString()
        )

        setItemMargin(adapterPosition, list)
    }

    private fun setItemMargin(position: Int, list: List<ShopFeaturedShowcase>) {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (position == 0) {
            // set margin for first featured showcase item
            layoutParams.setMargins(MARGIN_DP_10.toPx().toInt(), 0, 0, 0)
        }
        else if (position == list.size - 1) {
            // set margin for last featured showcase item
            layoutParams.setMargins(0, 0, MARGIN_DP_10.toPx().toInt(), 0)
        }
        itemView.layoutParams = layoutParams
    }
}