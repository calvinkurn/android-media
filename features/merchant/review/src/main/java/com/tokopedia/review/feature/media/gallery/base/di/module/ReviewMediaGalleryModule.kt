package com.tokopedia.review.feature.media.gallery.base.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.review.feature.media.gallery.base.di.qualifier.ReviewMediaGalleryGson
import com.tokopedia.review.feature.media.gallery.base.di.scope.ReviewMediaGalleryScope
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.review.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.review.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.review.feature.media.player.video.presentation.model.VideoMediaItemUiModel
import com.tokopedia.reviewcommon.util.RuntimeTypeAdapterFactory
import dagger.Module
import dagger.Provides

@Module
class ReviewMediaGalleryModule {

    @Provides
    @ReviewMediaGalleryScope
    @ReviewMediaGalleryGson
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(MediaItemUiModel::class.java)
                    .registerSubtype(LoadingStateItemUiModel::class.java, LoadingStateItemUiModel::class.java.name)
                    .registerSubtype(ImageMediaItemUiModel::class.java, ImageMediaItemUiModel::class.java.name)
                    .registerSubtype(VideoMediaItemUiModel::class.java, VideoMediaItemUiModel::class.java.name)
            ).create()
    }
}
