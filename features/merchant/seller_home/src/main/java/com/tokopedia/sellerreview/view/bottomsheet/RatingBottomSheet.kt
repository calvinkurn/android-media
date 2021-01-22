package com.tokopedia.sellerreview.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerreview.common.Const
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.sir_rating_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 20/01/21
 */

class RatingBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "RatingBottomSheet"
        fun createInstance(): RatingBottomSheet {
            return RatingBottomSheet().apply {
                overlayClickDismiss = false
            }
        }
    }

    private var childView: View? = null
    private var ratingStatus: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initRatingStatus()
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun dismiss() {
        view?.post {
            if (isVisible) {
                super.dismiss()
            }
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
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

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child = inflater.inflate(R.layout.sir_rating_bottom_sheet, container, false)
        childView = child
        setChild(child)
        setupView()
    }

    private fun setupView() = childView?.run {
        val defaultImageIndex = 3
        setOnStarClicked(this, defaultImageIndex)
        ratePickerSirRating.setDescriptionStatus("")
        ratePickerSirRating.setListener(object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                setOnStarClicked(this@run, position)
                showRatePickerStatus(position)
            }
        })
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