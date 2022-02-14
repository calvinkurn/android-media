package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.home_recom.databinding.ItemRecommendationErrorBinding
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding
import java.util.concurrent.TimeoutException

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class RecommendationErrorViewHolder(view: View, val listener: RecommendationErrorListener) : AbstractViewHolder<RecommendationErrorDataModel>(view){

    private var binding: ItemRecommendationErrorBinding? by viewBinding()
    override fun bind(element: RecommendationErrorDataModel) {
        binding?.globalError?.run {
            if (element.type == -1) {
                setType(if (element.throwable is TimeoutException) GlobalError.NO_CONNECTION else GlobalError.PAGE_NOT_FOUND)
            } else {
                setType(element.type)
            }
            setActionClickListener {
                if (element.throwable is TimeoutException) listener.onRefreshRecommendation()
                else listener.onCloseRecommendation()
            }
            errorSecondaryAction.hide()
        }
    }

}