package com.tokopedia.product.detail.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.detail.di.RawQueryKeyConstant.NAME_LAYOUT_ID_DAGGER
import com.tokopedia.product.detail.di.RawQueryKeyConstant.PDP_COMPONENT_FILTER_SHARED_PREF_KEY
import com.tokopedia.product.detail.di.RawQueryKeyConstant.PDP_LAYOUT_ID_KEY
import com.tokopedia.product.detail.di.RawQueryKeyConstant.PDP_LAYOUT_ID_SHARED_PREF_KEY
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Yehezkiel on 19/08/21
 */
@Module
class ProductDetailDevModule {
    @ProductDetailScope
    @Provides
    @Named(NAME_LAYOUT_ID_DAGGER)
    fun provideLayoutIdTesting(@ApplicationContext context: Context): String {
        return if (!GlobalConfig.isAllowDebuggingTools()) {
            ""
        } else {
            context.getSharedPreferences(PDP_LAYOUT_ID_SHARED_PREF_KEY, Context.MODE_PRIVATE)
                ?.getString(PDP_LAYOUT_ID_KEY, "").orEmpty()
        }
    }

    @ProductDetailScope
    @Provides
    @ComponentFilter
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            PDP_COMPONENT_FILTER_SHARED_PREF_KEY,
            Context.MODE_PRIVATE
        )
    }
}
