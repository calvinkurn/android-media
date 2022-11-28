package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by yovi.putra on 24/11/22"
 * Project name: android-tokopedia-core
 **/

data class ProductCustomInfoTitleDataModel(
    val name: String = "",
    val type: String = "",
    var title: String = "",
    var status: Status = Status.HIDE
) : DynamicPdpDataModel {

    enum class Status(val value: String) {
        SHOW("show"),
        HIDE("hide"),
        PLACEHOLDER("placeholder");

        companion object {

            fun fromString(status: String) = when (status) {
                PLACEHOLDER.value -> PLACEHOLDER
                SHOW.value -> SHOW
                else -> HIDE
            }
        }
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name() = name

    override fun type() = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel) = newData == this

    override fun newInstance() = this.copy()

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? = null
}
