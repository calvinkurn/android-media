package com.tokopedia.variant_common.view.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.adapter.VariantOptionAdapter
import kotlinx.android.synthetic.main.item_variant_container_view_holder.view.*


/**
 * Created by mzennis on 2020-03-11.
 */
class VariantContainerViewHolder(val view: View, val listener: ProductVariantListener) : RecyclerView.ViewHolder(view) {

    private var variantOptionAdapter: VariantOptionAdapter? = null

    fun bind(data: VariantCategory) = with(view) {
        variantOptionAdapter = VariantOptionAdapter(listener)
        if (data.getPositionSelectedOption() > 4) {
            rv_variant.layoutManager?.scrollToPosition(data.getPositionSelectedOption())
        }
        rv_variant.adapter = variantOptionAdapter

        txtVariantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.name)

        if (data.getSelectedOption() == null) {
            txtVariantSelectedOption.text = context.getString(R.string.variant_option_builder_2, data.variantOptions.size)
            txtVariantSelectedOption.setTextColor(MethodChecker.getColor(view.context, R.color.Neutral_N700_44))
        } else {
            txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            txtVariantSelectedOption.setTextColor(MethodChecker.getColor(view.context, R.color.Neutral_N700_96))
        }

        if (data.isLeaf && listener.getStockWording() != "") {
            txtVariantStockWording.show()
            txtVariantStockWording.text = MethodChecker.fromHtml(listener.getStockWording())
        } else {
            txtVariantStockWording.hide()
        }

        if (data.variantGuideline.isNotEmpty()) {
            txtVariantGuideline.show()
            txtVariantGuideline.setOnClickListener {
                listener.onVariantGuideLineClicked(data.variantGuideline)
            }
        } else {
            txtVariantGuideline.hide()
        }

        rv_variant.itemAnimator = null
        variantOptionAdapter?.setData(data)
    }
}