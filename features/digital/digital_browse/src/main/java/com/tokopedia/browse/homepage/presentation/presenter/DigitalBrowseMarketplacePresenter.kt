package com.tokopedia.browse.homepage.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.browse.R
import com.tokopedia.browse.common.data.DigitalBrowsePopularAnalyticsModel
import com.tokopedia.browse.homepage.domain.subscriber.GetMarketplaceSubscriber
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseMarketplaceUseCase
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseMarketplaceContract
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseMarketplacePresenter @Inject
constructor(private val digitalBrowseMarketplaceUseCase: DigitalBrowseMarketplaceUseCase) :
        BaseDaggerPresenter<DigitalBrowseMarketplaceContract.View>(), DigitalBrowseMarketplaceContract.Presenter,
        GetMarketplaceSubscriber.MarketplaceActionListener {

    private val compositeSubscription: CompositeSubscription = CompositeSubscription()

    override fun onInit() {
        getMarketplaceDataCache()
    }

    private fun getMarketplaceDataCache() {
        compositeSubscription.add(
                digitalBrowseMarketplaceUseCase.getCache()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(GetMarketplaceSubscriber(object : GetMarketplaceSubscriber.MarketplaceActionListener {
                            override fun onErrorGetMarketplace(throwable: Throwable) {
                                getMarketplaceDataCloud()
                            }

                            override fun onSuccessGetMarketplace(digitalBrowseMarketplaceData: DigitalBrowseMarketplaceViewModel) {
                                if (digitalBrowseMarketplaceData.rowViewModelList!!.isNotEmpty() &&
                                        digitalBrowseMarketplaceData.popularBrandsList!!.isNotEmpty()) {
                                    view.renderData(digitalBrowseMarketplaceData)
                                }

                                getMarketplaceDataCloud()
                            }
                        }))
        )
    }

    override fun getMarketplaceDataCloud() {
        compositeSubscription.add(
                digitalBrowseMarketplaceUseCase.createObservable(
                        GraphqlHelper.loadRawString(view.fragmentContext!!.resources,
                                R.raw.digital_browse_category_query),
                        GraphqlHelper.loadRawString(view.fragmentContext!!.resources,
                                R.raw.digital_browse_brand_query))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(GetMarketplaceSubscriber(this))
        )
    }

    override fun onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }

        detachView()
    }

    override fun getPopularAnalyticsModel(viewModel: DigitalBrowsePopularBrandsViewModel, position: Int): DigitalBrowsePopularAnalyticsModel =
            DigitalBrowsePopularAnalyticsModel(viewModel.id, viewModel.name!!, position + 1)

    override fun getPopularAnalyticsModelList(popularBrandsList: List<DigitalBrowsePopularBrandsViewModel>): List<DigitalBrowsePopularAnalyticsModel> {
        var position = 0
        val popularAnalyticsModelList = ArrayList<DigitalBrowsePopularAnalyticsModel>()

        for (item in popularBrandsList) {
            position++
            val popularAnalyticsItem = DigitalBrowsePopularAnalyticsModel(item.id, item.name!!, position)
            popularAnalyticsModelList.add(popularAnalyticsItem)
        }

        return popularAnalyticsModelList
    }

    override fun onErrorGetMarketplace(throwable: Throwable) {
        if (isViewAttached) {
            if (view.getCategoryItemCount() < 2) {
                view.showGetDataError(throwable)
            }
        }
    }

    override fun onSuccessGetMarketplace(digitalBrowseMarketplaceData: DigitalBrowseMarketplaceViewModel) {
        view.renderData(digitalBrowseMarketplaceData)
        view.sendPopularImpressionAnalytics(getPopularAnalyticsModelList(
                digitalBrowseMarketplaceData.popularBrandsList!!))
    }
}
