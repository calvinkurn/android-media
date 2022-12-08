package com.tokopedia.review.feature.bulk_write_review.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.review.feature.bulk_write_review.di.qualifier.BulkReviewGson
import com.tokopedia.review.feature.bulk_write_review.di.scope.BulkReviewScope
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetBadRatingCategoryRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitRequestState
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
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
                ).registerSubtype(BulkReviewGetFormRequestState.Requesting::class.java)
                    .registerSubtype(BulkReviewGetFormRequestState.Complete.Success::class.java)
                    .registerSubtype(BulkReviewGetFormRequestState.Complete.Error::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    BulkReviewGetBadRatingCategoryRequestState::class.java,
                    BulkReviewGetBadRatingCategoryRequestState::type.name,
                    true
                ).registerSubtype(BulkReviewGetBadRatingCategoryRequestState.Requesting::class.java)
                    .registerSubtype(BulkReviewGetBadRatingCategoryRequestState.Complete.Success::class.java)
                    .registerSubtype(BulkReviewGetBadRatingCategoryRequestState.Complete.Error::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    BulkReviewSubmitRequestState::class.java,
                    BulkReviewSubmitRequestState::type.name,
                    true
                ).registerSubtype(BulkReviewSubmitRequestState.Requesting::class.java)
                    .registerSubtype(BulkReviewSubmitRequestState.Complete.Success::class.java)
                    .registerSubtype(BulkReviewSubmitRequestState.Complete.Error::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(
                    CreateReviewMediaUploadResult::class.java,
                    CreateReviewMediaUploadResult::type.name,
                    true
                ).registerSubtype(CreateReviewMediaUploadResult.Success::class.java)
                    .registerSubtype(CreateReviewMediaUploadResult.Error::class.java)
            )
            .create()
    }
}
