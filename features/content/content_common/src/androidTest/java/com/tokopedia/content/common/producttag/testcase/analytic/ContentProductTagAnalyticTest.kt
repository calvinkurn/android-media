package com.tokopedia.content.common.producttag.testcase.analytic

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.VerificationModes.times
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.R
import com.tokopedia.content.common.const.DEFAULT_DELAY
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.builder.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.builder.LastTaggedModelBuilder
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.helper.*
import com.tokopedia.content.common.producttag.helper.breadcrumb
import com.tokopedia.content.common.producttag.helper.fakeSearchBar
import com.tokopedia.content.common.producttag.helper.globalSearchShopRv
import com.tokopedia.content.common.producttag.helper.lastTaggedSearchBar
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ContentProductTagAnalyticTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** Mock */
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAnalytic: ContentProductTagAnalytic = mockk(relaxed = true)

    /** Builder */
    private val lastTaggedModelBuilder = LastTaggedModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()

    /** Helper */
    private val daggerHelper = ContentProductTagDaggerHelper(targetContext)

    /** Model */
    private val lastTaggedProduct = lastTaggedModelBuilder.buildPagedDataModel()
    private val aceProduct = globalSearchModelBuilder.buildResponseModel()
    private val aceShop = globalSearchModelBuilder.buildShopResponseModel()
    private val quickFilter = globalSearchModelBuilder.buildQuickFilterList()
    private val keyword = "pokemon"

    private fun launchActivity(argumentBuilder: ContentProductTagArgument.Builder) {
        ActivityScenario.launch<ContentProductTagTestActivity>(
            Intent(targetContext, ContentProductTagTestActivity::class.java).apply {
                putExtra(ContentProductTagTestActivity.EXTRA_ARGUMENT, argumentBuilder.build())
            }
        )

        delay(DEFAULT_DELAY)
    }

    init {
        coEvery { mockUserSession.userId } returns "123"
        coEvery { mockUserSession.shopId } returns "456"

        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns lastTaggedProduct
        coEvery { mockRepo.searchAceProducts(any()) } returns aceProduct
        coEvery { mockRepo.searchAceShops(any()) } returns aceShop
        coEvery { mockRepo.getQuickFilter(any(), any()) } returns quickFilter

        coEvery { mockAnalytic.clickBreadcrumb(any()) } returns Unit

        daggerHelper.setupDagger(
            mockUserSession = mockUserSession,
            mockRepo = mockRepo,
            mockAnalytic = mockAnalytic,
        )
    }

    /**
     * Notes : This test only verify whether the
     * analytic interface is being hit or not
     * because the analytic implementation can be vary
     * from each other that implement it.
     *
     * If you want verify the analytic field as well,
     * please do so on your module.
     */

    @Test
    fun contentProductTag_clickBreadcrumb_nonShopSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
        )

        click(breadcrumb)

        verify { mockAnalytic.clickBreadcrumb(false) }
    }

    @Test
    fun contentProductTag_ugc_clickBreadcrumb_shopSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        click(lastTaggedSearchBar)
        delay()

        type(fakeSearchBar, keyword)
        delay()

        pressActionSoftKeyboard(fakeSearchBar)
        delay()

        click(targetContext.getString(R.string.content_creation_toko_text))
        delay()

        clickItemRecyclerView(globalSearchShopRv, 0)
        delay()

        click(breadcrumb)
        delay()

        verify { mockAnalytic.clickBreadcrumb(true) }
    }
}