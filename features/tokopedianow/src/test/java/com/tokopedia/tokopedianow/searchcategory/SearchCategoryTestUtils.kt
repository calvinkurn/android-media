package com.tokopedia.tokopedianow.searchcategory

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.common.util.ProductAdsMapper
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.ProductLabelGroup
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import java.io.File
import org.hamcrest.CoreMatchers.`is` as shouldBe

inline fun <reified T> String.jsonToObject(): T {
    val systemClassLoader = ClassLoader.getSystemClassLoader()
    val urlResource = systemClassLoader.getResource(this)
    val urlResourcePath = urlResource.path
    val fileFromUrlResourcePath = File(urlResourcePath)
    val jsonString = String(fileFromUrlResourcePath.readBytes())

    return Gson().fromJson(jsonString, T::class.java)
}

fun Visitable<*>.assertOutOfCoverageDataView() {
    assertThat(this, instanceOf(TokoNowEmptyStateOocUiModel::class.java))

    val tokoNowEmptyStateOocUiModel = this as TokoNowEmptyStateOocUiModel
    assertThat(tokoNowEmptyStateOocUiModel.hostSource, shouldBe(SearchApiConst.DEFAULT_VALUE_SOURCE_SEARCH))
}

fun Visitable<*>.assertChooseAddressDataView() {
    assertThat(this, instanceOf(ChooseAddressDataView::class.java))
}

fun Visitable<*>.assertBannerDataView() {
    assertThat(this, instanceOf(BannerDataView::class.java))
}

fun Visitable<*>.assertCategoryFilterDataView(categoryFilterDataValue: DataValue) {
    assertThat(this, instanceOf(CategoryFilterDataView::class.java))

    val categoryFilterDataView = this as CategoryFilterDataView
    val categoryFilterItemList = categoryFilterDataView.categoryFilterItemList
    val expectedCategoryFilter = categoryFilterDataValue.filter[0].options
    assertThat(categoryFilterItemList.size, shouldBe(expectedCategoryFilter.size))

    expectedCategoryFilter.forEachIndexed { index, categoryOption ->
        val categoryFilterItemDataView = categoryFilterItemList[index]
        val expectedCategoryOption = OptionHelper.copyOptionAsExclude(categoryOption)

        assertThat(categoryFilterItemDataView.option.key, shouldBe(expectedCategoryOption.key))
        assertThat(categoryFilterItemDataView.option.value, shouldBe(expectedCategoryOption.value))
        assertThat(categoryFilterItemDataView.option.name, shouldBe(expectedCategoryOption.name))
    }
}

fun Visitable<*>.assertQuickFilterDataView(
        quickFilterDataValue: DataValue,
        mapParameter: Map<String, String>,
) {
    assertThat(this, instanceOf(QuickFilterDataView::class.java))

    val quickFilterDataView = this as QuickFilterDataView

    val quickFilterItemList = quickFilterDataView.quickFilterItemList
    val expectedQuickFilter = quickFilterDataValue.filter
    assertThat(quickFilterItemList.size, shouldBe(expectedQuickFilter.size))

    assertThat(quickFilterDataView.mapParameter, shouldBe(mapParameter))

    expectedQuickFilter.forEachIndexed { index, quickFilter ->
        val sortFilterItemDataView = quickFilterItemList[index]

        assertThat(sortFilterItemDataView.sortFilterItem.title.toString(), shouldBe(quickFilter.title))
        assertThat(sortFilterItemDataView.sortFilterItem.typeUpdated, shouldBe(false))

        val chevronListenerMatcher = if (quickFilter.options.size > 1) notNullValue() else nullValue()
        assertThat(sortFilterItemDataView.sortFilterItem.chevronListener, chevronListenerMatcher)
    }
}

fun Visitable<*>.assertProductCountDataView(productCountText: String) {
    assertThat(this, instanceOf(ProductCountDataView::class.java))

    val productCountDataView = this as ProductCountDataView
    assertThat(productCountDataView.totalDataText, shouldBe(productCountText))
}

fun Visitable<*>.assertProductAdsCarousel(response: ProductAdsResponse) {
    val expected = ProductAdsMapper.mapProductAdsCarousel(response)
    assertEquals(expected, this)
}

fun verifyProductItemDataViewList(
        expectedProductList: List<Product>,
        actualProductItemDataViewList: List<ProductItemDataView>,
        startPosition: Int,
        needToVerifyAtc: Boolean = true
) {
    assertThat(actualProductItemDataViewList.size, shouldBe(expectedProductList.size))

    expectedProductList.forEachIndexed { productIndex, expectedProduct ->
        val actualProductDataView = actualProductItemDataViewList[productIndex]
        val expectedPosition = productIndex + startPosition

        assertThat(actualProductDataView.productCardModel.productId, shouldBe(expectedProduct.id))
        assertThat(actualProductDataView.productCardModel.imageUrl, shouldBe(expectedProduct.imageUrl300))
        assertThat(actualProductDataView.productCardModel.name, shouldBe(expectedProduct.name))
        assertThat(actualProductDataView.productCardModel.price, shouldBe(expectedProduct.price))
        assertThat(actualProductDataView.productCardModel.slashPrice, shouldBe(expectedProduct.originalPrice))
        assertThat(actualProductDataView.parentId, shouldBe(expectedProduct.parentId))
        assertThat(actualProductDataView.shop.id, shouldBe(expectedProduct.shop.id))
        assertThat(actualProductDataView.shop.name, shouldBe(expectedProduct.shop.name))
        assertThat(actualProductDataView.productCardModel.rating, shouldBe(expectedProduct.ratingAverage))
        assertThat(actualProductDataView.sourceEngine, shouldBe(expectedProduct.sourceEngine))
        assertThat(actualProductDataView.boosterList, shouldBe(expectedProduct.boosterList))
        assertThat(actualProductDataView.position, shouldBe(expectedPosition))
        if (needToVerifyAtc) assertATCConfiguration(actualProductDataView, expectedProduct)
        assertLabelGroupDataView(
                actualProductDataView.productCardModel.labelGroupList,
                expectedProduct.labelGroupList
        )
    }
}

private fun assertLabelGroupDataView(
    labelGroupDataViewList: List<ProductCardCompactUiModel.LabelGroup>,
    labelGroupList: List<ProductLabelGroup>
) {
    assertThat(labelGroupDataViewList.size, shouldBe(labelGroupList.size))

    labelGroupList.forEachIndexed { labelGroupIndex, productLabelGroup ->
        val actualLabelGroupDataView = labelGroupDataViewList[labelGroupIndex]

        assertThat(actualLabelGroupDataView.title, shouldBe(productLabelGroup.title))
        assertThat(actualLabelGroupDataView.position, shouldBe(productLabelGroup.position))
        assertThat(actualLabelGroupDataView.type, shouldBe(productLabelGroup.type))
    }
}

private fun assertATCConfiguration(
        actualProductDataView: ProductItemDataView,
        expectedProduct: Product,
) {
    val hasVariantATC = actualProductDataView.parentId != "0" && actualProductDataView.parentId != ""
    assertThat(actualProductDataView.parentId, shouldBe(expectedProduct.parentId))
    if (!hasVariantATC) {
        assertThat(actualProductDataView.productCardModel.minOrder, shouldBe(expectedProduct.minOrder))
        assertThat(actualProductDataView.productCardModel.maxOrder, shouldBe(expectedProduct.maxOrder))
    }
}

fun Visitable<*>.assertProductFeedbackWidget(){
    assertThat(this, instanceOf(TokoNowFeedbackWidgetUiModel::class.java))
}

fun List<Visitable<*>>.assertNotProductFeedbackWidget(){
   val tempList = this.filterIsInstance<TokoNowFeedbackWidgetUiModel>()
    Assert.assertEquals(tempList.size,0)
}

fun assertNoProductFeedbackWidget(list:List<Visitable<*>>){
    var found = false
    list.forEach {
        if(it is TokoNowFeedbackWidgetUiModel)
           found = true
    }
    Assert.assertEquals(found,false)
}
