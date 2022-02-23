package com.tokopedia.gifting.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gifting.di.DaggerGiftingComponent
import com.tokopedia.gifting.domain.model.Addon
import com.tokopedia.gifting.domain.model.Basic
import com.tokopedia.gifting.domain.model.Inventory
import com.tokopedia.gifting.domain.model.Shop
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetGiftingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GiftingBottomSheet(private val addOnId: String) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: GiftingViewModel
    private var binding by autoClearedNullable<BottomsheetGiftingBinding>()
    private val titleAddOn by lazy { binding?.layoutContent?.titleAddOn }
    private val priceAddOn by lazy { binding?.layoutContent?.priceAddOn }
    private val imageAddOn by lazy { binding?.layoutContent?.imageAddOn }
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
        initInjector()
        setPageLoading(true)

        observeGetAddOnByProduct()
        observeErrorThrowable()

        viewModel.getAddOn(addOnId)
    }

    private fun initInjector() {
        DaggerGiftingComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeGetAddOnByProduct() {
        viewModel.getAddOnResult.observe(viewLifecycleOwner) {
            setPageLoading(false)
            setTextShopLocationAction(it.staticInfo.infoURL.orEmpty())
            setupPageFromResponseData(it.addOnByIDResponse.firstOrNull())
        }
    }

    private fun setupPageFromResponseData(addon: Addon?) {
        addon?.let {
            setupCardAddOn(it.basic, it.inventory)
            setupShopSection(it.shop)
        }
    }

    private fun setupCardAddOn(basic: Basic, inventory: Inventory) {
        priceAddOn?.text = inventory.price.orZero().getCurrencyFormatted()
        imageAddOn?.loadImageRounded(basic.metadata.pictures.firstOrNull()?.url200.orEmpty())
    }

    private fun setupShopSection(shop: Shop) {
        textShopName?.text = shop.name
    }

    private fun observeErrorThrowable() {
        viewModel.errorThrowable.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(context, it)
            Toaster.build(requireView(), errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).apply {
                anchorView = layoutShimmer
            }.show()
        }
    }

    private fun setPageLoading(isLoading: Boolean) {
        layoutContent?.isVisible = !isLoading
        layoutShimmer?.isVisible = isLoading
        bottomSheetHeader.isVisible = !isLoading
    }

    private fun setTextShopLocationAction(infoUrl: String) {
        textShopLocation?.setOnClickListener {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, infoUrl))
        }
    }
}