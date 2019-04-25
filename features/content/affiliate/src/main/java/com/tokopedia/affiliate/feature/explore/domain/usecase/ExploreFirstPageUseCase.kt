package com.tokopedia.affiliate.feature.explore.domain.usecase

import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreFirstPageViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreFirstPageUseCase @Inject constructor(
        private val exploreSectionUseCase: ExploreSectionUseCase,
        private val exploreUseCase: ExploreUseCase,
        private val exploreSortUseCase: ExploreSortUseCase
) : UseCase<ExploreFirstPageViewModel>() {

    var exploreParams: ExploreParams = ExploreParams()

    override fun createObservable(requestParams: RequestParams?)
            : Observable<ExploreFirstPageViewModel> {
        exploreUseCase.exploreParams = exploreParams
        return Observable.zip(
                exploreSectionUseCase.createObservable(requestParams).subscribeOn(Schedulers.io()),
                exploreUseCase.createObservable(requestParams).subscribeOn(Schedulers.io()),
                exploreSortUseCase.createObservable(requestParams).subscribeOn(Schedulers.io()))
        { sections, exploreViewModel, sorts ->
            ExploreFirstPageViewModel(
                    sections,
                    exploreViewModel.exploreProducts,
                    sorts,
                    exploreViewModel.nextCursor
            )
        }
    }
}