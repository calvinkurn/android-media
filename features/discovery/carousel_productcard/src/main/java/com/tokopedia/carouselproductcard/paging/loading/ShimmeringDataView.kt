package com.tokopedia.carouselproductcard.paging.loading

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.HasGroup
import com.tokopedia.carouselproductcard.paging.Spannable
import com.tokopedia.carouselproductcard.paging.TypeFactory

internal class ShimmeringDataView(
    override val group: CarouselPagingGroupModel,
) : Visitable<TypeFactory>, HasGroup, Spannable {

    override val pageInGroup: Int
        get() = PAGE

    override val pageCount: Int
        get() = PAGE_COUNT

    override val spanSize: Int
        get() = SPAN_SIZE

    override fun type(typeFactory: TypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {
        fun from(group: CarouselPagingGroupModel): ShimmeringDataView {
            return ShimmeringDataView(group)
        }

        private const val PAGE = 1
        private const val PAGE_COUNT = 1
        private const val SPAN_SIZE = 3
    }
}
