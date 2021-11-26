package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory

/**
 * Created by Lukas on 08/10/20.
 */

data class RecommendationErrorDataModel(
        val throwable: Throwable,
        val name: String = "recomPageError",
        val type: Int = -1
): HomeRecommendationDataModel {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val LAYOUT = R.layout.item_recommendation_error
    }

    override fun name(): String = name

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean = newData is RecommendationErrorDataModel

    override fun newInstance(): HomeRecommendationDataModel = this.copy()

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null
}
interface RecommendationErrorListener{
    fun onRefreshRecommendation()
    fun onCloseRecommendation()
    fun onShowSnackbarError(throwable: Throwable)
}