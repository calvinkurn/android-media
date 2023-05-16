package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyorderhistory.data.model.PmsNotification
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohPmsButtonItemBinding
import com.tokopedia.unifyorderhistory.util.UohConsts.PMS_IMAGE_URL
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter

class UohPmsButtonViewHolder(private val binding: UohPmsButtonItemBinding, private val actionListener: UohItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UohTypeData) {
        if (item.dataObject is PmsNotification) {
            binding.imagePms.loadImage(PMS_IMAGE_URL)
            actionListener?.onImpressionPmsButton()
            val counter = item.dataObject.notifications.buyerOrderStatus.paymentStatus
            if (counter > 0) {
                binding.notificationCounter.setNotification(counter.toString(), NotificationUnify.COUNTER_TYPE, NotificationUnify.COLOR_PRIMARY)
                binding.notificationCounter.show()
            } else {
                binding.notificationCounter.gone()
            }
            itemView.setOnClickListener {
                actionListener?.onPmsButtonClicked()
            }
        }
    }
}
