package com.tokopedia.tokofood.feature.ordertracking.presentation.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.databinding.DriverCallBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DriverCallBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<DriverCallBottomsheetBinding>()

    private var driverPhoneNumber: String? = null
    private var isCallable: Boolean? = null

    private var tracking: (() -> Unit)? = null

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
        setupViews()
    }

    private fun setDriverPhoneNumber() {
        arguments?.run {
            this@DriverCallBottomSheet.driverPhoneNumber = getString(DRIVER_PHONE_NUMBER_KEY, "").orEmpty()
            this@DriverCallBottomSheet.isCallable = getBoolean(IS_CALLABLE_KEY, false)
        }
    }

    private fun setupViews() {
        binding?.run {
            if (isCallable == true) {
                tvDriverCallTitle.text = getString(com.tokopedia.tokofood.R.string.driver_call_enabled_title_bottomsheet)
                tvDriverCallDesc.text = getString(com.tokopedia.tokofood.R.string.driver_call_enabled_desc_bottomsheet)
                btnDriverCall.show()
                setDriverCallBtn()
            } else {
                tvDriverCallTitle.text = getString(com.tokopedia.tokofood.R.string.driver_call_disabled_title_bottomsheet)
                tvDriverCallDesc.text = getString(com.tokopedia.tokofood.R.string.driver_call_disabled_desc_bottomsheet)
                btnDriverCall.hide()
            }
        }
    }

    private fun setDriverCallBtn() {
        binding?.btnDriverCall?.setOnClickListener {
            tracking?.invoke()
            driverPhoneNumber?.let { driverPhoneNumber ->
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

    fun setTrackingListener(tracking: () -> Unit) {
        this.tracking = tracking
    }

    companion object {
        fun newInstance(driverPhoneNumber: String, isCallable: Boolean): DriverCallBottomSheet {
            return DriverCallBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(DRIVER_PHONE_NUMBER_KEY, driverPhoneNumber)
                bundle.putBoolean(IS_CALLABLE_KEY, isCallable)
                arguments = bundle
            }
        }

        private const val DRIVER_PHONE_NUMBER_KEY = "driverPhoneNumber"
        private const val IS_CALLABLE_KEY = "isCallable"
        const val TEL_PREFIX = "tel"

        val TAG: String = DriverCallBottomSheet::class.java.simpleName
    }

}