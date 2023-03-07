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
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_CPL_ACTIVATED
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_PRODUCT_ID
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHIPPER_SERVICES
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHOP_ID
import com.tokopedia.editshipping.util.EditShippingConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    private var productId: Long = 0
    private var isCPLActivated: Boolean = false
    private var shipperServicesIds: List<Long>? = null

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
            productId = it.getLong(EXTRA_PRODUCT_ID)
            isCPLActivated = it.getBoolean(EXTRA_CPL_ACTIVATED)
            shipperServicesIds = it.getExtraShipperServices()
        }
    }

    private fun Bundle.getExtraShipperServices(): List<Long>? {
        return getIntegerArrayList(EXTRA_SHIPPER_SERVICES)?.takeIf { it.isNotEmpty() }?.map { it.toLong() }
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
        viewModel.getCPLList(shopId, productId.toString(), shipperServicesIds)

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
                is Fail -> {
                    binding.swipeRefresh.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }
                else -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.shippingEditorLayoutOndemand.gone()
                    binding.shippingEditorLayoutConventional.gone()
                    binding.btnSaveShipper.gone()
                }
            }
        })
    }

    private fun populateView(data: CustomProductLogisticModel) {
        updateShipperData(data)
        binding.swipeRefresh.isRefreshing = false
        binding.svShippingEditor.visible()
        binding.btnSaveShipper.visible()
        binding.globalError.gone()
        if (cplItemOnDemandAdapter.getShownShippers().isNotEmpty()) {
            binding.shippingEditorLayoutOndemand.visible()
        }

        if (cplItemConventionalAdapter.getShownShippers().isNotEmpty()) {
            binding.shippingEditorLayoutConventional.visible()
        }
    }

    private fun updateShipperData(data: CustomProductLogisticModel) {
        if (isCPLActivated) {
            if (data.shipperList.size == 1 && data.shipperList[0].header == ON_DEMAND_VALIDATION) {
                populateShipperData(data, SHIPPER_ON_DEMAND)
                cplItemOnDemandAdapter.setAllProductIdsActivated()
            } else if (data.shipperList.size == 1 && data.shipperList[0].header == CONVENTIONAL_VALIDATION) {
                populateShipperData(data, SHIPPER_CONVENTIONAL)
                cplItemConventionalAdapter.setAllProductIdsActivated()
            } else {
                populateShipperData(data, ALL_SHIPPER_AVAILABLE)
                cplItemOnDemandAdapter.setAllProductIdsActivated()
                cplItemConventionalAdapter.setAllProductIdsActivated()
            }
        } else {
            if (data.shipperList.size == 1 && data.shipperList[0].header == ON_DEMAND_VALIDATION) {
                populateShipperData(data, SHIPPER_ON_DEMAND)
                cplItemOnDemandAdapter.setProductIdsActivated(data.cplProduct[0])
            } else if (data.shipperList.size == 1 && data.shipperList[0].header == CONVENTIONAL_VALIDATION) {
                populateShipperData(data, SHIPPER_CONVENTIONAL)
                cplItemConventionalAdapter.setProductIdsActivated(data.cplProduct[0])
            } else {
                populateShipperData(data, ALL_SHIPPER_AVAILABLE)
                cplItemOnDemandAdapter.setProductIdsActivated(data.cplProduct[0])
                cplItemConventionalAdapter.setProductIdsActivated(data.cplProduct[0])
            }
        }
    }

    private fun populateShipperData(data: CustomProductLogisticModel, shipperCase: Int) {
        when (shipperCase) {
            SHIPPER_ON_DEMAND -> {
                cplItemOnDemandAdapter.addData(data.shipperList[0].shipper)
            }
            SHIPPER_CONVENTIONAL -> {
                cplItemConventionalAdapter.addData(data.shipperList[0].shipper)
            }
            else -> {
                cplItemOnDemandAdapter.addData(data.shipperList[0].shipper)
                cplItemConventionalAdapter.addData(data.shipperList[1].shipper)
            }
        }
    }

    private fun validateSaveButton() {
        val activatedSpIds = getListActivatedSpIds(
            cplItemOnDemandAdapter.getActivateSpIds(),
            cplItemConventionalAdapter.getActivateSpIds()
        )
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

    private fun getListActivatedSpIds(
        onDemandList: List<Int>,
        conventionalList: List<Int>
    ): List<Int> {
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

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(
                                it,
                                DEFAULT_ERROR_MESSAGE,
                                Toaster.LENGTH_SHORT,
                                type = Toaster.TYPE_ERROR
                            ).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(
                        it,
                        throwable.message
                            ?: DEFAULT_ERROR_MESSAGE,
                        Toaster.LENGTH_SHORT,
                        type = Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding.globalError.setType(type)
        binding.globalError.setActionClickListener {
            context?.let {
                viewModel.getCPLList(shopId, productId.toString(), shipperServicesIds)
            }
        }
        binding.globalError.visible()
        binding.svShippingEditor.gone()
        binding.btnSaveShipper.gone()
    }


    companion object {
        const val SHIPPER_ON_DEMAND = 1
        const val SHIPPER_CONVENTIONAL = 2
        const val ALL_SHIPPER_AVAILABLE = 3
        const val ON_DEMAND_VALIDATION = "Dijemput Kurir"
        const val CONVENTIONAL_VALIDATION = "Antar ke Kantor Agen"

        fun newInstance(extra: Bundle): CustomProductLogisticFragment {
            return CustomProductLogisticFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_SHOP_ID, extra.getLong(EXTRA_SHOP_ID))
                    putLong(EXTRA_PRODUCT_ID, extra.getLong(EXTRA_PRODUCT_ID))
                    putBoolean(EXTRA_CPL_ACTIVATED, extra.getBoolean(EXTRA_CPL_ACTIVATED))
                    putIntegerArrayList(EXTRA_SHIPPER_SERVICES, extra.getIntegerArrayList(EXTRA_SHIPPER_SERVICES))
                }
            }
        }
    }

    override fun onCheckboxItemClicked() {
        binding.btnSaveShipper.isEnabled = true
    }

}