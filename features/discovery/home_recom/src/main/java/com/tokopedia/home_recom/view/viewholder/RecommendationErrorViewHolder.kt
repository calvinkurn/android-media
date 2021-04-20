package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorListener
import com.tokopedia.kotlin.extensions.view.hide
import kotlinx.android.synthetic.main.item_recommendation_error.view.*
import java.util.concurrent.TimeoutException

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class RecommendationErrorViewHolder(view: View, val listener: RecommendationErrorListener) : AbstractViewHolder<RecommendationErrorDataModel>(view){

    override fun bind(element: RecommendationErrorDataModel) {
        itemView.global_error?.run {
            setType(if(element.throwable is TimeoutException) GlobalError.NO_CONNECTION else GlobalError.PAGE_NOT_FOUND)
            setActionClickListener {
                if(element.throwable is TimeoutException) listener.onRefreshRecommendation()
                else listener.onCloseRecommendation()
            }
            errorSecondaryAction.hide()
        }
    }

}