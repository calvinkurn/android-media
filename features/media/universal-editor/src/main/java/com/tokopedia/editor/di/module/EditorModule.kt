package com.tokopedia.editor.di.module

import android.content.Context
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.analytics.image.placement.ImagePlacementAnalytics
import com.tokopedia.editor.analytics.image.placement.ImagePlacementAnalyticsImpl
import com.tokopedia.editor.analytics.input.text.InputTextAnalytics
import com.tokopedia.editor.analytics.input.text.InputTextAnalyticsImpl
import com.tokopedia.editor.analytics.main.editor.MainEditorAnalytics
import com.tokopedia.editor.analytics.main.editor.MainEditorAnalyticsImpl
import com.tokopedia.editor.ui.main.EditorParamFetcher
import com.tokopedia.editor.ui.main.EditorParamFetcherImpl
import com.tokopedia.editor.ui.player.EditorVideoLoadControl
import com.tokopedia.editor.util.provider.ColorProvider
import com.tokopedia.editor.util.provider.ColorProviderImpl
import com.tokopedia.editor.util.provider.ResourceProvider
import com.tokopedia.editor.util.provider.ResourceProviderImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class EditorModule {

    @Binds
    @ActivityScope
    internal abstract fun bindEditorParamFetcher(impl: EditorParamFetcherImpl): EditorParamFetcher

    @Binds
    @ActivityScope
    internal abstract fun bindColorProvider(impl: ColorProviderImpl): ColorProvider

    @Binds
    @ActivityScope
    internal abstract fun bindResourceProvider(impl: ResourceProviderImpl): ResourceProvider

    @Binds
    @ActivityScope
    internal abstract fun bindMainEditorAnalyticsProvider(impl: MainEditorAnalyticsImpl): MainEditorAnalytics

    @Binds
    @ActivityScope
    internal abstract fun bindInputTextAnalyticsProvider(impl: InputTextAnalyticsImpl): InputTextAnalytics

    @Binds
    @ActivityScope
    internal abstract fun bindImagePlacementAnalyticsProvider(impl: ImagePlacementAnalyticsImpl): ImagePlacementAnalytics

    @Module
    companion object {

        @Provides
        @ActivityScope
        fun providesSimpleExoPlayer(
            @ApplicationContext context: Context,
            remoteConfig: RemoteConfig
        ): SimpleExoPlayer {
            val isLoadControlExperimental = remoteConfig.getBoolean("CONTENT_EXOPLAYER_CUSTOM_LOAD_CONTROL", true)

            return SimpleExoPlayer
                .Builder(context)
                .setLoadControl(
                    if (isLoadControlExperimental) EditorVideoLoadControl()
                    else DefaultLoadControl.Builder().createDefaultLoadControl()
                )
                .build()
        }
    }
}
