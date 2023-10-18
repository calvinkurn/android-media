package com.tokopedia.carouselproductcard.paging.list

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingProductCardView
import com.tokopedia.carouselproductcard.paging.Paging
import com.tokopedia.carouselproductcard.paging.HasGroup
import com.tokopedia.carouselproductcard.paging.Spannable
import com.tokopedia.carouselproductcard.paging.TypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel

internal data class ProductCardListDataView(
    val productCardModel: ProductCardModel,
    override val spanSize: Int,
    override val page: Int,
    override val group: CarouselPagingGroupModel,
    override val pageInGroup: Int,
    override val pageCount: Int,
    val productIndex: Int,
    val listener: CarouselPagingProductCardView.CarouselPagingListener,
): Visitable<TypeFactory>, HasGroup, Paging, Spannable, ImpressHolder() {

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
            productIndex: Int,
            listener: CarouselPagingProductCardView.CarouselPagingListener,
        ) = ProductCardListDataView(
            productCardModel,
            spanSize,
            page,
            group,
            pageInGroup,
            pageCount,
            productIndex,
            listener,
        )
    }
}
