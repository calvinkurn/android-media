package com.tokopedia.product.manage.oldlist.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.manage.oldlist.data.ConfirmationProductData
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType.Companion.STOCK_DEFAULT
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType.Companion.STOCK_UNLIMITED

class ConfirmationProductAdapter(private var data: List<ConfirmationProductData>)
    : RecyclerView.Adapter<ConfirmationProductAdapter.ConfirmationProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ConfirmationProductViewHolder {
        return ConfirmationProductViewHolder(LayoutInflater.from(parent.context).inflate(com.tokopedia.product.manage.R.layout.item_confirmation_product, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ConfirmationProductViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ConfirmationProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val txtProductTitle: TextView = view.findViewById(com.tokopedia.product.manage.R.id.txt_title_product_confirmation)
        private val imgProduct: ImageView = view.findViewById(com.tokopedia.product.manage.R.id.img_product_confirmation)
        private val txtProductStock: TextView = view.findViewById(com.tokopedia.product.manage.R.id.txt_stock_confirmation)
        private val txtProductEtalase: TextView = view.findViewById(com.tokopedia.product.manage.R.id.txt_etalase_confirmation)

        fun bind(data: ConfirmationProductData) {
            txtProductTitle.text = data.productName
            ImageHandler.loadImage(view.context, imgProduct, data.productImgUrl, com.tokopedia.design.R.drawable.ic_loading_image)

            if (data.productEtalaseName != "") {
                txtProductEtalase.visibility = View.VISIBLE
                txtProductEtalase.text = data.productEtalaseName
            } else {
                txtProductEtalase.visibility = View.GONE
            }

            if (data.statusStock != STOCK_DEFAULT) {
                txtProductStock.visibility = View.VISIBLE
                txtProductStock.text = if (data.statusStock == STOCK_UNLIMITED)
                    view.resources.getString(com.tokopedia.product.manage.item.R.string.label_always_active)
                else
                    view.resources.getString(com.tokopedia.product.manage.item.R.string.label_always_nonactive)
            } else {
                txtProductStock.visibility = View.GONE
            }
        }
    }
}