package com.tokopedia.product.manage.feature.etalase.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemProductManageEtalaseBinding
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import com.tokopedia.utils.view.binding.viewBinding

class EtalaseViewHolder(
    itemVew: View,
    private val listener: OnClickListener
): AbstractViewHolder<EtalaseViewModel>(itemVew) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_etalase
    }

    private val binding by viewBinding<ItemProductManageEtalaseBinding>()

    private val radioButtonEtalase
        get() = binding?.radioButtonEtalase

    override fun bind(etalase: EtalaseViewModel) {
        binding?.etalaseName?.text = etalase.name

        radioButtonEtalase?.setOnCheckedChangeListener { _, isSelected ->
            listener.onClickEtalase(isSelected, etalase)
        }

        itemView.setOnClickListener { toggleEtalase() }
    }

    fun toggleEtalase(): Boolean {
        radioButtonEtalase?.run {
            isChecked = !isChecked
        }
        return radioButtonEtalase?.isChecked == true
    }

    fun uncheckEtalase() {
        radioButtonEtalase?.isChecked = false
    }

    interface OnClickListener {
        fun onClickEtalase(isChecked: Boolean, etalase: EtalaseViewModel)
    }
}