package com.tokopedia.play.broadcaster.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalytic
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.config.ProductConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
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

@Component(
    dependencies = [BaseAppComponent::class],
    modules = [
        PlayBroadcastFragmentModule::class,
        PlayBroadcastViewModelModule::class,
        PlayBroadcastConfigStoreModule::class,
        PlayBroadcastDataStoreModule::class,
        PlayBroadcastRepositoryModule::class,
        PlayBroadcastBindModule::class,
        PlayBroadcastModule::class
    ]
)
@ActivityRetainedScope
abstract class ActivityRetainedComponent : ViewModel() {

    @ApplicationContext
    abstract fun appContext(): Context

    abstract fun userSession(): UserSessionInterface

    abstract fun graphqlRepository(): GraphqlRepository

    abstract fun permissionPrefs(): PermissionSharedPreferences

    abstract fun contentTaggingAnalytic(): PlayBroadcastContentTaggingAnalytic

    abstract fun mapper(): PlayBroadcastMapper

    abstract fun coroutineDispatcher(): CoroutineDispatchers

    abstract fun broadcastAnalytic(): PlayBroadcastAnalytic

    /**
     * Config
     */
    abstract fun channelConfigStore(): ChannelConfigStore

    abstract fun productConfigStore(): ProductConfigStore

    abstract fun hydraConfigStore(): HydraConfigStore

    /**
     * Util
     */
    abstract fun navBarDialogCustomizer(): PlayBroadcastDialogCustomizer

    abstract fun broadcaster(): Broadcaster

    abstract fun playWebSocket(): PlayWebSocket

    abstract fun playBroRepository(): PlayBroadcastRepository

    abstract fun playLogger(): PlayLogger

    abstract fun hydraSharedPref(): HydraSharedPreferences

    /**
     * Inject
     */
    abstract fun inject(broadcastActivity: PlayBroadcastActivity)

    abstract fun inject(activity: PlayCoverCameraActivity)
}