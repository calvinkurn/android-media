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
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.adapter.FollowRecomAdapter
import com.tokopedia.feedplus.profilerecommendation.view.contract.FollowRecomContract
import com.tokopedia.feedplus.profilerecommendation.view.presenter.FollowRecomPresenter
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomInfoViewModel
import com.tokopedia.feedplus.profilerecommendation.di.DaggerFollowRecomComponent
import com.tokopedia.feedplus.profilerecommendation.view.custom.DialogOnboardingRecomFollowView
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecomAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardThumbnailViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardViewModel
import com.tokopedia.kol.feature.video.view.activity.MediaPreviewActivity
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
class FollowRecomFragment : BaseDaggerFragment(), FollowRecomContract.View, FollowRecomAdapter.ActionListener, DialogOnboardingRecomFollowView.ActionListener {

    companion object {

        const val EXTRA_INTEREST_IDS = "interest_ids"

        fun newInstance(interestIds: IntArray): FollowRecomFragment {
            val fragment = FollowRecomFragment()
            val args = Bundle()
            args.putIntArray(EXTRA_INTEREST_IDS, interestIds)
            fragment.arguments = args
            return fragment
        }
    }

    var cursor = ""

    @Inject
    lateinit var presenter: FollowRecomPresenter

    private val dialogOnboardingFollowView: DialogOnboardingRecomFollowView? by lazy {
        context?.let(::DialogOnboardingRecomFollowView)
    }
    private val dialogOnboardingFollow: CloseableBottomSheetDialog by lazy {
        CloseableBottomSheetDialog.createInstanceRounded(context).apply {
            dialogOnboardingFollowView?.let { view -> setCustomContentView(view, "Dialog Onboarding Follow", false) }
            setCancelable(false)
        }
    }
    private val interestIds: IntArray
        get() = arguments?.getIntArray(EXTRA_INTEREST_IDS) ?: intArrayOf()

    private lateinit var followRecomAdapter: FollowRecomAdapter
    private lateinit var rvFollowRecom: RecyclerView
    private lateinit var btnAction: UnifyButton
    private lateinit var tvInfo: TextView
    private lateinit var infoViewModel: FollowRecomInfoViewModel
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun getScreenName(): String {
        return "Follow Recommendation"
    }

    override fun initInjector() {
        val component = DaggerFollowRecomComponent.builder().baseAppComponent(
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

    override fun onGetFollowRecommendationList(recomList: List<FollowRecomCardViewModel>, cursor: String) {
        followRecomAdapter.apply {
            hideLoading()
            scrollListener.updateStateAfterGetData()
            scrollListener.setHasNextPage(true)
            addItems(recomList)
        }
        this.cursor = cursor
    }

    override fun onGetFollowRecommendationInfo(infoViewModel: FollowRecomInfoViewModel) {
        this.infoViewModel = infoViewModel
        setupInfo(infoViewModel, if(!::followRecomAdapter.isInitialized) followRecomAdapter.getFollowedCount() else 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onFollowButtonClicked(authorId: String, isFollowed: Boolean, actionToCall: FollowRecomAction) {
        presenter.followUnfollowRecommendation(authorId, actionToCall)
    }

    override fun onFollowStateChanged(followCount: Int) {
        setupInfo(infoViewModel, followCount)
    }

    override fun onSuccessFollowRecommendation(id: String) {
        followRecomAdapter.updateFollowState(id, FollowRecomAction.FOLLOW)
        updateDialogIfApplicable(id)
    }

    override fun onSuccessUnfollowRecommendation(id: String) {
        followRecomAdapter.updateFollowState(id, FollowRecomAction.UNFOLLOW)
        updateDialogIfApplicable(id)
    }

    override fun onSuccessFollowAllRecommendation() {
        presenter.setOnboardingStatus()
    }

    override fun onFinishSetOnboardingStatus() {
        openFeed()
    }

    override fun onGetError(error: Throwable) {
        onGetError(ErrorHandler.getErrorMessage(context, error))
    }

    override fun onGetError(error: String) {
        followRecomAdapter.hideLoading()
        view?.let { view -> Toaster.showError(view, error, 2000) }
    }

    override fun onNameOrAvatarClicked(model: FollowRecomCardViewModel) {
        setupDialogViewWithModel(model)
        dialogOnboardingFollow.show()
    }

    override fun showLoading() {
        view?.showLoadingTransparent()
    }

    override fun hideLoading() {
        view?.hideLoadingTransparent()
    }

    override fun onCloseButtonClicked() {
        dialogOnboardingFollow.dismiss()
    }

    override fun onFollowButtonClicked(authorId: String, action: FollowRecomAction) {
        presenter.followUnfollowRecommendation(authorId, action)
    }

    override fun onThumbnailClicked(model: FollowRecomCardThumbnailViewModel) {
        context
                ?.let { ctx -> MediaPreviewActivity.createIntent(ctx, model.id, 0) }
                ?.run(::startActivity)
    }

    private fun initView(view: View) {
        view.run {
            rvFollowRecom = findViewById(R.id.rv_follow_recom)
            btnAction = findViewById(R.id.btn_action)
            tvInfo = findViewById(R.id.tv_info)
        }
    }

    private fun setupView(view: View) {
        followRecomAdapter = FollowRecomAdapter(emptyList(), this)
        rvFollowRecom.apply {
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = followRecomAdapter
            this.layoutManager = layoutManager
            scrollListener = object: EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (cursor != "") {
                        followRecomAdapter.showLoading()
                        presenter.getFollowRecommendationList(interestIds, cursor)
                    }
                }
            }
            addOnScrollListener(scrollListener)
        }
        btnAction.setOnClickListener { onBtnActionClicked() }
    }

    private fun setupInfo(infoViewModel: FollowRecomInfoViewModel, numOfFollowed: Int) {
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
        if (infoViewModel.minFollowed <= followRecomAdapter.getFollowedCount()) presenter.setOnboardingStatus()
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

    private fun updateDialogIfApplicable(id: String) {
        if (dialogOnboardingFollowView?.tag == id)
            followRecomAdapter.getItemByAuthorId(id)?.let(::setupDialogViewWithModel)
    }

    private fun setupDialogViewWithModel(model: FollowRecomCardViewModel) {
        dialogOnboardingFollowView?.apply {
            setupDialog(
                    authorId = model.authorId,
                    name = model.title,
                    avatarUrl = model.avatar,
                    badgeUrl = model.badgeUrl,
                    instruction = model.followInstruction,
                    isFollowed = model.isFollowed
            )
            listener = this@FollowRecomFragment
        }
    }
}