package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.description.DescriptionBottomSheet
import com.tokopedia.vouchercreation.detail.model.VoucherDetailUiModel
import com.tokopedia.vouchercreation.detail.view.VoucherDetailListener
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 09/05/20
 */

abstract class BaseDetailFragment : BaseListFragment<VoucherDetailUiModel, VoucherDetailAdapterFactoryImpl>(),
        VoucherDetailListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_detail, container, false)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvMvcVoucherDetail

    override fun getAdapterTypeFactory(): VoucherDetailAdapterFactoryImpl {
        return VoucherDetailAdapterFactoryImpl(this)
    }

    override fun onItemClicked(t: VoucherDetailUiModel?) {

    }

    override fun onFooterButtonClickListener() {

    }

    override fun showTipsAndTrickBottomSheet() {

    }

    override fun onFooterCtaTextClickListener() {

    }

    override fun showDescriptionBottomSheet(title: String, content: String) {
        if (!isAdded) return
        DescriptionBottomSheet(context ?: return)
                .show(title, content, childFragmentManager)
    }
}