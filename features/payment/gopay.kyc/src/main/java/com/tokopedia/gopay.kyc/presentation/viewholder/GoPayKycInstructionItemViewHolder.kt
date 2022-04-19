package com.tokopedia.gopay.kyc.presentation.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gopay.kyc.R
import kotlinx.android.synthetic.main.gopay_kyc_general_item_layout.view.*
import kotlinx.android.synthetic.main.gopay_kyc_ktp_instruction_item_layout.view.*
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class GoPayKycInstructionItemViewHolder(
    val context: Context,
    private val layoutParams: ViewGroup.LayoutParams
) {

    private fun getLayout() = R.layout.gopay_kyc_ktp_instruction_item_layout

    fun bindData(@StringRes instruction: Int): View {
        val instructionView = LayoutInflater.from(context).inflate(getLayout(), null)
        instructionView.apply {
            layoutParams = this@GoPayKycInstructionItemViewHolder.layoutParams
            goPayKycBulletInstruction.text = context.getString(instruction)
            if (context.isDarkMode())
                goPayKtpBullet.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_gopay_bullet_dot_white))
            else
                goPayKtpBullet.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.ic_gopay_bullet_dot_black))
        }
        return instructionView
    }
}