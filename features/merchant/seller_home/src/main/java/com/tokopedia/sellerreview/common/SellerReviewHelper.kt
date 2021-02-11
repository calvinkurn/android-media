package com.tokopedia.sellerreview.common

import android.content.Context
import android.os.Handler
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerreview.view.bottomsheet.BaseBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.RatingBottomSheet
import com.tokopedia.sellerreview.view.bottomsheet.ThankYouBottomSheet
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

/**
 * Created By @ilhamsuaib on 21/01/21
 */

//PRD : https://tokopedia.atlassian.net/wiki/spaces/MT/pages/1077608743/PRD+Internal+Review+Loop+Seller+App

@SellerHomeScope
class SellerReviewHelper @Inject constructor(
        private val cacheHandler: SellerReviewCacheHandler,
        private val userSession: UserSessionInterface,
        private val remoteConfig: SellerAppReviewRemoteConfig,
        private val dispatchers: CoroutineDispatchers
) {

    companion object {
        private const val QUOTA_CHECK_DELAY = 1000L
        private const val POPUP_DELAY = 500L
        private const val SELLER_APP_ON_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=com.tokopedia.sellerapp"
        private const val MIN_STARS_TO_RATE_ON_PLAYSTORE = 4
        private const val REVIEW_PERIOD_IN_DAYS = 30
    }

    private val handler by lazy { Handler() }
    private var popupAlreadyShown = false

    /**
     * We want to show in app review bottom sheet when
     * the seller has done one of the 3 journeys (added product, posted feed or replied 5 chats)
     * and never seen the bottom sheet before within last 30 days
     * */
    suspend fun checkForReview(context: Context, fm: FragmentManager) {
        val isEnabled = remoteConfig.isSellerReviewEnabled()
        if (!isEnabled || popupAlreadyShown || !SellerReviewUtils.getConnectionStatus(context)) return

        try {
            delay(QUOTA_CHECK_DELAY)
            val hasAddedProduct = cacheHandler.getBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_ADDED_PRODUCT), false)
            val hasPostedFeed = cacheHandler.getBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_POSTED_FEED), false)
            val hasReplied5Chats = cacheHandler.getStringSet(getUniqueKey(Const.SharedPrefKey.KEY_CHATS_REPLIED_TO), emptySet()).size >= 5
            val hasOpenedReview = cacheHandler.getBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_OPENED_REVIEW), false)
            val allowPopupShown = canShowPopup()

            withContext(dispatchers.main) {
                if (allowPopupShown && (getAskReviewStatus() || !hasOpenedReview) && (hasAddedProduct || hasPostedFeed || hasReplied5Chats)) {
                    showInAppReviewBottomSheet(context, fm)
                }
            }
        } catch (e: Exception) {
            Timber.w(e)
        }
    }

    /**
     * we can only show the popup on prod signed apk or app review debugging is enabled
     * */
    private fun canShowPopup(): Boolean {
        val isAllowDebuggingTools = GlobalConfig.isAllowDebuggingTools()
        val appReviewDebugEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.KEY_IS_ALLOW_APP_REVIEW_DEBUGGING, false)
        return !isAllowDebuggingTools || appReviewDebugEnabled
    }

    private suspend fun showInAppReviewBottomSheet(context: Context, fm: FragmentManager) {
        //we can't show bottom sheet if FragmentManager's state has already been saved
        if (fm.isStateSaved || popupAlreadyShown || !SellerReviewUtils.getConnectionStatus(context)) return

        popupAlreadyShown = true

        resetQuotaCheck()

        val ratingBottomSheet = (fm.findFragmentByTag(RatingBottomSheet.TAG) as? RatingBottomSheet)
                ?: RatingBottomSheet.createInstance()

        ratingBottomSheet.setOnDestroyListener {
            popupAlreadyShown = false
        }
        ratingBottomSheet.setOnSubmittedListener {
            setOnRatingSubmitted(context, fm, it)
        }

        showBottomSheet(fm, ratingBottomSheet)
    }

    private fun setOnRatingSubmitted(context: Context, fm: FragmentManager, rating: Int) {
        if (rating >= MIN_STARS_TO_RATE_ON_PLAYSTORE) {
            rateOnPlayStore(context, fm)
        } else {
            handler.postDelayed({
                showFeedBackBottomSheet(context, fm, rating)
            }, POPUP_DELAY)
        }
    }

    private fun showFeedBackBottomSheet(context: Context, fm: FragmentManager, rating: Int) {
        //we can't show bottom sheet if FragmentManager's state has already been saved
        if (fm.isStateSaved || !SellerReviewUtils.getConnectionStatus(context)) return

        val feedbackBottomSheet = (fm.findFragmentByTag(FeedbackBottomSheet.TAG) as? FeedbackBottomSheet)
                ?: FeedbackBottomSheet.createInstance(rating)

        feedbackBottomSheet.setOnSubmittedListener {
            handler.postDelayed({
                showTankYouBottomSheet(fm)
            }, POPUP_DELAY)
        }

        showBottomSheet(fm, feedbackBottomSheet)
    }

    private fun showTankYouBottomSheet(fm: FragmentManager) {
        //we can't show bottom sheet if FragmentManager's state has already been saved
        if (fm.isStateSaved) return

        val thankYouBottomSheet = (fm.findFragmentByTag(ThankYouBottomSheet.TAG) as? ThankYouBottomSheet)
                ?: ThankYouBottomSheet.createInstance()

        showBottomSheet(fm, thankYouBottomSheet)
    }

    private fun showBottomSheet(fm: FragmentManager, fragment: BaseBottomSheet) {
        //to prevent IllegalStateException: Fragment already added
        if (fragment.isAdded) {
            fragment.dismiss()
            return
        }

        Handler().post {
            fragment.show(fm)
        }
    }

    /**
     * @return true if the in-app review pop-up never seen for the last 30 days
     * */
    private fun getAskReviewStatus(): Boolean {
        val lastReviewAsked = cacheHandler.getLong(getUniqueKey(Const.SharedPrefKey.KEY_LAST_REVIEW_ASKED), Date().time)
        val daysDiff = getDateDiffInDays(Date(lastReviewAsked), Date())
        return daysDiff.absoluteValue > REVIEW_PERIOD_IN_DAYS
    }

    private fun rateOnPlayStore(context: Context, fm: FragmentManager) {
        RouteManager.route(context, SELLER_APP_ON_GOOGLE_PLAY)
        handler.postDelayed({
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
        return TimeUnit.DAYS.convert(diffInMillis.absoluteValue, TimeUnit.MILLISECONDS).toInt()
    }

    private suspend fun resetQuotaCheck() {
        withContext(dispatchers.io) {
            try {
                cacheHandler.putBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_ADDED_PRODUCT), false)
                cacheHandler.putBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_POSTED_FEED), false)
                cacheHandler.putBoolean(getUniqueKey(Const.SharedPrefKey.KEY_HAS_OPENED_REVIEW), true)
                cacheHandler.putStringSet(getUniqueKey(Const.SharedPrefKey.KEY_CHATS_REPLIED_TO), mutableSetOf())
                val todayMillis = Date().time
                cacheHandler.putLong(getUniqueKey(Const.SharedPrefKey.KEY_LAST_REVIEW_ASKED), todayMillis)
                cacheHandler.applyEditor()
            } catch (e: Exception) {
                Timber.w(e)
            }
        }
    }

    private fun getUniqueKey(key: String): String {
        return SellerReviewUtils.getUniqueKey(key, userSession.userId)
    }
}