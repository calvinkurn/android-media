package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.domain.model.CheckEligible
import com.tokopedia.withdraw.saldowithdrawal.helper.BulletPointSpan
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant

class RekPremBankAccountInfoBottomSheet : BottomSheetUnify() {

    private val childLayout = R.layout.swd_dialog_rp_bank_account

    private lateinit var childView: View

    private lateinit var bulletPointSpan: BulletPointSpan

    private lateinit var checkEligible: CheckEligible

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_CHECK_ELIGIBLE_DATA))
                checkEligible = it.getParcelable(ARG_CHECK_ELIGIBLE_DATA)
            else {
                this.dismiss()
                return
            }
        } ?: run {
            this.dismiss()
            return
        }
        childView = LayoutInflater.from(context).inflate(childLayout, null, false)
        setChild(childView)
        context?.let { context ->
            addBulletToTextView(context, R.string.swd_premium_account_info_line_one,
                    childView.findViewById(R.id.tvPremiumAccountInfoLineOne))
            addBulletToTextView(context, R.string.swd_premium_account_info_line_Two,
                    childView.findViewById(R.id.tvPremiumAccountInfoLineTwo))
            addBulletToTextView(context, R.string.swd_premium_account_info_line_Three,
                    childView.findViewById(R.id.tvPremiumAccountInfoLineThree))
            if (!checkEligible.data.isPowerMerchant)
                childView.findViewById<View>(R.id.btnJoinPremiumAccount).gone()
            else if (checkEligible.data.isIsPowerWD) {
                childView.findViewById<View>(R.id.btnJoinPremiumAccount).gone()
            } else {
                childView.findViewById<View>(R.id.btnJoinPremiumAccount).visible()
                childView.findViewById<View>(R.id.btnJoinPremiumAccount).setOnClickListener {
                    dismiss()
                    WithdrawConstant.openRekeningAccountInfoPage(context)
                }
            }
            childView.findViewById<View>(R.id.tvMoreInfo).setOnClickListener {
                dismiss()
                WithdrawConstant.openRekeningAccountInfoPage(context)
            }
        }
    }

    private fun addBulletToTextView(context: Context,
                                    @StringRes stringID: Int, textView: TextView) {
        if (!::bulletPointSpan.isInitialized) {
            val bulletSpacing = context.resources.getDimension(R.dimen.spacing_lvl3).toInt()
            val bulletRadius = context.resources.getDimension(R.dimen.swd_corner_radius)
            val color = ContextCompat.getColor(context, R.color.Neutral_N700_68)
            bulletPointSpan = BulletPointSpan(bulletSpacing, bulletRadius, color)
        }
        val charSequence = getString(stringID)
        val spannable = SpannableString(charSequence)
        spannable.setSpan(bulletPointSpan, 0, charSequence.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    companion object {

        private const val ARG_CHECK_ELIGIBLE_DATA = "arg_check_eligible_data"

        fun getInstance(checkEligible: CheckEligible): RekPremBankAccountInfoBottomSheet {
            return RekPremBankAccountInfoBottomSheet().apply {
                val bundle = Bundle()
                bundle.putParcelable(ARG_CHECK_ELIGIBLE_DATA, checkEligible)
                arguments = bundle
            }
        }
    }

}