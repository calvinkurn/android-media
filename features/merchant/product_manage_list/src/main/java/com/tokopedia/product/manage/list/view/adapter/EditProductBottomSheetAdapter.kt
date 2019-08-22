package com.tokopedia.product.manage.list.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.view.bottomsheets.EditProductBottomSheet

class EditProductBottomSheetAdapter(private var data: List<BulkBottomSheetType>,
                                    private val listener: EditProductBottomSheet.EditProductInterface)
    : RecyclerView.Adapter<EditProductBottomSheetAdapter.EditProductBottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): EditProductBottomSheetViewHolder {
        return EditProductBottomSheetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bs_edit, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: EditProductBottomSheetViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun clearData(){
        data = listOf(BulkBottomSheetType.EtalaseType(""),
                BulkBottomSheetType.StockType(""),
                BulkBottomSheetType.DeleteType())
        notifyDataSetChanged()
    }

    inner class EditProductBottomSheetViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private var imgDrawable = view.findViewById<ImageView>(R.id.img_bs_edit)
        private var txtTitle = view.findViewById<TextView>(R.id.txt_title_edit)
        private var txtValue = view.findViewById<TextView>(R.id.txt_value_edit)
        private var imgArrow = view.findViewById<ImageView>(R.id.img_arrow_bulk)

        fun bind(data: BulkBottomSheetType) {
            view.setOnClickListener {
                when (data) {
                    is BulkBottomSheetType.EtalaseType -> listener.goToEtalasePicker(data.etalaseId ?: 0)
                    is BulkBottomSheetType.DeleteType -> listener.goToEditStock()
                    else -> listener.goToEditStock()
                }
            }

            if (data.shouldDisplayDrawable) {
                imgDrawable.visibility = View.VISIBLE
            } else {
                imgDrawable.visibility = View.GONE
            }

            txtTitle.text = view.resources.getString(data.labelTitle)

            if (data.editValue == "") {
                txtValue.visibility = View.GONE
                imgArrow.visibility = View.VISIBLE
            } else {
                txtValue.visibility = View.VISIBLE
                txtValue.text = data.editValue
                imgArrow.visibility = View.GONE
            }
        }
    }
}