package com.tokopedia.tokofood.feature.search.common.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemSearchErrorStateBinding
import com.tokopedia.tokofood.feature.search.common.presentation.uimodel.TokofoodSearchErrorStateUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TokofoodSearchErrorStateViewHolder(itemView: View,
                                         private val listener: Listener) :
    AbstractViewHolder<TokofoodSearchErrorStateUiModel>(itemView) {

    private val binding: ItemSearchErrorStateBinding? by viewBinding()

    private var onActionClickListener: () -> Unit = {}

    init {
        binding?.errorItemSearchErrorState?.setActionClickListener {
            onActionClickListener.invoke()
        }
    }

    override fun bind(element: TokofoodSearchErrorStateUiModel) {
        renderGlobalError(element.globalErrorType)
    }

    private fun renderGlobalError(globalErrorType: Int) {
        binding?.errorItemSearchErrorState?.run {
            setType(globalErrorType)
            setButtonFull(true)
            if (globalErrorType == GlobalError.NO_CONNECTION || globalErrorType == GlobalError.SERVER_ERROR) {
                onActionClickListener = listener::onRetry
            } else {
                errorAction.text =
                    context?.getString(com.tokopedia.tokofood.R.string.search_common_to_homepage)
                        .orEmpty()
                onActionClickListener = listener::onGoToHome
            }
        }
    }

    interface Listener {
        fun onRetry()
        fun onGoToHome()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_search_error_state
    }

}