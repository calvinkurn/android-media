package com.tokopedia.otp.validator.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.otp.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

@ValidatorScope
@Module
class ValidatorQueryModule{

    @ValidatorScope
    @Provides
    @Named(ValidatorQueryConstant.QUERY_OTP_MODE_LIST)
    fun provideRawQueryOtpModeList(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_otp_mode_list)

    @ValidatorScope
    @Provides
    @IntoMap
    @StringKey(ValidatorQueryConstant.QUERY_OTP_VALIDATE)
    fun provideRawQueryOtpValidate(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_otp_validate)

    @ValidatorScope
    @Provides
    @IntoMap
    @StringKey(ValidatorQueryConstant.QUERY_OTP_REQUEST)
    fun provideRawQueryOtpRequest(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_otp_request)
}