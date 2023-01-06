package com.tokopedia.review.feature.bulk_write_review.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.review.feature.bulk_write_review.di.qualifier.BulkReviewGson
import com.tokopedia.review.feature.bulk_write_review.di.scope.BulkReviewScope
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class BulkReviewModule {
    @Provides
    @BulkReviewScope
    @BulkReviewGson
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    BulkReviewGetFormRequestState::class.java,
                    BulkReviewGetFormRequestState::type.name,
                    true
                ).registerSubtype(
                    BulkReviewGetFormRequestState.Requesting::class.java,
                    BulkReviewGetFormRequestState.Requesting::class.java.name
                ).registerSubtype(
                    BulkReviewGetFormRequestState.Complete.Success::class.java,
                    BulkReviewGetFormRequestState.Complete.Success::class.java.name
                ).registerSubtype(
                    BulkReviewGetFormRequestState.Complete.Error::class.java,
                    BulkReviewGetFormRequestState.Complete.Error::class.java.name
                )
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    BulkReviewGetBadRatingCategoryRequestState::class.java,
                    BulkReviewGetBadRatingCategoryRequestState::type.name,
                    true
                ).registerSubtype(
                    BulkReviewGetBadRatingCategoryRequestState.Requesting::class.java,
                    BulkReviewGetBadRatingCategoryRequestState.Requesting::class.java.name
                ).registerSubtype(
                    BulkReviewGetBadRatingCategoryRequestState.Complete.Success::class.java,
                    BulkReviewGetBadRatingCategoryRequestState.Complete.Success::class.java.name
                ).registerSubtype(
                    BulkReviewGetBadRatingCategoryRequestState.Complete.Error::class.java,
                    BulkReviewGetBadRatingCategoryRequestState.Complete.Error::class.java.name
                )
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    BulkReviewSubmitRequestState::class.java,
                    BulkReviewSubmitRequestState::type.name,
                    true
                ).registerSubtype(
                    BulkReviewSubmitRequestState.Requesting::class.java,
                    BulkReviewSubmitRequestState.Requesting::class.java.name
                ).registerSubtype(
                    BulkReviewSubmitRequestState.Complete.Success::class.java,
                    BulkReviewSubmitRequestState.Complete.Success::class.java.name
                ).registerSubtype(
                    BulkReviewSubmitRequestState.Complete.Error::class.java,
                    BulkReviewSubmitRequestState.Complete.Error::class.java.name
                )
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    CreateReviewMediaUploadResult::class.java,
                    CreateReviewMediaUploadResult::type.name,
                    true
                ).registerSubtype(
                    CreateReviewMediaUploadResult.Success::class.java,
                    CreateReviewMediaUploadResult.Success::class.java.name
                ).registerSubtype(
                    CreateReviewMediaUploadResult.Error::class.java,
                    CreateReviewMediaUploadResult.Error::class.java.name
                )
            )
            .create()
    }

    @Provides
    @BulkReviewScope
    fun provideTrackingQueue(
        @ApplicationContext context: Context
    ): TrackingQueue {
        return TrackingQueue(context)
    }
}
