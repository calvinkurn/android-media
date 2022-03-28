package com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.component

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.module.DetailedReviewMediaGalleryViewModelModule
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.scope.DetailedReviewMediaGalleryScope
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity
import dagger.Component

@Component(
    modules = [DetailedReviewMediaGalleryViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
@DetailedReviewMediaGalleryScope
interface DetailedReviewMediaGalleryComponent {
    fun inject(activity: DetailedReviewMediaGalleryActivity)

    @DetailedReviewMediaGalleryViewModelFactory
    fun viewModelFactory(): ViewModelProvider.Factory
}