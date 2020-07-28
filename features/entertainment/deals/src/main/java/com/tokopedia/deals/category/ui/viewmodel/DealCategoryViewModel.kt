package com.tokopedia.deals.category.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.category.domain.GetBrandProductCategoryUseCase
import com.tokopedia.deals.category.domain.GetChipsCategoryUseCase
import com.tokopedia.deals.category.ui.dataview.ProductListDataView
import com.tokopedia.deals.category.utils.MapperCategoryLayout
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.ChipDataView
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView
import com.tokopedia.deals.common.utils.DealsDispatcherProvider
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.model.response.CuratedData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
        private val dispatcher: DealsDispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    override val coroutineContext: CoroutineContext
        get() = dispatcher.io() + SupervisorJob()

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

    init {
        shimmeringCategory()
    }


    fun getChipsData() {
        launchCatchError(block = {
            val curatedData = getChipCategory()
            privateObservableChips.value = mapCategoryLayout.mapCategoryToChips(curatedData.eventChildCategory.categories)
        }){
            privateErrorMessage.value = it
        }
    }

    fun getCategoryBrandData(category: String, coordinates: String, location: String, page: Int = 1, isFilter:Boolean) {
        launch {
            try {
                val brandProduct = getBrandProductCategory(category, coordinates, location, page)
                if (page == 1) {
                    val categoryLayout: List<DealsBaseItemDataView>
                    if (brandProduct.eventSearch.brands.isNotEmpty() && brandProduct.eventSearch.products.isNotEmpty()) {
                        categoryLayout = mapCategoryLayout.mapCategoryLayout(brandProduct, page)
                    } else {
                        categoryLayout = mapCategoryLayout.getEmptyLayout(isFilter)
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

    fun updateChips(location: Location, categoryId: String, isFilter: Boolean) {
        shimmeringCategory()
        getCategoryBrandData(categoryId, location.coordinates, location.locType.name, isFilter = isFilter)
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
                useParams(GetBrandProductCategoryUseCase.createParams(
                        category,
                        coordinates,
                        location,
                        page
                ))
            }.executeOnBackground()
            data
        } catch (t: Throwable) {
            throw t
        }
    }

    fun shimmeringCategory() {
        val layouts = mutableListOf<DealsBaseItemDataView>()
        layouts.add(BRAND_POPULAR, DealsBrandsDataView(oneRow = true))
        layouts.add(PRODUCT_LIST, ProductListDataView())

        privateObservablDealsCategoryLayout.postValue(layouts)
    }

    companion object {
        const val BRAND_POPULAR = 0
        const val PRODUCT_LIST = 1
    }
}