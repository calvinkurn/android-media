package com.tokopedia.autocompletecomponent.universal.presenter.viewmodel.testinstance

import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel

internal val universalSearchItems = listOf(
    UniversalSearchModel.UniversalSearchItem(
        id = "6303",
        template = "carousel",
        title = "testing",
        type = "highlighted",
        trackingOption = 3,
        componentId = "05.01.01.00"
    ),
    UniversalSearchModel.UniversalSearchItem(
        id = "",
        template = "double_line",
        title = "Cek juga halaman ini",
        type = "related",
        trackingOption = 0,
        componentId = ""
    ),
    UniversalSearchModel.UniversalSearchItem(
        id = "",
        template = "list_grid",
        title = "Yang lagi promo hari ini",
        type = "related",
        trackingOption = 3,
        componentId = "05.03.03.00"
    )
)

internal val universalSearchData = UniversalSearchModel.UniversalSearchData(universalSearchItems)
internal val universalSearchHeader = UniversalSearchModel.UniversalSearchHeader()
internal val universalSearch = UniversalSearchModel.UniversalSearch(universalSearchHeader, universalSearchData)
internal val universalSearchModel = UniversalSearchModel(universalSearch)