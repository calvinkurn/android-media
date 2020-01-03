package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.shop.presentation.model.*
import com.tokopedia.search.shouldBe

internal inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
    }
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query: String) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this.shouldHaveCpmViewModel(0)
    this.shouldHaveTotalCountViewModel(1, query, true)
    this.shouldHaveShopItemViewModel(2, lastIndex - 1)
    this.shouldHaveLoadingMoreViewModel(lastIndex)
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query: String) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this.shouldHaveCpmViewModel(0)
    this.shouldHaveTotalCountViewModel(1, query, true)
    this.shouldHaveShopItemViewModel(2, lastIndex)
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithoutCpmViewModel(query: String) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this.shouldHaveTotalCountViewModel(0, query, false)
    this.shouldHaveShopItemViewModel(1, lastIndex)
}

private fun State<List<Visitable<*>>>?.shouldNotBeNull() {
    if (this == null || this.data == null) {
        throw AssertionError("Data to assert is null")
    }
}

private fun State<List<Visitable<*>>>?.shouldHaveCpmViewModel(cpmViewModelPosition: Int) {
    val data = this?.data as List<Visitable<*>>

    data[cpmViewModelPosition].shouldBeInstanceOf<ShopCpmViewModel>()
}

private fun State<List<Visitable<*>>>?.shouldHaveTotalCountViewModel(totalCountViewModelPosition: Int, query: String, isAdsBannerVisible: Boolean) {
    val data = this?.data as List<Visitable<*>>

    data[totalCountViewModelPosition].shouldBeInstanceOf<ShopTotalCountViewModel>()

    val shopTotalCountViewModel = data[totalCountViewModelPosition] as ShopTotalCountViewModel
    shopTotalCountViewModel.shouldHaveCorrectQuery(query)
    shopTotalCountViewModel.shouldHaveCorrectIsAdsBannerVisible(isAdsBannerVisible)
}

private fun ShopTotalCountViewModel.shouldHaveCorrectQuery(query: String) {
    return this.query shouldBe query
}

private fun ShopTotalCountViewModel.shouldHaveCorrectIsAdsBannerVisible(isAdsBannerVisible: Boolean) {
    return this.isAdsBannerVisible shouldBe isAdsBannerVisible
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
    this.shouldBeInstanceOf<ShopViewModel.ShopItem>()

    val shopItem = this as ShopViewModel.ShopItem
    shopItem.shouldHaveCorrectPosition(index + 1)
    shopItem.shouldHaveCorrectProductItemPosition()
}

internal fun ShopViewModel.ShopItem.shouldHaveCorrectPosition(expectedPosition: Int) {
    this.position shouldBe expectedPosition
}

internal fun ShopViewModel.ShopItem.shouldHaveCorrectProductItemPosition() {
    this.productList.forEachIndexed { index, productItem ->
        productItem.position shouldBe index + 1
    }
}

internal fun State<List<Visitable<*>>>?.shouldHaveShopItemCount(size: Int) {
    this?.data?.count { it is ShopViewModel.ShopItem } shouldBe size
}

internal fun State<List<Visitable<*>>>?.shouldBeNullOrEmpty() {
    this?.data.isNullOrEmpty() shouldBe true
}

internal fun State<List<Visitable<*>>>?.shouldOnlyHaveEmptySearchModel() {
    this?.data?.shouldHaveSize(1)
    this?.data?.first().shouldBeInstanceOf<ShopEmptySearchViewModel>()
}

internal fun State<List<Visitable<*>>>?.shouldHaveEmptySearchWithRecommendation() {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    this?.data?.first().shouldBeInstanceOf<ShopEmptySearchViewModel>()
    this.shouldHaveRecommendationTitle()
    this.shouldHaveShopItemViewModel(2, lastIndex)
}

private fun State<List<Visitable<*>>>?.shouldHaveRecommendationTitle() {
    this?.data?.get(1).shouldBeInstanceOf<ShopRecommendationTitleViewModel>()
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
        if (it is ShopEmptySearchViewModel) {
            it.isFilterActive shouldBe expectedIsFilterActive
            return
        }
    }
}