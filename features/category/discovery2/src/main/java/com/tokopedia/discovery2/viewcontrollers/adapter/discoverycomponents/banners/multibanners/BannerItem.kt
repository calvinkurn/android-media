package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.content.Context
import com.tokopedia.unifycomponents.ImageUnify
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.Utils

class BannerItem(val bannerItemData: DataItem, val constraintLayout: ConstraintLayout,
                 val constraintSet: ConstraintSet, val viewWidth: Int? = Utils.DEFAULT_BANNER_WIDTH, val viewHeight: Int? = Utils.DEFAULT_BANNER_HEIGHT, val index: Int,
                 val previousBannerItem: BannerItem?, val context: Context, val islastItem: Boolean) {

    val bannerImageView = ImageUnify(context)
    private val VIEW_ID_CONSTANT = 100

    init {
        bannerImageView.id = VIEW_ID_CONSTANT + index;
        addImageConstrains()
    }

    private fun addImageConstrains() {

        constraintLayout.addView(bannerImageView)
        constraintSet.clear(bannerImageView.id)
        constraintSet.constrainWidth(bannerImageView.getId(), ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(bannerImageView.getId(), ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.setDimensionRatio(bannerImageView.id, "H, $viewWidth : $viewHeight")
        constraintSet.setHorizontalWeight(bannerImageView.id, 1.0F)

        if (previousBannerItem == null) {
            constraintSet.connect(bannerImageView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,
                    bannerItemData.leftMargin)
        } else {
            constraintSet.connect(previousBannerItem.bannerImageView.id, ConstraintSet.END, bannerImageView.id, ConstraintSet.START,
                    previousBannerItem.bannerItemData.rightMargin)
            constraintSet.connect(bannerImageView.id, ConstraintSet.START, previousBannerItem.bannerImageView.id, ConstraintSet.END,
                    bannerItemData.leftMargin)
        }

        if (islastItem) {
            constraintSet.connect(bannerImageView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,
                    bannerItemData.rightMargin)
        }
        constraintSet.connect(bannerImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        bannerImageView.setImageUrl(bannerItemData.imageUrlDynamicMobile ?: "")
        constraintSet.applyTo(constraintLayout)
    }
}