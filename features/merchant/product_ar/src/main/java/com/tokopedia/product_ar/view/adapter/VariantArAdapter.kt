package com.tokopedia.product_ar.view.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.view.ImageRoundedBorderSelectionView
import com.tokopedia.product_ar.view.SELECTMODE
import com.tokopedia.unifyprinciples.Typography

class VariantArAdapter : RecyclerView.Adapter<VariantArAdapter.ItemArImage>() {

    var counter: Int = 1

    private var arImageDatas: List<ModifaceUiModel> = listOf()

    fun setInitialData(arImageDatas: List<ModifaceUiModel>) {
        this.arImageDatas = arImageDatas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemArImage {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_variant_ar_view, parent, false)

        return ItemArImage(view)
    }

    override fun onBindViewHolder(holder: ItemArImage, position: Int) {
        holder.bind(arImageDatas[position])
    }

    override fun getItemCount(): Int = arImageDatas.size

    inner class ItemArImage(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgVariant = itemView.findViewById<ImageRoundedBorderSelectionView>(R.id.img_item_variant_ar)
        private val txtVariantName = itemView.findViewById<Typography>(R.id.txt_variant_ar)

        fun bind(data: ModifaceUiModel) {
            imgVariant.loadImage(data.backgroundUrl)
            txtVariantName.text = data.productName
            setupImageColor(data.modifaceProvider)

            itemView.setOnClickListener {
                imgVariant?.run {

                    imgVariant.mode = SELECTMODE.MULTIPLE
                    imgVariant.textCounter = counter.toString()
                    if (setSelected) {
                        txtVariantName.setTextColor(
                                ContextCompat.getColor(this.context,
                                        com.tokopedia.unifyprinciples.R.color.Unify_NN400)
                        )
                        counter--
                    } else {
                        txtVariantName.setTextColor(
                                ContextCompat.getColor(this.context,
                                        com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                        )
                        counter++
                    }
                    imgVariant.setSelected = !imgVariant.setSelected
                }
            }
        }

        private fun setupImageColor(data: ModifaceProvider) {
            try {
                val color = Color.argb(255, 218, 153, 155)
                imgVariant.setColorFilter(color, PorterDuff.Mode.DST_ATOP)
            } catch (e: Throwable) {
                //noop
            }
        }
    }
}

