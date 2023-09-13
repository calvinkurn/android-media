package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.UpdateCartTestHelper
import com.tokopedia.tokopedianow.searchcategory.UpdateCartTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.verify
import org.junit.Test

class CategoryUpdateCartTest: CategoryTestFixtures(), Callback {

    private lateinit var updateCartTestHelper: UpdateCartTestHelper

    override fun setUp() {
        super.setUp()

        updateCartTestHelper = UpdateCartTestHelper(
                tokoNowCategoryViewModel,
                getMiniCartListSimplifiedUseCase,
                this,
        )
    }

    override fun `Given first page API will be successful`() {
        val categoryModel =
                "oldcategory/first-page-products-variant-and-non-variant.json".jsonToObject<CategoryModel>()

        `Given get category first page use case will be successful`(categoryModel)
    }

    @Test
    fun `onViewResumed should update mini cart and quantity in product list`() {
        updateCartTestHelper.`onViewResumed should update mini cart and quantity in product list`()
    }

    @Test
    fun `onViewResumed should not update mini cart if shop id empty or zero`() {
        `Given choose address data`(ChooseAddressConstant.emptyAddress)
        `Given category view model`()

        `When view resumed`()

        `Then assert get mini cart list is not called`()
    }

    private fun `When view resumed`() {
        tokoNowCategoryViewModel.onViewResumed()
    }

    private fun `Then assert get mini cart list is not called`() {
        verify(exactly = 0) {
            getMiniCartListSimplifiedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `onViewUpdateCartItems should update quantity in product list`() {
        updateCartTestHelper.`onViewUpdateCartItems should update quantity in product list`()
    }

    @Test
    fun `update mini cart fail should hide mini cart`() {
        updateCartTestHelper.`update mini cart fail should hide mini cart`()
    }

    @Test
    fun `onViewReloadPage should have product with quantity from mini cart`() {
        updateCartTestHelper.`onViewReloadPage should have product with quantity from mini cart`()
    }

    @Test
    fun `when choose address null should NOT update miniCart live data`() {
        `Given address data null`()

        tokoNowCategoryViewModel.onViewUpdateCartItems(MiniCartSimplifiedData())

        tokoNowCategoryViewModel.miniCartWidgetLiveData
            .verifyValueEquals(null)

        tokoNowCategoryViewModel.isShowMiniCartLiveData
            .verifyValueEquals(null)
    }

    @Test
    fun `when warehouseId 0 should update isShowMiniCartLiveData false`() {
        val miniCartSimplifiedData = MiniCartSimplifiedData(isShowMiniCartWidget = true)

        `Given choose address data`(LocalCacheModel(warehouse_id = "0"))

        tokoNowCategoryViewModel.onViewReloadPage()
        tokoNowCategoryViewModel.onViewUpdateCartItems(miniCartSimplifiedData)

        tokoNowCategoryViewModel.isShowMiniCartLiveData
            .verifyValueEquals(false)
    }

    @Test
    fun `when warehouseId NOT equals 0 should update isShowMiniCartLiveData true`() {
        val miniCartSimplifiedData = MiniCartSimplifiedData(isShowMiniCartWidget = true)

        `Given choose address data`(LocalCacheModel(warehouse_id = "1"))

        tokoNowCategoryViewModel.onViewReloadPage()
        tokoNowCategoryViewModel.onViewUpdateCartItems(miniCartSimplifiedData)

        tokoNowCategoryViewModel.isShowMiniCartLiveData
            .verifyValueEquals(true)
    }

    @Test
    fun `when isShowMiniCartWidget false should update isShowMiniCartLiveData false`() {
        val miniCartSimplifiedData = MiniCartSimplifiedData(isShowMiniCartWidget = false)

        `Given choose address data`(LocalCacheModel(warehouse_id = "1"))

        tokoNowCategoryViewModel.onViewReloadPage()
        tokoNowCategoryViewModel.onViewUpdateCartItems(miniCartSimplifiedData)

        tokoNowCategoryViewModel.isShowMiniCartLiveData
            .verifyValueEquals(false)
    }
}
