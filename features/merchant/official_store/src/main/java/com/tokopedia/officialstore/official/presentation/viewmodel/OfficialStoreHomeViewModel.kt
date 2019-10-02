package com.tokopedia.officialstore.official.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfficialStoreHomeViewModel @Inject constructor(
        private val getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase,
        private val getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    val officialStoreBannersResult: LiveData<Result<OfficialStoreBanners>> by lazy {
        _officialStoreBannersResult
    }

    val officialStoreFeaturedShopResult: LiveData<Result<OfficialStoreFeaturedShop>> by lazy {
        _officialStoreFeaturedShopResult
    }

    private val _officialStoreBannersResult by lazy {
        MutableLiveData<Result<OfficialStoreBanners>>()
    }

    private val _officialStoreFeaturedShopResult by lazy {
        MutableLiveData<Result<OfficialStoreFeaturedShop>>()
    }

    fun getOfficialStoreBanners(categoryId: String) {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getOfficialStoreBannersUseCase.params = GetOfficialStoreBannerUseCase.
                        createParams(categoryId.toIntOrNull() ?: 0)
                getOfficialStoreBannersUseCase.executeOnBackground()
            }
            _officialStoreBannersResult.value = Success(response)
        }) {
            _officialStoreBannersResult.value = Fail(it)
        }
    }

    fun getOfficialStoreFeaturedShop(categoryId: String) {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getOfficialStoreFeaturedShopUseCase.params = GetOfficialStoreFeaturedUseCase.
                        createParams(categoryId.toIntOrNull() ?: 0)
                getOfficialStoreFeaturedShopUseCase.executeOnBackground()
            }
            _officialStoreFeaturedShopResult.value = Success(response)
        }) {
            _officialStoreFeaturedShopResult.value = Fail(it)
        }
    }


}