package com.tokopedia.buy_more_get_more.minicart.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.BmgmMiniCartDetailAdapterFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

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
        val showVerticalDivider: Boolean,
        val product: BmgmCommonDataModel.ProductModel,
        val showTopSpace: Boolean = false,
        val showBottomSpace: Boolean = false
    ) : MiniCartDetailUiModel {
        override fun type(typeFactory: BmgmMiniCartDetailAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}