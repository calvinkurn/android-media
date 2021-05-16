package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.util.rxEditText
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.FragmentSearchAddressBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoCleared
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class SearchPageFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)
    }

    private val compositeSubs: CompositeSubscription by lazy { CompositeSubscription() }

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)

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
        showInitialLoadMessage()
        setSearchView()
        setViewListener()
        initObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubs.clear()
    }

    private fun showInitialLoadMessage() {
        binding.tvMessageSearch.text = getString(R.string.txt_message_initial_load)
        binding.tvMessageSearch.setOnClickListener {
            //go to ANA form negative
        }
    }

    private fun setSearchView() {
        binding.searchPageInput.searchBarPlaceholder = getString(R.string.txt_hint_search)
        binding.searchPageInput.searchBarTextField.run {
            setOnClickListener {
                binding.searchPageInput.searchBarTextField.isCursorVisible = true
                openSoftKeyboard()

            }

            rxEditText(this).subscribe(object: Subscriber<String>() {
                override fun onNext(t: String) {
                    if (t.isNotEmpty()) {
                        loadAutoComplete(t)
                    }
                }

                override fun onCompleted() {
                    //no-op
                }

                override fun onError(e: Throwable?) {
                    //no-op
                }

            }).toCompositeSubs(compositeSubs)
        }

    }

    private fun openSoftKeyboard() {
        binding.searchPageInput.searchBarTextField.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

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
        //load list here
    }

    private fun hideListLocation() {
        //hide list here
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
                    //sned to maps
                } else {
                    fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
                        createLocationCallback(), null)
                }

            }
        } else {
            //bottomsheet blm aktifin GPS
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

    companion object {
        fun newInstance(bundle: Bundle): SearchPageFragment {
            return SearchPageFragment().apply {
                arguments = bundle
            }
        }
    }
}