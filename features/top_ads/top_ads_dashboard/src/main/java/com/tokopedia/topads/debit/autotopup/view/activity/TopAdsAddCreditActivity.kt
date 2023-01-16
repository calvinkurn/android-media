package com.tokopedia.topads.debit.autotopup.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseCreditBottomSheet
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import kotlinx.android.synthetic.main.topads_dash_activity_base_layout.*
import javax.inject.Inject

/**
 * Created by Nathaniel on 11/22/2016.
 */

class TopAdsAddCreditActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getNewFragment(): Fragment?  = null
    override fun getScreenName(): String? = null

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(TopAdsAutoTopUpViewModel::class.java)
    }

    private var userSession: UserSessionInterface? = null

    private val sheet: TopAdsChooseCreditBottomSheet? by lazy {
        TopAdsChooseCreditBottomSheet.newInstance().also {
            if (showFullScreenBottomSheet()) it.isFullpage = true
            if (isAutoTopUpActive()) it.isAutoTopUpActive = true
            if (isAutoTopUpSelected()) it.isAutoTopUpSelected = true
            it.customPeekHeight = 600
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView(R.layout.topads_base_layout)
        sheet?.show(supportFragmentManager)
        sheet?.setTitle(resources.getString(R.string.title_top_ads_add_credit))

        sheet?.onSaved = { productUrl, isAutoAdsSaved ->
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

        sheet?.setOnDismissListener {
            finish()
        }

    }

    override fun onBackPressed() {
        if (intent.extras?.getBoolean(
                TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH,
                false
            ) == true
        ) {
            val homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME)
            startActivity(homeIntent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
            .inject(this)
    }


    override fun getComponent(): TopAdsDashboardComponent =
        DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        ).build()

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

    private fun showFullScreenBottomSheet(): Boolean {
        return intent?.extras?.getBoolean(SHOW_FULL_SCREEN_BOTTOM_SHEET, false) ?: false
    }

    private fun isAutoTopUpActive(): Boolean {
        return intent?.extras?.getBoolean(IS_AUTO_TOP_UP_ACTIVE, false) ?: false
    }
    private fun isAutoTopUpSelected(): Boolean {
        return intent?.extras?.getBoolean(IS_AUTO_TOP_UP_SELECTED, false) ?: false
    }

    companion object {
        const val IS_AUTO_TOP_UP_ACTIVE = "isAutoTopUpActive"
        const val IS_AUTO_TOP_UP_SELECTED = "isAutoTopUpSelected"
        const val SHOW_FULL_SCREEN_BOTTOM_SHEET = "FullScreenBottomSheet"
    }
}
