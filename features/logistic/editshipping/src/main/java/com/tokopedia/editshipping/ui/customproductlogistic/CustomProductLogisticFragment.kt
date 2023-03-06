package com.tokopedia.editshipping.ui.customproductlogistic

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.databinding.FragmentCustomProductLogisticBinding
import com.tokopedia.editshipping.di.customproductlogistic.DaggerCustomProductLogisticComponent
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.CPL_CONVENTIONAL_INDEX
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.CPL_ON_DEMAND_INDEX
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_CPL_ACTIVATED
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_CPL_PARAM
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_PRODUCT_ID
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHIPPER_SERVICES
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHOP_ID
import com.tokopedia.editshipping.util.CustomProductLogisticConstant.EXTRA_SHOW_ONBOARDING_CPL
import com.tokopedia.editshipping.util.EditShippingConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.logisticCommon.data.model.ShipperCPLModel
import com.tokopedia.unifycomponents.Toaster
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
    private var productId: Long? = null
    private var shipperServicesIds: List<Long>? = null
    private var cplParam: List<Long>? = null

    private var whitelabelCoachmark: CoachMark2? = null

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
            if (it.containsKey(EXTRA_PRODUCT_ID)) {
                productId = it.getLong(EXTRA_PRODUCT_ID)
            }
            shipperServicesIds = it.getExtraShipperServices(EXTRA_SHIPPER_SERVICES)
            cplParam = it.getExtraShipperServices(EXTRA_CPL_PARAM)
        }
    }

    private fun Bundle.getExtraShipperServices(key: String): List<Long>? {
        return getLongArray(key)?.takeIf { it.isNotEmpty() }?.toList()
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
        initData()
        initAdapter()
        initObserver()
    }

    override fun onPause() {
        whitelabelCoachmark?.dismissCoachMark()
        whitelabelCoachmark = null

        super.onPause()
    }

    private fun initData() {
        val shouldShowOnBoarding = arguments?.getBoolean(EXTRA_SHOW_ONBOARDING_CPL, false) ?: false
        viewModel.getCPLList(shopId, productId, shipperServicesIds, cplParam, shouldShowOnBoarding)
    }

    private fun getWhitelabelView(): View? {
        val whitelabelServiceIndex = cplItemOnDemandAdapter.getWhitelabelServicePosition()
        return if (whitelabelServiceIndex != RecyclerView.NO_POSITION) {
            binding.rvOnDemandCpl.findViewHolderForAdapterPosition(whitelabelServiceIndex)?.itemView
        } else {
            null
        }
    }

    private fun getNormalServiceView(): View? {
        val normalServiceIndex = cplItemOnDemandAdapter.getFirstNormalServicePosition()
        return if (normalServiceIndex != RecyclerView.NO_POSITION) {
            binding.rvOnDemandCpl.findViewHolderForAdapterPosition(normalServiceIndex)?.itemView
        } else {
            null
        }
    }

    private fun showOnBoardingCoachmark(data: CustomProductLogisticModel) {
        val whitelabelView = getWhitelabelView()

        if (whitelabelView != null) {
            context?.let {
                val normalServiceView = getNormalServiceView()

                val coachMarkItems = generateOnBoardingCoachMark(it, normalServiceView, whitelabelView)
                whitelabelCoachmark = CoachMark2(it).apply {
                    setOnBoardingListener(coachMarkItems, data)
                    setStateAfterOnBoardingShown()
                    manualScroll(coachMarkItems)
                }
            }
        }
    }

    private fun generateOnBoardingCoachMark(
        context: Context,
        normalService: View?,
        whitelabelService: View
    ): ArrayList<CoachMark2Item> {
        val coachMarkItems = ArrayList<CoachMark2Item>()
        // dummy only
        coachMarkItems.add(
            CoachMark2Item(
                binding.tvAntarCpl,
                context.getString(R.string.whitelabel_instan_title_coachmark),
                context.getString(R.string.whitelabel_instan_description_coachmark)
            )
        )

        normalService?.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    view,
                    context.getString(R.string.whitelabel_onboarding_title_coachmark),
                    context.getString(R.string.whitelabel_onboarding_description_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        }

        whitelabelService.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    view,
                    context.getString(R.string.whitelabel_instan_title_coachmark),
                    context.getString(R.string.whitelabel_instan_description_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        }
        return coachMarkItems
    }

    private fun CoachMark2.setOnBoardingListener(coachMarkItems: ArrayList<CoachMark2Item>, data: CustomProductLogisticModel) {
        this.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                if (currentIndex < 1) {
                    this@setOnBoardingListener.dismissCoachMark()
                    finishActivity(data.getActivatedSpIds())
                } else {
                    this@setOnBoardingListener.hideCoachMark()
                    manualScroll(coachMarkItems, currentIndex)
                }
            }
        })
    }

    private fun CoachMark2.manualScroll(coachMarkItems: ArrayList<CoachMark2Item>, currentIndex: Int = 1) {
        coachMarkItems.getOrNull(currentIndex)?.anchorView?.let { rv ->
            binding.svShippingEditor.smoothScrollTo(0, rv.top)
            this.showCoachMark(coachMarkItems, null, currentIndex)
        }
    }

    private fun CoachMark2.setStateAfterOnBoardingShown() {
        this.onFinishListener = {
            viewModel.setAlreadyShowOnBoarding()
        }
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
        viewModel.cplState.observe(viewLifecycleOwner, {
            when (it) {
                is CPLState.FirstLoad -> {
                    populateView(it.data)
                }
                is CPLState.Failed -> {
                    binding.swipeRefresh.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }
                is CPLState.Update -> {
                    if (it.shipperGroup == ON_DEMAND_VALIDATION) {
                        cplItemOnDemandAdapter.modifyData(it.shipper)
                    } else if (it.shipperGroup == CONVENTIONAL_VALIDATION) {
                        cplItemConventionalAdapter.modifyData(it.shipper)
                    }
                }
                is CPLState.Loading -> {
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
        if (data.shouldShowOnBoarding) {
            Handler(Looper.getMainLooper()).postDelayed({
                showOnBoardingCoachmark(data)
            }, COACHMARK_ON_BOARDING_DELAY)
        }
        binding.btnSaveShipper.setOnClickListener { validateSaveButton() }
    }

    private fun updateShipperData(data: CustomProductLogisticModel) {
        if (data.shipperList.size == 1 && data.shipperList[0].header == ON_DEMAND_VALIDATION) {
            populateShipperData(data.shipperList[0].shipper, SHIPPER_ON_DEMAND)
        } else if (data.shipperList.size == 1 && data.shipperList[0].header == CONVENTIONAL_VALIDATION) {
            populateShipperData(data.shipperList[0].shipper, SHIPPER_CONVENTIONAL)
        } else if (data.shipperList.size > 1) {
            populateShipperData(data.shipperList[CPL_ON_DEMAND_INDEX].shipper, SHIPPER_ON_DEMAND)
            populateShipperData(
                data.shipperList[CPL_CONVENTIONAL_INDEX].shipper,
                SHIPPER_CONVENTIONAL
            )
        }
    }

    private fun populateShipperData(data: List<ShipperCPLModel>, shipperCase: Int) {
        when (shipperCase) {
            SHIPPER_ON_DEMAND -> {
                if (data.isEmpty()) {
                    binding.shippingEditorLayoutOndemand.gone()
                } else {
                    binding.shippingEditorLayoutOndemand.visible()
                    cplItemOnDemandAdapter.addData(data)
                }
            }
            SHIPPER_CONVENTIONAL -> {
                if (data.isEmpty()) {
                    binding.shippingEditorLayoutConventional.gone()
                } else {
                    binding.shippingEditorLayoutConventional.visible()
                    cplItemConventionalAdapter.addData(data)
                }
            }
        }
    }

    private fun validateSaveButton() {
        val activatedSpIds = viewModel.cplData.getActivatedSpIds()
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

    private fun finishActivity(shipperServices: List<Long>) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_SHIPPER_SERVICES, shipperServices.toLongArray())
                }
            )
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
            initData()
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
        private const val COACHMARK_ON_BOARDING_DELAY = 1000L

        fun newInstance(extra: Bundle): CustomProductLogisticFragment {
            return CustomProductLogisticFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_SHOP_ID, extra.getLong(EXTRA_SHOP_ID))
                    putLong(EXTRA_PRODUCT_ID, extra.getLong(EXTRA_PRODUCT_ID))
                    putBoolean(EXTRA_CPL_ACTIVATED, extra.getBoolean(EXTRA_CPL_ACTIVATED))
                    putBoolean(
                        EXTRA_SHOW_ONBOARDING_CPL,
                        extra.getBoolean(EXTRA_SHOW_ONBOARDING_CPL)
                    )
                    putLongArray(EXTRA_SHIPPER_SERVICES, extra.getLongArray(EXTRA_SHIPPER_SERVICES))
                    putLongArray(EXTRA_CPL_PARAM, extra.getLongArray(EXTRA_CPL_PARAM))
                }
            }
        }
    }

    override fun onShipperCheckboxClicked(shipperId: Long, check: Boolean) {
        binding.btnSaveShipper.isEnabled = true
        viewModel.setAllShipperServiceState(check, shipperId)
    }

    override fun onShipperProductCheckboxClicked(spId: Long, check: Boolean) {
        binding.btnSaveShipper.isEnabled = true
        viewModel.setShipperServiceState(check, spId)
    }

    override fun onWhitelabelServiceCheckboxClicked(spIds: List<Long>, check: Boolean) {
        binding.btnSaveShipper.isEnabled = true
        viewModel.setWhitelabelServiceState(spIds, check)
    }
}
