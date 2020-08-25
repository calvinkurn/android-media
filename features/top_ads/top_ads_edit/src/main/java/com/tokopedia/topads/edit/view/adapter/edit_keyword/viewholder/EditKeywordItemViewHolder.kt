package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordItemViewModel
import kotlinx.android.synthetic.main.topads_edit_keyword_base_layout.view.*
import kotlinx.android.synthetic.main.topads_edit_keyword_edit_item_layout.view.*

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordItemViewHolder(val view: View,
                                private val actionClick: ((pos:Int) -> Unit)?) : EditKeywordViewHolder<EditKeywordItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_keyword_edit_item_layout

        private const val SPECIFIC_TYPE = "Spesifik"
        private const val BROAD_TYPE = "Luas"
        private const val EXACT_POSITIVE = 21


    }

    override fun bind(item: EditKeywordItemViewModel) {

        item.data.let {
            view.keywordName.text = it.tag
            if(it.type == EXACT_POSITIVE){
                view.keywordType.text = SPECIFIC_TYPE
            }else{
                view.keywordType.text = BROAD_TYPE
            }
            view.keywordBudget.text = it.priceBid.toString()

//            if (adapterPosition < data.size)
//                view.keywordBudget.
//                text = data[adapterPosition].toString()
            view.setOnClickListener {
                actionClick?.invoke(adapterPosition)
            }
        }
    }
}