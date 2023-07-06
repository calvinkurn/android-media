package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcItemModel

/**
 * Created by Frenzel
 */
interface ComparisonBpcTypeFactory {
    fun type(dataModel: ComparisonBpcSeeMoreDataModel) : Int
    fun type(dataModel: ComparisonBpcItemModel) : Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<Visitable<ComparisonBpcTypeFactory>>
}
