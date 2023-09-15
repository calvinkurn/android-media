package com.tokopedia.epharmacy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyCheckoutFragment
import com.tokopedia.epharmacy.ui.fragment.EPharmacyOrderDetailFragment
import com.tokopedia.epharmacy.utils.EPHARMACY_ORDER_ID
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyOrderDetailActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var orderId = String.EMPTY

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setPageTitle()
    }

    private fun setPageTitle() {
        updateTitle(getString(com.tokopedia.epharmacy.R.string.epharmacy_order_detail_title))
    }

    private fun extractParameters() {
        intent?.data?.let { uri ->
            orderId = uri.getQueryParameter(EPHARMACY_ORDER_ID).orEmpty()
        }
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        extractParameters()
        return EPharmacyOrderDetailFragment.newInstance(
            Bundle().apply {
                putString(EPHARMACY_ORDER_ID, orderId)
            }
        )
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}
