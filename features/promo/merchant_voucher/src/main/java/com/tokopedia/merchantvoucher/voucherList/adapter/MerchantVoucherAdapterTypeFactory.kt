package com.tokopedia.merchantvoucher.voucherList.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringListViewHolder
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherList.adapter.viewholder.MerchantVoucherViewHolder

/**
 * Created by hendry on 01/10/18.
 */

class MerchantVoucherAdapterTypeFactory(onMerchantVoucherViewListener: MerchantVoucherView.OnMerchantVoucherViewListener,
                                        horizontalLayout: Boolean = false)
    : BaseAdapterTypeFactory() {
    private var onMerchantVoucherViewListener: MerchantVoucherView.OnMerchantVoucherViewListener? = null
    private var horizontalLayout: Boolean = false

    init {
        this.onMerchantVoucherViewListener = onMerchantVoucherViewListener
        this.horizontalLayout = horizontalLayout
    }

    fun type(viewModel: MerchantVoucherViewModel): Int {
        if (horizontalLayout) {
            return MerchantVoucherViewHolder.HORIZONTAL_LAYOUT
        } else {
            return MerchantVoucherViewHolder.LAYOUT
        }
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingShimmeringListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == LoadingShimmeringListViewHolder.LAYOUT) {
            LoadingShimmeringGridViewHolder(parent)
        } else if (type == MerchantVoucherViewHolder.LAYOUT ||
                type == MerchantVoucherViewHolder.HORIZONTAL_LAYOUT) {
            MerchantVoucherViewHolder(parent, onMerchantVoucherViewListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}