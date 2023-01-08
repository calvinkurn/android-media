package com.tokopedia.topads.common.view.adapter.etalase.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.etalase.uimodel.EtalaseItemUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Author errysuprayogi on 11,November,2019
 */
class EtalaseItemViewHolder(val view: View, var actionClick: ((pos: Int) -> Unit)?) :
    EtalaseViewHolder<EtalaseItemUiModel>(view) {

    val title: Typography? = view.findViewById(R.id.title)
    val check: ImageUnify? = view.findViewById(R.id.check)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_edit_select_layout_product_filter_list_item
    }

    init {
        view.setOnClickListener {
            actionClick?.invoke(adapterPosition)
        }
    }

    override fun bind(item: EtalaseItemUiModel) {
        item.let {
            title?.text = it.result.name
            check?.setImageDrawable(AppCompatResources.getDrawable(view.context,
                R.drawable.topads_ic_check))
            if (item.checked) {
                check?.visibility = View.VISIBLE
            } else {
                check?.visibility = View.INVISIBLE
            }
        }
    }

}