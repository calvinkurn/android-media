package com.tokopedia.affiliate.feature.explore.domain.usecase

import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreFirstPageViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func2
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreFirstPageUseCase @Inject constructor(
        private val exploreSectionUseCase: ExploreSectionUseCase,
        private val exploreUseCase: ExploreUseCase
) : UseCase<ExploreFirstPageViewModel>() {

    override fun createObservable(requestParams: RequestParams?)
            : Observable<ExploreFirstPageViewModel> {
        return Observable.zip(
                exploreSectionUseCase.createObservable(requestParams).observeOn(Schedulers.io()),
                exploreUseCase.createObservable(requestParams).observeOn(Schedulers.io())) { sections, exploreResponse ->
            ExploreFirstPageViewModel()
        }
    }
}