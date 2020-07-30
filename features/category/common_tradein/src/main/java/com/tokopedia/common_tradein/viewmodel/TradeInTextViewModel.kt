package com.tokopedia.common_tradein.viewmodel

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.R
import com.tokopedia.common_tradein.customviews.TradeInTextView
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.model.ValidateTradeInResponse
import com.tokopedia.common_tradein.usecase.CheckTradeInUseCase
import com.tokopedia.design.dialog.AccessRequestDialogFragment.Companion.TAG
import com.tokopedia.design.dialog.AccessRequestDialogFragment.Companion.newInstance
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TradeInTextViewModel @Inject constructor(private val useCase: CheckTradeInUseCase) :
        BaseViewModel(), ITradeInParamReceiver, CoroutineScope{
    val responseData: MutableLiveData<ValidateTradeInResponse> = MutableLiveData()
    private var activityWeakReference: WeakReference<FragmentActivity?>? = null

    fun setActivity(activity: FragmentActivity?) {
        activityWeakReference = WeakReference(activity)
    }

    fun showAccessRequestDialog() {
        if (activityWeakReference?.get() != null) {
            val fragmentManager = activityWeakReference?.get()?.supportFragmentManager
            val activity = activityWeakReference?.get()
            val accessDialog = newInstance()
            accessDialog.setBodyText(activity!!.getString(R.string.tradein_text_permission_description))
            accessDialog.setTitle(activity.getString(R.string.tradein_text_request_access))
            accessDialog.setNegativeButton("")
            accessDialog.show(fragmentManager!!, TAG)
        }
    }

    override fun checkTradeIn(tradeInParams: TradeInParams, hide: Boolean, application: Application) {
        if (tradeInParams.isEligible == 0) {
            launchCatchError(block = {
                val tradeInResponse = useCase.checkTradeIn(getQuery(application), tradeInParams).response
                if (tradeInResponse != null) {
                    responseData.value = tradeInResponse
                    val intent = Intent(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE)
                    intent.putExtra(TradeInTextView.EXTRA_ISELLIGIBLE, tradeInResponse.isEligible)
                    LocalBroadcastManager.getInstance(activityWeakReference?.get()!!).sendBroadcast(intent)
                    tradeInParams.isEligible = if (tradeInResponse.isEligible) 1 else 0
                    tradeInParams.usedPrice = tradeInResponse.usedPrice
                    tradeInParams.remainingPrice = tradeInResponse.remainingPrice
                    tradeInParams.isUseKyc = if (tradeInResponse.isUseKyc) 1 else 0
                    tradeInParams.widgetString = tradeInResponse.widgetString
                } else {
                    broadcastDefaultResponse()
                }
            }, onError = {
                it.printStackTrace()
                broadcastDefaultResponse()
            })
        } else {
            if (!hide) {
                val response = ValidateTradeInResponse()
                response.isEligible = true
                response.remainingPrice = tradeInParams.remainingPrice
                response.usedPrice = tradeInParams.usedPrice
                response.isUseKyc = tradeInParams.isUseKyc != 0
                response.widgetString = tradeInParams.widgetString
                responseData.setValue(response)
            } else {
                val response = ValidateTradeInResponse()
                response.isEligible = false
                responseData.setValue(response)
            }
        }
    }

    fun getQuery(application: Application): String {
        return GraphqlHelper.loadRawString(application.resources,
                R.raw.gql_validate_tradein)
    }

    private fun broadcastDefaultResponse() {
        if (activityWeakReference?.get() != null) {
            val intent = Intent(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE)
            intent.putExtra(TradeInTextView.EXTRA_ISELLIGIBLE, false)
            LocalBroadcastManager.getInstance(activityWeakReference?.get()!!).sendBroadcast(intent)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}