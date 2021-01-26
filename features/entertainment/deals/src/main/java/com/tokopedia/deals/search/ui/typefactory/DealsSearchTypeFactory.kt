package com.tokopedia.deals.search.ui.typefactory

import com.tokopedia.deals.search.model.visitor.*

interface DealsSearchTypeFactory {
    //Search
    fun type(viewItem: SectionTitleModel): Int
    fun type(viewItem: VoucherModel): Int
    fun type(item: MerchantModelModel): Int
    fun type(viewItem: CuratedModel): Int
    fun type(viewItem: RecentModel): Int
    fun type(viewItem: MoreBrandModel): Int
    fun type(viewItem: NotFoundModel): Int
}