package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartDetailAdapterFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel

/**
 * Created by @ilhamsuaib on 10/08/23.
 */

sealed interface MiniCartDetailUiModel : Visitable<BmgmMiniCartDetailAdapterFactory> {

    data class Section(
        val sectionText: String, val isDiscountSection: Boolean
    ) : MiniCartDetailUiModel {
        override fun type(typeFactory: BmgmMiniCartDetailAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class Product(
        val isDiscountedProduct: Boolean,
        val product: BmgmCommonDataUiModel.SingleProductUiModel
    ) : MiniCartDetailUiModel {
        override fun type(typeFactory: BmgmMiniCartDetailAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}