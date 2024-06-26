package com.tokopedia.localizationchooseaddress.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.localizationchooseaddress.R as localizationchooseaddressR
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressViewModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.ERROR_CODE_EMPTY_STATE_CHOSEN_ADDRESS
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.LCA_VERSION
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ChooseAddressWidget :
    ConstraintLayout,
    ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {

    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, type: Int) : super(context) {
        this.type = type
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initWithAttrs(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWithAttrs(attrs)
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: ChooseAddressViewModel

    private var chooseAddressWidgetListener: ChooseAddressWidgetListener? = null
    private var textChosenAddress: Typography? = null
    private var iconChooseAddress: IconUnify? = null
    private var iconChevronChooseAddress: IconUnify? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null
    private var hasClicked: Boolean? = false
    private var isSupportWarehouseLoc: Boolean = true
    private var type: Int = TYPE_NORMAL

    private fun initWithAttrs(attrs: AttributeSet?) {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, localizationchooseaddressR.styleable.ChooseAddressWidget)
        try {
            type = attributes.getInt(
                localizationchooseaddressR.styleable.ChooseAddressWidget_widgetType,
                TYPE_NORMAL
            )
        } finally {
            attributes.recycle()
        }
        init()
    }

    private fun init() {
        inflateView()
        initInjector()
        initViews()
    }

    private fun inflateView() {
        when(type) {
            TYPE_SIMPLIFIED -> View.inflate(context, localizationchooseaddressR.layout.choose_address_widget_simplified, this)
            else -> View.inflate(context, localizationchooseaddressR.layout.choose_address_widget, this)
        }
    }

    private fun initViews() {
        chooseAddressPref = ChooseAddressSharePref(context)

        textChosenAddress = findViewById(localizationchooseaddressR.id.text_chosen_address)
        iconChooseAddress = findViewById(localizationchooseaddressR.id.icon_location)

        iconChevronChooseAddress = if(type == TYPE_NORMAL) {
            findViewById(localizationchooseaddressR.id.btn_arrow)
        } else null

        getLocalizingAddressData()
    }

    private fun initInjector() {
        getComponent().inject(this)
    }

    private fun initChosenAddressObserver() {
        val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
        if (fragment != null) {
            viewModel.getChosenAddress.observe(fragment.viewLifecycleOwner, {
                when (it) {
                    is Success -> {
                        if (it.data.errorCode.code == ERROR_CODE_EMPTY_STATE_CHOSEN_ADDRESS) {
                            val data = it.data
                            val defaultAddress = ChooseAddressConstant.defaultAddress
                            val localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = defaultAddress.address_id,
                                cityId = defaultAddress.city_id,
                                districtId = defaultAddress.district_id,
                                lat = defaultAddress.lat,
                                long = defaultAddress.long,
                                label = defaultAddress.label,
                                postalCode = defaultAddress.postal_code,
                                shopId = data.tokonowModel.shopId.toString(),
                                warehouseId = data.tokonowModel.warehouseId.toString(),
                                warehouses = TokonowWarehouseMapper.mapWarehousesModelToLocal(data.tokonowModel.warehouses),
                                serviceType = data.tokonowModel.serviceType,
                                lastUpdate = data.tokonowModel.lastUpdate
                            )
                            if (viewModel.isFirstLoad && chooseAddressWidgetListener?.isNeedToRefreshTokonowData() == true && ChooseAddressUtils.isLocalizingTokonowHasUpdated(context, localData)) {
                                chooseAddressPref?.setLocalCache(localData)
                                chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                                // trigger home to refresh data
                                chooseAddressWidgetListener?.onTokonowDataRefreshed()
                            } else {
                                chooseAddressPref?.setLocalCache(localData)
                                chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                            }
                        } else {
                            val data = it.data
                            val localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = data.addressId.toString(),
                                cityId = data.cityId.toString(),
                                districtId = data.districtId.toString(),
                                lat = data.latitude,
                                long = data.longitude,
                                label = "${data.addressName} ${data.receiverName}",
                                postalCode = data.postalCode,
                                shopId = data.tokonowModel.shopId.toString(),
                                warehouseId = data.tokonowModel.warehouseId.toString(),
                                warehouses = TokonowWarehouseMapper.mapWarehousesModelToLocal(data.tokonowModel.warehouses),
                                serviceType = data.tokonowModel.serviceType,
                                lastUpdate = data.tokonowModel.lastUpdate
                            )
                            if (viewModel.isFirstLoad && chooseAddressWidgetListener?.isNeedToRefreshTokonowData() == true && ChooseAddressUtils.isLocalizingTokonowHasUpdated(context, localData)) {
                                chooseAddressPref?.setLocalCache(localData)
                                chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                                // trigger home to refresh data
                                chooseAddressWidgetListener?.onTokonowDataRefreshed()
                            } else {
                                chooseAddressPref?.setLocalCache(localData)
                                chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromBackground()
                            }
                        }
                    }
                    is Fail -> {
                        onLocalizingAddressError()
                    }
                }
                viewModel.isFirstLoad = false
            })
        }
    }

    private fun initRefreshTokonowObserver() {
        val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
        if (fragment != null) {
            viewModel.tokonowData.observe(fragment.viewLifecycleOwner, {
                when (it) {
                    is Success -> {
                        val data = it.data
                        val shouldRefresh = ChooseAddressUtils.refreshTokonowData(
                            context = context,
                            warehouses = TokonowWarehouseMapper.mapWarehouseItemToLocal(data.warehouses),
                            warehouseId = data.warehouseId,
                            serviceType = data.serviceType,
                            lastUpdate = data.lastUpdate,
                            shopId = data.shopId
                        )
                        // trigger home to refresh data
                        if (viewModel.isFirstLoad && shouldRefresh) {
                            chooseAddressWidgetListener?.onTokonowDataRefreshed()
                        }
                    }
                    is Fail -> {
                        // do nothing
                    }
                }
                viewModel.isFirstLoad = false
            })
        }
    }

    private fun getLocalizingAddressData() {
        val value = true
        value.let { chooseAddressWidgetListener?.onLocalizingAddressRollOutUser(it) }
    }

    fun updateWidget() {
        val textColor = chooseAddressWidgetListener?.onChangeTextColor()
        if (textColor != null) {
            val newColor = ContextCompat.getColor(context, textColor)
            textChosenAddress?.setTextColor(newColor)
            val iconColorID = chooseAddressWidgetListener?.iconLocationColor()
            val iconLocation = chooseAddressWidgetListener?.iconLocation()
            if (iconLocation != null && iconColorID != null) {
                val iconColor = ContextCompat.getColor(
                    context,
                    iconColorID
                )
                iconChooseAddress?.setImage(
                    iconLocation,
                    iconColor,
                    iconColor,
                    iconColor,
                    iconColor
                )
            } else {
                iconChooseAddress?.setImage(null, newColor, newColor, newColor, newColor)
            }
            iconChevronChooseAddress?.setImage(null, newColor, newColor, newColor, newColor)
        }
        val data = ChooseAddressUtils.getLocalizingAddressData(context)
        if (data?.city_id?.isEmpty() == true) {
            textChosenAddress?.text = data.label
        } else {
            val label = context.getString(localizationchooseaddressR.string.txt_send_to, data?.label)
            textChosenAddress?.text = HtmlLinkHelper(context, label).spannedString
        }
    }

    private fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    private fun initChooseAddressFlow() {
        val localData = ChooseAddressUtils.getLocalizingAddressData(context)
        updateWidget()
        if (viewModel.isFirstLoad) {
            if (localData.address_id.isEmpty() || localData.version != LCA_VERSION) {
                initChosenAddressObserver()
                chooseAddressWidgetListener?.getLocalizingAddressHostSourceData()
                    ?.let { viewModel.getStateChosenAddress(it, isSupportWarehouseLoc) }
            } else if (chooseAddressWidgetListener?.isNeedToRefreshTokonowData() == true) {
                initRefreshTokonowObserver()
                viewModel.getTokonowData(localData)
            } else {
                viewModel.isFirstLoad = false
            }
        }
    }

    fun bindChooseAddress(listener: ChooseAddressWidgetListener) {
        this.chooseAddressWidgetListener = listener
        val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
        if (fragment != null) {
            viewModel = ViewModelProviders.of(
                fragment,
                viewModelFactory
            )[ChooseAddressViewModel::class.java]
        }

        isSupportWarehouseLoc = chooseAddressWidgetListener?.isSupportWarehouseLoc() ?: true
        initChooseAddressFlow()

        setOnClickListener {
            if (hasClicked == false) {
                val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
                val source = chooseAddressWidgetListener?.getLocalizingAddressHostSourceTrackingData()
                val eventLabel = chooseAddressWidgetListener?.getEventLabelHostPage()
                source?.let { _source ->
                    eventLabel?.let { _eventLabel ->
                        ChooseAddressTracking.onClickWidget(
                            _source,
                            userSession.userId,
                            _eventLabel
                        )
                    }
                }
                if (chooseAddressWidgetListener?.needToTrackTokoNow() == true) {
                    chooseAddressWidgetListener?.onClickChooseAddressTokoNowTracker()
                }
                val chooseAddressBottomSheet = ChooseAddressBottomSheet()
                chooseAddressBottomSheet.setListener(this)
                chooseAddressBottomSheet.show(fragment?.childFragmentManager)
                hasClicked = true
            }
        }
    }

    override fun onLocalizingAddressServerDown() {
        onLocalizingAddressError()
    }

    override fun onAddressDataChanged() {
        val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
        fragment?.view?.let {
            Toaster.build(
                it,
                context.getString(localizationchooseaddressR.string.toaster_success_chosen_address),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL
            ).show()
        }
        chooseAddressWidgetListener?.onLocalizingAddressUpdatedFromWidget()
    }

    override fun getLocalizingAddressHostSourceBottomSheet(): String {
        val source = chooseAddressWidgetListener?.getLocalizingAddressHostSourceData()
        return if (source?.isNotEmpty() == true) {
            source
        } else {
            ""
        }
    }

    override fun onLocalizingAddressLoginSuccessBottomSheet() {
        chooseAddressWidgetListener?.onLocalizingAddressLoginSuccess()
    }

    override fun onDismissChooseAddressBottomSheet() {
        hasClicked = false
    }

    override fun isSupportWarehouseLoc(): Boolean {
        return chooseAddressWidgetListener?.isSupportWarehouseLoc() ?: false
    }

    override fun isFromTokonowPage(): Boolean {
        return chooseAddressWidgetListener?.isFromTokonowPage() ?: false
    }

    private fun onLocalizingAddressError() {
        val fragment = chooseAddressWidgetListener?.getLocalizingAddressHostFragment()
        fragment?.view?.let {
            Toaster.build(
                it,
                context.getString(localizationchooseaddressR.string.toaster_failed_chosen_address),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
        chooseAddressWidgetListener?.onLocalizingAddressServerDown()
    }

    interface ChooseAddressWidgetListener {
        /**
         * Action choosen address from user by widget / bottomshet
         * Host must update content UI
         */
        fun onLocalizingAddressUpdatedFromWidget()

        /**
         * Address updated from background if device have not address saved in local cache.
         * this first user rollout
         * host can ignore this. optional to update UI
         */
        fun onLocalizingAddressUpdatedFromBackground()

        /**
         * this listen if we get server down on widget/bottomshet.
         * Host mandatory to GONE LocalizingAddressWidget
         */
        fun onLocalizingAddressServerDown()

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
         * String Tracking Source of Host Page
         * By default, this method will return String Source from getLocalizingAddressHostSourceData
         */
        fun getLocalizingAddressHostSourceTrackingData(): String {
            return getLocalizingAddressHostSourceData()
        }

        /**
         * this listen is use to notify host/fragment if login is success
         * host/fragment need to refresh their page
         */
        fun onLocalizingAddressLoginSuccess()

        /**
         * String Event Label of Host Page
         * By default, this method will return String empty
         */
        fun getEventLabelHostPage(): String {
            return ""
        }

        /**
         * To differentiate page that needs to track TokoNow or not
         * By default, this method will return false
         */
        fun needToTrackTokoNow(): Boolean {
            return false
        }

        /**
         * custom tracker for choose address widget of TokoNow page
         * By default, this method will be empty
         */
        fun onClickChooseAddressTokoNowTracker() {
        }

        /**
         * Int Color for Text label
         */
        fun onChangeTextColor(): Int {
            return unifyprinciplesR.color.Unify_NN950_96
        }

        /**
         * To differentiate feature that need warehouse loc or not
         */
        fun isSupportWarehouseLoc(): Boolean {
            return true
        }

        /**
         * To differentiate feature that need to refresh tokonow data or not
         */
        fun isNeedToRefreshTokonowData(): Boolean {
            return false
        }

        /**
         * To trigger UI refresh after getting new tokonow warehouse data
         */
        fun onTokonowDataRefreshed() {}

        /**
         * To check from tokonow page or not
         */
        fun isFromTokonowPage(): Boolean {
            return false
        }

        /**
         * To set icon location
         */
        fun iconLocation(): Int {
            return IconUnify.LOCATION
        }

        /**
         * To set icon location color
         */
        fun iconLocationColor(): Int {
            return onChangeTextColor()
        }
    }

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_SIMPLIFIED = 1
    }
}
