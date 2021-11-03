package com.tokopedia.imagepicker_insta.views.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.models.NoPermissionData
import com.tokopedia.imagepicker_insta.views.viewholders.NoPermissionViewHolder

class NoPermissionAdapter(private val dataList: List<NoPermissionData>) : RecyclerView.Adapter<NoPermissionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoPermissionViewHolder {
        return NoPermissionViewHolder.getInstance(parent)
    }

    override fun onBindViewHolder(holder: NoPermissionViewHolder, position: Int) {
        holder.setData(dataList[position].iconRes, dataList[position].title, dataList[position].permissionFunc.invoke())

        holder.btn.setOnClickListener {
            dataList[position].requestPermission.invoke()
        }
        if (position == itemCount - 1) {
            holder.divider.visibility = View.GONE
        } else {
            holder.divider.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}