package com.tokopedia.favorite.domain.model

import com.tokopedia.abstraction.common.utils.paging.PagingHandler.PagingHandlerModel

/**
 * @author Kulomady on 1/19/17.
 */
data class FavoriteShop (
        var isDataValid: Boolean = false,
        var message: String? = null,
        var data: List<FavoriteShopItem>? = null,
        var isNetworkError: Boolean = false,
        var pagingModel: PagingHandlerModel? = null
)
