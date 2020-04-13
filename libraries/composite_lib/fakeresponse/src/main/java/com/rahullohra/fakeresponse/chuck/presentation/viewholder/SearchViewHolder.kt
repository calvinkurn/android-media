package com.rahullohra.fakeresponse.chuck.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.data.models.ResponseListData
import java.text.DateFormat

class SearchViewHolder(itemView: View, val itemClickCallback: (TransactionEntity) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.fake_item_search
    }

    val tvResponseCode: TextView = itemView.findViewById(R.id.tvResponseCode)
    val tvHeader: TextView = itemView.findViewById(R.id.tvHeader)
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
    val imageCheck: AppCompatImageView = itemView.findViewById(R.id.imageCheck)

    fun setData(data: TransactionEntity) {
        tvResponseCode.text = data.responseCode?.toString() ?: ""

        val headerSb = StringBuilder(data.method ?: "")
        headerSb.append(" ")
        headerSb.append(data.path ?: "")
        tvHeader.text = headerSb

        tvTitle.text = data.host ?: ""

        if (data.requestDate != null) {
            tvMessage.text = DateFormat.getTimeInstance().format(data.requestDate)
        }

        imageCheck.visibility = if (data.isInExportMode) View.VISIBLE else View.GONE

        itemView.setOnClickListener {
            if(data.isInExportMode){
                handleExportClick(data)
            }else {
                handleNormalClick(data)
            }
        }
    }

    fun handleExportClick(data: TransactionEntity){
        if(data.isSelectedForExport){
            imageCheck.setBackgroundResource(R.drawable.fake_blue_circle)
        }else{
            imageCheck.setBackgroundResource(R.drawable.fake_grey_circle)
        }
        data.isSelectedForExport = !data.isSelectedForExport
    }

    fun handleNormalClick(data: TransactionEntity){
        itemClickCallback.invoke(data)
    }
}