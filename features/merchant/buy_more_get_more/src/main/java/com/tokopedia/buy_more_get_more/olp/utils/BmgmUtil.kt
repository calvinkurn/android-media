package com.tokopedia.buy_more_get_more.olp.utils

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.bmsm_widget.domain.entity.MainProduct
import com.tokopedia.buy_more_get_more.olp.utils.constant.Constant
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.helper.CartDataHelper
import com.tokopedia.cart.view.mapper.CartUiModelMapper
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget

object BmgmUtil {
    fun loadImageWithEmptyTarget(
        context: Context,
        url: String,
        properties: Properties.() -> Unit,
        mediaTarget: MediaBitmapEmptyTarget<Bitmap>
    ) {
        com.tokopedia.media.loader.loadImageWithEmptyTarget(context, url, properties, mediaTarget)
    }

    fun getGiftListMainProducts(
        cartDataList: ArrayList<Any>,
        offerId: Long,
        shopId: String,
        offerTypeId: Long = Constant.BMSM_GWP_OFFER_TYPE
    ): List<MainProduct> {
        val cartItem = cartDataList.filterIsInstance<CartItemHolderData>()
        val cartStringOrder = cartItem.firstOrNull {
            it.shopHolderData.shopId == shopId
        }?.cartStringOrder.orEmpty()

        return CartDataHelper.getListProductByOfferIdAndCartStringOrder(
            cartDataList,
            offerId,
            cartStringOrder
        )
            .filter { it.isSelected && it.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerTypeId == offerTypeId }
            .map { MainProduct(it.productId.toLongOrZero(), it.quantity) }
    }

    fun CartData.toCartDataList(): List<Any> {
        return CartUiModelMapper.mapAvailableGroupUiModel(this)
    }
}
