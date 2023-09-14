package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.ARGUMENT_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.ARGUMENT_IS_PINPOINT

/**
 * Created by Irfan Khoirul on 17/11/18.
 * Deeplink: DISTRICT_RECOMMENDATION_SHOP_SETTINGS
 */
class DiscomActivity : BaseSimpleActivity() {

    companion object {
        fun newInstance(activity: Activity?, token: Token?, isLocalization: Boolean?): Intent {
            val intent = Intent(activity, DiscomActivity::class.java)
            intent.putExtra(ARGUMENT_DATA_TOKEN, token)
            intent.putExtra(IS_LOCALIZATION, isLocalization)
            return intent
        }

        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS = "district_recommendation_address"
        const val ARGUMENT_DATA_TOKEN = "token"
        const val IS_LOCALIZATION = "is_localization"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        val addressState = intent.getStringExtra(ARGUMENT_ADDRESS_STATE).orEmpty()
        val isPinpoint = intent.getBooleanExtra(ARGUMENT_IS_PINPOINT, false)
        val isLocalization = intent.getBooleanExtra(IS_LOCALIZATION, false)
        return DiscomFragment.newInstance(isPinpoint, addressState, isLocalization)
    }
}
