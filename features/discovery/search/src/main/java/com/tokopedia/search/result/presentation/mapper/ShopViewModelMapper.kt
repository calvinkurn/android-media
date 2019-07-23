package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.model.ShopViewModel

class ShopViewModelMapper: Mapper<SearchShopModel, ShopViewModel> {

    override fun convert(source: SearchShopModel): ShopViewModel {
        val searchShopData = source.aceSearchShop

        return ShopViewModel(
                source = searchShopData.source,
                totalShop = searchShopData.totalShop,
                searchUrl = searchShopData.searchUrl,
                paging = createPagingViewModel(searchShopData.paging),
                tabName = searchShopData.tabName,
                shopItemList = createShopItemViewModelList(searchShopData.shopList),
                topSellerData = createShopItemViewModelList(searchShopData.topSellerData),
                topOfficialSellerData = createShopItemViewModelList(searchShopData.topOfficialSellerData),
                cpmModel = source.cpmModel
        )
    }

    private fun createPagingViewModel(pagingModel: SearchShopModel.AceSearchShop.Paging): ShopViewModel.Paging {
        return ShopViewModel.Paging(
                uriNext = pagingModel.uriNext,
                uriPrevious = pagingModel.uriPrevious
        )
    }

    private fun createShopItemViewModelList(
            shopItemModelList: List<SearchShopModel.AceSearchShop.ShopItem>
    ): List<ShopViewModel.ShopItem> {
        val shopItemViewModelList = mutableListOf<ShopViewModel.ShopItem>()

        shopItemModelList.forEach { shopItemModel ->
            shopItemViewModelList.add(createShopItemViewModel(shopItemModel))
        }

        return shopItemViewModelList
    }

    private fun createShopItemViewModel(
            shopItemModel: SearchShopModel.AceSearchShop.ShopItem
    ): ShopViewModel.ShopItem {
        return ShopViewModel.ShopItem(
                id = shopItemModel.id,
                name = shopItemModel.name,
                domain = shopItemModel.domain,
                url = shopItemModel.url,
                applink = shopItemModel.applink,
                image = shopItemModel.image,
                image300 = shopItemModel.image300,
                description = shopItemModel.description,
                tagLine = shopItemModel.tagLine,
                location = shopItemModel.location,
                totalTransaction = shopItemModel.totalTransaction,
                totalFavorite = shopItemModel.totalFavorite,
                goldShop = shopItemModel.goldShop,
                isOwner = shopItemModel.isOwner,
                rateSpeed = shopItemModel.rateSpeed,
                rateAccuracy = shopItemModel.rateAccuracy,
                rateService = shopItemModel.rateService,
                status = shopItemModel.status,
                productList = createShopItemProductViewModelList(shopItemModel.productList),
                voucher = createShopItemVoucherViewModel(shopItemModel.voucher),
                lucky = shopItemModel.lucky,
                reputationImageUri = shopItemModel.reputationImageUri,
                reputationScore = shopItemModel.reputationScore,
                isOfficial = shopItemModel.isOfficial,
                gaKey = shopItemModel.gaKey
        )
    }

    private fun createShopItemProductViewModelList(
            shopItemProductList: List<SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct>
    ): List<ShopViewModel.ShopItem.ShopItemProduct> {
        val shopItemProductViewModelList = mutableListOf<ShopViewModel.ShopItem.ShopItemProduct>()

        shopItemProductList.forEach { shopItemProduct ->
            shopItemProductViewModelList.add(createShopItemProductViewModel(shopItemProduct))
        }

        return shopItemProductViewModelList
    }

    private fun createShopItemProductViewModel(
            shopItemProduct: SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct
    ): ShopViewModel.ShopItem.ShopItemProduct {
        return ShopViewModel.ShopItem.ShopItemProduct(
                id = shopItemProduct.id,
                name = shopItemProduct.name,
                url = shopItemProduct.url,
                applink = shopItemProduct.applink,
                price = shopItemProduct.price,
                priceFormat = shopItemProduct.priceFormat,
                imageUrl = shopItemProduct.imageUrl
        )
    }

    private fun createShopItemVoucherViewModel(
            shopItemVoucherModel: SearchShopModel.AceSearchShop.ShopItem.ShopItemVoucher
    ): ShopViewModel.ShopItem.ShopItemVoucher {
        return ShopViewModel.ShopItem.ShopItemVoucher(
                freeShipping = shopItemVoucherModel.freeShipping,
                cashback = createShopItemVoucherCashbackViewModel(shopItemVoucherModel.cashback)
        )
    }

    private fun createShopItemVoucherCashbackViewModel(
            shopItemVoucherCashbackModel: SearchShopModel.AceSearchShop.ShopItem.ShopItemVoucher.ShopItemVoucherCashback
    ): ShopViewModel.ShopItem.ShopItemVoucher.ShopItemVoucherCashback {
        return ShopViewModel.ShopItem.ShopItemVoucher.ShopItemVoucherCashback(
                isPercentage = shopItemVoucherCashbackModel.isPercentage,
                cashbackValue = shopItemVoucherCashbackModel.cashbackValue
        )
    }
}