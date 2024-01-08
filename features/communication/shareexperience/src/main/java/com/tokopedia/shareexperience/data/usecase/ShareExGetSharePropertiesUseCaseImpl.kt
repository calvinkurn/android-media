package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.ShareExBottomSheetResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExPropertyResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExShareBodyResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExSharePropertiesResponseDto
import com.tokopedia.shareexperience.data.dto.ShareExWrapperResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateEligibilityResponseDto
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateRegistrationWidgetResponseDto
import com.tokopedia.shareexperience.data.dto.imagegenerator.ShareExImageGeneratorResponseDto
import com.tokopedia.shareexperience.data.mapper.ShareExPropertyMapper
import com.tokopedia.shareexperience.data.repository.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExBottomSheetRequest
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
    ): Flow<ShareExBottomSheetModel> {
        return flow {
            val result = mapper.mapDefault(defaultUrl, defaultImageUrl)
            emit(result)
        }.flowOn(dispatchers.io)
    }

    override suspend fun getData(params: ShareExProductBottomSheetRequest): Flow<ShareExBottomSheetModel> {
        return getShareBottomSheetResponse(params)
    }

    override suspend fun getData(params: ShareExShopBottomSheetRequest): Flow<ShareExBottomSheetModel> {
        return getShareBottomSheetResponse(params)
    }

    override suspend fun getData(params: ShareExDiscoveryBottomSheetRequest): Flow<ShareExBottomSheetModel> {
        return getShareBottomSheetResponse(params)
    }

    private suspend fun getShareBottomSheetResponse(params: ShareExBottomSheetRequest): Flow<ShareExBottomSheetModel> {
        return flow {
//            val dto = repository.request<ShareExBottomSheetRequest, ShareExWrapperResponseDto>(
//                sharePropertiesQuery, params
//            )
            val dto = getDummyResponseDto()
            val result = mapper.map(dto.response.bottomSheet)
            emit(result)
        }.flowOn(dispatchers.io)
    }

    private fun getDummyResponseDto(): ShareExWrapperResponseDto {
        return ShareExWrapperResponseDto(
            ShareExSharePropertiesResponseDto(
                ShareExBottomSheetResponseDto(
                    title = "Bagikan ke teman kamu",
                    subtitle = "Mau bagi halaman yang mana?",
                    chips = getChips(),
                    properties = getProperties(),
                    imageGeneratorPayload = ShareExImageGeneratorResponseDto() // TODO: setup this
                )
            )
        )
    }

    private fun getChips(): List<String> {
        return listOf(
            "Halaman Toko",
            "Semua Produk",
            "Etalase",
            "Feed"
        )
    }

    private fun getProperties(): List<ShareExPropertyResponseDto> {
        return listOf(
            ShareExPropertyResponseDto(
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
                affiliateEligibility = ShareExAffiliateEligibilityResponseDto(
                    eligible = false,
                    commission = "",
                    label = "Komisi Extra",
                    date = "Hingga 31 Des 2024"
                )
            ),
            ShareExPropertyResponseDto(
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
                    eligible = true,
                    commission = "Rp16.000",
                    label = "Komisi Extra",
                    date = "Hingga 31 Des 2024"
                )
            ),
            ShareExPropertyResponseDto(
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
                    eligible = true,
                    commission = "Rp16.000"
                )
            ),
            ShareExPropertyResponseDto(
                shareBody = ShareExShareBodyResponseDto(
                    title = "",
                    thumbnailUrls = getImageThumbnails(0)
                ),
                affiliateRegistrationWidget = ShareExAffiliateRegistrationWidgetResponseDto(),
                affiliateEligibility = ShareExAffiliateEligibilityResponseDto(
                    eligible = false,
                    commission = "Rp16.000",
                    label = "Komisi Extra",
                    date = "Hingga 31 Des 2024"
                )
            ),
            ShareExPropertyResponseDto(
                shareBody = ShareExShareBodyResponseDto(
                    title = "",
                    thumbnailUrls = getImageThumbnails(0)
                ),
                affiliateRegistrationWidget = ShareExAffiliateRegistrationWidgetResponseDto(),
                affiliateEligibility = ShareExAffiliateEligibilityResponseDto()
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
}
