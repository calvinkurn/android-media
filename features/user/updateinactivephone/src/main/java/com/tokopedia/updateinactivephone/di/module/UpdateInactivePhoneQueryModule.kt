package com.tokopedia.updateinactivephone.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants
import com.tokopedia.updateinactivephone.di.UpdateInActiveQualifier
import com.tokopedia.updateinactivephone.di.UpdateInactivePhoneScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class UpdateInactivePhoneQueryModule {
    @UpdateInactivePhoneScope
    @Provides
    @IntoMap
    @StringKey(UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_CHECK_PHONE_NUMBER_STATUS)
    fun provideRawQueryCheckPhoneNumberStatus(@UpdateInActiveQualifier context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_check_phone_number_status)

    @UpdateInactivePhoneScope
    @Provides
    @IntoMap
    @StringKey(UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_VALIDATE_USER_DATA)
    fun provideRawQueryValidateUserData(@UpdateInActiveQualifier context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_validate_user_data)

    @UpdateInactivePhoneScope
    @Provides
    @IntoMap
    @StringKey(UpdateInactivePhoneConstants.UpdateInactivePhoneQueryConstant.QUERY_UPDATE_PHONE_EMAIL)
    fun provideRawQueryUpdatePhoneEmail(@UpdateInActiveQualifier context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_update_phone_email)
}