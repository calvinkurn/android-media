package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmProductPlaceholderViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmSingleProductViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmBundledProductViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmMiniCartDetailProductViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmMiniCartDetailSectionViewHolder
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.purchase_platform.common.feature.bmgm.adapter.BmgmMiniCartAdapterFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailAdapterFactoryImpl : BaseAdapterTypeFactory(),
    BmgmMiniCartDetailAdapterFactory {

    override fun type(model: MiniCartDetailUiModel.Section): Int {
        return BmgmMiniCartDetailSectionViewHolder.RES_LAYOUT
    }

    override fun type(model: MiniCartDetailUiModel.Product): Int {
        return BmgmMiniCartDetailProductViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BmgmMiniCartDetailSectionViewHolder.RES_LAYOUT -> BmgmMiniCartDetailSectionViewHolder(
                parent
            )

            BmgmMiniCartDetailProductViewHolder.RES_LAYOUT -> BmgmMiniCartDetailProductViewHolder(
                parent
            )

            else -> super.createViewHolder(parent, type)
        }
    }
}