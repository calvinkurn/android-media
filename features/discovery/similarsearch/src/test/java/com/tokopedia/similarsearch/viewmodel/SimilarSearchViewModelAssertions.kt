package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.discovery.common.State
import com.tokopedia.similarsearch.divider.DividerViewModel
import com.tokopedia.similarsearch.emptyresult.EmptyResultViewModel
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.shouldBeInstanceOf
import com.tokopedia.similarsearch.title.TitleViewModel
import com.tokopedia.similarsearch.utils.asObjectDataLayer

internal fun State<List<Any>>?.shouldHaveCorrectViewModelListWithLoadingMore(similarProductItemList: List<Product>) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    // Divider + Title + Loading More + SimilarProductList size
    this.shouldHaveCorrectDataSize(3 + similarProductItemList.size)

    this.shouldHaveDividerViewModel(0)
    this.shouldHaveTitleViewModel(1)
    this.shouldHaveSimilarProductItemModel(2, similarProductItemList)
    this.shouldHaveLoadingMoreViewModel(lastIndex)
}

private fun State<List<Any>>?.shouldNotBeNull() {
    if (this == null || this.data == null) {
        throw AssertionError("Data to assert is null")
    }
}

private fun State<List<Any>>?.shouldHaveCorrectDataSize(expectedDataSize: Int) {
    (this?.data?.size ?: 0).shouldBe(expectedDataSize,
            "Unexpected data size. Actual ${this?.data?.size ?: 0}, expected: $expectedDataSize")
}

private fun State<List<Any>>?.shouldHaveDividerViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<DividerViewModel>()
}

private fun State<List<Any>>?.shouldHaveTitleViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<TitleViewModel>()
}

private fun State<List<Any>>?.shouldHaveSimilarProductItemModel(fromIndex: Int, similarProductItemList: List<Product>) {
    val list = this?.data as List<Any>
    val similarProductItemViewModelList = list.subList(fromIndex, fromIndex + similarProductItemList.size)

    similarProductItemViewModelList.size.shouldBe(similarProductItemList.size, "Similar Search Item View Model size is not correct. Expected ${similarProductItemList.size}, Actual: ${similarProductItemViewModelList.size}")

    similarProductItemViewModelList.forEachIndexed { index, it ->
        it shouldBe similarProductItemList[index]
        (it as Product).position shouldBe index + 1
    }
}

private fun State<List<Any>>?.shouldHaveLoadingMoreViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<LoadingMoreModel>()
}

internal fun State<List<Any>>?.shouldHaveCorrectViewModelListWithoutLoadingMore(similarProductItemList: List<Product>) {
    this.shouldNotBeNull()

    // Divider + Title + SimilarProductList size
    this.shouldHaveCorrectDataSize(2 + similarProductItemList.size)

    this.shouldHaveDividerViewModel(0)
    this.shouldHaveTitleViewModel(1)
    this.shouldHaveSimilarProductItemModel(2, similarProductItemList)
}

internal fun State<List<Any>>?.shouldBeNullOrEmpty() {
    this?.data.isNullOrEmpty().shouldBe(true, "Data should be null")
}

internal fun State<List<Any>>?.shouldHaveCorrectEmptyResultView() {
    this.shouldNotBeNull()

    // Divider + Empty Result
    this.shouldHaveCorrectDataSize(2)

    this.shouldHaveDividerViewModel(0)
    this.shouldHaveEmptyResultViewModel(1)
}

private fun State<List<Any>>?.shouldHaveEmptyResultViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<EmptyResultViewModel>()
}

internal fun List<Any>.shouldHaveSimilarProductWithExpectedWishlistStatus(productId: String, expectedWishlistStatus: Boolean) {
    this.getSimilarProductItem(productId).isWishlisted.shouldBe(
            expectedWishlistStatus,
            "Chosen Product $productId isWishlisted status should be $expectedWishlistStatus")
}

private fun List<Any>.getSimilarProductItem(productId: String): Product {
    return this.find { it is Product && it.id == productId } as Product
}

internal fun List<Any>?.shouldBeListOfMapOfProductItemAsObjectDataLayer(similarProductItemList: List<Product>) {
    this.shouldBeInstanceOf<List<Any>>()

    this?.size ?: 0 shouldBe similarProductItemList.size

    this?.forEachIndexed { index, it ->
        it.shouldBeInstanceOf<Map<String, Any>>()
        it shouldBe similarProductItemList[index].asObjectDataLayer()
    }
}