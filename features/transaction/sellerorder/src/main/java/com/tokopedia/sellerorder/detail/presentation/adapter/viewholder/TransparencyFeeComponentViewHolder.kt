package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.updatePadding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.shape.CornerFamily
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemComponentIncomeDetailBinding
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.DetailTransparencyFeeAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.TransparencyFeeAttributesAdapterFactoryImpl
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentUiModel
import com.tokopedia.unifycomponents.toPx

class TransparencyFeeComponentViewHolder(
    view: View?,
    actionListener: DetailTransparencyFeeAdapterFactoryImpl.ActionListener
) : AbstractViewHolder<TransparencyFeeComponentUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_component_income_detail

        const val ROUNDED_RADIUS = 4f

        private const val PADDING_VERTICAL_CONTENT = 4
    }

    private val binding = ItemComponentIncomeDetailBinding.bind(itemView)

    private val typeFactory = TransparencyFeeAttributesAdapterFactoryImpl(actionListener)

    private val attributesAdapter = BaseAdapter(typeFactory)

    override fun bind(element: TransparencyFeeComponentUiModel) {
        setupDividerVerticalRadius(element.isFirstIndex, element.isLastIndex)
        setupValue(element.value)
        setupVerticalPadding(element)
        setupBottomDivider(element.isLastIndex)
        setAttributes(element.attributes)
    }

    private fun setupDividerVerticalRadius(isFirstIndex: Boolean, isLastIndex: Boolean) {
        val shapeDivider = binding.dividerVerticalIncomeDetail
        shapeDivider.shapeAppearanceModel = shapeDivider.shapeAppearanceModel.toBuilder().apply {
            if (isFirstIndex) {
                setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
                setTopRightCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
            }
            if (isLastIndex) {
                setBottomLeftCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
                setBottomRightCorner(
                    CornerFamily.ROUNDED,
                    ROUNDED_RADIUS
                )
            }
        }.build()
    }

    private fun setupValue(value: String) {
        binding.tvComponentIncomeDetailValue.text = value
    }

    private fun setupVerticalPadding(element: TransparencyFeeComponentUiModel) {
        binding.rvComponentIncomeDetailAttributes.updatePadding(
            top = if (element.isFirstIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx(),
            bottom = if (element.isLastIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx()
        )
        binding.tvComponentIncomeDetailValue.updatePadding(
            top = if (element.isFirstIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx(),
            bottom = if (element.isLastIndex) Int.ZERO else PADDING_VERTICAL_CONTENT.toPx()
        )
    }

    private fun setupBottomDivider(show: Boolean) {
        binding.dividerTransparencyFeeComponent.showWithCondition(show)
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
        binding.rvComponentIncomeDetailAttributes.run {
            show()
            layoutManager = FlexboxLayoutManager(context).apply {
                alignItems = AlignItems.FLEX_START
            }
            adapter = attributesAdapter
        }
    }

    private fun hideAttributes() {
        binding.rvComponentIncomeDetailAttributes.hide()
    }

    private fun setAttributesData(attributes: List<BaseTransparencyFeeAttributes>) {
        attributesAdapter.setElement(attributes)
    }
}
