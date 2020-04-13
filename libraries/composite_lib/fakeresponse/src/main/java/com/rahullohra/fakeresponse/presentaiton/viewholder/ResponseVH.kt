package com.rahullohra.fakeresponse.presentaiton.viewholder

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.data.models.ResponseItemType
import com.rahullohra.fakeresponse.data.models.ResponseListData
import com.rahullohra.fakeresponse.Router
import com.rahullohra.fakeresponse.data.models.SearchType

class ResponseVH(itemView: View, val itemClickCallback: (SearchType, Boolean) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.fake_item_response
    }

    val tvQueryName: TextView = itemView.findViewById(R.id.tvQueryName)
    val tvCustomName: TextView = itemView.findViewById(R.id.tvCustomName)
    val cb: CheckBox = itemView.findViewById(R.id.cb)
    val imageCheck: AppCompatImageView = itemView.findViewById(R.id.imageCheck)


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
            if(data.isInExportMode){
                handleExportClick(data)
            }else {
                handleNormalCLickBehaviour(data, it.context)
            }
        }
    }

    fun handleExportClick(data: ResponseListData){
        if(data.isSelectedForExport){
            imageCheck.setBackgroundResource(R.drawable.fake_blue_circle)
        }else{
            imageCheck.setBackgroundResource(R.drawable.fake_grey_circle)
        }
        data.isSelectedForExport = !data.isSelectedForExport
    }

    fun handleNormalCLickBehaviour(data: ResponseListData, context:Context){
        if (data.responseType == ResponseItemType.REST) {
            Router.routeToAddRest(context, data.id)
        } else {
            Router.routeToAddGql(context, data.id)
        }
    }
}