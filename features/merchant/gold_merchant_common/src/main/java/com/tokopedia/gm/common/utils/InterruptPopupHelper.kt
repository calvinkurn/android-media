package com.tokopedia.gm.common.utils

import android.content.Context
import android.os.Handler
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.gm.common.view.bottomsheet.PMFinalInterruptBottomSheet
import com.tokopedia.gm.common.view.bottomsheet.PMTransitionInterruptBottomSheet
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.gm.common.view.worker.GetPMInterruptDataWorker
import com.tokopedia.kotlin.extensions.orFalse
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 08/04/21
 */

object InterruptPopupHelper {

    private const val WORKER_TAG = "get_pm_interrupt_dat_worker"

    private val gson = Gson()
    private var oneTimeWorkRequest: OneTimeWorkRequest? = null
    private var pmCommonPreferenceManager: PMCommonPreferenceManager? = null

    fun showPopup(context: Context, owner: LifecycleOwner, fm: FragmentManager) {
        initVar(context)

        fetchInterruptDataWorker(context, owner) {
            showPmInterruptBottomSheet(context, it, fm)
        }
    }

    private fun initVar(context: Context) {
        if (pmCommonPreferenceManager == null) {
            pmCommonPreferenceManager = PMCommonPreferenceManager(context.applicationContext)
        }
        initWorker()
    }

    private fun fetchInterruptDataWorker(context: Context, owner: LifecycleOwner, function: (PowerMerchantInterruptUiModel) -> Unit) {
        oneTimeWorkRequest?.let { otw ->
            val workManager = WorkManager.getInstance(context.applicationContext)
            workManager.beginUniqueWork(WORKER_TAG, ExistingWorkPolicy.REPLACE, otw).enqueue()

            workManager.getWorkInfoByIdLiveData(otw.id)
                    .observe(owner) {
                        if (it.state == WorkInfo.State.SUCCEEDED) {
                            function(getDataFromWorkerOutput(it.outputData))
                        }
                    }
        }
    }

    private fun getDataFromWorkerOutput(outputData: Data): PowerMerchantInterruptUiModel {
        val json = gson.toJson(outputData.keyValueMap)
        return gson.fromJson(json, object : TypeToken<PowerMerchantInterruptUiModel>() {}.type)
    }

    private fun initWorker() {
        if (oneTimeWorkRequest == null) {
            val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            val duration = 0L
            oneTimeWorkRequest = OneTimeWorkRequest.Builder(GetPMInterruptDataWorker::class.java)
                    .setConstraints(constraints)
                    .setInitialDelay(duration, TimeUnit.SECONDS)
                    .build()
        }
    }

    private fun showPmInterruptBottomSheet(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        when (data.periodType) {
            PeriodType.FINAL_PERIOD -> setupInterruptFinalPeriod(data, fm)
            PeriodType.TRANSITION_PERIOD -> setupInterruptTransitionPeriod(context, data, fm)
            else -> showInterruptPage(context)
        }
    }

    private fun setupInterruptFinalPeriod(data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        val bottomSheet = PMFinalInterruptBottomSheet.getInstance(fm)
        if (!bottomSheet.isAdded) {
            Handler().post {
                bottomSheet.setData(data).show(fm)
            }
        }
    }

    private fun setupInterruptTransitionPeriod(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        val hasOpenedInterruptPage = pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE, false)
        if (hasOpenedInterruptPage.orFalse()) {
            showTransitionPmInterruptPopup(data, fm)
        } else {
            showInterruptPage(context)
        }
    }

    private fun showTransitionPmInterruptPopup(data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        val hasShowInterruptPopup = pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_HAS_SHOW_TRANSITION_INTERRUPT_POPUP, false)
        if (!hasShowInterruptPopup.orFalse()) {
            val bottomSheet = PMTransitionInterruptBottomSheet.getInstance(fm)
            Handler().post {
                if (!bottomSheet.isAdded) {
                    //saveFlagHasShowPmInterruptPopup()
                    bottomSheet.setData(data)
                            .setOnCtaClickListener {
                                //RouteManager.route(this@SellerHomeActivity, ApplinkConst.SHOP_SCORE_DETAIL)
                            }
                            .show(fm)
                }
            }
        }
    }

    private fun showInterruptPage(context: Context) {
        RouteManager.route(context, PMConstant.Urls.SHOP_SCORE_INTERRUPT_PAGE)
    }
}