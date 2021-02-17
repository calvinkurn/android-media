package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.circularsliderbanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularListener
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularModel
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewHolder
import com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager.CircularViewPagerAdapter
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

private const val VIEW_ID_CONSTANT = 100

class BannerCircularAdapter(itemList: List<CircularModel>, listener: CircularListener) : CircularViewPagerAdapter(itemList, listener) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        return SliderBannerItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.circular_slide_banner_item_layout, parent, false))
    }
}

class SliderBannerItemViewHolder(itemView: View) : CircularViewHolder(itemView) {
    private var constraintLayout: ConstraintLayout = itemView.findViewById(R.id.parent_layout)
    private val constraintSet = ConstraintSet()
    private val bannerImageView = ImageUnify(itemView.context)
    override fun bind(item: CircularModel, listener: CircularListener) {
        constraintLayout.removeAllViews()
        createImageViewConstrains(item)
        bannerImageView.loadImage(item.url) {
            setPlaceHolder(R.color.grey_1100)
            setErrorDrawable(R.color.grey_1100)
        }
        constraintSet.applyTo(constraintLayout)
        bannerImageView.setOnClickListener {
            listener.onClick(adapterPosition)
        }
    }

    private fun createImageViewConstrains(item: CircularModel) {
        bannerImageView.cornerRadius = 0
        bannerImageView.id = VIEW_ID_CONSTANT
        constraintLayout.addView(bannerImageView)
        constraintSet.clear(bannerImageView.id)
        constraintSet.constrainWidth(bannerImageView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(bannerImageView.id, ConstraintSet.MATCH_CONSTRAINT)
        constraintSet.setDimensionRatio(bannerImageView.id, "H, ${Utils.extractDimension(item.url, "width")} : ${Utils.extractDimension(item.url)}")
        constraintSet.setHorizontalWeight(bannerImageView.id, 1.0F)
        constraintSet.connect(bannerImageView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(bannerImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(bannerImageView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
    }
}
