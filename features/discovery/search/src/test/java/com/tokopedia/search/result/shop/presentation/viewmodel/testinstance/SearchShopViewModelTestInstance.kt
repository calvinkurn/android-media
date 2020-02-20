package com.tokopedia.search.result.shop.presentation.viewmodel.testinstance

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.result.cpmJSONObject
import com.tokopedia.search.result.notCpmShopJsonObject
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.topads.sdk.domain.model.CpmModel

internal val pagingWithNextPage = SearchShopModel.AceSearchShop.Paging(uriNext = "Some random string indicating has next page")
internal val pagingWithoutNextPage = SearchShopModel.AceSearchShop.Paging(uriNext = "")

internal val shopItemProductList: List<SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct> = mutableListOf<SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct>().also {
    it.add(SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct(id = 1))
    it.add(SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct(id = 2))
    it.add(SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct(id = 3))
}

internal val shopItemList: List<SearchShopModel.AceSearchShop.ShopItem> = mutableListOf<SearchShopModel.AceSearchShop.ShopItem>().also {
    it.add(SearchShopModel.AceSearchShop.ShopItem(id = "1", productList = shopItemProductList))
    it.add(SearchShopModel.AceSearchShop.ShopItem(id = "2", productList = shopItemProductList))
    it.add(SearchShopModel.AceSearchShop.ShopItem(id = "3", productList = shopItemProductList))
    it.add(SearchShopModel.AceSearchShop.ShopItem(id = "4", productList = shopItemProductList))
}

internal val moreShopItemList: List<SearchShopModel.AceSearchShop.ShopItem> = mutableListOf<SearchShopModel.AceSearchShop.ShopItem>().also {
    it.add(SearchShopModel.AceSearchShop.ShopItem(id = "5", productList = shopItemProductList))
    it.add(SearchShopModel.AceSearchShop.ShopItem(id = "6", productList = shopItemProductList))
}

internal val aceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
        paging = pagingWithNextPage,
        shopList = shopItemList
)

internal val aceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
        paging = pagingWithoutNextPage,
        shopList = shopItemList
)

internal val moreAceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
        paging = pagingWithNextPage,
        shopList = moreShopItemList
)

internal val moreAceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
        paging = pagingWithoutNextPage,
        shopList = moreShopItemList
)

internal val cpmModel = CpmModel(cpmJSONObject)
internal val notCpmShopModel = CpmModel(notCpmShopJsonObject)

internal val searchShopModel = SearchShopModel(aceSearchShopWithNextPage, cpmModel)
internal val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage, cpmModel)
internal val searchShopModelWithoutCpm = SearchShopModel(aceSearchShopWithNextPage)
internal val searchShopModelWithoutValidCpmShop = SearchShopModel(aceSearchShopWithNextPage, notCpmShopModel)
internal val searchShopModelEmptyList = SearchShopModel()
internal val searchMoreShopModel = SearchShopModel(moreAceSearchShopWithNextPage)
internal val searchMoreShopModelWithoutNextPage = SearchShopModel(moreAceSearchShopWithoutNextPage)

internal val officialOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId("official", "true", "Official Store")
)
internal val shopFilter = Filter().also { filter ->
    filter.options = mutableListOf<Option>().also { optionList ->
        optionList.add(officialOption)
    }
}
internal val jakartaOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.FCITY, "1", "Jakarta")
)
internal val tangerangOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.FCITY, "2", "Tangerang")
)
internal val bogorOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.FCITY, "3", "Bogor")
)
internal val locationFilter = Filter().also { filter ->
    filter.options = mutableListOf<Option>().also { optionList ->
        optionList.add(jakartaOption)
        optionList.add(tangerangOption)
        optionList.add(bogorOption)
    }
}
internal val minPriceOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Harga minimum")
)
internal val maxPriceOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.PMAX, "", "Harga maximum")
)
internal val priceFilter = Filter().also { filter ->
    filter.options = mutableListOf<Option>().also { optionList ->
        optionList.add(minPriceOption)
        optionList.add(maxPriceOption)
    }
}
internal val handphoneOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.SC, "13", "Handphone")
)
internal val tvOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.SC, "14", "TV")
)
internal val kulkasOption = OptionHelper.generateOptionFromUniqueId(
        OptionHelper.constructUniqueId(SearchApiConst.SC, "15", "Kulkas")
)
internal val categoryFilter = Filter().also { filter ->
    filter.options = mutableListOf<Option>().also { optionList ->
        optionList.add(handphoneOption)
        optionList.add(tvOption)
        optionList.add(kulkasOption)
    }
}

internal val dynamicFilterModel = DynamicFilterModel().also { dynamicFilterModel ->
    dynamicFilterModel.data = DataValue()
    dynamicFilterModel.data.filter = mutableListOf<Filter>().also {
        it.add(shopFilter)
        it.add(locationFilter)
        it.add(priceFilter)
        it.add(categoryFilter)
    }
}