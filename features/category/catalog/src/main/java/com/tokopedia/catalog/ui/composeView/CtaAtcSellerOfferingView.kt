package com.tokopedia.catalog.ui.composeView

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.nest.principles.ui.NestTheme

class CtaAtcSellerOfferingView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var shopNameState = mutableStateOf(String.EMPTY)
    private var badgeState = mutableStateOf(String.EMPTY)
    private var priceState = mutableStateOf(String.EMPTY)
    private var slashPriceState = mutableStateOf(String.EMPTY)
    private var ratingState = mutableStateOf(String.EMPTY)
    private var soldState = mutableStateOf(String.EMPTY)
    private var themeState = mutableStateOf(false)
    private var onClick: (() -> Unit)? = null

    init {
        val composeview = ComposeView(context)
        composeview.setContent {
            NestTheme {
                CtaSellerOffering(
                    shopNameState.value,
                    badgeState.value,
                    priceState.value,
                    slashPriceState.value,
                    ratingState.value,
                    soldState.value,
                    themeState.value,
                    onClick
                )
            }
        }
        addView(composeview)
    }


    fun setShopName(shopName: String) {
        shopNameState.value = shopName
    }

    fun setBadge(badge: String) {
        badgeState.value = badge
    }

    fun setPrice(price: String) {
        priceState.value = price
    }

    fun setSlashPrice(slashPrice: String) {
        slashPriceState.value = slashPrice
    }

    fun setRating(rating: String) {
        ratingState.value = rating
    }

    fun setSold(sold: String) {
        soldState.value = sold
    }

    fun setTheme(isDarkTheme: Boolean) {
        themeState.value = isDarkTheme
    }

    fun setOnClick(onClick: () -> Unit) {
        this.onClick = onClick
    }
}



