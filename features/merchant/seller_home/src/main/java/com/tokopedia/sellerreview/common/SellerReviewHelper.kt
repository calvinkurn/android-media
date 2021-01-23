package com.tokopedia.sellerreview.common

import android.content.Context
import android.os.Handler
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerreview.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.RatingBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.ThankYouBottomSheet

/**
 * Created By @ilhamsuaib on 21/01/21
 */

object SellerReviewHelper {

    private const val POPUP_DELAY = 1000L

    fun checkForReview(context: Context, fm: FragmentManager) {
        Handler().postDelayed({
            RatingBottomSheet.createInstance()
                    .setOnSubmittedListener {
                        Handler().postDelayed({
                            showFeedBackBottomSheet(fm)
                        }, 500)
                    }
                    .show(fm)
        }, POPUP_DELAY)
    }

    private fun showFeedBackBottomSheet(fm: FragmentManager) {
        FeedbackBottomSheet.createInstance()
                .setOnSubmittedListener {
                    Handler().postDelayed({
                        showTankYouBottomSheet(fm)
                    }, 500)
                }
                .show(fm)
    }

    private fun showTankYouBottomSheet(fm: FragmentManager) {
        ThankYouBottomSheet.createInstance()
                .show(fm)
    }
}