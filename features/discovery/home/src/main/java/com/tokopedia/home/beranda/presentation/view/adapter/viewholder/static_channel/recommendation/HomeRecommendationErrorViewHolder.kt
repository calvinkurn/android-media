package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationError
import com.tokopedia.home.databinding.ItemHomeRecommendationErrorLayoutBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeRecommendationErrorViewHolder(
    view: View,
    private val listener: GlobalRecomListener
) : AbstractViewHolder<HomeRecommendationError>(view) {

    private val binding = ItemHomeRecommendationErrorLayoutBinding.bind(itemView)

    override fun bind(element: HomeRecommendationError) {
        with(binding.globalUnifyError) {
            element.throwable?.getGlobalErrorType()?.let { setType(it) }
            setActionClickListener { listener.onRetryGetProductRecommendationData() }
        }
    }

    private fun Throwable?.getGlobalErrorType(): Int {
        return when (this) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_recommendation_error_layout
    }
}
