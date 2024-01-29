package com.tokopedia.recommendation_widget_common.widget.foryou.state.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouErrorBinding
import com.tokopedia.recommendation_widget_common.widget.foryou.HomeRecommendationListener
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ErrorStateModel
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorStateViewHolder constructor(
    view: View,
    private val listener: HomeRecommendationListener
) : AbstractViewHolder<ErrorStateModel>(view) {

    private val binding = WidgetForYouErrorBinding.bind(itemView)

    override fun bind(element: ErrorStateModel) {
        with(binding.globalUnifyError) {
            element.throwable?.getGlobalErrorType()?.let { setType(it) }
            setActionClickListener { listener.onRetryGetProductRecommendationData() }
        }
    }

    private fun Throwable?.getGlobalErrorType(): Int {
        return when (this) {
            is SocketTimeoutException,
            is UnknownHostException,
            is ConnectException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_error
    }
}

