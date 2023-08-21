package com.tokopedia.home_account.privacy_account.view.adapter.viewholder

/**
 * Created by Yoris on 04/08/21.
 */

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.LayoutItemAccountBinding
import com.tokopedia.home_account.privacy_account.data.UserAccountDataView
import com.tokopedia.home_account.privacy_account.listener.AccountItemListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.noreflection.viewBinding
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
open class AccountViewHolder(
    private val listener: AccountItemListener,
    view: View
): RecyclerView.ViewHolder(view) {

    private val binding by viewBinding(LayoutItemAccountBinding::bind)

    fun bind(item: UserAccountDataView) {
        binding?.itemAccountPartnerName?.text = item.partnerName
        if(item.isLinked) {
            binding?.itemAccountLinkBtn?.hide()
            binding?.itemAccountViewBtn?.show()
            binding?.itemAccountLinkedDate?.show()
            binding?.itemAccountContainer?.setOnClickListener { listener.onViewAccountClicked() }
            binding?.itemAccountLinkedDate?.text = item.linkDate
        }
        else  {
            binding?.itemAccountLinkedDate?.hide()
            binding?.itemAccountLinkBtn?.show()
            binding?.itemAccountViewBtn?.hide()
            binding?.itemAccountContainer?.setOnClickListener { listener.onLinkAccountClicked() }
        }

        binding?.itemAccountStatus?.text = item.status
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.layout_item_account
    }

}
