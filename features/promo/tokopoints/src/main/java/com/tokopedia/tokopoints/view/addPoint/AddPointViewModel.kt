package com.tokopedia.tokopoints.view.addPoint

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopoints.view.model.addpointsection.SheetHowToGetV2
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AddPointViewModel @Inject constructor(private val rewardUsecase: RewardUseCase) : BaseViewModel(Dispatchers.Main), TokopointAddPointContract.Presenter {


    val sheetLiveData = MutableLiveData<Resources<SheetHowToGetV2>>()

    override fun getRewardPoint() {
        launchCatchError(block = {
            sheetLiveData.value = Loading()
            val rewardPointResponse = rewardUsecase.execute()
            rewardPointResponse.sheetHowToGetV2?.let {
                sheetLiveData.value = Success(it)
            }
        }) {}
    }
}
