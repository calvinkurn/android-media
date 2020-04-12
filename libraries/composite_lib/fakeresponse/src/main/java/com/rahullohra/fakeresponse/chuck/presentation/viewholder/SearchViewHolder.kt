package com.rahullohra.fakeresponse.chuck.presentation.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.chuck.TransactionEntity
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


        itemView.setOnClickListener {
            itemClickCallback.invoke(data)
        }
    }
}