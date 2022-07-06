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
import com.tokopedia.epharmacy.utils.DEFAULT_ZERO_VALUE
import com.tokopedia.epharmacy.utils.EXTRA_CHECKOUT_ID
import com.tokopedia.epharmacy.utils.EXTRA_ORDER_ID_LONG
import com.tokopedia.epharmacy.utils.MEDIA_PICKER_REQUEST_CODE
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
            if (pathSegments.size > 0) pathSegments[0]?.split("-")?.lastOrNull()?.trim()?.toLong() ?: DEFAULT_ZERO_VALUE else DEFAULT_ZERO_VALUE
        }

        checkoutId = if(intent.hasExtra(EXTRA_CHECKOUT_ID)){
            intent.getStringExtra(EXTRA_CHECKOUT_ID) ?: ""
        }else ""

        return UploadPrescriptionFragment.newInstance(Bundle().apply {
            putLong(EXTRA_ORDER_ID_LONG,orderId)
            putString(EXTRA_CHECKOUT_ID,checkoutId)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            MEDIA_PICKER_REQUEST_CODE -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.e_pharmacy_parent_view)
                fragment?.onActivityResult(requestCode, resultCode, data)
            }else -> super.onActivityResult(requestCode, resultCode, data)

        }

    }

    override fun getComponent() = ePharmacyComponent

    private fun initInjector() = DaggerEPharmacyComponent.builder()
        .baseAppComponent(
            (applicationContext as BaseMainApplication)
                .baseAppComponent
        ).build()
}