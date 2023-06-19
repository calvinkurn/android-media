package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.UrlParamUtils.keywords
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Product.LabelGroup
import com.tokopedia.search.result.mps.shopwidget.MPSButtonDataView
import com.tokopedia.search.result.mps.shopwidget.MPSProductLabelGroupDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import com.tokopedia.search.shouldBe
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Test

private const val MPSLoadMoreDuplicateJSON = "mps/mps-duplicate-shop.json"
internal class MultiProductSearchShopWidgetTest: MultiProductSearchTestFixtures() {

    private val parameter = mapOf(
        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
        SearchApiConst.Q1 to "samsung",
        SearchApiConst.Q2 to "xiaomi",
        SearchApiConst.Q3 to "iphone",
    )
    private val mpsViewModel = mpsViewModel(
        MPSState(
            parameter
        ),
    )

    @Test
    fun `multi product search success will show shop widget list`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS Use Case success`(mpsModel)

        `When view created`()

        `Then assert shop widget visitable list first page`(mpsViewModel.visitableList, mpsModel)
    }

    private fun `When view created`() {
        mpsViewModel.onViewCreated()
    }

    private fun `Then assert shop widget visitable list first page`(
        mpsVisitableList: List<Visitable<*>>,
        mpsModel: MPSModel,
    ) {
        assertMPSShopWidget(
            mpsVisitableList.filterIsInstance<MPSShopWidgetDataView>(),
            mpsModel.shopList
        )
    }

    private fun assertMPSShopWidget(
        mpsShopWidgetDataViewList: List<MPSShopWidgetDataView>,
        shopList: List<Shop>
    ) {
        assertThat(mpsShopWidgetDataViewList.size, `is`(shopList.size))

        mpsShopWidgetDataViewList.forEachIndexed { index, mpsShopWidgetDataView ->
            val shopItemModel = shopList[index]
            assertMPSShopWidgetDataView(mpsShopWidgetDataView, shopItemModel)
        }
    }

    private fun assertMPSShopWidgetDataView(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        shopItemModel: Shop,
    ) {
        assertThat(mpsShopWidgetDataView.id, `is`(shopItemModel.id))
        assertThat(mpsShopWidgetDataView.name, `is`(shopItemModel.name))
        assertThat(mpsShopWidgetDataView.imageUrl, `is`(shopItemModel.imageUrl))
        assertThat(mpsShopWidgetDataView.location, `is`(shopItemModel.location))
        assertThat(mpsShopWidgetDataView.applink, `is`(shopItemModel.applink))
        assertThat(mpsShopWidgetDataView.componentId, `is`(shopItemModel.componentId))
        assertThat(mpsShopWidgetDataView.trackingOption, `is`(shopItemModel.trackingOption))
        assertThat(mpsShopWidgetDataView.keywords, `is`(parameter.keywords()))

        assertThat(mpsShopWidgetDataView.ticker.type, `is`(shopItemModel.ticker.type))
        assertThat(mpsShopWidgetDataView.ticker.message, `is`(shopItemModel.ticker.message))

        assertThat(
            mpsShopWidgetDataView.shopFreeOngkir.imageUrl,
            `is`(shopItemModel.freeOngkir.imageUrl)
        )
        assertThat(
            mpsShopWidgetDataView.shopFreeOngkir.isActive,
            `is`(shopItemModel.freeOngkir.isActive)
        )

        assertThat(mpsShopWidgetDataView.badgeList.size, `is`(shopItemModel.badgeList.size))
        mpsShopWidgetDataView.badgeList.forEachIndexed { index, mpsShopBadgeDataView ->
            assertThat(mpsShopBadgeDataView.imageUrl, `is`(shopItemModel.badgeList[index].imageUrl))
            assertThat(mpsShopBadgeDataView.isShow, `is`(shopItemModel.badgeList[index].isShow))
        }

        assertButtonDataViewList(
            mpsShopWidgetDataView.buttonList,
            shopItemModel.buttonList,
            shopItemModel.id,
            shopItemModel.name,
        )

        assertThat(mpsShopWidgetDataView.productList.size, `is`(shopItemModel.productCardList.size))
        mpsShopWidgetDataView.productList.forEachIndexed { index, mpsShopWidgetProductDataView ->
            assertMPSShopWidgetProductDataView(
                mpsShopWidgetProductDataView,
                shopItemModel,
                shopItemModel.productCardList[index],
                index + 1,
            )
        }

        shopItemModel.viewAllCard?.let { viewAllCard ->
            assertEquals(viewAllCard.text, mpsShopWidgetDataView.viewAllCard.text)
            assertEquals(viewAllCard.applink, mpsShopWidgetDataView.viewAllCard.applink)
            assertEquals(viewAllCard.componentId, mpsShopWidgetDataView.viewAllCard.componentId)
            assertEquals(viewAllCard.trackingOption, mpsShopWidgetDataView.viewAllCard.trackingOption)
            assertEquals(parameter.keywords(), mpsShopWidgetDataView.viewAllCard.keywords)
        }
    }

    private fun assertButtonDataViewList(
        buttonDataViewList: List<MPSButtonDataView>,
        buttonList: List<Shop.Button>,
        componentValueId: String,
        componentValueName: String,
    ) {
        assertThat(buttonDataViewList.size, `is`(buttonList.size))
        buttonDataViewList.forEachIndexed { index, mpsButtonDataView ->
            assertThat(mpsButtonDataView.name, `is`(buttonList[index].name))
            assertThat(mpsButtonDataView.text, `is`(buttonList[index].text))
            assertThat(mpsButtonDataView.applink, `is`(buttonList[index].applink))
            assertThat(mpsButtonDataView.componentId, `is`(buttonList[index].componentId))
            assertThat(mpsButtonDataView.trackingOption, `is`(buttonList[index].trackingOption))
            assertThat(mpsButtonDataView.keywords, `is`(parameter.keywords()))
            assertThat(mpsButtonDataView.componentValueId, `is`(componentValueId))
            assertThat(mpsButtonDataView.componentValueName, `is`(componentValueName))
        }
    }

    private fun assertMPSShopWidgetProductDataView(
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView,
        shopItem: Shop,
        shopItemProduct: Shop.Product,
        position: Int,
    ) {
        assertThat(mpsShopWidgetProductDataView.id, `is`(shopItemProduct.id))
        assertThat(mpsShopWidgetProductDataView.imageUrl, `is`(shopItemProduct.imageUrl))
        assertThat(mpsShopWidgetProductDataView.name, `is`(shopItemProduct.name))
        assertThat(mpsShopWidgetProductDataView.applink, `is`(shopItemProduct.applink))
        assertThat(mpsShopWidgetProductDataView.price, `is`(shopItemProduct.price))
        assertThat(mpsShopWidgetProductDataView.priceFormat, `is`(shopItemProduct.priceFormat))
        assertThat(mpsShopWidgetProductDataView.originalPrice, `is`(shopItemProduct.originalPrice))
        assertThat(mpsShopWidgetProductDataView.discountPercentage, `is`(shopItemProduct.discountPercentage))
        assertThat(mpsShopWidgetProductDataView.ratingAverage, `is`(shopItemProduct.ratingAverage))
        assertThat(mpsShopWidgetProductDataView.parentId, `is`(shopItemProduct.parentId))
        assertThat(mpsShopWidgetProductDataView.stock, `is`(shopItemProduct.stock))
        assertThat(mpsShopWidgetProductDataView.minOrder, `is`(shopItemProduct.minOrder))
        assertThat(mpsShopWidgetProductDataView.componentId, `is`(shopItemProduct.componentId))
        assertThat(mpsShopWidgetProductDataView.trackingOption, `is`(shopItemProduct.trackingOption))
        assertThat(mpsShopWidgetProductDataView.position, `is`(position))

        assertButtonDataViewList(
            mpsShopWidgetProductDataView.buttonList,
            shopItemProduct.buttonList,
            shopItem.id,
            "${shopItem.name} ^ 0",
        )

        assertThat(mpsShopWidgetProductDataView.labelGroupList.size, `is`(shopItemProduct.labelGroupList.size))
        mpsShopWidgetProductDataView.labelGroupList.forEachIndexed { index, mpsProductLabelGroupDataView ->
            assertMPSProductLabelGroupDataView(
                mpsProductLabelGroupDataView,
                shopItemProduct.labelGroupList[index],
            )
        }
    }

    private fun assertMPSProductLabelGroupDataView(
        mpsProductLabelGroupDataView: MPSProductLabelGroupDataView,
        labelGroup: LabelGroup,
    ) {
        assertThat(mpsProductLabelGroupDataView.position, `is`(labelGroup.position))
        assertThat(mpsProductLabelGroupDataView.title, `is`(labelGroup.title))
        assertThat(mpsProductLabelGroupDataView.type, `is`(labelGroup.type))
        assertThat(mpsProductLabelGroupDataView.url, `is`(labelGroup.url))
    }

    @Test
    fun `multi product search load more success will append shop widget list`() {
        val firstPageMPSModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(firstPageMPSModel)
        val mpsViewModel = mpsViewModel(mpsStateAfterFirstPage)

        val loadMoreMPSModel = MPSLoadMoreSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS load more use case success`(loadMoreMPSModel)

        `When load more`(mpsViewModel)

        `Then assert shop widget visitable list load more`(
            mpsViewModel.visitableList,
            firstPageMPSModel,
            loadMoreMPSModel,
        )
    }

    @Test
    fun `multi product search load more success will not append duplicate shop widget list`() {
        val firstPageMPSModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(firstPageMPSModel)
        val mpsViewModel = mpsViewModel(mpsStateAfterFirstPage)

        val loadMoreMPSModel = MPSLoadMoreDuplicateJSON.jsonToObject<MPSModel>()
        `Given MPS load more use case success`(loadMoreMPSModel)

        `When load more`(mpsViewModel)

        `Then assert shop widget visitable list load more does not contain duplicate`(
            mpsViewModel.visitableList,
            firstPageMPSModel,
            loadMoreMPSModel,
        )
    }

    private fun `When load more`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewLoadMore()
    }

    private fun `Then assert shop widget visitable list load more`(
        mpsVisitableList: List<Visitable<*>>,
        firstPageMPSModel: MPSModel,
        loadMoreMPSModel: MPSModel,
    ) {
        val firstPageShopListSize = firstPageMPSModel.shopList.size
        val secondPageShopListSize = loadMoreMPSModel.shopList.size
        val totalShopCount = firstPageShopListSize + secondPageShopListSize
        val allShopWidgetVisitableList = mpsVisitableList.filterIsInstance<MPSShopWidgetDataView>()

        assertEquals(totalShopCount, allShopWidgetVisitableList.size)

        val loadMoreShopWidgetVisitableList = allShopWidgetVisitableList
            .takeLast(secondPageShopListSize)

        loadMoreShopWidgetVisitableList.forEachIndexed { index, mpsShopWidgetDataView ->
            val shopItemModel = loadMoreMPSModel.shopList[index]
            assertMPSShopWidgetDataView(mpsShopWidgetDataView, shopItemModel)
        }
    }

    private fun `Then assert shop widget visitable list load more does not contain duplicate`(
        mpsVisitableList: List<Visitable<*>>,
        firstPageMPSModel: MPSModel,
        loadMoreMPSModel: MPSModel,
    ) {
        val firstPageShopListSize = firstPageMPSModel.shopList.size
        val secondPageShopListNotDuplicate = loadMoreMPSModel.shopList.filterNot {
                shop -> firstPageMPSModel.shopList.any { it.id == shop.id }
        }
        val duplicateShopId = loadMoreMPSModel.shopList.filter {
                shop -> firstPageMPSModel.shopList.any { it.id == shop.id }
        }.map { it.id }
        val secondPageShopListSize = secondPageShopListNotDuplicate.size
        val totalShopCount = firstPageShopListSize + secondPageShopListSize
        val allShopWidgetVisitableList = mpsVisitableList.filterIsInstance<MPSShopWidgetDataView>()

        assertEquals(totalShopCount, allShopWidgetVisitableList.size)

        val loadMoreShopWidgetVisitableList = allShopWidgetVisitableList
            .takeLast(secondPageShopListSize)

        loadMoreShopWidgetVisitableList.forEachIndexed { index, mpsShopWidgetDataView ->
            val shopItemModel = secondPageShopListNotDuplicate[index]
            assertMPSShopWidgetDataView(mpsShopWidgetDataView, shopItemModel)
        }

        loadMoreShopWidgetVisitableList.none { it.id in duplicateShopId } shouldBe true
    }
}
