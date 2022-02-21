package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.MemberItemDataView
import com.tokopedia.home_account.databinding.HomeAccountItemMemberBinding
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class MemberItemViewHolder(itemView: View, val listener: HomeAccountUserListener) :
    BaseViewHolder(itemView) {

    private val binding: HomeAccountItemMemberBinding? by viewBinding()

    fun getSubTitle(): String = binding?.homeAccountItemMemberSubtitle?.text.toString()

    fun bind(member: MemberItemDataView) {
        binding?.homeAccountItemMemberSubtitle?.text = member.subtitle
        binding?.homeAccountItemMemberTitle?.text = member.title
        binding?.homeAccountItemMemberIcon?.let {
            ImageUtils.loadImageWithoutPlaceholderAndError(
                it, member.icon
            )
        }
        binding?.root?.setOnClickListener {
            listener.onMemberItemClicked(member.applink, member.type)
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_item_member

        const val TYPE_DEFAULT = 1
        const val TYPE_TOKOMEMBER = 2
        const val TYPE_TOPQUEST = 3
        const val TYPE_KUPON_SAYA = 4
    }

}