package com.tokopedia.topads.debit.autotopup.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseCreditBottomSheet
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsTopUpCreditInterruptSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.topads_dash_activity_base_layout.*

class TopAdsCreditTopUpActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? = null

    private var userSession: UserSessionInterface? = null

    private val topAdsChooseCreditBottomSheet: TopAdsChooseCreditBottomSheet by lazy {
        TopAdsChooseCreditBottomSheet.newInstance().also {
            it.isFullpage = isAutoTopUpSelected()
            it.isAutoTopUpActive = isAutoTopUpActive()
            it.isAutoTopUpSelected = isAutoTopUpSelected()
            it.customPeekHeight = 600
            it.setTitle(resources.getString(R.string.title_top_ads_add_credit))
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

        topAdsChooseCreditBottomSheet.onSaved = { productUrl, isAutoAdsSaved ->
            if (productUrl.isNotEmpty()) chooseCredit(productUrl)
            else if (isAutoAdsSaved) {
                root?.let {
                    Toaster.build(
                        it, getString(R.string.topads_dash_auto_topup_activated_toast),
                        Snackbar.LENGTH_SHORT,
                        Toaster.TYPE_NORMAL,
                        getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)
                    ).show()
                }
            }
        }
        topAdsChooseCreditBottomSheet.setOnDismissListener {
            finish()
        }

    }

    private fun chooseCredit(productUrl: String) {
        setResult(Activity.RESULT_OK)
        val intent = Intent(this, TopAdsPaymentCreditActivity::class.java).apply {
            putExtra(KEY_URL, getUrl(productUrl))
            putExtra(KEY_TITLE, resources.getString(R.string.title_top_ads_add_credit))
        }
        startActivity(intent)

    }

    private fun getUrl(productUrl: String): String {
        return URLGenerator.generateURLSessionLogin(
            Uri.encode(productUrl),
            userSession?.deviceId,
            userSession?.userId
        )
    }

    private fun isAutoTopUpActive(): Boolean {
        return intent?.extras?.getBoolean(IS_AUTO_TOP_UP_ACTIVE, false) ?: false
    }

    private fun isAutoTopUpSelected(): Boolean {
        return intent?.extras?.getBoolean(IS_AUTO_TOP_UP_SELECTED, false) ?: false
    }

    private fun getTopUpCount(): Int {
        return intent?.extras?.getInt(IS_AUTO_TOP_UP_SELECTED, 0) ?: 0
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
