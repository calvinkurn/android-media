package com.rahullohra.fakeresponse.presentaiton.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.ResponseListData

class ResponseVH(itemView: View, val itemClickCallback: (Int, Boolean) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun getLayout() = R.layout.item_response
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

        cb.isChecked = data.isChecked
        cb.setOnCheckedChangeListener(null)
        cb.setOnCheckedChangeListener { buttonView, isChecked ->
            itemClickCallback.invoke(data.id, isChecked)
//            cb.setOnCheckedChangeListener(null)
        }
    }

}