package com.tokopedia.home_component.widget.card

import android.view.View
import android.widget.ImageView
import com.tokopedia.home_component.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setLayoutWidth
import com.tokopedia.kotlin.extensions.view.show

class ProductStockBar(private val view: View) {

    private val stockBarActive = view.findViewById<View>(R.id.stockbar_active)
    private val stockBarInActive = view.findViewById<View>(R.id.stockbar_inactive)
    private val icFire = view.findViewById<ImageView>(R.id.ic_fire)

    init {
        stockBarActive.setBackgroundResource(R.drawable.bg_spc_stockbar_foreground)
    }

    fun shouldShowStockBar(isStockBarSupported: Boolean, model: SmallProductModel.StockBar) {
        if (!isStockBarSupported) {
            hideStockBar()
            return
        }

        view.post {
            val forceMaxPercentageThreshold = shouldSetMaxPercentageForcibly(model.percentage)
            val width = view.measuredWidth * forceMaxPercentageThreshold / 100

            setStockBarActiveLength(width)

            when (forceMaxPercentageThreshold) {
                0 -> showInactiveStockBar()
                in 1 until SmallProductModel.StockBar.MIN_THRESHOLD -> showStockBarWithoutFire()
                else -> showStockBarWithFire()
            }
        }
    }

    private fun shouldSetMaxPercentageForcibly(percentage: Int): Int {
        val maxValue = SmallProductModel.StockBar.MAX_THRESHOLD

        return if (percentage > maxValue) {
            maxValue
        } else {
            percentage
        }
    }

    private fun setStockBarActiveLength(size: Int) {
        stockBarActive.setLayoutWidth(size)
    }

    private fun showStockBarWithFire() {
        stockBarActive.show()
        stockBarInActive.show()
        icFire.show()

        stockBarActive.requestLayout()
    }

    private fun showStockBarWithoutFire() {
        stockBarActive.show()
        stockBarInActive.show()
        icFire.gone()

        stockBarActive.requestLayout()
    }

    private fun showInactiveStockBar() {
        stockBarInActive.show()
        stockBarActive.gone()
        icFire.gone()
    }

    private fun hideStockBar() {
        stockBarActive.gone()
        stockBarInActive.gone()
        icFire.gone()
    }
}
