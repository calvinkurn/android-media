package com.tokopedia.carouselproductcard.paging

data class CarouselPagingModel(
    val productCardGroupList: List<CarouselPagingGroupProductModel> = listOf(),
    val currentGroupIndex: Int = 0,
    val currentPageInGroup: Int = FIRST_PAGE_IN_GROUP,
) {

    val selectedProductGroup =
        productCardGroupList.getOrNull(currentGroupIndex) ?: productCardGroupList.firstOrNull()

    val selectedGroup = selectedProductGroup?.group

    fun pageCountBeforeGroup(groupIndex: Int, itemPerPage: Int) =
        productCardGroupList.subList(0, groupIndex).sumOf { it.getPageCount(itemPerPage) }

    companion object {
        const val FIRST_PAGE_IN_GROUP = 1
        const val LAST_PAGE_IN_GROUP = -1
    }
}
