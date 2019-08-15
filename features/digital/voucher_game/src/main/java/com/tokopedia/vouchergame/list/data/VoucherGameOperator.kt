package com.tokopedia.vouchergame.list.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchergame.list.view.adapter.VoucherGameListAdapterFactory

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameOperator(

        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes = Attributes()

): Visitable<VoucherGameListAdapterFactory> {

        override fun type(typeFactory: VoucherGameListAdapterFactory) = typeFactory.type(this)

        class Attributes(

                @SerializedName("name")
                @Expose
                val name: String = "",
                @SerializedName("image")
                @Expose
                val image: String = "",
                @SerializedName("image_url")
                @Expose
                val image_url: String = ""

        )

}