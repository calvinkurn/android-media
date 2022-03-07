package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.SettingDataView
import com.tokopedia.home_account.databinding.HomeAccountExpandableLayoutBinding
import com.tokopedia.home_account.view.adapter.HomeAccountUserCommonAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SettingViewHolder(itemView: View, val listener: HomeAccountUserListener) :
    BaseViewHolder(itemView) {

    private val binding: HomeAccountExpandableLayoutBinding? by viewBinding()

    private var rotationAngle = 0F

    var adapter: HomeAccountUserCommonAdapter? = null

    fun getTitle() = binding?.homeAccountExpandableLayoutTitle?.text.toString()

    fun bind(setting: SettingDataView) {
        if (setting.title.isNotEmpty()) {
            binding?.homeAccountExpandableLayoutTitle?.visibility = View.VISIBLE
            binding?.homeAccountExpandableLayoutTitle?.text = setting.title
        } else {
            binding?.homeAccountExpandableLayoutTitle?.visibility = View.GONE
        }

        if (setting.showArrowDown) {
            binding?.homeAccountExpandableArrow?.show()
        } else {
            binding?.homeAccountExpandableArrow?.hide()
        }
        setupItemAdapter(setting)
        listener.onItemViewBinded(adapterPosition, itemView, setting)
    }

    private fun setupItemAdapter(setting: SettingDataView) {
        adapter = HomeAccountUserCommonAdapter(listener, CommonViewHolder.LAYOUT)
        adapter?.list = setting.items
        binding?.homeAccountExpandableLayoutRv?.adapter = adapter
        binding?.homeAccountExpandableLayoutRv?.layoutManager = LinearLayoutManager(
            binding?.homeAccountExpandableLayoutRv?.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding?.homeAccountExpandableLayoutRv?.isNestedScrollingEnabled = false
        expandCollapseItem(setting.isExpanded)

        binding?.root?.setOnClickListener {
            if (binding?.homeAccountExpandableArrow?.visibility == View.VISIBLE) {
                val id: Int = when (setting.title) {
                    TITLE_APP_SETTING -> AccountConstants.SettingCode.SETTING_APP_SETTING
                    TITLE_ABOUT_TOKOPEDIA -> AccountConstants.SettingCode.SETTING_ABOUT_TOKOPEDIA
                    else -> 0
                }
                listener.onSettingItemClicked(CommonDataView(id = id, body = setting.title))
                setting.isExpanded = !setting.isExpanded
                rotationAngle = if (rotationAngle == 0F) 180F else 0F //toggle
                binding?.homeAccountExpandableArrow?.animate()?.rotation(rotationAngle)
                    ?.setDuration(400)?.start()
                expandCollapseItem(setting.isExpanded)
            }
        }
        adapter?.run {
            listener.onCommonAdapterReady(adapterPosition, this)
        }
    }

    private fun expandCollapseItem(isExpanded: Boolean) {
        if (isExpanded) {
            binding?.homeAccountExpandableLayoutRv?.show()
        } else {
            binding?.homeAccountExpandableLayoutRv?.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.home_account_expandable_layout

        val SETTING_ORIENTATION_VERTICAL = 1
        val TITLE_APP_SETTING = "Pengaturan Aplikasi"
        val TITLE_ABOUT_TOKOPEDIA = "Seputar Tokopedia"
    }

}