package com.tokopedia.minicart.bmgm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.tokopedia.minicart.bmgm.presentation.model.BmgmSingleProductUiModel
import com.tokopedia.minicart.databinding.ItemBmgmMiniCartBundledProductBinding

/**
 * Created by @ilhamsuaib on 04/08/23.
 */

class BmgmBundledProductAdapter(
    private val items: List<BmgmSingleProductUiModel>
) : Adapter<BmgmBundledProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBmgmMiniCartBundledProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        binding: ItemBmgmMiniCartBundledProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}