package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
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
import androidx.core.app.ActivityCompat
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
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentSearchAddressBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageFragment
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_LATITUDE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_LONGITUDE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.android.synthetic.main.fragment_google_map.*
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class SearchPageFragment: BaseDaggerFragment(), AutoCompleteListAdapter.AutoCompleteItemListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)
    }

    private lateinit var autoCompleteAdapter: AutoCompleteListAdapter

    private var bottomSheetLocUndefined: BottomSheetUnify? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    private var isPositiveFlow: Boolean = true

    private var isFromPinpoint: Boolean = false

    private var isPermissionAccessed: Boolean = false

    private var saveDataModel: SaveAddressDataModel? = null
    private var currentKotaKecamatan: String? = ""
    private var isPolygon: Boolean = false
    private var distrcitId: Int? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW)
            isFromPinpoint = it.getBoolean(EXTRA_FROM_PINPOINT)
            currentKotaKecamatan = it.getString(EXTRA_KOTA_KECAMATAN)
            saveDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            isPolygon = it.getBoolean(EXTRA_IS_POLYGON)
            distrcitId = it.getInt(EXTRA_DISTRICT_ID)
        }
        if (saveDataModel != null) {
            saveDataModel?.let {
                viewModel.setAddress(it)
            }
        }
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
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_PINPOINT_PAGE) {
                val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
                var newAddress = data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
                if (newAddress == null) {
                    newAddress = data?.getParcelableExtra(EXTRA_SAVE_DATA_UI_MODEL)
                }
                isFromAddressForm?.let { finishActivity(newAddress, it) }
            } else if (requestCode == REQUEST_ADDRESS_FORM_PAGE) {
                val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
                finishActivity(newAddress, false)
            }
        } else {
            showInitialLoadMessage()
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?, isFromAddressForm: Boolean) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(LogisticConstant.EXTRA_ADDRESS_NEW, data)
                putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
            })
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isAllowed = false
        for (permission in permissions) {
            if (!isPermissionAccessed) {
                if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, permission) } == true) {
                    //no-op
                } else {
                    if (activity?.let { ActivityCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_GRANTED) {
                        isAllowed = true
                        getLocation()
                        isPermissionAccessed = true
                    } else {
                        showBottomSheetLocUndefined(true)
                    }
                }
            }
        }

        if (isAllowed) {
            AddNewAddressRevampAnalytics.onClickAllowLocationSearch(userSession.userId)
        } else {
            AddNewAddressRevampAnalytics.onClickDontAllowLocationSearch(userSession.userId)
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
            AddNewAddressRevampAnalytics.onClickIsiAlamatManualSearch(userSession.userId)
            if (!isPositiveFlow && isFromPinpoint) {
                Intent(context, AddressFormActivity::class.java).apply {
                    putExtra(EXTRA_IS_POSITIVE_FLOW, false)
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, viewModel.getAddress())
                    putExtra(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
                    startActivityForResult(this, REQUEST_ADDRESS_FORM_PAGE)
                }
            } else {
                Intent(context, AddressFormActivity::class.java).apply {
                    putExtra(EXTRA_IS_POSITIVE_FLOW, true)
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, viewModel.getAddress())
                    startActivityForResult(this, REQUEST_ADDRESS_FORM_PAGE)
                }
            }
        }
    }

    private fun setSearchView() {
        binding.searchPageInput.searchBarTextField.run {
            setOnClickListener {
                AddNewAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
            }

            addTextChangedListener(object: TextWatcher {
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
    }


    private fun setViewListener() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        binding.rlSearchCurrentLocation.setOnClickListener {
            AddNewAddressRevampAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            if (AddNewAddressUtils.isGpsEnabled(context)) {
                if (allPermissionsGranted()) {
                    hasRequestedLocation = true
                    getLocation()
                } else {
                    hasRequestedLocation = false
                    requestPermissionLocation()
                }
            } else {
                requestPermissionLocation()
            }
        }
    }

    private fun showBottomSheetLocUndefined(isDontAskAgain: Boolean){
        isPermissionAccessed = true
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding = BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener {
                isPermissionAccessed = false
                AddNewAddressRevampAnalytics.onClickXOnBlockGpsSearch(userSession.userId)
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                isPermissionAccessed = false
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined?.show(it, "")
        }
    }

    private fun setupBottomSheetLocUndefined(viewBinding: BottomsheetLocationUndefinedBinding, isDontAskAgain: Boolean) {
        viewBinding.run {
            imgLocUndefined.setImageUrl(LOCATION_NOT_FOUND)
            tvLocUndefined.text = getString(R.string.txt_location_not_detected)
            tvInfoLocUndefined.text = getString(R.string.txt_info_location_not_detected)
            btnActivateLocation.setOnClickListener {
                AddNewAddressRevampAnalytics.onClickAktifkanLayananLokasiSearch(userSession.userId)
                if (!isDontAskAgain) {
                    goToSettingLocationDevice()
                } else {
                    goToSettingLocationApps()
                }

            }
        }
    }

    private fun goToSettingLocationDevice() {
        if (context?.let { turnGPSOn(it) } == false) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun goToSettingLocationApps() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        activity?.startActivityForResult(intent, RESULT_PERMISSION_CODE)
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
                isPermissionAccessed = false
                if (data != null) {
                   goToPinpointPage(null, data.latitude, data.longitude, false, true)
                } else {
                    fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
                        createLocationCallback(), null)
                }

            }
        } else {
            showBottomSheetLocUndefined(false)
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
        AddNewAddressRevampAnalytics.onClickDropdownSuggestion(userSession.userId)
        if (!isPositiveFlow && isFromPinpoint) goToPinpointPage(placeId, null, null, true, false)
        else goToPinpointPage(placeId, null, null, false, true)
    }

    private fun goToPinpointPage(placeId: String?, latitude: Double?, longitude: Double?, isFromAddressForm: Boolean, isPositiveFlow: Boolean) {
        val bundle = Bundle()
        bundle.putString(EXTRA_PLACE_ID, placeId)
        latitude?.let { bundle.putDouble(EXTRA_LATITUDE, it) }
        longitude?.let { bundle.putDouble(EXTRA_LONGITUDE, it) }
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
        bundle.putBoolean(EXTRA_IS_POLYGON, isPolygon)
        distrcitId?.let { bundle.putInt(EXTRA_DISTRICT_ID, it) }
        startActivityForResult(context?.let { PinpointNewPageActivity.createIntent(it, bundle) }, REQUEST_PINPOINT_PAGE)
    }

    companion object {
        private const val RESULT_PERMISSION_CODE = 1234

        private const val REQUEST_ADDRESS_FORM_PAGE = 1599
        private const val REQUEST_PINPOINT_PAGE = 1998

        fun newInstance(bundle: Bundle): SearchPageFragment {
            return SearchPageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_KOTA_KECAMATAN, bundle.getString(EXTRA_KOTA_KECAMATAN))
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, bundle.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, bundle.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putBoolean(EXTRA_FROM_PINPOINT, bundle.getBoolean(EXTRA_FROM_PINPOINT))
                    putBoolean(EXTRA_IS_POLYGON, bundle.getBoolean(EXTRA_IS_POLYGON))
                    putInt(EXTRA_DISTRICT_ID, bundle.getInt(EXTRA_DISTRICT_ID))
                }
            }
        }
    }
}