package com.tokopedia.product.detail.di
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named
/**
 * Created by Yehezkiel on 19/08/21
 */
@Module
class ProductDetailModuleTesting {
    @ProductDetailScope
    @Provides
    @Named("layoutIdPdp")
    fun provideLayoutIdTesting(@ApplicationContext context: Context): String {
        return if (!GlobalConfig.isAllowDebuggingTools()) {
            ""
        } else {
            context.getSharedPreferences("layoutIdSharedPref", Context.MODE_PRIVATE)?.getString("layoutIdTest", "")
                    ?: ""
        }
    }
}