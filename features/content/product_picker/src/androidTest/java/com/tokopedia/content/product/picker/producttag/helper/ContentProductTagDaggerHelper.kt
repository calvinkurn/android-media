package com.tokopedia.content.product.picker.producttag.helper

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.content.product.picker.ugc.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.product.picker.producttag.di.ContentCreationProductTagTestModule
import com.tokopedia.content.product.picker.producttag.di.ContentProductTagTestInjector
import com.tokopedia.content.product.picker.producttag.di.ContentProductTagTestModule
import com.tokopedia.content.product.picker.producttag.di.DaggerContentProductTagTestComponent
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.ugc.util.preference.ProductTagPreference
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
class ContentProductTagDaggerHelper(
    private val context: Context,
) {

    fun setupDagger(
        mockUserSession: UserSessionInterface = mockk(relaxed = true),
        mockRepo: ProductTagRepository = mockk(relaxed = true),
        mockAnalytic: ContentProductTagAnalytic = mockk(relaxed = true),
        mockProductTagPreference: ProductTagPreference = mockk(relaxed = true),
    ) {
        ContentProductTagTestInjector.set(
            DaggerContentProductTagTestComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .contentProductTagTestModule(
                    ContentProductTagTestModule(
                        mockUserSession = mockUserSession,
                        mockAnalytic = mockAnalytic,
                        mockProductTagPreference = mockProductTagPreference,
                    )
                )
                .contentCreationProductTagTestModule(
                    ContentCreationProductTagTestModule(
                        mockRepo = mockRepo,
                    )
                )
                .build()
        )
    }
}
