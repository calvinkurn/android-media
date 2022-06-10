package com.tokopedia.review.feature.reviewdetail.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.review.feature.reviewdetail.analytics.ProductReviewDetailTracking
import com.tokopedia.review.feature.reviewdetail.di.qualifier.ReviewDetailGson
import com.tokopedia.review.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
import dagger.Module
import dagger.Provides

@Module(includes = [ReviewProductDetailViewModelModule::class])
class ReviewProductDetailModule {
    @ReviewDetailScope
    @Provides
    fun provideProductReviewDetailTracking(): ProductReviewDetailTracking {
        return ProductReviewDetailTracking()
    }

    @Provides
    @ReviewDetailScope
    @ReviewDetailGson
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ReviewMediaThumbnailVisitable::class.java)
                    .registerSubtype(ReviewMediaImageThumbnailUiModel::class.java, ReviewMediaImageThumbnailUiModel::class.java.name)
                    .registerSubtype(ReviewMediaVideoThumbnailUiModel::class.java, ReviewMediaVideoThumbnailUiModel::class.java.name)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ReviewMediaImageThumbnailUiState::class.java)
                    .registerSubtype(ReviewMediaImageThumbnailUiState.Showing::class.java, ReviewMediaImageThumbnailUiState.Showing::class.java.name)
                    .registerSubtype(ReviewMediaImageThumbnailUiState.ShowingSeeMore::class.java, ReviewMediaImageThumbnailUiState.ShowingSeeMore::class.java.name)
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ReviewMediaVideoThumbnailUiState::class.java)
                    .registerSubtype(ReviewMediaVideoThumbnailUiState.Showing::class.java, ReviewMediaVideoThumbnailUiState.Showing::class.java.name)
                    .registerSubtype(ReviewMediaVideoThumbnailUiState.ShowingSeeMore::class.java, ReviewMediaVideoThumbnailUiState.ShowingSeeMore::class.java.name)
            )
            .create()
    }
}