package com.tokopedia.affiliate.feature.createpost.view.service

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.tokopedia.affiliate.feature.createpost.domain.usecase.SubmitPostUseCase
import com.tokopedia.affiliate.feature.createpost.view.util.SubmitPostNotificationManager
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.affiliatecommon.data.pojo.submitpost.response.SubmitPostData
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.user.session.UserSession
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 26/02/19.
 */
class SubmitPostService : IntentService(TAG) {

    @Inject
    lateinit var submitPostUseCase: SubmitPostUseCase

    private var notificationManager: SubmitPostNotificationManager? = null

    companion object {
        private val TAG = SubmitPostService::class.java.simpleName
        private const val DRAFT_ID = "draft_id"

        fun createIntent(context: Context, draftId: String): Intent {
            return Intent(context, SubmitPostService::class.java).apply {
                putExtra(DRAFT_ID, draftId)
            }
        }
    }

    override fun onHandleIntent(intent: Intent) {
        val id: String = intent.getStringExtra(DRAFT_ID) ?: return
        val cacheManager = PersistentCacheManager(baseContext, id)
        val viewModel: CreatePostViewModel = cacheManager.get(
                CreatePostViewModel.TAG,
                CreatePostViewModel::class.java,
                null
        ) ?: return
        val notificationId = Random().nextInt()
        notificationManager = getNotificationManager(notificationId, viewModel.completeImageList.size)
        submitPostUseCase.notificationManager = notificationManager
        submitPostUseCase.execute(
                SubmitPostUseCase.createRequestParams(
                        viewModel.productIdList.firstOrNull() ?: "",
                        viewModel.adIdList.firstOrNull() ?: "",
                        viewModel.token,
                        viewModel.completeImageList,
                        viewModel.mainImageIndex
                ),
                getSubscriber()
        )
    }

    private fun getNotificationManager(id: Int, maxCount: Int): SubmitPostNotificationManager {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val userSession = UserSession(this)
        return object : SubmitPostNotificationManager(id, maxCount, manager, this@SubmitPostService) {
            override fun getSuccessIntent(): PendingIntent {
                //TODO milhamj handle intent
                val intent = RouteManager.getIntent(context, "tokopedia://people/${userSession.userId}")
                return PendingIntent.getActivity(context, 0, intent, 0)
            }

            override fun getFailedIntent(): PendingIntent {
                //TODO milhamj handle intent
                val intent = RouteManager.getIntent(context, "tokopedia://people/${userSession.userId}")
                return PendingIntent.getActivity(context, 0, intent, 0)
            }
        }
    }

    private fun getSubscriber(): Subscriber<SubmitPostData> {
        return object : Subscriber<SubmitPostData>() {
            override fun onNext(submitPostData: SubmitPostData?) {
                if (submitPostData == null
                        || submitPostData.feedContentSubmit.success != SubmitPostData.SUCCESS) {
                    notificationManager?.onFailedPost()
                    return
                } else if (!TextUtils.isEmpty(submitPostData.feedContentSubmit.error)) {
                    notificationManager?.onFailedPost()
                    return
                }
                notificationManager?.onSuccessPost()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                notificationManager?.onFailedPost()
            }
        }
    }
}