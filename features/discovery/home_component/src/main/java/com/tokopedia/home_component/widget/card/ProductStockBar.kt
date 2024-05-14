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

    fun shouldShowStockBar(isStockBarSupported: Boolean, model: SmallProductModel.StockBar) {
        if (!isStockBarSupported) {
            hideStockBar()
            return
        }

        view.post {
            val width = view.measuredWidth * model.percentage / 100
            setStockBarActiveLength(width)

            when (model.shouldHandleFireIconVisibility()) {
                is SmallProductModel.StockBar.Type.Inactive -> showInactiveStockBar()
                is SmallProductModel.StockBar.Type.ActiveWithoutFire -> showStockBar()
                is SmallProductModel.StockBar.Type.ActiveWithFire -> showStockBarWithFire()
            }
        }
    }

    private fun setStockBarActiveLength(size: Int) {
        stockBarActive.setLayoutWidth(size)
    }

    private fun showStockBar() {
        stockBarActive.setBackgroundResource(R.drawable.bg_spc_stockbar_shape_foreground)

        stockBarActive.show()
        stockBarInActive.show()
        icFire.gone()

        stockBarActive.requestLayout()
    }

    private fun showStockBarWithFire() {
        stockBarActive.setBackgroundResource(R.drawable.bg_spc_stockbar_foreground)

        stockBarActive.show()
        stockBarInActive.show()
        icFire.show()

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
