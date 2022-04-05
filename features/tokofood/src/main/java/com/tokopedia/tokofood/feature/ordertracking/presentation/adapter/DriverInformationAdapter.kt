package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
                val nn500Color =
                    ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_NN500)
                imgDriverInformation.setImage(item.iconInformation, nn500Color, nn500Color)
                tvDriverInformation.text = item.informationName
            }
        }
    }
}