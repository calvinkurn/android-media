package com.tokopedia.vouchercreation.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.adapter.MerchantVoucherTargetAdapter
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetAdapterTypeFactory
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel
import kotlinx.android.synthetic.main.fragment_merchant_voucher_target.*

class MerchantVoucherTargetFragment(onNextInvoker: () -> Unit = {}) : BaseCreateMerchantVoucherFragment<VoucherTargetTypeFactory, VoucherTargetAdapterTypeFactory>(onNextInvoker) {

    companion object {
        @JvmStatic
        fun createInstance(onNext: () -> Unit) = MerchantVoucherTargetFragment(onNext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merchant_voucher_target, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun getAdapterTypeFactory(): VoucherTargetAdapterTypeFactory = VoucherTargetAdapterTypeFactory()

    override fun onItemClicked(t: Visitable<VoucherTargetTypeFactory>?) {}

    override fun loadData(page: Int) {}

    private fun setupView() {
        renderList(listOf(VoucherTargetUiModel()))
        voucherTargetRecyclerView?.adapter = MerchantVoucherTargetAdapter(::onRequestNotify)
        voucherTargetRecyclerView?.layoutManager = LinearLayoutManager(context)
        nextButton?.setOnClickListener {
            onNext()
        }
    }

    private fun onRequestNotify() {
        voucherTargetRecyclerView?.run {
            post {
                adapter?.notifyDataSetChanged()
            }
        }
    }
}