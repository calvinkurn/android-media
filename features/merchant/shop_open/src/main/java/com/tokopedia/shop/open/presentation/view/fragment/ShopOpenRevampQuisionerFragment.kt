package com.tokopedia.shop.open.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.analytic.ShopOpenRevampTracking
import com.tokopedia.shop.open.common.ErrorConstant.ERROR_GET_SURVEY_QUESTIONS
import com.tokopedia.shop.open.common.ErrorConstant.ERROR_SAVE_LOCATION_SHIPPING
import com.tokopedia.shop.open.common.ErrorConstant.ERROR_SEND_SURVEY
import com.tokopedia.shop.open.common.EspressoIdlingResource
import com.tokopedia.shop.open.common.ExitDialog
import com.tokopedia.shop.open.common.PageNameConstant.FINISH_SPLASH_SCREEN_PAGE
import com.tokopedia.shop.open.common.ScreenNameTracker
import com.tokopedia.shop.open.common.ShopOpenRevampErrorHandler
import com.tokopedia.shop.open.di.DaggerShopOpenRevampComponent
import com.tokopedia.shop.open.di.ShopOpenRevampComponent
import com.tokopedia.shop.open.di.ShopOpenRevampModule
import com.tokopedia.shop.open.listener.FragmentNavigationInterface
import com.tokopedia.shop.open.listener.SurveyListener
import com.tokopedia.shop.open.presentation.adapter.ShopOpenRevampQuisionerAdapter
import com.tokopedia.shop.open.presentation.viewmodel.ShopOpenRevampViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

class ShopOpenRevampQuisionerFragment :
        BaseDaggerFragment(),
        SurveyListener,
        HasComponent<ShopOpenRevampComponent>  {

    @Inject
    lateinit var viewModel: ShopOpenRevampViewModel
    private lateinit var btnNext: UnifyButton
    private val questionsAndAnswersId = mutableMapOf<Int, MutableList<Int>>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: ShopOpenRevampQuisionerAdapter? = null
    private var shopOpenRevampTracking: ShopOpenRevampTracking? = null
    private var fragmentNavigationInterface: FragmentNavigationInterface? = null
    private var isNeedLocation = false
    private var header: HeaderUnify? = null
    private lateinit var loading: LoaderUnify

    private var shopId = 0
    private var postCode = ""
    private var courierOrigin = 0
    private var addrStreet = ""
    private var latitude = ""
    private var longitude = ""

    companion object {
        const val THREE_FRAGMENT_TAG = "three"
        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val REQUEST_CODE_PINPOINT = 789
        const val EXTRA_ADDRESS_MODEL = "EXTRA_ADDRESS_MODEL"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationInterface = context as FragmentNavigationInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            shopOpenRevampTracking = ShopOpenRevampTracking(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_open_revamp_quisioner, container, false)
        loading = view.findViewById(R.id.loading)
        btnNext = view.findViewById(R.id.next_button_quisioner_page)
        recyclerView = view.findViewById(R.id.recycler_view_questions_list)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = ShopOpenRevampQuisionerAdapter(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter
        btnNext.isEnabled = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarActions(view)
        shopOpenRevampTracking?.sendScreenNameTracker(ScreenNameTracker.SCREEN_SHOP_SURVEY)
        setupPreconditions()
        showLoader()
        loadDataSurvey()
        observeSurveyData()
        observeSendSurveyResult()
        observeSaveShipmentLocationData()

        btnNext.setOnClickListener {
            shopOpenRevampTracking?.clickButtonNextFromSurveyPage()
            val dataSurveyInput: MutableMap<String, Any> = viewModel.getDataSurveyInput(questionsAndAnswersId)
            EspressoIdlingResource.increment()
            viewModel.sendSurveyData(dataSurveyInput)
        }
    }

    override fun onCheckedCheckbox(questionId: Int, choiceId: Int) {
        btnNext.isEnabled = true
        if (!questionsAndAnswersId.containsKey(questionId)) {
            questionsAndAnswersId[questionId] = mutableListOf()
            questionsAndAnswersId[questionId]?.add(choiceId)
        } else {
            questionsAndAnswersId[questionId]?.add(choiceId)
        }
    }

    override fun onUncheckedCheckbox(questionId: Int, choiceId: Int) {
        questionsAndAnswersId?.let {
            if (it.containsKey(questionId)) {
                it[questionId]?.remove(choiceId)
                if (it.get(questionId)?.isEmpty() == true) {
                    btnNext.isEnabled = false
                }
            }
            if (it.isEmpty()) {
                btnNext.isEnabled = false
            }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onDestroy() {
        viewModel.getSurveyDataResponse.removeObservers(this)
        viewModel.sendSurveyDataResponse.removeObservers(this)
        viewModel.saveShopShipmentLocationResponse.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun getComponent(): ShopOpenRevampComponent {
        return activity.run {
            DaggerShopOpenRevampComponent
                    .builder()
                    .shopOpenRevampModule(ShopOpenRevampModule())
                    .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun onResume() {
        super.onResume()
        closeKeyboard()
    }

    private fun setupToolbarActions(view: View?) {
        header = view?.findViewById<HeaderUnify>(R.id.toolbar_questioner)?.apply {
            actionText = getString(R.string.button_label_skip)
            transparentMode = context.isDarkMode()
            isShowShadow = false
            setNavigationOnClickListener {
                shopOpenRevampTracking?.clickBackButtonFromSurveyPage()
                fragmentNavigationInterface?.showExitDialog()
            }
            actionTextView?.setOnClickListener {
                shopOpenRevampTracking?.clickTextSkipFromSurveyPage()
                gotoPickLocation()
            }
        }
    }

    private fun observeSurveyData() {
        viewModel.getSurveyDataResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    hideLoader()
                    val questions = it.data.getSurveyData.result.questions
                    if (questions.size > 0) {
                        adapter?.updateDataQuestionsList(questions)
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorNetwork(errorMessage) {
                        loadDataSurvey()
                    }
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ERROR_GET_SURVEY_QUESTIONS,
                            userId = userSession.userId,
                            message = it.throwable.message ?: ""
                    )
                }
            }
        })
    }

    private fun observeSendSurveyResult() {
        viewModel.sendSurveyDataResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    val isSuccess = it.data.sendSurveyData.success
                    val message = it.data.sendSurveyData.message
                    if (isSuccess) {
                        showLoader()
                        gotoPickLocation()
                    } else {
                        showErrorResponse(message)
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorNetwork(errorMessage) {
                        val dataSurveyInput: MutableMap<String, Any> = viewModel.getDataSurveyInput(questionsAndAnswersId)
                        viewModel.sendSurveyData(dataSurveyInput)
                    }
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ERROR_SEND_SURVEY,
                            userId = userSession.userId,
                            message = errorMessage
                    )
                }
            }
        })
    }

    private fun observeSaveShipmentLocationData() {
        viewModel.saveShopShipmentLocationResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    val isSuccess = it.data.ongkirOpenShopShipmentLocation.dataSuccessResponse.success
                    if (isSuccess) {
                        showLoader()
                        EspressoIdlingResource.increment()
                        fragmentNavigationInterface?.navigateToNextPage(FINISH_SPLASH_SCREEN_PAGE, THREE_FRAGMENT_TAG)
                    } else {
                        showLoader()
                        gotoPickLocation()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorNetwork(errorMessage) {
                        if (shopId != 0 && postCode != "" && courierOrigin != 0
                                && addrStreet != "" && latitude != "" && longitude != "") {
                            saveShipmentLocation(shopId, postCode, courierOrigin, addrStreet, latitude, longitude)
                        }
                    }
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ERROR_SAVE_LOCATION_SHIPPING,
                            userId = userSession.userId,
                            message = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun gotoPickLocation() {
        val intent = RouteManager.getIntent(
                activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, false)
        startActivityForResult(intent, REQUEST_CODE_PINPOINT)
    }

    private fun showErrorResponse(message: String) {
        view?.let {
            Toaster.showError(it, message, Snackbar.LENGTH_LONG)
        }
    }

    private fun showErrorNetwork(errorMessage: String, retry: () -> Unit) {
        view?.let {
            Toaster.showErrorWithAction(
                    it,
                    errorMessage,
                    Snackbar.LENGTH_LONG,
                    getString(R.string.open_shop_revamp_retry),
                    View.OnClickListener {
                        retry.invoke()
                    }
            )
        }
    }

    private fun loadDataSurvey() {
        EspressoIdlingResource.increment()
        viewModel.getSurveyQuizionaireData()
    }

    private fun setupPreconditions() {
        arguments?.let {
            // Check the needs to bypass to logistic without opening survey page
            isNeedLocation = it.getBoolean(
                    ApplinkConstInternalMarketplace.PARAM_IS_NEED_LOC,
                    false)
        }

        if (isNeedLocation) {
            showLoader()
            gotoPickLocation()
        }
    }

    private fun showLoader() {
        header?.visibility =  View.INVISIBLE
        recyclerView?.visibility = View.INVISIBLE
        btnNext.visibility = View.INVISIBLE
        loading.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        header?.visibility =  View.VISIBLE
        recyclerView?.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        loading.visibility = View.INVISIBLE
    }

    private fun saveShipmentLocation(shopId: Int, postalCode: String, courierOrigin: Int,
                                     addrStreet: String, lat: String, long: String) {
        EspressoIdlingResource.increment()
        viewModel.saveShippingLocation(
                viewModel.getSaveShopShippingLocationData(
                        shopId, postalCode, courierOrigin, addrStreet, lat, long
                )
        )
    }

    private fun closeKeyboard() {
        activity?.let {
            val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_PINPOINT -> if (resultCode == Activity.RESULT_OK) {
                showLoader()
                data?.let {
                    val saveAddressDataModel = it.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_MODEL)
                    var _latitudeString: String = ""
                    var _longitudeString: String = ""
                    var _postalCode: String = ""
                    var _districtId: Int = 0
                    var _formattedAddress: String = ""

                    saveAddressDataModel?.let {
                        _latitudeString = if (it.latitude != null) it.latitude.toString() else ""
                        _longitudeString = if (it.longitude != null) it.longitude.toString() else ""
                        _postalCode = if (it.postalCode != null) it.postalCode.toString() else ""
                        _districtId = if (it.districtId != null) it.districtId else 0
                        _formattedAddress = if (it.formattedAddress != null) it.formattedAddress else ""
                    }

                    val _shopId = if (userSession.shopId.isNotEmpty()) userSession.shopId.toInt() else 0 // Get shopId from create Shop

                    if (!_shopId.equals(0) &&
                            _postalCode.isNotEmpty() &&
                            _latitudeString.isNotEmpty() &&
                            _longitudeString.isNotEmpty() &&
                            _districtId != 0 &&
                            _formattedAddress.isNotEmpty()) {

                        shopId = _shopId
                        postCode = _postalCode
                        courierOrigin = _districtId.toInt()
                        addrStreet = _formattedAddress
                        latitude = _latitudeString
                        longitude = _longitudeString
                        saveShipmentLocation(shopId, postCode, courierOrigin, addrStreet, latitude, longitude)
                    } else {
                        view?.let {
                            Toaster.showError(it, "Please select valid address", Snackbar.LENGTH_LONG)
                        }
                        gotoPickLocation()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (isNeedLocation) {
                    // Should have stayed in the feature instead of finish the feature
                    hideLoader()
                } else {
                    hideLoader()
                    showExitOrPickLocationDialog()
                }
            }
        }
    }

    private fun showExitOrPickLocationDialog() {
        activity?.let {
            val exitDialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            exitDialog.apply {
                setTitle(ExitDialog.TITLE)
                setDescription(ExitDialog.DESCRIPTION)
                setPrimaryCTAText(getString(R.string.open_shop_cancel))
                setPrimaryCTAClickListener {
                    this.dismiss()
                    gotoPickLocation()
                }
                setSecondaryCTAText(getString(R.string.open_shop_logout_button))
                setSecondaryCTAClickListener {
                    if (GlobalConfig.isSellerApp()) {
                        RouteManager.route(exitDialog.context, ApplinkConstInternalGlobal.LOGOUT)
                    }
                    it.finish()
                }
                show()
            }
        }
    }
}