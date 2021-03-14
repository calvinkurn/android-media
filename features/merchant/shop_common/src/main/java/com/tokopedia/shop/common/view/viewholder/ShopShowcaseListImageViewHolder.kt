package com.tokopedia.shop.common.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ShopShowcaseListImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var showcaseImage: ImageUnify? = null
    private var tvShowcaseName: Typography? = null
    private var tvShowcaseCount: Typography? = null
    private var showcaseCampaignLabel: Label? = null

    init {
        showcaseImage = itemView.findViewById(R.id.ivShowcaseImage)
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
        showcaseCampaignLabel = itemView.findViewById(R.id.showcaseCampaignLabel)
    }

    fun bind(element: ShopEtalaseModel) {
        showcaseImage?.setImageUrl(element.imageUrl ?: "")
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