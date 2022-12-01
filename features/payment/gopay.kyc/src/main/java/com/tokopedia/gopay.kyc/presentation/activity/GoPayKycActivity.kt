package com.tokopedia.gopay.kyc.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.di.DaggerGoPayKycComponent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayPlusKycBenefitFragment
import com.tokopedia.header.HeaderUnify

class GoPayKycActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent> {

    private val kycComponent: GoPayKycComponent by lazy { initInjector() }

    private fun initInjector() =
        DaggerGoPayKycComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    /*
    * This helps in redirecting to entry point activity if user wants to exit the kyc flow
    * */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.hasExtra(IS_EXIT_KYC) == true)
            finish()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_kyc_layout
    override fun getParentViewResourceID(): Int = R.id.kycFrameLayout
    override fun getToolbarResourceID() = R.id.kycHeader
    override fun getNewFragment() = GoPayPlusKycBenefitFragment.newInstance()
    override fun getScreenName() = null
    override fun getComponent() = kycComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateHeaderTitle()
    }

    private fun updateHeaderTitle() {
        val header=findViewById<HeaderUnify>(R.id.kycHeader)
        header.headerTitle = getString(R.string.kyc_header_title)
    }

    companion object {
        const val IS_EXIT_KYC = "exitFlow"
        fun getIntent(context: Context) = Intent(context, GoPayKycActivity::class.java)
    }

}