package com.tokopedia.topads.edit.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import javax.inject.Inject

class EditAdGroupViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val groupInfoUseCase: GroupInfoUseCase
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
}
