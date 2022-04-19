package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.datamodel.HomeNavGlobalErrorDataModel
import com.tokopedia.homenav.databinding.HolderHomeNavGlobalErrorBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeNavGlobalErrorViewHolder(
        itemView: View,
        private val listener: HomeNavListener
): AbstractViewHolder<HomeNavGlobalErrorDataModel>(itemView) {
    private var binding: HolderHomeNavGlobalErrorBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_global_error
    }

    override fun bind(element: HomeNavGlobalErrorDataModel) {
        binding?.globalError?.run {
            errorSecondaryAction.hide()
            when (element.throwable) {
                is ConnectException, is UnknownHostException, is SocketTimeoutException -> {
                    setType(GlobalError.NO_CONNECTION)
                    setActionClickListener {
                        listener.onRefresh()
                    }
                }
                else -> {
                    setType(GlobalError.PAGE_FULL)
                    setActionClickListener {
                        listener.onRefresh()
                    }
                }
            }
        }

    }
}