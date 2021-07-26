package com.tokopedia.tokopedianow.searchcategory

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.ProductLabelGroup
import com.tokopedia.tokopedianow.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.OutOfCoverageDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.utils.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.tokopedianow.searchcategory.utils.RECOM_WIDGET
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CLP
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
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
    assertThat(this, instanceOf(OutOfCoverageDataView::class.java))
}

fun Visitable<*>.assertChooseAddressDataView() {
    assertThat(this, instanceOf(ChooseAddressDataView::class.java))
}

fun Visitable<*>.assertBannerDataView() {
    assertThat(this, instanceOf(BannerDataView::class.java))
}

fun Visitable<*>.assertTitleDataView(title: String, hasSeeAllCategoryButton: Boolean) {
    assertThat(this, instanceOf(TitleDataView::class.java))

    val titleDataView = this as TitleDataView
    assertThat(titleDataView.title, shouldBe(title))
    assertThat(titleDataView.hasSeeAllCategoryButton, shouldBe(hasSeeAllCategoryButton))
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

fun verifyProductItemDataViewList(
        expectedProductList: List<Product>,
        actualProductItemDataViewList: List<ProductItemDataView>,
        startPosition: Int,
) {
    assertThat(actualProductItemDataViewList.size, shouldBe(expectedProductList.size))

    expectedProductList.forEachIndexed { productIndex, expectedProduct ->
        val actualProductDataView = actualProductItemDataViewList[productIndex]
        val expectedPosition = productIndex + startPosition

        assertThat(actualProductDataView.id, shouldBe(expectedProduct.id))
        assertThat(actualProductDataView.imageUrl300, shouldBe(expectedProduct.imageUrl300))
        assertThat(actualProductDataView.name, shouldBe(expectedProduct.name))
        assertThat(actualProductDataView.price, shouldBe(expectedProduct.price))
        assertThat(actualProductDataView.priceInt, shouldBe(expectedProduct.priceInt))
        assertThat(actualProductDataView.discountPercentage, shouldBe(expectedProduct.discountPercentage))
        assertThat(actualProductDataView.originalPrice, shouldBe(expectedProduct.originalPrice))
        assertThat(actualProductDataView.parentId, shouldBe(expectedProduct.parentId))
        assertThat(actualProductDataView.shop.id, shouldBe(expectedProduct.shop.id))
        assertThat(actualProductDataView.shop.name, shouldBe(expectedProduct.shop.name))
        assertThat(actualProductDataView.ratingAverage, shouldBe(expectedProduct.ratingAverage))
        assertThat(actualProductDataView.sourceEngine, shouldBe(expectedProduct.sourceEngine))
        assertThat(actualProductDataView.boosterList, shouldBe(expectedProduct.boosterList))
        assertThat(actualProductDataView.position, shouldBe(expectedPosition))
        assertATCConfiguration(actualProductDataView, expectedProduct)
        assertLabelGroupDataView(
                actualProductDataView.labelGroupDataViewList,
                expectedProduct.labelGroupList
        )
        assertLabelGroupVariantDataView(
                actualProductDataView.labelGroupVariantDataViewList,
                expectedProduct.labelGroupVariantList
        )
    }
}

private fun assertLabelGroupDataView(
        labelGroupDataViewList: List<LabelGroupDataView>,
        labelGroupList: List<ProductLabelGroup>
) {
    assertThat(labelGroupDataViewList.size, shouldBe(labelGroupList.size))

    labelGroupList.forEachIndexed { labelGroupIndex, productLabelGroup ->
        val actualLabelGroupDataView = labelGroupDataViewList[labelGroupIndex]

        assertThat(actualLabelGroupDataView.url, shouldBe(productLabelGroup.url))
        assertThat(actualLabelGroupDataView.title, shouldBe(productLabelGroup.title))
        assertThat(actualLabelGroupDataView.position, shouldBe(productLabelGroup.position))
        assertThat(actualLabelGroupDataView.type, shouldBe(productLabelGroup.type))
    }
}

private fun assertLabelGroupVariantDataView(
        labelGroupVariantDataViewList: List<LabelGroupVariantDataView>,
        labelGroupVariantList: List<AceSearchProductModel.ProductLabelGroupVariant>
) {
    assertThat(labelGroupVariantDataViewList.size, shouldBe(labelGroupVariantList.size))

    labelGroupVariantList.forEachIndexed { labelGroupVariantIndex, labelGroupVariant ->
        val actualLabelGroupVariantDataView = labelGroupVariantDataViewList[labelGroupVariantIndex]

        assertThat(actualLabelGroupVariantDataView.title, shouldBe(labelGroupVariant.title))
        assertThat(actualLabelGroupVariantDataView.type, shouldBe(labelGroupVariant.type))
        assertThat(actualLabelGroupVariantDataView.typeVariant, shouldBe(labelGroupVariant.typeVariant))
        assertThat(actualLabelGroupVariantDataView.hexColor, shouldBe(labelGroupVariant.hexColor))
    }
}

private fun assertATCConfiguration(
        actualProductDataView: ProductItemDataView,
        expectedProduct: Product,
) {

    val hasVariantATC = actualProductDataView.variantATC != null
    val expectedHasVariantATC = expectedProduct.childs.isNotEmpty()
    val variantATCReason = "Variant ATC is null should be $expectedHasVariantATC"
    assertThat(variantATCReason, hasVariantATC, shouldBe(expectedHasVariantATC))

    val hasNonVariantATC = actualProductDataView.nonVariantATC != null
    val expectedHasNonVariantATC = expectedProduct.childs.isEmpty()
    val nonVariantATCReason = "Non Variant ATC is null should be $expectedHasNonVariantATC"
    assertThat(nonVariantATCReason, hasNonVariantATC, shouldBe(expectedHasNonVariantATC))

    if (expectedHasNonVariantATC) {
        assertThat(actualProductDataView.nonVariantATC?.minQuantity, shouldBe(expectedProduct.minOrder))
        assertThat(actualProductDataView.nonVariantATC?.maxQuantity, shouldBe(expectedProduct.stock))
    }
}

fun <T> Visitable<T>.assertRecommendationCarouselDataViewLoadingState() {
    assertThat(this, instanceOf(RecommendationCarouselDataView::class.java))

    val recomWidget = this as RecommendationCarouselDataView
    assertThat(recomWidget.carouselData.state, shouldBe(RecommendationCarouselData.STATE_LOADING))
}

fun assertTokonowRecommendationCarouselRequestParams(
        getRecommendationRequestParam: GetRecommendationRequestParam
) {
    assertThat(getRecommendationRequestParam.xSource, shouldBe(RECOM_WIDGET))
    assertThat(getRecommendationRequestParam.pageName, shouldBe(TOKONOW_CLP))
    assertThat(getRecommendationRequestParam.isTokonow, shouldBe(true))
    assertThat(getRecommendationRequestParam.pageNumber, shouldBe(PAGE_NUMBER_RECOM_WIDGET))
    assertThat(getRecommendationRequestParam.xDevice, shouldBe(DEFAULT_VALUE_OF_PARAMETER_DEVICE))
}