package com.tokopedia.inbox.universalinbox.stub.common

import com.tokopedia.inbox.universalinbox.stub.common.util.AndroidFileUtil
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMiscMapper
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsBannerViewHolder.Companion.DIMEN_ID
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.topads.sdk.TopAdsConstants
import com.tokopedia.topads.sdk.domain.model.TopAdsBannerResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import javax.inject.Inject

class UniversalInboxMiscMapperStub @Inject constructor() : UniversalInboxMiscMapper() {

    override fun getTopAdsUiModel(): UniversalInboxTopAdsBannerUiModel {
        val response = AndroidFileUtil.parse<TopAdsBannerResponse>(
            "topads/success_get_topads_banner.json",
            TopAdsBannerResponse::class.java
        )
        val topAdsViewModel = mapToTopAdsViewModel(response)
        return UniversalInboxTopAdsBannerUiModel(
            ads = topAdsViewModel,
            requested = true
        )
    }

    private fun mapToTopAdsViewModel(
        responseBanner: TopAdsBannerResponse
    ): List<TopAdsImageViewModel> {
        val list = ArrayList<TopAdsImageViewModel>()
        responseBanner.topadsDisplayBannerAdsV3.bannerListData?.forEach { data ->
            val model = TopAdsImageViewModel()
            val image = getImageById(data.banner?.images)
            with(model) {
                bannerId = data.id
                bannerName = data.banner?.name ?: ""
                position = data.banner?.position ?: 0
                ImpressHolder = data.banner?.shop?.shopImage
                layoutType = data.banner?.layoutType ?: TopAdsConstants.TdnBannerConstants.TYPE_SINGLE
                adClickUrl = data.adClickUrl ?: ""
                adViewUrl = data.adViewUrl ?: ""
                applink = data.applinks
                imageUrl = image.first
                imageWidth = image.second
                imageHeight = image.third
                isAutoScrollEnabled =
                    responseBanner.topadsDisplayBannerAdsV3.header?.autoScroll?.enable ?: false
                scrollDuration =
                    responseBanner.topadsDisplayBannerAdsV3.header?.autoScroll?.timer?.times(1000)
                        ?: 0
                nextPageToken =
                    responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.nextPageToken
                shopId = data.banner?.shop?.shopID?.toString() ?: ""
                currentPage =
                    responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.currentPage ?: ""
                kind = responseBanner.topadsDisplayBannerAdsV3.header?.pagination?.kind ?: ""
            }
            list.add(model)
        }
        return list
    }

    private fun getImageById(
        images: List<TopAdsBannerResponse.TopadsDisplayBannerAdsV3.BannerListData.Banner.Image>?
    ): Triple<String, Int, Int> {
        var imageUrl = ""
        var imageWidth = 0
        var imageHeight = 0

        images?.let {
            for (image in it) {
                if (image.dimension?.id == DIMEN_ID.toString()) {
                    imageUrl = image.url ?: ""
                    imageWidth = image.dimension?.width ?: 0
                    imageHeight = image.dimension?.height ?: 0
                    break
                }
            }
        }
        return Triple(imageUrl, imageWidth, imageHeight)
    }
}
