package com.tokopedia.mvc.presentation.list.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.presentation.list.adapter.VouchersAdapter
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MvcListFragment: BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl() {

    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()
    @Inject
    lateinit var viewModel: MvcListViewModel

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentMvcListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        viewModel.dummy()
    }

    private fun SmvcFragmentMvcListBinding.setupView() {
        rvVoucher.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvVoucher.adapter = VouchersAdapter().apply {
            setDataList(List(100) {
                it.toString() + it.toString()
            })
        }
        val config = HasPaginatedList.Config(
            pageSize = 100,
            onLoadNextPage = {
                //flashSaleAdapter.addItem(LoadingItem)
            }, onLoadNextPageFinished = {
                //flashSaleAdapter.removeItem(LoadingItem)
            })
        attachPaging(rvVoucher, config, ::getDataList)
    }

    private fun getDataList(page: Int, pageSize: Int) {
        val aaa = binding?.rvVoucher?.adapter as? VouchersAdapter
        aaa?.addDataList(List(pageSize) {
            it.toString() + it.toString()
        })
        notifyLoadResult(true)
    }
}
