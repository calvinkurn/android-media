package com.tokopedia.product.estimasiongkir.view.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.view.ProductDetailCommonBottomSheetBuilder
import com.tokopedia.product.detail.view.util.ProductSeparatorItemDecoration
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingErrorDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingShimmerDataModel
import com.tokopedia.product.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.product.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.estimasiongkir.tracking.SellyTracker
import com.tokopedia.product.estimasiongkir.tracking.SellyTracking
import com.tokopedia.product.estimasiongkir.util.ProductDetailShippingTracking
import com.tokopedia.product.estimasiongkir.view.adapter.ProductDetailShippingAdapter
import com.tokopedia.product.estimasiongkir.view.adapter.ProductDetailShippingDIffutil
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactoryImpl
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationBoeViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductDetailShippingBottomSheet : BottomSheetDialogFragment(), ProductDetailShippingListener, ChooseAddressWidget.ChooseAddressWidgetListener {

    companion object {
        const val TAG_SHIPPING_BOTTOM_SHEET = "TAG_SHIPPING_BOTTOM_SHEET"
        const val SOURCE = "product detail page"

        private const val ARG_BUYER_DISTRICT_ID = "buyer_district_id"
        private const val ARG_SELLER_DISTRICT_ID = "seller_district_id"
        private const val ARG_LAYOUT_ID = "layout_id"

        fun instance(
            buyerDistrictId: String,
            sellerDistrictId: String,
            layoutId: String
        ): ProductDetailShippingBottomSheet {
            return ProductDetailShippingBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_BUYER_DISTRICT_ID, buyerDistrictId)
                    putString(ARG_SELLER_DISTRICT_ID, sellerDistrictId)
                    putString(ARG_LAYOUT_ID, layoutId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private var viewModel: RatesEstimationBoeViewModel? = null
    private var shippingAdapter: ProductDetailShippingAdapter? = null
    private var rv: RecyclerView? = null
    private var viewContainer: View? = null
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
    }
    private var shouldRefresh: Boolean = false

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        setupBottomSheet(dialog)
        initInjector()
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewContainer = View.inflate(context, R.layout.bs_product_shipping_rate_estimate, null)
        viewContainer?.let {
            setupRecyclerView(it)
        }
        val closeBtn = viewContainer?.findViewById<IconUnify>(R.id.shipment_bottom_sheet_close)
        closeBtn?.setOnClickListener {
            dismiss()
        }
        observeData()
        return viewContainer
    }

    private fun setupBottomSheet(dialog: Dialog) {
        val dialogFragment = dialog as BottomSheetDialog
        dialogFragment.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogFragment.behavior.skipCollapsed = true
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (shouldRefresh) {
            sharedViewModel.setAddressChanged(true)
        }
    }

    override fun getTheme(): Int {
        return R.style.ProductBottomSheetDialogTheme
    }

    private fun initInjector() {
        activity?.let {
            DaggerRatesEstimationComponent.builder()
                .baseAppComponent((activity?.application as com.tokopedia.abstraction.base.app.BaseMainApplication).baseAppComponent)
                .ratesEstimationModule(RatesEstimationModule()).build()
                .inject(this)
        }
    }

    private fun initViewModel() {
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProvider(this, viewModelFactory).get(RatesEstimationBoeViewModel::class.java)
        }
    }

    fun show(childFragmentManager: FragmentManager) {
        show(childFragmentManager, TAG_SHIPPING_BOTTOM_SHEET)
    }

    private fun observeData() {
        sharedViewModel.rateEstimateRequest.observeOnce(this) {
            viewModel?.setRatesRequest(it)
        }

        viewModel?.ratesVisitableResult?.observe(this.viewLifecycleOwner) { result ->
            result.doSuccessOrFail({
                shippingAdapter?.submitList(it.data)
            }) { throwable ->
                showError(throwable)
            }
        }
    }

    private fun showError(it: Throwable) {
        val errorType = if (it is SocketTimeoutException || it is UnknownHostException || it is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
        shippingAdapter?.submitList(listOf(ProductShippingErrorDataModel(errorType = errorType)))
    }

    private fun setupRecyclerView(view: View) {
        rv = view.findViewById<RecyclerView?>(R.id.rv_product_shipping).apply {
            if (itemDecorationCount == Int.ZERO && context != null) {
                val decorator = ProductSeparatorItemDecoration(
                    context = requireContext()
                )
                addItemDecoration(decorator)
            }
            itemAnimator = null

            val asyncDiffer = AsyncDifferConfig.Builder(ProductDetailShippingDIffutil()).build()

            shippingAdapter = ProductDetailShippingAdapter(
                asyncDifferConfig = asyncDiffer,
                typeFactory = ProductShippingFactoryImpl(
                    listener = this@ProductDetailShippingBottomSheet,
                    chooseAddressListener = this@ProductDetailShippingBottomSheet
                )
            )

            adapter = shippingAdapter
        }

        showShimmerPage()
    }

    private fun showShimmerPage(height: Int = 0) {
        shippingAdapter?.submitList(listOf(ProductShippingShimmerDataModel(height = height)))
    }

    override fun onChooseAddressClicked() {
        shouldRefresh = true
        dismiss()
    }

    override fun openUspBottomSheet(uspImageUrl: String) {
        context?.let {
            val ratesEstimateRequest = sharedViewModel.rateEstimateRequest.value
            ProductDetailShippingTracking.onPelajariTokoCabangClicked(
                ratesEstimateRequest?.userId
                    ?: ""
            )
            if (ratesEstimateRequest?.isTokoNow == true) {
                RouteManager.route(context, EDUCATIONAL_INFO)
            } else {
                val bottomSheet = ProductDetailCommonBottomSheetBuilder.getUspBottomSheet(it, uspImageUrl)
                bottomSheet.show(childFragmentManager, ProductDetailCommonBottomSheetBuilder.TAG_USP_BOTTOM_SHEET)
            }
        }
    }

    override fun refreshPage(height: Int) {
        sharedViewModel.rateEstimateRequest.value?.let {
            showShimmerPage(height)
            viewModel?.setRatesRequest(it.copy(forceRefresh = true))
        }
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        onChooseAddressClicked()
        shouldRefresh = true
    }

    override fun onLocalizingAddressUpdatedFromBackground() {
    }

    override fun onLocalizingAddressServerDown() {
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
    }

    override fun getLocalizingAddressHostFragment(): Fragment = this

    override fun getLocalizingAddressHostSourceData(): String = ProductDetailCommonConstant.KEY_PRODUCT_DETAIL

    override fun getLocalizingAddressHostSourceTrackingData(): String = SOURCE

    override fun onLocalizingAddressLoginSuccess() {
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }

    override fun impressScheduledDelivery(prices: List<Pair<String, String>>, date: String) {
        val buyerDistrictId = arguments?.getString(ARG_BUYER_DISTRICT_ID) ?: ""
        val sellerDistrictId = arguments?.getString(ARG_SELLER_DISTRICT_ID) ?: ""
        val layoutId = arguments?.getString(ARG_LAYOUT_ID) ?: ""
        val beratSatuan = sharedViewModel.rateEstimateRequest.value?.productWeight?.toString() ?: ""
        val productId = sharedViewModel.rateEstimateRequest.value?.productId ?: ""
        val shopId = sharedViewModel.rateEstimateRequest.value?.shopId ?: ""
        val userId = sharedViewModel.rateEstimateRequest.value?.userId ?: ""

        val data = SellyTracker.ImpressionComponent(
            buyerDistrictId = buyerDistrictId,
            sellerDistrictId = sellerDistrictId,
            prices = prices,
            layoutId = layoutId,
            beratSatuan = beratSatuan,
            productId = productId,
            shopId = shopId,
            userId = userId
        )

        SellyTracking.impressScheduledDelivery(
            trackingQueue,
            data
        )
    }
}
