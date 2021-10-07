package com.tokopedia.search.result.domain.usecase.savelastfilter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.ACTION
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.ACTION_CREATE
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.ACTION_DELETE
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.ACTION_UPDATE
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.CATEGORY_ID_L2
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.LAST_FILTER
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.PARAM
import com.tokopedia.filter.common.data.SavedOption

class SaveLastFilterInput(
    @SerializedName(LAST_FILTER)
    @Expose
    val lastFilter: List<SavedOption> = listOf(),

    val action: Action = Create,

    @SerializedName(PARAM)
    @Expose
    val param: String = "",

    @SerializedName(CATEGORY_ID_L2)
    @Expose
    val categoryIdL2: String = "",
) {

    @SerializedName(ACTION)
    @Expose
    val actionString = action.toString()

    sealed class Action
    object Create: Action() {
        override fun toString() = ACTION_CREATE
    }
    object Update: Action() {
        override fun toString() = ACTION_UPDATE
    }
    object Delete: Action() {
        override fun toString() = ACTION_DELETE
    }
}