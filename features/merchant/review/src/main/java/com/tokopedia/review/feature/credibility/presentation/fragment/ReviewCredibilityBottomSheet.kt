package com.tokopedia.review.feature.credibility.presentation.fragment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.BottomsheetReviewCredibilityBinding
import com.tokopedia.review.feature.credibility.analytics.ReviewCredibilityTracking
import com.tokopedia.review.feature.credibility.di.ReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uistate.ReviewCredibilityGlobalErrorUiState
import com.tokopedia.review.feature.credibility.presentation.viewmodel.ReviewCredibilityViewModel
import com.tokopedia.review.feature.credibility.presentation.widget.ReviewCredibilityAchievementBoxWidget
import com.tokopedia.review.feature.credibility.presentation.widget.ReviewCredibilityFooterWidget
import com.tokopedia.review.feature.credibility.presentation.widget.ReviewCredibilityGlobalErrorWidget
import com.tokopedia.review.feature.credibility.presentation.widget.ReviewCredibilityHeaderWidget
import com.tokopedia.review.feature.credibility.presentation.widget.ReviewCredibilityStatisticBoxWidget
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

class ReviewCredibilityBottomSheet : BottomSheetUnify(), ReviewCredibilityFooterWidget.Listener,
    ReviewCredibilityHeaderWidget.Listener, ReviewCredibilityAchievementBoxWidget.Listener,
    ReviewCredibilityStatisticBoxWidget.Listener, ReviewCredibilityGlobalErrorWidget.Listener {

    companion object {
        private const val LOGIN_REQUEST_CODE = 200
        private const val ARG_USER_ID = "userID"
        private const val ARG_SOURCE = "source"
        private const val ARG_PRODUCT_ID = "productID"

        fun newInstance(userId: String, source: String, productId: String) =
            ReviewCredibilityBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                    putString(ARG_SOURCE, source)
                    putString(ARG_PRODUCT_ID, productId)
                }
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), viewModelFactory).get(ReviewCredibilityViewModel::class.java)
    }

    private var binding by viewBinding(BottomsheetReviewCredibilityBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        initData(savedInstanceState)
        initUiStateCollector()
        initBottomSheetParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = BottomsheetReviewCredibilityBinding.inflate(
            inflater, container, false
        ).apply { binding = this }.root
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RouteManager.route(context, viewModel.getPendingAppLink())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveUiState(outState)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        activity?.finish()
    }

    override fun onHeaderTransitionEnd() {
        viewModel.onHeaderStopTransitioning()
    }

    override fun onAchievementBoxTransitionEnd() {
        viewModel.onAchievementBoxStopTransitioning()
    }

    override fun onClickAchievementSticker(appLink: String, name: String) {
        val routed = RouteManager.route(context, appLink)
        if (routed) {
            ReviewCredibilityTracking.trackOnClickAchievementSticker(
                viewModel.isUsersOwnCredibility(),
                name,
                viewModel.getUserID(),
                viewModel.getReviewerUserID(),
                viewModel.getProductID()
            )
        }
    }

    override fun onClickSeeMoreAchievement(appLink: String, buttonText: String) {
        val routed = RouteManager.route(context, appLink)
        if (routed) {
            ReviewCredibilityTracking.trackOnClickSeeMoreAchievement(
                viewModel.isUsersOwnCredibility(),
                buttonText,
                viewModel.getUserID(),
                viewModel.getReviewerUserID(),
                viewModel.getProductID()
            )
        }
    }

    override fun onImpressAchievementStickers(achievements: List<ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel>) {
        ReviewCredibilityTracking.trackImpressAchievementStickers(
            viewModel.isUsersOwnCredibility(),
            achievements,
            viewModel.getUserID(),
            viewModel.getReviewerUserID(),
            viewModel.getProductID(),
            viewModel.getSource()
        )
    }

    override fun onStatisticBoxTransitionEnd() {
        viewModel.onStatisticBoxStopTransitioning()
    }

    override fun onClickFooterCTA(appLink: String): Boolean {
        return RouteManager.route(context, appLink)
    }

    override fun onClickCTAButton(appLink: String, buttonText: String) {
        if (viewModel.isUsersOwnCredibility()) {
            ReviewCredibilityTracking.trackOnClickCTASelfCredibility(
                buttonText, viewModel.getProductID(), viewModel.getUserID()
            )
        } else {
            ReviewCredibilityTracking.trackOnClickCTAOtherUserCredibility(
                buttonText,
                viewModel.getProductID(),
                viewModel.getUserID(),
                viewModel.getReviewerUserID()
            )
        }
        handleRouting(appLink)
    }

    override fun onFooterTransitionEnd() {
        viewModel.onFooterStopTransitioning()
    }

    override fun onMainCTAClicked() {
        viewModel.loadReviewCredibilityData()
    }

    override fun onGlobalErrorTransitionEnd() {
        viewModel.onGlobalErrorStopTransitioning()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initInjector() {
        (activity as? HasComponent<ReviewCredibilityComponent>)?.component?.inject(this)
    }

    private fun initUiStateCollector() {
        initHeaderUiStateCollector()
        initAchievementBoxUiStateCollector()
        initStatisticBoxUiStateCollector()
        initFooterUiStateCollector()
        initGlobalErrorUiStateCollector()
    }

    private fun initBottomSheetParams() {
        showKnob = true
        showCloseIcon = false
        showHeader = false
    }

    private fun initHeaderUiStateCollector() {
        collectLatestWhenResumed(viewModel.reviewCredibilityHeaderUiState) {
            binding?.widgetReviewCredibilityHeader?.updateUiState(it)
        }
    }

    private fun initAchievementBoxUiStateCollector() {
        collectLatestWhenResumed(viewModel.reviewCredibilityAchievementBoxUiState) {
            binding?.widgetReviewCredibilityAchievementBox?.updateUiState(it)
        }
    }

    private fun initStatisticBoxUiStateCollector() {
        collectLatestWhenResumed(viewModel.reviewCredibilityStatisticBoxUiState) {
            binding?.widgetReviewCredibilityStatisticBox?.updateUiState(it)
        }
    }

    private fun initFooterUiStateCollector() {
        collectLatestWhenResumed(viewModel.reviewCredibilityFooterUiState) {
            binding?.widgetReviewCredibilityFooter?.updateUiState(it)
        }
    }

    private fun initGlobalErrorUiStateCollector() {
        collectLatestWhenResumed(viewModel.reviewCredibilityGlobalErrorUiState) {
            binding?.globalErrorReviewCredibility?.updateUiState(it)
            toggleKnobVisibility(it is ReviewCredibilityGlobalErrorUiState.Hidden)
        }
    }

    private fun initViews() {
        binding?.widgetReviewCredibilityHeader?.setListener(this)
        binding?.widgetReviewCredibilityAchievementBox?.setListener(this)
        binding?.widgetReviewCredibilityStatisticBox?.setListener(this)
        binding?.widgetReviewCredibilityFooter?.setListener(this)
        binding?.globalErrorReviewCredibility?.setListener(this)
    }

    private fun initData(savedInstanceState: Bundle?) {
        viewModel.setProductID(getProductIDFromArguments())
        viewModel.setReviewerUserID(getReviewerUserIDFromArguments())
        viewModel.setSource(getSourceFromArguments())
        if (savedInstanceState == null) {
            viewModel.loadReviewCredibilityData()
        } else {
            viewModel.restoreUiState(savedInstanceState)
        }
    }

    private fun getProductIDFromArguments(): String {
        return arguments?.getString(ARG_PRODUCT_ID).orEmpty()
    }

    private fun getReviewerUserIDFromArguments(): String {
        return arguments?.getString(ARG_USER_ID).orEmpty()
    }

    private fun getSourceFromArguments(): String {
        return arguments?.getString(ARG_SOURCE).orEmpty()
    }

    private fun handleRouting(appLink: String) {
        if (viewModel.isFromInbox()) {
            if (appLink != ApplinkConst.REPUTATION) {
                RouteManager.route(context, appLink)
            }
            activity?.finish()
        } else {
            if (viewModel.isLoggedIn()) {
                activity?.finish()
                RouteManager.route(context, appLink)
            } else {
                viewModel.setPendingAppLink(appLink)
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), LOGIN_REQUEST_CODE)
            }
        }
    }

    private fun toggleKnobVisibility(show: Boolean) {
        knobView.showWithCondition(show)
        bottomSheetHeader.showWithCondition(show.not())
        bottomSheetClose.showWithCondition(show.not())
    }
}