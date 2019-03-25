package com.tokopedia.affiliate.feature.explore.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel
import rx.Subscriber

/**
 * @author by milhamj on 18/03/19.
 */
class GetExploreDataSubscriber(private val mainView: ExploreContract.View?,
                               private val isSearch: Boolean,
                               private val exploreParams: ExploreParams)
    : Subscriber<ExploreViewModel>() {

    override fun onNext(exploreViewModel: ExploreViewModel) {
        mainView?.hideLoading()
        mainView?.unsubscribeAutoComplete()

        if (exploreViewModel.exploreProducts.isEmpty()) {
            if (isSearch) {
                mainView?.affiliateAnalytics?.onSearchNotFound(exploreParams.keyword)
                mainView?.onEmptySearchResult()
            } else {
                mainView?.onEmptyProduct()
            }
        } else {
            mainView?.onSuccessGetData(
                    exploreViewModel.exploreProducts,
                    exploreViewModel.nextCursor,
                    isSearch
            )
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }

        mainView?.hideLoading()
        mainView?.unsubscribeAutoComplete()
        mainView?.onErrorGetData(ErrorHandler.getErrorMessage(mainView.context, e))
    }
}