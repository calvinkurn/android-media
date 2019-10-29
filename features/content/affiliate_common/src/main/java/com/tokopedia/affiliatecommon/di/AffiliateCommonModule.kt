package com.tokopedia.affiliatecommon.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.MUTATION_AFFILIATE_TRACKING
import com.tokopedia.affiliatecommon.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by milhamj on 2019-08-20.
 */

@Module
class AffiliateCommonModule {
    @Provides
    @Named(MUTATION_AFFILIATE_TRACKING)
    fun provideRawProductInfo(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_af_tracking)
}