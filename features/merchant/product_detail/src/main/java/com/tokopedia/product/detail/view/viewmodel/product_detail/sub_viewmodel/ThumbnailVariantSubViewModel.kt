package com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.library.subviewmodel.SubViewModel
import com.tokopedia.library.subviewmodel.extension.getMediator
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.Variant.Companion.isVariantEmpty
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.view.util.ProductDetailVariantLogic
import com.tokopedia.product.detail.view.viewmodel.product_detail.IThumbnailVariantSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.mediator.GetProductDetailDataMediator
import javax.inject.Inject

/**
 * Created by yovi.putra on 06/09/23"
 * Project name: tokopedia-app-wg
 **/

class ThumbnailVariantSubViewModel @Inject constructor() :
    SubViewModel(),
    IThumbnailVariantSubViewModel {

    companion object {
        private const val VARIANT_LEVEL_TWO_INDEX = 1
    }

    // slicing from _onVariantClickedData, because thumbnail variant feature using vbs for refresh pdp info
    private val _onThumbnailVariantSelectedData = MutableLiveData<ProductSingleVariantDataModel?>()
    override val onThumbnailVariantSelectedData: LiveData<ProductSingleVariantDataModel?>
        get() = _onThumbnailVariantSelectedData

    val pdpMediator: GetProductDetailDataMediator by getMediator()

    override fun onThumbnailVariantSelected(
        uiData: ProductSingleVariantDataModel?,
        variantId: String,
        categoryKey: String
    ) {
        val singleVariant = uiData ?: return
        val variantSelected = singleVariant.mapOfSelectedVariant
        val variantDataNonNull = pdpMediator.getVariant() ?: ProductVariant()
        val variantSelectUpdated = selectVariantTwoOnThumbnailVariantSelected(
            productVariant = variantDataNonNull,
            variantsSelected = variantSelected,
            newVariantId = variantId,
            newVariantCategoryKey = categoryKey
        )
        val variantLevelOneUpdated = ProductDetailVariantLogic.determineVariant(
            variantSelectUpdated,
            variantDataNonNull
        )

        if (variantLevelOneUpdated != null) {
            _onThumbnailVariantSelectedData.postValue(
                singleVariant.copy(
                    mapOfSelectedVariant = variantSelectUpdated,
                    variantLevelOne = variantLevelOneUpdated
                )
            )
        }
    }

    private fun selectVariantTwoOnThumbnailVariantSelected(
        productVariant: ProductVariant,
        variantsSelected: Map<String, String>,
        newVariantId: String,
        newVariantCategoryKey: String
    ): MutableMap<String, String> {
        val variants = variantsSelected.toMutableMap()
        val variantLevelTwo = productVariant.variants.getOrNull(VARIANT_LEVEL_TWO_INDEX)

        // in case, when swipe media but media is not variant
        if (newVariantId.isEmpty()) {
            setEmptyToVariantLevelOne(productVariant, variants)
            return variants
        }

        // don't move this order, because level 1 always on top and level 2 always below lvl1 in map
        variants[newVariantCategoryKey] = newVariantId

        if (variantLevelTwo != null) {
            setVariantTwoIfEmptyOnThumbnailVariantSelected(variantLevelTwo, variants)
        }

        return variants
    }

    private fun setEmptyToVariantLevelOne(
        productVariant: ProductVariant,
        variants: MutableMap<String, String>
    ) {
        // set empty to variant level 1, and keep variant level2 if available
        val variantLevelOne = productVariant.variants.getOrNull(Int.ZERO)

        if (variantLevelOne != null) {
            variants[variantLevelOne.pv.orEmpty()] = Int.ZERO.toString()
        }
    }

    private fun setVariantTwoIfEmptyOnThumbnailVariantSelected(
        variantLevelTwo: Variant,
        variants: MutableMap<String, String>
    ) {
        // if mapOfSelected still don't select yet with variant lvl two, so set default lvl2 with fist lvl2 item
        val variantLvl2Value = variants[variantLevelTwo.pv]
        if (variantLvl2Value.isVariantEmpty()) {
            // in case, thumb variant selected but variant two never select from vbs
            val variantLevelTwoId = variantLevelTwo.options.firstOrNull()?.id
            variants[variantLevelTwo.pv.orEmpty()] = variantLevelTwoId.orEmpty()
        }
    }
}
