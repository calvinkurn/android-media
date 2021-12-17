package com.tokopedia.sellerhome.view.viewhelper

import android.view.View
import android.view.WindowInsets
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav.LottieBottomNav

class SellerHomeOnApplyInsetsListener(
    private val sahContainer: View?,
    private val sahBottomNav: LottieBottomNav?
) : View.OnApplyWindowInsetsListener {
    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        sahContainer?.let { sahContainer ->
            val sahContainerPaddingBottom = insets.systemWindowInsetBottom.orZero() - sahBottomNav?.measuredHeight.orZero()
            sahContainer.setPadding(0, 0, 0, sahContainerPaddingBottom.coerceAtLeast(0))
        }
        return insets.consumeSystemWindowInsets()
    }
}