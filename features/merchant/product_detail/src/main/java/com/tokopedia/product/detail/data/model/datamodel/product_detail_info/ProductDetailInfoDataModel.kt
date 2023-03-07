package com.tokopedia.product.detail.data.model.datamodel.product_detail_info

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.product.info.util.ProductDetailInfoConstant.DESCRIPTION_DETAIL_KEY

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoDataModel(
    val name: String = "",
    val type: String = "",
    val title: String = "",
    val catalogBottomSheet: ProductDetailInfoSeeMore? = null,
    val bottomSheet: ProductDetailInfoSeeMore = ProductDetailInfoSeeMore(),
    val dataContent: List<ProductDetailInfoContent> = listOf()
) : DynamicPdpDataModel {

    val isCatalog
        get() = catalogBottomSheet?.let {
            it.actionTitle.isNotBlank() && it.bottomSheetTitle.isNotBlank()
        } ?: run {
            false
        }

    fun getShowableData(): List<ProductDetailInfoContent> {
        return dataContent.filter { it.showAtFront }
            .filterNot { it.title.lowercase() == DESCRIPTION_DETAIL_KEY }
    }

    fun getDescription(): String {
        return dataContent.find {
            it.title.lowercase() == DESCRIPTION_DETAIL_KEY
        }?.subtitle ?: ""
    }

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductDetailInfoDataModel) {
            dataContent.hashCode() == newData.dataContent.hashCode()
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
}

