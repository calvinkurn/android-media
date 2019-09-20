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
import com.tokopedia.feedplus.view.adapter.viewholder.onboarding.OnboardingAdapter
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.presenter.FeedOnboardingViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
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
class FeedOnboardingFragment : BaseDaggerFragment(), OnboardingAdapter.InterestPickItemListener {

    private val adapter : OnboardingAdapter by lazy {
        OnboardingAdapter(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var feedOnboardingPresenter: FeedOnboardingViewModel

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
        initView()
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

    override fun onInterestPickItemClicked(item: OnboardingDataViewModel) {
    }

    override fun onLihatSemuaItemClicked(selectedItemList: List<OnboardingDataViewModel>) {
    }

    override fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<OnboardingDataViewModel>) {
    }

    private fun initView() {
        interestList.adapter = adapter
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
        adapter.setList(data.dataList)
        titleTextView.text = String.format(
                getString(R.string.feed_choose_topic_you_like, data.minimumPick.toString())
        )
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