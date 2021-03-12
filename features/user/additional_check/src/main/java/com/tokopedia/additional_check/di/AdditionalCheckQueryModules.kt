package com.tokopedia.additional_check.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.internal.AdditionalCheckConstants
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class AdditionalCheckQueryModules {

    @AdditionalCheckScope
    @Provides
    @IntoMap
    @StringKey(AdditionalCheckConstants.QUERY_CHECK_BOTTOM_SHEET)
    fun provideRawQueryStatusPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_show_interrupt)

}