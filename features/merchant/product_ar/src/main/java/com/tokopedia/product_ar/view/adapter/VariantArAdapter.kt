package com.tokopedia.product_ar.view.adapter

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.util.ImageRoundedBorderSelectionView
import com.tokopedia.product_ar.view.ProductArListener
import com.tokopedia.unifyprinciples.Typography

class VariantArAdapter(private val listener: ProductArListener) : RecyclerView.Adapter<VariantArAdapter.ItemArImage>() {

    private var arImageDatas: MutableList<ModifaceUiModel> = mutableListOf()

    fun getCurrentArImageDatas(): MutableList<ModifaceUiModel> = arImageDatas

    fun updateList(newList: List<ModifaceUiModel>) {
        val diffResult = DiffUtil.calculateDiff(VariantArDiffutilCallback(arImageDatas.toMutableList(), newList))

        arImageDatas.clear()
        arImageDatas.addAll(newList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemArImage {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_variant_ar_view, parent, false)

        return ItemArImage(view)
    }

    override fun onBindViewHolder(holder: ItemArImage, position: Int) {
        holder.bind(arImageDatas[position])
    }

    override fun onBindViewHolder(holder: ItemArImage, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty() || payloads[0] !is Bundle) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            //4
            val bundle = payloads[0] as Bundle
            holder.bind(arImageDatas[position], bundle)
        }
    }

    override fun getItemCount(): Int = arImageDatas.size

    inner class ItemArImage(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgVariant = itemView.findViewById<ImageRoundedBorderSelectionView>(R.id.img_item_variant_ar)
        private val txtVariantName = itemView.findViewById<Typography>(R.id.txt_variant_ar)

        fun bind(data: ModifaceUiModel) {
            imgVariant.loadImage(data.backgroundUrl)
            txtVariantName.text = data.productName

            imgVariant?.setSelected = data.isSelected
            renderText(data.isSelected)
            setupImageColor(data.modifaceProductData)
            bindClickListener(data)
        }

        fun bind(data: ModifaceUiModel, bundle: Bundle) {
            if (bundle.containsKey("asdf")) {
                bindClickListener(data)
                renderText(data.isSelected)
                imgVariant?.setSelected = data.isSelected
            }
        }

        private fun bindClickListener(data: ModifaceUiModel) {
            itemView.setOnClickListener {
                imgVariant?.run {
                    setSelected = data.isSelected
                    listener.onVariantClicked(data.productId,
                            data.isSelected,
                            data.modifaceProductData)
                }
            }
        }

        private fun renderText(isSelected: Boolean) {
            if (isSelected) {
                txtVariantName.setTextColor(
                        ContextCompat.getColor(itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                )
            } else {
                txtVariantName.setTextColor(
                        ContextCompat.getColor(itemView.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN400)
                )
            }
        }

        private fun setupImageColor(data: MFEMakeupProduct) {
            try {
                imgVariant.setColorFilter(data.color, PorterDuff.Mode.DST_ATOP)
            } catch (e: Throwable) {
                //noop
            }
        }
    }
}

