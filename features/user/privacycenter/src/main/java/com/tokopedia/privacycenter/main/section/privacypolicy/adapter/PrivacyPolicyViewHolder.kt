package com.tokopedia.privacycenter.main.section.privacypolicy.adapter

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.privacycenter.databinding.PrivacyPolicyItemViewBinding
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.utils.view.binding.viewBinding

class PrivacyPolicyViewHolder(
    itemView: View,
    private val listener: PrivacyPolicyAdapter.Listener
) : BaseViewHolder(itemView) {

    fun bind(item: PrivacyPolicyDataModel) {
        PrivacyPolicyItemViewBinding.bind(itemView).run {
            itemPrivacyPolicy.run {
                if (listener.isFromBottomSheet) {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                } else {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                }

                text = item.sectionTitle
                setOnClickListener {
                    listener.onItemClicked(item)
                }
            }

            dividerPrivacyPolicy.showWithCondition(listener.isFromBottomSheet)
        }
    }
}
