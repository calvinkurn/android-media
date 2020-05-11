package com.tokopedia.fakeresponse.chuck.presentation.viewholder

import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.chuck.TransactionEntity
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
    val tvResponseType: AppCompatTextView = itemView.findViewById(R.id.tvResponseType)

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

        tvResponseType.text = itemView.context.getString(R.string.gql_chuck_text)
        tvResponseType.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.gqlColorLightBlue))
    }

    fun handleExportClick(data: TransactionEntity){
        data.isSelectedForExport = !data.isSelectedForExport
        if(data.isSelectedForExport){
            imageCheck.setBackgroundResource(R.drawable.fake_blue_circle)
        }else{
            imageCheck.setBackgroundResource(R.drawable.fake_grey_circle)
        }
    }

    fun handleNormalClick(data: TransactionEntity){
        itemClickCallback.invoke(data)
    }
}