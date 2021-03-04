package com.tokopedia.createpost.view.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.Content
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.createpost.*
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.domain.usecase.SubmitPostUseCase
import com.tokopedia.createpost.view.util.FeedSellerAppReviewHelper
import com.tokopedia.createpost.view.util.SubmitPostNotificationManager
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.twitter_share.TwitterManager
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import rx.Subscriber
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by milhamj on 26/02/19.
 */
class SubmitPostService : JobIntentService() {

    @Inject
    lateinit var submitPostUseCase: SubmitPostUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var twitterManager: TwitterManager

    @Inject
    lateinit var sellerAppReviewHelper: FeedSellerAppReviewHelper

    private var notificationManager: SubmitPostNotificationManager? = null

    companion object {
        private const val JOB_ID = 13131313

        fun startService(context: Context, draftId: String) {
            val work = Intent(context, SubmitPostService::class.java).apply {
                putExtra(DRAFT_ID, draftId)
            }
            enqueueWork(context, SubmitPostService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override fun onHandleWork(intent: Intent) {
        val id: String = intent.getStringExtra(DRAFT_ID) ?: return
        val cacheManager = SaveInstanceCacheManager(baseContext, id)
        val viewModel: CreatePostViewModel = cacheManager.get(
                CreatePostViewModel.TAG,
                CreatePostViewModel::class.java
        ) ?: return
        val notifId = Random().nextInt()
        notificationManager = getNotificationManager(notifId, viewModel)
        submitPostUseCase.notificationManager = notificationManager

        submitPostUseCase.execute(SubmitPostUseCase.createRequestParams(
                viewModel.postId,
                viewModel.authorType,
                viewModel.token,
                if (isTypeAffiliate(viewModel.authorType)) userSession.userId
                else userSession.shopId,
                viewModel.caption,
                (if (viewModel.fileImageList.isEmpty()) viewModel.urlImageList
                else viewModel.fileImageList).map { it.path to it.type },
                if (isTypeAffiliate(viewModel.authorType)) viewModel.adIdList
                else viewModel.productIdList
        ), getSubscriber())
    }

    private fun initInjector() {
        DaggerCreatePostComponent.builder()
                .createPostModule(CreatePostModule(this.applicationContext))
                .build()
                .inject(this)
    }

    private fun isTypeAffiliate(authorType: String) = authorType == TYPE_AFFILIATE

    private fun getNotificationManager(notifId: Int, viewModel: CreatePostViewModel): SubmitPostNotificationManager {
        val authorType = viewModel.authorType
        val firstImage = viewModel.completeImageList.firstOrNull()?.path ?: ""
        val maxCount = viewModel.completeImageList.size

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return object : SubmitPostNotificationManager(notifId, maxCount, firstImage, manager,
                this@SubmitPostService) {

            override fun getSuccessIntent(): PendingIntent {
                val appLink = when {
                    GlobalConfig.isSellerApp() -> {
                        ApplinkConst.SHOP_FEED.replace(SHOP_ID_PARAM, userSession.shopId)
                    }
                    isTypeAffiliate(authorType) -> {
                        ApplinkConst.PROFILE_SUCCESS_POST.replace(USER_ID_PARAM, userSession.userId)
                    }
                    else -> {
                        ApplinkConst.FEED
                    }
                }

                val intent = RouteManager.getIntent(context, appLink)
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(errorMessage: String): PendingIntent {
                val message = if (errorMessage.contains(context.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short), false))
                    context.getString(R.string.cp_error_create_post)
                else
                    errorMessage


                val applink = if (authorType == TYPE_AFFILIATE) {
                    ApplinkConst.AFFILIATE_DRAFT_POST
                } else {
                    ApplinkConst.CONTENT_DRAFT_POST
                }

                val cacheManager = SaveInstanceCacheManager(context, true)
                cacheManager.put(CreatePostViewModel.TAG, viewModel, TimeUnit.DAYS.toMillis(7))

                val intent = RouteManager.getIntent(
                        context,
                        applink.replace(DRAFT_ID_PARAM, cacheManager.id ?: "0")
                                .plus("?$CREATE_POST_ERROR_MSG=$message")
                )

                return PendingIntent.getActivity(context, 0, intent, 0)
            }
        }
    }

    private fun getSubscriber(): Subscriber<SubmitPostData> {
        return object : Subscriber<SubmitPostData>() {
            override fun onNext(submitPostData: SubmitPostData?) {
                if (submitPostData == null
                        || submitPostData.feedContentSubmit.success != SubmitPostData.SUCCESS) {
                    notificationManager?.onFailedPost(ErrorHandler.getErrorMessage(
                            this@SubmitPostService,
                            RuntimeException()
                    ))
                    return
                } else if (!TextUtils.isEmpty(submitPostData.feedContentSubmit.error)) {
                    notificationManager?.onFailedPost(submitPostData.feedContentSubmit.error)
                    return
                }
                notificationManager?.onSuccessPost()
                sendBroadcast()
                postContentToOtherService(submitPostData.feedContentSubmit.meta.content)
                addFlagOnCreatePostSuccess()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                notificationManager?.onFailedPost(ErrorHandler.getErrorMessage(
                        this@SubmitPostService,
                        e
                ))
            }

            private fun sendBroadcast() {
                val intent = Intent(BROADCAST_SUBMIT_POST)
                intent.putExtra(SUBMIT_POST_SUCCESS, true)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
    }

    private fun postContentToOtherService(content: Content) {
        if (twitterManager.shouldPostToTwitter) postToTwitter(content)
    }

    private fun postToTwitter(content: Content) {
        GlobalScope.launchCatchError(Dispatchers.IO, block = {
            twitterManager.postTweet(content.description)
        }) { Timber.d(it) }
    }


    private fun addFlagOnCreatePostSuccess() {
        sellerAppReviewHelper.savePostFeedFlag()
    }
}
