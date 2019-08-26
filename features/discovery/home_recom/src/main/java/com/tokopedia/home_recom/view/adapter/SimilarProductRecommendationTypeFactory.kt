package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel

/**
 * Created by Lukas on 26/08/19
 *
 * A interface for initialize all type factory pattern
 * It used by [SimilarProductRecommendationTypeFactory]
 */
interface SimilarProductRecommendationTypeFactory {
    fun type(dataModel: RecommendationItemDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}