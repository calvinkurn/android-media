package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.flashsale.data.mapper.ShopInfoMapper
import com.tokopedia.shop.flashsale.domain.entity.ShopInfo
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.usecase.GetBannerGeneratorDataUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class GetShareComponentMetadataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getBannerGeneratorDataUseCase: GetBannerGeneratorDataUseCase,
    private val userSession: UserSessionInterface,
    private val getShopInfoUseCase: GQLGetShopInfoUseCase,
    private val mapper: ShopInfoMapper
) : GraphqlUseCase<ShareComponentMetadata>(repository) {


    suspend fun execute(campaignId: Long): ShareComponentMetadata {
        return coroutineScope {
            val bannerDeferred = async { getBannerGeneratorDataUseCase.execute(campaignId) }
            val shopInfo = async { getShopInfo() }
            val banner = bannerDeferred.await()
            val shop = shopInfo.await()
            ShareComponentMetadata(banner, shop)
        }
    }

    private suspend fun getShopInfo(): ShopInfo {
        val shopId = userSession.shopId.toIntOrZero()
        val fields = GQLGetShopInfoUseCase.getDefaultShopFields() + listOf(GQLGetShopInfoUseCase.FIELD_GOLD_OS)

        getShopInfoUseCase.isFromCacheFirst = false
        getShopInfoUseCase.params =
            GQLGetShopInfoUseCase.createParams(listOf(shopId), fields = fields)
        val response = getShopInfoUseCase.executeOnBackground()
        return mapper.map(response)
    }

}