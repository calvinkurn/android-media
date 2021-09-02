package com.tokopedia.editshipping.ui.customproductlogistic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.databinding.FragmentCustomProductLogisticBinding
import com.tokopedia.editshipping.di.customproductlogistic.DaggerCustomProductLogisticComponent
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_PRODUCT_ID
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHIPPER_SERVICES
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHOP_ID
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class CustomProductLogisticFragment : BaseDaggerFragment(), CPLItemAdapter.CPLItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CustomProductLogisticViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CustomProductLogisticViewModel::class.java)
    }

    private val cplItemOnDemandAdapter by lazy { CPLItemAdapter(this) }
    private val cplItemConventionalAdapter by lazy { CPLItemAdapter(this) }

    private var shopId: Long = 0
    private var productId: String = ""

    private var binding by autoCleared<FragmentCustomProductLogisticBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerCustomProductLogisticComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopId = it.getLong(EXTRA_SHOP_ID)
            productId = it.getString(EXTRA_PRODUCT_ID) ?: ""
        }
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
        binding.swipeRefresh.isRefreshing = true
//        viewModel.getCPLList(2649340, "1685435966")
        viewModel.getCPLList(shopId, productId)

        binding.btnSaveShipper.setOnClickListener { validateSaveButton() }
    }

    private fun initAdapter() {
        binding.apply {
            rvOnDemandCpl.adapter = cplItemOnDemandAdapter
            rvOnDemandCpl.layoutManager = LinearLayoutManager(context)
            rvConventionalCpl.adapter = cplItemConventionalAdapter
            rvConventionalCpl.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObserver() {
        viewModel.cplList.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    populateView(it.data)
                }
                else -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.shippingEditorLayout.gone()
                    binding.btnSaveShipper.gone()
                }
            }
        })
    }

    private fun populateView(data: CustomProductLogisticModel) {
        updateShipperData(data)
        binding.swipeRefresh.isRefreshing = false
        binding.shippingEditorLayout.visible()
        binding.btnSaveShipper.visible()
        binding.globalError.gone()
        if (cplItemOnDemandAdapter.cplItem.isEmpty()) {
            //set empty here
        }
    }

    private fun updateShipperData(data: CustomProductLogisticModel) {
        cplItemOnDemandAdapter.addData(data.shipperList[0].shipper)
        cplItemOnDemandAdapter.setProductIdsActivated(data.cplProduct[0])
        cplItemConventionalAdapter.addData(data.shipperList[1].shipper)
        cplItemConventionalAdapter.setProductIdsActivated(data.cplProduct[0])
    }

    private fun validateSaveButton() {
        val activatedSpIds = getListActivatedSpIds(cplItemOnDemandAdapter.getActivateSpIds(), cplItemConventionalAdapter.getActivateSpIds())
        if (activatedSpIds.isEmpty()) {
            Toaster.build(
                requireView(),
                getString(R.string.toaster_cpl_error),
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
            binding.btnSaveShipper.isEnabled = false
        } else {
            finishActivity(activatedSpIds)
        }
    }

    private fun getListActivatedSpIds(onDemandList: List<Int>, conventionalList: List<Int>): List<Int> {
        val activatedListShipperIds = mutableListOf<Int>()
        activatedListShipperIds.addAll(onDemandList)
        activatedListShipperIds.addAll(conventionalList)
        return activatedListShipperIds
    }

    private fun finishActivity(shipperServices: List<Int>) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putIntegerArrayListExtra(EXTRA_SHIPPER_SERVICES, ArrayList(shipperServices))
            })
            finish()
        }
    }

    companion object {
        fun newInstance(extra: Bundle): CustomProductLogisticFragment {
            return CustomProductLogisticFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_SHOP_ID, extra.getLong(EXTRA_SHOP_ID))
                    putString(EXTRA_PRODUCT_ID, extra.getString(EXTRA_SHOP_ID))
                }
            }
        }
    }

    override fun onCheckboxItemClicked() {
        binding.btnSaveShipper.isEnabled = true
    }

}