package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.usecase.GetBannerGeneratorDataUseCase
import com.tokopedia.shop.flashsale.domain.usecase.ShopInfoByIdQueryUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class GetShareComponentMetadataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getBannerGeneratorDataUseCase: GetBannerGeneratorDataUseCase,
    private val shopInfoByIdQueryUseCase: ShopInfoByIdQueryUseCase
) : GraphqlUseCase<ShareComponentMetadata>(repository) {


    suspend fun execute(campaignId: Long): ShareComponentMetadata {
        return coroutineScope {
            val bannerDeferred = async { getBannerGeneratorDataUseCase.execute(campaignId) }
            val shopInfo = async { shopInfoByIdQueryUseCase.execute() }
            val banner = bannerDeferred.await()
            val shop = shopInfo.await()
            ShareComponentMetadata(banner, shop)
        }
    }

}