package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.ViewGroup
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class SimulationTableViewFactory(val context: Context?) {

    fun <T : Any> create(modelClass: Class<T>, layoutParams: ViewGroup.LayoutParams): T {
        context?.let {
            when {
                modelClass.isAssignableFrom(BlankViewTableRowHeader::class.java) -> {
                    return BlankViewTableRowHeader(context, layoutParams) as T
                }
                modelClass.isAssignableFrom(NoRecommendationViewTableRowHeader::class.java) -> {
                    return NoRecommendationViewTableRowHeader(context, layoutParams) as T
                }
                modelClass.isAssignableFrom(RecommendationViewTableRowHeader::class.java) -> {
                    return RecommendationViewTableRowHeader(context, layoutParams) as T
                }
                modelClass.isAssignableFrom(InstallmentViewTableColumnHeader::class.java) -> {
                    return InstallmentViewTableColumnHeader(context, layoutParams) as T
                }
                modelClass.isAssignableFrom(InstallmentViewTableContent::class.java) -> {
                    return InstallmentViewTableContent(context, layoutParams) as T
                }
                else -> {
                    throw IllegalArgumentException("unknown model class " + modelClass)
                }
            }
        } ?: throw IllegalArgumentException("unknown model class " + modelClass)
    }
}