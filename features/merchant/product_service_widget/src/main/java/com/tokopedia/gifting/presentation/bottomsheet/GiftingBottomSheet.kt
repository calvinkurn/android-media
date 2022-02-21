package com.tokopedia.gifting.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gifting.di.DaggerGiftingComponent
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetGiftingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GiftingBottomSheet(private val productId: Long) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: GiftingViewModel
    private var binding by autoClearedNullable<BottomsheetGiftingBinding>()
    private val titleAddOn by lazy { binding?.layoutContent?.titleAddOn }
    private val priceAddOn by lazy { binding?.layoutContent?.priceAddOn }
    private val textShopName by lazy { binding?.layoutContent?.textShopName }
    private val textShopLocation by lazy { binding?.layoutContent?.textShopLocation }
    private val iconShop by lazy { binding?.layoutContent?.iconShop }
    private val layoutContent by lazy { binding?.layoutContent?.root }
    private val layoutShimmer by lazy { binding?.layoutShimmer?.root }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        clearContentPadding = true
        overlayClickDismiss = true
        binding = BottomsheetGiftingBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.gifting_title_bottomsheet))
        setTextShopLocationAction()
        initInjector()
        setPageLoading(true)
        bottomSheetHeader.postDelayed({
            setPageLoading(false)
        }, 5000)


        viewModel.getWarehouseId(requireContext())
        viewModel.getWarehouseIdResult.observe(viewLifecycleOwner) { warehouseId ->
            viewModel.getAddOn(productId, warehouseId)
        }
        viewModel.getAddOnByProduct.observe(viewLifecycleOwner) {
            print(it.toString())
        }
        viewModel.errorThrowable.observe(viewLifecycleOwner) {
            print(it.toString())
        }

    }

    private fun setPageLoading(isLoading: Boolean) {
        layoutContent?.isVisible = !isLoading
        layoutShimmer?.isVisible = isLoading
        bottomSheetHeader.isVisible = !isLoading
    }

    private fun setTextShopLocationAction() {
        textShopLocation?.setOnClickListener {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, "https://www.tokopedia.com/help"))
        }
    }

    private fun initInjector() {
        DaggerGiftingComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}