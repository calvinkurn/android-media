package com.tokopedia.review.feature.reviewreply.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.review.feature.reviewreply.analytics.SellerReviewReplyTracking
import com.tokopedia.review.feature.reviewreply.di.qualifier.ReviewReplyGson
import com.tokopedia.review.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
import dagger.Module
import dagger.Provides

@Module(includes = [ReviewReplyViewModelModule::class])
class ReviewReplyModule {

    @ReviewReplyScope
    @Provides
    fun provideProductReviewTracking(): SellerReviewReplyTracking {
        return SellerReviewReplyTracking()
    }

    @Provides
    @ReviewReplyScope
    @ReviewReplyGson
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ReviewMediaThumbnailVisitable::class.java)
                    .registerSubtype(ReviewMediaImageThumbnailUiModel::class.java)
                    .registerSubtype(ReviewMediaVideoThumbnailUiModel::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ReviewMediaImageThumbnailUiState::class.java)
                    .registerSubtype(ReviewMediaImageThumbnailUiState.Showing::class.java)
                    .registerSubtype(ReviewMediaImageThumbnailUiState.ShowingSeeMore::class.java)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ReviewMediaVideoThumbnailUiState::class.java)
                    .registerSubtype(ReviewMediaVideoThumbnailUiState.Showing::class.java)
                    .registerSubtype(ReviewMediaVideoThumbnailUiState.ShowingSeeMore::class.java)
            )
            .create()
    }
}