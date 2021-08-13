package com.tokopedia.editshipping.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.analytics.EditShippingAnalytics
import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.model.editshipping.Courier
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping
import com.tokopedia.editshipping.domain.model.openshopshipping.OpenShopData
import com.tokopedia.editshipping.presenter.EditShippingPresenter
import com.tokopedia.editshipping.presenter.EditShippingPresenterImpl
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.ADDITIONAL_OPTION_REQUEST_CODE
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.CREATE_SHOP_PAGE
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.CURRENT_COURIER_MODEL
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.CURRENT_OPEN_SHOP_MODEL
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.EDIT_SHIPPING_DATA
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.EDIT_SHIPPING_RESULT_KEY
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.MAP_MODE
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.MODIFIED_COURIER_INDEX_KEY
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.OPEN_MAP_CODE
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.RESUME_OPEN_SHOP_DATA_KEY
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.SETTING_PAGE
import com.tokopedia.editshipping.ui.customview.CourierView
import com.tokopedia.editshipping.ui.customview.ShippingAddressLayout
import com.tokopedia.editshipping.ui.customview.ShippingHeaderLayout
import com.tokopedia.editshipping.ui.customview.ShippingInfoBottomSheet
import com.tokopedia.editshipping.util.EditShippingConstant.ARGUMENT_DATA_TOKEN
import com.tokopedia.editshipping.util.EditShippingConstant.LABEL_VALIDATION_BO
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.shopsettings.shipping.data.EditShippingUrl
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.popup_validation_bo.view.*
import timber.log.Timber

/**
 * Created by Kris on 2/19/2016.
 * TOKOPEDIA
 */
class EditShippingFragment : Fragment(), EditShippingViewListener {
    private val RESULT_INTENT_DISTRICT_RECOMMENDATION = "district_recommendation_address"
    var fragmentShipingMainLayout: LinearLayout? = null
    var fragmentShippingHeader: ShippingHeaderLayout? = null
    var addressLayout: ShippingAddressLayout? = null
    var submitButtonCreateShop: TextView? = null
    private var editShippingPresenter: EditShippingPresenter? = null
    private var mainProgressDialog: ProgressDialog? = null
    private var progressDialog: ProgressDialog? = null
    private var inputMethodManager: InputMethodManager? = null
    private var userSession: UserSessionInterface? = null
    private var bottomSheetValidation: BottomSheetUnify? = null
    private var mapMode = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_shop_shipping, container, false)
        initiateVariables(mainView)
        hideAllView()
        setHasOptionsMenu(isEditShipping)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentShippingHeader?.setViewListener(this)
        addressLayout?.setViewListener(this)
        fragmentShippingHeader?.setListener(editShippingPresenter)
        addressLayout?.setListener(editShippingPresenter)
        if (arguments?.containsKey(RESUME_OPEN_SHOP_DATA_KEY) == true) {
            editShippingPresenter?.setSavedInstance(arguments)
        } else {
            editShippingPresenter?.setSavedInstance(savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshView()
        NetworkErrorHelper.removeEmptyState(view)
        if (editShippingPresenter?.openShopModel != null) {
            editShippingPresenter?.bindDataToViewOpenShop(editShippingPresenter?.openShopModel)
        } else if (editShippingPresenter?.shopModel != null) {
            editShippingPresenter?.bindDataToView(editShippingPresenter?.shopModel)
        } else data
    }

    override fun onPause() {
        super.onPause()
        if (bottomSheetValidation?.isVisible == true) {
            bottomSheetValidation?.dismiss()
        }
    }

    private fun hideAllView() {
        fragmentShippingHeader?.visibility = View.GONE
        addressLayout?.visibility = View.GONE
        fragmentShipingMainLayout?.visibility = View.GONE
    }

    private val data: Unit
        private get() {
            if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
                shippingDataCreateShop
            } else {
                shippingData
            }
        }

    private fun initiateVariables(mainView: View) {
        mapMode = arguments?.getInt(MAP_MODE) ?: 0
        mainProgressDialog = ProgressDialog(activity)
        mainProgressDialog?.cancel()
        progressDialog = ProgressDialog(activity)
        editShippingPresenter = EditShippingPresenterImpl(this)
        inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        userSession = UserSession(activity)
        fragmentShipingMainLayout = mainView.findViewById<View>(R.id.fragment_shipping_main_layout) as LinearLayout
        fragmentShippingHeader = mainView.findViewById<View>(R.id.fragment_shipping_header) as  com.tokopedia.editshipping.ui.customview.ShippingHeaderLayout
        addressLayout = mainView.findViewById<View>(R.id.shipping_address_layout) as com.tokopedia.editshipping.ui.customview.ShippingAddressLayout
        submitButtonCreateShop = mainView.findViewById<View>(R.id.submit_button_create_shop) as TextView
        submitButtonCreateShop?.setOnClickListener { submitButtonOnClickListener() }
    }

    private val isEditShipping: Boolean
        private get() = arguments?.getInt(MAP_MODE) == SETTING_PAGE

    private val shippingData: Unit
        private get() {
            mainProgressDialog?.show()
            editShippingPresenter?.fetchData()
        }

    private val shippingDataCreateShop: Unit
        private get() {
            editShippingPresenter?.fetchDataOpenShop()
            fragmentShippingHeader?.visibility = View.GONE
            mainProgressDialog?.show()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        editShippingPresenter?.onViewDestroyed()
    }

    override val mainContext: Context?
        get() = activity

    override fun addCourier(courier: Courier?, courierIndex: Int) {
        val courierView = CourierView(activity)
        fragmentShipingMainLayout?.addView(courierView)
        courierView.setViewListener(this)
        courier?.let { courierView.renderData(it, courierIndex) }
    }

    override fun setShopDetailedInformation(data: ShopShipping?) {
        if (data != null) {
            addressLayout?.renderData(data)
        }
    }

    override fun setGeoAddress(address: String?) {
        addressLayout?.renderGeoAddress(address)
    }

    override fun setShopLocationData(shopData: ShopShipping?) {
        if (shopData != null) {
            fragmentShippingHeader?.renderData(shopData)
        }
    }

    override fun validateShowPopup(data: ValidateShippingModel?) {
        if (data != null) {
            if (data.data.showPopup) {
                data.let { openPopupValidation(it) }
            } else {
                submitData()
            }
        }
    }

    override val districtAndCity: String?
        get() = fragmentShippingHeader?.districtAndCity

    override val zipCode: String?
        get() = fragmentShippingHeader?.zipCodeData

    override val streetAddress: String?
        get() = addressLayout?.addressData

    override fun zipCodeEmpty() {
        fragmentShippingHeader?.setZipCodeError(activity?.getString(R.string.error_field_required))
    }

    override fun noServiceChosen() {
        NetworkErrorHelper.showSnackbar(activity, activity
                ?.getString(R.string.error_shipping_must_choose))
    }

    override fun finishLoading() {
        progressDialog?.dismiss()
    }

    override fun refreshData(messageStatus: String?) {
        onShowViewAfterLoading()
        Toast.makeText(activity, messageStatus, Toast.LENGTH_LONG).show()
    }

    override fun finishStartingFragment() {
        mainProgressDialog?.dismiss()
    }

    override fun setLocationProvinceCityDistrict(Province: String?, City: String?, District: String?) {
        fragmentShippingHeader?.updateLocationData(Province, City, District)
    }

    override fun setLocationProvinceCityDistrict() {
        fragmentShippingHeader?.updateLocationData(getString(R.string.hint_choose_city))
    }

    override fun initializeZipCodes() {
        fragmentShippingHeader?.initializeZipCodes()
    }

    override fun locationDialogTimeoutListener() {
        progressDialog?.dismiss()
    }

    override fun dismissFragment(messageStatus: String?) {
        Toast.makeText(activity, messageStatus, Toast.LENGTH_LONG).show()
        activity?.finish()
    }

    override fun openWebView(webResources: String?, courierIndex: Int) {
        progressDialog?.dismiss()
        val dialog: EditShippingWebViewDialog = EditShippingWebViewDialog
                .openAdditionalOptionDialog(webResources, courierIndex)
        if (fragmentManager?.findFragmentByTag("web_view_dialog") == null) {
            dialog.setTargetFragment(this, ADDITIONAL_OPTION_REQUEST_CODE)
            fragmentManager?.let { dialog.show(it, "web_view_dialog") }
        }
    }

    override fun showErrorToast(error: String?) {
        if(activity != null) {
            progressDialog?.dismiss()
            Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFragmentTimeout() {
        NetworkErrorHelper.showEmptyState(activity, view, object : NetworkErrorHelper.RetryClickedListener {
            override fun onRetryClicked() {
                mainProgressDialog?.show()
                if (mapMode == CREATE_SHOP_PAGE) editShippingPresenter?.fetchDataOpenShop() else editShippingPresenter?.fetchData()
            }
        })
    }

    override fun onFragmentNoConnection() {
        NetworkErrorHelper.showEmptyState(activity, view,
                getString(com.tokopedia.abstraction.R.string.msg_no_connection),
                object : NetworkErrorHelper.RetryClickedListener {
                    override fun onRetryClicked() {
                        if (mapMode == CREATE_SHOP_PAGE) editShippingPresenter?.fetchDataOpenShop() else editShippingPresenter?.fetchData()
                    }
                })
        activity?.invalidateOptionsMenu()
    }

    override fun onShowViewAfterLoading() {
        activity?.invalidateOptionsMenu()
        fragmentShippingHeader?.visibility = View.VISIBLE
        addressLayout?.visibility = View.VISIBLE
        fragmentShipingMainLayout?.visibility = View.VISIBLE
        if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE) submitButtonCreateShop?.visibility = View.VISIBLE else if (arguments?.containsKey(RESUME_OPEN_SHOP_DATA_KEY) == true) {
            submitButtonCreateShop?.visibility = View.VISIBLE
        } else submitButtonCreateShop?.visibility = View.GONE
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun openDataWebViewResources(courierIndex: Int) {
        editShippingPresenter?.dataWebViewResource(courierIndex,
                editShippingPresenter?.getCourierAdditionalOptionsURL(courierIndex))
    }

    override fun setServiceCondition(isChecked: Boolean, serviceIndex: Int, courierIndex: Int) {
        editShippingPresenter?.setServiceCondition(isChecked, serviceIndex, courierIndex)
    }

    override fun editAddress() {
        val token = editShippingPresenter?.token
        val intent = activity?.let { getDistrictRecommendationIntent(it, token) }
        startActivityForResult(intent, GET_DISTRICT_RECCOMENDATION_REQUEST_CODE)
    }

    private fun editShippingValid(): Boolean {
        return true
    }

    private val currentShippingConfiguration: OpenShopData?
        get() = editShippingPresenter?.passShippingData()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_DISTRICT_RECCOMENDATION_REQUEST_CODE -> {
                    val address: DistrictRecommendationAddress? = data?.getParcelableExtra(RESULT_INTENT_DISTRICT_RECOMMENDATION)
                    editShippingPresenter?.setSelectedAddress(address)
                    fragmentShippingHeader?.initializeZipCodes()
                    fragmentShippingHeader?.updateLocationData(
                            address?.provinceName,
                            address?.cityName,
                            address?.districtName)
                    changeLocationRequest(address?.districtId)
                }
                OPEN_MAP_CODE -> changeGoogleMapData(data)
                ADDITIONAL_OPTION_REQUEST_CODE -> {
                    additionalOptionRequest(data)
                    inputMethodManager?.hideSoftInputFromWindow(fragmentShipingMainLayout
                            ?.windowToken, 0)
                }
            }
        }
    }

    private fun changeLocationRequest(originId: Int?) {
        progressDialog?.show()
        if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE || arguments?.containsKey(RESUME_OPEN_SHOP_DATA_KEY) == true) {
            editShippingPresenter?.fetchDataByLocationOpenShop(originId.toString())
        } else {
            editShippingPresenter?.fetchDataByLocation(originId.toString())
        }
    }

    private fun changeGoogleMapData(data: Intent?) {
        addressLayout?.setGoogleMapData(data)
    }

    private fun additionalOptionRequest(data: Intent?) {
        editShippingPresenter?.setCourierAdditionalOptionConfig(
                data?.getIntExtra(MODIFIED_COURIER_INDEX_KEY, 0),
                data?.getStringExtra(EDIT_SHIPPING_RESULT_KEY))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (isAdded && activity != null) {
            activity?.menuInflater?.inflate(R.menu.save_btn_black, menu)
            val item = menu.findItem(R.id.action_send)
            item.title = getString(R.string.title_action_save_shipping)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_send)
        val fragmentHeader = fragmentShippingHeader?.isShown
        if (fragmentHeader != null) {
            item.isVisible = fragmentHeader
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragmentHeader = fragmentShippingHeader?.isShown
        if (item.itemId == R.id.action_send) {
            if (fragmentHeader != null) {
                editShippingPresenter?.getShopId()?.let { editShippingPresenter?.validateBo(it, editShippingPresenter?.getCompiledShippingId().toString()) }
            } else showErrorToast(getString(R.string.dialog_on_process))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitData() {
        editShippingPresenter?.submitValue()
    }

    private fun refreshView() {
        fragmentShipingMainLayout?.removeAllViews()
        editShippingPresenter?.refreshData()
    }

    fun submitButtonOnClickListener() {
        if (fragmentShipingMainLayout?.childCount == 0 && this.editShippingValid()) {
            val intent = Intent()
            intent.putExtra(EDIT_SHIPPING_DATA, currentShippingConfiguration)
            activity?.setResult(OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE, intent)
            activity?.finish()
        } else if (fragmentShipingMainLayout?.childCount == 0) {
            showErrorToast(activity?.getString(R.string.title_select_shop_location))
            EditShippingAnalytics.eventCreateShopFillLogisticError()
        }
    }

    override fun openGeoLocation() {
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(activity)
        if (ConnectionResult.SUCCESS == resultCode) {
            val locationPass = LocationPass()
            if (editShippingPresenter?.shopInformation?.shopLatitude?.isNotEmpty()!!
                    && editShippingPresenter?.shopInformation?.shopLongitude?.isNotEmpty()!!) {
                locationPass.latitude = editShippingPresenter?.shopInformation?.shopLatitude
                locationPass.longitude = editShippingPresenter?.shopInformation?.shopLongitude
                locationPass.generatedAddress = addressLayout?.googleMapAddressString
            } else {
                locationPass.districtName = editShippingPresenter?.shopInformation?.getDistrictName()
                locationPass.cityName = editShippingPresenter?.shopInformation?.getCityName()
            }
            val intent = activity?.let { getGeoLocationActivityIntent(it, locationPass) }
            startActivityForResult(intent, OPEN_MAP_CODE)
        } else {
            Timber.d("Google play services unavailable")
            val dialog = availability.getErrorDialog(activity, resultCode, 0)
            dialog.show()
        }
    }

    override fun showInfoBottomSheet(information: String?, serviceName: String?) {
        ShippingInfoBottomSheet(information, serviceName, activity)
    }

    override fun refreshLocationViewListener(updatedShopInfo: ShopShipping?) {
        refreshView()
        fragmentShippingHeader?.updateLocationData(updatedShopInfo?.provinceName,
                updatedShopInfo?.cityName,
                updatedShopInfo?.districtName)
    }

    override fun refreshLocationViewListener(address: DistrictRecommendationAddress?) {
        refreshView()
        
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
            editShippingPresenter?.saveOpenShopModel()
            outState?.putParcelable(CURRENT_OPEN_SHOP_MODEL,
                    editShippingPresenter?.openShopModel)
        } else {
            outState?.putParcelable(CURRENT_COURIER_MODEL,
                    editShippingPresenter?.shopModel)
        }
    }

    private fun getDistrictRecommendationIntent(activity: Activity, token: Token?): Intent? {
        val  intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS)
        intent.putExtra(ARGUMENT_DATA_TOKEN, token)
        return intent
    }

    private fun openPopupValidation(data: ValidateShippingModel) {
        bottomSheetValidation = BottomSheetUnify()
        val viewBottomSheetValidation = View.inflate(activity, R.layout.popup_validation_bo, null).apply {

            ticker_validation_bo.tickerTitle = HtmlLinkHelper(context, data.data.tickerTitle).spannedString.toString()
            ticker_validation_bo.setHtmlDescription(data.data.tickerContent)
            ticker_validation_bo.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    val url = data.data.tickerContent.substringAfter("<a href='").substringBefore("'>di sini</a>")
                    val intent = RouteManager.getIntent(context, String.format("%s?titlebar=false&url=%s", ApplinkConst.WEBVIEW, url))
                    startActivity(intent)
                }

                override fun onDismiss() {
                    //no-op
                }
            })

            point_one.text = HtmlLinkHelper(context, data.data.popupContent[0]).spannedString
            point_two.text = HtmlLinkHelper(context, data.data.popupContent[1]).spannedString
            point_three.text = HtmlLinkHelper(context, data.data.popupContent[2]).spannedString

            btn_nonaktifkan.setOnClickListener {
                submitData()
                bottomSheetValidation?.dismiss()
            }
            btn_aktifkan.setOnClickListener {
               bottomSheetValidation?.dismiss()
            }
        }

        bottomSheetValidation?.apply {
            setTitle(LABEL_VALIDATION_BO)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetValidation)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetValidation?.show(it, "show")
        }
    }

    private fun getGeoLocationActivityIntent(activity: Activity, locationPass: LocationPass): Intent? {
        val intent  = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        intent.apply {
            putExtra(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            putExtra(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, false)
        }
        return intent
    }

    companion object {
        private const val GET_DISTRICT_RECCOMENDATION_REQUEST_CODE = 100
        fun newInstance(): EditShippingFragment {
            val fragment = EditShippingFragment()
            val bundle = Bundle()
            bundle.putInt(MAP_MODE, SETTING_PAGE)
            fragment.arguments = bundle
            return fragment
        }

        fun createShopInstance(): EditShippingFragment {
            val fragment = EditShippingFragment()
            val bundle = Bundle()
            bundle.putInt(MAP_MODE, CREATE_SHOP_PAGE)
            fragment.arguments = bundle
            return fragment
        }

        fun resumeShopInstance(previousOpenShopState: Parcelable?): EditShippingFragment {
            val fragment = EditShippingFragment()
            val bundle = Bundle()
            bundle.putParcelable(RESUME_OPEN_SHOP_DATA_KEY, previousOpenShopState)
            fragment.arguments = bundle
            return fragment
        }
    }
}