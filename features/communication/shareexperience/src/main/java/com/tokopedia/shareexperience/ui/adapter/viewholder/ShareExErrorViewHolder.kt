package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.shareexperience.ui.model.ShareExErrorUiModel
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceErrorItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExErrorListener
import com.tokopedia.utils.view.binding.viewBinding

class ShareExErrorViewHolder(
    itemView: View,
    private val listener: ShareExErrorListener
): AbstractViewHolder<ShareExErrorUiModel>(itemView) {

    private val binding: ShareexperienceErrorItemBinding? by viewBinding()

    override fun bind(element: ShareExErrorUiModel) {
        binding?.shareexGlobalError?.showUnifyError(
            t = element.throwable,
            buttonOnClickListener = {
                listener.onErrorActionClicked()
            }
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_error_item
    }
}
