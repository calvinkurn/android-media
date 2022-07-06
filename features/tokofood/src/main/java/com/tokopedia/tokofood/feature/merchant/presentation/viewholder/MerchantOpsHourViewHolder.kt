package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemMerchantOpsHourBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour

class MerchantOpsHourViewHolder(private val binding: ItemMerchantOpsHourBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var context: Context? = null

    init {
        context = binding.root.context
    }

    fun bindData(merchantOpsHour: MerchantOpsHour) {
        // render ops day initial
        merchantOpsHour.initial?.let { initial ->
            binding.tpgOpsDayInitial.text = initial.toString()
        }
        // render ops day and time
        binding.tpgOpsDay.text = merchantOpsHour.day
        binding.tpgOpsHours.text = merchantOpsHour.time
        // 3 states : today, other day, close / holiday
        context?.run {
            when {
                merchantOpsHour.isToday -> {
                    val blackColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                    val backGround = ContextCompat.getDrawable(this, R.drawable.green_gn500_circle_background)
                    binding.tpgOpsDay.setTextColor(blackColor)
                    binding.tpgOpsHours.setTextColor(blackColor)
                    binding.tpgOpsDayInitial.background = backGround
                }
                merchantOpsHour.isWarning -> {
                    val blackColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                    val redColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                    val backGround = ContextCompat.getDrawable(this, R.drawable.red_rn500_circle_background)
                    binding.tpgOpsDay.setTextColor(blackColor)
                    binding.tpgOpsHours.setTextColor(redColor)
                    binding.tpgOpsDayInitial.background = backGround
                }
                else -> {
                    val greyColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN600)
                    val background = ContextCompat.getDrawable(this, R.drawable.grey_nn300_circle_background)
                    binding.tpgOpsDay.setTextColor(greyColor)
                    binding.tpgOpsHours.setTextColor(greyColor)
                    binding.tpgOpsDayInitial.background = background
                }
            }
        }
    }
}