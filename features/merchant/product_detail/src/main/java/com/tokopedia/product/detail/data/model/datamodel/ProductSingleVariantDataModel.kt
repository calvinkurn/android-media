package com.tokopedia.product.detail.data.model.datamodel

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.ProductDetailAdapterFactory

/**
 * Created by Yehezkiel on 21/05/21
 */
data class ProductSingleVariantDataModel(
    val type: String = "",
    val name: String = "",
    var variantLevelOne: VariantCategory? = null,
    var mapOfSelectedVariant: MutableMap<String, String> = mutableMapOf(),
    var isVariantError: Boolean = false,
    var isRefreshing: Boolean = false,
    var thumbnailType: String = "", // single variant for thumbnail variant in pdp
    var title: String = ""
) : DynamicPdpDataModel,
    LoadableComponent by BlocksLoadableComponent(
        isFinishedLoading = { false },
        customBlocksName = "ProductSingleVariantDataModel"
    ) {

    override val tabletSectionPosition: TabletPosition
        get() = TabletPosition.LEFT

    override fun type(): String = type

    override fun type(typeFactory: ProductDetailAdapterFactory): Int = typeFactory.type(this)

    override fun name(): String = name

    override val impressHolder = ImpressHolder()

    override fun equalsWith(newData: DynamicPdpDataModel): Boolean {
        return if (newData is ProductSingleVariantDataModel) {
            isVariantError == newData.isVariantError &&
                variantLevelOne == newData.variantLevelOne &&
                title == newData.title
        } else {
            false
        }
    }

    override fun newInstance(): DynamicPdpDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: DynamicPdpDataModel): Bundle? {
        val bundle = Bundle()
        return if (newData is ProductSingleVariantDataModel && isSelectedVariantChanged(newData)) {
            bundle.putInt(
                ProductDetailConstant.DIFFUTIL_PAYLOAD,
                ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT
            )
            bundle
        } else {
            null
        }
    }

    private fun isSelectedVariantChanged(newVariantDataModel: ProductSingleVariantDataModel): Boolean {
        var isChanged = false
        for ((key, value) in newVariantDataModel.mapOfSelectedVariant) {
            val currentValue = mapOfSelectedVariant[key]

            if (currentValue != value) {
                isChanged = true
                break
            }
        }
        return isChanged
    }

    override fun isLoading(): Boolean {
        return variantLevelOne == null
    }

    val isThumbnailType get() = thumbnailType == ProductDetailConstant.THUMB_MINI_VARIANT_OPTIONS

    fun getComponentNameAsThumbnail(): String = name() + " - thumbnail"
}
