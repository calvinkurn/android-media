package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine

import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_ICON_DRIFTING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_ICON_FUNNELING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_ICON_SEAMLESS_DRIFTING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_ICON_SEAMLESS_FUNNELING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_IMAGE_DRIFTING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_IMAGE_FUNNELING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_IMAGE_SEAMLESS_DRIFTING
import com.tokopedia.search.result.product.inspirationcarousel.TYPE_INSPIRATION_KEYWORD_IMAGE_SEAMLESS_FUNNELING

enum class LayoutType() {
    ICON_FUNNELING {
        override fun isFunneling(): Boolean = true
        override fun isIconKeyword(): Boolean = true
    },
    ICON_DRIFTING {
        override fun isIconKeyword(): Boolean = true
    },
    IMAGE_FUNNELING {
        override fun isFunneling(): Boolean = true
        override fun isSingleLine(): Boolean = true
    },
    IMAGE_DRIFTING {
        override fun isSingleLine(): Boolean = true
    },
    DEFAULT_SEAMLESS {
        override fun isGridLayout(): Boolean = false
    };

    open fun isGridLayout(): Boolean = true
    open fun isFunneling(): Boolean = false
    open fun isSingleLine(): Boolean = false
    open fun isIconKeyword(): Boolean = false

    companion object {
        fun getLayoutType(variant: String): LayoutType {
            return when (variant) {
                TYPE_INSPIRATION_KEYWORD_ICON_FUNNELING,
                TYPE_INSPIRATION_KEYWORD_ICON_SEAMLESS_FUNNELING -> ICON_FUNNELING
                TYPE_INSPIRATION_KEYWORD_IMAGE_FUNNELING,
                TYPE_INSPIRATION_KEYWORD_IMAGE_SEAMLESS_FUNNELING -> IMAGE_FUNNELING
                TYPE_INSPIRATION_KEYWORD_ICON_DRIFTING,
                TYPE_INSPIRATION_KEYWORD_ICON_SEAMLESS_DRIFTING -> ICON_DRIFTING
                TYPE_INSPIRATION_KEYWORD_IMAGE_DRIFTING,
                TYPE_INSPIRATION_KEYWORD_IMAGE_SEAMLESS_DRIFTING -> IMAGE_DRIFTING
                else -> DEFAULT_SEAMLESS
            }
        }
    }
}
