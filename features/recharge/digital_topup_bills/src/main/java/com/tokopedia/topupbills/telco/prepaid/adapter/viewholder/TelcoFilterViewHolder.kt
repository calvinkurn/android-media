package com.tokopedia.topupbills.telco.prepaid.adapter.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.FilterTagDataCollection
import kotlinx.android.synthetic.main.item_telco_filter_product.view.*

class TelcoFilterViewHolder(itemView: View?, checkableInteractionListener: CheckableInteractionListener?)
    : BaseCheckableViewHolder<FilterTagDataCollection>(itemView, checkableInteractionListener), View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private lateinit var checkbox: CheckBox

    init {
        initView()
    }

    private fun initView() {
        itemView.let {
            checkbox = itemView.findViewById(R.id.telco_filter_checkbox)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_telco_filter_product
    }

    override fun bind(element: FilterTagDataCollection) {
        super.bind(element)
        with(itemView) {
            telco_filter_tag.text = element.value
        }
        itemView.setOnClickListener(this)
    }

    override fun getCheckable(): CompoundButton {
        return checkbox
    }

    override fun onClick(p0: View?) {
        toggle()
    }
}