package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.CardButton

data class BroadMatchCardButton(val title: String = "", val applink: String = "") {

    companion object {
        fun create(cardButton: CardButton) =
            BroadMatchCardButton(
                cardButton.title,
                cardButton.applink,
            )
    }
}
