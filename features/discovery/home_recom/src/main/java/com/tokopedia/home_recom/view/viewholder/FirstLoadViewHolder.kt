package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.FirstLoadDataModel

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view shimmering loading
 */
class FirstLoadViewHolder(view: View) : AbstractViewHolder<FirstLoadDataModel>(view) {

    override fun bind(element: FirstLoadDataModel) {
    }

    companion object {
        val LAYOUT = R.layout.item_recommendation_first_loading
    }
}