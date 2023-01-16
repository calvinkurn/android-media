package com.tokopedia.mvc.domain.usecase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.common.util.PaginationConstant.INITIAL_PAGE
import com.tokopedia.mvc.data.mapper.GetCouponImagePreviewFacadeMapper
import com.tokopedia.mvc.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ShopData
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.universal_sharing.usecase.ImageGeneratorUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetCouponImagePreviewFacadeUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val initiateCouponUseCase: GetInitiateVoucherPageUseCase,
    private val getMostSoldProductsUseCase: ProductListUseCase,
    private val remoteDataSource: ImageGeneratorRemoteDataSource,
    private val mapper: GetCouponImagePreviewFacadeMapper
) {

    companion object {
        const val SORT_MODE_ID_SOLD = "SOLD"
        const val SORT_MODE_DIRECTION_DESC = "DESC"
    }

    suspend fun execute(
        isCreateMode: Boolean,
        voucherConfiguration: VoucherConfiguration,
        parentProductIds: List<Long>,
        imageRatio: ImageRatio
    ): Bitmap {
        return coroutineScope {
            val initiateCoupon = async {
                initiateCoupon(
                    isCreateMode,
                    voucherConfiguration.promoType,
                    voucherConfiguration.isVoucherProduct
                )
            }
            val shopDeferred = async { getShopBasicDataUseCase.execute() }
            val shop = shopDeferred.await()
            val coupon = initiateCoupon.await()
            val topProductPictures = getTopProductPictures(voucherConfiguration.isVoucherProduct, parentProductIds)

            val generateImageDeferred = async {
                generateCouponImage(
                    GenerateCouponImageParam(
                        isCreateMode = isCreateMode,
                        couponCodePrefix = coupon.prefixVoucherCode,
                        voucherConfiguration = voucherConfiguration,
                        topProductImageUrls = topProductPictures,
                        imageRatio = imageRatio,
                        shop = shop
                    )
                )
            }
            val image = generateImageDeferred.await()
            return@coroutineScope BitmapFactory.decodeByteArray(image, Int.ZERO, image.size)
        }
    }

    suspend fun executeGetImageUrl(
        isCreateMode: Boolean,
        voucherConfiguration: VoucherConfiguration,
        parentProductId: List<Long>,
        imageRatio: ImageRatio
    ): String {
        return coroutineScope {
            val initiateCoupon = async{
                initiateCoupon(
                    isCreateMode,
                    voucherConfiguration.promoType,
                    voucherConfiguration.isVoucherProduct
                )
            }
            val topProductsDeferred = async{ getMostSoldProducts(parentProductId) }
            val shopDeferred = async{ getShopBasicDataUseCase.execute() }

            val shop = shopDeferred.await()
            val coupon = initiateCoupon.await()
            val topProducts = topProductsDeferred.await()

            val generateImageDeferred = async{
                generateCouponImageUrl(
                    GenerateCouponImageParam(
                        isCreateMode = isCreateMode,
                        couponCodePrefix = coupon.prefixVoucherCode,
                        voucherConfiguration = voucherConfiguration,
                        topProductImageUrls = topProducts.map { it.picture },
                        imageRatio = imageRatio,
                        shop = shop
                    )
                )
            }

            generateImageDeferred.await()
        }
    }

    private suspend fun generateCouponImage(param: GenerateCouponImageParam): ByteArray {
        return remoteDataSource.previewImage(mapper.mapToPreviewImageParam(param)).bytes()
    }

    private suspend fun generateCouponImageUrl(param: GenerateCouponImageParam): String {
        val imageGeneratorUseCase = ImageGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
        imageGeneratorUseCase.params = mapper.mapToPreviewUrlImageParam(param)
        return imageGeneratorUseCase.executeOnBackground().imageUrl
    }

    private suspend fun getTopProductPictures(
        isVoucherProduct: Boolean,
        parentProductIds: List<Long>
    ): List<String> {
        return coroutineScope {
            if (isVoucherProduct) {
                val topProductsDeferred = async { getMostSoldProducts(parentProductIds) }
                topProductsDeferred.await()
            } else {
                listOf()
            }.map { it.picture }
        }
    }

    private suspend fun getMostSoldProducts(productIds: List<Long>): List<Product> {
        val param = ProductListUseCase.Param(
            searchKeyword = "",
            warehouseId = 0,
            page = INITIAL_PAGE,
            pageSize = productIds.size,
            categoryIds = listOf(),
            showcaseIds = listOf(),
            sortId = SORT_MODE_ID_SOLD,
            sortDirection = SORT_MODE_DIRECTION_DESC,
            extraInfo = listOf(),
            productIdInclude = productIds
        )
        return getMostSoldProductsUseCase.execute(param).products
    }

    private suspend fun initiateCoupon(
        isCreateMode: Boolean,
        promoType: PromoType,
        isVoucherProduct: Boolean
    ): VoucherCreationMetadata {
        val param = GetInitiateVoucherPageUseCase.Param(
            action = if (isCreateMode) VoucherAction.CREATE else VoucherAction.UPDATE,
            promoType = promoType,
            isVoucherProduct = isVoucherProduct
        )
        return initiateCouponUseCase.execute(param)
    }

    data class GenerateCouponImageParam (
        val isCreateMode: Boolean = false,
        val couponCodePrefix: String = "",
        val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(),
        val topProductImageUrls : List<String> = emptyList(),
        val imageRatio: ImageRatio = ImageRatio.SQUARE,
        val shop: ShopData = ShopData("", "", "")
    )
}
