package com.tokopedia.tkpd.flashsale.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.presentation.detail.mapper.CampaignDetailUIMapper
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.CampaignDetailBottomSheetModel
import javax.inject.Inject

class CampaignDetailBottomSheetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val campaignDetailUIMapper: CampaignDetailUIMapper
) : BaseViewModel(dispatchers.main){

    private val _campaignDetail = MutableLiveData<CampaignDetailBottomSheetModel>()
    val campaignDetail: LiveData<CampaignDetailBottomSheetModel> get() = _campaignDetail

    val fragmentList = Transformations.map(campaignDetail) {
        campaignDetailUIMapper.mapToFragments(it)
    }

    val showTab = Transformations.map(fragmentList) {
        campaignDetailUIMapper.getIsShowTab(it)
    }

    fun setCampaignDetailData(campaignDetail: CampaignDetailBottomSheetModel) {
        _campaignDetail.value = campaignDetail
    }
}
