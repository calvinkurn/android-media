package com.tokopedia.carouselproductcard.paging.list

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.Paging
import com.tokopedia.carouselproductcard.paging.HasGroup
import com.tokopedia.carouselproductcard.paging.Spannable
import com.tokopedia.carouselproductcard.paging.TypeFactory
import com.tokopedia.productcard.ProductCardModel

internal data class ProductCardListDataView(
    val productCardModel: ProductCardModel,
    override val spanSize: Int,
    override val page: Int,
    override val group: CarouselPagingGroupModel,
    override val pageInGroup: Int,
    override val pageCount: Int,
): Visitable<TypeFactory>, HasGroup, Paging, Spannable {

    override fun type(typeFactory: TypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {
        fun from(
            productCardModel: ProductCardModel,
            spanSize: Int,
            page: Int,
            group: CarouselPagingGroupModel,
            pageInGroup: Int,
            pageCount: Int,
        ) = ProductCardListDataView(
            productCardModel,
            spanSize,
            page,
            group,
            pageInGroup,
            pageCount,
        )
    }
}
