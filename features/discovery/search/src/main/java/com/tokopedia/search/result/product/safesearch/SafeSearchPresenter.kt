package com.tokopedia.search.result.product.safesearch

import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.result.domain.model.UserDOB
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.TickerDataView

interface SafeSearchPresenter {
    val isShowAdult: Boolean
    fun modifySearchParameterIfShowAdultEnabled(searchParameter: SearchParameter)
    fun showAdultForAdultTicker(ticker: TickerDataView)
    fun onSafeSearchViewDestroyed()
    fun setUserProfileDob(userDob : UserDOB)
    fun showBottomSheetInappropriate(itemProduct: ProductItemDataView)
    fun isShowAdultEnableAndProfileVerify() : Boolean
}
