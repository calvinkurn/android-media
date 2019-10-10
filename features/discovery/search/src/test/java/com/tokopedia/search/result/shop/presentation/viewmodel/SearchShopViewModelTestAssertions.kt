package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopEmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.shouldBe

internal inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
    }
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithLoadingMoreViewModel() {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldHaveCorrectVisitableList(lastIndex - 1)

    this?.data?.last().shouldBeInstanceOf<LoadingMoreModel>()
}

internal fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel() {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldHaveCorrectVisitableList(lastIndex)
}

private fun State<List<Visitable<*>>>?.shouldHaveCorrectVisitableList(shopItemLastIndex: Int) {
    this.shouldNotBeNull()

    val data = this?.data as List<Visitable<*>>

    data.first().shouldBeInstanceOf<ShopCpmViewModel>()
    data[1].shouldBeInstanceOf<ShopTotalCountViewModel>()
    data.subList(2, shopItemLastIndex).forEachIndexed { index, visitable ->
        visitable.verifyShopItemIsCorrect(index)
    }
}

private fun State<List<Visitable<*>>>?.shouldNotBeNull() {
    if (this == null || this.data == null) {
        throw AssertionError("Data to assert is null")
    }
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
            if (it.isFilterActive != expectedIsFilterActive) {
                throw AssertionError("ShopEmptySearchViewModel isFilterActive = ${it.isFilterActive}, expected = $expectedIsFilterActive")
            }

            return
        }
    }
}