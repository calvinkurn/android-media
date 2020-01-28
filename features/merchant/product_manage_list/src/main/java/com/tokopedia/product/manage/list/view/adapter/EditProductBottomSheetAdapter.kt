package com.tokopedia.product.manage.list.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.view.bottomsheets.EditProductBottomSheet

class EditProductBottomSheetAdapter(private val listener: EditProductBottomSheet.EditProductInterface)
    : RecyclerView.Adapter<EditProductBottomSheetAdapter.EditProductBottomSheetViewHolder>() {

    private var data: List<BulkBottomSheetType> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): EditProductBottomSheetViewHolder {
        return EditProductBottomSheetViewHolder(LayoutInflater.from(parent.context).inflate(com.tokopedia.product.manage.list.R.layout.item_bs_edit, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: EditProductBottomSheetViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun loadInitialData(listData: List<BulkBottomSheetType>) {
        data = listData
        notifyDataSetChanged()
    }

    fun clearData(listOfDefaultObject: List<BulkBottomSheetType>) {
        data = listOfDefaultObject
        notifyDataSetChanged()
    }

    fun setDataResult(etalaseValue: BulkBottomSheetType.EtalaseType?, stockValue: BulkBottomSheetType.StockType?) {
        if (etalaseValue != null) {
            data.getOrNull(0)?.editValue = etalaseValue.etalaseValue
            (data.getOrNull(0) as? BulkBottomSheetType.EtalaseType)?.etalaseId = etalaseValue.etalaseId
        } else if (stockValue != null) {
            data.getOrNull(1)?.editValue = stockValue.getStockStatusProductView()
        }
        notifyDataSetChanged()
    }


    inner class EditProductBottomSheetViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var imgDrawable = view.findViewById<ImageView>(com.tokopedia.product.manage.list.R.id.img_bs_edit)
        private var txtTitle = view.findViewById<TextView>(com.tokopedia.product.manage.list.R.id.txt_title_edit)
        private var txtValue = view.findViewById<TextView>(com.tokopedia.product.manage.list.R.id.txt_value_edit)
        private var imgArrow = view.findViewById<ImageView>(com.tokopedia.product.manage.list.R.id.img_arrow_bulk)

        fun bind(data: BulkBottomSheetType) {
            view.setOnClickListener {
                when (data) {
                    is BulkBottomSheetType.EtalaseType -> listener.goToEtalasePicker(data.etalaseId)
                    is BulkBottomSheetType.StockType -> listener.goToEditStock()
                    else -> listener.goToConfirmationBottomSheet(true)
                }
            }

            if (data.shouldDisplayDrawable) {
                imgDrawable.visibility = View.VISIBLE
            } else {
                imgDrawable.visibility = View.GONE
            }

            txtTitle.text = view.resources.getString(data.labelTitle)

            if (data.editValue == "" || data.editValue == "0") {
                txtValue.visibility = View.GONE
                imgArrow.visibility = View.VISIBLE
                txtValue.text = ""
            } else {
                txtValue.text = data.editValue
                txtValue.visibility = View.VISIBLE
                imgArrow.visibility = View.GONE
            }
        }
    }
}