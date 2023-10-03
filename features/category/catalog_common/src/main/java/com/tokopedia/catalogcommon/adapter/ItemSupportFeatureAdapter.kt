package com.tokopedia.catalogcommon.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalogcommon.databinding.ItemSupportFeatureBinding
import com.tokopedia.catalogcommon.uimodel.SupportFeaturesUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.dpToPx

class ItemSupportFeatureAdapter(private val itemList: List<SupportFeaturesUiModel.ItemSupportFeaturesUiModel>) :
    RecyclerView.Adapter<ItemSupportFeatureAdapter.ViewHolder>() {


    companion object {
        private const val BORDER_COLOR = "#AAB4C8"
        private const val CORNER_RADIUS = 12f
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSupportFeatureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUiModel = itemList[position]
        holder.bindToView(itemUiModel)
        setupBackground(holder.itemView,itemUiModel.backgroundColor)
    }

    private fun setupBackground(itemView: View, backgroundColor: Int) {
        val displayMetrics = itemView.resources.displayMetrics
        val shapeDrawable = GradientDrawable()
        shapeDrawable.shape = GradientDrawable.RECTANGLE

        shapeDrawable.cornerRadius = CORNER_RADIUS.dpToPx()
        shapeDrawable.setStroke(
            1.dpToPx(displayMetrics),
            BORDER_COLOR.stringHexColorParseToInt(30)
        )
        shapeDrawable.setColor(backgroundColor.orDefaultColor(itemView.context))
        itemView.background = shapeDrawable
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: ItemSupportFeatureBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val ivImage = itemView.ivIcon
        private val tvTitle = itemView.tvTitle
        private val tvDescription = itemView.tvDescription


        fun bindToView(itemUiModel: SupportFeaturesUiModel.ItemSupportFeaturesUiModel) {
            ivImage.loadImage(itemUiModel.icon)
            tvTitle.text = itemUiModel.title
            tvDescription.text = itemUiModel.description
            tvDescription.setTextColor(itemUiModel.descColor)
            tvTitle.setTextColor(itemUiModel.titleColor)

        }

    }
}
