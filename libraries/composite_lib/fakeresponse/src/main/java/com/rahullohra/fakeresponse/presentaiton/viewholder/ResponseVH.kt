package com.rahullohra.fakeresponse.presentaiton.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.ResponseItemType
import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.Router

class ResponseVH(itemView: View, val itemClickCallback: (ResponseListData, Boolean) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.fake_item_response
    }

    val tvQueryName: TextView = itemView.findViewById(R.id.tvQueryName)
    val tvCustomName: TextView = itemView.findViewById(R.id.tvCustomName)
    val cb: CheckBox = itemView.findViewById(R.id.cb)

    fun setData(data: ResponseListData) {
        tvQueryName.text = data.title
        if (!TextUtils.isEmpty(data.customName)) {
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

        itemView.setOnClickListener {
            if (data.responseType == ResponseItemType.REST) {
                Router.routeToAddRest(it.context, data.id)
            } else {
                Router.routeToAddGql(it.context, data.id)
            }
        }
    }

}