package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.util.rxEditText
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentSearchAddressBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageFragment
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_LATITUDE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_LONGITUDE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.android.synthetic.main.fragment_google_map.*
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class SearchPageFragment: BaseDaggerFragment(), AutoCompleteListAdapter.AutoCompleteItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)
    }

    private lateinit var autoCompleteAdapter: AutoCompleteListAdapter

    private var bottomSheetLocUndefined: BottomSheetUnify? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
    private val compositeSubs: CompositeSubscription by lazy { CompositeSubscription() }
    private var binding by autoCleared<FragmentSearchAddressBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
       getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        showInitialLoadMessage()
        setSearchView()
        setViewListener()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1599 && resultCode == Activity.RESULT_OK) {
            val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
            finishActivity(newAddress)
        } else if (requestCode == 1995) {
            showInitialLoadMessage()
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(LogisticConstant.EXTRA_ADDRESS_NEW, data)
            })
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size == requiredPermissions.size) {
            getLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubs.clear()
    }

    private fun initView() {
        autoCompleteAdapter = AutoCompleteListAdapter(this)
        binding.rvAddressList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvAddressList.adapter = autoCompleteAdapter

    }

    private fun showInitialLoadMessage() {
        binding.searchPageInput.searchBarPlaceholder = getString(R.string.txt_hint_search)
        binding.searchPageInput.searchBarTextField.setText("")
        binding.tvMessageSearch.text = getString(R.string.txt_message_initial_load)
        binding.tvMessageSearch.setOnClickListener {
            Toast.makeText(context, "This feature is under development", Toast.LENGTH_SHORT).show()
            //go to ANA form negative
        }
    }

    private fun setSearchView() {
        binding.searchPageInput.searchBarTextField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvMessageSearch.text = "Tidak ketemu? Isi alamat secara manual"
                if (TextUtils.isEmpty(binding.searchPageInput.searchBarTextField.text.toString())) {
                    hideListLocation()
                } else {
                    loadAutoComplete(binding.searchPageInput.searchBarTextField.text.toString())
                }
            }

        })


    }
/*
    private fun openSoftKeyboard() {
        binding.searchPageInput.searchBarTextField.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }*/

    private fun setViewListener() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        //loading dsb
        binding.rlSearchCurrentLocation.setOnClickListener {
            if (allPermissionsGranted()) {
                hasRequestedLocation = true
                getLocation()
            } else {
                hasRequestedLocation = false
                requestPermissionLocation()
            }
        }
    }

    private fun showBottomSheetLocUndefined(){
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding = BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBinding.root)
            setOnDismissListener { dismiss() }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined?.show(it, "")
        }
    }

    private fun setupBottomSheetLocUndefined(viewBinding: BottomsheetLocationUndefinedBinding) {
        viewBinding?.run {
            imgLocUndefined.setImageUrl(LOCATION_NOT_FOUND)
            tvLocUndefined.text = "Lokasi tidak terdeteksi"
            tvInfoLocUndefined.text = "Kami tidak dapat mengakses lokasimu. Untuk menggunakan fitur ini, silakan aktifkan layanan lokasi kamu."
            btnActivateLocation.setOnClickListener {
                goToSettingLocationPage()
            }
        }
    }

    private fun goToSettingLocationPage() {
        if (context?.let { turnGPSOn(it) } == false) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun turnGPSOn(context: Context): Boolean {
        var isGpsOn = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mSettingsClient = LocationServices.getSettingsClient(context)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000
        locationRequest.fastestInterval = 2 * 1000
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGpsOn = true
        } else {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(context as Activity) {
                        //  GPS is already enable, callback GPS status through listener
                        isGpsOn = true
                    }
                    .addOnFailureListener(context, OnFailureListener { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(context, AddressConstants.GPS_REQUEST)
                                } catch (sie: IntentSender.SendIntentException) {
                                    sie.printStackTrace()
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
        }
        return isGpsOn
    }

    private fun getLastLocationClient() {

    }

    private fun initObserver() {
        viewModel.autoCompleteList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    loadListLocation(it.data)
                }
                is Fail -> {
                    Timber.d(it.throwable)
                    hideListLocation()
                }
            }
        })
    }

    private fun loadAutoComplete(input: String) {
        viewModel.getAutoCompleteList(input)
    }

    private fun loadListLocation(suggestedPlace: Place) {
        if (suggestedPlace.data.isNotEmpty()) {
            binding.rvAddressList.visibility = View.VISIBLE
            autoCompleteAdapter.setData(suggestedPlace.data)
        }
    }

    private fun hideListLocation() {
        binding.rvAddressList.visibility = View.GONE
    }

    private fun requestPermissionLocation() {
        requestPermissions(requiredPermissions, 9876)
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (AddNewAddressUtils.isGpsEnabled(context)) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
                if (data != null) {
                   goToPinpointPage(null, data.latitude, data.longitude)
                } else {
                    fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
                        createLocationCallback(), null)
                }

            }
        } else {
            showBottomSheetLocUndefined()
        }
    }

    fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                   //send to maps
                    hasRequestedLocation = true
                }
            }
        }
    }


    override fun onItemClicked(placeId: String) {
        goToPinpointPage(placeId, null, null)
    }

    private fun goToPinpointPage(placeId: String?, latitude: Double?, longitude: Double?) {
        val bundle = Bundle()
        bundle.putString(EXTRA_PLACE_ID, placeId)
        latitude?.let { bundle.putDouble(EXTRA_LATITUDE, it) }
        longitude?.let { bundle.putDouble(EXTRA_LONGITUDE, it) }
        startActivityForResult(context?.let { PinpointNewPageActivity.createIntent(it, bundle) }, 1998)
        activity?.finish()
    }

    companion object {
        fun newInstance(bundle: Bundle): SearchPageFragment {
            return SearchPageFragment().apply {
                arguments = bundle
            }
        }
    }
}