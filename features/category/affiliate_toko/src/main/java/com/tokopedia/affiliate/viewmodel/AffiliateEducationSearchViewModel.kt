package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject

class AffiliateEducationSearchViewModel @Inject constructor() : BaseViewModel() {
    var searchKeyword = MutableLiveData<String>()
    fun getSearchKeyword(): LiveData<String> = searchKeyword
}
