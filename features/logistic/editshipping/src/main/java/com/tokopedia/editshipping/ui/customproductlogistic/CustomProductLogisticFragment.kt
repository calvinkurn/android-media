package com.tokopedia.editshipping.ui.customproductlogistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.editshipping.databinding.FragmentCustomProductLogisticBinding
import com.tokopedia.editshipping.di.customproductlogistic.DaggerCustomProductLogisticComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class CustomProductLogisticFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CustomProductLogisticViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CustomProductLogisticViewModel::class.java)
    }

    private val cplItemAdapter by lazy { CPLItemAdapter() }

    private var binding by autoCleared<FragmentCustomProductLogisticBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerCustomProductLogisticComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomProductLogisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initAdapter()
        initObserver()
    }

    private fun initViews() {
        viewModel.getCPLList(0, "123")
    }

    private fun initAdapter() {
        binding.apply {
            rvOnDemandCpl.adapter = cplItemAdapter
            rvOnDemandCpl.layoutManager = LinearLayoutManager(context)
            rvConventionalCpl.adapter = cplItemAdapter
            rvConventionalCpl.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObserver() {
        viewModel.cplList.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    populateView(it.data)
                }
            }
        })
    }

    private fun populateView(data: CustomProductLogisticModel) {
        binding.swipeRefresh.isRefreshing = false
        binding.shippingEditorLayout.visible()
        binding.btnSaveShipper.visible()
        binding.globalError.gone()
        updateShipperData(data)
    }

    private fun updateShipperData(data: CustomProductLogisticModel) {
        /*on demand only*/
        cplItemAdapter.addData(data.shipperList[0].shipper)
    }

}