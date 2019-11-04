package com.tokopedia.explore.view.viewmodel

/**
 * @author by milhamj on 20/07/18.
 */

data class ExploreViewModel(
        var exploreImageViewModelList: ArrayList<ExploreImageViewModel> = arrayListOf(),
        var tagViewModelList: ArrayList<ExploreCategoryViewModel> = arrayListOf())
