package com.tokopedia.seller.menu.common.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.menu.common.constant.Constant
import com.tokopedia.seller.menu.common.databinding.SellerRmTransactionBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.seller.menu.common.R as sellermenucommonR

class RMTransactionBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<SellerRmTransactionBottomsheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SellerRmTransactionBottomsheetBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransactionInfo()
    }

    fun show(fm: FragmentManager) {
        if (!isVisible) {
            show(fm, TAG)
        }
    }

    private fun setTransactionInfo() {
        binding?.tvSellerRmTransactionInfo?.text = getTransactionInfoString()
    }

    private fun getTransactionInfoString(): String {
        val currentTransactionString = getCurrentTransactionString()
        return if (currentTransactionString <= Constant.ShopStatus.MAX_TRANSACTION) {
            context?.getString(
                sellermenucommonR.string.seller_transaction_not_chargeable,
                Constant.ShopStatus.MAX_TRANSACTION
            ).orEmpty()
        } else {
            context?.getString(sellermenucommonR.string.seller_transaction_chargeable, Constant.ShopStatus.MAX_TRANSACTION).orEmpty()
        }
    }

    private fun getCurrentTransactionString(): Long {
        return arguments?.getLong(CURRENT_TRANSACTION_KEY).orZero()
    }

    companion object {
        private const val CURRENT_TRANSACTION_KEY = "current_transaction"

        private const val TAG = "rm_transaction_bottom_sheet"

        @JvmStatic
        fun createInstance(currentTransaction: Long): RMTransactionBottomSheet {
            return RMTransactionBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(CURRENT_TRANSACTION_KEY, currentTransaction)
                }
            }
        }
    }
}
