package com.tokopedia.shop.settings.basicinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.model.ShopEditTickerModel
import com.tokopedia.shop.settings.common.util.getTextWithSpannable
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_shop_edit_ticker.view.*

class ShopEditTickerViewHolder(
        view: View,
        private val listener: ShopEditTickerListener?
) : AbstractViewHolder<ShopEditTickerModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_edit_ticker
        val POSITION = 1
    }

    override fun bind(model: ShopEditTickerModel) {
        showShopEditShopInfoTicker(model)
    }

    private fun showShopEditShopInfoTicker(model: ShopEditTickerModel) {
        val isNameAllowed = model.isNameAllowed
        val isDomainAllowed = model.isDomainAllowed
        val reasonNameNotAllowed = model.reasonNameNotAllowed
        val reasonDomainNotAllowed = model.reasonDomainNotAllowed
        when {
            isNameAllowed && isDomainAllowed -> showWarningTicker()
            isNameAllowed && !isDomainAllowed -> showDomainNotAllowedTicker(reasonDomainNotAllowed)
            isDomainAllowed && !isNameAllowed -> showNameNotAllowedTicker(reasonNameNotAllowed)
            else -> showNameAndDomainNotAllowedTicker()
        }
    }

    private fun showWarningTicker() {
        val description = getString(R.string.ticker_warning_can_only_change_shopname_once)
        val readMore = getString(R.string.ticker_warning_read_more)
        val color = ContextCompat.getColor(itemView.context, R.color.merchant_green)
        val message = getTextWithSpannable(color, description, readMore).toString()
        showShopTicker(message, Ticker.TYPE_WARNING)
    }

    private fun showDomainNotAllowedTicker(message: String) {
        showShopTicker(message, Ticker.TYPE_INFORMATION)
    }

    private fun showNameNotAllowedTicker(message: String) {
        showShopTicker(message, Ticker.TYPE_INFORMATION)
    }

    private fun showNameAndDomainNotAllowedTicker() {
        val message = getString(R.string.shop_edit_change_name_and_domain_not_allowed)
        showShopTicker(message, Ticker.TYPE_INFORMATION)
    }

    private fun showShopTicker(message: String, type: Int) {
        itemView.shopEditTicker.tickerType = type
        itemView.shopEditTicker.setTextDescription(message)
        if (type == Ticker.TYPE_WARNING) {
            itemView.shopEditTicker?.run {
                tickerType = type
                setTextDescription(message)
                setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        listener?.onReadMoreClick()
                    }
                    override fun onDismiss() {}
                })
            }
        }
        itemView.shopEditTicker.show()
    }

    interface ShopEditTickerListener {
        fun onReadMoreClick()
    }
}