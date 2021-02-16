package com.tokopedia.explore.view.uimodel

/**
 * @author by milhamj on 20/07/18.
 */

data class ExploreViewModel(
        val exploreImageViewModelList: MutableList<ExploreImageViewModel> = arrayListOf(),
        val tagViewModelList: MutableList<ExploreCategoryViewModel> = arrayListOf())
