package com.tokopedia.product.detail.data.model.datamodel.review_list

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by yovi.putra on 16/02/23"
 * Project name: android-tokopedia-core
 **/


data class ProductReviewListDataModel(
    val type: String = "",
    val name: String = "",
    var shouldRender: Boolean = false,
    var data: ProductReviewListUiModel = ProductReviewListUiModel()
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductReviewListDataModel) {
            shouldRender == newData.shouldRender &&
                data == newData.data &&
                data.reviews.hashCode() == newData.data.hashCode()
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
