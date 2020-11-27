package com.tokopedia.product.detail.data.model.datamodel

import android.os.Parcelable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.info.util.ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY
import kotlinx.android.parcel.Parcelize

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoDataModel(
        val name: String = "",
        val type: String = "",
        val dataContent: List<ProductDetailInfoContent> = listOf()
) : DynamicPdpDataModel {

    fun getShowableData(): List<ProductDetailInfoContent> {
        return dataContent.filter { it.showAtFront }.filterNot { it.title.toLowerCase() == DESCRIPTION_DETAIL_KEY }
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

@Parcelize
data class ProductDetailInfoContent(
        val icon: String = "",
        val title: String = "",
        var subtitle: String = "",
        val applink: String = "",
        val showAtFront: Boolean = false,
        val isAnnotation: Boolean = false
) : Parcelable