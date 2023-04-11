package com.tokopedia.epharmacy.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.DaggerEPharmacyComponent
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.ui.fragment.UploadPrescriptionFragment
import com.tokopedia.epharmacy.utils.*
import com.tokopedia.epharmacy.utils.TrackerId.Companion.OPEN_SCREEN_ID
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var orderId = DEFAULT_ZERO_VALUE
    private var checkoutId = ""
    private var entryPoint = ""
    private var source = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        if (!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_EPHARMACY_UPLOAD_PAGE, true)) {
            this.finish()
        }
        super.onCreate(savedInstanceState)
        setPageTitle()
    }

    private fun setPageTitle() {
        updateTitle(getString(R.string.epharmacy_upload_title))
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        extractOrderId()
        extractCheckoutId()
        extractSource()
        extractEntryPoint()

        return UploadPrescriptionFragment.newInstance(
            Bundle().apply {
                putLong(EXTRA_ORDER_ID_LONG, orderId)
                putString(EXTRA_CHECKOUT_ID_STRING, checkoutId)
                putString(EXTRA_ENTRY_POINT_STRING, entryPoint)
                putString(EXTRA_SOURCE_STRING, source)
            }
        )
    }

    private fun extractOrderId() {
        orderId = if (intent.hasExtra(EXTRA_ORDER_ID_LONG)) {
            intent.getLongExtra(EXTRA_ORDER_ID_LONG, DEFAULT_ZERO_VALUE)
        } else {
            val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
            if (pathSegments.size > 0) {
                pathSegments.firstOrNull()?.split("-")?.lastOrNull()?.trim()?.toLongOrZero()
                    ?: DEFAULT_ZERO_VALUE
            } else {
                DEFAULT_ZERO_VALUE
            }
        }
    }

    private fun extractCheckoutId() {
        checkoutId = if (intent.hasExtra(EXTRA_CHECKOUT_ID_STRING)) {
            intent.getStringExtra(EXTRA_CHECKOUT_ID_STRING) ?: ""
        } else {
            ""
        }
    }

    private fun extractSource() {
        source = intent.getStringExtra(EXTRA_SOURCE_STRING) ?: ENTRY_POINT_CHECKOUT.lowercase()
    }

    private fun extractEntryPoint() {
        entryPoint = if (orderId == DEFAULT_ZERO_VALUE) {
            userViewUploadPrescriptionPage(ENTRY_POINT_CHECKOUT, checkoutId)
            ENTRY_POINT_CHECKOUT
        } else {
            userViewUploadPrescriptionPage(ENTRY_POINT_ORDER, orderId.toString())
            ENTRY_POINT_ORDER
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MEDIA_PICKER_REQUEST_CODE -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.e_pharmacy_parent_view)
                fragment?.onActivityResult(requestCode, resultCode, data)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()

    private fun userViewUploadPrescriptionPage(ep: String, id: String) {
        Tracker.Builder()
            .setEvent(EventKeys.OPEN_SCREEN)
            .setCustomProperty(EventKeys.TRACKER_ID, OPEN_SCREEN_ID)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.IS_LOGGED_IN, userSession.isLoggedIn.toString())
            .setCustomProperty(EventKeys.PAGE_PATH, "")
            .setCustomProperty(EventKeys.SCREEN_NAME, "view upload prescription page - $ep - new flow - $id")
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
