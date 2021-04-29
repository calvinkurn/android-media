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
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.shopscore.DeepLinkMapperShopScore
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.gm.common.view.bottomsheet.PMFinalInterruptBottomSheet
import com.tokopedia.gm.common.view.bottomsheet.PMTransitionInterruptBottomSheet
import com.tokopedia.gm.common.view.bottomsheet.SimpleInterruptBottomSheet
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.gm.common.view.worker.GetPMInterruptDataWorker
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import java.net.URLEncoder
import java.util.*
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

    fun getPeriodType() = data?.periodType

    fun showInterrupt(context: Context, owner: LifecycleOwner, fm: FragmentManager) {
        init(context)
        initWorker()

        fetchInterruptDataWorker(context, owner) {
            this.data = it
            showPmShopScoreInterrupt(context, it, fm)
        }
    }

    fun setShopScoreConsentStatus(context: Context, uri: Uri) {
        init(context)
        val isConsentApproved = uri.getBooleanQueryParameter(DeepLinkMapperShopScore.PARAM_IS_CONSENT, false)
        if (isConsentApproved) {
            pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_SHOP_SCORE_CONSENT_CHECKED, true)
            pmCommonPreferenceManager?.apply()
        }
    }

    fun onActivityResult(requestCode: Int, callback: () -> Unit) {
        if (requestCode == REQUEST_CODE && data?.periodType == PeriodType.TRANSITION_PERIOD) {
            val hasShownCoachMark = pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK, false).orFalse()
            if (!hasShownCoachMark) {
                callback()
            }
        }
    }

    fun saveRecommendationCoachMarkFlag() {
        pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK, true)
        pmCommonPreferenceManager?.apply()
    }

    fun getRecommendationCoachMarkStatus(): Boolean {
        val hasShownCoachMark = pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK, false).orFalse()
        val isTransitionPeriod = data?.periodType == PeriodType.TRANSITION_PERIOD
        return !hasShownCoachMark && hasOpenedInterruptPage() && isTransitionPeriod
    }

    fun openInterruptPage(context: Context) {
        init(context)
        val intent = RouteManager.getIntent(context, getInterruptPageUrl(context))
        (context as? Activity)?.startActivityForResult(intent, REQUEST_CODE)
        val numberOfPageOpened = pmCommonPreferenceManager?.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
        pmCommonPreferenceManager?.putInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, numberOfPageOpened.plus(1))
        pmCommonPreferenceManager?.apply()
    }

    fun destroy() {
        data = null
        oneTimeWorkRequest = null
        pmCommonPreferenceManager = null
    }

    private fun init(context: Context) {
        if (pmCommonPreferenceManager == null) {
            pmCommonPreferenceManager = PMCommonPreferenceManager(context.applicationContext)
        }
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
        when (data.periodType) {
            PeriodType.TRANSITION_PERIOD -> setupInterruptTransitionPeriod(context, data, fm)
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

    private fun showInterruptEndOfTenureNewSeller(context: Context, data: PowerMerchantInterruptUiModel, fm: FragmentManager) {
        val bottomSheet = SimpleInterruptBottomSheet.createInstance(true)
        val isBottomSheetEverSeen = pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_NEW_SELLER_END_OF_TENURE_POPUP, false).orFalse()
        if (fm.isStateSaved || bottomSheet.isAdded || isBottomSheetEverSeen) return

        val now = Date().time
        val shopAge = data.shopAge
        val endOfTenureDays = 90
        val remainingDays = endOfTenureDays.minus(shopAge)
        val canShopInterruptPopup = remainingDays in 0..7

        if (!(remainingDays in 1..endOfTenureDays && canShopInterruptPopup)) return

        val remainingDaysMillis = TimeUnit.DAYS.toMillis(remainingDays.toLong())
        val endOfTenureMillis = now.plus(remainingDaysMillis)

        val endOfTenureCal = Calendar.getInstance().apply {
            timeInMillis = endOfTenureMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val endOfTenureFirstMondayCal = Calendar.getInstance().apply {
            timeInMillis = endOfTenureMillis
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val dateFormat = "dd MMMM yyyy"
        val endOfTenureFirstMondayStr = when {
            endOfTenureFirstMondayCal < endOfTenureCal -> {
                val days7Millis = TimeUnit.DAYS.toMillis(7)
                DateFormatUtils.getFormattedDate(endOfTenureFirstMondayCal.timeInMillis.plus(days7Millis), dateFormat)
            }
            else -> DateFormatUtils.getFormattedDate(endOfTenureFirstMondayCal.timeInMillis, dateFormat)
        }

        val title = context.getString(R.string.gmc_new_seller_end_of_tenure_interrupt_title, endOfTenureFirstMondayStr)
        val description = context.getString(R.string.gmc_new_seller_end_of_tenure_interrupt_description)
        val ctaText = context.getString(R.string.gmc_check_your_shop_performance)

        bottomSheet.setContent(title, description)
                .setOnCtaClickListener(ctaText) {
                    RouteManager.route(context, ApplinkConst.SHOP_SCORE_DETAIL)
                }
                .setOnDismissListener {
                    pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_NEW_SELLER_END_OF_TENURE_POPUP, true)
                    pmCommonPreferenceManager?.apply()
                }
        bottomSheet.show(fm)
    }

    private fun showInterruptNewSellerPmIdle(context: Context, fm: FragmentManager) {
        val bottomSheet = SimpleInterruptBottomSheet.createInstance(false)
        val isBottomSheetEverSeen = pmCommonPreferenceManager?.getBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_NEW_SELLER_PM_IDLE_POPUP, false).orFalse()
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
                    pmCommonPreferenceManager?.putBoolean(PMCommonPreferenceManager.KEY_HAS_OPENED_NEW_SELLER_PM_IDLE_POPUP, true)
                    pmCommonPreferenceManager?.apply()
                }
        bottomSheet.show(fm)
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
        showInterruptPage(context, data)
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

    private fun getInterruptPageUrl(context: Context): String {
        val numberOfPageOpened = pmCommonPreferenceManager?.getInt(PMCommonPreferenceManager.KEY_NUMBER_OF_INTERRUPT_PAGE_OPENED, 0).orZero()
        val hasConsentChecked = hasConsentChecked()
        val param = mapOf<String, Any>(
                PARAM_OPEN to numberOfPageOpened.plus(1),
                PARAM_HAS_CLICKED to hasConsentChecked
        )
        val url = UriUtil.buildUriAppendParams(PMConstant.Urls.SHOP_SCORE_INTERRUPT_PAGE, param)
        val encodedUrl = URLEncoder.encode(url, "UTF-8")
        val backPressedMessage = context.getString(R.string.pm_on_back_pressed_disabled_message)
        return String.format("%s?url=%s&titlebar=false&back_pressed_enabled=false&back_pressed_message=%s", ApplinkConst.WEBVIEW, encodedUrl, backPressedMessage)
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