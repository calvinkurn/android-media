package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemSubComponentIncomeDetailBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentUiModel

class TransparencyFeeSubComponentViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeSubComponentUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sub_component_income_detail
    }

    private val binding = ItemSubComponentIncomeDetailBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    private val attributesAdapter = BaseAdapter(typeFactory)

    override fun bind(element: TransparencyFeeSubComponentUiModel) {
        setupDividerVerticalRadius(element.isFirstIndex, element.isLastIndex)
        setupLabel(element.label)
        setAttributes(element.attributes)
    }

    private fun setupDividerVerticalRadius(isFirstIndex: Boolean, isLastIndex: Boolean) {
        val shapeDivider = binding.dividerVerticalSubComponentIncomeDetail
        shapeDivider.shapeAppearanceModel = shapeDivider.shapeAppearanceModel.toBuilder().apply {
            if (isFirstIndex) {
                setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
                setTopRightCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
            }
            if (isLastIndex) {
                setBottomLeftCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
                setBottomRightCorner(
                    CornerFamily.ROUNDED,
                    TransparencyFeeComponentViewHolder.ROUNDED_RADIUS
                )
            }
        }.build()
    }

    private fun setupLabel(label: String) {
        binding.tvSubComponentIncomeDetailLabel.text = label
    }

    private fun setAttributes(attributes: List<BaseTransparencyFeeAttributes>) {
        if (attributes.isNotEmpty()) {
            setupRecyclerView()
            setAttributesData(attributes)
        } else {
            hideAttributes()
        }
    }

    private fun setupRecyclerView() {
        binding.rvSubComponentIncomeDetailAttributes.run {
            show()
            layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            adapter = attributesAdapter
        }
    }

    private fun hideAttributes() {
        binding.rvSubComponentIncomeDetailAttributes.hide()
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        attributesAdapter.setElement(attributes)
    }
}
