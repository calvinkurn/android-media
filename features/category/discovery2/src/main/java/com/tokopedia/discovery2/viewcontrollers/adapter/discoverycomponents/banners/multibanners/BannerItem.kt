package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.multibanners

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.unifycomponents.ImageUnify

private const val VIEW_ID_CONSTANT = 100

class BannerItem(val bannerItemData: DataItem, private val constraintLayout: ConstraintLayout,
                 private val constraintSet: ConstraintSet, private val viewWidth: Int? = Utils.DEFAULT_BANNER_WIDTH,
                 private val viewHeight: Int? = Utils.DEFAULT_BANNER_HEIGHT, private val index: Int,
                 private val previousBannerItem: BannerItem?, val context: Context, private val islastItem: Boolean) {

    var bannerImageView = View(context)

    init {
        addImageConstrains()
    }

    private fun addImageConstrains() {
        if (!bannerItemData.imageUrlDynamicMobile.isNullOrEmpty()) {
            bannerImageView = ImageUnify(context)
            (bannerImageView as ImageUnify).cornerRadius = 0
        }
        bannerImageView.id = VIEW_ID_CONSTANT + index
        constraintLayout.addView(bannerImageView)
        constraintSet.clear(bannerImageView.id)
        constraintSet.constrainWidth(bannerImageView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(bannerImageView.id, ConstraintSet.MATCH_CONSTRAINT)
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
        if (!bannerItemData.imageUrlDynamicMobile.isNullOrEmpty()) {
            try {
                if(context.isValidGlideContext())
                    (bannerImageView as ImageUnify).setImageUrl(bannerItemData.imageUrlDynamicMobile)
            } catch (e: Throwable) { }
        }
        constraintSet.applyTo(constraintLayout)
    }
}