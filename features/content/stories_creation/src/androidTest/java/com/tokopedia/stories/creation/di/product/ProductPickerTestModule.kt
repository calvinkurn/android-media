package com.tokopedia.stories.creation.di.product

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
@Module
class ProductPickerTestModule(
    private val mockRepository: ContentProductPickerSellerRepository,
    private val mockCommonRepository: ProductPickerSellerCommonRepository,
) {

    @Provides
    @ProductPickerTestScope
    fun provideContentProductPickerSellerRepository() = mockRepository

    @Provides
    @ProductPickerTestScope
    fun provideProductPickerSellerCommonRepository() = mockCommonRepository

    @Provides
    @ProductPickerTestScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
