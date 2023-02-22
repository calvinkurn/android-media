package com.tokopedia.search.result.mps

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.domain.model.MPSModel.AceSearchShop.Shop.Product.LabelGroup
import com.tokopedia.search.result.mps.shopwidget.MPSProductLabelGroupDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetDataView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetProductDataView
import com.tokopedia.search.result.stubExecute
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Test

internal class MultiProductSearchTest {

    private val mpsUseCase: UseCase<MPSModel> = mockk()

    private val mpsViewModel = MPSViewModel(
        mpsState = MPSState(
            mapOf(
                SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
                SearchApiConst.Q1 to "samsung",
                SearchApiConst.Q2 to "xiaomi",
                SearchApiConst.Q3 to "iphone",
            )
        ),
        mpsUseCase = mpsUseCase,
    )

    @Test
    fun `multi product search success`() {
        val mpsModel = "mps/mps.json".jsonToObject<MPSModel>()
        `Given MPS Use Case success`(mpsModel)

        `When view created`()

        val mpsState = mpsViewModel.stateFlow.value
        `Then assert state is success`(mpsState)
        `Then assert visitable list`(mpsState.result.data!!, mpsModel)
    }

    private fun `Given MPS Use Case success`(mpsModel: MPSModel) {
        mpsUseCase.stubExecute() returns mpsModel
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

    private fun assertMPSChooseAddress(
        mpsVisitableList: List<Visitable<*>>,
    ) {

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
        shopItemModel: MPSModel.AceSearchShop.Shop,
    ) {
        assertThat(mpsShopWidgetDataView.shopId, `is`(shopItemModel.id))
        assertThat(mpsShopWidgetDataView.shopName, `is`(shopItemModel.name))
        assertThat(mpsShopWidgetDataView.shopImage, `is`(shopItemModel.image))
        assertThat(mpsShopWidgetDataView.shopLocation, `is`(shopItemModel.location))
        assertThat(mpsShopWidgetDataView.shopButtonTitle, `is`(shopItemModel.buttonTitle))
        assertThat(mpsShopWidgetDataView.shopBadge.imageUrl, `is`(shopItemModel.badge.imageUrl))
        assertThat(mpsShopWidgetDataView.shopBadge.isShow, `is`(shopItemModel.badge.isShow))
        assertThat(mpsShopWidgetDataView.shopFreeOngkir.imageUrl, `is`(shopItemModel.freeOngkir.imageUrl))
        assertThat(mpsShopWidgetDataView.shopFreeOngkir.isActive, `is`(shopItemModel.freeOngkir.isActive))

        assertThat(mpsShopWidgetDataView.productList.size, `is`(shopItemModel.productList.size))
        mpsShopWidgetDataView.productList.forEachIndexed { index, mpsShopWidgetProductDataView ->
            assertMPSShopWidgetProductDataView(
                mpsShopWidgetProductDataView,
                shopItemModel.productList[index]
            )
        }
    }

    private fun assertMPSShopWidgetProductDataView(
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView,
        shopItemProduct: MPSModel.AceSearchShop.Shop.Product,
    ) {
        assertThat(mpsShopWidgetProductDataView.productId, `is`(shopItemProduct.id))
        assertThat(mpsShopWidgetProductDataView.productImageUrl, `is`(shopItemProduct.imageUrl))
        assertThat(mpsShopWidgetProductDataView.productName, `is`(shopItemProduct.name))
        assertThat(mpsShopWidgetProductDataView.applink, `is`(shopItemProduct.applink))
        assertThat(mpsShopWidgetProductDataView.priceFormat, `is`(shopItemProduct.priceFormat))
        assertThat(mpsShopWidgetProductDataView.originalPrice, `is`(shopItemProduct.originalPrice))
        assertThat(mpsShopWidgetProductDataView.discountPercentage, `is`(shopItemProduct.discountPercentage))
        assertThat(mpsShopWidgetProductDataView.ratingAverage, `is`(shopItemProduct.ratingAverage))

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

        val mpsState = mpsViewModel.stateFlow.value
        `Then assert state is error`(mpsState)
    }

    private fun `Given MPS Use Case failed`() {
        mpsUseCase.stubExecute() throws TestException("test exception")
    }

    private fun `Then assert state is error`(mpsState: MPSState) {
        assertThat(mpsState.result, `is`(instanceOf(State.Error::class.java)))
    }
}
