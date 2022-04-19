package com.tokopedia.internal_review.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.internal_review.R
import com.tokopedia.internal_review.analytics.ReviewTracking
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.internal_review.common.Const
import com.tokopedia.internal_review.common.InternalReviewUtils
import com.tokopedia.internal_review.factory.createReviewViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.sir_rating_bottom_sheet.view.*
import java.net.UnknownHostException

/**
 * Created By @ilhamsuaib on 20/01/21
 */

class RatingBottomSheet : BaseBottomSheet() {

    companion object {
        const val TAG = "SirRatingBottomSheet"
        private const val PAGE_NAME = "popup rating"

        fun createInstance(): RatingBottomSheet {
            return RatingBottomSheet().apply {
                showCloseIcon = false
                showHeader = false
            }
        }
    }

    private val mViewModel by lazy { createReviewViewModel(this) }
    private var ratingStatus: Array<String>? = null
    private var onSubmitted: ((Int) -> Unit)? = null
    private var givenRating = 0

    override fun getResLayout(): Int = R.layout.sir_rating_bottom_sheet

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initRatingStatus()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeReviewState()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isSubmitted) {
            tracker.sendClickDismissBottomSheetEvent(PAGE_NAME)
        }
    }

    override fun setupView() = childView?.run {
        val defaultImageIndex = 3
        setOnStarClicked(this, defaultImageIndex)
        ratePickerSirRating.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onRatingSelected(rating: Int) {
                setOnStarClicked(this@run, rating)
                showRatePickerStatus(rating)
                btnSirSubmit.isEnabled = true
                givenRating = rating
            }
        })
        btnSirCloseRating.setOnClickListener {
            dismiss()
        }
        btnSirSubmit.setOnClickListener {
            submitRating()
        }
    }

    override fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setOnSubmittedListener(action: (rating: Int) -> Unit): RatingBottomSheet {
        onSubmitted = action
        return this
    }

    private fun observeReviewState() {
        mViewModel.reviewStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSubmitted?.invoke(givenRating)
                    this.dismiss()
                }
                is Fail -> setOnError(it.throwable)
            }
        })
    }

    private fun setOnError(throwable: Throwable) {
        isSubmitted = false
        childView?.run {
            btnSirSubmit.isLoading = false
            showErrorToaster(throwable)
        }
        if (throwable is UnknownHostException) {
            tracker.sendImpressionNoNetworkEvent(PAGE_NAME)
        } else {
            tracker.sendImpressionErrorStateEvent(PAGE_NAME)
        }
    }

    /**
     * navigate to play store if user give 4 or 5 stars.
     * or open feedback bottom sheet if else
     * */
    private fun submitRating() = childView?.run {
        val isConnected = InternalReviewUtils.getConnectionStatus(requireContext())
        if (!isConnected) {
            setOnError(UnknownHostException())
            return@run
        }

        isSubmitted = true
        if (givenRating >= 4) {
            btnSirSubmit.isLoading = true
            val param = getParams(givenRating, "")
            mViewModel.submitReview(param)
            tracker.sendImpressionLoadingStateEvent(PAGE_NAME)
        } else {
            onSubmitted?.invoke(givenRating)
            this@RatingBottomSheet.dismiss()
        }
    }

    private fun initRatingStatus() {
        if (ratingStatus == null) {
            context?.let {
                ratingStatus = arrayOf(
                        it.getString(R.string.sir_rate_1_star),
                        it.getString(R.string.sir_rate_2_star),
                        it.getString(R.string.sir_rate_3_star),
                        it.getString(R.string.sir_rate_4_star),
                        it.getString(R.string.sir_rate_5_star)
                )
            }
        }
    }

    private fun setOnStarClicked(child: View, starPosition: Int) {
        val animationUrl = getAnimationUrlByIndex(starPosition)
        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(child.context, animationUrl)

        lottieCompositionLottieTask.addListener { result ->
            child.lottieSirRatingIllustration.setComposition(result)
            child.lottieSirRatingIllustration.playAnimation()
        }
    }

    private fun showRatePickerStatus(starPosition: Int) {
        val statusStr = ratingStatus?.get(starPosition.minus(1)) ?: ""
        childView?.tvSirStatus?.text = statusStr
    }

    private fun getAnimationUrlByIndex(position: Int): String {
        return when (position) {
            1 -> Const.LOTTIE_URL_RATING_1
            2 -> Const.LOTTIE_URL_RATING_2
            3 -> Const.LOTTIE_URL_RATING_3
            4 -> Const.LOTTIE_URL_RATING_4
            else -> Const.LOTTIE_URL_RATING_5
        }
    }
}