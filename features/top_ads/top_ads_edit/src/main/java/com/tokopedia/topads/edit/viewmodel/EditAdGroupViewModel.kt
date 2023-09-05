package com.tokopedia.topads.edit.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetTotalAdsAndKeywordsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.topads.edit.utils.Constants
import javax.inject.Inject

class EditAdGroupViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val groupInfoUseCase: GroupInfoUseCase,
    private val getTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase,
    private val validateNameAdGroupUseCase: TopAdsGroupValidateNameUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase
) :
    BaseViewModel(dispatchers.io) {


    fun getGroupInfo(
        groupId: String,
        onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit
    ) {
        groupInfoUseCase.setParams(groupId)
        groupInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topAdsGetPromoGroup?.data!!)
            },
            { throwable ->
                throwable.printStackTrace()
            }
        )
    }

    fun getTotalAdsAndKeywordsCount(
        groupId: String,
        onSuccess: (List<CountDataItem>) -> Unit
    ) {
        launchCatchError(block = {
            val response = getTotalAdsAndKeywordsUseCase(listOf(groupId))
            if (response.topAdsGetTotalAdsAndKeywords.errors.isEmpty())
                onSuccess(response.topAdsGetTotalAdsAndKeywords.data)
        }) {}
    }

    fun validateGroup(
        groupName: String,
        onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateNameV2) -> Unit),
    ) {
        validateNameAdGroupUseCase.setParams(groupName, Constants.SOURCE_ANDROID_EDIT_GROUP)
        validateNameAdGroupUseCase.execute(
            {
                onSuccess(it.topAdsGroupValidateName)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }
}
