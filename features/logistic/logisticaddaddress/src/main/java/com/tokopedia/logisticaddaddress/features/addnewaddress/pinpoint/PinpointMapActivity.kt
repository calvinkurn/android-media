package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token

/**
 * Created by fwidjaja on 2019-05-07.
 */
class PinpointMapActivity : BaseSimpleActivity() {
    private val FINISH_FLAG = 1212
    var SCREEN_NAME = "PinpointMapActivity"
    private var isFullFLow: Boolean = true
    private var isLogisticLabel: Boolean = true

    companion object {

        private const val EXTRA_REF = "EXTRA_REF"

        @JvmStatic
        fun newInstance(context: Context, lat: Double?, long: Double?, isShowAutoComplete: Boolean, token: Token?, isPolygon: Boolean,
                        isMismatchSolved: Boolean, isMismatch: Boolean, saveAddressDataModel: SaveAddressDataModel?, isChangesRequested: Boolean): Intent =
                Intent(context, PinpointMapActivity::class.java).apply {
                    putExtra(KERO_TOKEN, token)
                    putExtra(EXTRA_LAT, lat)
                    putExtra(EXTRA_LONG, long)
                    putExtra(EXTRA_SHOW_AUTOCOMPLETE, isShowAutoComplete)
                    putExtra(EXTRA_IS_POLYGON, isPolygon)
                    putExtra(EXTRA_IS_MISMATCH, isMismatch)
                    putExtra(EXTRA_IS_MISMATCH_SOLVED, isMismatchSolved)
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
                    putExtra(EXTRA_IS_CHANGES_REQUESTED, isChangesRequested)
                }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.extras?.let {
            isFullFLow = it.getBoolean(EXTRA_IS_FULL_FLOW, true)
            isLogisticLabel = it.getBoolean(EXTRA_IS_LOGISTIC_LABEL, true)
            it.getString(EXTRA_REF)?.let { from ->
                AddNewAddressAnalytics.sendScreenName(from)
            }
        }
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun getLayoutRes(): Int = R.layout.activity_pinpoint_map

    override fun getParentViewResourceID(): Int = R.id.pinpoint_parent

    override fun getNewFragment(): PinpointMapFragment? {
        var bundle = Bundle()
        intent.extras?.let {
            bundle = it
        }
        return PinpointMapFragment.newInstance(bundle)
    }

    override fun onStop() {
        setResult(FINISH_FLAG)
        super.onStop()
    }

    override fun onDestroy() {
        setResult(FINISH_FLAG)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var isAllowed = false
        for (i in permissions.indices) {
            if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                isAllowed = true
            }
        }

        if (isAllowed) {
            AddNewAddressAnalytics.eventClickButtonOkOnAllowLocation(isFullFLow, isLogisticLabel)
        } else {
            AddNewAddressAnalytics.eventClickButtonDoNotAllowOnAllowLocation(isFullFLow, isLogisticLabel)
        }

    }
}