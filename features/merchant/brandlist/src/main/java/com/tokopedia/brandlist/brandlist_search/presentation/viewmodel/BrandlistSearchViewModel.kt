package com.tokopedia.brandlist.brandlist_search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreAllBrands
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistAllBrandUseCase
import com.tokopedia.brandlist.brandlist_page.domain.GetBrandlistPopularBrandUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BrandlistSearchViewModel @Inject constructor(
        private val getBrandlistPopularBrandUseCase: GetBrandlistPopularBrandUseCase,
        private val getBrandlistAllBrandUseCase: GetBrandlistAllBrandUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    companion object {
        private const val INITIAL_OFFSET = 0
        private const val ALL_BRANDS_QUERY = ""
        private const val ALL_BRANDS_REQUEST_SIZE = 30
        private const val ALPHABETIC_ASC_SORT = 3
        private const val INITIAL_LETTER = 'a'
        private const val POPULARITY_SORT = 2
        private const val CATEGORY_ID = 0
    }

    private val _brandlistSearchResponse = MutableLiveData<Result<OfficialStoreAllBrands>>()
    val brandlistSearchResponse: LiveData<Result<OfficialStoreAllBrands>>
            get() = _brandlistSearchResponse
    private val _brandlistSearchRecommendationResponse = MutableLiveData<Result<OfficialStoreBrandsRecommendation>>()
    val brandlistSearchRecommendationResponse: LiveData<Result<OfficialStoreBrandsRecommendation>>
        get() = _brandlistSearchRecommendationResponse
    private val _brandlistAllBrandsSearchResponse = MutableLiveData<Result<OfficialStoreAllBrands>>()
    val brandlistAllBrandsSearchResponse: LiveData<Result<OfficialStoreAllBrands>>
        get() = _brandlistAllBrandsSearchResponse
    private val _brandlistAllBrandTotal = MutableLiveData<Result<Int>>()
    val brandlistAllBrandTotal: LiveData<Result<Int>>
        get() = _brandlistAllBrandTotal

    private var firstLetterChanged = false
    private var totalBrandSize = 0
    private var totalBrandOnTheChipHeader = 0
    var currentOffset = INITIAL_OFFSET
    var currentLetter = INITIAL_LETTER


    private fun getRequestSize(totalBrandSize: Int, renderedBrands: Int): Int {
        if (renderedBrands == 0) return ALL_BRANDS_REQUEST_SIZE
        val remainingBrands = totalBrandSize - renderedBrands
        return if (remainingBrands > ALL_BRANDS_REQUEST_SIZE) {
            ALL_BRANDS_REQUEST_SIZE
        } else {
            remainingBrands
        }
    }

    fun loadInitialBrands() {
        searchAllBrands(
                offset = INITIAL_OFFSET,
                query = ALL_BRANDS_QUERY,
                brandSize = ALL_BRANDS_REQUEST_SIZE,
                sortType = ALPHABETIC_ASC_SORT,
                firstLetter = INITIAL_LETTER.toString()
        )
    }

    fun loadMoreBrands(){
        val requestSize = getRequestSize(totalBrandSize, currentOffset)
        searchAllBrands(
                offset = currentOffset,
                query = ALL_BRANDS_QUERY,
                brandSize = requestSize,
                sortType = ALPHABETIC_ASC_SORT,
                firstLetter = currentLetter.toString()
        )
    }

    fun loadMoreBrands(brandFirstLetter: String){
        val requestSize = getRequestSize(totalBrandSize, currentOffset)
        searchAllBrands(
                offset = currentOffset,
                query = ALL_BRANDS_QUERY,
                brandSize = requestSize,
                sortType = ALPHABETIC_ASC_SORT,
                firstLetter = brandFirstLetter
        )
    }

    fun getTotalBrandSizeForChipHeader(): Int = totalBrandOnTheChipHeader
    fun updateTotalBrandSizeForChipHeader(totalNumber: Int) {
        this.totalBrandOnTheChipHeader = totalNumber
    }

    fun getTotalBrandSize(): Int = totalBrandSize
    fun updateTotalBrandSize(totalBrandSize: Int) {
        this.totalBrandSize = totalBrandSize
    }

    fun updateCurrentOffset(renderedBrands: Int) {
        currentOffset += renderedBrands
    }

    fun getFirstLetterChanged(): Boolean = firstLetterChanged

    fun resetParams() {
        firstLetterChanged = false
        totalBrandSize = 0
        currentOffset = INITIAL_OFFSET
    }

    fun searchBrand(
            offset: Int,
            query: String,
            brandSize: Int,
            firstLetter: String
    ) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getBrandlistAllBrandUseCase.params = GetBrandlistAllBrandUseCase.createParams(CATEGORY_ID, offset,
                        query, brandSize, POPULARITY_SORT, firstLetter)
                val searchBrandResult = getBrandlistAllBrandUseCase.executeOnBackground()
                searchBrandResult.let {
                    _brandlistSearchResponse.postValue(Success(it))
                }
            }
        }) {
            _brandlistSearchResponse.value = Fail(it)
        }
    }

    fun searchRecommendation(
            userId: String,
            categoryIds: ArrayList<Int>? = arrayListOf(0)
    ) {
        val categoryIdString = categoryIds.toString().replace(" ","")
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getBrandlistPopularBrandUseCase.params = GetBrandlistPopularBrandUseCase.
                        createParams(
                                userId.toInt(),
                                categoryIdString.substring(1, categoryIdString.length-1),
                                GetBrandlistPopularBrandUseCase.POPULAR_WIDGET_NAME
                        )
                val searchRecommendationResult = getBrandlistPopularBrandUseCase.executeOnBackground()
                searchRecommendationResult.let {
                    _brandlistSearchRecommendationResponse.postValue(Success(it))
                }
            }
        }) {
            _brandlistSearchRecommendationResponse.value = Fail(it)
        }
    }

    fun searchAllBrands(
            offset: Int,
            query: String,
            brandSize: Int,
            sortType: Int,
            firstLetter: String
    ) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getBrandlistAllBrandUseCase.params = GetBrandlistAllBrandUseCase.createParams(CATEGORY_ID, offset,
                        query, brandSize, sortType, firstLetter)
                val searchBrandResult = getBrandlistAllBrandUseCase.executeOnBackground()
                searchBrandResult.let {
                    _brandlistAllBrandsSearchResponse.postValue(Success(it))
                }
            }
        }) {
            _brandlistAllBrandsSearchResponse.value = Fail(it)
        }
    }

    fun getTotalBrands() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getBrandlistAllBrandUseCase.params = GetBrandlistAllBrandUseCase.createParams(CATEGORY_ID, INITIAL_OFFSET,
                        ALL_BRANDS_QUERY, 0, ALPHABETIC_ASC_SORT, "")
                val searchBrandResult = getBrandlistAllBrandUseCase.executeOnBackground()
                searchBrandResult.let {
                    _brandlistAllBrandTotal.postValue(Success(it.totalBrands))
                }
            }
        }) {
            _brandlistAllBrandTotal.value = Fail(it)
        }
    }
}