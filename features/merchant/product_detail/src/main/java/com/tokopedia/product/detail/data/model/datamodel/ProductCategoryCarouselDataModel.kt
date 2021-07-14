package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.pdplayout.CategoryCarousel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 13/07/21
 */
data class ProductCategoryCarouselDataModel(
        var name: String = "",
        var type: String = "",
        var titleCarousel: String = "",
        var linkText: String = "",
        var applink: String = "",
        var categoryList: List<CategoryCarousel> = listOf()
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductCategoryCarouselDataModel) {
            titleCarousel == newData.titleCarousel
                    && linkText == newData.linkText
                    && applink == newData.applink
                    && categoryList.hashCode() == newData.categoryList.hashCode()
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
