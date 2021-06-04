package com.tokopedia.tokomart.data

import com.tokopedia.tokomart.home.domain.model.*

fun createHomeLayoutList(): List<HomeLayoutResponse> {
    return listOf(
                HomeLayoutResponse(
                        id = "34923",
                        layout = "category_tokonow",
                        header = Header(
                                name = "Category Tokonow",
                                serverTimeUnix = 0
                        ),
                )
            )
}

fun createTicker(): TickerResponse {
    return  TickerResponse(
                ticker = Tickers(
                        tickerList = listOf(
                                Ticker(
                                        id = "10",
                                        title = "Welcome to Tokonow",
                                        message = "Tokonow is one of the best feature",
                                        color = "#FFF"
                                )

                        )
                )
    )
}

fun createKeywordSearch(): KeywordSearchData {
    return KeywordSearchData(
            searchData = SearchPlaceholder(
                    data = Data(
                            keyword = "Hello World",
                            placeholder = "Search tomato"
                    )
            )
    )
}