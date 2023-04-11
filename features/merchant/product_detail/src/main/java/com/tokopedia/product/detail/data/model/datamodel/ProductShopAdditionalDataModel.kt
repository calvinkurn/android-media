package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by yovi.putra on 17/10/22"
 * Project name: android-tokopedia-core
 **/

data class ProductShopAdditionalDataModel(
    val name: String = String.EMPTY,
    val type: String = String.EMPTY,
    var title: String = String.EMPTY,
    var icon: String = String.EMPTY,
    var description: String = String.EMPTY,
    var appLink: String = String.EMPTY,
    var linkText: String = String.EMPTY,
    var labels: List<String> = emptyList()
) : DynamicPdpDataModel {

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductShopAdditionalDataModel) {
            title == newData.title &&
                icon == newData.icon &&
                description == newData.description &&
                appLink == newData.appLink &&
                linkText == newData.linkText &&
                labels.hashCode() == newData.labels.hashCode()
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        return null
    }

    val isLoading get() = title.isEmpty()
}
