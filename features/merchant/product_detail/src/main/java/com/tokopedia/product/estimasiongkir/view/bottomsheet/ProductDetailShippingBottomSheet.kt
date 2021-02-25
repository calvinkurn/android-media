package com.tokopedia.product.estimasiongkir.view.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.util.ProductSeparatorItemDecoration
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingShimmerDataModel
import com.tokopedia.product.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.product.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.estimasiongkir.view.adapter.ProductDetailShippingAdapter
import com.tokopedia.product.estimasiongkir.view.adapter.ProductDetailShippingDIffutil
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactoryImpl
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationBoeViewModel
import javax.inject.Inject


/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductDetailShippingBottomSheet : BottomSheetDialogFragment(), ProductDetailShippingListener, ChooseAddressWidget.ChooseAddressWidgetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: RatesEstimationBoeViewModel? = null
    private var adapter: ProductDetailShippingAdapter? = null
    private var rv: RecyclerView? = null
    private var viewContainer: View? = null
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
    }
    private var shouldRefresh: Boolean = false

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        viewContainer = View.inflate(context, R.layout.bs_product_shipping_rate_estimate, null)
        setupBottomSheet(dialog, viewContainer)

        dialog.run {
            viewContainer?.let {
                setupRecyclerView(it)
                setContentView(it)
            }

            initInjector()
            initViewModel()
            observeData()
        }
    }

    private fun setupBottomSheet(dialog: Dialog, view: View?) {
        val closeBtn = view?.findViewById<IconUnify>(R.id.shipment_bottom_sheet_close)
        closeBtn?.setOnClickListener {
            dismiss()
        }
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
        show(childFragmentManager, "shipping_bs")
    }

    private fun observeData() {
        sharedViewModel.rateEstimateRequest.observeOnce(this) {
            viewModel?.setRatesRequest(it)
        }

        viewModel?.ratesVisitableResult?.observe(this) {
            it.doSuccessOrFail({
                adapter?.submitList(it.data)
            }) {
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        rv = view.findViewById(R.id.rv_product_shipping)
        if (rv?.itemDecorationCount == 0 && context != null) {
            rv?.addItemDecoration(ProductSeparatorItemDecoration(requireContext()))
        }
        rv?.itemAnimator = null
        val asyncDiffer = AsyncDifferConfig.Builder(ProductDetailShippingDIffutil())
                .build()

        adapter = ProductDetailShippingAdapter(asyncDiffer, ProductShippingFactoryImpl(this, this))

        rv?.adapter = adapter
        showShimmerPage()
    }

    private fun showShimmerPage(height: Int = 0) {
        adapter?.submitList(listOf(ProductShippingShimmerDataModel(height = height)))
    }

    override fun onChooseAddressClicked() {
        shouldRefresh = true
        dismiss()
//        showShimmerPage(rv?.height ?: 0)
//        viewModel?.setRatesRequest(sharedViewModel.rateEstimateRequest.value?.copy(forceRefresh = true)
//                ?: RatesEstimateRequest())
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

    override fun getLocalizingAddressHostSourceData(): String = "product detail"

    override fun onLocalizingAddressLoginSuccess() {
    }
}