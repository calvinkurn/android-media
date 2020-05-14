package com.tokopedia.salam.umrah.pdp.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.pdp.di.DaggerUmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.di.UmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpDetailFragment

/**
 * @author by M on 31/10/19
 */
class UmrahPdpDetailActivity : BaseSimpleActivity(), HasComponent<UmrahPdpComponent> {
    override fun getComponent(): UmrahPdpComponent = DaggerUmrahPdpComponent.builder()
            .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
            .build()

    override fun getNewFragment(): Fragment? = UmrahPdpDetailFragment().getInstance(
            intent.getStringExtra(UmrahPdpActivity.EXTRA_SLUG_NAME),
            intent.getIntExtra(EXTRA_TOTAL_AVAILABILITY,0)
    )

    companion object {
        private const val EXTRA_TOTAL_AVAILABILITY = "EXTRA_TOTAL_AVAILABILITY"
        fun createIntent(context: Context, slugName: String, totalAvailability: Int): Intent =
                Intent(context, UmrahPdpDetailActivity::class.java)
                        .putExtra(UmrahPdpActivity.EXTRA_SLUG_NAME, slugName)
                        .putExtra(EXTRA_TOTAL_AVAILABILITY, totalAvailability)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}