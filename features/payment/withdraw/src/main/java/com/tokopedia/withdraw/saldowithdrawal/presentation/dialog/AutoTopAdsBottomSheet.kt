package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.os.Bundle
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.withdraw.databinding.SwdDialogAutoTopadsBottomsheetBinding

class AutoTopAdsBottomSheet: BottomSheetUnify() {

    private var binding: SwdDialogAutoTopadsBottomsheetBinding? = null
    private var originalAmount: Long = 0L
    private var recommendedAmount: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        originalAmount = arguments?.getLong(ORIGINAL_AMOUNT) ?: 0L
        recommendedAmount = arguments?.getLong(RECOMMENDED_AMOUNT) ?: 0L
        binding = SwdDialogAutoTopadsBottomsheetBinding.inflate(layoutInflater)
        setChild(binding?.root)

        setupViews()
    }

    private fun setupViews() {
        setUpTextAmount()
        setupButtonsListener()
    }

    private fun setUpTextAmount() {
        binding?.tvTotalWithdrawAmount?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(originalAmount, false)
        binding?.tvRekomendasiAmount?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(recommendedAmount, false)
    }

    private fun setupButtonsListener() {
        binding?.btnRecommendedWithdraw?.setOnClickListener {
            activity?.setResult(RECOMMENDED_WD)
            activity?.finish()
        }
        binding?.btnOriginalWithdraw?.setOnClickListener {
            activity?.setResult(ORIGINAL_WD)
            activity?.finish()
        }
    }

    companion object {
        const val RECOMMENDED_WD = 123
        const val ORIGINAL_WD = 124
        const val TAG = "AutoTopAdsBottomSheet"
        private const val ORIGINAL_AMOUNT = "originalAmount"
        private const val RECOMMENDED_AMOUNT = "recommendedAmount"
        fun getInstance(originalAmount: Long, recommendedAmount: Long): AutoTopAdsBottomSheet {
            return AutoTopAdsBottomSheet().apply {
                val bundle = Bundle()
                bundle.putLong(ORIGINAL_AMOUNT, originalAmount)
                bundle.putLong(RECOMMENDED_AMOUNT, recommendedAmount)
                arguments = bundle
            }
        }
    }
}
