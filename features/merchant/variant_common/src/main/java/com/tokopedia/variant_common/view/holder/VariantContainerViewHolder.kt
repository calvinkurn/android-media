package com.tokopedia.variant_common.view.holder

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.adapter.VariantOptionAdapter

/**
 * Created by mzennis on 2020-03-11.
 */
class VariantContainerViewHolder(val view: View, val listener: ProductVariantListener) : RecyclerView.ViewHolder(view), ProductVariantListener by listener {

    private val variantOptionAdapter = VariantOptionAdapter(this)
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private val txtVariantSelectedOption = view.findViewById<TextView>(R.id.txtVariantSelectedOption)
    private val txtVariantStockWording = view.findViewById<TextView>(R.id.txtVariantStockWording)
    private val txtVariantCategoryName = view.findViewById<TextView>(R.id.txtVariantCategoryName)
    private val txtVariantGuideline = view.findViewById<TextView>(R.id.txtVariantGuideline)
    private val rvVariant = view.findViewById<RecyclerView>(R.id.rv_variant)

    private val context: Context
        get() = view.context

    init {
        rvVariant.adapter = variantOptionAdapter
        rvVariant.layoutManager = layoutManager
        rvVariant.setHasFixedSize(true)
        rvVariant.itemAnimator = null
    }

    fun bind(data: VariantCategory, isOptionChanged: Boolean) {
        if (isOptionChanged) {
            setSelectedOptionText(data)
            setStockText(data)
            variantOptionAdapter.setData(data.variantOptions)
        }
    }

    fun bind(data: VariantCategory) {
        setSelectedOptionText(data)
        setStockText(data)

        if (data.variantGuideline.isNotEmpty() && !listener.onVariantGuideLineHide()) {
            txtVariantGuideline.show()
            txtVariantGuideline.setOnClickListener {
                listener.onVariantGuideLineClicked(data.variantGuideline)
            }
        } else {
            txtVariantGuideline.hide()
        }

        variantOptionAdapter.setData(data.variantOptions)
    }

    override fun onSelectionChanged(view: View, position: Int) {
        if (!layoutManager.isViewPartiallyVisible(view, true, true))
            view.post { rvVariant.smoothScrollToPosition(position) }
    }

    private fun setSelectedOptionText(data: VariantCategory) {
        txtVariantCategoryName.text = context.getString(R.string.variant_option_builder_1, data.name)

        if (data.getSelectedOption() == null) {
            txtVariantSelectedOption.text = context.getString(R.string.variant_option_builder_2, data.variantOptions.size)
            txtVariantSelectedOption.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        } else {
            txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            txtVariantSelectedOption.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
    }

    private fun setStockText(data: VariantCategory) {
        if (data.isLeaf && listener.getStockWording() != "") {
            txtVariantStockWording.show()
            txtVariantStockWording.text = MethodChecker.fromHtml(listener.getStockWording())
        } else {
            txtVariantStockWording.hide()
        }
    }

    companion object {
        const val VARIANT_OPTION_CHANGED = "option_changed"
    }
}