package com.tokopedia.logisticaddaddress.features.pinpoint.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.constant.PinpointSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_CURRENT_LOC
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_DISTRICT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LAT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LOCATION_PASS
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_LONG_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_SOURCE_PINPOINT
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_CURRENT_LOC
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_DISTRICT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.PARAM_SOURCE
import com.tokopedia.logisticaddaddress.utils.ParcelableHelper.parcelable
import com.tokopedia.url.TokopediaUrl

class PinpointWebviewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val url = generateUrl()
        return PinpointWebviewFragment.newInstance(
            generateUrl(),
            intent.parcelable(
                KEY_LOCATION_PASS
            ),
            intent.parcelable(KEY_ADDRESS_DATA),
            intent.getStringExtra(KEY_SOURCE_PINPOINT)
        )
    }

    private fun generateUrl(): String {
        return Uri.parse(TokopediaUrl.getInstance().WEB)
            .buildUpon()
            .appendPath(URL_PINPOINT)
            .setDistrictId()
            .setLatitude()
            .setLongitude()
            .setCurrentLoc()
            .setSource()
            .build().toString().decodeToUtf8()
    }

    private fun Uri.Builder.setDistrictId(): Uri.Builder {
        val districtId: Long = if (intent.hasExtra(KEY_DISTRICT_ID)) {
            intent.getLongExtra(KEY_DISTRICT_ID, 0L)
        } else {
            intent.data?.getQueryParameter(PARAM_DISTRICT_ID)?.toLongOrZero() ?: 0L
        }

        districtId.takeIf { value -> value != 0L }
            ?.let { data ->
                appendQueryParameter(PARAM_DISTRICT_ID, data.toString())
            }
        return this
    }

    private fun Uri.Builder.setCurrentLoc(): Uri.Builder {
        val currentLoc: Boolean = if (intent.hasExtra(KEY_CURRENT_LOC)) {
            intent.getBooleanExtra(KEY_CURRENT_LOC, false)
        } else {
            intent.data?.getQueryParameter(PARAM_CURRENT_LOC)?.toBooleanStrictOrNull() ?: false
        }

        currentLoc.takeIf { value -> value }
            ?.let { data ->
                appendQueryParameter(PARAM_CURRENT_LOC, data.toString())
            }
        return this
    }

    private fun Uri.Builder.setLongitude(): Uri.Builder {
        val longitude: Double = if (intent.hasExtra(KEY_LONG_ID)) {
            intent.getDoubleExtra(KEY_LONG_ID, 0.0)
        } else {
            intent.data?.getQueryParameter(PARAM_LONG)?.toDoubleOrZero() ?: 0.0
        }

        longitude.takeIf { value -> value != 0.0 }
            ?.let { data ->
                appendQueryParameter(PARAM_LONG, data.toString())
            }
        return this
    }

    private fun Uri.Builder.setLatitude(): Uri.Builder {
        val latitude: Double = if (intent.hasExtra(KEY_LAT_ID)) {
            intent.getDoubleExtra(KEY_LAT_ID, 0.0)
        } else {
            intent.data?.getQueryParameter(PARAM_LAT)?.toDoubleOrZero() ?: 0.0
        }

        latitude.takeIf { value -> value != 0.0 }
            ?.let { data ->
                appendQueryParameter(PARAM_LAT, data.toString())
            }
        return this
    }

    private fun Uri.Builder.setSource(): Uri.Builder {
        val value: String? = if (intent.hasExtra(KEY_SOURCE_PINPOINT)) {
            intent.getStringExtra(KEY_SOURCE_PINPOINT)
        } else {
            intent.data?.getQueryParameter(PARAM_SOURCE)
        }

        value?.takeIf { data -> data.isNotEmpty() }?.run {
            try {
                appendQueryParameter(PARAM_SOURCE, PinpointSource.valueOf(this).param)
            } catch (@Suppress("SwallowedException") e: IllegalArgumentException) {
                // no op
            }
        }
        return this
    }

    companion object {
        private const val URL_PINPOINT: String = "pin-point-web-view"
        fun getIntent(
            context: Context,
            districtId: Long? = null,
            lat: Double? = null,
            lng: Double? = null,
            currentLoc: Boolean = false,
            locationPass: LocationPass? = null,
            saveAddressDataModel: SaveAddressDataModel? = null,
            source: PinpointSource? = null
        ): Intent {
            return Intent(context, PinpointWebviewActivity::class.java).apply {
                putExtra(KEY_DISTRICT_ID, districtId)
                putExtra(KEY_LAT_ID, lat)
                putExtra(KEY_LONG_ID, lng)
                putExtra(KEY_CURRENT_LOC, currentLoc)
                putExtra(KEY_LOCATION_PASS, locationPass)
                putExtra(KEY_ADDRESS_DATA, saveAddressDataModel)
                putExtra(KEY_SOURCE_PINPOINT, source.toString())
            }
        }
    }
}
