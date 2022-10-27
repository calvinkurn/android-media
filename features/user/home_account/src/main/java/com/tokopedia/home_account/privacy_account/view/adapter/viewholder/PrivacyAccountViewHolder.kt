package com.tokopedia.home_account.privacy_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.R
import com.tokopedia.home_account.consentWithdrawal.data.ConsentGroupDataModel
import com.tokopedia.home_account.databinding.ViewItemAccountPrivacyBinding
import com.tokopedia.home_account.privacy_account.listener.PrivacyAccountListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class PrivacyAccountViewHolder(
    private val listener: PrivacyAccountListener,
    view: View
) : RecyclerView.ViewHolder(view) {

    private val itemBinding by viewBinding(ViewItemAccountPrivacyBinding::bind)

    fun bind(item: ConsentGroupDataModel) {
        itemBinding?.apply {
            privacyItemImage.apply {
                clearImage()
                loadIcon(item.groupImage) {
                    useCache(true)
                }
            }
            privacyItemTitle.text = item.groupTitle
            privacyItemDescription.text = item.groupSubtitle
            privacyItemTextButton.hide()

            root.setOnClickListener {
                listener.onConsentGroupClicked(item.id)
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.view_item_account_privacy
    }
}
