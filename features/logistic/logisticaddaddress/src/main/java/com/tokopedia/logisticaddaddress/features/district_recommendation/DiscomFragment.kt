package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.toAddressUiStateOrNull
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.databinding.FragmentDistrictRecommendationBinding
import com.tokopedia.logisticaddaddress.di.districtrecommendation.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.mapper.AddressMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity.Companion.IS_LOCALIZATION
import com.tokopedia.logisticaddaddress.features.district_recommendation.uimodel.DiscomSource
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DiscomFragment :
    DiscomBottomSheetRevamp.DiscomRevampListener,
    BaseDaggerFragment() {
    companion object {

        const val ARGUMENT_DATA_TOKEN = "token"
        const val ARGUMENT_IS_PINPOINT = "is_pinpoint"
        const val ARGUMENT_ADDRESS_STATE = "address_state"
        const val DEBOUNCE_DELAY_IN_MILIS: Long = 700

        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID = "district_id"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME = "district_name"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID = "city_id"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME = "city_name"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID = "province_id"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME = "province_name"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES = "zipcodes"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE = "latitude"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE = "longitude"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODE = "zipcode"
        const val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_IS_PINPOINT = "pinpoint"

        @JvmStatic
        fun newInstance(isLocalization: Boolean): DiscomFragment = DiscomFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_LOCALIZATION, isLocalization)
            }
        }

        @JvmStatic
        fun newInstance(token: Token, isLocalization: Boolean): DiscomFragment =
            DiscomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGUMENT_DATA_TOKEN, token)
                    putBoolean(IS_LOCALIZATION, isLocalization)
                }
            }

        fun newInstance(isPinpoint: Boolean, addressState: String, isLocalization: Boolean): DiscomFragment {
            return DiscomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_ADDRESS_STATE, addressState)
                    putBoolean(ARGUMENT_IS_PINPOINT, isPinpoint)
                    putBoolean(IS_LOCALIZATION, isLocalization)
                }
            }
        }
    }

    private var source: DiscomSource = DiscomSource.ShopAddress
    private var isGmsAvailable: Boolean = false

    private var bottomSheet: DiscomBottomSheetRevamp? = null

    private var binding by autoClearedNullable<FragmentDistrictRecommendationBinding>()

    @Inject
    lateinit var addressMapper: AddressMapper
    override fun getScreenName(): String? = null

    override fun initInjector() {
        val districtRecommendationComponent = DaggerDistrictRecommendationComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        districtRecommendationComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val isPinpoint = it.getBoolean(ARGUMENT_IS_PINPOINT, false)
            val addressState = it.getString(ARGUMENT_ADDRESS_STATE).toAddressUiStateOrNull()
            val isLocalization = it.getBoolean(IS_LOCALIZATION)
            source = getDiscomSource(isPinpoint, addressState, isLocalization)
        }
        checkLocationAvailability()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDistrictRecommendationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openBottomSheet()
        setOnBackPressed()
    }

    override fun onGetDistrict(districtAddress: Address) {
        val arrayListZipCodes = arrayListOf<String>()
        arrayListZipCodes.addAll(districtAddress.zipCodes)
        activity?.let {
            val resultIntent = Intent().apply {
                putExtra(
                    DiscomActivity.INTENT_DISTRICT_RECOMMENDATION_ADDRESS,
                    addressMapper.convertAddress(districtAddress)
                )
                putExtra(
                    INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID,
                    districtAddress.districtId
                )
                putExtra(
                    INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME,
                    districtAddress.districtName
                )
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, districtAddress.cityId)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, "")
                putExtra(
                    INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID,
                    districtAddress.provinceId
                )
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, "")
                putStringArrayListExtra(
                    INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES,
                    arrayListZipCodes
                )
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LATITUDE, districtAddress.lat)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_LONGITUDE, districtAddress.long)
            }
            it.setResult(Activity.RESULT_OK, resultIntent)
            it.finish()
        }
    }

    override fun onChooseZipcode(districtAddress: Address, zipCode: String, isPinpoint: Boolean) {
        activity?.let {
            val resultIntent = Intent().apply {
                putExtra(
                    DiscomActivity.INTENT_DISTRICT_RECOMMENDATION_ADDRESS,
                    districtAddress
                )
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODE, zipCode)
                putExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_IS_PINPOINT, isPinpoint)
            }
            it.setResult(Activity.RESULT_OK, resultIntent)
            it.finish()
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    bottomSheet?.dismiss()
                    activity?.finish()
                }
            }
        )
    }

    private fun openBottomSheet() {
        bottomSheet = DiscomBottomSheetRevamp.show(
            activity?.supportFragmentManager,
            this,
            source,
            isGmsAvailable
        )
    }

    private fun checkLocationAvailability() {
        context?.let {
            isGmsAvailable = MapsAvailabilityHelper.isMapsAvailable(it)
        }
    }

    private fun getDiscomSource(
        isPinpoint: Boolean,
        addressState: AddressUiState?,
        isLocalization: Boolean
    ): DiscomSource {
        return if (isLocalization) {
            DiscomSource.LCA
        } else if (addressState != null) {
            DiscomSource.UserAddress(addressState, isPinpoint)
        } else {
            DiscomSource.ShopAddress
        }
    }
}
