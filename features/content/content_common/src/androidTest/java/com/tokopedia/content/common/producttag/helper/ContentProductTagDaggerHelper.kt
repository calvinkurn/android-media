package com.tokopedia.content.common.producttag.helper

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.content.common.producttag.di.ContentCreationProductTagTestModule
import com.tokopedia.content.common.producttag.di.ContentProductTagTestInjector
import com.tokopedia.content.common.producttag.di.ContentProductTagTestModule
import com.tokopedia.content.common.producttag.di.DaggerContentProductTagTestComponent
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
class ContentProductTagDaggerHelper(
    private val context: Context,
) {

    fun setupDagger(
        mockUserSession: UserSessionInterface,
        mockRepo: ProductTagRepository,
    ) {
        ContentProductTagTestInjector.set(
            DaggerContentProductTagTestComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .contentProductTagTestModule(
                    ContentProductTagTestModule(
                        mockUserSession = mockUserSession,
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