package com.tokopedia.product.manage.cassava.testcase

import com.tokopedia.product.manage.cassava.robot.actionTest
import org.junit.Test

class ProductManageTrackerValidationTest : ProductManageTrackerValidationTestFixture() {

    companion object {
        private const val CLICK_EDIT_PRICE_BUTTON_PATH =
            "tracker/merchant/product_manage_list/list/pml_click_edit_price_button.json"
        private const val CLICK_EDIT_STOCK_BUTTON_PATH =
            "tracker/merchant/product_manage_list/list/pml_click_edit_stock_button.json"
        private const val CLICK_ON_PRODUCT_PATH =
            "tracker/merchant/product_manage_list/list/pml_click_on_product.json"
        private const val CLICK_TAB_FILTER_PATH =
            "tracker/merchant/product_manage_list/list/pml_click_tab_filter.json"
    }

    @Test
    fun validateEditStockButtonClicked() {
        actionTest {
            clickEditStockButton(activityRule.activity)
            validate(CLICK_EDIT_STOCK_BUTTON_PATH)
        }
    }

    @Test
    fun validateEditPriceButtonClicked() {
        actionTest {
            clickEditPriceButton(activityRule.activity)
            validate(CLICK_EDIT_PRICE_BUTTON_PATH)
        }
    }

    @Test
    fun validateProductClicked() {
        actionTest {
            blockAllIntent()
            clickProduct()
            validate(CLICK_ON_PRODUCT_PATH)
        }
    }

    @Test
    fun validateFilterTabClicked() {
        actionTest {
            clickFilterTab()
            validate(CLICK_TAB_FILTER_PATH)
        }
    }

}