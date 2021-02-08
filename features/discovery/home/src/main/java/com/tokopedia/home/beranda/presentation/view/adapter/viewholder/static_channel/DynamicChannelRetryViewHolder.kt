package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelRetryModel
import kotlinx.android.synthetic.main.home_retry_dynamic_channel.view.*

/**
 * @author by DevAra on 02/04/20.
 */
class DynamicChannelRetryViewHolder(itemView: View, val listener: HomeCategoryListener) : AbstractViewHolder<DynamicChannelRetryModel?>(itemView) {
    private var mLastClickTime: Long = 0
    private val CLICK_TIME_INTERVAL: Long = 500

    override fun bind(element: DynamicChannelRetryModel?) {
        if (element?.isLoading == false) {
            itemView.progressBar.visibility = View.GONE
            itemView.dynamic_channel_button_retry.visibility = View.VISIBLE
        } else {
            itemView.progressBar.visibility = View.VISIBLE
            itemView.dynamic_channel_button_retry.visibility = View.GONE
        }

        itemView.dynamic_channel_button_retry.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - mLastClickTime > CLICK_TIME_INTERVAL) {
                listener.onDynamicChannelRetryClicked()
            }
            mLastClickTime = now

            itemView.progressBar.visibility = View.VISIBLE
            it.visibility = View.GONE
        }
    }

    companion object {
        private val TAG = DynamicChannelRetryViewHolder::class.java.simpleName

        @LayoutRes
        val LAYOUT = R.layout.home_retry_dynamic_channel
    }

}