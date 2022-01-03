package com.tokopedia.search.result.shop.presentation.model

import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Lazy

internal data class ShopSearchUseCaseDataView(
    val searchShopFirstPageUseCase: Lazy<UseCase<SearchShopModel>>,
    val searchShopLoadMoreUseCase: Lazy<UseCase<SearchShopModel>>,
    val getDynamicFilterUseCase: Lazy<UseCase<DynamicFilterModel>>,
    val getShopCountUseCase: Lazy<UseCase<Int>>
)