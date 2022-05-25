package com.tokopedia.review.feature.inboxreview.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.review.feature.inboxreview.di.qualifier.InboxReviewGson
import com.tokopedia.review.feature.inboxreview.di.scope.InboxReviewScope
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
import dagger.Module
import dagger.Provides

@Module
class InboxReviewModule {
    @Provides
    @InboxReviewScope
    @InboxReviewGson
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