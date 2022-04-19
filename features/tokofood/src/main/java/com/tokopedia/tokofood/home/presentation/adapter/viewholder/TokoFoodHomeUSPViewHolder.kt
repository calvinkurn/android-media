package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.media.Image
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TokoFoodHomeUSPViewHolder(
    itemView: View
): AbstractViewHolder<TokoFoodHomeUSPUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_usp

        private const val USP_SIZE = 3
        private const val GO_FOOD_IMAGE = "https://images.tokopedia.net/img/ic_tokofood_gofood.png"
    }

    private var imgFirstUsp: ImageUnify? = null
    private var imgSecondUsp: ImageUnify? = null
    private var imgThirdUsp: ImageUnify? = null
    private var imgGoFood: ImageView? = null
    private var tgFirstUsp: Typography? = null
    private var tgSecondUsp: Typography? = null
    private var tgThirdUsp: Typography? = null

    override fun bind(element: TokoFoodHomeUSPUiModel) {
        imgFirstUsp = itemView.findViewById(R.id.img_first_usp)
        imgSecondUsp = itemView.findViewById(R.id.img_second_usp)
        imgThirdUsp = itemView.findViewById(R.id.img_third_usp)
        imgGoFood = itemView.findViewById(R.id.img_gofood)
        tgFirstUsp = itemView.findViewById(R.id.tg_first_usp)
        tgSecondUsp = itemView.findViewById(R.id.tg_second_usp)
        tgThirdUsp = itemView.findViewById(R.id.tg_third_usp)

        element.uspModel?.response?.list?.let {
            if (it.size == USP_SIZE) {
                imgFirstUsp?.loadImage(it.get(0).iconUrl)
                imgSecondUsp?.loadImage(it.get(1).iconUrl)
                imgThirdUsp?.loadImage(it.get(2).iconUrl)
                imgGoFood?.loadImage(GO_FOOD_IMAGE)

                tgFirstUsp?.text = it.get(0).title
                tgSecondUsp?.text = it.get(1).title
                tgThirdUsp?.text = it.get(2).title
            }
        }
    }
}