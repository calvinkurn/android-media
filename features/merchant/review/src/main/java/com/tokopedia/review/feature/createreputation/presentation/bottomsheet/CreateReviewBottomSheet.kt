package com.tokopedia.review.feature.createreputation.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.R
import com.tokopedia.review.ReviewInstance
import com.tokopedia.review.feature.createreputation.di.DaggerCreateReviewComponent
import com.tokopedia.review.feature.createreputation.model.ProductData
import com.tokopedia.review.feature.createreputation.model.ProductRevGetForm
import com.tokopedia.review.feature.createreputation.presentation.fragment.CreateReviewFragment
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.CreateReviewViewModel
import com.tokopedia.review.feature.createreputation.presentation.widget.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CreateReviewBottomSheet : BottomSheetUnify() {

    companion object {
        fun createInstance(productId: Long, reputationId: Long): CreateReviewBottomSheet {
            return CreateReviewBottomSheet().apply {
                this.productId = productId
                this.reputationId = reputationId
            }
        }
    }

    @Inject
    lateinit var createReviewViewModel: CreateReviewViewModel

    // View Elements
    private var productCard: CreateReviewProductCard? = null
    private var ratingPrompt: Typography? = null
    private var ratingStars: AnimatedRatingPickerCreateReviewView? = null
    private var incentivesContainer: View? = null
    private var textAreaTitle: Typography? = null
    private var textArea: CreateReviewTextArea? = null
    private var textAreaBottomSheet: CreateReviewTextAreaBottomSheet? = null
    private var anonymousOption: CreateReviewAnonymousOption? = null
    private var progressBar: CreateReviewProgressBar? = null
    private var submitButton: UnifyButton? = null

    private var productId: Long = 0L
    private var reputationId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        context?.let {
            val view = View.inflate(context, R.layout.widget_create_review_text_area_bottom_sheet, null)
            setChild(view)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, R.layout.bottomsheet_create_review, null)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeGetForm()
        observeIncentive()
        getForm()
        getIncentiveOvoData()
        setRatingInitialState()
        setRatingClickListener()
    }

    private fun initInjector() {
        activity?.let {
            DaggerCreateReviewComponent
                    .builder()
                    .reviewComponent(ReviewInstance.getComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    private fun bindViews() {
        productCard = view?.findViewById(R.id.review_form_product_card)
        ratingPrompt = view?.findViewById(R.id.review_form_rating_prompt)
        ratingStars = view?.findViewById(R.id.review_form_rating)
        incentivesContainer = view?.findViewById(R.id.review_form_incentives_container)
        textAreaTitle = view?.findViewById(R.id.review_form_text_area_title)
        textArea = view?.findViewById(R.id.review_form_text_area)
        anonymousOption = view?.findViewById(R.id.review_form_anonymous_option)
        progressBar = view?.findViewById(R.id.review_form_progress_bar_widget)
        submitButton = view?.findViewById(R.id.review_form_submit_button)
    }

    private fun setRatingClickListener() {
        ratingStars?.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                super.onClick(position)
                hideRatingPrompt()
                showAllViews()
                expandBottomSheet()
                updateTitleBasedOnSelectedRating(position)
            }
        })
    }

    private fun setRatingInitialState() {
        ratingStars?.resetStars()
    }

    private fun showAllViews() {
        incentivesContainer?.show()
        textAreaTitle?.show()
        textArea?.show()
        anonymousOption?.show()
        progressBar?.show()
        submitButton?.show()
    }

    private fun hideRatingPrompt() {
        ratingPrompt?.hide()
    }

    private fun expandBottomSheet() {
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getForm() {
        createReviewViewModel.getProductReputation(productId, reputationId)
    }

    private fun getIncentiveOvoData() {
        createReviewViewModel.getProductIncentiveOvo(productId, reputationId)
    }

    private fun observeGetForm() {
        createReviewViewModel.getReputationDataForm.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetReviewForm(it.data)
                is Fail -> onErrorGetReviewForm(it.throwable)
            }
        })
    }

    private fun observeIncentive() {
        createReviewViewModel.incentiveOvo.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun onSuccessGetReviewForm(data: ProductRevGetForm) {
        with(data.productrevGetForm) {
            when {
                !validToReview -> {
                    dismiss()
                    return
                }
                productData.productStatus == 0 -> {
                    dismiss()
                    return
                }
            }
            setProductDetail(productData)
        }
    }

    private fun setProductDetail(data: ProductData) {
        productCard?.setProduct(data)
    }

    private fun setProductOnClickListener() {
        productCard?.setOnClickListener {
            goToPdp()
        }
    }

    private fun goToPdp() {
        RouteManager.route(context, "")
    }

    private fun onErrorGetReviewForm(throwable: Throwable) {

    }

    private fun updateTitleBasedOnSelectedRating(position: Int) {
        when {
            position < CreateReviewFragment.RATING_3 -> {
                if (position == CreateReviewFragment.RATING_1) {
                    textAreaTitle?.text = resources.getString(R.string.review_create_worst_title)
                } else {
                    textAreaTitle?.text = resources.getString(R.string.review_create_negative_title)
                }
            }
            position == CreateReviewFragment.RATING_3 -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_neutral_title)
            }
            position == CreateReviewFragment.RATING_4 -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_positive_title)
            }
            else -> {
                textAreaTitle?.text = resources.getString(R.string.review_create_best_title)
            }
        }
    }
}