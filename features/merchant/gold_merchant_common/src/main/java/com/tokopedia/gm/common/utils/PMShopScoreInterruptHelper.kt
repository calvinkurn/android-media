package com.tokopedia.gm.common.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.powermerchant.PowerMerchantDeepLinkMapper
import com.tokopedia.applink.shopscore.DeepLinkMapperShopScore
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.gm.common.view.bottomsheet.SimpleInterruptBottomSheet
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.gm.common.view.worker.GetPMInterruptDataWorker
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.Toaster
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class PMShopScoreInterruptHelper @Inject constructor(
        private val pmCommonPreferenceManager: PMCommonPreferenceManager,
        private val remoteConfig: PMCommonRemoteConfig
) {

    companion object {
        private const val WORKER_TAG = "get_pm_ss_interrupt_data_worker"

        private const val PARAM_OPEN = "open"
        private const val PARAM_HAS_CLICKED = "has_clicked"

        private const val REQUEST_CODE = 403
    }

    private var data: PowerMerchantInterruptUiModel? = null
    private var oneTimeWorkRequest: OneTimeWorkRequest? = null

    fun getPeriodType() = data?.periodType

    fun showInterrupt(context: Context, owner: LifecycleOwner, fm: FragmentManager) {
        if (!remoteConfig.getPmShopScoreInterruptEnabled()) {
            return
        }

        initWorker()
        fetchInterruptDataWorker(context, owner) {
            this.data = it
            showPmShopScoreInterrupt(context, it, fm)
        }
    }

    fun setShopScoreConsentStatus(uri: Uri, callback: (isConsent: Boolean) -> Unit) {
        val isConsentApproved = uri.getBooleanQueryParameter(DeepLinkMapperShopScore.PARAM_IS_CONSENT, false)
        callback(isConsentApproved)
        if (isConsentApproved) {
            pmCommonPreferenceManager.putBoolean(PMCommonPreferenceManager.KEY_SHOP_SCORE_CONSENT_CHECKED, true)
            pmCommonPreferenceManager.apply()
        }
    }

    fun onActivityResult(requestCode: Int, callback: () -> Unit) {
        if (requestCode == REQUEST_CODE && data?.periodType == PeriodType.TRANSITION_PERIOD) {
            val hasShownCoachMark = pmCommonPreferenceManager.getBoolean(PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK, false).orFalse()
            if (!hasShownCoachMark) {
                callback()
            }
        }
    }

    fun saveRecommendationCoachMarkFlag() {
        pmCommonPreferenceManager.putBoolean(PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK, true)
        pmCommonPreferenceManager.apply()
    }

    fun getRecommendationCoachMarkStatus(): Boolean {
        val hasShownCoachMark = pmCommonPreferenceManager.getBoolean(PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK, false).orFalse()
        val isTransitionPeriod = data?.periodType == PeriodType.TRANSITION_PERIOD
        return !hasShownCoachMark && hasOpenedInterruptPage() && isTransitionPeriod
    }

    private fun openInterruptPage(context: Context) {
        val intent = RouteManager.getIntent(context, getInterruptPageUrl(context))
        (context as? Activity)?.startActivityForResult(intent, REQUEST_CODE)
        val numberOfPageOpened = pmCommonPreferenceManager.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
        pmCommonPreferenceManager.putInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, numberOfPageOpened.plus(1))
        pmCommonPreferenceManager.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE, true)
        pmCommonPreferenceManager.apply()
    }

    fun showsShopScoreConsentToaster(view: View?) {
        view?.run {
            Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
            val message = context.getString(R.string.gmc_shop_score_consent_approved_message)
            val ctaText = context.getString(R.string.gmc_oke)
            Toaster.build(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL, ctaText)
                    .show()
        }
    }

    fun showToasterPmProInterruptPage(uri: Uri, rootView: View?) {
        val state = uri.getQueryParameter(PowerMerchantDeepLinkMapper.QUERY_PARAM_STATE).orEmpty()
        rootView?.run {
            val message = when (state) {
                PowerMerchantDeepLinkMapper.VALUE_STATE_APPROVED -> context.getString(R.string.gmc_pm_pro_interrupt_action_approved)
                PowerMerchantDeepLinkMapper.VALUE_STATE_STAY -> context.getString(R.string.gmc_pm_pro_interrupt_action_stay)
                PowerMerchantDeepLinkMapper.VALUE_STATE_AGREED -> context.getString(R.string.gmc_pm_pro_interrupt_action_agreed)
                else -> ""
            }

            if (message.isNotBlank()) {
                Toaster.toasterCustomBottomHeight = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl8)
                Toaster.build(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        .show()
            }
        }
    }

    fun destroy() {
        data = null
        oneTimeWorkRequest = null
    }

    private fun fetchInterruptDataWorker(context: Context, owner: LifecycleOwner, function: (PowerMerchantInterruptUiModel) -> Unit) {
        oneTimeWorkRequest?.let { otw ->
            val workManager = WorkManager.getInstance(context.applicationContext)
            workManager.beginUniqueWork(WORKER_TAG, ExistingWorkPolicy.REPLACE, otw).enqueue()

            workManager.getWorkInfoByIdLiveData(otw.id)
                    .observe(owner) {
                        it?.let { workInfo ->
                            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                                function(getDataFromWorkerOutput(workInfo.outputData))
                            }
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

    private fun showPmShopScoreInterrupt(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        if (data.isOfficialStore) return

        when (data.periodType) {
            PeriodType.TRANSITION_PERIOD -> setupInterruptTransitionPeriod(context, data)
            PeriodType.COMMUNICATION_PERIOD -> setupInterruptCommunicationPeriod(context, data, fm)
        }
    }

    private fun setupInterruptCommunicationPeriod(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        if (data.isNewSeller && data.pmStatus == PMStatusConst.IDLE) {
            showInterruptNewSellerPmIdle(context, fm)
        } else {
            showInterruptPage(context, data)
        }
    }

    private fun showInterruptNewSellerPmIdle(context: Context, fm: FragmentManager) {
        val bottomSheet = SimpleInterruptBottomSheet.createInstance(false)
        val isBottomSheetEverSeen = pmCommonPreferenceManager.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_NEW_SELLER_PM_IDLE_POPUP, false).orFalse()
        if (fm.isStateSaved || bottomSheet.isAdded || isBottomSheetEverSeen) return

        val endOfCommPeriod = PMConstant.TRANSITION_PERIOD_START_DATE

        val currentFormat = "dd MMMM yyyy"
        val simpleFormat = "dd MMMM"
        val endOfCommPeriodDayMonth = DateFormatUtils.formatDate(currentFormat, simpleFormat, endOfCommPeriod)

        val title = context.getString(R.string.gmc_new_seller_potential_inactive_interrupt_title, endOfCommPeriod)
        val description = context.getString(R.string.gmc_new_seller_potential_inactive_interrupt_description, endOfCommPeriodDayMonth)
        val ctaText = context.getString(R.string.gmc_check_your_shop_performance)
        val imgIllustration = PMConstant.Images.PM_INACTIVE

        bottomSheet.setContent(title, description, imgIllustration)
                .setOnCtaClickListener(ctaText) {
                    RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
                }
                .setOnDismissListener {
                    pmCommonPreferenceManager.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_NEW_SELLER_PM_IDLE_POPUP, true)
                    pmCommonPreferenceManager.apply()
                }
        bottomSheet.show(fm)
    }

    private fun setupInterruptTransitionPeriod(context: Context, data: PowerMerchantInterruptUiModel) {
        showInterruptPage(context, data)
    }

    private fun showInterruptPage(context: Context, data: PowerMerchantInterruptUiModel) {
        if (data.periodType == PeriodType.TRANSITION_PERIOD) {
            if (!hasOpenedInterruptPage()) {
                openInterruptPage(context)
            }
        } else if (data.periodType == PeriodType.COMMUNICATION_PERIOD) {
            val hasConsentChecked = hasConsentChecked()
            if (!hasConsentChecked) {
                openInterruptPage(context)
            }
        }
    }

    private fun getInterruptPageUrl(context: Context): String {
        val numberOfPageOpened = pmCommonPreferenceManager.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
        val hasConsentChecked = hasConsentChecked()
        val param = mapOf<String, Any>(
                PARAM_OPEN to numberOfPageOpened.plus(1),
                PARAM_HAS_CLICKED to hasConsentChecked
        )
        val url = UriUtil.buildUriAppendParams(PMConstant.Urls.SHOP_SCORE_INTERRUPT_PAGE, param)
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        val backPressedMessage = URLEncoder.encode(context.getString(R.string.pm_on_back_pressed_disabled_message), StandardCharsets.UTF_8.toString())
        val backPressedEnabled = (getPeriodType() != PeriodType.COMMUNICATION_PERIOD).toString()
        val showTitleBar = (getPeriodType() != PeriodType.COMMUNICATION_PERIOD).toString()

        return String.format("%s?titlebar=%s&back_pressed_enabled=%s&back_pressed_message=%s&url=%s", ApplinkConst.WEBVIEW, showTitleBar, backPressedEnabled, backPressedMessage, encodedUrl)
    }

    private fun hasConsentChecked(): Boolean {
        return pmCommonPreferenceManager.getBoolean(PMCommonPreferenceManager.KEY_SHOP_SCORE_CONSENT_CHECKED, false).orFalse()
    }

    private fun hasOpenedInterruptPage(): Boolean {
        return pmCommonPreferenceManager.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_COMMUNICATION_INTERRUPT_PAGE, false).orFalse()
    }
}