package com.tokopedia.product.manage.feature.etalase.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import kotlinx.android.synthetic.main.item_product_manage_etalase.view.*

class EtalaseViewHolder(
    itemVew: View,
    private val listener: OnClickListener
): AbstractViewHolder<EtalaseViewModel>(itemVew) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_etalase
    }

    private val radioButtonEtalase by lazy { itemView.radioButtonEtalase }

    override fun bind(etalase: EtalaseViewModel) {
        itemView.etalaseName.text = etalase.name

        radioButtonEtalase.setOnCheckedChangeListener { _, isSelected ->
            listener.onClickEtalase(isSelected, etalase)
        }

        itemView.setOnClickListener { toggleEtalase() }
    }

    fun toggleEtalase(): Boolean {
        radioButtonEtalase.apply { isChecked = !isChecked }
        return radioButtonEtalase.isChecked
    }

    fun uncheckEtalase() {
        radioButtonEtalase.isChecked = false
    }

    interface OnClickListener {
        fun onClickEtalase(isChecked: Boolean, etalase: EtalaseViewModel)
    }
}