package com.tokopedia.carouselproductcard.paging

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.paging.list.ProductCardListDataView
import com.tokopedia.carouselproductcard.paging.loading.ShimmeringDataView

internal object VisitableFactory {

    private const val DEFAULT_SPAN_SIZE = 1

    fun from(
        carouselPagingModel: CarouselPagingModel,
        itemPerPage: Int,
        listener: CarouselPagingProductCardView.CarouselPagingListener,
    ): List<Visitable<TypeFactory>> =
        carouselPagingModel.productCardGroupList.mapIndexed { groupIndex, groupProduct ->
            if (groupProduct.productItemList.isEmpty())  {
                listOf(ShimmeringDataView.from(groupProduct.group))
            } else {
                List(groupProduct.productItemList.size) { index ->
                    val pageInGroup = (index / itemPerPage) + 1
                    val pageCountBeforeGroup =
                        carouselPagingModel.pageCountBeforeGroup(groupIndex, itemPerPage)

                    ProductCardListDataView.from(
                        productCardModel = groupProduct.productItemList[index],
                        spanSize = spanSize(itemPerPage, index, groupProduct),
                        page = pageCountBeforeGroup + pageInGroup,
                        group = groupProduct.group,
                        pageInGroup = pageInGroup,
                        pageCount = groupProduct.getPageCount(itemPerPage),
                        productIndex = index,
                        listener = listener,
                    )
                }
            }
        }.flatten()

    private fun spanSize(
        itemPerPage: Int,
        index: Int,
        group: CarouselPagingGroupProductModel,
    ) = if (index == group.productItemList.lastIndex) itemPerPage - (index % itemPerPage)
        else DEFAULT_SPAN_SIZE
}
