package com.tokopedia.shop.common.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ShopShowcaseListImageViewHolder(
        itemView: View,
        private val listener: ShopShowcaseListImageListener
) : RecyclerView.ViewHolder(itemView) {

    private var showcaseImage: ImageUnify? = null
    private var tvShowcaseName: Typography? = null
    private var tvShowcaseCount: Typography? = null
    private var showcaseCampaignLabel: Label? = null
    private var showcaseId: String = ""

    init {
        showcaseImage = itemView.findViewById(R.id.ivShowcaseImage)
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
        showcaseCampaignLabel = itemView.findViewById(R.id.showcaseCampaignLabel)
    }

    fun bind(element: ShopEtalaseUiModel) {
        showcaseId = element.id
        tvShowcaseName?.text = element.name
        tvShowcaseCount?.text = itemView.context.getString(
                R.string.shop_page_showcase_product_count_text,
                element.count.toString()
        )
        showcaseCampaignLabel?.visibility = if (isShowCampaignLabel(element.type)) {
            View.VISIBLE
        } else {
            View.GONE
        }
        showcaseCampaignLabel?.setLabel(getCampaignLabelTitle(element.type))

        // try catch to avoid crash ImageUnify on loading image with Glide
        try {
            if (showcaseImage?.context?.isValidGlideContext() == true) {
                showcaseImage?.setImageUrl(element.imageUrl)
            }
        } catch (e: Throwable) {
        }

        // showcase item impressed listener
        showcaseImage?.addOnImpressionListener(
                holder = element,
                listener = object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onShowcaseListItemImpressed(element, adapterPosition)
                    }
                }
        )

        // showcase item click listener
        itemView.setOnClickListener {
            listener.onShowcaseListItemSelected(element, adapterPosition)
        }
    }

    private fun isShowCampaignLabel(type: Int): Boolean {
        return when (type) {
            ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
            ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN -> true
            else -> false
        }
    }

    private fun getCampaignLabelTitle(type: Int): String {
        return when (type) {
            ShopEtalaseTypeDef.ETALASE_CAMPAIGN -> {
                itemView.context.getString(R.string.shop_page_showcase_npl_text)
            }
            ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN -> {
                itemView.context.getString(R.string.shop_page_showcase_thematic_text)
            }
            else -> ""
        }
    }

}

interface ShopShowcaseListImageListener {
    fun onShowcaseListItemSelected(element: ShopEtalaseUiModel, position: Int)
    fun onShowcaseListItemImpressed(element: ShopEtalaseUiModel, position: Int)
}