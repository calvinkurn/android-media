package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorListener
import com.tokopedia.home_recom.model.datamodel.TitleDataModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import kotlinx.android.synthetic.main.item_recommendation_error.view.*
import java.util.concurrent.TimeoutException

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class RecommendationErrorViewHolder(view: View, val listener: RecommendationErrorListener) : AbstractViewHolder<RecommendationErrorDataModel>(view){

    override fun bind(element: RecommendationErrorDataModel) {
        itemView.global_error?.setType(if(element.throwable is TimeoutException) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR)
        itemView.global_error?.setActionClickListener {
            listener.refresh()
        }
    }

}