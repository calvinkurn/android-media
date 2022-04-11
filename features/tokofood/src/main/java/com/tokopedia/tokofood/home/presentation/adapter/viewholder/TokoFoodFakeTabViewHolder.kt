package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodFakeTabUiModel

class TokoFoodFakeTabViewHolder(
    itemView: View
): AbstractViewHolder<TokoFoodFakeTabUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_fake_tab
    }

    var imgNyam: ImageView? = null
    var imgTokoFood: ImageView? = null

    override fun bind(element: TokoFoodFakeTabUiModel) {
        setupTokoFoodFakeTab(element)
    }

    private fun setupTokoFoodFakeTab(element: TokoFoodFakeTabUiModel) {
        imgNyam = itemView.findViewById(R.id.img_nyam_tab)
        imgTokoFood = itemView.findViewById(R.id.img_tokofood_tab)

        imgNyam?.loadImage(element.fakeTab.imgNyam)
        imgTokoFood?.loadImage(element.fakeTab.imgTokoFood)

        imgNyam?.setOnClickListener {

        }

        imgTokoFood?.setOnClickListener {

        }
    }
}