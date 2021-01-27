package com.tokopedia.sellerreview.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerreview.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.RatingBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.ThankYouBottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.absoluteValue

/**
 * Created By @ilhamsuaib on 21/01/21
 */

//PRD : https://tokopedia.atlassian.net/wiki/spaces/MT/pages/1077608743/PRD+Internal+Review+Loop+Seller+App

@SellerHomeScope
class SellerReviewHelper @Inject constructor(
        private val cacheHandler: LocalCacheHandler
) : CoroutineScope {

    companion object {
        private const val POPUP_DELAY = 500L
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    /**
     * We want to show in app review bottom sheet when
     * the seller has done one of the 3 journeys (added product, posted feed or replied 5 chats)
     * and never seen the bottom sheet before within last 30 days
     * */
    fun checkForReview(context: Context, fm: FragmentManager) {
        launchCatchError(block = {
            val hasAddedProduct = cacheHandler.getBoolean(TkpdCache.SellerInAppReview.KEY_HAS_ADDED_PRODUCT, false)
            val hasPostedFeed = cacheHandler.getBoolean(TkpdCache.SellerInAppReview.KEY_HAS_POSTED_FEED, false)
            val hasReplied5Chats = cacheHandler.getStringSet(TkpdCache.SellerInAppReview.KEY_CHATS_REPLIED_TO, emptySet()).size >= 5
            val hasOpenedReview = cacheHandler.getBoolean(TkpdCache.SellerInAppReview.KEY_HAS_OPENED_REVIEW, false)

            withContext(Dispatchers.Main) {
                if ((getAskReviewStatus() || !hasOpenedReview) && (hasAddedProduct || hasPostedFeed || hasReplied5Chats)) {
                    showInAppReviewBottomSheet(context, fm)
                }
            }
        }, onError = {
            Timber.w(it)
        })
    }

    private fun showInAppReviewBottomSheet(context: Context, fm: FragmentManager) {
        //we can't show bottom sheet if FragmentManager's state has already been saved
        if (fm.isStateSaved) return

        resetQuotaCheck()

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
        val sellerAppPlayStoreUrl = "market://details?id=${context.packageName}"
        intent.data = Uri.parse(sellerAppPlayStoreUrl)
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

    private fun resetQuotaCheck() {
        launchCatchError(block = {
            cacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_ADDED_PRODUCT, false)
            cacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_POSTED_FEED, false)
            cacheHandler.putBoolean(TkpdCache.SellerInAppReview.KEY_HAS_OPENED_REVIEW, true)
            cacheHandler.putStringSet(TkpdCache.SellerInAppReview.KEY_CHATS_REPLIED_TO, mutableSetOf())
            val todayMillis = Date().time
            cacheHandler.putLong(TkpdCache.SellerInAppReview.KEY_LAST_REVIEW_ASKED, todayMillis)
            cacheHandler.applyEditor()
        }, onError = {
            Timber.w(it)
        })
    }
}