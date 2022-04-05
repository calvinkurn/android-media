package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.digital_product_detail.databinding.BottomSheetSummaryPulsaBinding
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SummaryTelcoBottomSheet: BottomSheetUnify() {

    private lateinit var titleBottomSheet : String
    private lateinit var denom: DenomData

    private var binding by autoClearedNullable<BottomSheetSummaryPulsaBinding>()

    fun setTitleBottomSheet(titleBottomSheet: String) {
        this.titleBottomSheet = titleBottomSheet
    }

    fun setDenomData (denomData: DenomData) {
        this.denom = denomData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(
                    requireContext(),
                    it.getString(SAVED_INSTANCE_MANAGER_ID)
                )
                titleBottomSheet = manager.getString(SAVED_TITLE_DATA) ?: ""
                denom = manager.get(SAVED_DENOM_DATA, DenomData::class.java) ?: DenomData()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_DENOM_DATA, denom)
                it.put(SAVED_TITLE_DATA, titleBottomSheet)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehaviorKnob(view, true)
    }

    private fun initView() {
        isFullpage = false
        isDragable = false
        showCloseIcon = false

        binding = BottomSheetSummaryPulsaBinding.inflate(LayoutInflater.from(context))
        binding?.run{
            if (::denom.isInitialized) {
                tgTotalPay.text = denom.price
                tgTotalPrice.text = if (!denom.slashPrice.isNullOrEmpty())
                    denom.slashPrice else denom.price

                if (denom.pricePlain.isMoreThanZero() && denom.slashPricePlain.isMoreThanZero()) {
                    tgTotalDiscount.show()
                    tgTotalDiscountTitle.show()
                    tgTotalDiscount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        (denom.pricePlain.toLong() - denom.slashPricePlain.toLong()), false
                    )
                }
            }
        }

        if (::titleBottomSheet.isInitialized) setTitle(titleBottomSheet)
        setChild(binding?.root)
    }

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_DENOM_DATA = "SAVED_DENOM_DATA"
        private const val SAVED_TITLE_DATA = "SAVED_TITLE_DATA"

        fun getInstance(): SummaryTelcoBottomSheet = SummaryTelcoBottomSheet()
    }
}