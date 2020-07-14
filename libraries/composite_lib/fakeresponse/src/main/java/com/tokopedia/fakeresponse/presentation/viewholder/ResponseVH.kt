package com.tokopedia.fakeresponse.presentation.viewholder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.Router
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.data.models.SearchType
import java.text.DateFormat

class ResponseVH(itemView: View, val itemClickCallback: (SearchType, Boolean) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.fake_item_response
    }

    val tvQueryName: TextView = itemView.findViewById(R.id.tvQueryName)
    val tvCustomName: TextView = itemView.findViewById(R.id.tvCustomName)
    val cb: CheckBox = itemView.findViewById(R.id.cb)
    val imageCheck: AppCompatImageView = itemView.findViewById(R.id.imageCheck)
    val tvResponseType: AppCompatTextView = itemView.findViewById(R.id.tvResponseType)
    val tvLastUpdated: AppCompatTextView = itemView.findViewById(R.id.tvLastUpdated)

    fun setData(data: ResponseListData) {
        tvQueryName.text = data.title
        if (!data.customName.isNullOrEmpty()) {
            tvCustomName.text = data.customName
            tvCustomName.visibility = View.VISIBLE
        } else {
            tvCustomName.visibility = View.GONE
        }

        cb.setOnCheckedChangeListener(null)
        cb.isChecked = data.isChecked
        cb.setOnCheckedChangeListener { buttonView, isChecked ->
            itemClickCallback.invoke(data, isChecked)
        }

        imageCheck.visibility = if (data.isInExportMode) View.VISIBLE else View.GONE

        itemView.setOnClickListener {
            if (data.isInExportMode) {
                handleExportClick(data)
            } else {
                handleNormalCLickBehaviour(data, it.context)
            }
        }
        if (data.responseType == ResponseItemType.GQL) {
            tvResponseType.text = itemView.context.getString(R.string.gql_text)
            tvResponseType.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.gqlColorBlue))
        } else {
            tvResponseType.text = itemView.context.getString(R.string.gql_rest_text)
            tvResponseType.supportBackgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.gqlColorPurple))
        }
        data.updatedAt?.let {
            tvLastUpdated.text = DateFormat.getTimeInstance().format(data.updatedAt)
        }
    }

    fun handleExportClick(data: ResponseListData) {
        data.isSelectedForExport = !data.isSelectedForExport

        if (data.isSelectedForExport) {
            imageCheck.setBackgroundResource(R.drawable.fake_blue_circle)
        } else {
            imageCheck.setBackgroundResource(R.drawable.fake_grey_circle)
        }
    }

    fun handleNormalCLickBehaviour(data: ResponseListData, context: Context) {
        if (data.responseType == ResponseItemType.REST) {
            Router.routeToAddRest(context, data.id)
        } else {
            Router.routeToAddGql(context, data.id)
        }
    }
}