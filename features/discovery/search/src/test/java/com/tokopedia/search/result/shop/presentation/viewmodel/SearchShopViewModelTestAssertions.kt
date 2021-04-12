package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.search.result.shop.presentation.model.*
import com.tokopedia.discovery.common.State
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchDataView
import com.tokopedia.search.result.shop.presentation.model.ShopDataView
import com.tokopedia.search.shouldBe

internal inline fun <reified T> Any?.shouldBeInstanceOf(customMessage: String = "") {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        val message = if (customMessage.isEmpty()) "$actualClassName should be instance of $expectedClassName" else customMessage
        throw AssertionError(message)
    }
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query: String) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this.shouldHaveCpmViewModel(0)
    this.shouldHaveShopItemViewModel(1, lastIndex)
    this.shouldHaveLoadingMoreViewModel(lastIndex)
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query: String) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this.shouldHaveCpmViewModel(0)
    this.shouldHaveShopItemViewModel(1, lastIndex)
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithoutCpmViewModel() {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this.shouldHaveShopItemViewModel(0, lastIndex)
    this.shouldHaveLoadingMoreViewModel(lastIndex)
}

private fun State<List<Visitable<*>>>?.shouldNotBeNull() {
    if (this == null || this.data == null) {
        throw AssertionError("Data to assert is null")
    }
}

private fun State<List<Visitable<*>>>?.shouldHaveCpmViewModel(cpmViewModelPosition: Int) {
    val data = this?.data as List<Visitable<*>>

    data[cpmViewModelPosition].shouldBeInstanceOf<ShopCpmDataView>()
}

private fun State<List<Visitable<*>>>?.shouldHaveShopItemViewModel(shopItemStartPosition: Int, shopItemLastPosition: Int) {
    val data = this?.data as List<Visitable<*>>

    data.subList(shopItemStartPosition, shopItemLastPosition).forEachIndexed { index, visitable ->
        visitable.verifyShopItemIsCorrect(index)
    }
}

private fun State<List<Visitable<*>>>?.shouldHaveLoadingMoreViewModel(loadingMoreViewModelPosition: Int) {
    val data = this?.data as List<Visitable<*>>

    data[loadingMoreViewModelPosition].shouldBeInstanceOf<LoadingMoreModel>()
}

internal fun Visitable<*>.verifyShopItemIsCorrect(index: Int) {
    this.shouldBeInstanceOf<ShopDataView.ShopItem>()

    val shopItem = this as ShopDataView.ShopItem
    shopItem.shouldHaveCorrectPosition(index + 1)
    shopItem.shouldHaveCorrectProductItemPosition()
}

internal fun ShopDataView.ShopItem.shouldHaveCorrectPosition(expectedPosition: Int) {
    this.position shouldBe expectedPosition
}

internal fun ShopDataView.ShopItem.shouldHaveCorrectProductItemPosition() {
    this.productList.forEachIndexed { index, productItem ->
        productItem.position shouldBe index + 1
    }
}

internal fun State<List<Visitable<*>>>?.shouldHaveShopItemCount(size: Int) {
    this?.data?.count { it is ShopDataView.ShopItem } shouldBe size
}

internal fun State<List<Visitable<*>>>?.shouldBeNullOrEmpty() {
    this?.data.isNullOrEmpty() shouldBe true
}

internal fun State<List<Visitable<*>>>?.shouldOnlyHaveEmptySearchModel() {
    this?.data?.shouldHaveSize(1)
    this?.data?.first().shouldBeInstanceOf<ShopEmptySearchDataView>()
}

internal fun State<List<Visitable<*>>>?.shouldHaveEmptySearchWithRecommendationAndLoadMore() {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this?.data?.first().shouldBeInstanceOf<ShopEmptySearchDataView>()
    this.shouldHaveRecommendationTitle()
    this.shouldHaveShopItemViewModel(2, lastIndex - 1)
    this.shouldHaveLoadingMoreViewModel(lastIndex)
}

internal fun State<List<Visitable<*>>>?.shouldHaveEmptySearchWithRecommendationWithoutLoadMore() {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this?.data?.first().shouldBeInstanceOf<ShopEmptySearchDataView>()
    this.shouldHaveRecommendationTitle()
    this.shouldHaveShopItemViewModel(2, lastIndex)
}

private fun State<List<Visitable<*>>>?.shouldHaveRecommendationTitle() {
    this?.data?.get(1).shouldBeInstanceOf<ShopRecommendationTitleDataView>()
}

internal fun List<*>.shouldHaveSize(expectedSize: Int) {
    if (this.size != expectedSize) {
        throw AssertionError("Size should be $expectedSize. Actual size: ${this.size}")
    }
}

internal fun State<List<Visitable<*>>>?.shouldHaveEmptySearchModelWithExpectedIsFilter(expectedIsFilterActive: Boolean) {
    if (this?.data?.size == 0) {
        throw AssertionError("Search Shop State has no data, expected one ShopEmptySearchViewModel with isFilter = $expectedIsFilterActive")
    }

    this?.data?.forEach {
        if (it is ShopEmptySearchDataView) {
            it.isFilterActive shouldBe expectedIsFilterActive
            return
        }
    }
}