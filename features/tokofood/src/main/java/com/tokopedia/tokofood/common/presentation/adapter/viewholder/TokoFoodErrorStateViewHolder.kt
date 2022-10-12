package com.tokopedia.tokofood.common.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodErrorStateBinding
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.utils.view.binding.viewBinding
import java.net.UnknownHostException

class TokoFoodErrorStateViewHolder (
    itemView: View,
    private val listener: TokoFoodErrorStateListener? = null
): AbstractViewHolder<TokoFoodErrorStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_error_state
    }

    private var binding: ItemTokofoodErrorStateBinding? by viewBinding()
    private var globalError: GlobalError? = null

    override fun bind(element: TokoFoodErrorStateUiModel) {
        setupLayout()
        setGlobalErrorState(element.throwable)
    }

    private fun setupLayout() {
        globalError = binding?.globalErrorTokofood
    }

    private fun setGlobalErrorState(throwable: Throwable) {
        val typeError = when{
            throwable is UnknownHostException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }

        globalError?.run {
            setType(typeError)
            setActionClickListener {
                listener?.onClickRetryError()
            }
        }
    }

    interface TokoFoodErrorStateListener {
        fun onClickRetryError()
    }
}