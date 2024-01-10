package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.data.model.social_proof.SocialProofUiModel
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 19/05/20
 */
data class ProductMiniSocialProofDataModel(
    val type: String = "",
    val name: String = "",
    val items: List<SocialProofUiModel> = emptyList()
) : DynamicPdpDataModel,
    LoadableComponent by BlocksLoadableComponent(
        isFinishedLoading = { false },
        customBlocksName = "ProductMiniSocialProofDataModel"
    ) {

    override fun position(): TabletPosition = TabletPosition.LEFT

    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductMiniSocialProofDataModel) {
            items.hashCode() == newData.items.hashCode()
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

    override fun isLoading(): Boolean {
        return items.isEmpty()
    }
}
