package com.tokopedia.product.addedit.optionpicker.adapter

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.optionpicker.model.OptionModel
import com.tokopedia.unifyprinciples.Typography
/**
 * Created by faisalramd on 2020-03-09.
 */

class OptionViewHolder(val view: View?,
                       private val listener: OptionTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<OptionModel>(view) {
    override fun bind(element: OptionModel) {
        val tvOption: Typography? = view?.findViewById(R.id.tv_option)
        val ivOption: ImageView? = view?.findViewById(R.id.iv_option)
        tvOption?.text = element.text
        ivOption?.visibility = if (element.isSelected) View.VISIBLE else View.GONE
        itemView.setOnClickListener {
            listener?.onClick(element.text, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_option_check
    }
}