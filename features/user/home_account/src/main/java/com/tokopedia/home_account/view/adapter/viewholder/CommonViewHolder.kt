package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.databinding.HomeAccountItemCommonBinding
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class CommonViewHolder(itemView: View, val listener: HomeAccountUserListener) :
    BaseViewHolder(itemView) {

    private val binding: HomeAccountItemCommonBinding? by viewBinding()

    fun getTitle() = binding?.accountUserItemCommonTitle?.text.toString()

    fun bind(common: CommonDataView) {
        binding?.accountUserItemCommonTitle?.text = common.title
        if (common.icon != 0) {
            binding?.accountUserItemCommonIcon?.setImage(common.icon)
        }
        if (common.urlIcon.isNotEmpty()) {
            ImageHandler.loadImageFit2(
                binding?.accountUserItemCommonIcon?.context,
                binding?.accountUserItemCommonIcon,
                common.urlIcon
            )
        }

        binding?.root?.setOnClickListener {
            listener.onSettingItemClicked(common)
        }
        if (common.endText.isNotEmpty() && common.type != TYPE_SWITCH) {
            binding?.accountUserItemCommonEndText?.show()
            binding?.accountUserItemCommonEndText?.text = common.endText
        }

        binding?.accountUserItemCommonTitle?.setPadding(0, 0, 0, 0)

        when (common.type) {
            TYPE_WITHOUT_BODY -> {
                binding?.accountUserItemCommonBody?.hide()
                binding?.accountUserItemCommonTitle?.setPadding(0, 10, 0, 0)
            }
            TYPE_SWITCH -> {
                binding?.root?.isClickable = false
                binding?.accountUserItemCommonSwitch?.show()
                binding?.accountUserItemCommonBody?.text = common.body
                binding?.accountUserItemCommonSwitch?.isChecked = common.isChecked
                binding?.accountUserItemCommonSwitch?.setOnCheckedChangeListener { _, isChecked ->
                    binding?.accountUserItemCommonSwitch?.let {
                        listener.onSwitchChanged(
                            common,
                            isChecked,
                            it
                        )
                    }
                }
            }
            else -> {
                binding?.accountUserItemCommonBody?.text = common.body
            }
        }
        setupLabel(common)
    }

    private fun setupLabel(common: CommonDataView) {
        if (common.labelText.isNotEmpty()) {
            binding?.accountUserLabel?.setLabel(common.labelText)
            binding?.accountUserLabel?.show()
        } else {
            binding?.accountUserLabel?.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_common

        const val TYPE_DEFAULT = 1
        const val TYPE_SWITCH = 2
        const val TYPE_WITHOUT_BODY = 3
    }
}