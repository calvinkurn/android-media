package com.tokopedia.content.common.producttag.testcase.config

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.R
import com.tokopedia.content.common.const.DEFAULT_DELAY
import com.tokopedia.content.common.producttag.builder.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.builder.LastTaggedModelBuilder
import com.tokopedia.content.common.producttag.builder.ProductTagSourceBuilder
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity.Companion.EXTRA_ARGUMENT
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.helper.*
import com.tokopedia.content.common.producttag.helper.sourceMyAccountLabel
import com.tokopedia.content.common.producttag.helper.sourceMyShop
import com.tokopedia.content.common.producttag.helper.sourceTokopedia
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.reflection.getPrivateField
import com.tokopedia.content.test.util.*
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
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
    private val productTagSourceBuilder = ProductTagSourceBuilder()
    private val lastTaggedModelBuilder = LastTaggedModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()

    /** Helper */
    private val daggerHelper = ContentProductTagDaggerHelper(targetContext)

    /** Model */
    private val completeSource = productTagSourceBuilder.buildSourceList()
    private val lastTaggedProduct = lastTaggedModelBuilder.buildPagedDataModel()
    private val aceProduct = globalSearchModelBuilder.buildResponseModel()
    private val maxSelectedProduct = 3

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

    /** Table of Test (search this keyword below to navigate directly to the section)
     * 1. authorTypeTest
     * 2. productTagSourceTest
     * 3. isMultipleSelectionProductTest
     * 4. isFullPageAutocompleteTest
     * 5. backButtonTest
     * 6. isShowActionBarDividerTest
     */

    /** authorTypeTest */
    @Test
    fun contentProductTag_ugc_breadcrumbVisible() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
        )

        isVisible(breadcrumb)

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_seller_breadcrumbHidden() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_SHOP)
        )

        isHidden(breadcrumb)

        delay(DEFAULT_DELAY)
    }

    /** productTagSourceTest */
    @Test
    fun contentProductTag_productTagSource_completeSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
        )

        click(breadcrumb)

        isVisible(
            sourceTokopedia,

            sourceMyAccountLabel,
            sourceLastPurchased,

            sourceMyShopLabel,
            sourceMyShop,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_productTagSource_noMyShopSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                productTagSourceBuilder.buildSourceList {
                    listOf(ProductTagSource.MyShop)
                }
            )
        )

        click(breadcrumb)

        isVisible(
            sourceTokopedia,

            sourceMyAccountLabel,
            sourceLastPurchased,
        )

        isHidden(
            sourceMyShopLabel,
            sourceMyShop,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_productTagSource_noLastPurchasedSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                productTagSourceBuilder.buildSourceList {
                    listOf(ProductTagSource.LastPurchase)
                }
            )
        )

        click(breadcrumb)

        isVisible(
            sourceTokopedia,

            sourceMyShopLabel,
            sourceMyShop,
        )

        isHidden(
            sourceMyAccountLabel,
            sourceLastPurchased,
        )

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_productTagSource_onlyGlobalSearch() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(
                productTagSourceBuilder.buildSourceList {
                    listOf(ProductTagSource.LastPurchase, ProductTagSource.MyShop)
                }
            )
        )

        click(breadcrumb)

        isVisible(
            sourceTokopedia,
        )

        isHidden(
            sourceMyAccountLabel,
            sourceLastPurchased,

            sourceMyShopLabel,
            sourceMyShop,
        )

        delay(DEFAULT_DELAY)
    }

    /** isMultipleSelectionProductTest */
    @Test
    fun contentProductTag_isMultipleSelectionProduct_false_shouldFinishPicker() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(false, 0)
        )

        val position = 1
        val selectedProduct = listOf(lastTaggedProduct.dataList[position].toSelectedProduct())

        isHidden(saveButton)

        verifyText(pageTitle, targetContext.getString(R.string.content_creation_product_tag_title))

        clickItemRecyclerView(lastTaggedRv, 1)


        verifyText(testSelectedProductText, selectedProduct.toString())

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_titleHasCounter_checkBoxVisible_submitButtonVisible() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
        )

        verifyText(
            pageTitle,
            targetContext.getString(R.string.content_creation_multiple_product_tag_title)
                .format(0, maxSelectedProduct)
        )

        verifyItemRecyclerView(lastTaggedRv, 0) {
            it.itemView.findViewById<CheckboxUnify>(checkboxProduct).isVisible
        }

        isVisible(saveButton)

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_ableToPickUntilMaxProduct() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
        )

        val positionList = listOf(0, 1, 2)
        val selectedProducts = positionList.map { position ->
            SelectedProductUiModel.createOnlyId(lastTaggedProduct.dataList[position].id)
        }

        positionList.forEachIndexed { idx, position ->
            clickItemRecyclerView(lastTaggedRv, position)
            verifyText(
                pageTitle,
                targetContext.getString(R.string.content_creation_multiple_product_tag_title)
                    .format(idx+1, maxSelectedProduct)
            )
            delay(DEFAULT_DELAY)
        }

        click(saveButton)

        verifyText(testSelectedProductText, selectedProducts.toString())

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_cannotTickMoreThanMaxProduct() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
        )

        val positionList = listOf(0, 1, 2, 3)
        val selectedProducts = positionList.take(maxSelectedProduct).map { position ->
            SelectedProductUiModel.createOnlyId(lastTaggedProduct.dataList[position].id)
        }

        positionList.forEach { position ->
            clickItemRecyclerView(lastTaggedRv, position)
            delay(DEFAULT_DELAY)
        }
        verifyText(
            pageTitle,
            targetContext.getString(R.string.content_creation_multiple_product_tag_title)
                .format(maxSelectedProduct, maxSelectedProduct)
        )

        click(saveButton)

        verifyText(testSelectedProductText, selectedProducts.toString())

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_doubleClickOnProduct_checkedFalse() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
        )

        val position = 0
        clickItemRecyclerView(lastTaggedRv, position)
        verifyItemRecyclerView(lastTaggedRv, position) {
            it.itemView.findViewById<CheckboxUnify>(checkboxProduct).isChecked
        }
        verifyText(
            pageTitle,
            targetContext.getString(R.string.content_creation_multiple_product_tag_title)
                .format(1, maxSelectedProduct)
        )
        delay(DEFAULT_DELAY)

        clickItemRecyclerView(lastTaggedRv, position)
        verifyItemRecyclerView(lastTaggedRv, position) {
            it.itemView.findViewById<CheckboxUnify>(checkboxProduct).isChecked.not()
        }
        verifyText(
            pageTitle,
            targetContext.getString(R.string.content_creation_multiple_product_tag_title)
                .format(0, maxSelectedProduct)
        )
        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isMultipleSelectionProduct_true_noProductSelected_submitButtonDisabled() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(false)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
        )

        val position = 0

        verifyButtonState(saveButton, false)

        clickItemRecyclerView(lastTaggedRv, position)
        verifyItemRecyclerView(lastTaggedRv, position) {
            it.itemView.findViewById<CheckboxUnify>(checkboxProduct).isChecked
        }
        delay(DEFAULT_DELAY)

        verifyButtonState(saveButton, true)

        clickItemRecyclerView(lastTaggedRv, position)
        verifyItemRecyclerView(lastTaggedRv, position) {
            it.itemView.findViewById<CheckboxUnify>(checkboxProduct).isChecked.not()
        }
        delay(DEFAULT_DELAY)

        verifyButtonState(saveButton, false)

        delay(DEFAULT_DELAY)
    }

    /** isFullPageAutocompleteTest */
    @Test
    fun contentProductTag_isFullPageAutocomplete_true() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(true, "")
        )

        openAutocomplete()

        isVisible(autocompleteSearchBar)

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isFullPageAutocomplete_false() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openAutocomplete()

        delay(DEFAULT_DELAY)

        isVisible(
            fakeAutocompleteBackButton,
            fakeAutocompleteSearchBar,
        )

        delay(DEFAULT_DELAY)
    }

    /** backButtonTest */
    @Test
    fun contentProductTag_backButton_back() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setBackButton(ContentProductTagConfig.BackButton.Back)
        )

        verify<IconUnify>(backButton) {
            val iconId = it.getPrivateField<Int>("iconId")
            iconId == IconUnify.ARROW_BACK
        }
    }

    @Test
    fun contentProductTag_backButton_close() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setBackButton(ContentProductTagConfig.BackButton.Close)
        )

        verify<IconUnify>(backButton) {
            val iconId = it.getPrivateField<Int>("iconId")
            iconId == IconUnify.CLOSE
        }
    }

    /** isShowActionBarDividerTest */
    @Test
    fun contentProductTag_isShowActionBarDivider_true_showDivider() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsShowActionBarDivider(true)
        )

        isVisible(divider)

        delay(DEFAULT_DELAY)
    }

    @Test
    fun contentProductTag_isShowActionBarDivider_false_hideDivider() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsShowActionBarDivider(false)
        )

        isHidden(divider)

        delay(DEFAULT_DELAY)
    }
}