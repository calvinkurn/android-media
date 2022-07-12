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
import com.tokopedia.epharmacy.utils.ActionKeys.Companion.IMAGE_UPLOAD_FAILED
import com.tokopedia.epharmacy.utils.ActionKeys.Companion.IMAGE_UPLOAD_SUCCESS
import com.tokopedia.epharmacy.utils.ActionKeys.Companion.SUBMIT_PRESCRIPTION
import com.tokopedia.epharmacy.utils.ActionKeys.Companion.SUBMIT_SUCCESS
import com.tokopedia.epharmacy.utils.ActionKeys.Companion.UPLOAD_PRESCRIPTION
import com.tokopedia.epharmacy.utils.CategoryKeys.Companion.UPLOAD_PRESCRIPTION_PAGE
import com.tokopedia.epharmacy.utils.DEFAULT_ZERO_VALUE
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_ID_STRING
import com.tokopedia.epharmacy.utils.EXTRA_ORDER_ID_LONG
import com.tokopedia.epharmacy.utils.EventKeys.Companion.BUSINESS_UNIT_VALUE
import com.tokopedia.epharmacy.utils.EventKeys.Companion.CLICK_CONTENT
import com.tokopedia.epharmacy.utils.EventKeys.Companion.CURRENT_SITE_VALUE
import com.tokopedia.epharmacy.utils.EventKeys.Companion.IS_LOGGED_IN
import com.tokopedia.epharmacy.utils.EventKeys.Companion.OPEN_SCREEN
import com.tokopedia.epharmacy.utils.EventKeys.Companion.PAGE_PATH
import com.tokopedia.epharmacy.utils.EventKeys.Companion.SCREEN_NAME
import com.tokopedia.epharmacy.utils.EventKeys.Companion.TRACKER_ID
import com.tokopedia.epharmacy.utils.EventKeys.Companion.VIEW_CONTENT_IRIS
import com.tokopedia.epharmacy.utils.MEDIA_PICKER_REQUEST_CODE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EPharmacyActivity : BaseSimpleActivity(), HasComponent<EPharmacyComponent> {

    private val ePharmacyComponent: EPharmacyComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    @Inject
    lateinit var userSession: UserSessionInterface

    private var orderId = DEFAULT_ZERO_VALUE
    private var checkoutId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ePharmacyComponent.inject(this)
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.epharmacy_upload_title))
    }

    override fun getLayoutRes() = R.layout.epharmacy_activity
    override fun getToolbarResourceID() = R.id.e_pharmacy_header
    override fun getParentViewResourceID(): Int = R.id.e_pharmacy_parent_view

    override fun getNewFragment(): Fragment {
        orderId = if (intent.hasExtra(EXTRA_ORDER_ID_LONG))
            intent.getLongExtra(EXTRA_ORDER_ID_LONG, DEFAULT_ZERO_VALUE)
        else {
            val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
            if (pathSegments.size > 0) pathSegments[0]?.split("-")?.lastOrNull()?.trim()?.toLong()
                ?: DEFAULT_ZERO_VALUE else DEFAULT_ZERO_VALUE
        }

        checkoutId = if (intent.hasExtra(EXTRA_CHECKOUT_ID_STRING)) {
            intent.getStringExtra(EXTRA_CHECKOUT_ID_STRING) ?: ""
        } else ""

        return UploadPrescriptionFragment.newInstance(Bundle().apply {
            putLong(EXTRA_ORDER_ID_LONG, orderId)
            putString(EXTRA_CHECKOUT_ID_STRING, checkoutId)
        })
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


    fun userViewUploadPrescriptionPage(
        isLoggedInStatus: String,
        screenName: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(OPEN_SCREEN)
            .setCustomProperty(TRACKER_ID, "32358")
            .setBusinessUnit(BUSINESS_UNIT_VALUE)
            .setCurrentSite(CURRENT_SITE_VALUE)
            .setCustomProperty(IS_LOGGED_IN, isLoggedInStatus)
            .setCustomProperty(PAGE_PATH, "")
            .setCustomProperty(SCREEN_NAME, screenName)
            .setUserId(userId)
            .build()
            .send()
    }


    fun sendUploadPrescriptionButtonClickFromPreview(entryPoint : String, id : String) {
        Tracker.Builder()
            .setEvent(CLICK_CONTENT)
            .setEventAction(UPLOAD_PRESCRIPTION)
            .setEventCategory(UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: $id")
            .setCustomProperty(TRACKER_ID, "32781")
            .setBusinessUnit(BUSINESS_UNIT_VALUE)
            .setCurrentSite(CURRENT_SITE_VALUE)
            .build()
            .send()
    }

    fun sendUploadImageSuccessEvent(entryPoint : String, id : String) {
        Tracker.Builder()
            .setEvent(VIEW_CONTENT_IRIS)
            .setEventAction(IMAGE_UPLOAD_SUCCESS)
            .setEventCategory(UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: $id")
            .setCustomProperty(TRACKER_ID, "32782")
            .setBusinessUnit(BUSINESS_UNIT_VALUE)
            .setCurrentSite(CURRENT_SITE_VALUE)
            .build()
            .send()
    }


    fun sendSubmitButtonClickEvent(entryPoint : String, id : String) {
        Tracker.Builder()
            .setEvent(CLICK_CONTENT)
            .setEventAction(SUBMIT_PRESCRIPTION)
            .setEventCategory(UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: $id")
            .setCustomProperty(TRACKER_ID, "32783")
            .setBusinessUnit(BUSINESS_UNIT_VALUE)
            .setCurrentSite(CURRENT_SITE_VALUE)
            .build()
            .send()
    }


    fun sendSubmitSuccessEvent(entryPoint : String, id : String) {
        Tracker.Builder()
            .setEvent(VIEW_CONTENT_IRIS)
            .setEventAction(SUBMIT_SUCCESS)
            .setEventCategory(UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: $id")
            .setCustomProperty(TRACKER_ID, "32786")
            .setBusinessUnit(BUSINESS_UNIT_VALUE)
            .setCurrentSite(CURRENT_SITE_VALUE)
            .build()
            .send()
    }


    fun sendUploadImageFailedEvent(entryPoint : String, id : String) {
        Tracker.Builder()
            .setEvent(VIEW_CONTENT_IRIS)
            .setEventAction(IMAGE_UPLOAD_FAILED)
            .setEventCategory(UPLOAD_PRESCRIPTION_PAGE)
            .setEventLabel("entry_point: $entryPoint - id: $id")
            .setCustomProperty(TRACKER_ID, "32787")
            .setBusinessUnit(BUSINESS_UNIT_VALUE)
            .setCurrentSite(CURRENT_SITE_VALUE)
            .build()
            .send()
    }
}