package com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.databinding.TokochatMaskingPhoneNumberBottomsheetBinding
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.IV_MASKING_PHONE_NUMBER
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber

class MaskingPhoneNumberBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatMaskingPhoneNumberBottomsheetBinding>()

    private var driverPhoneNumber: String? = null

    private var analyticsListener: AnalyticsListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.tokochat_common.R.layout.tokochat_masking_phone_number_bottomsheet,
            container,
            false
        )
        binding = TokochatMaskingPhoneNumberBottomsheetBinding.bind(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setDriverPhoneNumber()
    }

    private fun setDriverPhoneNumber() {
        this@MaskingPhoneNumberBottomSheet.driverPhoneNumber =
            arguments?.getString(DRIVER_PHONE_NUMBER_KEY, "").orEmpty()
    }

    private fun setupViews() {
        binding?.tokochatIvMaskingPhoneNumber?.loadImage(IV_MASKING_PHONE_NUMBER)
        setDriverCallBtn()
        setCloseMaskingPhoneNumber()
    }

    private fun setCloseMaskingPhoneNumber() {
        setOnDismissListener {
            analyticsListener?.onCloseMaskingPhoneNumberBottomSheet()
        }
    }

    private fun setDriverCallBtn() {
        binding?.tokochatBtnMaskingPhoneNumber?.setOnClickListener {
            analyticsListener?.onConfirmCallOnBottomSheetCallDriver()
            driverPhoneNumber?.let { driverPhoneNumber ->
                driverCallToIntent(driverPhoneNumber)
            }
        }
    }

    private fun driverCallToIntent(driverPhoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("$TELEPHONY_URI$driverPhoneNumber")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun setTrackingListener(analyticsListener: AnalyticsListener) {
        this.analyticsListener = analyticsListener
    }

    interface AnalyticsListener {
        fun onCloseMaskingPhoneNumberBottomSheet()
        fun onConfirmCallOnBottomSheetCallDriver()
    }

    companion object {
        fun newInstance(driverPhoneNumber: String): MaskingPhoneNumberBottomSheet {
            return MaskingPhoneNumberBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(DRIVER_PHONE_NUMBER_KEY, driverPhoneNumber)
                arguments = bundle
            }
        }

        private const val DRIVER_PHONE_NUMBER_KEY = "driverPhoneNumber"
        private const val TELEPHONY_URI = "tel:"

        val TAG: String = MaskingPhoneNumberBottomSheet::class.java.simpleName
    }
}
