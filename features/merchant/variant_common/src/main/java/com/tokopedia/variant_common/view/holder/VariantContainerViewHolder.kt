package com.tokopedia.variant_common.view.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.adapter.VariantOptionAdapter


/**
 * Created by mzennis on 2020-03-10.
 */
class VariantContainerViewHolder(val view: View, val listener: ProductVariantListener) : RecyclerView.ViewHolder(view) {

    private var variantCategoryName = view.findViewById<Typography>(R.id.txtVariantCategoryName)
    private var variantSelectedOption = view.findViewById<Typography>(R.id.txtVariantSelectedOption)
    private var variantStockWording = view.findViewById<Typography>(R.id.txtVariantStockWording)
    private var variantGuideline = view.findViewById<Typography>(R.id.txtVariantGuideline)
    private var rvChildVariant = view.findViewById<RecyclerView>(R.id.rv_variant)

    private var variantOptionAdapter: VariantOptionAdapter? = null

    fun bind(data: VariantCategory) = with(view) {
        variantOptionAdapter = VariantOptionAdapter(listener)
        if (data.getPositionSelectedOption() > 4) {
            rvChildVariant.layoutManager?.scrollToPosition(data.getPositionSelectedOption())
        }
        rvChildVariant.adapter = variantOptionAdapter

        variantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.name)

        if (data.getSelectedOption() == null) {
            variantSelectedOption.text = context.getString(R.string.variant_option_builder_2, data.variantOptions.size)
        } else {
            variantSelectedOption.text = data.getSelectedOption()?.variantName
        }

        if (data.isLeaf && listener.getStockWording() != "") {
            variantStockWording.show()
            variantStockWording.text = MethodChecker.fromHtml(listener.getStockWording())
        } else {
            variantStockWording.hide()
        }

        if (data.variantGuideline.isNotEmpty()) {
            variantGuideline.show()
            variantGuideline.setOnClickListener {
                listener.onVariantGuideLineClicked(data.variantGuideline)
            }
        } else {
            variantGuideline.hide()
        }

        rvChildVariant.itemAnimator = null
        variantOptionAdapter?.setData(data)
    }
}