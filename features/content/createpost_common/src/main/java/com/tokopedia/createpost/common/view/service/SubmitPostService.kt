package com.tokopedia.createpost.common.view.service

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST_NEW
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS_NEW
import com.tokopedia.createpost.common.domain.entity.SubmitPostData
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.DRAFT_ID
import com.tokopedia.createpost.common.TYPE_AFFILIATE
import com.tokopedia.createpost.common.TYPE_CONTENT_USER
import com.tokopedia.createpost.common.di.qualifier.CreatePostCommonDispatchers
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.DaggerCreatePostCommonComponent
import com.tokopedia.createpost.common.di.qualifier.SubmitPostCoroutineScope
import com.tokopedia.createpost.common.domain.entity.SubmitPostResult
import com.tokopedia.createpost.common.domain.usecase.SubmitPostUseCase
import com.tokopedia.createpost.common.view.viewmodel.CreatePostViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.createpost.common.view.util.PostUpdateProgressManager

/**
 * Revamped By : Jonathan Darwin on October 13, 2022
 */
@Suppress("LateinitUsage")
class SubmitPostService : JobIntentServiceX() {

    @Inject
    lateinit var submitPostUseCase: SubmitPostUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var sellerAppReviewHelper: com.tokopedia.createpost.common.view.util.FeedSellerAppReviewHelper

    @Inject
    @SubmitPostCoroutineScope
    lateinit var scope: CoroutineScope

    @Inject
    @CreatePostCommonDispatchers
    lateinit var dispatchers: CoroutineDispatchers

    private var postUpdateProgressManager: PostUpdateProgressManager? = null

    companion object {
        private const val JOB_ID = 13131314

        fun startService(context: Context, draftId: String) {
            val work = Intent(context, SubmitPostService::class.java).apply {
                putExtra(DRAFT_ID, draftId)
            }
            enqueueWork(context, SubmitPostService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        initInjector()
        super.onCreate()
    }

    override fun onHandleWork(intent: Intent) {
        val id: String = intent.getStringExtra(DRAFT_ID) ?: return
        val cacheManager = SaveInstanceCacheManager(baseContext, id)
        val viewModel: CreatePostViewModel = cacheManager.get(
            CreatePostViewModel.TAG,
            CreatePostViewModel::class.java
        ) ?: return

        postUpdateProgressManager = getProgressManager(viewModel)
        postUpdateProgressManager!!.setCreatePostData(viewModel)
        if (!viewModel.isEditState)
            postUpdateProgressManager!!.setFirstIcon(viewModel.completeImageList.first().path)

        postUpdateProgressManager?.isEditPostValue(viewModel.isEditState)
        postUpdateProgressManager?.onAddProgress()

        submitPostUseCase.postUpdateProgressManager = postUpdateProgressManager

        scope.launchCatchError(block = {
            submitPostUseCase.state.collectLatest { state ->
                when(state) {
                    is SubmitPostResult.Fail -> {
                        postUpdateProgressManager?.onFailedPost(
                            com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
                                this@SubmitPostService,
                                state.throwable,
                            )
                        )
                        stopService()
                    }
                    is SubmitPostResult.Success -> {
                        val result = state.data

                        if (result == null || result.feedContentSubmit.success != SubmitPostData.SUCCESS) {
                            postUpdateProgressManager?.onFailedPost(
                                com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
                                    this@SubmitPostService,
                                    RuntimeException()
                                )
                            )
                            stopService()

                            return@collectLatest
                        } else if (!TextUtils.isEmpty(result.feedContentSubmit.error)) {
                            postUpdateProgressManager?.onFailedPost(result.feedContentSubmit.error)
                            stopService()

                            return@collectLatest
                        }


                        withContext(dispatchers.main) {
                            postUpdateProgressManager?.onSuccessPost()
                            sendBroadcast()
                            addFlagOnCreatePostSuccess()

                            stopService()
                        }
                    }
                }
            }
        }) {
            postUpdateProgressManager?.onFailedPost(
                com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
                    this@SubmitPostService,
                    it,
                )
            )

            stopService()
        }

        scope.launch {
            submitPostUseCase.execute(
                viewModel.postId,
                viewModel.authorType,
                viewModel.token,
                if (isTypeAffiliate(viewModel.authorType) || isTypeBuyer(viewModel.authorType)) userSession.userId
                else userSession.shopId,
                viewModel.caption,
                viewModel.completeImageList.map {
                    getFileAbsolutePath(it.path)!! to it.type
                },
                if (isTypeAffiliate(viewModel.authorType)) viewModel.adIdList
                else viewModel.productIdList, viewModel.completeImageList,
                viewModel.mediaWidth,
                viewModel.mediaHeight
            )
        }


        /**
         * The code below will be used when we have migrated
         * both image & video uploader to uploadpedia
         */
//        scope.launchCatchError(block = {
//            val result = submitPostUseCase.executeOnBackground(
//                viewModel.postId,
//                viewModel.authorType,
//                viewModel.token,
//                if (isTypeAffiliate(viewModel.authorType) || isTypeBuyer(viewModel.authorType)) userSession.userId
//                else userSession.shopId,
//                viewModel.caption,
//                viewModel.completeImageList.map {
//                    getFileAbsolutePath(it.path)!! to it.type
//                },
//                if (isTypeAffiliate(viewModel.authorType)) viewModel.adIdList
//                else viewModel.productIdList, viewModel.completeImageList,
//                viewModel.mediaWidth,
//                viewModel.mediaHeight
//            )
//
//            if (result == null || result.feedContentSubmit.success != SubmitPostData.SUCCESS) {
//                postUpdateProgressManager?.onFailedPost(
//                    com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
//                        this@SubmitPostServiceNew,
//                        RuntimeException()
//                    )
//                )
//                return@launchCatchError
//            } else if (!TextUtils.isEmpty(result.feedContentSubmit.error)) {
//                postUpdateProgressManager?.onFailedPost(result.feedContentSubmit.error)
//                return@launchCatchError
//            }
//
//
//            postUpdateProgressManager?.onSuccessPost()
//            sendBroadcast()
//            postContentToOtherService(result.feedContentSubmit.meta.content)
//            addFlagOnCreatePostSuccess()
//        }) {
//            postUpdateProgressManager?.onFailedPost(
//                com.tokopedia.abstraction.common.utils.network.ErrorHandler.getErrorMessage(
//                    this@SubmitPostServiceNew,
//                    it,
//                )
//            )
//        }
    }

    private fun stopService() {
        stopSelf()
        scope.cancel()
    }

    private fun getFileAbsolutePath(path: String): String? {
        return if (path.startsWith("${ContentResolver.SCHEME_FILE}://"))
            Uri.parse(path).path
        else
            path
    }

    private fun initInjector() {
        DaggerCreatePostCommonComponent.builder()
            .createPostCommonModule(CreatePostCommonModule(this.applicationContext))
            .build()
            .inject(this)
    }

    private fun isTypeAffiliate(authorType: String) = authorType == TYPE_AFFILIATE
    private fun isTypeBuyer(authorType: String) = authorType == TYPE_CONTENT_USER

    private fun getProgressManager(viewModel: CreatePostViewModel): PostUpdateProgressManager {
        val firstImage = ""
        try {
            com.tokopedia.createpost.common.view.util.FileUtil.createFilePathFromUri(
                applicationContext,
                Uri.parse(viewModel.completeImageList.firstOrNull()?.path ?: "")
            )
        } catch (e: Exception) {
            Timber.e("Exception")
        }
        val maxCount = viewModel.completeImageList.size
        return object : PostUpdateProgressManager(maxCount, firstImage, applicationContext) {
        }
    }

    private fun sendBroadcast() {
        val intent = Intent(BROADCAST_SUBMIT_POST_NEW)
        intent.putExtra(SUBMIT_POST_SUCCESS_NEW, true)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun addFlagOnCreatePostSuccess() {
        sellerAppReviewHelper.savePostFeedFlag()
    }
}
