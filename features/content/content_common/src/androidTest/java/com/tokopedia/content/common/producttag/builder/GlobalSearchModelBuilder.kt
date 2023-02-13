package com.tokopedia.content.common.producttag.builder

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.model.PagedGlobalSearchProductResponse
import com.tokopedia.content.common.producttag.model.PagedGlobalSearchShopResponse
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.filter.common.data.*

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
class GlobalSearchModelBuilder {

    fun buildResponseModel(
        size: Int = 10,
        hasNextPage: Boolean = true,
        nextCursor: String = "1",
        suggestion: SuggestionUiModel = SuggestionUiModel(),
        ticker: TickerUiModel = TickerUiModel(),
    ): PagedGlobalSearchProductResponse {
        return PagedGlobalSearchProductResponse(
            pagedData = PagedDataUiModel(
                dataList = List(size) {
                    ProductUiModel(
                        id = it.toString(),
                        shopID = it.toString(),
                        shopName = "Shop $it",
                        shopBadge = listOf(),
                        name = "Product $it",
                        coverURL = "",
                        webLink = "",
                        appLink = "",
                        star = (it % 5).toString(),
                        price = (it * 10000).toDouble(),
                        priceFmt = "Rp ${(it * 10000)}",
                        isDiscount = true,
                        discount = 5.0,
                        discountFmt = "5%",
                        priceOriginal = (it * 10000).toDouble(),
                        priceOriginalFmt = "Rp ${(it * 10000).toDouble()}",
                        priceDiscount = (it * 10000).toDouble(),
                        priceDiscountFmt = "Rp ${(it * 10000).toDouble()}",
                        totalSold = it,
                        totalSoldFmt = it.toString(),
                        isBebasOngkir = false,
                        bebasOngkirStatus = "",
                        bebasOngkirURL = "",
                    )
                },
                nextCursor = nextCursor,
                hasNextPage = hasNextPage
            ),
            header = SearchHeaderUiModel(
                totalDataText = "$size",
                totalData = size,
                responseCode = 200,
                keywordProcess = "",
                componentId = "",
            ),
            suggestion = suggestion,
            ticker = ticker,
        )
    }

    fun buildShopResponseModel(
        size: Int = 10,
        hasNextPage: Boolean = true,
        nextCursor: String = "1",
    ) = PagedGlobalSearchShopResponse(
        totalShop = size,
        pagedData = PagedDataUiModel(
            dataList = List(size) {
                ShopUiModel(
                    shopId = it.toString(),
                    shopName = "Shop $it",
                )
            },
            hasNextPage = hasNextPage,
            nextCursor = nextCursor,
        ),
        header = SearchHeaderUiModel(
            totalDataText = "$size",
            totalData = size,
            responseCode = 200,
            keywordProcess = "",
            componentId = "",
        ),
    )

    fun buildSortFilterResponseModel(
        sizeFilter: Int = 5,
        sizeSort: Int = 5,
    ): DynamicFilterModel {
        return DynamicFilterModel(
            data = DataValue(
                filter = List(sizeFilter) {
                    Filter(
                        title = "Filter $it",
                        subTitle = "Subtitle $it",
                        options = List(3) { optionIdx ->
                            Option(
                                name = "Option $optionIdx",
                                value = "true",
                                inputType = "checkbox",
                                isPopular = true,
                                isNew = false,
                            )
                        }
                    )
                },
                sort = List(sizeSort) {
                    Sort(
                        name = "Sort $it",
                        key = "key $it",
                        value = "value $it",
                    )
                }
            )
        )
    }

    fun buildQuickFilterModel(
        name: String = "QuickFilter",
        icon: String = "",
        key: String = "key",
        value: String = "value",
    ) = QuickFilterUiModel(
        name = name,
        icon = icon,
        key = key,
        value = value,
    )

    fun buildQuickFilterList(
        size: Int = 5,
    ) = List(size) {
        QuickFilterUiModel(
            name = "QuickFilter $it",
            icon = "",
            key = "key$it",
            value = "value$it",
        )
    }

    fun buildSuggestionModel(
        text: String = "Do you mean \"pokemon\"?",
        query: String = "q=pokemon",
        suggestion: String = "pokemon",
    ) = SuggestionUiModel(
        text = text,
        query = query,
        suggestion = suggestion,
    )

    fun buildTickerModel(
        text: String = "This is ticker",
        query: String = "skip_rewrite=true",
    ) = TickerUiModel(
        text = text,
        query = query,
    )

    fun buildSortFilterModel(
        size: Int = 2,
    ) : Map<String, String> {
        val result = mutableMapOf<String, String>()

        repeat(size) {
            result["key$it"] = "value$it"
        }

        return result
    }
}