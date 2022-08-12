package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.tokofood.databinding.BottomsheetMerchantInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.MerchantOpsHourAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.unifycomponents.BottomSheetUnify

class MerchantInfoBottomSheet : BottomSheetUnify() {

    companion object {

        private const val TAG = "MerchantInfoBottomSheet"
        private const val MERCHANT_NAME = "MERCHANT_NAME"
        private const val MERCHANT_ADDRESS = "MERCHANT_ADDRESS"
        private const val MERCHANT_OPS_HOURS = "MERCHANT_OPS_HOURS"

        @JvmStatic
        fun createInstance(name: String, address: String, merchantOpsHours: List<MerchantOpsHour>): MerchantInfoBottomSheet {
            return MerchantInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(MERCHANT_NAME, name)
                    putString(MERCHANT_ADDRESS, address)
                    putParcelableArrayList(MERCHANT_OPS_HOURS, ArrayList(merchantOpsHours))
                }
            }
        }
    }

    private var binding: BottomsheetMerchantInfoLayoutBinding? = null

    private var adapter: MerchantOpsHourAdapter = MerchantOpsHourAdapter()

    private val merchantName: String by lazy {
        arguments?.getString(MERCHANT_NAME) ?: ""
    }

    private val merchantAddress: String by lazy {
        arguments?.getString(MERCHANT_ADDRESS) ?: ""
    }

    private val merchantOpsHours: ArrayList<MerchantOpsHour> by lazy {
        arguments?.getParcelableArrayList(MERCHANT_OPS_HOURS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetMerchantInfoLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        showKnob = true
        showCloseIcon = false
        isHideable = true
        showHeader = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        renderData(merchantName, merchantAddress, merchantOpsHours)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetMerchantInfoLayoutBinding?) {
        binding?.rvMerchantOpsHours?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun renderData(merchantName: String,
                           merchantAddress: String,
                           merchantOpsHours: ArrayList<MerchantOpsHour>) {
        binding?.tpgMerchantName?.text = merchantName
        binding?.tpgMerchantAddress?.text = merchantAddress
        adapter.setMerchantOpsHours(merchantOpsHours)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }
}