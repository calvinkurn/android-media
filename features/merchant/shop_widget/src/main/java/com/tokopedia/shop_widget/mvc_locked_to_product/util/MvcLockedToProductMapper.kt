package com.tokopedia.shop_widget.mvc_locked_to_product.util

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.*

object MvcLockedToProductMapper {

    fun mapToMvcLockedToProductRequestUiModel(
        promoId: String,
        page: Int,
        perPage: Int,
        selectedSortData: MvcLockedToProductSortUiModel,
        userAddressLocalData: LocalCacheModel
    ): MvcLockedToProductRequestUiModel {
        return MvcLockedToProductRequestUiModel(
            promoId,
            page,
            perPage,
            selectedSortData,
            userAddressLocalData
        )
    }

    fun mapToMvcLockedToProductLayoutUiModel(
        response: MvcLockedToProductResponse.ShopPageMVCProductLock,
        selectedSortData: MvcLockedToProductSortUiModel
    ): MvcLockedToProductLayoutUiModel {
        val hasNextPage = checkHasNextPage(response.nextPage)
        val mvcLockedToProductVoucherUiModel = mapToMvcVoucherUiModel(response.voucher)
        val mvcLockedToProductTotalProductAndSortUiModel = mapToMvcTotalProductAndSortUiModel(
            response.productList.totalProduct,
            response.productList.totalProductWording,
            selectedSortData
        )
        val mvcLockedToProductProductListUiModel =
            mapToMvcLockedToProductProductListUiModel(response)
        return MvcLockedToProductLayoutUiModel(
            hasNextPage,
            mvcLockedToProductVoucherUiModel,
            mvcLockedToProductTotalProductAndSortUiModel,
            mvcLockedToProductProductListUiModel
        )
    }

    fun mapToMvcLockedToProductProductListUiModel(response: MvcLockedToProductResponse.ShopPageMVCProductLock): List<MvcLockedToProductGridProductUiModel> {
        return response.productList.data.map {
            mapToMvcProductUiModel(it)
        }
    }

    private fun checkHasNextPage(nextPage: Int): Boolean {
        return nextPage.isMoreThanZero()
    }

    private fun mapToMvcProductUiModel(
        productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data
    ): MvcLockedToProductGridProductUiModel {
        return MvcLockedToProductGridProductUiModel(
            productResponse.productID.toString(),
            productResponse.childIDs,
            productResponse.city,
            productResponse.minimumOrder,
            productResponse.stock,
            productResponse.productInCart,
            mapToProductCardModel(productResponse)
        )
    }

    private fun mapToProductCardModel(
        productResponse: MvcLockedToProductResponse.ShopPageMVCProductLock.ProductList.Data
    ): ProductCardModel {
        return ProductCardModel(
            productName = productResponse.name,
            productImageUrl = productResponse.imageUrl,
            formattedPrice = productResponse.displayPrice,
            slashedPrice = productResponse.originalPrice,
            discountPercentage = productResponse.discountPercentage,
            freeOngkir = ProductCardModel.FreeOngkir(
                productResponse.isShowFreeOngkir,
                productResponse.freeOngkirPromoIcon
            ),
            isOutOfStock = productResponse.isSoldOut,
            ratingCount = productResponse.rating,
            countSoldRating = productResponse.averageRating.toString(),
            reviewCount = productResponse.totalReview.toIntOrZero(),
            labelGroupList = productResponse.labelGroups.map { mapToProductCardLabelGroup(it) }
        )
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
    ): MvcLockedToProductTotalProductAndSortUiModel {
        return MvcLockedToProductTotalProductAndSortUiModel(
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
            voucherResponse.totalQuotaLeftWording
        )
    }

}
