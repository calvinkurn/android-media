package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingDriverInformationBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverInformationUiModel


class DriverInformationAdapter(
    private val driverInformationList: List<DriverInformationUiModel>
) : RecyclerView.Adapter<DriverInformationAdapter.DriverInformationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverInformationViewHolder {
        val binding = ItemTokofoodOrderTrackingDriverInformationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DriverInformationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverInformationViewHolder, position: Int) {
        if (driverInformationList.isNotEmpty()) {
            holder.bind(driverInformationList[position])
        }
    }

    override fun getItemCount(): Int = driverInformationList.size

    inner class DriverInformationViewHolder(
        private val binding: ItemTokofoodOrderTrackingDriverInformationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DriverInformationUiModel) {
            with(binding) {
                imgDriverInformation.loadImage(item.iconInformationUrl)

                tvDriverInformation.text = item.informationName
            }
        }
    }
}