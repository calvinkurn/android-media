package com.tokopedia.shop.flashsale.presentation.creation.campaign_rule.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.extension.toBulletSpan
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

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

    private var paymentMethodPoint1: Typography? = null
    private var paymentMethodPoint2: Typography? = null
    private var paymentMethodPoint3: Typography? = null
    private var paymentMethodPoint4: Typography? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView(view: View) {
        setTitle(getString(R.string.choose_payment_method_title))

        paymentMethodPoint1 = view.findViewById(R.id.tg_point_1_payment_method)
        paymentMethodPoint2 = view.findViewById(R.id.tg_point_2_payment_method)
        paymentMethodPoint3 = view.findViewById(R.id.tg_point_3_payment_method)
        paymentMethodPoint4 = view.findViewById(R.id.tg_point_4_payment_method)
        showCloseIcon = true
    }

    private fun setupContent() {
        paymentMethodPoint1?.apply {
            text =
                SpannableString(getString(R.string.payment_method_point_1)).toBulletSpan()
        }
        paymentMethodPoint2?.apply {
            text =
                SpannableString(getString(R.string.payment_method_point_2)).toBulletSpan()
        }
        paymentMethodPoint3?.apply {
            text =
                SpannableString(getString(R.string.payment_method_point_3)).toBulletSpan()
        }
        paymentMethodPoint4?.run {
            text =
                SpannableString(getString(R.string.payment_method_point_4)).toBulletSpan()
        }
    }
}