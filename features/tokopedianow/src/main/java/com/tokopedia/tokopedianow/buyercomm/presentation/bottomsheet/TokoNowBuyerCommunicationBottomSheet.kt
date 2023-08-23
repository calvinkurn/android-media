package com.tokopedia.tokopedianow.buyercomm.presentation.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.sendClickCloseButtonEvent
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.sendClickTermsAndConditionButtonEvent
import com.tokopedia.tokopedianow.buyercomm.analytic.BuyerCommunicationAnalytics.sendImpressionBottomSheetEvent
import com.tokopedia.tokopedianow.buyercomm.di.component.DaggerBuyerCommunicationComponent
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import com.tokopedia.tokopedianow.buyercomm.presentation.view.BuyerCommunicationBenefitItemView
import com.tokopedia.tokopedianow.buyercomm.presentation.view.BuyerCommunicationShipmentItemView
import com.tokopedia.tokopedianow.buyercomm.presentation.viewmodel.TokoNowBuyerCommunicationViewModel
import com.tokopedia.tokopedianow.common.constant.ConstantUrl
import com.tokopedia.tokopedianow.databinding.BottomsheetTokopedianowBuyerCommunicationBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TokoNowBuyerCommunicationBottomSheet : BottomSheetUnify() {

    companion object {
        fun newInstance(
            data: BuyerCommunicationData?
        ): TokoNowBuyerCommunicationBottomSheet {
            return TokoNowBuyerCommunicationBottomSheet().apply {
                this.data = data
            }
        }

        private val TAG = TokoNowBuyerCommunicationBottomSheet::class.java.simpleName
    }

    private var binding by autoClearedNullable<BottomsheetTokopedianowBuyerCommunicationBinding>()

    private var data: BuyerCommunicationData? = null

    @Inject
    lateinit var viewModel: TokoNowBuyerCommunicationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        observeLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated(data)
    }

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.finish()
        super.onDismiss(dialog)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initBottomSheet() {
        binding = BottomsheetTokopedianowBuyerCommunicationBinding
            .inflate(LayoutInflater.from(context))
        setTitle(getString(R.string.tokopedianow_buyer_communication_title))
        setChild(binding?.root)
        clearContentPadding = true
        isFullpage = false
    }

    private fun observeLiveData() {
        observe(viewModel.buyerCommunicationData) {
            showBottomSheetLayout(it)
        }
    }

    private fun showBottomSheetLayout(data: BuyerCommunicationData) {
        showOperationHour(data)
        showShipmentOptions(data)
        showBenefitList()
        showIllustrationImage()
        showTermsAndConditionText()
        setCloseButtonClickListener()
        sendImpressionTracker()
    }

    private fun showOperationHour(data: BuyerCommunicationData) {
        binding?.apply {
            context?.let { context ->
                val warehouseStatusTextColor = if (data.isWarehouseOpen()) {
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_GN500
                    )
                } else {
                    ContextCompat.getColor(
                        context,
                        unifyprinciplesR.color.Unify_NN600
                    )
                }
                textOperationalHour.text = data.operationHour
                textWarehouseStatus.text = data.warehouseStatus
                textWarehouseStatus.setTextColor(warehouseStatusTextColor)
            }
        }
    }

    private fun showShipmentOptions(data: BuyerCommunicationData) {
        context?.let { context ->
            data.shipmentOptions.forEach { shipment ->
                val shipmentItemView = BuyerCommunicationShipmentItemView(context)
                shipmentItemView.bind(shipment)
                binding?.containerShipmentOptionsList?.addView(shipmentItemView)
            }
        }
    }

    private fun showBenefitList() {
        context?.let { context ->
            createBenefitList().forEachIndexed { index, stringId ->
                val number = (index + 1).toString()
                val benefitItemView = BuyerCommunicationBenefitItemView(context)
                benefitItemView.bind(number, getString(stringId))
                binding?.containerBenefitList?.addView(benefitItemView)
            }
        }
    }

    private fun showIllustrationImage() {
        binding?.imageIllustration?.loadImage(
            TokopediaImageUrl.TOKOPEDIANOW_BUYER_COMMUNICATION_ILLUSTRATION
        )
    }

    private fun showTermsAndConditionText() {
        context?.let { context ->
            val text = SpannableString(
                getString(
                    R.string.tokopedianow_buyer_communication_tnc
                )
            )
            val tncText = getString(
                R.string.tokopedianow_buyer_communication_tnc_label
            )
            val colorSpan = ForegroundColorSpan(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
            )
            val boldSpan = StyleSpan(Typeface.BOLD)
            val startIndex = text.indexOf(tncText)
            val endIndex = text.length - 1

            text.setSpan(boldSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            text.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding?.textTnc?.setOnClickListener {
                openTermsAndConditionWebview()
                sendClickTermsAndConditionTracker()
            }
            binding?.textTnc?.text = text
        }
    }

    private fun createBenefitList(): List<Int> {
        return listOf(
            R.string.tokopedianow_buyer_communication_schedule_benefit,
            R.string.tokopedianow_buyer_communication_delivery_benefit,
            R.string.tokopedianow_buyer_communication_two_hour_benefit
        )
    }

    private fun openTermsAndConditionWebview() {
        RouteManager.route(
            context,
            "${ApplinkConst.WEBVIEW}?url=${ConstantUrl.TERMS_AND_CONDITION}"
        )
    }

    private fun setCloseButtonClickListener() {
        setCloseClickListener {
            sendClickCloseButtonTracker()
            dismiss()
        }
    }

    private fun sendImpressionTracker() {
        val warehouses = viewModel.getWarehousesData()
        sendImpressionBottomSheetEvent(warehouses)
    }

    private fun sendClickCloseButtonTracker() {
        val warehouses = viewModel.getWarehousesData()
        sendClickCloseButtonEvent(warehouses)
    }

    private fun sendClickTermsAndConditionTracker() {
        val warehouses = viewModel.getWarehousesData()
        sendClickTermsAndConditionButtonEvent(warehouses)
    }

    private fun injectDependencies() {
        DaggerBuyerCommunicationComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}
