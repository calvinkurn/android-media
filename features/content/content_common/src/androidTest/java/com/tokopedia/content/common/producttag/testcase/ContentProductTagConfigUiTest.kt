package com.tokopedia.content.common.producttag.testcase

import android.content.Intent
import androidx.appcompat.content.res.AppCompatResources
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.R
import com.tokopedia.content.common.const.DEFAULT_DELAY
import com.tokopedia.content.common.producttag.builder.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.builder.LastTaggedModelBuilder
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity.Companion.EXTRA_ARGUMENT
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.helper.ContentProductTagDaggerHelper
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.play.test.espresso.delay
import com.tokopedia.play.test.util.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.content.common.test.R as testR
import com.tokopedia.iconunify.R.drawable as iconR
import com.tokopedia.autocompletecomponent.R as autocompleteR

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

        delay(DEFAULT_DELAY)
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
     * 2. (DONE) productTagSource
     * 3. (DONE) isMultipleSelectionProduct -> checkbox show or not & select unselect
     * 4. isFullPageAutocomplete -> from us or not
     * 5. (DONE) BackButton
     * 6. (DONE) isShowActionBarDivider
     */

    /** AuthorType Test */
    @Test
    fun contentProductTag_ugc_breadcrumbVisible() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
        )

        isVisible(
            R.id.tv_cc_product_tag_product_source_label,
            R.id.tv_cc_product_tag_product_source,
            R.id.ic_cc_product_tag_chevron_1,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_seller_breadcrumbHidden() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_SHOP)
        )

        isHidden(
            R.id.tv_cc_product_tag_product_source_label,
            R.id.tv_cc_product_tag_product_source,
            R.id.ic_cc_product_tag_chevron_1,
        )

        delay(DEFAULT_DELAY)
    }

    /** ProductTagSource Test */
    @Test
    fun contentProductTag_productTagSource_completeSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                listOf(
                    ProductTagSource.GlobalSearch,
                    ProductTagSource.MyShop,
                    ProductTagSource.LastPurchase,
                ).joinToString(separator = ",") { it.tag }
            )
        )

        click(R.id.tv_cc_product_tag_product_source)

        isVisible(
            R.id.cl_global_search,

            R.id.tv_my_account_label,
            R.id.cl_last_purchase,

            R.id.tv_my_shop_label,
            R.id.cl_my_shop,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_productTagSource_noMyShopSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                listOf(
                    ProductTagSource.GlobalSearch,
                    ProductTagSource.LastPurchase,
                ).joinToString(separator = ",") { it.tag }
            )
        )

        click(R.id.tv_cc_product_tag_product_source)

        isVisible(
            R.id.cl_global_search,

            R.id.tv_my_account_label,
            R.id.cl_last_purchase,
        )

        isHidden(
            R.id.tv_my_shop_label,
            R.id.cl_my_shop,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_productTagSource_noLastPurchasedSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                listOf(
                    ProductTagSource.GlobalSearch,
                    ProductTagSource.MyShop,
                ).joinToString(separator = ",") { it.tag }
            )
        )

        click(R.id.tv_cc_product_tag_product_source)

        isVisible(
            R.id.cl_global_search,

            R.id.tv_my_shop_label,
            R.id.cl_my_shop,
        )

        isHidden(
            R.id.tv_my_account_label,
            R.id.cl_last_purchase,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_productTagSource_onlyGlobalSearch() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                listOf(
                    ProductTagSource.GlobalSearch,
                ).joinToString(separator = ",") { it.tag }
            )
        )

        click(R.id.tv_cc_product_tag_product_source)

        isVisible(
            R.id.cl_global_search,
        )

        isHidden(
            R.id.tv_my_shop_label,
            R.id.cl_my_shop,

            R.id.tv_my_account_label,
            R.id.cl_last_purchase,
        )

        delay(DEFAULT_DELAY)
    }

    /** IsMultipleSelectionProduct Test */
    @Test
    fun contentProductTag_isMultipleSelectionProduct_false_shouldFinishPicker() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(false, 0)
        )

        val position = 1
        val selectedProduct = listOf(lastTaggedProduct.dataList[position].toSelectedProduct())

        isHidden(R.id.btn_save)

        clickItemRecyclerView(R.id.rv_last_tagged_product, 1)

        select(testR.id.tv_selected_product)
            .verifyText(selectedProduct.toString())

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_checkBoxVisible_submitButtonVisible() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, 3)
        )

        verifyItemRecyclerView(R.id.rv_last_tagged_product, 0) {
            it.itemView.findViewById<CheckboxUnify>(R.id.checkbox_product).isVisible
        }

        isVisible(R.id.btn_save)

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_ableToPickUntilMaxProduct() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, 3)
        )

        val positionList = listOf(0, 1, 2)
        val selectedProducts = positionList.map { position ->
            SelectedProductUiModel.createOnlyId(lastTaggedProduct.dataList[position].id)
        }

        positionList.forEach { position ->
            clickItemRecyclerView(R.id.rv_last_tagged_product, position)
            delay(DEFAULT_DELAY)
        }

        click(R.id.btn_save)

        select(testR.id.tv_selected_product)
            .verifyText(selectedProducts.toString())

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_cannotTickMoreThanMaxProduct() {
        val maxProduct = 3
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, maxProduct)
        )

        val positionList = listOf(0, 1, 2, 3)
        val selectedProducts = positionList.take(maxProduct).map { position ->
            SelectedProductUiModel.createOnlyId(lastTaggedProduct.dataList[position].id)
        }

        positionList.forEach { position ->
            clickItemRecyclerView(R.id.rv_last_tagged_product, position)
            delay(DEFAULT_DELAY)
        }

        click(R.id.btn_save)

        select(testR.id.tv_selected_product)
            .verifyText(selectedProducts.toString())

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_doubleClickOnProduct_checkedFalse() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, 3)
        )

        val position = 0
        clickItemRecyclerView(R.id.rv_last_tagged_product, position)
        verifyItemRecyclerView(R.id.rv_last_tagged_product, position) {
            it.itemView.findViewById<CheckboxUnify>(R.id.checkbox_product).isChecked
        }
        delay(DEFAULT_DELAY)

        clickItemRecyclerView(R.id.rv_last_tagged_product, position)
        verifyItemRecyclerView(R.id.rv_last_tagged_product, position) {
            it.itemView.findViewById<CheckboxUnify>(R.id.checkbox_product).isChecked.not()
        }
        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_noProductSelected_submitButtonDisabled() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, 3)
        )

        val position = 0

        verifyButtonState(R.id.btn_save, false)

        clickItemRecyclerView(R.id.rv_last_tagged_product, position)
        verifyItemRecyclerView(R.id.rv_last_tagged_product, position) {
            it.itemView.findViewById<CheckboxUnify>(R.id.checkbox_product).isChecked
        }
        delay(DEFAULT_DELAY)

        verifyButtonState(R.id.btn_save, true)

        clickItemRecyclerView(R.id.rv_last_tagged_product, position)
        verifyItemRecyclerView(R.id.rv_last_tagged_product, position) {
            it.itemView.findViewById<CheckboxUnify>(R.id.checkbox_product).isChecked.not()
        }
        delay(DEFAULT_DELAY)

        verifyButtonState(R.id.btn_save, false)

        delay(DEFAULT_DELAY)
    }

    /** isFullPageAutocomplete Test */
    @Test
    fun contentProductTag_isFullPageAutocomplete_true() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(true, "")
        )

        click(R.id.cl_search)

        isVisible(
            autocompleteR.id.autocompleteSearchBar
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isFullPageAutocomplete_false() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        click(R.id.cl_search)

        isVisible(
            R.id.ic_back,
            R.id.sb_autocomplete,
        )

        delay(DEFAULT_DELAY)
    }

    /** BackButton Test */
    @Test
    fun contentProductTag_backButton_back() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setBackButton(ContentProductTagConfig.BackButton.Back)
        )

        verify<IconUnify>(R.id.ic_cc_product_tag_back) {
            it.iconImg == AppCompatResources.getDrawable(targetContext, iconR.iconunify_arrow_back)
        }
    }

    @Test
    fun contentProductTag_backButton_close() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setBackButton(ContentProductTagConfig.BackButton.Close)
        )

        verify<IconUnify>(R.id.ic_cc_product_tag_back) {
            it.iconImg == AppCompatResources.getDrawable(targetContext, iconR.iconunify_close)
        }
    }

    /** isShowActionBarDivider Test */
    @Test
    fun contentProductTag_isShowActionBarDivider_true_showDivider() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsShowActionBarDivider(true)
        )

        isVisible(R.id.view_cc_product_tag_divider)

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isShowActionBarDivider_false_hideDivider() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsShowActionBarDivider(false)
        )

        isHidden(R.id.view_cc_product_tag_divider)

        delay(DEFAULT_DELAY)
    }
}