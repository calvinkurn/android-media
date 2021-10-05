package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyorderhistory.data.model.UohFilterBundle
import com.tokopedia.unifyorderhistory.databinding.BottomsheetOptionUohItemBinding
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetOptionAdapter

/**
 * Created by fwidjaja on 05/10/21.
 */
class UohBottomSheetOptionItemViewHolder(private val binding: BottomsheetOptionUohItemBinding,
                                         private val actionListener: UohBottomSheetOptionAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    private var isReset = false
    private var selectedRadio = -1
    private var selectedKey = ""

    fun bind(position: Int, filterBundleList: ArrayList<UohFilterBundle>, filterType: Int) {
        if (position > -1 && position < filterBundleList.size) {
            val filterBundle = filterBundleList[position]
            if (filterBundle.type == 0) {
                binding.run {
                    sublabelOption.gone()
                    dividerSublabel.gone()
                    labelOption.visible()
                    dividerLabel.visible()
                    if (filterType == UohConsts.TYPE_FILTER_STATUS && filterBundle.value.isEmpty()) {
                        binding.labelOption.text = UohConsts.ALL_STATUS_TRANSACTION
                    } else if (filterType == UohConsts.TYPE_FILTER_STATUS && filterBundle.value.equals(UohConsts.TRANSAKSI_BERLANGSUNG, true)) {
                        binding.labelOption.text = UohConsts.SEMUA_TRANSAKSI_BERLANGSUNG
                    } else {
                        binding.labelOption.text = filterBundle.value
                    }
                }
            } else if (filterBundle.type == 1) {
                binding.run {
                    labelOption.gone()
                    dividerLabel.gone()
                    sublabelOption.visible()
                    dividerSublabel.visible()
                    sublabelOption.text = filterBundle.value
                }
            }

            binding.root.setOnClickListener { selectItem(position, filterBundle, filterType) }
            binding.rbOption.setOnClickListener { selectItem(position, filterBundle, filterType) }

            if (!isReset) {
                if (selectedKey.isEmpty() && selectedRadio == -1) {
                    if (filterType == UohConsts.TYPE_FILTER_DATE && filterBundle.key == "0" ||
                            filterType == UohConsts.TYPE_FILTER_STATUS && filterBundle.key.isEmpty() ||
                            filterType == UohConsts.TYPE_FILTER_CATEGORY && filterBundle.value.equals(UohConsts.ALL_PRODUCTS, true)) {
                        binding.rbOption.isChecked = true
                    }
                } else {
                    if (filterBundle.key.equals(selectedKey, true) && selectedRadio == -1) {
                        binding.rbOption.isChecked = true
                    } else {
                        binding.rbOption.isChecked = position == selectedRadio
                    }
                }
            } else {
                if (selectedKey.isEmpty() && selectedRadio == -1) {
                    if ((filterType == UohConsts.TYPE_FILTER_DATE && position == 0) ||
                            (filterType == UohConsts.TYPE_FILTER_STATUS && filterBundle.value == UohConsts.ALL_STATUS_TRANSACTION) ||
                            (filterType == UohConsts.TYPE_FILTER_CATEGORY && filterBundle.value == UohConsts.ALL_PRODUCTS)) {
                        binding.rbOption.isChecked = true
                    } else {
                        if (filterBundle.key.equals(selectedKey, true) && selectedRadio == -1) {
                            binding.rbOption.isChecked = true
                        } else {
                            binding.rbOption.isChecked = position == selectedRadio
                        }
                    }
                }
            }
        }
    }

    private fun selectItem(position: Int, filterBundle: UohFilterBundle, filterType: Int) {
        isReset = false
        selectedRadio = position
        actionListener?.onOptionItemClick(filterBundle.key, filterBundle.value, filterType)
    }
}