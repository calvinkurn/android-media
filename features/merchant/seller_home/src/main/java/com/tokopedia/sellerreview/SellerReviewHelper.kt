package com.tokopedia.sellerreview

import android.content.Context
import android.os.Handler
import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerreview.view.bottomsheet.RatingBottomSheet

/**
 * Created By @ilhamsuaib on 21/01/21
 */

object SellerReviewHelper {

    private const val POPUP_DELAY = 1000L

    fun checkForReview(context: Context, fm: FragmentManager) {
        Handler().postDelayed({
            RatingBottomSheet.createInstance()
                    .show(fm)
        }, POPUP_DELAY)
    }
}