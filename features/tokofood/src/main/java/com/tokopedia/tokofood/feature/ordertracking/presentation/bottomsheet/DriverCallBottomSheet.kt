package com.tokopedia.tokofood.feature.ordertracking.presentation.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokofood.databinding.DriverCallBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DriverCallBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<DriverCallBottomsheetBinding>()

    private var driverPhoneNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DriverCallBottomsheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDriverPhoneNumber()
        setTitle(getString(com.tokopedia.tokofood.R.string.driver_call_title_bottomsheet))
        setDriverCallBtn()
    }

    private fun setDriverPhoneNumber() {
        this.driverPhoneNumber = arguments?.getString(DRIVER_PHONE_NUMBER_KEY, "").orEmpty()
    }

    private fun setDriverCallBtn() {
        binding?.btnDriverCall?.setOnClickListener {
            if (driverPhoneNumber.isNotBlank()) {
                driverCallToIntent(driverPhoneNumber)
            }
        }
    }

    private fun driverCallToIntent(driverPhoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.fromParts(TEL_PREFIX, driverPhoneNumber, null)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun dismissBottomSheet() {
        if (isVisible) {
            dismiss()
        }
    }

    companion object {
        fun newInstance(driverPhoneNumber: String): DriverCallBottomSheet {
            return DriverCallBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(DRIVER_PHONE_NUMBER_KEY, driverPhoneNumber)
                arguments = bundle
            }
        }

        const val DRIVER_PHONE_NUMBER_KEY = "driverPhoneNumber"
        const val TEL_PREFIX = "tel"

        val TAG: String = DriverCallBottomSheet::class.java.simpleName
    }

}