package com.tokopedia.otp.notif.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.otp.R
import com.tokopedia.otp.notif.domain.pojo.ListDeviceData
import com.tokopedia.unifycomponents.setImage
import kotlinx.android.synthetic.main.item_device_active_push_notif.view.*

/**
 * Created by Ade Fulki on 05/10/20.
 */

class ActiveDevicesAdapter(private val list: ArrayList<ListDeviceData>)
    : RecyclerView.Adapter<ActiveDevicesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_device_active_push_notif, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.view.icon_device?.setImage(R.drawable.ic_phone_otp, 0f)
        holder.view.device_name?.text = list[position].deviceName

        if ((position + 1) == list.size) {
            holder.view.view.visibility = View.INVISIBLE
        }
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view)

}