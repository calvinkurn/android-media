package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopPerformanceWidgetImageTextComponentBinding
import com.tokopedia.shop.pageheader.presentation.ShopPageHeaderActionButtonWidgetMarginItemDivider
import com.tokopedia.shop.pageheader.presentation.adapter.ShopImageTextComponentImagesAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderImageTextComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderPerformanceWidgetImageTextComponentViewHolder(
    itemView: View,
    private val shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
    private val listener: Listener
) : AbstractViewHolder<ShopPageHeaderImageTextComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_performance_widget_image_text_component
    }

    interface Listener {
        fun onImpressionShopPerformanceWidgetImageTextItem(
            componentModel: ShopPageHeaderImageTextComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopPerformanceWidgetImageTextComponentBinding? by viewBinding()
    private val recyclerViewImages: RecyclerView? = viewBinding?.rvImages
    private val textLabel: TextView? = viewBinding?.textLabel
    private val constraintLayout: ConstraintLayout? = viewBinding?.constraintLayoutImageTextComponent

    override fun bind(model: ShopPageHeaderImageTextComponentUiModel) {
        configureLayoutPosition(model)
        setLayoutData(model)
        itemView.addOnImpressionListener(model) {
            listener.onImpressionShopPerformanceWidgetImageTextItem(
                model,
                shopPageHeaderWidgetUiModel
            )
        }
    }

    private fun setLayoutData(model: ShopPageHeaderImageTextComponentUiModel) {
        val labelValue = model.textComponent.data.textHtml
        textLabel?.text = MethodChecker.fromHtml(labelValue)
        setRecycleViewImagesData(model.images.data)
    }

    private fun configureLayoutPosition(model: ShopPageHeaderImageTextComponentUiModel) {
        val valueStyleImages = model.images.style
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        if (valueStyleImages == 1) {
            constraintSet.connect(recyclerViewImages?.id.orZero(), ConstraintSet.TOP, textLabel?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.connect(textLabel?.id.orZero(), ConstraintSet.BOTTOM, recyclerViewImages?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(textLabel?.id.orZero(), ConstraintSet.TOP, constraintLayout?.id.orZero(), ConstraintSet.TOP)
        } else {
            constraintSet.connect(textLabel?.id.orZero(), ConstraintSet.TOP, recyclerViewImages?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.connect(recyclerViewImages?.id.orZero(), ConstraintSet.BOTTOM, textLabel?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(recyclerViewImages?.id.orZero(), ConstraintSet.TOP, constraintLayout?.id.orZero(), ConstraintSet.TOP)
        }
        constraintSet.applyTo(constraintLayout)
    }

    private fun setRecycleViewImagesData(listImages: List<ShopPageHeaderImageTextComponentUiModel.Images.Data>) {
        recyclerViewImages?.apply {
            val adapter = ShopImageTextComponentImagesAdapter()
            this.adapter = adapter
            val manager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            layoutManager = manager
            if (itemDecorationCount == 0)
                addItemDecoration(ShopPageHeaderActionButtonWidgetMarginItemDivider())
            adapter.setImagesData(listImages)
        }
    }
}
