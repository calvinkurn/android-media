package com.tokopedia.play.broadcaster.setup.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagBindModule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.config.ProductConfigStore
import com.tokopedia.play.broadcaster.di.*
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [
        PlayBroadcastRepositoryTestModule::class,
        PlayBroadcastTestModule::class,
        PlayBroadcastBindTestModule::class,

        PlayBroadcastFragmentModule::class,
        PlayBroadcastViewModelModule::class,
        PlayBroadcastConfigStoreModule::class,
        PlayBroadcastDataStoreModule::class,
        PlayBroadcastNetworkModule::class,
        ContentCreationProductTagBindModule::class,
        UGCOnboardingModule::class
    ]
)
@ActivityRetainedScope
abstract class PlayBroadcastTestComponent : ActivityRetainedComponent()  {

    abstract override fun appContext(): Context

    abstract override fun userSession(): UserSessionInterface

    abstract override fun graphqlRepository(): GraphqlRepository

    abstract override fun permissionPrefs(): PermissionSharedPreferences

    abstract override fun mapper(): PlayBroadcastMapper

    abstract override fun coroutineDispatcher(): CoroutineDispatchers

    abstract override fun broadcastAnalytic(): PlayBroadcastAnalytic

    abstract override fun channelConfigStore(): ChannelConfigStore

    abstract override fun productConfigStore(): ProductConfigStore

    abstract override fun hydraConfigStore(): HydraConfigStore

    abstract override fun navBarDialogCustomizer(): PlayBroadcastDialogCustomizer

    abstract override fun broadcaster(): Broadcaster

    abstract override fun broadcastTimer(): PlayBroadcastTimer

    abstract override fun playWebSocket(): PlayWebSocket

    abstract override fun playBroRepository(): PlayBroadcastRepository

    abstract override fun playLogger(): PlayLogger

    abstract override fun hydraSharedPref(): HydraSharedPreferences

    abstract override fun inject(broadcastActivity: PlayBroadcastActivity)

    abstract override fun inject(activity: PlayCoverCameraActivity)
}
