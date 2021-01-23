package com.tokopedia.sellerreview.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerreview.common.Const
import kotlinx.android.synthetic.main.sir_rating_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 20/01/21
 */

class RatingBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "RatingBottomSheet"

        fun createInstance(): RatingBottomSheet {
            return RatingBottomSheet().apply {
                overlayClickDismiss = false
            }
        }
    }

    private var ratingStatus: Array<String>? = null
    private var onSubmitted: ((Int) -> Unit)? = null
    private var givenRating = 0

    override fun getResLayout(): Int = R.layout.sir_rating_bottom_sheet

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initRatingStatus()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = childView?.run {
        val defaultImageIndex = 3
        setOnStarClicked(this, defaultImageIndex)
        ratePickerSirRating.setDescriptionStatus("")
        ratePickerSirRating.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                setOnStarClicked(this@run, position)
                showRatePickerStatus(position)
                btnSirSubmit.isEnabled = true
                givenRating = position
            }
        })
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

    private fun submitRating() {
        childView?.run {
            btnSirSubmit.isLoading = true

            this@RatingBottomSheet.dismiss()
            onSubmitted?.invoke(givenRating)
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
        childView?.ratePickerSirRating?.setDescriptionStatus(statusStr)
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