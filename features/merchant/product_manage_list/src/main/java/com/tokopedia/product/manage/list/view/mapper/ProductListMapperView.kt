package com.tokopedia.product.manage.list.view.mapper

import com.tokopedia.product.manage.list.view.model.ProductListManageModelView
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Data
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListResponse
import javax.inject.Inject

class ProductListMapperView @Inject constructor() {

    fun mapIntoViewModel(productList: ProductListResponse): ProductListManageModelView {
        val productListManageModelView = ProductListManageModelView()

        productListManageModelView.isHasNextPage = productList.getProductList.links.next != ""

        if (productList.getProductList.data.isNotEmpty()) {
            val products = mapToListProducts(productList.getProductList.data)
            productListManageModelView.setProductListPickerViewModels(products)
        }

        return productListManageModelView
    }

    private fun mapToListProducts(productList: List<Data>): List<ProductManageViewModel> {
        val productManageListViewModel = mutableListOf<ProductManageViewModel>()

        productManageListViewModel.addAll(
                productList.map {
                    convertData(it)
                }
        )
        return productManageListViewModel
    }

    private fun convertData(data: Data): ProductManageViewModel {
        val productManageViewModel = ProductManageViewModel()

        productManageViewModel.run {
            id = data.productId
            imageUrl = data.primaryImage.thumbnail
            imageFullUrl = data.primaryImage.original
            setTitle(data.name)
            productPrice = data.price.text
            productCurrencyId = data.price.currencyId
            productStatus = data.status.toString()
            productUrl = data.productUrl
            productCurrencySymbol = data.price.currencyText
            productReturnable = booleanToInt(data.flags.isFreereturn)
            productPreorder = booleanToInt(data.flags.isPreorder)
            productWholesale = if (data.wholesaleResponse.isNotEmpty()) 1 else 0
            productPricePlain = data.price.value.toString()
            productCashback = data.cashBack.cashBack
            productCashbackAmount = data.cashBack.cashbackAmount
            productStock = data.stock
            productUsingStock = if (data.stock == 0) 0 else 1
            productVariant = booleanToInt(data.flags.isVariant)
            isFeatureProduct = data.flags.isFeatured
        }

        return productManageViewModel
    }

    private fun booleanToInt(isTrue: Boolean): Int {
        return if (isTrue) {
            1
        } else {
            0
        }
    }
}