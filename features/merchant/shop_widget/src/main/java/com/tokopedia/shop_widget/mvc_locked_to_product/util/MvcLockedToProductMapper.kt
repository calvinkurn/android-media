package com.tokopedia.shop_widget.mvc_locked_to_product.util

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*

object MvcLockedToProductMapper {

    fun mapToMvcLockedToProductRequestUiModel(
        shopId: String,
        promoId: String,
        page: Int,
        perPage: Int,
        selectedSortData: MvcLockedToProductSortUiModel,
        userAddressLocalData: LocalCacheModel
    ): MvcLockedToProductRequestUiModel {
        return MvcLockedToProductRequestUiModel(
            shopId,
            promoId,
            page,
            perPage,
            selectedSortData,
            userAddressLocalData
        )
    }

    fun mapToMvcLockedToProductLayoutUiModel(
        response: MvcLockedToProductResponse.ShopPageMVCProductLock,
        selectedSortData: MvcLockedToProductSortUiModel,
        isSellerView:Boolean
    ): MvcLockedToProductLayoutUiModel {
        val hasNextPage = checkHasNextPage(response.nextPage)
        val mvcLockedToProductVoucherUiModel = mapToMvcVoucherUiModel(response.voucher)
        val mvcLockedToProductTotalProductAndSortUiModel = mapToMvcTotalProductAndSortUiModel(
            response.productList.totalProduct,
            response.productList.totalProductWording,
            selectedSortData
        )
        val mvcLockedToProductProductListUiModel = mapToMvcLockedToProductProductListUiModel(
            response.productList,
            isSellerView
        )
        val mvcLockedToProductErrorUiModel = mapToMvcLockedToProductErrorUiModel(
            response.error.message,
            response.error.description,
            ctaText = response.error.ctaText,
            ctaLink = response.error.ctaLink
        )
        return MvcLockedToProductLayoutUiModel(
            hasNextPage,
            mvcLockedToProductVoucherUiModel,
            mvcLockedToProductTotalProductAndSortUiModel,
            mvcLockedToProductProductListUiModel,
            mvcLockedToProductErrorUiModel
        )
    }

    fun mapToMvcLockedToProductErrorUiModel(
        errorTitle: String = "",
        errorDescription: String,
        globalErrorType: Int = -1,
        ctaText: String = "",
        ctaLink: String = ""
    ): MvcLockedToProductGlobalErrorUiModel {
        return MvcLockedToProductGlobalErrorUiModel(
            errorTitle,
            errorDescription,
            globalErrorType,
            ctaText,
            ctaLink
        )
    }

    fun mapToMvcLockedToProductProductListUiModel(
        productListResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList,
        isSellerView: Boolean

    ): List<MvcLockedToProductGridProductUiModel> {
        return productListResponse.data.map {
            mapToMvcProductUiModel(it, isSellerView)
        }
    }

    private fun checkHasNextPage(nextPage: Int): Boolean {
        return nextPage.isMoreThanZero()
    }

    private fun mapToMvcProductUiModel(
        productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data,
        isSellerView: Boolean

    ): MvcLockedToProductGridProductUiModel {
        return MvcLockedToProductGridProductUiModel(
            productResponse.productID,
            productResponse.finalPrice,
            productResponse.childIDs,
            productResponse.city,
            productResponse.minimumOrder,
            productResponse.stock,
            MvcLockedToProductGridProductUiModel.ProductInCart(
                productResponse.productInCart.productId,
                productResponse.productInCart.qty
            ),
            productResponse.isVariant(),
            mapToProductCardModel(productResponse, isSellerView)
        )
    }

    private fun mapToProductCardModel(
        productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data,
        isSellerView: Boolean
    ): ProductCardModel {

            val model = createBaseProductCartModelPhase2(productResponse)
            if (productResponse.isVariant() || isSellerView) {
                return createProductCardWithDefaultAtcModel(model)
            } else {
                return createProductCardWithQuantityAtcModel(productResponse, model)
            }

    }

    private fun createBaseProductCartModelPhase2(
        productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data
    ): ProductCardModel {
        return ProductCardModel(
            productName = productResponse.name,
            productImageUrl = productResponse.imageUrl,
            formattedPrice = productResponse.displayPrice,
            slashedPrice = productResponse.originalPrice,
            discountPercentage = getProductCardDiscountPercentage(productResponse.discountPercentage),
            freeOngkir = ProductCardModel.FreeOngkir(
                productResponse.isShowFreeOngkir,
                productResponse.freeOngkirPromoIcon
            ),
            isOutOfStock = productResponse.isSoldOut,
            ratingCount = productResponse.rating,
            countSoldRating = getProductCardRating(productResponse.averageRating),
            reviewCount = productResponse.totalReview.toIntOrZero(),
            labelGroupList = productResponse.labelGroups.map { mapToProductCardLabelGroup(it) }
        )
    }

    private fun createProductCardWithQuantityAtcModel(
        productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data,
        productCardModel: ProductCardModel
    ): ProductCardModel {
        val productInCart = productResponse.productInCart.qty
        return if (productInCart.isZero()) {
            productCardModel.copy(
                nonVariant = null,
                hasAddToCartButton = true
            )
        } else {
            productCardModel.copy(
                nonVariant = ProductCardModel.NonVariant(
                    quantity = productInCart,
                    minQuantity = Int.ONE,
                    maxQuantity = productResponse.stock
                ),
                hasAddToCartButton = false
            )
        }
    }

    private fun createProductCardWithDefaultAtcModel(model: ProductCardModel): ProductCardModel {
        return model.copy(
            hasAddToCartButton = true
        )
    }

    private fun createProductCardModelPhase1(productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data): ProductCardModel {
        return ProductCardModel(
            productName = productResponse.name,
            productImageUrl = productResponse.imageUrl,
            formattedPrice = productResponse.displayPrice,
            slashedPrice = productResponse.originalPrice,
            discountPercentage = getProductCardDiscountPercentage(productResponse.discountPercentage),
            freeOngkir = ProductCardModel.FreeOngkir(
                productResponse.isShowFreeOngkir,
                productResponse.freeOngkirPromoIcon
            ),
            isOutOfStock = productResponse.isSoldOut,
            ratingCount = productResponse.rating,
            countSoldRating = getProductCardRating(productResponse.averageRating),
            reviewCount = productResponse.totalReview.toIntOrZero(),
            labelGroupList = productResponse.labelGroups.map { mapToProductCardLabelGroup(it) }
        )
    }


    private fun getProductCardRating(averageRating: Double): String {
        return averageRating.toString().takeIf { averageRating != 0.0 }.orEmpty()
    }

    private fun getProductCardDiscountPercentage(discountPercentage: String): String {
        return discountPercentage.replace("%", "").let { discount ->
            discount.takeIf { it != "0" &&  it.isNotEmpty() }?.let{
                "$discount%"
            }.orEmpty()
        }
    }

    private fun mapToProductCardLabelGroup(
        labelGroup: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data.LabelGroups
    ): ProductCardModel.LabelGroup {
        return ProductCardModel.LabelGroup(
            position = labelGroup.position,
            title = labelGroup.title,
            type = labelGroup.type,
            imageUrl = labelGroup.url
        )
    }

    private fun mapToMvcTotalProductAndSortUiModel(
        totalProduct: Int,
        totalProductWording: String,
        selectedSortData: MvcLockedToProductSortUiModel
    ): MvcLockedToProductSortSectionUiModel {
        return MvcLockedToProductSortSectionUiModel(
            totalProduct,
            totalProductWording,
            selectedSortData
        )
    }

    private fun mapToMvcVoucherUiModel(
        voucherResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.Voucher
    ): MvcLockedToProductVoucherUiModel {
        return MvcLockedToProductVoucherUiModel(
            voucherResponse.shopImage,
            voucherResponse.title,
            voucherResponse.baseCode,
            voucherResponse.expiredWording,
            voucherResponse.totalQuotaLeft,
            voucherResponse.totalQuotaLeftWording,
            voucherResponse.minPurchaseWording
        )
    }

}
