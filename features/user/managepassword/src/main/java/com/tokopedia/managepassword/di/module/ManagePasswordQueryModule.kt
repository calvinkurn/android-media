package com.tokopedia.managepassword.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.common.ManagePasswordConstant
import com.tokopedia.managepassword.di.ManagePasswordScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class ManagePasswordQueryModule {

    @ManagePasswordScope
    @Provides
    @IntoMap
    @StringKey(ManagePasswordConstant.QUERY_CHANGE_PASSWORD)
    fun provideChangePasswordQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_change_password)
    }

    @ManagePasswordScope
    @Provides
    @IntoMap
    @StringKey(ManagePasswordConstant.QUERY_FORGOT_PASSWORD)
    fun provideForgotPasswordQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_reset_password)
    }
}