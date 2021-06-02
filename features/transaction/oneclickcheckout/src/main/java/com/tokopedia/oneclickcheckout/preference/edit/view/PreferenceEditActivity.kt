package com.tokopedia.oneclickcheckout.preference.edit.view

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditModule
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.PaymentMethodFragment
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

open class PreferenceEditActivity : BaseActivity(), HasComponent<PreferenceEditComponent>, PreferenceEditParent {

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private var _addressId: Long = 0
    private var _paymentProfile = ""
    private var _paymentAmount = 0.0
    private var _directPaymentStep = false

    private var tvTitle: Typography? = null
    private var tvSubtitle: Typography? = null
    private var btnBack: IconUnify? = null
    private var btnAdd: IconUnify? = null
    private var btnDelete: IconUnify? = null
    private var stepper: ProgressBarUnify? = null

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .preferenceEditModule(PreferenceEditModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference_edit)
        initViews()
        component.inject(this)
    }

    override fun onBackPressed() {
        preferenceListAnalytics.eventClickBackArrowInPilihPembayaran()
        super.onBackPressed()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        tvTitle = findViewById(R.id.tv_title)
        tvSubtitle = findViewById(R.id.tv_subtitle)
        btnAdd = findViewById(R.id.btn_add)
        btnDelete = findViewById(R.id.btn_delete)
        stepper = findViewById(R.id.stepper)

        btnBack?.setOnClickListener {
            onBackPressed()
        }

        _paymentProfile = intent.getStringExtra(EXTRA_PAYMENT_PROFILE) ?: ""
        _paymentAmount = intent.getDoubleExtra(EXTRA_PAYMENT_AMOUNT, 0.0)
        _directPaymentStep = intent.getBooleanExtra(EXTRA_DIRECT_PAYMENT_STEP, false)

        val ft = supportFragmentManager.beginTransaction()
        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0) {
            for (fragment in fragments) {
                ft.remove(fragment)
            }
        }

        ft.replace(R.id.container, PaymentMethodFragment.newInstance())
    }

    override fun getAddressId(): Long {
        return _addressId
    }

    override fun setHeaderTitle(title: String) {
        tvTitle?.text = title
    }

    override fun setHeaderSubtitle(subtitle: String) {
        tvSubtitle?.text = subtitle
    }

    override fun hideStepper() {
        tvSubtitle?.gone()
        stepper?.gone()
    }

    override fun hideAddButton() {
        btnAdd?.gone()
    }

    override fun hideDeleteButton() {
        btnDelete?.gone()
    }

    override fun getPaymentProfile(): String {
        return _paymentProfile
    }

    override fun getPaymentAmount(): Double {
        return _paymentAmount
    }

    override fun isDirectPaymentStep(): Boolean {
        return _directPaymentStep
    }

    companion object {

        const val EXTRA_PREFERENCE_INDEX = "preference_index"
        const val EXTRA_GATEWAY_CODE = "gateway_code"
        const val EXTRA_PAYMENT_PROFILE = "payment_profile"
        const val EXTRA_PAYMENT_AMOUNT = "payment_amount"
        const val EXTRA_IS_EXTRA_PROFILE = "is_extra_profile"

        const val FROM_FLOW_OSP_STRING = "osp"

        const val EXTRA_DIRECT_PAYMENT_STEP = "direct_payment_step"

        const val EXTRA_RESULT_GATEWAY = "RESULT_GATEWAY"
        const val EXTRA_RESULT_METADATA = "RESULT_METADATA"
    }
}
