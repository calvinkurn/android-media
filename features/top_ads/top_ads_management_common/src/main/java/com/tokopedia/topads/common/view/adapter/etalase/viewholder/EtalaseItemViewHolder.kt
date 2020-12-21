package com.tokopedia.topads.common.view.adapter.etalase.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.etalase.viewmodel.EtalaseItemViewModel
import kotlinx.android.synthetic.main.topads_edit_select_layout_product_filter_list_item.view.*

/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseItemViewHolder(val view: View, var actionClick: ((pos:Int) -> Unit)?): EtalaseViewHolder<EtalaseItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_filter_list_item
    }

    init {
        view.setOnClickListener {
            actionClick?.invoke(adapterPosition)
        }
    }

    override fun bind(item: EtalaseItemViewModel) {
        item.let {
            view.title.text = it.result.name
            view.check.setImageDrawable(AppCompatResources.getDrawable(view.context, com.tokopedia.topads.common.R.drawable.topads_ic_check))
            if(item.checked){
                view.check.visibility = View.VISIBLE
            } else {
                view.check.visibility = View.INVISIBLE
            }
        }
    }

}