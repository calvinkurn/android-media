package com.tokopedia.deals.home.ui.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.home.di.DaggerDealsHomeComponent
import com.tokopedia.deals.home.di.DealsHomeComponent
import com.tokopedia.deals.home.ui.fragment.DealsHomeFragment
import com.tokopedia.deals.location_picker.DealsLocationConstants
import com.tokopedia.deals.location_picker.model.response.Location

/**
 * @author by jessica on 16/06/20
 */

class DealsHomeActivity : DealsBaseActivity(), HasComponent<DealsHomeComponent> {

    override fun getNewFragment(): Fragment = DealsHomeFragment.getInstance()

    override fun isSearchAble(): Boolean = false

    override fun getComponent(): DealsHomeComponent {
        return DaggerDealsHomeComponent.builder()
                .dealsComponent(getDealsComponent())
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