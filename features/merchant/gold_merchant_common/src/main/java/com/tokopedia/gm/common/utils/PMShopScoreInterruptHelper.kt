package com.tokopedia.gm.common.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Handler
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.shopscore.DeepLinkMapperShopScore
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.gm.common.view.bottomsheet.PMFinalInterruptBottomSheet
import com.tokopedia.gm.common.view.bottomsheet.PMTransitionInterruptBottomSheet
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.gm.common.view.worker.GetPMInterruptDataWorker
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class PMShopScoreInterruptHelper @Inject constructor() {

    companion object {
        private const val WORKER_TAG = "get_pm_ss_interrupt_data_worker"

        private const val PARAM_OPEN = "open"
        private const val PARAM_HAS_CLICKED = "has_clicked"

        private const val REQUEST_CODE = 403
    }

    private var data: PowerMerchantInterruptUiModel? = null
    private var oneTimeWorkRequest: OneTimeWorkRequest? = null
    private var pmCommonPreferenceManager: PMCommonPreferenceManager? = null

    fun showInterrupt(context: Context, owner: LifecycleOwner, fm: FragmentManager) {
        initVar(context)

        fetchInterruptDataWorker(context, owner) {
            this.data = it
            showPmInterruptBottomSheet(context, it, fm)
        }
    }

    fun setShopScoreConsentStatus(context: Context, uri: Uri) {
        initVar(context)
        val isConsentApproved = uri.getBooleanQueryParameter(DeepLinkMapperShopScore.PARAM_IS_CONSENT, false)
        if (isConsentApproved) {
            pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_SHOP_SCORE_CONSENT_CHECKED, true)
            pmCommonPreferenceManager?.apply()
        }
    }

    fun onActivityResult(requestCode: Int, callback: () -> Unit) {
        if (requestCode == REQUEST_CODE && data?.periodType == PeriodType.TRANSITION_PERIOD) {
            val numberOfPageOpened = pmCommonPreferenceManager?.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
            val isFirstTimeOpenedInterruptPage = numberOfPageOpened == 1
            if (isFirstTimeOpenedInterruptPage) {
                callback()
            }
        }
    }

    fun destroy() {
        data = null
        oneTimeWorkRequest = null
        pmCommonPreferenceManager = null
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
        val gson = Gson()
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
            else -> showInterruptPage(context, data)
        }
    }

    private fun setupInterruptFinalPeriod(data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        val bottomSheet = PMFinalInterruptBottomSheet.getInstance(fm)
        if (!bottomSheet.isAdded && !fm.isStateSaved) {
            Handler().post {
                bottomSheet.setData(data).show(fm)
            }
        }
    }

    private fun setupInterruptTransitionPeriod(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        if (hasOpenedInterruptPage()) {
            showTransitionPmInterruptPopup(context, data, fm)
        } else {
            showInterruptPage(context, data)
        }
    }

    private fun showTransitionPmInterruptPopup(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        if (!hasOpenedInterruptPopup()) {
            val bottomSheet = PMTransitionInterruptBottomSheet.getInstance(fm)
            Handler().post {
                if (!bottomSheet.isAdded && !fm.isStateSaved) {
                    pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_TRANSITION_INTERRUPT_POPUP, true)
                    pmCommonPreferenceManager?.apply()
                    bottomSheet.setData(data)
                            .setOnCtaClickListener {
                                RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
                            }
                            .show(fm)
                }
            }
        }
    }

    private fun showInterruptPage(context: Context, data: PowerMerchantInterruptUiModel) {
        if (data.periodType == PeriodType.TRANSITION_PERIOD) {
            if (!hasOpenedInterruptPage()) {
                pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE, true)
                pmCommonPreferenceManager?.apply()
                openInterruptPage(context)
            }
        } else {
            val hasConsentChecked = hasConsentChecked()
            if (!hasConsentChecked) {
                pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE, true)
                pmCommonPreferenceManager?.apply()
                openInterruptPage(context)
            }
        }
    }

    private fun openInterruptPage(context: Context) {
        val intent = RouteManager.getIntent(context, getInterruptPageUrl())
        (context as? Activity)?.startActivityForResult(intent, REQUEST_CODE)
        val numberOfPageOpened = pmCommonPreferenceManager?.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
        pmCommonPreferenceManager?.putInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, numberOfPageOpened.plus(1))
        pmCommonPreferenceManager?.apply()
    }

    private fun getInterruptPageUrl(): String {
        val numberOfPageOpened = pmCommonPreferenceManager?.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
        val hasConsentChecked = hasConsentChecked()
        val param = mapOf<String, Any>(
                PARAM_OPEN to numberOfPageOpened.plus(1),
                PARAM_HAS_CLICKED to hasConsentChecked
        )
        val url = UriUtil.buildUriAppendParams(PMConstant.Urls.SHOP_SCORE_INTERRUPT_PAGE, param)
        val encodedUrl = URLEncoder.encode(url, "UTF-8")
        return String.format("%s?url=%s", ApplinkConst.WEBVIEW, encodedUrl)
    }

    private fun hasConsentChecked(): Boolean {
        return pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_SHOP_SCORE_CONSENT_CHECKED, false).orFalse()
    }

    private fun hasOpenedInterruptPopup(): Boolean {
        return pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_TRANSITION_INTERRUPT_POPUP, false).orFalse()
    }

    private fun hasOpenedInterruptPage(): Boolean {
        return pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE, false).orFalse()
    }
}