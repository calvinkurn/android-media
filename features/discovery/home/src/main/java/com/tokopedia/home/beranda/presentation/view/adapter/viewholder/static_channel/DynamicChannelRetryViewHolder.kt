package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelRetryModel
import com.tokopedia.home.databinding.HomeRetryDynamicChannelBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by DevAra on 02/04/20.
 */
class DynamicChannelRetryViewHolder(itemView: View, val listener: HomeCategoryListener) : AbstractViewHolder<DynamicChannelRetryModel?>(itemView) {
    private var binding: HomeRetryDynamicChannelBinding? by viewBinding()
    private var mLastClickTime: Long = 0
    private val CLICK_TIME_INTERVAL: Long = 500

    override fun bind(element: DynamicChannelRetryModel?) {
        if (element?.isLoading == false) {
            binding?.progressBar?.visibility = View.GONE
            binding?.dynamicChannelButtonRetry?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.dynamicChannelButtonRetry?.visibility = View.GONE
        }

        binding?.dynamicChannelButtonRetry?.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - mLastClickTime > CLICK_TIME_INTERVAL) {
                listener.onDynamicChannelRetryClicked()
            }
            mLastClickTime = now

            binding?.progressBar?.visibility = View.VISIBLE
            it.visibility = View.GONE
        }
    }

    companion object {
        private val TAG = DynamicChannelRetryViewHolder::class.java.simpleName

        @LayoutRes
        val LAYOUT = R.layout.home_retry_dynamic_channel
    }

}