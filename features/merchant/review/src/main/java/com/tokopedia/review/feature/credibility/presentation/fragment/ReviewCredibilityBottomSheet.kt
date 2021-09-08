package com.tokopedia.review.feature.credibility.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInboxInstance
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityLabel
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStat
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.di.DaggerReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.di.ReviewCredibilityComponent
import com.tokopedia.review.feature.credibility.presentation.viewmodel.ReviewCredibilityViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ReviewCredibilityBottomSheet : BottomSheetUnify(), HasComponent<ReviewCredibilityComponent> {

    companion object {
        fun newInstance(userId: String, source: String) = ReviewCredibilityBottomSheet().apply {
            this.userId = userId
            this.source = source
        }
    }

    @Inject
    lateinit var viewModel: ReviewCredibilityViewModel
    private var userId: String = ""
    private var source: String = ""

    private var reviewerName: Typography? = null
    private var joinDate: Typography? = null
    private var footer: Typography? = null
    private var mainButton: UnifyButton? = null
    private var learnMore: Typography? = null

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

    private fun bindViews(view: View) {
        with(view) {
            reviewerName = findViewById(R.id.review_credibility_reviewer_name)
            joinDate = findViewById(R.id.review_credibility_join_date)
            footer = findViewById(R.id.review_credibility_footer)
            mainButton = findViewById(R.id.review_credibility_button)
            learnMore = findViewById(R.id.review_credibility_learn_more)
        }
    }

    private fun getReviewerCredibility() {
        viewModel.getReviewCredibility(source, userId)
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
        with(reviewCredibility) {
            setupLabels(label)
            setupStatistics(stats)
        }
    }

    private fun onFailGetReviewerCredibility() {

    }

    private fun setupLabels(label: ReviewerCredibilityLabel) {
        with(label) {
            setReviewerName(userName)
            setJoinDate(joinDate)
            setFooterText(footer)
            setButtonText(ctaText)
            setLearnMoreClickListener(infoText)
        }
    }

    private fun setupStatistics(statistics: List<ReviewerCredibilityStat>) {
        val statisticsToShow = statistics.filter { it.shouldShow }
    }

    private fun setReviewerName(reviewerName: String) {
        this.reviewerName?.text = reviewerName
    }

    private fun setJoinDate(joinDate: String) {
        this.joinDate?.text = joinDate
    }

    private fun setFooterText(footer: String) {
        this.footer?.text = footer
    }

    private fun setButtonText(buttonText: String) {
        mainButton?.text = buttonText
    }

    private fun setLearnMoreClickListener(url: String) {
        this.learnMore?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.WEBVIEW, url)
        }
    }

}