package com.tokopedia.shop.flash_sale.presentation.creation.campaign_rule.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flash_sale.common.extension.toBulletSpan
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ChoosePaymentMethodBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(): ChoosePaymentMethodBottomSheet =
            ChoosePaymentMethodBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_choose_payment_method,
                    null
                )
                setChild(view)
            }

        private const val TAG = "CampaignTeaserInformationBottomSheet"
    }

    private var paymentMethodPoint1: Typography? = null
    private var paymentMethodPoint2: Typography? = null
    private var paymentMethodPoint3: Typography? = null
    private var paymentMethodPoint4: Typography? = null

    @SuppressLint("ResourcePackage")
    private val paymentMethodPoint1Text =
        SpannableString(getString(R.string.payment_method_point_1))
    @SuppressLint("ResourcePackage")
    private val paymentMethodPoint2Text =
        SpannableString(getString(R.string.payment_method_point_2))
    @SuppressLint("ResourcePackage")
    private val paymentMethodPoint3Text =
        SpannableString(getString(R.string.payment_method_point_3))
    @SuppressLint("ResourcePackage")
    private val paymentMethodPoint4Text =
        SpannableString(getString(R.string.payment_method_point_4))

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
        setTitle(getString(R.string.campaign_teaser_info_title))

        paymentMethodPoint1 = view.findViewById(R.id.tg_point_1_payment_method)
        paymentMethodPoint2 = view.findViewById(R.id.tg_point_2_payment_method)
        paymentMethodPoint3 = view.findViewById(R.id.tg_point_3_payment_method)
        paymentMethodPoint4 = view.findViewById(R.id.tg_point_4_payment_method)
        showCloseIcon = true
    }

    private fun setupContent() {
        paymentMethodPoint1Text.toBulletSpan()
        paymentMethodPoint2Text.toBulletSpan()
        paymentMethodPoint3Text.toBulletSpan()
        paymentMethodPoint4Text.toBulletSpan()

        paymentMethodPoint1?.run {
            text = paymentMethodPoint1Text
        }
        paymentMethodPoint2?.run {
            text = paymentMethodPoint2Text
        }
        paymentMethodPoint3?.run {
            text = paymentMethodPoint3Text
        }
        paymentMethodPoint4?.run {
            text = paymentMethodPoint4Text
        }
    }
}