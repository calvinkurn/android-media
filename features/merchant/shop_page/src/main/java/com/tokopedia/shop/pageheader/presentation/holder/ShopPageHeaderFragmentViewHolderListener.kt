package com.tokopedia.shop.pageheader.presentation.holder

import com.tokopedia.creation.common.presentation.bottomsheet.ContentCreationBottomSheet
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestResult
import com.tokopedia.shop.pageheader.presentation.bottomsheet.ShopPageHeaderRequestUnmoderateBottomSheet
import com.tokopedia.stories.widget.StoriesWidgetManager

interface ShopPageHeaderFragmentViewHolderListener {
    fun onFollowerTextClicked(shopFavourited: Boolean)
    fun setFollowStatus(isFollowing: Boolean)
    fun onShopCoverClicked(isOfficial: Boolean, isPowerMerchant: Boolean)
    fun onShopStatusTickerClickableDescriptionClicked(linkUrl: CharSequence)
    fun openShopInfo()
    fun onStartLiveStreamingClicked()
    fun saveFirstTimeVisit()
    fun isFirstTimeVisit(): Boolean?
    fun onSendRequestOpenModerate(optionValue: String)
    fun onCompleteSendRequestOpenModerate()
    fun onCompleteCheckRequestModerateStatus(moderateStatusResult: ShopModerateRequestResult)
    fun setShopUnmoderateRequestBottomSheet(bottomSheetPageHeader: ShopPageHeaderRequestUnmoderateBottomSheet)
    fun onClickShopBasicInfoSection(appLink: String){}
    fun onShopReviewClicked(appLink: String){}
    fun onChatButtonClicked(){}

    fun onFollowButtonClicked(){}

    fun onUspClicked(listDynamicUspValue: List<String>) {}

    fun getStoriesWidgetManager(): StoriesWidgetManager

    fun getContentCreationListener(): ContentCreationBottomSheet.Listener
}
