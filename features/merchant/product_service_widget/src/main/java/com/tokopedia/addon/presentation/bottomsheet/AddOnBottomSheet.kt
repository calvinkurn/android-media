package com.tokopedia.addon.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.addon.di.DaggerAddOnComponent
import com.tokopedia.addon.presentation.viewmodel.AddOnViewModel
import com.tokopedia.product_service_widget.databinding.BottomsheetGiftingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.product_service_widget.R

class AddOnBottomSheet(private val addOnId: String) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: AddOnViewModel
    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<BottomsheetGiftingBinding>()
    private val titleTips by lazy { binding?.layoutContent?.titleTips }
    private val titleAddOn by lazy { binding?.layoutContent?.titleAddOn }
    private val priceAddOn by lazy { binding?.layoutContent?.priceAddOn }
    private val imageAddOn by lazy { binding?.layoutContent?.imageAddOn }
    private val textShopName by lazy { binding?.layoutContent?.textShopName }
    private val textShopLocation by lazy { binding?.layoutContent?.textShopLocation }
    private val textCaptionShopName by lazy { binding?.layoutContent?.textCaptionShopName }
    private val textCaptionPromo by lazy { binding?.layoutContent?.textCaptionPromo }
    private val iconPromo by lazy { binding?.layoutContent?.iconPromo }
    private val layoutContent by lazy { binding?.layoutContent?.root }
    private val layoutShimmer by lazy { binding?.layoutShimmer?.root }

    private var shopTier: Long? = null
    private var shopIdDisplayed: String = ""

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

        observeGetAddOnByProduct()
        observeIsTokoCabang()

        viewModel.getAddOn(addOnId)
    }

    private fun initInjector() {
        DaggerAddOnComponent.builder()
            .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun observeGetAddOnByProduct() {
        viewModel.getAddOnResult.observe(viewLifecycleOwner) {
            shopTier = it.addOnByIDResponse.firstOrNull()?.shop?.shopTier
            shopIdDisplayed = it.addOnByIDResponse.firstOrNull()?.basic?.shopID.orEmpty()
        }
    }

    private fun observeIsTokoCabang() {
        viewModel.isTokoCabang.observe(viewLifecycleOwner) {
            if (it) {
                titleTips?.text = getString(R.string.gifting_title_tips_tokocabang)
                textCaptionShopName?.text = getString(R.string.gifting_caption_shop_name_tokocabang)
                textShopName?.text = ""
            }
        }
    }
}
