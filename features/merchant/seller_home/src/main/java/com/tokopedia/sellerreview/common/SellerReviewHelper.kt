package com.tokopedia.sellerreview.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerreview.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.RatingBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.ThankYouBottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

/**
 * Created By @ilhamsuaib on 21/01/21
 */

@SellerHomeScope
class SellerReviewHelper @Inject constructor(
        private val cacheHandler: LocalCacheHandler
) : CoroutineScope {

    companion object {
        private const val POPUP_DELAY = 500L
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun checkForReview(context: Context, fm: FragmentManager) {
        //we can't show bottom sheet if FragmentManager's state has already been saved
        if (fm.isStateSaved) return

        launch {
            initDummy()

            val hasAddedProduct = cacheHandler.getBoolean(TkpdCache.SellerInAppReview.KEY_HAS_ADDED_PRODUCT, false)
            val hasPostedFeed = cacheHandler.getBoolean(TkpdCache.SellerInAppReview.KEY_HAS_POSTED_FEED, false)
            val hasReplied5Chats = cacheHandler.getInt(TkpdCache.SellerInAppReview.KEY_NUMBER_OF_CHATS_REPLIED_TO, 0) >= 5

            withContext(Dispatchers.Main) {
                if (getAskReviewStatus() && (hasAddedProduct || hasPostedFeed || hasReplied5Chats)) {
                    showInAppReviewBottomSheet(context, fm)
                }
            }
        }
    }

    private fun initDummy() {
        cacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_ADDED_PRODUCT, true)
        cacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_POSTED_FEED, true)
        cacheHandler.putInt(TkpdCache.SellerInAppReview.KEY_NUMBER_OF_CHATS_REPLIED_TO, 5)
        val last31Days = System.currentTimeMillis().minus(TimeUnit.DAYS.toMillis(31))
        cacheHandler.putLong(TkpdCache.SellerInAppReview.KEY_LAST_REVIEW_ASKED, last31Days)
        cacheHandler.applyEditor()
    }

    private fun showInAppReviewBottomSheet(context: Context, fm: FragmentManager) {
        if (fm.isStateSaved) return

        RatingBottomSheet.createInstance()
                .setOnSubmittedListener {
                    setOnRatingSubmitted(context, fm, it)
                }
                .show(fm)
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

    /**
     * @return true if the in-app review pop-up never seen for the last 30 days
     * */
    private fun getAskReviewStatus(): Boolean {
        val lastReviewAsked = cacheHandler.getLong(TkpdCache.SellerInAppReview.KEY_LAST_REVIEW_ASKED, Date().time)
        val daysDiff = getDateDiffInDays(Date(lastReviewAsked), Date())
        return daysDiff.absoluteValue > 30
    }

    private fun rateOnPlayStore(context: Context, fm: FragmentManager) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=${context.packageName}")
        context.startActivity(intent)

        Handler().postDelayed({
            showTankYouBottomSheet(fm)
        }, POPUP_DELAY)
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    private fun getDateDiffInDays(date1: Date, date2: Date): Int {
        val diffInMillis = date2.time - date1.time
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS).toInt()
    }
}