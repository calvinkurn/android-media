package com.tokopedia.shareexperience.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.shareexperience.ui.model.ShareExErrorUiModel
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.databinding.ShareexperienceErrorItemBinding
import com.tokopedia.shareexperience.ui.listener.ShareExErrorListener
import com.tokopedia.utils.view.binding.viewBinding
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ShareExErrorViewHolder(
    itemView: View,
    private val listener: ShareExErrorListener
): AbstractViewHolder<ShareExErrorUiModel>(itemView) {

    private val binding: ShareexperienceErrorItemBinding? by viewBinding()

    init {
        binding?.shareexGlobalError?.setActionClickListener {
            listener.onErrorActionClicked()
        }
    }

    override fun bind(element: ShareExErrorUiModel) {
        bindUnifyError(element)
    }

    private fun bindUnifyError(element: ShareExErrorUiModel) {
        val errorType = getErrorType(element.throwable)
        binding?.shareexGlobalError?.setType(errorType)
    }

    private fun getErrorType(throwable: Throwable): Int {
        return when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                GlobalError.NO_CONNECTION
            }
            else -> GlobalError.SERVER_ERROR
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shareexperience_error_item
    }
}
