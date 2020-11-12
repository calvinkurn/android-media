package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.buyerorder.unifiedhistory.common.di.UohScope
import com.tokopedia.buyerorder.unifiedhistory.list.view.fragment.UohListFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by fwidjaja on 10/11/20.
 */
@UohListScope
@Module
class UohListModule constructor(val uohListFragment: UohListFragment) {

    @UohListScope
    @Provides
    fun provideContext(): Context? = uohListFragment.activity

    @UohListScope
    @Provides
    @Named("atcMutation")
    fun provideAddToCartMutation(context: Context?): String {
        return GraphqlHelper.loadRawString(context?.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }
}