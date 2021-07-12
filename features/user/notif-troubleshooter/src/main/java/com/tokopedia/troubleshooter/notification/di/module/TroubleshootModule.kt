package com.tokopedia.troubleshooter.notification.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingnotif.usersetting.base.BaseSettingRepository
import com.tokopedia.settingnotif.usersetting.base.SettingRepository
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.data.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManager
import com.tokopedia.troubleshooter.notification.data.service.fcm.FirebaseInstanceManagerImpl
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationChannelManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationChannelManagerImpl
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManager
import com.tokopedia.troubleshooter.notification.data.service.notification.NotificationCompatManagerImpl
import com.tokopedia.troubleshooter.notification.data.service.ringtone.RingtoneModeService
import com.tokopedia.troubleshooter.notification.data.service.ringtone.RingtoneModeServiceImpl
import com.tokopedia.troubleshooter.notification.di.TroubleshootContext
import com.tokopedia.troubleshooter.notification.di.TroubleshootScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module class TroubleshootModule(private val context: Context) {

    @Provides
    @TroubleshootContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @TroubleshootScope
    fun provideUserSession(
            @TroubleshootContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @TroubleshootScope
    fun provideFirebaseInstanceManager(): FirebaseInstanceManager {
        return FirebaseInstanceManagerImpl()
    }

    @Provides
    @TroubleshootScope
    fun provideNotificationChannelManager(): NotificationChannelManager {
        return NotificationChannelManagerImpl(context)
    }

    @Provides
    @TroubleshootScope
    fun provideNotificationCompatManager(): NotificationCompatManager {
        return NotificationCompatManagerImpl(context)
    }

    @Provides
    @TroubleshootScope
    fun provideRingtoneModeService(): RingtoneModeService {
        return RingtoneModeServiceImpl(context)
    }

    @Provides
    @TroubleshootScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @TroubleshootScope
    fun provideSettingRepository(): SettingRepository {
        return BaseSettingRepository(GraphqlInteractor.getInstance().graphqlRepository)
    }

    @Provides
    @Named(KEY_USER_SETTING)
    fun provideUserSettingUseCase(
            repository: SettingRepository,
            @TroubleshootContext context: Context
    ): GetUserSettingUseCase {
        return getUseSettingUseCase(repository, context, R.raw.query_push_notif_setting)
    }

    @Provides
    @Named(KEY_SELLER_SETTING)
    fun provideSellerSettingUseCase(
            repository: SettingRepository,
            @TroubleshootContext context: Context
    ): GetUserSettingUseCase {
        return getUseSettingUseCase(repository, context, R.raw.query_seller_notif_setting)
    }

    @Provides
    @TroubleshootScope
    fun provideTroubleshootUseCase(
            repository: GraphqlRepository,
            @TroubleshootContext context: Context
    ): TroubleshootStatusUseCase {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_send_notif_troubleshooter
        )
        return TroubleshootStatusUseCase(repository, query)
    }

    private fun getUseSettingUseCase(
            repository: SettingRepository,
            @TroubleshootContext context: Context,
            queryRes: Int
    ): GetUserSettingUseCase {
        val query = GraphqlHelper.loadRawString(context.resources, queryRes)
        return GetUserSettingUseCase(repository, query)
    }

    companion object {
        const val KEY_USER_SETTING = "key_user_setting"
        const val KEY_SELLER_SETTING = "key_seller_setting"
    }

}