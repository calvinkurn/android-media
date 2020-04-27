package com.tokopedia.search.result.presentation.presenter.profile

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.model.ProfileRecommendationTitleViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.presenter.profile.testinstance.searchProfileModelEmptyResultWithRecommendation
import com.tokopedia.search.result.shop.presentation.viewmodel.shouldBeInstanceOf
import com.tokopedia.search.shouldBe

fun List<Visitable<*>>?.shouldOnlyHaveEmptyResult() {
    this ?: throw AssertionError("List is null")

    this.size shouldBe 1
    this[0].shouldBeInstanceOf<EmptySearchProfileViewModel>()
}

fun List<Visitable<*>>?.shouldHaveEmptyResultAndRecommendationProfiles() {
    this ?: throw AssertionError("List is null")

    // Empty Search + Profile Recommendation Title + Profile Recommendation Size
    this.size shouldBe 2 + searchProfileModelEmptyResultWithRecommendation.aceSearchProfile.topProfile.size

    this[0].shouldBeInstanceOf<EmptySearchProfileViewModel>()
    this[1].shouldBeInstanceOf<ProfileRecommendationTitleViewModel>()
    for (i in 2 until this.size) {
        val recommendationProfileViewModelPosition = i - 1
        this[i].verifyRecommendationProfileViewModel(i, recommendationProfileViewModelPosition)
    }
}

private fun Visitable<*>?.verifyRecommendationProfileViewModel(visitablePosition: Int, recommendationProfileViewModelPosition: Int) {
    if (this !is ProfileViewModel) {
        throw AssertionError("Visitable index $visitablePosition is not instance of ${ProfileViewModel::class.java.simpleName}")
    }

    this.isRecommendation shouldBe true
    this.position shouldBe recommendationProfileViewModelPosition
}