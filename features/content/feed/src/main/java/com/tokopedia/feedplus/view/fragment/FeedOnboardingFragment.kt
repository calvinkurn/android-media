package com.tokopedia.feedplus.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.presenter.FeedOnboardingViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.kotlin.extensions.view.showLoading
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_feed_onboarding.*
import javax.inject.Inject

/**
 * @author by milhamj on 2019-09-20.
 */
class FeedOnboardingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var feedOnboardingPresenter: FeedOnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            feedOnboardingPresenter = viewModelProvider.get(FeedOnboardingViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feedOnboardingPresenter.onboardingResp.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> onSuccessGetData(it.data)
                is Fail -> onErrorGetData(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    override fun getScreenName(): String =  ""

    override fun initInjector() {
        activity?.application?.let {
            DaggerFeedPlusComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(it))
                    .build()
                    .inject(this)
        }
    }

    private fun loadData() {
        feedOnboardingPresenter.getOnboardingData("",  true)
        showLoading()
    }

    private fun showLoading() {
        mainView.showLoading()
    }

    private fun hideLoading() {
        mainView.hideLoading()
    }

    private fun onSuccessGetData(data: OnboardingViewModel) {

    }

    private fun onErrorGetData(throwable: Throwable) {
        context?.let {
            NetworkErrorHelper.showEmptyState(it,
                    mainView,
                    ErrorHandler.getErrorMessage(it, throwable)) {
                loadData()
            }
        }
    }
}