package com.tokopedia.hotel.search_map.data

import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel

data class HotelLoadingModel (
        var isForHorizontalItem: Boolean = false) : LoadingModel()