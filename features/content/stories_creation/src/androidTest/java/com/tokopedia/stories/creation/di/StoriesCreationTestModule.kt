package com.tokopedia.stories.creation.di

import android.content.Context
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.picker.seller.analytic.ContentPinnedProductAnalytic
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.creation.common.presentation.utils.ContentCreationRemoteConfigManager
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.play_common.util.VideoSnapshotHelper
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on October 24, 2023
 */
@Module
class StoriesCreationTestModule(
    private val context: Context,
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true),
    private val mockRepository: StoriesCreationRepository = mockk(relaxed = true),
    private val mockCreationUploader: CreationUploader = mockk(relaxed = true),
    private val mockContentCreationRemoteConfig: ContentCreationRemoteConfigManager = mockk(relaxed = true),
    private val mockRouter: Router = mockk(relaxed = true),
    private val mockVideoSnapshotHelper: VideoSnapshotHelper = mockk(relaxed = true),
) {

    @Provides
    @StoriesCreationTestScope
    fun provideActivityContext() = context

    @Provides
    @StoriesCreationTestScope
    fun provideUserSession() = mockUserSession

    @Provides
    @StoriesCreationTestScope
    fun provideStoriesCreationRepository() = mockRepository

    @Provides
    @StoriesCreationTestScope
    fun provideCreationUploader() = mockCreationUploader

    @Provides
    @StoriesCreationTestScope
    fun provideContentCreationRemoteConfigManager() = mockContentCreationRemoteConfig

    @Provides
    @StoriesCreationTestScope
    fun provideRouter() = mockRouter

    @Provides
    @StoriesCreationTestScope
    fun provideVideoSnapshotHelper() = mockVideoSnapshotHelper

    @Provides
    @StoriesCreationTestScope
    fun provideStoriesCreationProductSellerAnalytic() = mockk<ContentProductPickerSellerAnalytic>(relaxed = true)

    @Provides
    @StoriesCreationTestScope
    fun provideContentPinnedProductAnalytic() = mockk<ContentPinnedProductAnalytic>(relaxed = true)

    @Provides
    @StoriesCreationTestScope
    fun provideContentProductPickerSellerRepository() = mockk<ContentProductPickerSellerRepository>(relaxed = true)
}
