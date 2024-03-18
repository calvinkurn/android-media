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
import com.tokopedia.epharmacy.utils.CategoryKeys
import com.tokopedia.epharmacy.utils.EPHARMACY_ENABLER_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_GROUP_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EventKeys
import com.tokopedia.epharmacy.utils.TrackerId.Companion.CLICK_BACK_CHECKOUT
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyCheckoutActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private var tokoConsultationId = 0L
    private var enablerId = 0L
    private var groupId = String.EMPTY

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        findViewById<HeaderUnify>(R.id.e_pharmacy_header).apply {
            setSupportActionBar(toolbar)
            setNavigationOnClickListener {
                sendClickBackEvent("$enablerId - $groupId - $tokoConsultationId")
                finish()
            }
            supportActionBar?.let {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
            }
            setPageTitle()
        }
    }

    private fun setPageTitle() {
        updateTitle(getString(R.string.epharmacy_checkout_page_title))
    }

    private fun extractParameters() {
        intent?.data?.let { uri ->
            groupId = uri.getQueryParameter(EPHARMACY_GROUP_ID).orEmpty()
            enablerId = uri.getQueryParameter(EPHARMACY_ENABLER_ID).toLongOrZero()
            tokoConsultationId = uri.getQueryParameter(EPHARMACY_TOKO_CONSULTATION_ID).toLongOrZero()
        }
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        extractParameters()
        return EPharmacyCheckoutFragment.newInstance(
            Bundle().apply {
                putString(EPHARMACY_GROUP_ID, groupId)
                putLong(EPHARMACY_ENABLER_ID, enablerId)
                putLong(EPHARMACY_TOKO_CONSULTATION_ID, tokoConsultationId)
            }
        )
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()

    private fun sendClickBackEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_GROCERIES)
            .setEventAction("click back")
            .setEventCategory(CategoryKeys.EPHARMACY_CHAT_DOKTER_CHECKOUT_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, CLICK_BACK_CHECKOUT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}
