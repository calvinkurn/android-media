package com.tokopedia.autocompletecomponent.universal.analytics

interface UniversalEventTracking {
    interface Event {
        companion object {
            const val PRODUCT_VIEW = "productView"
            const val PRODUCT_CLICK = "productClick"
        }
    }

    interface Category {
        companion object {
            const val SEARCH_RESULT = "search result"
        }
    }

    interface Action {
        companion object {
            const val IMPRESSION_CAROUSEL_PRODUCT = "impression - carousel product"
            const val CLICK_CAROUSEL_PRODUCT = "click - carousel product"
        }
    }
}