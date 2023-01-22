package com.tokopedia.topads.debit.autotopup.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseCreditBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsTopUpCreditInterruptSheet

class TopAdsCreditTopUpActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? = null

    private val topAdsChooseCreditBottomSheet: TopAdsChooseCreditBottomSheet by lazy {
        TopAdsChooseCreditBottomSheet.newInstance().also {
            it.isFullpage = isAutoTopUpSelected()
            it.isAutoTopUpActive = isAutoTopUpActive()
            it.isAutoTopUpSelected = isAutoTopUpSelected()
        }
    }
    private val topAdsTopUpCreditInterruptSheet by lazy {
        TopAdsTopUpCreditInterruptSheet.newInstance().also {
            if (isAutoTopUpActive()) it.isAutoTopUpActive = true
            it.topUpCount = getTopUpCount()
            it.autoTopUpBonus = getAutoTopUpBonus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_base_layout)

        if (Utils.isShowInterruptSheet(this)) {
            topAdsTopUpCreditInterruptSheet.show(supportFragmentManager)
        } else {
            topAdsChooseCreditBottomSheet.show(supportFragmentManager)
        }

        topAdsChooseCreditBottomSheet.setOnDismissListener {
            finish()
        }

    }

    private fun isAutoTopUpActive(): Boolean {
        return intent?.extras?.getBoolean(IS_AUTO_TOP_UP_ACTIVE, false) ?: false
    }

    private fun isAutoTopUpSelected(): Boolean {
        return intent?.extras?.getBoolean(IS_AUTO_TOP_UP_SELECTED, false) ?: false
    }

    private fun getTopUpCount(): Int {
        return intent?.extras?.getInt(IS_AUTO_TOP_UP_SELECTED, Int.ZERO) ?: Int.ZERO
    }

    private fun getAutoTopUpBonus(): Double {
        return intent?.extras?.getDouble(AUTO_TOP_UP_BONUS, 0.0) ?: 0.0
    }

    companion object {
        const val IS_AUTO_TOP_UP_ACTIVE = "isAutoTopUpActive"
        const val IS_AUTO_TOP_UP_SELECTED = "isAutoTopUpSelected"
        const val TOP_UP_COUNT = "TopUpCount"
        const val AUTO_TOP_UP_BONUS = "AutoTopUpBonus"
    }
}
