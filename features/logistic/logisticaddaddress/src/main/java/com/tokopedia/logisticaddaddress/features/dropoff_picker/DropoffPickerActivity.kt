package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R


class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    lateinit var mMapView: MapView
    lateinit var mSearchInput: SearchInputView
    lateinit var mSearchText: EditText
    var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropoff_picker)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)
        mSearchInput = findViewById(R.id.search_input_dropoff)
        mSearchText = mSearchInput.searchTextView
        mSearchText.isCursorVisible = false
        mSearchText.isFocusable = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,
                    com.tokopedia.design.R.color.green_600)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val mapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map_dropoff) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
