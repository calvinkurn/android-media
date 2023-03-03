package com.tokopedia.minicart.common.simplified

import android.content.Intent
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

interface MiniCartSimplifiedWidgetListener : MiniCartWidgetListener {

    /**
     * Callback when user click Check Cart Button
     * Holder fragment needs to route with given intent
     *
     * @param intent intent to Cart Page
     * @param isPromoValid returns true when promo is valid & applied
     */
    fun onClickCheckCart(intent: Intent, isPromoValid: Boolean)
}
