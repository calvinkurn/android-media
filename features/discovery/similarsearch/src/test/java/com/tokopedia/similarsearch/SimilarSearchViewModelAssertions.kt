package com.tokopedia.similarsearch

import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.discovery.common.State

internal fun State<List<Any>>?.shouldHaveCorrectViewModelListWithLoadingMore(similarProductItemList: List<Product>) {
    val lastIndex = this?.data?.lastIndex ?: 0

    this.shouldNotBeNull()

    val expectedDataSize = 2 + similarProductItemList.size
    (this?.data?.size ?: 0).shouldBe(expectedDataSize,
            "Unexpected data size. Actual ${this?.data?.size ?: 0}, expected: $expectedDataSize")

    this.shouldHaveDividerViewModel(0)
    this.shouldHaveSimilarProductItemModel(1, similarProductItemList)
    this.shouldHaveLoadingMoreViewModel(lastIndex)
}

private fun State<List<Any>>?.shouldNotBeNull() {
    if (this == null || this.data == null) {
        throw AssertionError("Data to assert is null")
    }
}

private fun State<List<Any>>?.shouldHaveDividerViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<DividerViewModel>()
}

private fun State<List<Any>>?.shouldHaveSimilarProductItemModel(fromIndex: Int, similarProductItemList: List<Product>) {
    val list = this?.data as List<Any>
    val similarProductItemViewModelList = list.subList(fromIndex, fromIndex + similarProductItemList.size)

    similarProductItemViewModelList.size.shouldBe(similarProductItemList.size, "Similar Search Item View Model size is not correct. Expected ${similarProductItemList.size}, Actual: ${similarProductItemViewModelList.size}")

    similarProductItemViewModelList.forEachIndexed { index, it ->
        it shouldBe similarProductItemList[index]
    }
}

private fun State<List<Any>>?.shouldHaveLoadingMoreViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<LoadingMoreModel>()
}

internal fun State<List<Any>>?.shouldHaveCorrectViewModelListWithoutLoadingMore(similarProductItemList: List<Product>) {
    this.shouldNotBeNull()

    val expectedDataSize = 1 + similarProductItemList.size
    (this?.data?.size ?: 0).shouldBe(expectedDataSize,
            "Unexpected data size. Actual ${this?.data?.size ?: 0}, expected: $expectedDataSize")

    this.shouldHaveDividerViewModel(0)
    this.shouldHaveSimilarProductItemModel(1, similarProductItemList)
}

internal fun State<List<Any>>?.shouldBeNullOrEmpty() {
    this?.data.isNullOrEmpty().shouldBe(true, "Data should be null")
}

internal fun State<List<Any>>?.shouldHaveCorrectEmptyResultView() {
    this.shouldNotBeNull()

    this?.data?.size ?: 0 shouldBe 2

    this.shouldHaveDividerViewModel(0)
    this.shouldHaveEmptyResultViewModel(1)
}

private fun State<List<Any>>?.shouldHaveEmptyResultViewModel(position: Int) {
    val list = this?.data as List<Any>

    list[position].shouldBeInstanceOf<EmptyResultViewModel>()
}