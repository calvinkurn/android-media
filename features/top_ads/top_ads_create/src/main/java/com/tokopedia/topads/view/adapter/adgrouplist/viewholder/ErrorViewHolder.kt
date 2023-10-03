package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.topads.create.R
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.topads.create.databinding.AdGroupErrorViewholderLayoutBinding
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ErrorViewHolder(itemView:View,listener:ErrorListener? = null) : AbstractViewHolder<ErrorUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.ad_group_error_viewholder_layout
    }

    private val binding: AdGroupErrorViewholderLayoutBinding? by viewBinding()

    init {
        binding?.error?.setActionClickListener {
            listener?.onErrorActionClicked()
        }
    }

    override fun bind(data: ErrorUiModel?) {
       data?.let {
           binding?.error?.setType(it.errorType)
           when(it.errorType){
               GlobalError.SERVER_ERROR -> {
                   binding?.error?.apply {
                       errorTitle.text = context.getString(R.string.ad_group_connection_error_title)
                       errorDescription.text = context.getString(R.string.ad_group_connection_error_desc)
                       errorAction.visibility = View.VISIBLE
                       errorAction.text = context.getString(R.string.ad_group_connection_error_cta)
                   }
               }
               GlobalError.PAGE_NOT_FOUND -> {
                   binding?.error?.apply {
                       errorTitle.text = context.getString(topadscommonR.string.topads_commong_ad_group_search_error_title)
                       errorDescription.text = context.getString(topadscommonR.string.topads_common_ad_group_search_error_desc)
                       errorAction.visibility = View.GONE
                   }
               }
               else -> {}
           }
       }
    }

    interface ErrorListener{
        fun onErrorActionClicked()
    }
}
