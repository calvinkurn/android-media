package com.tokopedia.pdpsimulation.common.helper

import androidx.viewpager.widget.ViewPager
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

sealed class PaymentMode
object PayLater : PaymentMode()
object CreditCard : PaymentMode()

fun ViewPager.onPageSelected(action: (Int) -> Unit) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            action.invoke(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    })
}

fun Ticker.onLinkClickedEvent(action: (CharSequence) -> Unit) {
    this.setDescriptionClickEvent(object : TickerCallback {
        override fun onDescriptionViewClick(linkUrl: CharSequence) {
            action.invoke(linkUrl)
        }

        override fun onDismiss() {
        }
    })
}
