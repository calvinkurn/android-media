package com.tokopedia.search.result.product.safesearch

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.result.domain.model.UserDOB
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.product.dialog.BottomSheetInappropriateView
import javax.inject.Inject

class SafeSearchPresenterDelegate @Inject constructor(
    private val safeSearchPreference: MutableSafeSearchPreference,
    private val safeSearchView: SafeSearchView,
    private val bottomSheetInappropriateView : BottomSheetInappropriateView,
) : SafeSearchPresenter {
    override val isShowAdult: Boolean
        get() = safeSearchPreference.isShowAdult

    private var userDob : UserDOB = UserDOB()

    override fun modifySearchParameterIfShowAdultEnabled(searchParameter: SearchParameter) {
        if (isShowAdult) {
            searchParameter.set(SearchApiConst.SHOW_ADULT, SearchApiConst.SHOW_ADULT_ENABLED)
        }
    }

    private fun enableShowAdult() {
        safeSearchPreference.isShowAdult = true
        safeSearchView.registerSameSessionListener(safeSearchPreference)
    }

    private fun isShowAdultTicker(ticker: TickerDataView): Boolean {
        return SAFE_SEARCH_TICKER_COMPONENT_ID == ticker.componentId
    }

    override fun showAdultForAdultTicker(ticker: TickerDataView) {
        if (isShowAdultTicker(ticker)) {
            enableShowAdult()
        }
    }

    override fun onSafeSearchViewDestroyed() {
        safeSearchView.release()
    }

    override fun showBottomSheetInappropriate(itemProduct: ProductItemDataView) {
        bottomSheetInappropriateView.openInappropriateWarningBottomSheet(
            itemProduct,
            isAdultUser(),
        ) {
            enableShowAdult()
        }
    }

    override fun setUserProfileDob(userDob : UserDOB) {
        this.userDob = userDob
    }

    override fun isShowAdultEnableAndProfileVerify() = isShowAdult
        && userDob.isAdultAndVerify()

    private fun isAdultUser() = userDob.isAdultAndVerify()

}
