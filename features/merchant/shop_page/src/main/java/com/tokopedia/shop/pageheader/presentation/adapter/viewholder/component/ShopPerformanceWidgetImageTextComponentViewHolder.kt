package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.component

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.ShopPageActionButtonWidgetMarginItemDivider
import com.tokopedia.shop.pageheader.presentation.adapter.ShopImageTextComponentImagesAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderImageTextComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel

class ShopPerformanceWidgetImageTextComponentViewHolder(
        itemView: View,
        private val shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
        private val listener: Listener
) : AbstractViewHolder<ShopHeaderImageTextComponentUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_performance_widget_image_text_component
    }

    interface Listener {
        fun onImpressionShopPerformanceWidgetImageTextItem(
                componentModel: ShopHeaderImageTextComponentUiModel,
                shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )
    }

    private val recyclerViewImages: RecyclerView? = itemView.findViewById(R.id.rv_images)
    private val textLabel: TextView? = itemView.findViewById(R.id.text_label)
    private val constraintLayout: ConstraintLayout? = itemView.findViewById(R.id.constraint_layout_image_text_component)


    override fun bind(model: ShopHeaderImageTextComponentUiModel) {
        configureLayoutPosition(model)
        setLayoutData(model)
        itemView.addOnImpressionListener(model){
            listener.onImpressionShopPerformanceWidgetImageTextItem(
                    model,
                    shopHeaderWidgetUiModel
            )
        }
    }

    private fun setLayoutData(model: ShopHeaderImageTextComponentUiModel) {
        val labelValue = model.textComponent.data.textHtml
        textLabel?.text = MethodChecker.fromHtml(labelValue)
        setRecycleViewImagesData(model.images.data)
    }

    private fun configureLayoutPosition(model: ShopHeaderImageTextComponentUiModel) {
        val valueStyleImages = model.images.style
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        if (valueStyleImages == 1){
            constraintSet.connect(recyclerViewImages?.id.orZero(), ConstraintSet.TOP,textLabel?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.connect(textLabel?.id.orZero(), ConstraintSet.BOTTOM, recyclerViewImages?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(textLabel?.id.orZero(), ConstraintSet.TOP, constraintLayout?.id.orZero(), ConstraintSet.TOP)
        }else{
            constraintSet.connect(textLabel?.id.orZero(), ConstraintSet.TOP,recyclerViewImages?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.connect(recyclerViewImages?.id.orZero(), ConstraintSet.BOTTOM, textLabel?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(recyclerViewImages?.id.orZero(), ConstraintSet.TOP, constraintLayout?.id.orZero(), ConstraintSet.TOP)
        }
        constraintSet.applyTo(constraintLayout)
    }

    private fun setRecycleViewImagesData(listImages: List<ShopHeaderImageTextComponentUiModel.Images.Data>) {
        recyclerViewImages?.apply {
            val adapter = ShopImageTextComponentImagesAdapter()
            this.adapter = adapter
            val manager= LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
            layoutManager = manager
            if (itemDecorationCount == 0)
                addItemDecoration(ShopPageActionButtonWidgetMarginItemDivider())
            adapter.setImagesData(listImages)
        }
    }

}