package com.tokopedia.feedplus.profilerecommendation.view.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.adapter.FollowRecommendationAdapter
import com.tokopedia.feedplus.profilerecommendation.view.contract.FollowRecommendationContract
import com.tokopedia.feedplus.profilerecommendation.view.presenter.FollowRecommendationPresenter
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationInfoViewModel
import com.tokopedia.feedplus.profilerecommendation.di.DaggerFollowRecommendationComponent
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecommendationAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationCardViewModel
import com.tokopedia.kotlin.extensions.view.hideLoadingTransparent
import com.tokopedia.kotlin.extensions.view.showLoadingTransparent
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationFragment : BaseDaggerFragment(), FollowRecommendationContract.View, FollowRecommendationAdapter.ActionListener {

    companion object {

        const val EXTRA_INTEREST_IDS = "interest_ids"

        fun newInstance(interestIds: IntArray): FollowRecommendationFragment {
            val fragment = FollowRecommendationFragment()
            val args = Bundle()
            args.putIntArray(EXTRA_INTEREST_IDS, interestIds)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var presenter: FollowRecommendationPresenter

    var cursor = ""

    private lateinit var followRecommendationAdapter: FollowRecommendationAdapter

    private lateinit var rvFollowRecom: RecyclerView
    private lateinit var btnAction: UnifyButton
    private lateinit var tvInfo: TextView

    private lateinit var infoViewModel: FollowRecommendationInfoViewModel

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private val interestIds: IntArray
        get() = arguments?.getIntArray(EXTRA_INTEREST_IDS) ?: intArrayOf()

    override fun getScreenName(): String {
        return "Follow Recommendation"
    }

    override fun initInjector() {
        val component = DaggerFollowRecommendationComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                .build()

        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        return inflater.inflate(R.layout.fragment_follow_recommendation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        presenter.getFollowRecommendationList(interestIds, cursor)
    }

    private fun initView(view: View) {
        view.run {
            rvFollowRecom = findViewById(R.id.rv_follow_recom)
            btnAction = findViewById(R.id.btn_action)
            tvInfo = findViewById(R.id.tv_info)
        }
    }

    private fun setupView(view: View) {
        followRecommendationAdapter = FollowRecommendationAdapter(emptyList(), this)
        rvFollowRecom.apply {
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = followRecommendationAdapter
            this.layoutManager = layoutManager
            scrollListener = object: EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (cursor != "") {
                        followRecommendationAdapter.showLoading()
                        presenter.getFollowRecommendationList(interestIds, cursor)
                    }
                }
            }
            addOnScrollListener(scrollListener)
        }
        btnAction.setOnClickListener { onBtnActionClicked() }
    }

    override fun onGetFollowRecommendationList(recomList: List<FollowRecommendationCardViewModel>, cursor: String) {
        followRecommendationAdapter.apply {
            hideLoading()
            scrollListener.updateStateAfterGetData()
            scrollListener.setHasNextPage(true)
            addItems(recomList)
        }
        this.cursor = cursor
    }

    override fun onGetFollowRecommendationInfo(infoViewModel: FollowRecommendationInfoViewModel) {
        this.infoViewModel = infoViewModel
        setupInfo(infoViewModel, if(!::followRecommendationAdapter.isInitialized) followRecommendationAdapter.getFollowedCount() else 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onFollowButtonClicked(authorId: String, isFollowed: Boolean, actionToCall: FollowRecommendationAction) {
        presenter.followUnfollowRecommendation(authorId, actionToCall)
    }

    override fun onFollowStateChanged(followCount: Int) {
        setupInfo(infoViewModel, followCount)
    }

    override fun onSuccessFollowRecommendation(id: String) {
        followRecommendationAdapter.updateFollowState(id, FollowRecommendationAction.FOLLOW)
    }

    override fun onSuccessUnfollowRecommendation(id: String) {
        followRecommendationAdapter.updateFollowState(id, FollowRecommendationAction.UNFOLLOW)
    }

    override fun onSuccessFollowAllRecommendation() {
        presenter.setOnboardingStatus()
    }

    override fun onSuccessSetOnboardingStatus() {
        openFeed()
    }

    override fun onGetError(error: Throwable) {
        onGetError(ErrorHandler.getErrorMessage(context, error))
    }

    override fun onGetError(error: String) {
        followRecommendationAdapter.hideLoading()
        view?.let { view -> Toaster.showError(view, error, 2000) }
    }

    override fun showLoading() {
        view?.showLoadingTransparent()
    }

    override fun hideLoading() {
        view?.hideLoadingTransparent()
    }

    private fun setupInfo(infoViewModel: FollowRecommendationInfoViewModel, numOfFollowed: Int) {
        if (infoViewModel.minFollowed <= numOfFollowed) {
            btnAction.text = getString(R.string.feed_open_feed)
            tvInfo.text = getString(R.string.feed_finish_follow_recommendation)
        }
        else {
            btnAction.text = infoViewModel.buttonCTA
            tvInfo.text = try { String.format(infoViewModel.instructionText, infoViewModel.minFollowed - numOfFollowed) } catch (e: Exception) { "" }
        }
    }

    private fun onBtnActionClicked() {
        if (infoViewModel.minFollowed <= followRecommendationAdapter.getFollowedCount()) presenter.setOnboardingStatus()
        else followAllRecommendation()
    }

    private fun openFeed() {
        if (RouteManager.isSupportApplink(context, ApplinkConst.FEED)) {
            RouteManager.route(context, ApplinkConst.FEED)
        }
    }

    private fun followAllRecommendation() {
        presenter.followAllRecommendation(interestIds)
    }
}