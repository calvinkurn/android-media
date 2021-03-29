package com.tokopedia.shop.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

abstract class ShopShowcaseListImageBaseViewHolder(
        itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_showcase_list_image
    }

    var tvShowcaseName: Typography? = null
    var tvShowcaseCount: Typography? = null
    var ivShowcaseImage: ImageUnify? = null
    var showcaseCampaignLabel: Label? = null

    abstract var showcaseActionButton: Any?

    abstract fun bind(element: Any)

    init {
        tvShowcaseName = itemView.findViewById(R.id.tvShowcaseName)
        tvShowcaseCount = itemView.findViewById(R.id.tvShowcaseCount)
        ivShowcaseImage = itemView.findViewById(R.id.ivShowcaseImage)
        showcaseCampaignLabel = itemView.findViewById(R.id.showcaseCampaignLabel)
    }

    fun isShowCampaignLabel(type: Int): Boolean {
        return when (type) {
            ShopEtalaseTypeDef.ETALASE_CAMPAIGN,
            ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN -> true
            else -> false
        }
    }

    fun getCampaignLabelTitle(type: Int): String {
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