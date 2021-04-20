package com.tokopedia.topads.debit.autotopup.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity
import com.tokopedia.topads.debit.autotopup.view.sheet.TopAdsChooseNominalBottomSheet
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_URL
import javax.inject.Inject

/**
 * Created by Nathaniel on 11/22/2016.
 */

class TopAdsAddCreditActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun getNewFragment() = null
    private var creditResponse: CreditResponse? = null
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(TopAdsAutoTopUpViewModel::class.java)
    }

    private var userSession: UserSessionInterface? = null

    override fun getScreenName(): String? = null
    private val sheet: TopAdsChooseNominalBottomSheet? by lazy {
        TopAdsChooseNominalBottomSheet.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView(R.layout.topads_base_layout)
        populateData()
    }

    override fun onBackPressed() {
        if (intent.extras?.getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false) == true) {
            val homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME)
            startActivity(homeIntent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }


    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    private fun populateData() {
        userSession = UserSession(this)
        viewModel.populateCreditList(userSession?.shopId.toString(), ::onSuccessCreditInfo)
    }

    private fun onSuccessCreditInfo(data: CreditResponse) {
        creditResponse = data
        sheet?.show(supportFragmentManager, creditResponse, true, 0)
        sheet?.setTitle(resources.getString(R.string.title_top_ads_add_credit))
        sheet?.onSaved = { pos ->
            if (creditResponse?.credit?.isNotEmpty() == true)
                chooseCredit(pos)
        }
        sheet?.setOnDismissListener {
            finish()
        }
    }

    private fun chooseCredit(selectedCreditPos: Int) {
        if (selectedCreditPos > -1) {
            val selected = creditResponse?.credit?.get(selectedCreditPos)
            setResult(Activity.RESULT_OK)
            val intent = Intent(this, TopAdsPaymentCreditActivity::class.java).apply {
                putExtra(KEY_URL, getUrl(selected))
                putExtra(KEY_TITLE, resources.getString(R.string.title_top_ads_add_credit))
            }
            startActivity(intent)
        }
    }

    private fun getUrl(selected: DataCredit?): String {
        return URLGenerator.generateURLSessionLogin(
                Uri.encode(selected?.productUrl),
                userSession?.deviceId,
                userSession?.userId)
    }
}