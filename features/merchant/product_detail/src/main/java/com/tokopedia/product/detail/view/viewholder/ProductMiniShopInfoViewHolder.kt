package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniShopInfoDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_mini_shop_info.view.*

/**
 * Created by Yehezkiel on 20/05/20
 */
class ProductMiniShopInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMiniShopInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_shop_info
    }

    override fun bind(element: ProductMiniShopInfoDataModel) {
        if (!element.isOS && !element.isGoldMerchant || element.shopName.isEmpty()) {
            view.setPadding(0, 0, 0, 0)
            view.layoutParams.height = 0
        } else {
            view.shop_name_container.show()
            view.setOnClickListener { listener.onMiniShopInfoClicked(getComponentTrackData(element)) }
            view.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
            view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            showOfficialStore(element.isGoldMerchant, element.isOS, element.shopName)
        }
    }

    private fun showOfficialStore(isGoldMerchant: Boolean, isOfficialStore: Boolean, shopname: String) = with(view) {
        if (isGoldMerchant && !isOfficialStore) {
            txt_mini_shop_desc.hide()
            shop_badge.show()
            shop_badge.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_power_merchant))
        } else if (isOfficialStore) {
            txt_mini_shop_desc.show()
            shop_badge.show()
            shop_badge.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_pdp_new_official_store))
        } else if (!isGoldMerchant && !isOfficialStore) {
            shop_badge.hide()
            txt_mini_shop_desc.hide()
        }

        renderShopTitle(shopname)
    }

    private fun renderShopTitle(shopname: String) = with(view) {
        val boldTypeface = com.tokopedia.unifyprinciples.getTypeface(context, "RobotoBold.ttf")
        txt_mini_shop_title.typeface = boldTypeface
        txt_mini_shop_title.text = MethodChecker.fromHtml(shopname)
        txt_mini_shop_title.show()
    }

    private fun getComponentTrackData(element: ProductMiniShopInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}