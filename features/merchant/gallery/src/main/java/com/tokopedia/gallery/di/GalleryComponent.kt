package com.tokopedia.gallery.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gallery.ImageReviewGalleryFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [GalleryViewModelModule::class, GalleryModule::class], dependencies = [BaseAppComponent::class])
@GalleryScope
interface GalleryComponent {
    @ApplicationContext
    fun getContext(): Context

    fun getRetrofitBuilder(): Retrofit.Builder

    fun getGraphqlRepository(): GraphqlRepository

    fun coroutineDispatcher(): CoroutineDispatchers

    fun inject(galleryFragment: ImageReviewGalleryFragment)
}