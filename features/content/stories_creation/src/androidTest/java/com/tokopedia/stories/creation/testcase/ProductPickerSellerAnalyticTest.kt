package com.tokopedia.stories.creation.testcase

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.stories.creation.helper.ProductPickerLauncher
import com.tokopedia.stories.creation.provider.ProductPickerTestActivityProvider
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 19, 2023
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductPickerSellerAnalyticTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val launcher = ProductPickerLauncher(targetContext)

    private val mockRepository: ContentProductPickerSellerRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        ProductPickerTestActivityProvider.mockRepository = mockRepository
        
        launcher.launchActivity()
    }

    @Test
    fun testAnalytic_storiesCreation_viewProductChooser() {

    }
}
