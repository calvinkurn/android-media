package com.tokopedia.deals.ui.search.ui.typefactory

import com.tokopedia.deals.ui.search.model.visitor.CuratedModel
import com.tokopedia.deals.ui.search.model.visitor.MerchantModelModel
import com.tokopedia.deals.ui.search.model.visitor.MoreBrandModel
import com.tokopedia.deals.ui.search.model.visitor.NotFoundModel
import com.tokopedia.deals.ui.search.model.visitor.RecentModel
import com.tokopedia.deals.ui.search.model.visitor.SectionTitleModel
import com.tokopedia.deals.ui.search.model.visitor.VoucherModel

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
