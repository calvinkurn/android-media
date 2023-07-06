package com.tokopedia.editshipping.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.analytics.EditShippingAnalytics
import com.tokopedia.editshipping.data.preference.WhitelabelInstanCoachMarkSharePref
import com.tokopedia.editshipping.databinding.FragmentShopShippingBinding
import com.tokopedia.editshipping.databinding.PopupValidationBoBinding
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
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.RESUME_OPEN_SHOP_DATA_KEY
import com.tokopedia.editshipping.ui.EditShippingViewListener.Companion.SETTING_PAGE
import com.tokopedia.editshipping.ui.customview.CourierView
import com.tokopedia.editshipping.ui.customview.ShippingInfoBottomSheet
import com.tokopedia.editshipping.util.EditShippingConstant.ARGUMENT_DATA_TOKEN
import com.tokopedia.editshipping.util.EditShippingConstant.LABEL_VALIDATION_BO
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.util.PinpointRolloutHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by Kris on 2/19/2016.
 * TOKOPEDIA
 */
class EditShippingFragment : Fragment(), EditShippingViewListener {
    private val RESULT_INTENT_DISTRICT_RECOMMENDATION = "district_recommendation_address"
    private var editShippingPresenter: EditShippingPresenter? = null
    private var mainProgressDialog: ProgressDialog? = null
    private var progressDialog: ProgressDialog? = null
    private var inputMethodManager: InputMethodManager? = null
    private var userSession: UserSessionInterface? = null
    private var bottomSheetValidation: BottomSheetUnify? = null
    private var mapMode = 0
    private var cacheManager: SaveInstanceCacheManager? = null

    private var whitelabelCoachmark: CoachMark2? = null

    private var binding by autoClearedNullable<FragmentShopShippingBinding>()

    private val pinpointPageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            changeGoogleMapData(it.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopShippingBinding.inflate(inflater, container, false)
        initiateVariables()
        hideAllView()
        setHasOptionsMenu(isEditShipping)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.fragmentShippingHeader?.setViewListener(this)
        binding?.shippingAddressLayout?.setViewListener(this)
        binding?.fragmentShippingHeader?.setListener(editShippingPresenter)
        binding?.shippingAddressLayout?.setListener(editShippingPresenter)
        if (arguments?.containsKey(RESUME_OPEN_SHOP_DATA_KEY) == true) {
            editShippingPresenter?.setSavedInstance(arguments)
        } else {
            context?.let {
                cacheManager = SaveInstanceCacheManager(it, savedInstanceState)
                editShippingPresenter?.setSavedInstance(cacheManager)
            }
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
        } else {
            data
        }
    }

    override fun onPause() {
        whitelabelCoachmark?.dismissCoachMark()
        whitelabelCoachmark = null

        super.onPause()
        if (bottomSheetValidation?.isVisible == true) {
            bottomSheetValidation?.dismiss()
        }
    }

    private fun hideAllView() {
        binding?.fragmentShippingHeader?.visibility = View.GONE
        binding?.shippingAddressLayout?.visibility = View.GONE
        binding?.fragmentShippingMainLayout?.visibility = View.GONE
    }

    private val data: Unit
        private get() {
            if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
                shippingDataCreateShop
            } else {
                shippingData
            }
        }

    private fun initiateVariables() {
        mapMode = arguments?.getInt(MAP_MODE) ?: 0
        mainProgressDialog = ProgressDialog(activity)
        mainProgressDialog?.cancel()
        progressDialog = ProgressDialog(activity)
        editShippingPresenter = EditShippingPresenterImpl(this)
        inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        userSession = UserSession(activity)
        binding?.submitButtonCreateShop?.setOnClickListener { submitButtonOnClickListener() }
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
            binding?.fragmentShippingHeader?.visibility = View.GONE
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
        binding?.fragmentShippingMainLayout?.addView(courierView)
        courierView.setViewListener(this)
        courier?.let { courierView.renderData(it, courierIndex) }
    }

    override fun setShopDetailedInformation(data: ShopShipping?) {
        if (data != null) {
            binding?.shippingAddressLayout?.renderData(data)
        }
    }

    override fun setGeoAddress(address: String?) {
        binding?.shippingAddressLayout?.renderGeoAddress(address)
    }

    override fun setShopLocationData(shopData: ShopShipping?) {
        if (shopData != null) {
            binding?.fragmentShippingHeader?.renderData(shopData)
        }
    }

    override fun validateShowPopup(data: ValidateShippingModel?) {
        if (data != null) {
            if (data.data.showPopup) {
                openPopupValidation(data)
            } else {
                submitData()
            }
        }
    }

    override val districtAndCity: String?
        get() = binding?.fragmentShippingHeader?.districtAndCity

    override val zipCode: String?
        get() = binding?.fragmentShippingHeader?.zipCodeData

    override val streetAddress: String?
        get() = binding?.shippingAddressLayout?.addressData

    override fun zipCodeEmpty() {
        binding?.fragmentShippingHeader?.setZipCodeError(activity?.getString(R.string.error_field_required))
    }

    override fun noServiceChosen() {
        NetworkErrorHelper.showSnackbar(
            activity,
            activity
                ?.getString(R.string.error_shipping_must_choose)
        )
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

    override fun setLocationProvinceCityDistrict(
        Province: String?,
        City: String?,
        District: String?
    ) {
        binding?.fragmentShippingHeader?.updateLocationData(Province, City, District)
    }

    override fun setLocationProvinceCityDistrict() {
        binding?.fragmentShippingHeader?.updateLocationData(getString(R.string.hint_choose_city))
    }

    override fun initializeZipCodes() {
        binding?.fragmentShippingHeader?.initializeZipCodes()
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
        if (activity != null) {
            progressDialog?.dismiss()
            Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFragmentTimeout() {
        NetworkErrorHelper.showEmptyState(
            activity,
            view,
            object : NetworkErrorHelper.RetryClickedListener {
                override fun onRetryClicked() {
                    mainProgressDialog?.show()
                    if (mapMode == CREATE_SHOP_PAGE) editShippingPresenter?.fetchDataOpenShop() else editShippingPresenter?.fetchData()
                }
            }
        )
    }

    override fun onFragmentNoConnection() {
        NetworkErrorHelper.showEmptyState(
            activity,
            view,
            getString(com.tokopedia.abstraction.R.string.msg_no_connection),
            object : NetworkErrorHelper.RetryClickedListener {
                override fun onRetryClicked() {
                    if (mapMode == CREATE_SHOP_PAGE) editShippingPresenter?.fetchDataOpenShop() else editShippingPresenter?.fetchData()
                }
            }
        )
        activity?.invalidateOptionsMenu()
    }

    override fun onShowViewAfterLoading() {
        activity?.invalidateOptionsMenu()
        binding?.fragmentShippingHeader?.visibility = View.VISIBLE
        binding?.shippingAddressLayout?.visibility = View.VISIBLE
        binding?.fragmentShippingMainLayout?.visibility = View.VISIBLE
        if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
            binding?.submitButtonCreateShop?.visibility =
                View.VISIBLE
        } else if (arguments?.containsKey(RESUME_OPEN_SHOP_DATA_KEY) == true) {
            binding?.submitButtonCreateShop?.visibility = View.VISIBLE
        } else {
            binding?.submitButtonCreateShop?.visibility = View.GONE
        }
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun openDataWebViewResources(courierIndex: Int) {
        editShippingPresenter?.dataWebViewResource(
            courierIndex,
            editShippingPresenter?.getCourierAdditionalOptionsURL(courierIndex)
        )
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
                    val address: DistrictRecommendationAddress? =
                        data?.getParcelableExtra(RESULT_INTENT_DISTRICT_RECOMMENDATION)
                    editShippingPresenter?.setSelectedAddress(address)
                    binding?.fragmentShippingHeader?.initializeZipCodes()
                    binding?.fragmentShippingHeader?.updateLocationData(
                        address?.provinceName,
                        address?.cityName,
                        address?.districtName
                    )
                    changeLocationRequest(address?.districtId)
                }
                ADDITIONAL_OPTION_REQUEST_CODE -> {
                    additionalOptionRequest(data)
                    inputMethodManager?.hideSoftInputFromWindow(
                        binding?.fragmentShippingMainLayout
                            ?.windowToken,
                        0
                    )
                }
            }
        }
    }

    private fun changeLocationRequest(originId: Long?) {
        progressDialog?.show()
        if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE || arguments?.containsKey(
                RESUME_OPEN_SHOP_DATA_KEY
            ) == true
        ) {
            editShippingPresenter?.fetchDataByLocationOpenShop(originId.toString())
        } else {
            editShippingPresenter?.fetchDataByLocation(originId.toString())
        }
    }

    private fun changeGoogleMapData(data: Intent?) {
        binding?.shippingAddressLayout?.setGoogleMapData(data)
    }

    private fun additionalOptionRequest(data: Intent?) {
        editShippingPresenter?.setCourierAdditionalOptionConfig(
            data?.getIntExtra(MODIFIED_COURIER_INDEX_KEY, 0),
            data?.getStringExtra(EDIT_SHIPPING_RESULT_KEY)
        )
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
        val fragmentHeader = binding?.fragmentShippingHeader?.isShown
        if (fragmentHeader != null) {
            item.isVisible = fragmentHeader
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragmentHeader = binding?.fragmentShippingHeader?.isShown
        if (item.itemId == R.id.action_send) {
            if (fragmentHeader != null) {
                editShippingPresenter?.getShopId()?.let {
                    editShippingPresenter?.validateBo(
                        it,
                        editShippingPresenter?.getCompiledShippingId().toString()
                    )
                }
            } else {
                showErrorToast(getString(R.string.dialog_on_process))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submitData() {
        editShippingPresenter?.submitValue()
    }

    private fun refreshView() {
        binding?.fragmentShippingMainLayout?.removeAllViews()
        editShippingPresenter?.refreshData()
    }

    fun submitButtonOnClickListener() {
        if (binding?.fragmentShippingMainLayout?.childCount == 0 && this.editShippingValid()) {
            val intent = Intent()
            intent.putExtra(EDIT_SHIPPING_DATA, currentShippingConfiguration)
            activity?.setResult(OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE, intent)
            activity?.finish()
        } else if (binding?.fragmentShippingMainLayout?.childCount == 0) {
            showErrorToast(activity?.getString(R.string.title_select_shop_location))
            EditShippingAnalytics.eventCreateShopFillLogisticError()
        }
    }

    override fun openGeoLocation() {
        val locationPass = LocationPass()
        if (editShippingPresenter?.shopInformation?.shopLatitude?.isNotEmpty() ?: false &&
            editShippingPresenter?.shopInformation?.shopLongitude?.isNotEmpty() ?: false
        ) {
            locationPass.latitude = editShippingPresenter?.shopInformation?.shopLatitude
            locationPass.longitude = editShippingPresenter?.shopInformation?.shopLongitude
            locationPass.generatedAddress = binding?.shippingAddressLayout?.googleMapAddressString
        } else {
            locationPass.districtName = editShippingPresenter?.shopInformation?.getDistrictName()
            locationPass.cityName = editShippingPresenter?.shopInformation?.getCityName()
        }
        val intent = activity?.let { getGeoLocationActivityIntent(it, locationPass) }
        pinpointPageResult.launch(intent)
    }

    override fun showInfoBottomSheet(information: String?, serviceName: String?) {
        ShippingInfoBottomSheet(information, serviceName, activity)
    }

    override fun refreshLocationViewListener(updatedShopInfo: ShopShipping?) {
        refreshView()
        binding?.fragmentShippingHeader?.updateLocationData(
            updatedShopInfo?.provinceName,
            updatedShopInfo?.cityName,
            updatedShopInfo?.districtName
        )
    }

    override fun refreshLocationViewListener(address: DistrictRecommendationAddress?) {
        refreshView()
    }

    override fun showOnBoarding(whitelabelIndex: Int, normalServiceIndex: Int) {
        context?.let {
            val sharedPref = WhitelabelInstanCoachMarkSharePref(it)
            if (sharedPref.getCoachMarkState() == true) {
                if (whitelabelIndex != -1 || normalServiceIndex != -1) {
                    val whitelabelView = binding?.fragmentShippingMainLayout?.getChildAt(whitelabelIndex)
                    if (whitelabelView != null) {
                        val normalServiceView =
                            binding?.fragmentShippingMainLayout?.getChildAt(normalServiceIndex)

                        val coachMarkItems =
                            generateOnBoardingCoachMark(it, normalServiceView, whitelabelView)
                        whitelabelCoachmark = CoachMark2(it).apply {
                            setOnBoardingListener(coachMarkItems)
                            setStateAfterOnBoardingShown(coachMarkItems, sharedPref)
                            manualScroll(coachMarkItems)
                        }
                    }
                }
            }
        }
    }

    private fun generateOnBoardingCoachMark(
        context: Context,
        normalService: View?,
        whitelabelService: View
    ): ArrayList<CoachMark2Item> {
        val coachMarkItems = ArrayList<CoachMark2Item>()
        normalService?.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    view,
                    context.getString(R.string.whitelabel_onboarding_title_coachmark),
                    context.getString(R.string.whitelabel_onboarding_description_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        }

        whitelabelService.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    view,
                    context.getString(R.string.whitelabel_instan_title_coachmark),
                    context.getString(R.string.whitelabel_instan_description_coachmark),
                    CoachMark2.POSITION_TOP
                )
            )
        }
        return coachMarkItems
    }

    private fun CoachMark2.setOnBoardingListener(coachMarkItems: ArrayList<CoachMark2Item>) {
        this.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                this@setOnBoardingListener.hideCoachMark()
                manualScroll(coachMarkItems, currentIndex)
            }
        })
    }

    private fun CoachMark2.manualScroll(
        coachMarkItems: ArrayList<CoachMark2Item>,
        currentIndex: Int = 0
    ) {
        coachMarkItems.getOrNull(currentIndex)?.anchorView?.let { rv ->
            binding?.mainScroll?.smoothScrollTo(0, rv.top)
            this.showCoachMark(coachMarkItems, null, currentIndex)
        }
    }

    private fun CoachMark2.setStateAfterOnBoardingShown(
        coachMarkItems: ArrayList<CoachMark2Item>,
        sharedPref: WhitelabelInstanCoachMarkSharePref
    ) {
        if (coachMarkItems.size > 1) {
            this.onFinishListener = {
                sharedPref.setCoachMarkState(false)
            }
        } else if (coachMarkItems.isNotEmpty()) {
            sharedPref.setCoachMarkState(false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager?.onSave(outState)
        if (arguments?.getInt(MAP_MODE) == CREATE_SHOP_PAGE) {
            editShippingPresenter?.saveOpenShopModel()
            cacheManager?.put(
                CURRENT_OPEN_SHOP_MODEL,
                editShippingPresenter?.openShopModel
            )
        } else {
            cacheManager?.put(
                CURRENT_COURIER_MODEL,
                editShippingPresenter?.shopModel
            )
        }
    }

    private fun getDistrictRecommendationIntent(activity: Activity, token: Token?): Intent? {
        val intent = RouteManager.getIntent(
            activity,
            ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS
        )
        intent.putExtra(ARGUMENT_DATA_TOKEN, token)
        return intent
    }

    private fun openPopupValidation(data: ValidateShippingModel) {
        bottomSheetValidation = BottomSheetUnify()
        context?.let { ctx ->
            val viewBottomSheetValidation = PopupValidationBoBinding.inflate(LayoutInflater.from(ctx)).apply {
                tickerValidationBo.tickerTitle =
                    HtmlLinkHelper(ctx, data.data.tickerTitle).spannedString.toString()
                tickerValidationBo.setHtmlDescription(data.data.tickerContent)
                tickerValidationBo.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        val url = data.data.tickerContent.substringAfter("<a href='")
                            .substringBefore("'>di sini</a>")
                        val intent = RouteManager.getIntent(
                            context,
                            "${ApplinkConst.WEBVIEW}?titlebar=false&url=$url"
                        )
                        startActivity(intent)
                    }

                    override fun onDismiss() {
                        // no-op
                    }
                })

                pointOne.text = HtmlLinkHelper(ctx, data.data.popupContent[0]).spannedString
                pointTwo.text = HtmlLinkHelper(ctx, data.data.popupContent[1]).spannedString
                pointThree.text = HtmlLinkHelper(ctx, data.data.popupContent[2]).spannedString

                btnNonaktifkan.setOnClickListener {
                    submitData()
                    bottomSheetValidation?.dismiss()
                }
                btnAktifkan.setOnClickListener {
                    bottomSheetValidation?.dismiss()
                }
            }

            bottomSheetValidation?.apply {
                setTitle(LABEL_VALIDATION_BO)
                setCloseClickListener { dismiss() }
                setChild(viewBottomSheetValidation.root)
                setOnDismissListener { dismiss() }
            }
        }

        fragmentManager?.let {
            bottomSheetValidation?.show(it, "show")
        }
    }

    private fun getGeoLocationActivityIntent(
        activity: Activity,
        locationPass: LocationPass
    ): Intent? {
        activity.let {
            if (PinpointRolloutHelper.eligibleForRevamp(it, true)) {
                // go to pinpoint
                val bundle = Bundle().apply {
                    putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                    if (!locationPass.latitude.isNullOrEmpty() && !locationPass.longitude.isNullOrEmpty()) {
                        putDouble(AddressConstant.EXTRA_LAT, locationPass.latitude.toDouble())
                        putDouble(AddressConstant.EXTRA_LONG, locationPass.longitude.toDouble())
                    }
                    putString(AddressConstant.EXTRA_CITY_NAME, locationPass.cityName)
                    putString(AddressConstant.EXTRA_DISTRICT_NAME, locationPass.districtName)
                }
                return RouteManager.getIntent(it, ApplinkConstInternalLogistic.PINPOINT).apply {
                    putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                }
            } else {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
                intent.apply {
                    putExtra(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
                    putExtra(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, false)
                }
                return intent
            }
        }
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
