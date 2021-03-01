package com.tokopedia.shop.pageheader.presentation.holder

import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestResult
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopRequestUnmoderateBottomSheet

interface ShopPageFragmentViewHolderListener {
    fun onFollowerTextClicked(shopFavourited: Boolean)
    fun toggleFavorite(isFavourite: Boolean)
    fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean)
    fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence)
    fun openShopInfo()
    fun onStartLiveStreamingClicked()
    fun onSendRequestOpenModerate(optionValue : String)
    fun onCompleteSendRequestOpenModerate()
    fun onCompleteCheckRequestModerateStatus(moderateStatusResult : ShopModerateRequestResult)
    fun setShopUnmoderateRequestBottomSheet(bottomSheet: ShopRequestUnmoderateBottomSheet)
}