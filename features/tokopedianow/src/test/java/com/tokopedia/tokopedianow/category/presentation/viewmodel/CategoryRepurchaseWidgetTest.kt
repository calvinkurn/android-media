package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.utils.REPURCHASE_WIDGET_POSITION
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Test

class CategoryRepurchaseWidgetTest: CategoryTestFixtures() {

    @Test
    fun `show repurchase widget after 4th product`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget.json"
            .jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        `Then assert repurchase widget in visitable list`(visitableList)
    }

    private fun `Then assert repurchase widget in visitable list`(
        visitableList: List<Visitable<*>>,
    ) {
        val indexOfFirstProductCard = visitableList.indexOfFirst { it is ProductItemDataView }
        val repurchaseWidgetIndex = indexOfFirstProductCard + REPURCHASE_WIDGET_POSITION

        assertThat(
            visitableList[repurchaseWidgetIndex],
            instanceOf(TokoNowRecentPurchaseUiModel::class.java)
        )
    }

    @Test
    fun `do not show repurchase if less than 4 products`() {
        val categoryModel = "category/repurchasewidget/repurchase-widget-less-than-4-products.json"
            .jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        `Then assert no repurchase widget in visitable list`(visitableList)
    }

    private fun `Then assert no repurchase widget in visitable list`(
        visitableList: List<Visitable<*>>,
    ) {
        assertThat(
            visitableList,
            everyItem(not(instanceOf(TokoNowRecentPurchaseUiModel::class.java)))
        )
    }
}