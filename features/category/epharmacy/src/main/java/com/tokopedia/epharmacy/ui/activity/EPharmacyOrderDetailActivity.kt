package com.tokopedia.epharmacy.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.EPharmacyOrderDetailFragment
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_VERTICAL_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_WAITING_INVOICE
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.epharmacy.R as epharmacyR

class EPharmacyOrderDetailActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var tConsultationId = 0L
    private var waitingInvoice = false
    private var verticalId = String.EMPTY

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setPageTitle()
    }

    private fun setPageTitle() {
        updateTitle(getString(epharmacyR.string.epharmacy_order_detail_title))
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path.orEmpty()).pathSegments
        tConsultationId = if (pathSegments.size > 1) pathSegments[1].toLongOrZero() else 0L
        verticalId = if (pathSegments.size > 2) pathSegments[2].orEmpty() else String.EMPTY

        intent?.data?.let {
            waitingInvoice = it.getBooleanQueryParameter(EPHARMACY_WAITING_INVOICE, false).orFalse()
        }
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        extractParameters()
        return EPharmacyOrderDetailFragment.newInstance(
            Bundle().apply {
                putLong(EPHARMACY_TOKO_CONSULTATION_ID, tConsultationId)
                putBoolean(EPHARMACY_WAITING_INVOICE, waitingInvoice)
                putString(EPHARMACY_VERTICAL_ID, verticalId)
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
