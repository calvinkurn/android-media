package com.tokopedia.shop.open.shop_open_revamp.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
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
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.shop_open_revamp.analytic.ShopOpenRevampTracking
import com.tokopedia.shop.open.shop_open_revamp.common.ExitDialog
import com.tokopedia.shop.open.shop_open_revamp.common.PageNameConstant.FINISH_SPLASH_SCREEN_PAGE
import com.tokopedia.shop.open.shop_open_revamp.di.DaggerShopOpenRevampComponent
import com.tokopedia.shop.open.shop_open_revamp.di.ShopOpenRevampComponent
import com.tokopedia.shop.open.shop_open_revamp.di.ShopOpenRevampModule
import com.tokopedia.shop.open.shop_open_revamp.listener.FragmentNavigationInterface
import com.tokopedia.shop.open.shop_open_revamp.listener.SurveyListener
import com.tokopedia.shop.open.shop_open_revamp.presentation.adapter.ShopOpenRevampQuisionerAdapter
import com.tokopedia.shop.open.shop_open_revamp.presentation.viewmodel.ShopOpenRevampViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopOpenRevampQuisionerFragment :
        BaseDaggerFragment(),
        SurveyListener,
        HasComponent<ShopOpenRevampComponent>  {

    @Inject
    lateinit var viewModel: ShopOpenRevampViewModel
    private lateinit var btnNext: UnifyButton
    private lateinit var btnBack: ImageView
    private lateinit var btnSkip: TextView
    private val questionsAndAnswersId = mutableMapOf<Int, MutableList<Int>>()
    private val userSession: UserSessionInterface by lazy { UserSession(activity) }
    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: ShopOpenRevampQuisionerAdapter? = null
    private var shopOpenRevampTracking: ShopOpenRevampTracking? = null
    private var fragmentNavigationInterface: FragmentNavigationInterface? = null
    private var isNeedLocation = false
    private lateinit var loading: LoaderUnify
    private lateinit var toolbar: Toolbar

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
        toolbar = view.findViewById(R.id.toolbar)
        loading = view.findViewById(R.id.loading)
        btnNext = view.findViewById(R.id.next_button_quisioner_page)
        btnBack = view.findViewById(R.id.btn_back_quisioner_page)
        btnSkip = view.findViewById(R.id.btn_skip_quisioner_page)
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
        setupPreconditions()
        showLoader()
        loadDataSurvey()
        observeSurveyData()
        observeSendSurveyResult()
        observeSaveShipmentLocationData()

        btnBack.setOnClickListener {
            shopOpenRevampTracking?.clickBackButtonFromSurveyPage()
            fragmentNavigationInterface?.showExitDialog()
        }

        btnSkip.setOnClickListener {
            shopOpenRevampTracking?.clickTextSkipFromSurveyPage()
            gotoPickLocation()
        }

        btnNext.setOnClickListener {
            shopOpenRevampTracking?.clickButtonNextFromSurveyPage()
            viewModel.sendInputSurveyData(questionsAndAnswersId)
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

    private fun observeSurveyData() {
        viewModel.getSurveyDataResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    hideLoader()
                    val questions = it.data.getSurveyData.result.questions
                    if (questions.size > 0) {
                        adapter?.updateDataQuestionsList(questions)
                    }
                }
                is Fail -> {
                    showErrorNetwork(it.throwable) {
                        loadDataSurvey()
                    }
                }
            }
        })
    }

    private fun observeSendSurveyResult() {
        viewModel.sendSurveyDataResponse.observe(this, Observer {
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
                    showErrorNetwork(it.throwable) {
                        viewModel.sendInputSurveyData(questionsAndAnswersId)
                    }
                }
            }
        })
    }

    private fun observeSaveShipmentLocationData() {
        viewModel.saveShopShipmentLocationResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val isSuccess = it.data.ongkirOpenShopShipmentLocation.dataSuccessResponse.success
                    if (isSuccess) {
                        showLoader()
                        fragmentNavigationInterface?.navigateToNextPage(FINISH_SPLASH_SCREEN_PAGE, THREE_FRAGMENT_TAG)
                    } else {
                        showLoader()
                        gotoPickLocation()
                    }
                }
                is Fail -> {
                    showErrorNetwork(it.throwable) {
                        if (shopId != 0 && postCode != "" && courierOrigin != 0
                                && addrStreet != "" && latitude != "" && longitude != "") {
                            saveShipmentLocation(shopId, postCode, courierOrigin, addrStreet, latitude, longitude)
                        }
                    }
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

    private fun showErrorNetwork(t: Throwable, retry: () -> Unit) {
        view?.let {
            Toaster.showErrorWithAction(
                    it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG,
                    getString(R.string.open_shop_revamp_retry),
                    View.OnClickListener {
                        retry.invoke()
                    }
            )
        }
    }

    private fun loadDataSurvey() {
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
        toolbar.visibility =  View.INVISIBLE
        recyclerView?.visibility = View.INVISIBLE
        btnNext.visibility = View.INVISIBLE
        loading.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        toolbar.visibility =  View.VISIBLE
        recyclerView?.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        loading.visibility = View.INVISIBLE
    }

    private fun saveShipmentLocation(shopId: Int, postalCode: String, courierOrigin: Int,
                                     addrStreet: String, lat: String, long: String) {
        viewModel.saveShippingLocation(shopId, postalCode, courierOrigin, addrStreet, lat, long)
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
                    val latitudeString = saveAddressDataModel.latitude.toString()
                    val longitudeString = saveAddressDataModel.longitude.toString()
                    val _shopId = if (userSession.shopId.isNotEmpty()) userSession.shopId.toInt() else 0 // Get shopId from create Shop

                    if (!_shopId.equals(0) &&
                            saveAddressDataModel.postalCode.isNotEmpty() &&
                            latitudeString.isNotEmpty() &&
                            longitudeString.isNotEmpty() &&
                            saveAddressDataModel.districtId != 0 &&
                            saveAddressDataModel.formattedAddress.isNotEmpty()) {

                        shopId = _shopId
                        postCode = saveAddressDataModel.postalCode
                        courierOrigin = saveAddressDataModel.districtId.toInt()
                        addrStreet = saveAddressDataModel.formattedAddress
                        latitude = latitudeString
                        longitude = longitudeString
                        saveShipmentLocation(shopId, postCode, courierOrigin, addrStreet, latitude, longitude)
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (isNeedLocation) {
                    activity?.finish()
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