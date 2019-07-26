package com.tokopedia.shop.settings.etalase.view.viewholder

import android.support.v4.content.ContextCompat
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.util.*
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel

/**
 * Created by hendry on 16/08/18.
 */
class ShopEtalaseViewHolder(itemView: View,
                            private val onOnShopEtalaseViewHolderListener: OnShopEtalaseViewHolderListener?) : AbstractViewHolder<ShopEtalaseViewModel>(itemView) {

    private val ivMenuMore: View
    private val tvEtalaseName: TextView
    private val tvEtalaseCount: TextView
    private val boldColor: ForegroundColorSpan

    init {
        ivMenuMore = itemView.findViewById(R.id.ivMenuMore)
        tvEtalaseName = itemView.findViewById(R.id.tvEtalaseName)
        tvEtalaseCount = itemView.findViewById(R.id.tvEtalaseCount)
        boldColor = ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.design.R.color.font_black_primary_70))
    }

    interface OnShopEtalaseViewHolderListener {
        val keyword: String
        fun onIconMoreClicked(shopEtalaseViewModel: ShopEtalaseViewModel)
    }

    override fun bind(shopEtalaseViewModel: ShopEtalaseViewModel) {
        var keyword = ""
        if (onOnShopEtalaseViewHolderListener != null) {
            keyword = onOnShopEtalaseViewHolderListener.keyword
        }
        tvEtalaseName.text = getSpandableColorText(shopEtalaseViewModel.name, keyword, boldColor)
        tvEtalaseCount.text = tvEtalaseCount.context.getString(R.string.x_products, shopEtalaseViewModel.count)
        if (shopEtalaseViewModel.type == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
            ivMenuMore.setOnClickListener {
                onOnShopEtalaseViewHolderListener?.onIconMoreClicked(shopEtalaseViewModel)
            }
            ivMenuMore.visibility = View.VISIBLE
        } else { // etalase default cannot be editted/deleted
            ivMenuMore.visibility = View.GONE
        }
    }

    companion object {

        val LAYOUT = R.layout.item_shop_settings_etalase
    }

}
