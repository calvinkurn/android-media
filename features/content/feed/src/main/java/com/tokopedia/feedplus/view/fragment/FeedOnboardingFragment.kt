package com.tokopedia.feedplus.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.activity.FollowRecomActivity
import com.tokopedia.feedplus.view.activity.FeedOnboardingActivity
import com.tokopedia.feedplus.view.adapter.viewholder.onboarding.OnboardingAdapter
import com.tokopedia.feedplus.view.di.DaggerFeedPlusComponent
import com.tokopedia.feedplus.view.presenter.FeedOnboardingViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.SubmitInterestResponseViewModel
import com.tokopedia.kol.KolComponentInstance
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_feed_onboarding.*
import javax.inject.Inject

/**
 * @author by milhamj on 2019-09-20.
 */
class FeedOnboardingFragment : BaseDaggerFragment(), OnboardingAdapter.InterestPickItemListener, FeedOnboardingActivity.FeedOnboardingActivityListener {

    companion object {
        private val OPEN_RECOM_PROFILE = 1236
        val EXTRA_SELECTED_IDS = "EXTRA_SELECTED_IDS"
        fun getInstance(bundle: Bundle): FeedOnboardingFragment {
            val fragment = FeedOnboardingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var feedAnalyticTracker: FeedAnalyticTracker

    private var selectedIdList: List<Int> = arrayListOf()

    private val adapter : OnboardingAdapter by lazy {
        OnboardingAdapter(this, "")
    }

    lateinit var data: OnboardingViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var feedOnboardingPresenter: FeedOnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedIdList = arguments?.getIntegerArrayList(EXTRA_SELECTED_IDS)?.toList() ?: arrayListOf()
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

        feedOnboardingPresenter.submitInterestPickResp.observe(this, Observer {
            view?.hideLoadingTransparent()
            when (it) {
                is Success -> onSuccessSubmitInterestData(it.data)
                is Fail -> onErrorSubmitInterestPickData(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedAnalyticTracker.eventOpenInterestPickDetail()
        initView()
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when(requestCode) {
            OPEN_RECOM_PROFILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    activity?.let {
                        it.setResult(Activity.RESULT_OK, Intent())
                        it.finish()
                    }
                }
            }
        }
    }

    override fun getScreenName(): String =  FeedAnalyticTracker.Screen.INTEREST_PICK_DETAIL

    override fun initInjector() {
        activity?.application?.let {
            DaggerFeedPlusComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(it))
                    .build()
                    .inject(this)
        }
    }

    override fun onInterestPickItemClicked(item: OnboardingDataViewModel) {
        checkButtonSaveInterest()
        feedAnalyticTracker.eventClickFeedInterestPick(item.name)
    }

    override fun onLihatSemuaItemClicked(selectedItemList: List<OnboardingDataViewModel>) {
    }

    override fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<OnboardingDataViewModel>) {
    }

    override fun onBackPressedOnActivity(): Intent {
        return getIntentResult()
    }

    private fun initView() {
        interestList.adapter = adapter
        interestList.addItemDecoration(OnboardingAdapter.getItemDecoration())
        saveInterest.setOnClickListener {
            feedAnalyticTracker.eventClickFeedCheckInspiration(adapter.getSelectedItemIdList().size.toString())
            view?.showLoadingTransparent()
            feedOnboardingPresenter.submitInterestPickData(adapter.getSelectedItems(), "", OPEN_RECOM_PROFILE)
        }
    }

    private fun loadData() {
        feedOnboardingPresenter.getOnboardingData("",  true)
        showLoading()
    }

    private fun checkButtonSaveInterest() {
        saveInterest.isEnabled = data.minimumPick <= adapter.getSelectedItems().size
    }

    private fun showLoading() {
        loadingLayout.visible()
    }

    private fun hideLoading() {
        loadingLayout.gone()
    }

    private fun onSuccessGetData(data: OnboardingViewModel) {
        this.data = data
        adapter.setList(data.dataList)
        adapter.setSelectedItemIds(selectedIdList)
        titleTextView.text = String.format(data.titleFull, data.minimumPick)
        checkButtonSaveInterest()
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

    private fun onSuccessSubmitInterestData(data: SubmitInterestResponseViewModel) {
        context?.let {
            startActivityForResult(FollowRecomActivity.createIntent(it, data.idList.toIntArray()), OPEN_RECOM_PROFILE)
        }
    }

    private fun onErrorSubmitInterestPickData(throwable: Throwable) {
        view?.let{
            Toaster.showError(it,
                    ErrorHandler.getErrorMessage(activity, throwable),
                    Snackbar.LENGTH_LONG
            )
        }
    }

    fun getIntentResult() : Intent {
        val intent = Intent()
        intent.putIntegerArrayListExtra(EXTRA_SELECTED_IDS, ArrayList(adapter.getSelectedItemIdList()))
        return intent
    }
}