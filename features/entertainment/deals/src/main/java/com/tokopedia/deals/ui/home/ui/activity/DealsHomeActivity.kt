package com.tokopedia.deals.ui.home.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.di.DaggerDealsComponent
import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.di.DealsModule
import com.tokopedia.deals.ui.category.DealsCategoryActivity
import com.tokopedia.deals.ui.home.ui.fragment.DealsHomeFragment
import com.tokopedia.deals.ui.location_picker.DealsLocationConstants
import com.tokopedia.deals.ui.location_picker.model.response.Location

/**
 * @author by jessica on 16/06/20
 */

class DealsHomeActivity : DealsBaseActivity(), HasComponent<DealsComponent> {

    override fun getNewFragment(): Fragment = DealsHomeFragment.getInstance()

    override fun isSearchAble(): Boolean = false

    override fun getComponent(): DealsComponent {
        return DaggerDealsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .dealsModule(DealsModule(this))
            .build()
    }

    override fun isHomePage(): Boolean = true

    override fun setCurrentLocation(location: Location) {
        if (location.name.isEmpty()) {
            baseViewModel.getCurrentLocation(location.coordinates)
        } else {
            if (location.locType.name.equals(DealsLocationConstants.LANDMARK, true)) {
                dealsLocationUtils.updateLocation(location)
                startActivityForResult(
                    DealsCategoryActivity.getCallingIntent(this, "", true),
                    DEALS_LANDMARK_CATEGORY_REQ_CODE
                )
            } else {
                baseViewModel.setCurrentLocation(location)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            DEALS_LANDMARK_CATEGORY_REQ_CODE -> {
                dealsLocationUtils.updateLocation(currentLoc)
            }
        }
    }

    companion object {
        const val DEALS_LANDMARK_CATEGORY_REQ_CODE = 333
        fun getCallingIntent(context: Context): Intent = Intent(context, DealsHomeActivity::class.java)
    }
}
