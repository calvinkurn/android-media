package com.tokopedia.vouchergame.list.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory
import com.tokopedia.vouchergame.list.view.model.VoucherGameOperatorAttributes

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameOperator(

        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("attributes")
        @Expose
        val attributes: VoucherGameOperatorAttributes = VoucherGameOperatorAttributes(),
        var position: Int = 0

): Visitable<VoucherGameListAdapterFactory> {

        override fun type(typeFactory: VoucherGameListAdapterFactory) = typeFactory.type(this)

}