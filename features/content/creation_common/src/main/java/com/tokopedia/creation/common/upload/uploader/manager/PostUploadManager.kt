package com.tokopedia.creation.common.upload.uploader.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.createpost.common.di.qualifier.SubmitPostCoroutineScope
import com.tokopedia.createpost.common.domain.usecase.SubmitPostUseCase
import com.tokopedia.createpost.common.view.util.FeedSellerAppReviewHelper
import com.tokopedia.creation.common.upload.model.CreationUploadData
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 21, 2023
 */
class PostUploadManager @Inject constructor(
    private val submitPostUseCase: SubmitPostUseCase,
    private val sellerAppReviewHelper: FeedSellerAppReviewHelper,
    @SubmitPostCoroutineScope private val scope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers,
) : CreationUploadManager {

    override suspend fun execute(
        uploadData: CreationUploadData,
        listener: CreationUploadManagerListener
    ): Boolean {
        TODO("Not yet implemented")
    }
}
