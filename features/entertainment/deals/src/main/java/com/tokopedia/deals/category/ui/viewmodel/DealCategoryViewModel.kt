package com.tokopedia.deals.category.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.category.domain.GetBrandProductCategoryUseCase
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.category.utils.MapperCategoryLayout
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.model.response.CuratedData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DealCategoryViewModel @Inject constructor(
        val mapCategoryLayout: MapperCategoryLayout,
        private val chipsCategoryUseCase: GetChipsCategoryUseCase,
        private val brandProductCategoryUseCase: GetBrandProductCategoryUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private val privateObservableChips = MutableLiveData<DealsChipsDataView>()
    val observableChips: LiveData<DealsChipsDataView>
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

    init {
        getInitialData()
    }

    fun getCategoryBrandData(category: String, coordinates: String, location: String, page: Int = 1, isFilter:Boolean) {
        launch {
            try {
                val curatedData = getChipCategory()
                val brandProduct = getBrandProductCategory(category, coordinates, location, page)
                if (page == 1) {
                    var categoryLayout = listOf<DealsBaseItemDataView>()
                    if (!brandProduct.eventSearch.brands.isEmpty() && !brandProduct.eventSearch.products.isEmpty()) {
                        categoryLayout = mapCategoryLayout.mapCategoryLayout(curatedData, brandProduct, page)
                    } else {
                        categoryLayout = mapCategoryLayout.mapChipLayout(curatedData, isFilter)
                    }
                    privateObservablDealsCategoryLayout.value = categoryLayout
                } else {
                    privateObservableProducts.value = listOf(mapCategoryLayout.mapProducttoLayout(brandProduct, page))
                }
            } catch (t: Throwable) {
                privateErrorMessage.value = t
            }
        }
    }

    fun updateChips(chips: DealsChipsDataView, location: Location, categoryId: String, isFilter: Boolean) {
        loadFilterShimmering()
        getCategoryBrandData(categoryId, location.coordinates, location.locType.name, isFilter = isFilter)
        privateObservableChips.value = chips
    }

    fun loadFilterShimmering() {
        privateObservablDealsCategoryLayout.value?.let {
            val layouts = it.toMutableList()
            layouts.subList(0, 1)
            layouts.add(BRAND_POPULAR, DealsBrandsDataView())
            layouts.add(PRODUCT_LIST, ProductListDataView())
            privateObservablDealsCategoryLayout.value = layouts
        }
    }

    private suspend fun getChipCategory(): CuratedData {
        return try {
            return chipsCategoryUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }

    private suspend fun getBrandProductCategory(
            category: String,
            coordinates: String,
            location: String,
            page: Int
    ): SearchData {
        return try {
            val data = brandProductCategoryUseCase.apply {
                params = GetBrandProductCategoryUseCase.createParams(
                        category,
                        coordinates,
                        location,
                        page
                )
            }.executeOnBackground()
            data
        } catch (t: Throwable) {
            throw t
        }
    }

    private fun getInitialData() {
        val layouts = mutableListOf<DealsBaseItemDataView>()
        layouts.add(CHIPS_CATEGORIES, DealsChipsDataView())
        layouts.add(BRAND_POPULAR, DealsBrandsDataView())
        layouts.add(PRODUCT_LIST, ProductListDataView())

        privateObservablDealsCategoryLayout.postValue(layouts)
    }

    companion object {
        const val CHIPS_CATEGORIES = 0
        const val BRAND_POPULAR = 1
        const val PRODUCT_LIST = 2
    }
}