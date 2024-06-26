package com.tokopedia.shop_showcase.shop_showcase_tab.presentation.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.databinding.ItemShopFeaturedShowcaseBinding
import com.tokopedia.shop_showcase.shop_showcase_tab.presentation.model.FeaturedShowcaseUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Rafli Syam on 09/03/2021
 */
class ShopPageFeaturedShowcaseViewHolder(
    itemView: View,
    private val listener: ShopPageFeaturedShowcaseListener,
    private val shouldForceToLightMode: Boolean
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val MARGIN_DP_10 = 10.0f
    }

    private val viewBinding: ItemShopFeaturedShowcaseBinding? by viewBinding()
    private var ivShowcaseImg: ImageUnify? = null
    private var tvShowcaseName: Typography? = null

    init {
        ivShowcaseImg = viewBinding?.ivShowcaseImg
        tvShowcaseName = viewBinding?.tvShowcaseName
    }

    fun bind(list: List<FeaturedShowcaseUiModel>) {
        val element = list[adapterPosition]

        tvShowcaseName?.text = element.name

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

        handleColorSchema(shouldForceToLightMode)
    }

    private fun handleColorSchema(shouldForceToLightMode: Boolean) {
        if (shouldForceToLightMode) {
            val cardBackgroundColor = ContextCompat.getColor(
                viewBinding?.container?.context ?: return,
                R.color.dms_clr_Unify_NN0_0_light
            )

            viewBinding?.container?.setCardBackgroundColor(cardBackgroundColor)

            val showcaseTextColor = MethodChecker.getColor(
                viewBinding?.container?.context ?: return,
                unifyprinciplesR.color.Unify_Static_Black
            )
            viewBinding?.tvShowcaseName?.setTextColor(showcaseTextColor)
        }
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
