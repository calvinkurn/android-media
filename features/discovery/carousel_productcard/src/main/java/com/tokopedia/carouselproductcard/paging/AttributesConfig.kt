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
    }

    companion object {
        private const val DEFAULT_SHOW_PAGING_INDICATOR = true
        private const val DEFAULT_PAGING_PADDING_HORIZONTAL_PX = 0
    }
}
