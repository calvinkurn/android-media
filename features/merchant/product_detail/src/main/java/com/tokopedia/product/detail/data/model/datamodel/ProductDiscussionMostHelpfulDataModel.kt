package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.talk.Question
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductDiscussionMostHelpfulDataModel(
        val type: String = "",
        val name: String = "",
        var totalQuestion: Int = 0,
        var questions: List<Question>? = null,
        var isShimmering: Boolean = true
) : DynamicPdpDataModel {

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductDiscussionMostHelpfulDataModel) {
            isShimmering == newData.isShimmering
                    && questions?.size == newData.questions?.size
                    && totalQuestion == newData.totalQuestion
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