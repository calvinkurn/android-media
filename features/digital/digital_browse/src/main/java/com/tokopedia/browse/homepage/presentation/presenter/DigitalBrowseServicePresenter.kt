package com.tokopedia.browse.homepage.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.browse.R
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel
import com.tokopedia.browse.homepage.domain.subscriber.GetDigitalCategorySubscriber
import com.tokopedia.browse.homepage.domain.usecase.DigitalBrowseServiceUseCase
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseServiceContract
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel
import com.tokopedia.browse.homepage.presentation.model.IndexPositionModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 07/09/18.
 */

class DigitalBrowseServicePresenter @Inject
constructor(private val digitalBrowseServiceUseCase: DigitalBrowseServiceUseCase) :
        BaseDaggerPresenter<DigitalBrowseServiceContract.View>(), DigitalBrowseServiceContract.Presenter,
        GetDigitalCategorySubscriber.DigitalCategoryActionListener {

    private val compositeSubscription: CompositeSubscription = CompositeSubscription()

    override fun onInit() {
        getCategoryDataFromCache()
    }

    override fun getDigitalCategoryCloud() {
        compositeSubscription.add(
                digitalBrowseServiceUseCase.createObservable(
                        GraphqlHelper.loadRawString(view.fragmentContext!!.resources,
                                R.raw.digital_browse_category_query))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(GetDigitalCategorySubscriber(this, view.fragmentContext!!))
        )
    }

    override fun processTabData(titleMap: Map<String, IndexPositionModel>, viewModel: DigitalBrowseServiceViewModel, categoryId: Int) {
        view.showTab()

        var selectedTabIndex = 0

        val selectedTab: String? = viewModel.categoryViewModelList!!
                .firstOrNull { it.isTitle && it.id == categoryId }
                ?.name
                ?: ""

        val titleList = ArrayList<String>()
        for (i in 0 until titleMap.size) {
            titleList.add("")
        }

        for ((key, value) in titleMap) {
            titleList[value.indexPositionInTab] = key

            if (key == selectedTab) {
                selectedTabIndex = value.indexPositionInTab
            }
        }

        titleList
                .filter { it != "" }
                .forEach { view.addTab(it) }

        view.renderTab(selectedTabIndex)
    }

    override fun getItemPositionInGroup(titleMap: Map<String, IndexPositionModel>, itemPositionInList: Int): DigitalBrowseServiceAnalyticsModel {
        val model = DigitalBrowseServiceAnalyticsModel()
        var lastTitlePosition = 0

        for ((key, value) in titleMap) {
            if (value.indexPositionInList in lastTitlePosition..(itemPositionInList - 1)) {

                lastTitlePosition = value.indexPositionInList

                model.headerName = key
                model.headerPosition = value.indexPositionInTab + 1
            }
        }

        model.iconPosition = itemPositionInList - lastTitlePosition

        return model
    }

    override fun onErrorGetDigitalCategory(throwable: Throwable) {
        if (isViewAttached) {
            if (view.getItemCount() < 2) {
                view.showGetDataError(throwable)
            }
        }
    }

    override fun onSuccessGetDigitalCategory(digitalBrowseServiceViewModel: DigitalBrowseServiceViewModel) {
        view.renderData(digitalBrowseServiceViewModel)
    }

    private fun getCategoryDataFromCache() {
        compositeSubscription.add(
                digitalBrowseServiceUseCase.getCategoryDataFromCache()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(GetDigitalCategorySubscriber(object : GetDigitalCategorySubscriber.DigitalCategoryActionListener {
                            override fun onErrorGetDigitalCategory(throwable: Throwable) {
                                getDigitalCategoryCloud()
                            }

                            override fun onSuccessGetDigitalCategory(digitalBrowseServiceViewModel: DigitalBrowseServiceViewModel) {
                                if (digitalBrowseServiceViewModel.categoryViewModelList != null &&
                                        digitalBrowseServiceViewModel.categoryViewModelList.isNotEmpty()) {
                                    view.renderData(digitalBrowseServiceViewModel)
                                }

                                getDigitalCategoryCloud()
                            }
                        }, view.fragmentContext!!))
        )
    }

    override fun onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }

        detachView()
    }
}
