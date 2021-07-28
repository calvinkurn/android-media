package com.tokopedia.play.broadcaster.di.broadcast

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.config.ProductConfigStore
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import com.tokopedia.play.broadcaster.view.activity.PlayBroadcastActivity
import com.tokopedia.play.broadcaster.view.activity.PlayCoverCameraActivity
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.analytic.tag.PlayBroadcastContentTaggingAnalytic
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * Created by jegul on 20/05/20
 */
@Component(
        dependencies = [BaseAppComponent::class],
        modules = [PlayBroadcastViewModelModule::class,
            PlayBroadcastModule::class,
            PlayBroadcastFragmentModule::class,
            PlayBroadcastBindModule::class]
)
@PlayBroadcastScope
interface PlayBroadcastComponent {

    @ApplicationContext fun appContext(): Context

    fun permissionPrefs(): PermissionSharedPreferences

    fun userSession(): UserSessionInterface

    fun inject(broadcastActivity: PlayBroadcastActivity)

    fun inject(activity: PlayCoverCameraActivity)

    fun analytic(): PlayBroadcastAnalytic

    fun contentTaggingAnalytic(): PlayBroadcastContentTaggingAnalytic

    fun mapper(): PlayBroadcastMapper

    fun coroutineDispatcher(): CoroutineDispatchers

    /**
     * Config
     */
    fun channelConfigStore(): ChannelConfigStore

    fun productConfigStore(): ProductConfigStore

    fun hydraConfigStore(): HydraConfigStore

    /**
     * Util
     */
    fun navBarDialogCustomizer(): PlayBroadcastDialogCustomizer
}