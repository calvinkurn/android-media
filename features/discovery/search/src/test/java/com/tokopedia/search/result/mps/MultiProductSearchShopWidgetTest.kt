package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.domain.model.MPSModel.AceSearchShopMPS.Shop.Product.LabelGroup
import com.tokopedia.search.result.mps.shopwidget.MPSButtonDataView
import com.tokopedia.search.result.mps.shopwidget.MPSProductLabelGroupDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import com.tokopedia.search.result.stubExecute
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Test

internal class MultiProductSearchShopWidgetTest: MultiProductSearchTestFixtures() {

    private val mpsViewModel = mpsViewModel(
        MPSState(
            mapOf(
                SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
                SearchApiConst.Q1 to "samsung",
                SearchApiConst.Q2 to "xiaomi",
                SearchApiConst.Q3 to "iphone",
            )
        ),
    )

    @Test
    fun `multi product search success`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS Use Case success`(mpsModel)

        `When view created`()

        val mpsState = mpsViewModel.stateValue
        `Then assert state is success`(mpsState)
        `Then assert visitable list`(mpsState.result.data!!, mpsModel)
    }

    private fun `When view created`() {
        mpsViewModel.onViewCreated()
    }

    private fun `Then assert state is success`(mpsState: MPSState) {
        assertThat(mpsState.result, `is`(instanceOf(State.Success::class.java)))
    }

    private fun `Then assert visitable list`(
        mpsVisitableList: List<Visitable<*>>,
        mpsModel: MPSModel,
    ) {
        assertMPSShopWidget(mpsVisitableList, mpsModel)
    }

    private fun assertMPSShopWidget(
        mpsVisitableList: List<Visitable<*>>,
        mpsModel: MPSModel
    ) {
        val mpsShopWidgetDataViewList =
            mpsVisitableList.filterIsInstance<MPSShopWidgetDataView>()
        val shopList = mpsModel.shopList

        assertThat(mpsShopWidgetDataViewList.size, `is`(shopList.size))

        mpsShopWidgetDataViewList.forEachIndexed { index, mpsShopWidgetDataView ->
            val shopItemModel = shopList[index]
            assertMPSShopWidgetDataView(mpsShopWidgetDataView, shopItemModel)
        }
    }

    private fun assertMPSShopWidgetDataView(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        shopItemModel: MPSModel.AceSearchShopMPS.Shop,
    ) {
        assertThat(mpsShopWidgetDataView.id, `is`(shopItemModel.id))
        assertThat(mpsShopWidgetDataView.name, `is`(shopItemModel.name))
        assertThat(mpsShopWidgetDataView.imageUrl, `is`(shopItemModel.imageUrl))
        assertThat(mpsShopWidgetDataView.location, `is`(shopItemModel.location))
        assertThat(mpsShopWidgetDataView.applink, `is`(shopItemModel.applink))
        assertThat(mpsShopWidgetDataView.componentId, `is`(shopItemModel.componentId))
        assertThat(mpsShopWidgetDataView.trackingOption, `is`(shopItemModel.trackingOption))

        assertThat(mpsShopWidgetDataView.ticker.type, `is`(shopItemModel.ticker.type))
        assertThat(mpsShopWidgetDataView.ticker.message, `is`(shopItemModel.ticker.message))

        assertThat(mpsShopWidgetDataView.shopFreeOngkir.imageUrl, `is`(shopItemModel.freeOngkir.imageUrl))
        assertThat(mpsShopWidgetDataView.shopFreeOngkir.isActive, `is`(shopItemModel.freeOngkir.isActive))

        assertThat(mpsShopWidgetDataView.badgeList.size, `is`(shopItemModel.badgeList.size))
        mpsShopWidgetDataView.badgeList.forEachIndexed { index, mpsShopBadgeDataView ->
            assertThat(mpsShopBadgeDataView.imageUrl, `is`(shopItemModel.badgeList[index].imageUrl))
            assertThat(mpsShopBadgeDataView.isShow, `is`(shopItemModel.badgeList[index].isShow))
        }

        assertButtonDataViewList(mpsShopWidgetDataView.buttonList, shopItemModel.buttonList)

        assertThat(mpsShopWidgetDataView.productList.size, `is`(shopItemModel.productList.size))
        mpsShopWidgetDataView.productList.forEachIndexed { index, mpsShopWidgetProductDataView ->
            assertMPSShopWidgetProductDataView(
                mpsShopWidgetProductDataView,
                shopItemModel.productList[index]
            )
        }
    }

    private fun assertButtonDataViewList(
        buttonDataViewList: List<MPSButtonDataView>,
        buttonList: List<MPSModel.AceSearchShopMPS.Shop.Button>
    ) {
        assertThat(buttonDataViewList.size, `is`(buttonList.size))
        buttonDataViewList.forEachIndexed { index, mpsButtonDataView ->
            assertThat(mpsButtonDataView.text, `is`(buttonList[index].text))
            assertThat(mpsButtonDataView.applink, `is`(buttonList[index].applink))
            assertThat(mpsButtonDataView.componentId, `is`(buttonList[index].componentId))
            assertThat(mpsButtonDataView.trackingOption, `is`(buttonList[index].trackingOption))
        }
    }

    private fun assertMPSShopWidgetProductDataView(
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView,
        shopItemProduct: MPSModel.AceSearchShopMPS.Shop.Product,
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

        assertButtonDataViewList(mpsShopWidgetProductDataView.buttonList, shopItemProduct.buttonList)

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
    fun `multi product search failed`() {
        `Given MPS Use Case failed`()

        `When view created`()

        val mpsState = mpsViewModel.stateValue
        `Then assert state is error`(mpsState)
    }

    private fun `Given MPS Use Case failed`() {
        mpsUseCase.stubExecute() throws TestException("test exception")
    }

    private fun `Then assert state is error`(mpsState: MPSState) {
        assertThat(mpsState.result, `is`(instanceOf(State.Error::class.java)))
    }
}
