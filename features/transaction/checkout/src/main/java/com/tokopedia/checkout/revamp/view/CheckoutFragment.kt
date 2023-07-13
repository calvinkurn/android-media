package com.tokopedia.checkout.revamp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.databinding.FragmentCheckoutBinding
import com.tokopedia.checkout.databinding.HeaderCheckoutBinding
import com.tokopedia.checkout.revamp.di.CheckoutModule
import com.tokopedia.checkout.revamp.di.DaggerCheckoutComponent
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapter
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.adapter.CheckoutDiffUtilCallback
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import com.tokopedia.purchase_platform.common.utils.animateGone
import com.tokopedia.purchase_platform.common.utils.animateShow
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class CheckoutFragment : BaseDaggerFragment(), CheckoutAdapterListener, UploadPrescriptionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheckoutViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CheckoutViewModel::class.java]
    }

    private var binding by autoCleared<FragmentCheckoutBinding>()

    private var header by autoCleared<HeaderCheckoutBinding>()

    private val adapter: CheckoutAdapter = CheckoutAdapter(this, this)

    private val isPlusSelected: Boolean
        get() = arguments?.getBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, false) ?: false

    private val deviceId: String
        get() = if (arguments?.getString(ShipmentFormRequest.EXTRA_DEVICE_ID) != null) {
            arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID)!!
        } else {
            ""
        }

    private val isOneClickShipment: Boolean
        get() = arguments != null && arguments!!.getBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT)

    private val checkoutLeasingId: String
        get() {
            var leasingId = "0"
            if (arguments != null && arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID) != null &&
                !arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID)
                    .equals("null", ignoreCase = true)
            ) {
                leasingId = arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID)!!
            }
            return leasingId
        }

    private val isTradeIn: Boolean
        get() = arguments != null && arguments!!.getString(
            ShipmentFormRequest.EXTRA_DEVICE_ID,
            ""
        ) != null && arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "").isNotEmpty()

    private val checkoutPageSource: String
        get() {
            var pageSource: String = CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
            if (arguments != null && arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE) != null) {
                pageSource = arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE)!!
            }
            return pageSource
        }

    override fun getScreenName(): String {
        return if (isOneClickShipment) {
            ConstantTransactionAnalytics.ScreenName.ONE_CLICK_SHIPMENT
        } else {
            ConstantTransactionAnalytics.ScreenName.CHECKOUT
        }
    }

    override fun initInjector() {
        if (activity != null) {
            val baseMainApplication = activity!!.application as BaseMainApplication
            DaggerCheckoutComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .checkoutModule(CheckoutModule(this))
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.findViewById<View>(com.tokopedia.abstraction.R.id.toolbar)?.isVisible = false
        binding = FragmentCheckoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerCheckout.setNavigationOnClickListener {
            onBackPressed()
        }
        header = HeaderCheckoutBinding.inflate(LayoutInflater.from(context))
        header.tvCheckoutHeaderAddressHeader.isVisible = false
        header.tvCheckoutHeaderAddressName.isVisible = false
        binding.headerCheckout.customView(header.root)

        binding.rvCheckout.adapter = adapter
        binding.rvCheckout.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.tvCheckoutTesting.setOnClickListener {
            if (header.tvCheckoutHeaderText.isVisible) {
                header.tvCheckoutHeaderText.animateGone()
                header.tvCheckoutHeaderAddressHeader.animateShow()
                header.tvCheckoutHeaderAddressName.animateShow()
            } else {
                header.tvCheckoutHeaderText.animateShow()
                header.tvCheckoutHeaderAddressHeader.animateGone()
                header.tvCheckoutHeaderAddressName.animateGone()
            }
            viewModel.test()
        }

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isOneClickShipment = isOneClickShipment
        viewModel.isTradeIn = isTradeIn
        viewModel.deviceId = deviceId
        viewModel.checkoutLeasingId = checkoutLeasingId
        viewModel.isPlusSelected = isPlusSelected
        observeData()

        viewModel.loadSAF(
            skipUpdateOnboardingState = true,
            isReloadData = false,
            isReloadAfterPriceChangeHigher = false
        )
    }

    private fun observeData() {
        viewModel.listData.observe(viewLifecycleOwner) {
            val diffResult = DiffUtil.calculateDiff(CheckoutDiffUtilCallback(it, adapter.list))
            adapter.list = it
            diffResult.dispatchUpdatesTo(adapter)
        }

        viewModel.pageState.observe(viewLifecycleOwner) {
            when (it) {
                is CheckoutPageState.CacheExpired -> {
                }

                is CheckoutPageState.CheckNoAddress -> {
                }

                CheckoutPageState.EmptyData -> {
                }

                is CheckoutPageState.Error -> {
                    var errorMessage = it.throwable.message ?: ""
                    if (!(it.throwable is CartResponseErrorException || it.throwable is AkamaiErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    }
                    if (errorMessage.isEmpty()) {
                        errorMessage =
                            getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message)
                    }
                    Toaster.build(binding.root, errorMessage, type = Toaster.TYPE_ERROR).show()
                }

                CheckoutPageState.Loading -> {
                }

                is CheckoutPageState.NoAddress -> {
                }

                is CheckoutPageState.NoMatchedAddress -> {
                }

                is CheckoutPageState.Success -> {
                    binding.rvCheckout.isVisible = true
                }
            }
        }
    }

    fun onBackPressed(): Boolean {
        activity?.finish()
        return true
    }

    companion object {

        fun newInstance(
            isOneClickShipment: Boolean,
            leasingId: String,
            pageSource: String,
            isPlusSelected: Boolean,
            bundle: Bundle?
        ): CheckoutFragment {
            val b = bundle ?: Bundle()
            b.putString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID, leasingId)
            if (leasingId.isNotEmpty()) {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, true)
            } else {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment)
            }
            b.putString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE, pageSource)
            b.putBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, isPlusSelected)
            val checkoutFragment = CheckoutFragment()
            checkoutFragment.arguments = b
            return checkoutFragment
        }
    }

    // region adapter listener

    override fun onChangeAddress() {
        TODO("Not yet implemented")
    }

    override fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        // todo
    }

    override fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        TODO("Not yet implemented")
    }

    override fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        TODO("Not yet implemented")
    }

    override fun onViewFreeShippingPlusBadge() {
        // todo
    }

    override fun uploadPrescriptionAction(
        uploadPrescriptionUiModel: UploadPrescriptionUiModel,
        buttonText: String,
        buttonNotes: String
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickAddOnProductLevel(
        product: CheckoutProductModel,
        addOnWording: AddOnGiftingWordingModel
    ) {
        TODO("Not yet implemented")
    }

    override fun onImpressionAddOnProductLevel(productId: String) {
        TODO("Not yet implemented")
    }

    // endregion
}
