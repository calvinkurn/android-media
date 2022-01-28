package com.tokopedia.digital_product_detail.presentation.adapter.viewholder

import android.widget.CheckBox
import android.widget.CompoundButton
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.databinding.ViewPdpFilterCheckboxBinding

class DigitalPDPFilterAllViewHolder(private val binding: ViewPdpFilterCheckboxBinding,
                                    private val checkableInteractionListener: CheckableInteractionListener?)
    : BaseCheckableViewHolder<FilterTagDataCollection>(binding.root, checkableInteractionListener),
    CompoundButton.OnCheckedChangeListener {

    private lateinit var checkbox: CheckBox

    init {
        initView()
    }

    private fun initView(){
        binding?.let {
            checkbox = it.checboxFilterTag
        }
    }

    override fun bind(element: FilterTagDataCollection) {
        super.bind(element)
        binding?.let {
            it.tgFilterTag.text = element.value
            it.root?.let {
                toggle()
            }
        }
    }

    override fun getCheckable(): CompoundButton {
        return checkbox
    }

    companion object {
        val LAYOUT = R.layout.view_pdp_filter_checkbox
    }
}