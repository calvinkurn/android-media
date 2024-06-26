package com.tokopedia.carouselproductcard.paging

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.tokopedia.carouselproductcard.R

internal class AttributesConfig {
    var showPagingIndicator: Boolean = false
        private set

    var pagingPaddingHorizontal: Int = 0
        private set

    var itemWidthPercentage: Float = DEFAULT_ITEM_WIDTH_PERCENTAGE
        private set

    fun load(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CarouselPagingProductCardView,
            0,
            0
        )

        try {
            loadConfigFromTypedArray(typedArray)
        } finally {
            typedArray.recycle()
        }
    }

    private fun loadConfigFromTypedArray(typedArray: TypedArray) {
        showPagingIndicator = typedArray.getBoolean(
            R.styleable.CarouselPagingProductCardView_showPagingIndicator,
            DEFAULT_SHOW_PAGING_INDICATOR
        )

        pagingPaddingHorizontal = typedArray.getDimensionPixelSize(
            R.styleable.CarouselPagingProductCardView_pagingPaddingHorizontal,
            DEFAULT_PAGING_PADDING_HORIZONTAL_PX
        )

        itemWidthPercentage = typedArray.getFloat(
            R.styleable.CarouselPagingProductCardView_itemWidthPercentage,
            DEFAULT_ITEM_WIDTH_PERCENTAGE
        )
    }

    companion object {
        private const val DEFAULT_SHOW_PAGING_INDICATOR = true
        private const val DEFAULT_PAGING_PADDING_HORIZONTAL_PX = 0
        private const val DEFAULT_ITEM_WIDTH_PERCENTAGE = 0.75f
    }
}
