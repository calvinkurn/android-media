package com.tokopedia.mapviewer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mapviewer.fragment.MapViewerFragment

class MapViewerActivity : BaseSimpleActivity(){

    override fun getParentViewResourceID(): Int = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun getNewFragment(): Fragment = MapViewerFragment.getInstance(
            intent.getStringExtra(EXTRA_NAME),
            intent.getDoubleExtra(EXTRA_LATITUDE, 0.0),
            intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0),
            intent.getStringExtra(EXTRA_ADDRESS),
            intent.getStringExtra(EXTRA_PIN)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(intent.getStringExtra(EXTRA_NAME))
    }

    companion object {

        const val EXTRA_NAME = "EXTRA_PROPERTY_NAME"
        const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
        const val EXTRA_ADDRESS = "EXTRA_ADDRESS"
        const val EXTRA_PIN = "EXTRA_PIN"

        fun getCallingIntent(context: Context, propertyName: String, latitude: Double, longitude: Double,
                             address: String, pin: String) = Intent(context, MapViewerActivity::class.java)
                .putExtra(EXTRA_NAME, propertyName)
                .putExtra(EXTRA_LATITUDE, latitude)
                .putExtra(EXTRA_LONGITUDE, longitude)
                .putExtra(EXTRA_ADDRESS, address)
                .putExtra(EXTRA_PIN,pin)

    }

}
