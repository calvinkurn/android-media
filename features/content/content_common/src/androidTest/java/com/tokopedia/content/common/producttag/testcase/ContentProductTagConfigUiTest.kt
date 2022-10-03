package com.tokopedia.content.common.producttag.testcase

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.R
import com.tokopedia.content.common.const.DEFAULT_DELAY
import com.tokopedia.content.common.const.INITIAL_DELAY
import com.tokopedia.content.common.producttag.builder.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.builder.LastTaggedModelBuilder
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity.Companion.EXTRA_ARGUMENT
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.helper.ContentProductTagDaggerHelper
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.test.util.isHidden
import com.tokopedia.play.test.util.isVisible
import com.tokopedia.play.test.util.select
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ContentProductTagConfigUiTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** Mock */
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    /** Builder */
    private val lastTaggedModelBuilder = LastTaggedModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()

    /** Helper */
    private val daggerHelper = ContentProductTagDaggerHelper(targetContext)

    /** Model */
    private val lastTaggedProduct = lastTaggedModelBuilder.buildPagedDataModel()
    private val aceProduct = globalSearchModelBuilder.buildResponseModel()

    private fun launchActivity(argumentBuilder: ContentProductTagArgument.Builder) {
        ActivityScenario.launch<ContentProductTagTestActivity>(
            Intent(targetContext, ContentProductTagTestActivity::class.java).apply {
                putExtra(EXTRA_ARGUMENT, argumentBuilder.build())
            }
        )
    }

    init {
        coEvery { mockUserSession.userId } returns "123"
        coEvery { mockUserSession.shopId } returns "456"

        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns lastTaggedProduct
        coEvery { mockRepo.searchAceProducts(any()) } returns aceProduct

        daggerHelper.setupDagger(
            mockUserSession = mockUserSession,
            mockRepo = mockRepo,
        )
    }

    /**
     * Test:
     * 1. (DONE) authorType -> breadcrumb is not showing
     * 2. productTagSource
     * 3. isMultipleSelectionProduct -> checkbox show or not & select unselect
     * 4. isFullPageAutocomplete -> from us or not
     * 5. isShowActionBarDivider
     */

    @Test
    fun contentProductTag_ugc_breadcrumbVisible() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
        )

        delay(INITIAL_DELAY)

        select(R.id.tv_cc_product_tag_product_source_label)
            .isVisible()

        select(R.id.tv_cc_product_tag_product_source)
            .isVisible()

        select(R.id.ic_cc_product_tag_chevron_1)
            .isVisible()

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_seller_breadcrumbHidden() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_SHOP)
        )

        delay(INITIAL_DELAY)

        select(R.id.tv_cc_product_tag_product_source_label)
            .isHidden()

        select(R.id.tv_cc_product_tag_product_source)
            .isHidden()

        select(R.id.ic_cc_product_tag_chevron_1)
            .isHidden()

        delay(DEFAULT_DELAY)
    }
}