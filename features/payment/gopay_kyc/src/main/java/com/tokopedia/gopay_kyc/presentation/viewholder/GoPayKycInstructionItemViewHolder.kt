package com.tokopedia.gopay_kyc.presentation.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.tokopedia.gopay_kyc.R
import kotlinx.android.synthetic.main.gopay_kyc_general_item_layout.view.*
import kotlinx.android.synthetic.main.gopay_kyc_ktp_instruction_item_layout.view.*

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
        }
        return instructionView
    }
}