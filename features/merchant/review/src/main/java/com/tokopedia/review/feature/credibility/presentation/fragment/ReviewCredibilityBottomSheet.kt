package com.tokopedia.review.feature.credibility.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInboxInstance
import com.tokopedia.review.feature.credibility.analytics.ReviewCredibilityTracking
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityLabel
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStat
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.di.DaggerReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.di.ReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.presentation.viewmodel.ReviewCredibilityViewModel
import com.tokopedia.review.feature.credibility.presentation.widget.ReviewCredibilityStatisticBoxWidget
import com.tokopedia.review.feature.inbox.pending.presentation.fragment.ReviewPendingFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewCredibilityBottomSheet : BottomSheetUnify(), HasComponent<ReviewCredibilityComponent> {

    companion object {
        const val LOGIN_REQUEST_CODE = 200
        const val READING_SOURCE_PARAM = "review-list"
        fun newInstance(userId: String, source: String, productId: String) = ReviewCredibilityBottomSheet().apply {
            this.userId = userId
            this.source = source
            this.productId = productId
        }
    }

    @Inject
    lateinit var viewModel: ReviewCredibilityViewModel
    private var userId: String = ""
    private var source: String = ""
    private var productId: String = ""

    private var reviewerName: Typography? = null
    private var joinDate: Typography? = null
    private var footer: Typography? = null
    private var mainButton: UnifyButton? = null
    private var learnMore: Typography? = null
    private var statisticsBox: ReviewCredibilityStatisticBoxWidget? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var loadingView: View? = null
    private var loadingBox: ConstraintLayout? = null
    private var globalError: GlobalError? = null

    private var applink = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.bottomsheet_review_credibility, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setDismissBehavior()
        observeReviewerCredibility()
        getReviewerCredibility()
    }

    override fun getComponent(): ReviewCredibilityComponent? {
        return activity?.run {
            DaggerReviewCredibilityComponent
                .builder()
                .reviewInboxComponent(ReviewInboxInstance.getComponent(application))
                .build()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            RouteManager.route(context, applink)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun bindViews(view: View) {
        with(view) {
            reviewerName = findViewById(R.id.review_credibility_reviewer_name)
            joinDate = findViewById(R.id.review_credibility_join_date)
            footer = findViewById(R.id.review_credibility_footer)
            mainButton = findViewById(R.id.review_credibility_button)
            learnMore = findViewById(R.id.review_credibility_learn_more)
            statisticsBox = findViewById(R.id.review_credibility_statistics_box)
            loadingView = findViewById(R.id.review_credibility_loading)
            coordinatorLayout = findViewById(R.id.review_credibility_coordinator_layout)
            loadingBox = findViewById(R.id.review_credibility_statistics_loading_box)
            globalError = findViewById(R.id.review_credibility_error)
        }
    }

    private fun getReviewerCredibility() {
        showLoading()
        globalError?.hide()
        viewModel.getReviewCredibility(mapSourceToParam(), userId)
    }

    private fun observeReviewerCredibility() {
        viewModel.reviewerCredibility.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetReviewerCredibility(it.data)
                is Fail -> onFailGetReviewerCredibility()
            }
        })
    }

    private fun onSuccessGetReviewerCredibility(reviewCredibility: ReviewerCredibilityStatsWrapper) {
        hideLoading()
        globalError?.hide()
        showKnob()
        with(reviewCredibility) {
            setupLabels(label)
            setupStatistics(label.subtitle, stats)
        }
    }

    private fun onFailGetReviewerCredibility() {
        globalError?.apply {
            show()
            hideKnob()
            setActionClickListener {
                getReviewerCredibility()
            }
            setSecondaryActionClickListener {
                dismiss()
                RouteManager.route(context, ApplinkConstInternalGlobal.GENERAL_SETTING)
            }
        }
    }

    private fun setupLabels(label: ReviewerCredibilityLabel) {
        with(label) {
            setReviewerName(userName)
            setJoinDate(joinDate)
            setFooterText(footer)
            setButton(ctaText, applink)
            setLearnMoreClickListener(infoText)
        }
    }

    private fun setupStatistics(title: String, statistics: List<ReviewerCredibilityStat>) {
        val statisticsToShow = statistics.filter { it.shouldShow }
        statisticsBox?.setStatistics(title, statisticsToShow)
    }

    private fun setReviewerName(reviewerName: String) {
        this.reviewerName?.text = reviewerName
    }

    private fun setJoinDate(joinDate: String) {
        this.joinDate?.text = joinDate
    }

    private fun setFooterText(footer: String) {
        this.footer?.text = context?.let { HtmlLinkHelper(it, footer).spannedString } ?: footer
    }

    private fun setButton(buttonText: String, applink: String) {
        mainButton?.apply {
            text = buttonText
            setOnClickListener {
                if (isUsersOwnCredibility()) {
                    ReviewCredibilityTracking.trackOnClickCTASelfCredibility(buttonText, userId, source, viewModel.userId)
                } else {
                    ReviewCredibilityTracking.trackOnClickCTAOtherUserCredibility(buttonText, userId, productId, source, viewModel.userId)
                }
                handleRouting(applink)
            }
        }
    }

    private fun setLearnMoreClickListener(learnMoreText: String) {
        this.learnMore?.apply {
            text = HtmlLinkHelper(context, learnMoreText).spannedString
            movementMethod = object : LinkMovementMethod() {
                override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
                    val action = event.action

                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                        var x = event.x
                        var y = event.y.toInt()

                        x -= widget.totalPaddingLeft
                        y -= widget.totalPaddingTop

                        x += widget.scrollX
                        y += widget.scrollY

                        val layout = widget.layout
                        val line = layout.getLineForVertical(y)
                        val off = layout.getOffsetForHorizontal(line, x)

                        val link = buffer.getSpans(off, off, URLSpan::class.java)
                        if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                            return routeToWebView(link.first().url.toString())
                        }
                    }
                    return super.onTouchEvent(widget, buffer, event);
                }
            }
        }
    }

    private fun routeToWebView(url: String): Boolean {
        dismiss()
        return RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    private fun setDismissBehavior() {
        setOnDismissListener {
            activity?.finish()
        }
    }

    private fun showLoading() {
        showKnob()
        loadingView?.show()
        loadingBox?.setBackgroundResource(R.drawable.bg_review_credibility_statistics_box)
    }

    private fun hideLoading() {
        loadingView?.hide()
    }

    private fun handleRouting(applink: String) {
        if (isFromInbox()) {
            if (applink != ApplinkConst.REPUTATION) {
                RouteManager.route(context, applink)
            }
            dismiss()
        } else {
            if (viewModel.isLoggedIn()) {
                dismiss()
                RouteManager.route(context, applink)
            } else {
                this.applink = applink
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), LOGIN_REQUEST_CODE)
            }
        }
    }

    private fun isUsersOwnCredibility(): Boolean {
        return viewModel.isUsersOwnCredibility(userId)
    }

    private fun mapSourceToParam(): String {
        return if (isFromInbox()) source else READING_SOURCE_PARAM
    }

    private fun isFromInbox(): Boolean {
        return source == ReviewPendingFragment.INBOX_SOURCE
    }

    private fun showKnob() {
        knobView.show()
        bottomSheetHeader.hide()
        bottomSheetClose.hide()
    }

    private fun hideKnob() {
        knobView.hide()
        bottomSheetHeader.show()
        bottomSheetClose.show()
    }

}