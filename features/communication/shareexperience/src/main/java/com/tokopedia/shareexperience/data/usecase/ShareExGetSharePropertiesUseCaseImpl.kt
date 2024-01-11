package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.ShareExBottomSheetResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExGenerateLinkPropertiesResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExPropertyResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExShareBodyResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExSharePropertiesResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExWrapperResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateEligibilityResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateRegistrationWidgetResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorArgResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExPropertyImageGeneratorResponseDto
import com.tokopedia.shareexperience.data.mapper.ShareExPropertyMapper
import com.tokopedia.shareexperience.data.query.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.asFlowResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExDiscoveryBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExShopBottomSheetRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetSharePropertiesUseCaseImpl @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val mapper: ShareExPropertyMapper,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetSharePropertiesUseCase {

    private val sharePropertiesQuery = ShareExGetSharePropertiesQuery()

    override fun getDefaultData(
        defaultUrl: String,
        defaultImageUrl: String
    ): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return flow {
            val result = mapper.mapDefault(defaultUrl, defaultImageUrl)
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    override suspend fun getData(params: ShareExProductBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return getShareBottomSheetResponse(params)
    }

    override suspend fun getData(params: ShareExShopBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return getShareBottomSheetResponse(params)
    }

    override suspend fun getData(params: ShareExDiscoveryBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return getShareBottomSheetResponse(params)
    }

    private suspend fun getShareBottomSheetResponse(params: ShareExBottomSheetRequest): Flow<ShareExResult<ShareExBottomSheetModel>> {
        return flow {
            val request = getRequest(params)
//            dto = repository.request<ShareExBottomSheetWrapperRequest, ShareExWrapperResponseDto>(
//                sharePropertiesQuery,
//                request
//            )
            val dto = getDummyResponseDto()
            val result = mapper.map(dto.response.bottomSheet)
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    private fun getRequest(params: ShareExBottomSheetRequest): ShareExBottomSheetWrapperRequest {
        return ShareExBottomSheetWrapperRequest(params)
    }

    private fun getDummyResponseDto(): ShareExWrapperResponseDto {
        return ShareExWrapperResponseDto(
            ShareExSharePropertiesResponseDto(
                ShareExBottomSheetResponseDto(
                    title = "Bagikan ke teman kamu",
                    subtitle = "Mau bagi halaman yang mana?",
                    properties = getProperties()
                )
            )
        )
    }

    private fun getProperties(): List<ShareExPropertyResponseDto> {
        return listOf(
            ShareExPropertyResponseDto(
                chipTitle = "Halaman Toko",
                shareBody = ShareExShareBodyResponseDto(
                    title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    thumbnailUrls = getImageThumbnails(5)
                ),
                affiliateRegistrationWidget = ShareExAffiliateRegistrationWidgetResponseDto(
                    icon = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                    title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    description = "Daftar dan raih komisi Rp16.000/barang terjual dengan bagikan link ini.",
                    label = "BARU",
                    link = "tokopedia://topchat"
                ),
                affiliateEligibility = null,
                imageGeneratorPayload = getShareExPropertyImageGeneratorResponse(),
                generateLinkProperties = getGenerateLinkProperties()
            ),
            ShareExPropertyResponseDto(
                chipTitle = "Semua Produk",
                shareBody = ShareExShareBodyResponseDto(
                    title = "Lorem ipsum dolor sit amet.",
                    thumbnailUrls = getImageThumbnails(3)
                ),
                affiliateRegistrationWidget = ShareExAffiliateRegistrationWidgetResponseDto(
                    icon = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                    title = "Tokopedia Affiliate",
                    description = "Daftar dan raih komisi Rp16.000/barang terjual dengan bagikan link ini.",
                    label = "BARU",
                    link = ""
                ),
                affiliateEligibility = ShareExAffiliateEligibilityResponseDto(
                    commission = "<b>Komisi Rp16.000</b> / barang dijual",
                    badge = "Komisi Extra",
                    expiredDate = "Hingga 31 Des 2024"
                ),
                imageGeneratorPayload = getShareExPropertyImageGeneratorResponse(),
                generateLinkProperties = getGenerateLinkProperties()
            ),
            ShareExPropertyResponseDto(
                chipTitle = "Etalase",
                shareBody = ShareExShareBodyResponseDto(
                    title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    thumbnailUrls = getImageThumbnails(1)
                ),
                affiliateRegistrationWidget = ShareExAffiliateRegistrationWidgetResponseDto(
                    icon = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                    title = "Tokopedia Affiliate",
                    description = "Daftar dan raih komisi Rp16.000/barang terjual dengan bagikan link ini.",
                    label = "BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU BARU",
                    link = "tokopedia://topchat"
                ),
                affiliateEligibility = ShareExAffiliateEligibilityResponseDto(
                    commission = "<b>Komisi Rp16.000</b> / barang dijual"
                ),
                imageGeneratorPayload = getShareExPropertyImageGeneratorResponse(),
                generateLinkProperties = getGenerateLinkProperties()
            ),
            ShareExPropertyResponseDto(
                chipTitle = "Feed",
                shareBody = ShareExShareBodyResponseDto(
                    title = "",
                    thumbnailUrls = getImageThumbnails(0)
                ),
                affiliateRegistrationWidget = ShareExAffiliateRegistrationWidgetResponseDto(),
                affiliateEligibility = null,
                imageGeneratorPayload = getShareExPropertyImageGeneratorResponse(),
                generateLinkProperties = getGenerateLinkProperties()
            )
        )
    }

    private fun getImageThumbnails(imageCount: Int): List<String> {
        return when (imageCount) {
            1 -> listOf("https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png")
            2 -> listOf(
                "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/10/18/999822bb-f444-4602-9203-1643d3f2393a.jpg"
            )
            3 -> listOf(
                "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/10/18/999822bb-f444-4602-9203-1643d3f2393a.jpg",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/7/13/a0a46e5a-c3de-4c73-b25f-35d8a52dafc3.jpg"
            )
            4 -> listOf(
                "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/10/18/999822bb-f444-4602-9203-1643d3f2393a.jpg",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/7/13/a0a46e5a-c3de-4c73-b25f-35d8a52dafc3.jpg",
                "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/5/31/82f8d445-615a-49d0-a567-bc9ab6f75195.png"
            )
            5 -> listOf(
                "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/6/29/2aa21dbc-42e5-45e8-9fc1-3c0a0eca67d4.png",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/10/18/999822bb-f444-4602-9203-1643d3f2393a.jpg",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2022/7/13/a0a46e5a-c3de-4c73-b25f-35d8a52dafc3.jpg",
                "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2023/5/31/82f8d445-615a-49d0-a567-bc9ab6f75195.png",
                "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2022/7/6/69b75de5-b8b1-4d3f-9b10-3a1db5e1913a.jpg"
            )
            else -> listOf()
        }
    }

    private fun getShareExPropertyImageGeneratorResponse(): ShareExPropertyImageGeneratorResponseDto {
        return ShareExPropertyImageGeneratorResponseDto(
            sourceId = "RKdhUE",
            args = listOf(
                ShareExPropertyImageGeneratorArgResponseDto("product_id", "2150932863"),
                ShareExPropertyImageGeneratorArgResponseDto("product_price", "600001.000000"),
                ShareExPropertyImageGeneratorArgResponseDto("product_rating", "0"),
                ShareExPropertyImageGeneratorArgResponseDto("product_title", "Kitchin Sukēru P2P-001"),
                ShareExPropertyImageGeneratorArgResponseDto("is_bebas_ongkir", "false"),
                ShareExPropertyImageGeneratorArgResponseDto("bebas_ongkir_type", "0"),
                ShareExPropertyImageGeneratorArgResponseDto("has_ribbon", "0"),
                ShareExPropertyImageGeneratorArgResponseDto("has_campaign", "0"),
                ShareExPropertyImageGeneratorArgResponseDto("campaign_discount", "0"),
                ShareExPropertyImageGeneratorArgResponseDto("new_product_price", "0"),
                ShareExPropertyImageGeneratorArgResponseDto("campaign_info", ""),
                ShareExPropertyImageGeneratorArgResponseDto("campaign_name", ""),
                ShareExPropertyImageGeneratorArgResponseDto("product_image_orientation", "")
            )
        )
    }

    private fun getGenerateLinkProperties(): ShareExGenerateLinkPropertiesResponseDto {
        return ShareExGenerateLinkPropertiesResponseDto(
            message = "TEST MESSAGE",
            ogTitle = "THIS IS OG TITLE",
            ogDescription = "THIS IS OG DESCRIPTION",
            ogType = "",
            ogImageUrl = "https://images.tokopedia.net/img/cache/300/tPxBYm/2022/6/22/2511631a-0065-441a-9c34-6575b80b8bfc.jpg",
            ogVideo = "",
            desktopUrl = "https://www.tokopedia.com/msi-official/msi-cyborg-15-a12ucx-i5-12450h-16gb-512gb-rtx2050-15-6-fhd-144hz-w11-ddr5-16gb-367d6?extParam=whid%3D2506520",
            androidUrl = "https://www.tokopedia.com/msi-official/msi-cyborg-15-a12ucx-i5-12450h-16gb-512gb-rtx2050-15-6-fhd-144hz-w11-ddr5-16gb-367d6?extParam=whid%3D2506520",
            iosUrl = "https://www.tokopedia.com/msi-official/msi-cyborg-15-a12ucx-i5-12450h-16gb-512gb-rtx2050-15-6-fhd-144hz-w11-ddr5-16gb-367d6?extParam=whid%3D2506520",
            androidDeeplinkPath = "product/9310162582",
            iosDeeplinkPath = "product/9310162582",
            androidMinVersion = "",
            iosMinVersion = "",
            canonicalUrl = ""
        )
    }
}
