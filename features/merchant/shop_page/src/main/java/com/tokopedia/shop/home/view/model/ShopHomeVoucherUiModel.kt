package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.common.data.model.MerchantVoucherCouponUiModel
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

/**
 * Created by rizqiaryansa on 2020-02-21.
 */
data class ShopHomeVoucherUiModel(
        override val widgetId: String = "",
        override val layoutOrder: Int = -1,
        override val name: String = "",
        override val type: String = "",
        override val header: BaseShopHomeWidgetUiModel.Header = BaseShopHomeWidgetUiModel.Header(),
        val data: MerchantVoucherCouponUiModel? = null,
        val isError: Boolean = false
) : BaseShopHomeWidgetUiModel {

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}