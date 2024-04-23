package com.tokopedia.product.preview

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreferences
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.product.preview.data.ProductPreviewMockData
import com.tokopedia.product.preview.robot.ProductPreviewViewModelUiTestRobot
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Rule
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductPreviewUiTest {

    @get:Rule
    var cassavaTest = CassavaTestRule(sendValidationResult = false)

    private val analyticProductPreviewTracker = "tracker/content/productpreview/product_preview.json"

    private val mockData = ProductPreviewMockData()
    private val repository: ProductPreviewRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val productPreviewPref: ProductPreviewSharedPreferences = mockk(relaxed = true)

    private fun getProductPreviewRobot(
        sourceModel: ProductPreviewSourceModel
    ) = ProductPreviewViewModelUiTestRobot(
        productPreviewSourceModel = sourceModel,
        repo = repository,
        userSession = userSession,
        productPreviewPref = productPreviewPref
    )

    private val productId = "123"
}
