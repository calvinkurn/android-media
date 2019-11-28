package com.tokopedia.digital.productV2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.productV2.model.CatalogOperatorAttributes
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory
import com.tokopedia.vouchergame.list.view.model.VoucherGameOperatorAttributes

/**
 * Created by resakemal on 12/08/19.
 */
class CatalogOperator(

        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("attributes")
        @Expose
        val attributes: CatalogOperatorAttributes = CatalogOperatorAttributes(),
        var position: Int = 0

): Visitable<VoucherGameListAdapterFactory> {

        override fun type(typeFactory: VoucherGameListAdapterFactory) = typeFactory.type(this)

}