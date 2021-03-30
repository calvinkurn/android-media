package com.tokopedia.shop.showcase.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.common.view.model.ShopEtalaseUiModel
import com.tokopedia.shop.common.view.viewholder.ShopShowcaseListImageBaseViewHolder

/**
 * Created by Rafli Syam on 30/03/2021
 */
class ShopShowcaseListImageViewHolder(
        itemView: View,
        private val listener: ShopShowcaseListImageListener
) : ShopShowcaseListImageBaseViewHolder(itemView) {

    // set to null since its buyer view
    override var showcaseActionButton: Any? = null

    override fun bind(element: Any) {

        renderShowcaseMainInfo(element)

        // showcase show campaign label condition
        val showcaseItem = element as ShopEtalaseUiModel
        showcaseCampaignLabel?.showWithCondition(isShowCampaignLabel(showcaseItem.type))
        showcaseCampaignLabel?.setLabel(getCampaignLabelTitle(showcaseItem.type))

        // showcase item impressed listener
        ivShowcaseImage?.addOnImpressionListener(
                holder = showcaseItem,
                listener = object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onShowcaseListItemImpressed(element, adapterPosition)
                    }
                }
        )

        // handle showcase item click listener
        setItemShowcaseClickListener {
            listener.onShowcaseListItemSelected(element, adapterPosition)
        }
    }
}

interface ShopShowcaseListImageListener {
    fun onShowcaseListItemSelected(element: ShopEtalaseUiModel, position: Int)
    fun onShowcaseListItemImpressed(element: ShopEtalaseUiModel, position: Int)
}