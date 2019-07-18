package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.domain.model.SearchShopModelKt
import com.tokopedia.search.result.presentation.model.ShopViewModelKt

class ShopViewModelMapperKt: Mapper<SearchShopModelKt, ShopViewModelKt> {

    override fun convert(source: SearchShopModelKt): ShopViewModelKt {
        val searchShopData = source.aceSearchShop

        return ShopViewModelKt(
                source = searchShopData.source,
                totalShop = searchShopData.totalShop,
                searchUrl = searchShopData.searchUrl,
                paging = createPagingViewModel(searchShopData.paging),
                tabName = searchShopData.tabName,
                shopItemList = createShopItemViewModelList(searchShopData.shopList)

        )
    }

    private fun createPagingViewModel(pagingModel: SearchShopModelKt.AceSearchShop.Paging): ShopViewModelKt.Paging {
        return ShopViewModelKt.Paging(
                uriNext = pagingModel.uriNext,
                uriPrevious = pagingModel.uriPrevious
        )
    }

    private fun createShopItemViewModelList(
            shopListModelList: List<SearchShopModelKt.AceSearchShop.ShopItem>
    ): List<ShopViewModelKt.ShopItem> {
        val shopViewModelList = listOf<ShopViewModelKt.ShopItem>()


        return shopViewModelList
    }

    private fun createShopItemViewModel(
            shopItemModel: SearchShopModelKt.AceSearchShop.ShopItem
    ): ShopViewModelKt.ShopItem {
        return ShopViewModelKt.ShopItem(
                shopId = "",
                shopName = "",
                shopDomain = "",
                shopUrl = "",
                shopApplink = "",
                shopImage = "",
                shopImage300 = "",
                shopDescription = "",
                shopTagLine = "",
                shopLocation = "",
                shopTotalTransaction = "",
                shopTotalFavorite = "",
                shopGoldShop = 0,
                shopIsOwner = 0,
                shopRateSpeed = 0,
                shopRateAccuracy = 0,
                shopRateService = 0,
                shopStatus = 0,
                shopItemProductList = createShopItemProductViewModelList(shopItemModel.productList),
                voucher = ShopViewModelKt.ShopItem.ShopItemVoucher(),
                shopLucky = "",
                reputationImageUri = "",
                reputationScore = 0,
                isOfficial = false,
                gaKey = ""
        )
    }

    private fun createShopItemProductViewModelList(
            shopItemProductList: List<SearchShopModelKt.AceSearchShop.ShopItem.ShopItemProduct>
    ): List<ShopViewModelKt.ShopItem.ShopItemProduct> {
        val shopItemProductViewModelList = mutableListOf<ShopViewModelKt.ShopItem.ShopItemProduct>()

        shopItemProductList.forEach { shopItemProduct ->
            shopItemProductViewModelList.add(createShopItemProductViewModel(shopItemProduct))
        }

        return shopItemProductViewModelList
    }

    private fun createShopItemProductViewModel(
            shopItemProduct: SearchShopModelKt.AceSearchShop.ShopItem.ShopItemProduct
    ): ShopViewModelKt.ShopItem.ShopItemProduct {
        return ShopViewModelKt.ShopItem.ShopItemProduct(
                id = shopItemProduct.id,
                name = shopItemProduct.name,
                url = shopItemProduct.url,
                applink = shopItemProduct.applink,
                price = shopItemProduct.price,
                imageUrl = shopItemProduct.imageUrl
        )
    }

    private fun createShopItemVoucherViewModel(
            shomItemVoucherModel: SearchShopModelKt.AceSearchShop.ShopItem.ShopItemVoucher
    ): ShopViewModelKt.ShopItem.ShopItemVoucher {

    }

    private fun createShopItemVoucherCashbackViewModel(
            shopItemVoucherCashbackViewModel: ShopItemVoucher
    )
}