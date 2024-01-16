package com.tokopedia.deals.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.ChipDataView
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.data.entity.Category
import com.tokopedia.deals.domain.DealsSearchUseCase
import com.tokopedia.deals.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.ui.category.dataview.ProductListDataView
import com.tokopedia.deals.ui.category.mapper.MapperCategoryLayout
import com.tokopedia.deals.ui.location_picker.model.response.Location
import com.tokopedia.deals.ui.search.domain.DealsSearchGqlQueries
import kotlinx.coroutines.launch
import javax.inject.Inject

class DealCategoryViewModel @Inject constructor(
    private val mapCategoryLayout: MapperCategoryLayout,
    private val chipsCategoryCoroutine: GetChipsCategoryUseCase,
    private val dealsSearchUseCase: DealsSearchUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val privateObservableChips = MutableLiveData<List<ChipDataView>>()
    val observableChips: LiveData<List<ChipDataView>>
        get() = privateObservableChips

    private val privateObservablDealsCategoryLayout = MutableLiveData<List<DealsBaseItemDataView>>()
    val observableDealsCategoryLayout: LiveData<List<DealsBaseItemDataView>>
        get() = privateObservablDealsCategoryLayout

    private val privateErrorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable>
        get() = privateErrorMessage

    private val privateObservableProducts = MutableLiveData<List<DealsBaseItemDataView>>()
    val observableProducts: LiveData<List<DealsBaseItemDataView>>
        get() = privateObservableProducts

    private val privateObservableCategories = MutableLiveData<List<Category>>()
    val observableCategories: LiveData<List<Category>> get() = privateObservableCategories

    init {
        shimmeringCategory()
    }

    fun getChipsData() {
        launch {
            runCatching {
                val result = chipsCategoryCoroutine(Unit)
                privateObservableChips.value = mapCategoryLayout.mapCategoryToChips(result.eventChildCategory.categories)
                privateObservableCategories.value = result.eventChildCategory.categories.map { category ->
                    return@map if (category.id == CATEGORY_ID) {
                        category.copy(isCard = 1)
                    } else {
                        category
                    }
                }
            }.onFailure {
                privateErrorMessage.value = it
            }
        }
    }

    fun getCategoryBrandData(category: String, coordinates: String, location: String, page: Int = 1, isFilter: Boolean) {
        launch {
            val rawQuery = DealsSearchGqlQueries.getEventSearchQuery()
            dealsSearchUseCase.getDealsSearchResult(
                onSuccessSearch(page, isFilter, category), onErrorSearch(),
                "",
                coordinates,
                location,
                category,
                page.toString(),
                rawQuery,
                com.tokopedia.deals.ui.search.DealsSearchConstants.BRAND_PRODUCT_TREE
            )
        }
    }

    fun updateChips(location: Location, categoryId: String, isFilter: Boolean) {
        shimmeringCategory()
        getCategoryBrandData(categoryId, location.coordinates, location.locType.name, isFilter = isFilter)
    }

    private fun onSuccessSearch(page: Int, isFilter: Boolean, category: String): (SearchData) -> Unit {
        return {
            if (page == 1) {
                val categoryLayout = if (it.eventSearch.brands.isNotEmpty() ||
                    it.eventSearch.products.isNotEmpty()
                ) {
                    mapCategoryLayout.mapCategoryLayout(it, page, category)
                } else {
                    mapCategoryLayout.getEmptyLayout(isFilter)
                }
                privateObservablDealsCategoryLayout.value = categoryLayout
            } else {
                privateObservableProducts.value = mapCategoryLayout.mapProducttoLayout(it, page)
            }
        }
    }

    private fun onErrorSearch(): (Throwable) -> Unit {
        return {
            privateErrorMessage.value = it
        }
    }

    fun shimmeringCategory() {
        val layouts = mutableListOf<DealsBaseItemDataView>()
        layouts.add(BRAND_POPULAR, DealsBrandsDataView(oneRow = true))
        layouts.add(PRODUCT_LIST, ProductListDataView())

        privateObservablDealsCategoryLayout.value = layouts
    }

    override fun onCleared() {
        super.onCleared()
        dealsSearchUseCase.cancelJobs()
    }

    companion object {
        const val BRAND_POPULAR = 0
        const val PRODUCT_LIST = 1
        const val CATEGORY_ID = "25"
    }
}
