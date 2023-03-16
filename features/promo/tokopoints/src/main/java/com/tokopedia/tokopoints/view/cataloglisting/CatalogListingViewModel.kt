package com.tokopedia.tokopoints.view.cataloglisting

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CatalogListingViewModel @Inject constructor(private val repository : CatalogListingRepository) : BaseViewModel(Dispatchers.Main), CatalogListingContract.Presenter {
    override var pointRangeId: Int = 0
    override var currentCategoryId: Int = 0
    override var currentSubCategoryId = 0

    val bannerLiveDate = MutableLiveData<Resources<CatalogBannerBase>>()
    val filterLiveData = MutableLiveData<Resources<CatalogFilterBase>>()
    val pointLiveData = MutableLiveData<Resources<TokoPointStatusEntity>>()

    override fun getHomePageData(slugCategory: String?, slugSubCategory: String?, isBannerRequire: Boolean) {
        launchCatchError(block = {
            filterLiveData.value = Loading()
            val graphqlResponse = repository.getHomePageData(slugCategory,slugSubCategory, isBannerRequire)
            val outer = graphqlResponse.getData<CatalogBannerOuter>(CatalogBannerOuter::class.java)
            if (outer == null) {
                bannerLiveDate.value = ErrorMessage("")
            } else {
                bannerLiveDate.value = Success(outer.bannerData)
            }
            //handling the catalog listing and tabs
            val catalogFilterOuter = graphqlResponse.getData<CatalogFilterOuter>(CatalogFilterOuter::class.java)
            if (catalogFilterOuter == null) {
                filterLiveData.value = ErrorMessage("")
            } else {
                filterLiveData.value = Success(catalogFilterOuter.filter)
            }
        }){
            filterLiveData.value = ErrorMessage("")
        }
    }

    override fun getPointData() {
        launchCatchError(block = {
           val  pointDetailEntity = repository.getPointData()
            if (pointDetailEntity==null) {
                pointLiveData.value = ErrorMessage("")
            } else {
                if (pointDetailEntity.tokoPoints.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                    pointLiveData.value = Success(pointDetailEntity.tokoPoints.status)
                }
            }
        }){}
    }

}
