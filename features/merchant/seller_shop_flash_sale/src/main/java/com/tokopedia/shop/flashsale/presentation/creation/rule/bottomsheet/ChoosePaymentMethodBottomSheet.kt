package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomSheetChoosePaymentMethodBinding
import com.tokopedia.shop.flashsale.common.extension.toBulletSpan
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChoosePaymentMethodBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): ChoosePaymentMethodBottomSheet =
            ChoosePaymentMethodBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_choose_payment_method,
                    null
                )
                setChild(view)
            }

        private const val TAG = "ChoosePaymentMethodBottomSheet"
    }

    private var binding by autoClearedNullable<SsfsBottomSheetChoosePaymentMethodBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomSheetChoosePaymentMethodBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView() {
        setTitle(getString(R.string.choose_payment_method_title))
        showCloseIcon = true
    }

    private fun setupContent() {
        binding?.run {
            tgPoint1PaymentMethod.apply {
                text =
                    SpannableString(getString(R.string.choose_payment_method_point_1)).toBulletSpan()
            }
            tgPoint2PaymentMethod.apply {
                text =
                    SpannableString(getString(R.string.choose_payment_method_point_2)).toBulletSpan()
            }
            tgPoint3PaymentMethod.apply {
                text =
                    SpannableString(getString(R.string.choose_payment_method_point_3)).toBulletSpan()
            }
            tgPoint4PaymentMethod.apply {
                text =
                    SpannableString(getString(R.string.choose_payment_method_point_4)).toBulletSpan()
            }
        }
    }
}