package com.tokopedia.affiliate.feature.explore.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreProduct
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreSort
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreFirstPageViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 14/03/19.
 */
class ExploreFirstPageUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val exploreSectionUseCase: ExploreSectionUseCase,
        private val exploreUseCase: ExploreUseCase
) : UseCase<ExploreFirstPageViewModel>() {

    override fun createObservable(requestParams: RequestParams?)
            : Observable<ExploreFirstPageViewModel> {
        return Observable.zip(
                exploreSectionUseCase.createObservable(requestParams).observeOn(Schedulers.io()),
                exploreUseCase.createObservable(requestParams).observeOn(Schedulers.io()))
        { sections, exploreResponse ->

            val data: ExploreData = exploreResponse.getData(ExploreData::class.java)
            val visitables: MutableList<Visitable<*>> = arrayListOf()
            visitables.addAll(sections)
            visitables.addAll(mapExploreProducts(data.exploreProduct))

            ExploreFirstPageViewModel(visitables, mapSortModel(data.sort))
        }
    }


    private fun mapExploreProducts(exploreProduct: ExploreProduct): List<Visitable<*>> {
        val itemList = ArrayList<Visitable<*>>()
        exploreProduct.products.forEach {
            itemList.add(
                    ExploreViewModel(ExploreCardViewModel(
                            it.name,
                            context.getString(R.string.af_get_commission),
                            it.commission,
                            it.image,
                            "",
                            it.adId,
                            it.productId
                    ))
            )
        }
        return itemList
    }

    private fun mapSortModel(exploreSort: ExploreSort): List<SortViewModel> {
        val itemList = ArrayList<SortViewModel>()
        exploreSort.sorts.forEach {
            itemList.add(SortViewModel(
                    it.key,
                    it.isAsc,
                    it.text,
                    false
            ))
        }
        return itemList
    }
}