package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.ShareVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.ShareVoucherViewHolder

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class ShareVoucherFactoryImpl(
        private val onItemClickListener: (ShareVoucherUiModel) -> Unit
) : BaseAdapterTypeFactory(), ShareVoucherFactory {

    override fun type(model: ShareVoucherUiModel): Int = ShareVoucherViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShareVoucherViewHolder.RES_LAYOUT -> ShareVoucherViewHolder(parent, onItemClickListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}