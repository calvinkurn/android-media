package com.tokopedia.fcmcommon.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class FcmQueryModule {

    @Provides
    @IntoMap
    @FcmScope
    @StringKey(FirebaseMessagingManager.QUERY_UPDATE_FCM_TOKEN)
    fun provideQueryUpdateFcmToken(@ApplicationContext context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_update_fcm_token)
    }

}