package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.picker.seller.domain.repository.AbstractProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.play.broadcaster.domain.model.addproduct.AddProductTagChannelRequest
import com.tokopedia.play.broadcaster.domain.usecase.AddProductTagUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetProductsInEtalaseUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.etalase.GetSelfEtalaseListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetPinnedProductUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetCampaignListUseCase
import com.tokopedia.play.broadcaster.domain.usecase.campaign.GetProductTagSummarySectionUseCase
import com.tokopedia.content.product.picker.seller.domain.usecase.campaign.GetProductsInCampaignUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.exception.PinnedProductException
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    getCampaignListUseCase: GetCampaignListUseCase,
    getProductsInCampaignUseCase: GetProductsInCampaignUseCase,
    getSelfEtalaseListUseCase: GetSelfEtalaseListUseCase,
    private val getProductsInEtalaseUseCase: GetProductsInEtalaseUseCase,
    private val addProductTagUseCase: AddProductTagUseCase,
    private val getProductTagSummarySectionUseCase: GetProductTagSummarySectionUseCase,
    private val setPinnedProductUseCase: SetPinnedProductUseCase,
    private val productMapper: PlayBroProductUiMapper,
    private val userSession: UserSessionInterface,
) : AbstractProductPickerSellerRepository(
    dispatchers,
    getCampaignListUseCase,
    getProductsInCampaignUseCase,
    getSelfEtalaseListUseCase,
    productMapper,
    userSession
) {

    override suspend fun getProductsInEtalase(
        etalaseId: String,
        cursor: String,
        keyword: String,
        sort: SortUiModel,
    ): PagedDataUiModel<ProductUiModel> = withContext(dispatchers.io) {
        if (userSession.shopId.isBlank()) error("User does not has shop")

        val param = GetProductsInEtalaseUseCase.Param(
            authorId = userSession.shopId,
            authorType = AUTHOR_TYPE_SELLER,
            cursor = cursor,
            limit = PRODUCTS_IN_ETALASE_PER_PAGE,
            keyword = keyword,
            sort = sort,
            etalaseId = etalaseId,
        )

        val response = getProductsInEtalaseUseCase.executeWithParam(param)

        return@withContext productMapper.mapProductsInEtalase(response)
    }

    override suspend fun setProductTags(creationId: String, productIds: List<String>) {
        withContext(dispatchers.io) {
            addProductTagUseCase(AddProductTagChannelRequest(creationId, productIds))
        }
    }

    override suspend fun getProductTagSummarySection(creationId: String, fetchCommission: Boolean) =
        withContext(dispatchers.io) {
            val response = getProductTagSummarySectionUseCase.apply {
                setRequestParams(GetProductTagSummarySectionUseCase.createParams(creationId, fetchCommission))
            }.executeOnBackground()

            return@withContext productMapper.mapProductTagSection(response)
        }

    private var lastRequestTime: Long = 0L
    private val isEligibleForPin: Boolean
        get() {
            val diff = System.currentTimeMillis() - lastRequestTime
            return diff >= DELAY_MS
        }

    override suspend fun setPinProduct(creationId: String, product: ProductUiModel): Boolean =
        withContext(dispatchers.io) {
            return@withContext if (isEligibleForPin || product.pinStatus.isPinned) {
                setPinnedProductUseCase.apply {
                    setRequestParams(createParam(creationId, product))
                }.executeOnBackground().data.success.apply {
                    if (this && !product.pinStatus.isPinned) lastRequestTime = System.currentTimeMillis()
                    else if (!this) throw PinnedProductException(
                        String.format(
                            "Gagal %1s pin di produk. Coba lagi, ya.",
                            if (product.pinStatus.isPinned) "lepas" else "pasang"
                        )
                    )
                    else return@apply
                }
            } else {
                throw PinnedProductException()
            }
        }

    companion object {
        private const val DELAY_MS = 5000L
    }
}
