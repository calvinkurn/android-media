package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.domain.repository.PlayBroProductRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetShopProductsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductsInEtalaseUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCampaignListUseCase: GetCampaignListUseCase,
    private val getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
    private val getShopProductsUseCase: GetShopProductsUseCase,
    private val productMapper: PlayBroProductUiMapper,
    private val userSession: UserSessionInterface,
) : PlayBroProductRepository {

    override suspend fun getCampaignList(): List<CampaignUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val response = getCampaignListUseCase.apply {
            setRequestParams(GetCampaignListUseCase.createParams(shopId = userSession.shopId))
        }.executeOnBackground()

        return@withContext productMapper.mapCampaignList(response)
    }

    override suspend fun getEtalaseList(): List<EtalaseUiModel> = withContext(dispatchers.io) {
        val response = getSelfEtalaseListUseCase.executeOnBackground()

        return@withContext productMapper.mapEtalaseList(response)
    }

    override suspend fun getProductsInEtalase(
        etalaseId: String,
        page: Int,
        keyword: String,
    ): List<ProductUiModel> = withContext(dispatchers.io) {
//        if (userSession.shopId.isBlank()) error("User does not has shop")
//
//        val response = getShopProductsUseCase.apply {
//            setRequestParams(
//                GetShopProductsUseCase.createParams(
//                    shopId = userSession.shopId,
//                    page = page,
//                    perPage = PRODUCTS_IN_ETALASE_PER_PAGE,
//                    etalaseId = etalaseId,
//                    keyword = keyword,
//                    sort = 0,
//                )
//            )
//        }.executeOnBackground()
//
//        return@withContext productMapper.mapProductsInEtalase(response)

        return@withContext List(10) {
            if (it % 2 == 0) {
                ProductUiModel(
                    id = it.toString(),
                    name = "Test A",
                    imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/12/29/3e1c930b-8f4d-429d-9e0c-8cc09b2a1dc2.png",
                    stock = 10,
                    price = OriginalPrice("Rp123.000", 123.0)
                )
            } else {
                ProductUiModel(
                    id = it.toString(),
                    name = "Test B",
                    imageUrl = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/1/4/8d05640e-c272-4835-b4cd-a75b7c5e98c3.png",
                    stock = 5,
                    price = DiscountedPrice(
                        "Rp456.000",
                        456.0,
                        30,
                        "Rp123.000",
                        123.0
                    )
                )
            }
        }
    }

    companion object {
        private const val PRODUCTS_IN_ETALASE_PER_PAGE = 25
    }
}