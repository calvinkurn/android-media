package com.tokopedia.search.result.shop.presentation.viewmodel.testinstance

import com.google.gson.Gson
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.cpmJSONObject
import com.tokopedia.search.result.cpmJsonString
import com.tokopedia.search.result.notCpmShopJsonObject
import com.tokopedia.search.result.notCpmShopJsonString
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.topads.sdk.domain.model.CpmModel

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
        shopList = shopItemList,
        totalShop = 6
)

internal val aceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
        shopList = shopItemList,
        totalShop = 4
)

internal val aceSearchShopWithRecommendationHasNextPage = SearchShopModel.AceSearchShop(
        topShopList = shopItemList,
        totalShop = 6
)

internal val aceSearchShopWithRecommendationWithoutNextPage = SearchShopModel.AceSearchShop(
        topShopList = shopItemList,
        totalShop = 4
)

internal val moreAceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
        shopList = moreShopItemList,
        totalShop = 8
)

internal val moreAceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
        shopList = moreShopItemList,
        totalShop = 6
)

internal val moreAceSearchShopWithRecommendationHasNextPage = SearchShopModel.AceSearchShop(
        topShopList = moreShopItemList,
        totalShop = 8
)

internal val moreAceSearchShopWithRecommendationWithoutNextPage = SearchShopModel.AceSearchShop(
        topShopList = moreShopItemList,
        totalShop = 6
)

internal val cpmModel = Gson().fromJson(cpmJsonString, CpmModel::class.java)
internal val notCpmShopModel = Gson().fromJson(notCpmShopJsonString, CpmModel::class.java)

internal val searchShopQuickFilterModel = "searchshop/quickfilter/quick-filter-response.json".jsonToObject<SearchShopModel.FilterSort>()
internal val searchShopModel = SearchShopModel(aceSearchShopWithNextPage, cpmModel, searchShopQuickFilterModel)
internal val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage, cpmModel, searchShopQuickFilterModel)
internal val searchShopModelWithoutCpm = SearchShopModel(aceSearchShop = aceSearchShopWithNextPage, quickFilter = searchShopQuickFilterModel)
internal val searchShopModelWithoutValidCpmShop = SearchShopModel(aceSearchShopWithNextPage, notCpmShopModel, searchShopQuickFilterModel)
internal val searchShopModelEmptyList = SearchShopModel()
internal val searchShopModelEmptyWithRecommendationHasNextPage = SearchShopModel(aceSearchShopWithRecommendationHasNextPage)
internal val searchShopModelEmptyWithRecommendationWithoutNextPage = SearchShopModel(aceSearchShopWithRecommendationWithoutNextPage)
internal val searchMoreShopModel = SearchShopModel(moreAceSearchShopWithNextPage)
internal val searchMoreShopModelWithoutNextPage = SearchShopModel(moreAceSearchShopWithoutNextPage)
internal val searchMoreShopWithRecommendationHasNextPage = SearchShopModel(moreAceSearchShopWithRecommendationHasNextPage)
internal val searchMoreShopWithRecommendationWithoutNextPage = SearchShopModel(moreAceSearchShopWithRecommendationWithoutNextPage)
internal val searchShopModelEmptyQuickFilter = SearchShopModel(aceSearchShopWithNextPage, cpmModel)

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
