package com.tokopedia.phoneverification.di.revamp

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.phoneverification.PhoneVerificationConst
import com.tokopedia.phoneverification.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class PhoneVerificationQueryModule(private val activity: Activity)  {

    @PhoneVerificationScope
    @Provides
    fun getContext(): Context = activity


    @PhoneVerificationScope
    @Provides
    @IntoMap
    @StringKey(PhoneVerificationConst.MUTATION_USER_MSISDN_ADD)
    fun provideRawQueryUserMsisdnAdd(@PhoneVerificationScope context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_user_msisdn_add)

}