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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import com.tokopedia.withdraw.saldowithdrawal.di.component.DaggerWithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.helper.BulletPointSpan
import javax.inject.Inject

class RekPremBankAccountInfoBottomSheet : BottomSheetUnify() {

    private val childLayout = R.layout.swd_dialog_rp_bank_account

    private val childView: View by lazy(LazyThreadSafetyMode.NONE) {
        LayoutInflater.from(context).inflate(childLayout, null, false)
    }

    @Inject
    lateinit var userSession: dagger.Lazy<UserSession>

    private lateinit var bulletPointSpan: BulletPointSpan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setChild(childView)
        context?.let { context ->
            addBulletToTextView(context, R.string.swd_premium_account_info_line_one,
                    childView.findViewById(R.id.tvPremiumAccountInfoLineOne))
            addBulletToTextView(context, R.string.swd_premium_account_info_line_Two,
                    childView.findViewById(R.id.tvPremiumAccountInfoLineTwo))
            addBulletToTextView(context, R.string.swd_premium_account_info_line_Three,
                    childView.findViewById(R.id.tvPremiumAccountInfoLineThree))
            childView.findViewById<View>(R.id.btnJoinPremiumAccount).setOnClickListener {
                WithdrawConstant.openRekeningAccountInfoPage(context, userSession.get())
            }
            childView.findViewById<View>(R.id.tvMoreInfo).setOnClickListener {
                //todo pending from business side
            }
        }
    }

    private fun initInjector() {
        activity?.let {
            DaggerWithdrawComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build().inject(this)
        } ?: run {
            dismiss()
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

}