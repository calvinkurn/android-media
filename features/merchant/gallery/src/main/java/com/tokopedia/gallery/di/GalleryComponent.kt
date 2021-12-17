package com.tokopedia.gallery.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gallery.ImageReviewGalleryFragment
import dagger.Component

@Component(modules = [GalleryViewModelModule::class, GalleryModule::class], dependencies = [BaseAppComponent::class])
@GalleryScope
interface GalleryComponent {
    fun inject(galleryFragment: ImageReviewGalleryFragment)
}