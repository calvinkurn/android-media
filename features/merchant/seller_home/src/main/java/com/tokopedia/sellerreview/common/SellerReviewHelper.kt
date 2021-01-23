package com.tokopedia.sellerreview.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerreview.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.RatingBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.ThankYouBottomSheet

/**
 * Created By @ilhamsuaib on 21/01/21
 */

object SellerReviewHelper {

    private const val POPUP_DELAY = 500L

    fun checkForReview(context: Context, fm: FragmentManager) {
        //we can't show bottom sheet if FragmentManager's state has already been saved
        if (fm.isStateSaved) return

        Handler().postDelayed({
            RatingBottomSheet.createInstance()
                    .setOnSubmittedListener {
                        setOnRatingSubmitted(context, fm, it)
                    }
                    .show(fm)
        }, POPUP_DELAY)
    }

    private fun setOnRatingSubmitted(context: Context, fm: FragmentManager, rating: Int) {
        if (rating >= 4) {
            rateOnPlayStore(context, fm)
        } else {
            Handler().postDelayed({
                showFeedBackBottomSheet(fm)
            }, POPUP_DELAY)
        }
    }

    private fun showFeedBackBottomSheet(fm: FragmentManager) {
        FeedbackBottomSheet.createInstance()
                .setOnSubmittedListener {
                    Handler().postDelayed({
                        showTankYouBottomSheet(fm)
                    }, POPUP_DELAY)
                }
                .show(fm)
    }

    private fun showTankYouBottomSheet(fm: FragmentManager) {
        ThankYouBottomSheet.createInstance()
                .show(fm)
    }

    private fun rateOnPlayStore(context: Context, fm: FragmentManager) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=${context.packageName}")
        context.startActivity(intent)

        Handler().postDelayed({
            showTankYouBottomSheet(fm)
        }, POPUP_DELAY)
    }
}