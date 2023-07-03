package com.tokopedia.scp_rewards_touchpoints.toaster

import android.content.Context
import com.tokopedia.scp_rewards_touchpoints.toaster.model.ScpRewardsToasterModel

class ScpToasterHelper {
    companion object {
        suspend fun fetchToasterData(context: Context, orderID: Int, sourceName: String, pageName: String) {
            // if toaster applink is present call showToaster()
        }

        fun showToaster(scpRewardsToasterModel: ScpRewardsToasterModel) {
            // set cta to bottom sheet app link
            // show toaster view
        }
    }
    // Integrator will call ScpToasterHelper.fetchToasterData(callBack:Callback, data:DataNeededForUseCase)
}
