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
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationFragment : BaseDaggerFragment(), FollowRecommendationContract.View, FollowRecommendationAdapter.ActionListener {

    companion object {

        fun newInstance(): FollowRecommendationFragment {
            return FollowRecommendationFragment()
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
        presenter.getFollowRecommendationList(listOf(1), cursor)
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
            adapter = followRecommendationAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        btnAction.setOnClickListener { onBtnActionClicked() }
    }

    override fun onGetFollowRecommendationList(recomList: List<FollowRecommendationCardViewModel>, cursor: String) {
        followRecommendationAdapter.apply {
            addItems(recomList)
        }
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

    override fun onGetError(error: String) {
        view?.let { view -> Toaster.showError(view, error, 2000) }
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
        if (infoViewModel.minFollowed <= followRecommendationAdapter.getFollowedCount()) openFeed()
        else followAllRecommendation()
    }

    private fun openFeed() {
        if (RouteManager.isSupportApplink(context, ApplinkConst.FEED)) {
            RouteManager.route(context, ApplinkConst.FEED)
        }
    }

    private fun followAllRecommendation() {
        TODO("Handle follow all recommendation")
    }
}