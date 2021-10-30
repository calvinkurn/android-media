package com.tokopedia.gm.common.utils

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PMTier
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager
import com.tokopedia.gm.common.data.source.local.PMCommonPreferenceManager.Companion.KEY_HAS_OPENED_END_PERIOD_INTERRUPT_PAGE
import com.tokopedia.gm.common.view.bottomsheet.EndGameInterruptBottomSheet
import com.tokopedia.gm.common.view.model.PowerMerchantInterruptUiModel
import com.tokopedia.gm.common.view.worker.GetPMInterruptDataWorker
import com.tokopedia.kotlin.extensions.orFalse
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

        private const val PATTERN_DATE_PERIOD = "yyyy-MM-dd"
        private const val PATTERN_DATE_PERIOD_TEXT = "dd MMMM"


        private const val REQUEST_CODE = 403
        private const val SHOP_SCORE_THRESHOLD = 60
    }

    private var data: PowerMerchantInterruptUiModel? = null
    private var oneTimeWorkRequest: OneTimeWorkRequest? = null

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

    fun saveRecommendationCoachMarkFlag() {
        pmCommonPreferenceManager.putBoolean(
            PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK,
            true
        )
        pmCommonPreferenceManager.apply()
    }

    fun getRecommendationCoachMarkStatus(): Boolean {
        return pmCommonPreferenceManager.getBoolean(
            PMCommonPreferenceManager.KEY_RECOMMENDATION_COACH_MARK,
            false
        ).orFalse()
    }

    fun destroy() {
        data = null
        oneTimeWorkRequest = null
    }

    private fun fetchInterruptDataWorker(
        context: Context,
        owner: LifecycleOwner,
        function: (PowerMerchantInterruptUiModel) -> Unit
    ) {
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

    private fun showPmShopScoreInterrupt(
        context: Context,
        data: PowerMerchantInterruptUiModel,
        fm: FragmentManager
    ) {
        if (data.isOfficialStore) return

        when (data.periodType) {
            PeriodType.END_GAME_PERIOD -> setupInterruptEndGamePeriod(context, data, fm)
        }
    }

    private fun setupInterruptEndGamePeriod(
        context: Context, data: PowerMerchantInterruptUiModel,
        fm: FragmentManager
    ) {
        if ((data.pmTier == PMTier.REGULAR || data.pmTier == PMTier.PRO)) {
            if (data.pmStatus == PMStatusConst.IDLE && data.shopScore < SHOP_SCORE_THRESHOLD) {
                showInterruptExistingSellerPMIdle(context, data, fm)
            }
        }
    }

    private fun showInterruptExistingSellerPMIdle(
        context: Context,
        data: PowerMerchantInterruptUiModel,
        fm: FragmentManager
    ) {
        val isPMPro = data.pmTier == PMTier.PRO
        val endPeriodStartDate =
            DateFormatUtils.formatDate(
                PATTERN_DATE_PERIOD, PATTERN_DATE_PERIOD_TEXT,
                data.periodStartDate
            )

        val titleEndGameInterrupt = context.getString(
            R.string.title_end_game_period_information_interrupt,
            endPeriodStartDate
        )
        val titleCardEndGameInterrupt = if (isPMPro) {
            context.getString(R.string.title_card_pm_pro_end_game_bottom_sheet)
        } else {
            context.getString(R.string.title_card_pm_end_game_bottom_sheet)
        }
        val descCardEndGameInterrupt = if (isPMPro) {
            context.getString(R.string.desc_card_pm_pro_end_game_bottom_sheet)
        } else {
            context.getString(R.string.desc_card_pm_end_game_bottom_sheet)
        }
        val bottomSheet = EndGameInterruptBottomSheet.createInstance(
            titleEndGameInterrupt,
            titleCardEndGameInterrupt,
            descCardEndGameInterrupt
        )
        val isHasOpenedInterruptEndGame =
            pmCommonPreferenceManager.getBoolean(KEY_HAS_OPENED_END_PERIOD_INTERRUPT_PAGE, false)

        bottomSheet.setOnDismissListener {
            if (!isHasOpenedInterruptEndGame) {
                pmCommonPreferenceManager.putBoolean(KEY_HAS_OPENED_END_PERIOD_INTERRUPT_PAGE, true)
                pmCommonPreferenceManager.apply()
            }
        }

        if (!isHasOpenedInterruptEndGame) {
            bottomSheet.show(fm)
        }
    }
}