package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.DownloadVoucherViewHolder

/**
 * Created By @ilhamsuaib on 28/04/20
 */

class DownloadVoucherFactoryImpl : BaseAdapterTypeFactory(), DownloadVoucherFactory {

    override fun type(model: DownloadVoucherUiModel): Int = DownloadVoucherViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DownloadVoucherViewHolder.RES_LAYOUT -> DownloadVoucherViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}