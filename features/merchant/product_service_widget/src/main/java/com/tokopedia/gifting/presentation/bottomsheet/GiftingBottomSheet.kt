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
import com.tokopedia.gifting.presentation.uimodel.AddOnType
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.gifting.tracking.GiftingBottomsheetTracking
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetGiftingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class GiftingBottomSheet(private val addOnId: String) : BottomSheetUnify() {

    @Inject
    lateinit var viewModel: GiftingViewModel
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
        observeIsTokoCabang()

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
            setTextShopLocationAction(it.staticInfo.infoURL)
            setTextPromo(it.staticInfo.promoText)
            setupPageFromResponseData(it.addOnByIDResponse.firstOrNull())
            GiftingBottomsheetTracking.trackPageImpression(
                bottomSheetTitle.text.toString(), userSession.userId, it.addOnByIDResponse)
        }
    }

    private fun observeErrorThrowable() {
        viewModel.errorThrowable.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(context, it)
            Toaster.build(requireView(), errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).apply {
                anchorView = layoutShimmer
            }.show()
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

    private fun setupPageFromResponseData(addon: Addon?) {
        addon?.let {
            setupCardAddOn(it.basic, it.inventory)
            setupShopSection(it.shop)
        }
    }

    private fun setupCardAddOn(basic: Basic, inventory: Inventory) {
        priceAddOn?.text = inventory.price.orZero().getCurrencyFormatted()
        imageAddOn?.loadImageRounded(basic.metadata.pictures.firstOrNull()?.url200.orEmpty())
        titleAddOn?.text = when (basic.type) {
            AddOnType.GREETING_CARD_TYPE.name -> getString(R.string.gifting_greeting_card_text)
            AddOnType.GREETING_CARD_AND_PACKAGING_TYPE.name -> getString(R.string.gifting_greeting_card_and_package_text)
            else -> ""
        }
    }

    private fun setupShopSection(shop: Shop) {
        textShopName?.text = shop.name
    }

    private fun setPageLoading(isLoading: Boolean) {
        layoutContent?.isVisible = !isLoading
        layoutShimmer?.isVisible = isLoading
        bottomSheetHeader.isVisible = !isLoading
    }

    private fun setTextPromo(promoText: String) {
        textCaptionPromo?.text = promoText
        textCaptionPromo?.isVisible = promoText.isNotEmpty()
        iconPromo?.isVisible = promoText.isNotEmpty()
    }

    private fun setTextShopLocationAction(infoUrl: String) {
        textShopLocation?.setOnClickListener {
            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, infoUrl))
            GiftingBottomsheetTracking.trackInfoURLClick(
                bottomSheetTitle.text.toString(), userSession.userId)
        }
    }
}