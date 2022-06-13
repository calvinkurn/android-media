package com.tokopedia.otp.notif.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.otp.R
import com.tokopedia.otp.databinding.ItemDeviceActivePushNotifBinding
import com.tokopedia.otp.notif.domain.pojo.ListDeviceData
import com.tokopedia.unifycomponents.setImage

/**
 * Created by Ade Fulki on 05/10/20.
 */

class ActiveDevicesAdapter(private val list: ArrayList<ListDeviceData>)
    : RecyclerView.Adapter<ActiveDevicesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val viewBinding = ItemDeviceActivePushNotifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(viewBinding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.iconDevice.setImage(R.drawable.ic_phone_otp, 0f)
        holder.binding.deviceName.text = list[position].deviceName
        if ((position + 1) == list.size) {
            holder.binding.view.visibility = View.INVISIBLE
        }
    }

    class Holder(val binding: ItemDeviceActivePushNotifBinding) : RecyclerView.ViewHolder(binding.root)

}