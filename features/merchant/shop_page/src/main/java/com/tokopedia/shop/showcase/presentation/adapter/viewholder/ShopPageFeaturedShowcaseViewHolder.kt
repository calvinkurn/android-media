package com.tokopedia.shop.showcase.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.shop.R
import com.tokopedia.shop.showcase.presentation.model.FeaturedShowcaseUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseViewHolder(
        itemView: View,
        private val listener: ShopPageFeaturedShowcaseListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val MARGIN_DP_10 = 10.0f
    }

    private var ivShowcaseImg: ImageUnify? = null
    private var tvShowcaseName: Typography? = null
    private var tvShowcaseCount: Typography? = null

    init {
        ivShowcaseImg = itemView.findViewById(R.id.ivShowcaseImg)
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
    }

    fun bind(list: List<FeaturedShowcaseUiModel>) {

        val element = list[adapterPosition]

        tvShowcaseName?.text = element.name
        tvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_featured_product_count_text,
                element.count.toString()
        )

        // try catch to avoid crash ImageUnify on loading image with Glide
        try {
            if (ivShowcaseImg?.context?.isValidGlideContext() == true) {
                element.imageUrl?.let { ivShowcaseImg?.setImageUrl(it) }
            }
        } catch (e: Throwable) {
        }

        // adjust margin for first or last item.
        setItemMargin(adapterPosition, list)

        // featured showcase item click listener
        itemView.setOnClickListener {
            listener.onFeaturedShowcaseClicked(element, adapterPosition)
        }

        // featured showcase item impressed listener
        ivShowcaseImg?.addOnImpressionListener(
                holder = element,
                listener = object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onFeaturedShowcaseImpressed(element, adapterPosition)
                    }
                }
        )
    }

    private fun setItemMargin(position: Int, list: List<FeaturedShowcaseUiModel>) {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (position == 0) {
            // set margin for first featured showcase item
            layoutParams.setMargins(MARGIN_DP_10.toPx().toInt(), 0, 0, 0)
        } else if (position == list.size - 1) {
            // set margin for last featured showcase item
            layoutParams.setMargins(0, 0, MARGIN_DP_10.toPx().toInt(), 0)
        }
        itemView.layoutParams = layoutParams
    }
}

interface ShopPageFeaturedShowcaseListener {
    fun onFeaturedShowcaseClicked(element: FeaturedShowcaseUiModel, position: Int)
    fun onFeaturedShowcaseImpressed(element: FeaturedShowcaseUiModel, position: Int)
}