package com.tokopedia.gifting.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.gifting.di.DaggerGiftingComponent
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetGiftingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GiftingBottomSheet(private val productId: Long) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: GiftingViewModel
    private var binding by autoClearedNullable<BottomsheetGiftingBinding>()
    private val titleAddOn by lazy { binding?.titleAddOn }
    private val priceAddOn by lazy { binding?.priceAddOn }
    private val textShopName by lazy { binding?.textShopName }
    private val textShopLocation by lazy { binding?.textShopLocation }
    private val iconShop by lazy { binding?.iconShop }

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
        viewModel.dummy()
    }

    private fun setTextShopLocationAction() {
        textShopLocation?.setOnClickListener {
            //TODO: redirect to location page in webview
        }
    }

    private fun initInjector() {
        DaggerGiftingComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}