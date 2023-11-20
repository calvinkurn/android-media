package com.tokopedia.mvc.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetUpdateCouponTipBinding
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class UpdateCouponTipBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetUpdateCouponTipBinding>()

    companion object {
        @JvmStatic
        fun newInstance(
            isVoucherApproved: Boolean
        ): UpdateCouponTipBottomSheet {
            return UpdateCouponTipBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BundleConstant.BUNDLE_KEY_IS_VOUCHER_APPROVED, isVoucherApproved)
                }
            }
        }
    }

    private val isApproved by lazy { arguments?.getBoolean(BundleConstant.BUNDLE_KEY_IS_VOUCHER_APPROVED) as Boolean }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = SmvcBottomsheetUpdateCouponTipBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.smvc_ubah_kupon_label))
        setChild(viewBinding.root)
        binding?.tpgTip?.text =
            if (isApproved) {
                getString(R.string.smvc_update_coupon_tip_label_approved)
            } else {
                getString(R.string.smvc_update_coupon_tip_label_not_approved)
            }
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}
