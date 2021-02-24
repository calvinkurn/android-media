package com.tokopedia.localizationchooseaddress.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChooseAddressWidget: ConstraintLayout, ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var textChosenAddress: Typography? = null
    private var buttonChooseAddress: IconUnify? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ChooseAddressViewModel

    private var chooseAddressWidgetListener: ChooseAddressWidgetListener? = null

    init {
        View.inflate(context, R.layout.choose_address_widget, this)
        initInjector()
        initViews()
    }

    private fun initViews() {
        chooseAddressPref = ChooseAddressSharePref(context)

        textChosenAddress = findViewById(R.id.text_chosen_address)
        buttonChooseAddress = findViewById(R.id.btn_arrow)

        checkRollence()
    }

    private fun initInjector() {
        getComponent().inject(this)
    }

    private fun initObservers() {
        /**
         * Test only, gql not ready yet from BE side
         * Hit viewModel on the first start
         */
        viewModel.test.observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                }
            }

        })

        viewModel.getChosenAddress.observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.addressId == 0) {
                        val source = chooseAddressWidgetListener?.getLocalizingAddressHostSourceData()
                        source?.let { it -> viewModel.getDefaultChosenAddress("", it) }
                    } else {
                        val data = it.data
                        val localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = data.addressId.toString(),
                                cityId = data.cityId.toString(),
                                districtId = data.districtId.toString(),
                                lat = data.latitude,
                                long = data.longitude,
                                label = data.addressName,
                                postalCode = data.postalCode
                        )
                        chooseAddressPref?.setLocalCache(localData)
                        chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                    }
                }
            }
        })

        viewModel.getDefaultAddress.observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.addressData.addressId == 0) {
                        val data = ChooseAddressConstant.defaultAddress
                        viewModel.setStateChosenAddress(2, data.address_id, "", "", data.lat, data.long, data.district_id, "")
                    } else {
                        val data = it.data.addressData
                        val localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = data.addressId.toString(),
                                cityId = data.cityId.toString(),
                                districtId = data.districtId.toString(),
                                lat = data.latitude,
                                long = data.longitude,
                                label = data.addressName,
                                postalCode = data.postalCode.toString()
                        )
                        chooseAddressPref?.setLocalCache(localData)
                        chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                    }
                }
            }
        })

        viewModel.setChosenAddress.observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val data = it.data
                    val localData = ChooseAddressUtils.setLocalizingAddressData(
                            addressId = data.addressId.toString(),
                            cityId = data.cityId.toString(),
                            districtId = data.districtId.toString(),
                            lat = data.latitude,
                            long = data.longitude,
                            label = data.addressName,
                            postalCode = data.postalCode.toString()
                    )
                    chooseAddressPref?.setLocalCache(localData)
                    chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                }
            }
        })
    }

    private fun checkRollence(){
        val value = chooseAddressPref?.getRollenceValue()
        value?.let { chooseAddressWidgetListener?.onLocalizingAddressRollOutUser(it) }
    }

    fun updateWidget(){
        val data = chooseAddressPref?.getLocalCacheData()
        val label = context.getString(R.string.txt_send_to, data?.label)
        textChosenAddress?.text = HtmlLinkHelper(context, label).spannedString
    }

    private fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
                .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun initChooseAddressFlow() {
        val localData = ChooseAddressUtils.getLocalizingAddressData(context)
        localData?.let { chooseAddressPref?.setLocalCache(it) }
        if (localData?.city_id?.isEmpty() == true) {
            textChosenAddress?.text = context.getString(R.string.txt_label_default)
            chooseAddressWidgetListener?.getLocalizingAddressHostSourceData()?.let { viewModel.getStateChosenAddress(it) }
        } else {
            updateWidget()
        }
    }

    fun bindChooseAddress(listener: ChooseAddressWidgetListener) {
        this.chooseAddressWidgetListener = listener
        val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
        if (fragment != null) {
            viewModel = ViewModelProviders.of(fragment, viewModelFactory)[ChooseAddressViewModel::class.java]
        }

        initChooseAddressFlow()
        initObservers()

        buttonChooseAddress?.setOnClickListener {
            val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
            val chooseAddressBottomSheet = ChooseAddressBottomSheet()
            chooseAddressBottomSheet.setListener(this)
            chooseAddressBottomSheet.show(fragment?.childFragmentManager)
        }
    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressWidgetListener?.onLocalizingAddressServerDown()
    }

    override fun onAddressDataChanged() {
        chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromWidget()
    }

    override fun getLocalizingAddressHostSourceBottomSheet(): String {
        val source = chooseAddressWidgetListener?.getLocalizingAddressHostSourceData()
        return if (source?.isNotEmpty() == true) source
        else ""
    }

    override fun onLocalizingAddressLoginSuccessBottomSheet() {
        chooseAddressWidgetListener?.onLocalizingAddressLoginSuccess()
    }

    interface ChooseAddressWidgetListener {
        /**
         * Action choosen address from user by widget / bottomshet
         * Host must update content UI
         */
        fun onLocalizingAddressUpdatedFromWidget();

        /**
         * Address updated from background if device have not address saved in local cache.
         * this first user rollout
         * host can ignore this. optional to update UI
         */
        fun onLocalizingAddressUpdatedFromBackground();

        /**
         * this listen if we get server down on widget/bottomshet.
         * Host mandatory to GONE LocalizingAddressWidget
         */
        fun onLocalizingAddressServerDown();

        /**
         * this trigger to Host this feature active or not
         * Host must GONE widget if isRollOutUser == false
         * Host must VISIBLE widget if isRollOutUser == true
         */
        fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean)

        /**
         * We need Object Host Fragment to get viewmodel
         */
        fun getLocalizingAddressHostFragment(): Fragment

        /**
         * String Source of Host Page
         */
        fun getLocalizingAddressHostSourceData(): String

        /**
         * this listen is use to notify host/fragment if login is success
         * host/fragment need to refresh their page
         */
        fun onLocalizingAddressLoginSuccess()
    }

}