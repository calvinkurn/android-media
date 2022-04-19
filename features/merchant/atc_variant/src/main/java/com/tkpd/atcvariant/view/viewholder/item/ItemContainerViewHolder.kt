package com.tkpd.atcvariant.view.viewholder.item

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.tkpd.atcvariant.R
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.common.view.AtcVariantOptionAdapter
import java.util.*


/**
 * Created by mzennis on 2020-03-11.
 */
class ItemContainerViewHolder(val view: View, val listener: AtcVariantListener) : RecyclerView.ViewHolder(view), AtcVariantListener by listener {

    private val variantOptionAdapter = AtcVariantOptionAdapter(this)
    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private val txtVariantSelectedOption = view.findViewById<TextView>(R.id.txt_variant_selected_option)
    private val txtVariantCategoryName = view.findViewById<TextView>(R.id.txt_variant_category_name)
    private val txtVariantGuideline = view.findViewById<TextView>(R.id.txt_variant_guideline)
    private val rvVariant = view.findViewById<RecyclerView>(R.id.rv_atc_variant)

    private val context: Context
        get() = view.context

    init {
        rvVariant.adapter = variantOptionAdapter
        rvVariant.setHasFixedSize(true)
        rvVariant.itemAnimator = null
    }

    fun bind(data: VariantCategory, isOptionChanged: Boolean) {
        if (isOptionChanged) {
            setSelectedOptionText(data)

            rvVariant.layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            if (rvVariant.itemDecorationCount == 0) {
                val itemDecoration = FlexboxItemDecoration(context).apply {
                    setDrawable(ContextCompat.getDrawable(context, R.drawable.bg_atc_chip_divider))
                    setOrientation(FlexboxItemDecoration.HORIZONTAL)
                }

                rvVariant.addItemDecoration(itemDecoration)
            }
            variantOptionAdapter.setData(data.variantOptions)
        }
    }

    fun bind(data: VariantCategory) {
        setSelectedOptionText(data)

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
        txtVariantCategoryName.text = context.getString(R.string.atc_variant_option_builder_3, data.name).capitalize(Locale.getDefault())

        if (data.getSelectedOption() == null || hideVariantName()) {
            txtVariantSelectedOption.text = ""
        } else {
            txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            txtVariantSelectedOption.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
    }
}