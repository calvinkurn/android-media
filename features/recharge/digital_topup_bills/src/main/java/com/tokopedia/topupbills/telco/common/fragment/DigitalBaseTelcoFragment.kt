package com.tokopedia.topupbills.telco.common.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.common.analytics.DigitalTopupEventTracking
import com.tokopedia.topupbills.telco.common.covertContactUriToContactData
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.viewmodel.SharedTelcoViewModel
import com.tokopedia.topupbills.telco.data.RechargeCatalogPrefixSelect
import com.tokopedia.topupbills.telco.data.TelcoCatalogPrefixSelect
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.prepaid.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
abstract class DigitalBaseTelcoFragment : BaseTopupBillsFragment() {

    protected lateinit var pageContainer: RelativeLayout
    protected lateinit var tickerView: Ticker
    private lateinit var viewModel: SharedTelcoViewModel
    protected var listMenu = mutableListOf<TelcoTabItem>()
    protected var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect())

    override var categoryId: Int = 0
        set(value) {
            field = value
            categoryName = topupAnalytics.getCategoryName(value)
        }

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

    override fun initInjector() {
        getComponent(DigitalTelcoComponent::class.java).inject(this)
    }

    private fun subscribeUi() {
        viewModel.selectedRecentNumber.observe(this, Observer {
            onClickItemRecentNumber(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedTelcoViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
        val checkoutView = getCheckoutView()
        checkoutView?.run {
            listener = object : TopupBillsCheckoutWidget.ActionListener {
                override fun onClickNextBuyButton() {
                    setupCheckoutData()
                    processTransaction()
                }
            }
        }
    }

    private fun renderTicker(tickers: List<TopupBillsTicker>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<TickerData>()
            for (item in tickers) {
                messages.add(TickerData(item.name, item.content,
                        when (item.type) {
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }))
            }
            context?.run {
                tickerView.addPagerView(TickerPagerAdapter(this, messages), messages)
            }
            tickerView.visibility = View.VISIBLE
        } else {
            tickerView.visibility = View.GONE
        }

    }

    protected fun navigateContact() {
        topupAnalytics.eventClickOnContactPickerHomepage()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.let {
                permissionCheckerHelper.checkPermission(it,
                        PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper.onPermissionDenied(it, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                            }

                            override fun onPermissionGranted() {
                                openContactPicker()
                            }
                        }, "")
            }
        } else {
            openContactPicker()
        }
    }

    protected fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            NetworkErrorHelper.showSnackbar(activity,
                    getString(R.string.error_message_contact_not_found))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.run {
            permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        val contactURI = data.data
                        val contact = contactURI?.covertContactUriToContactData(it.contentResolver)
                        setInputNumberFromContact(contact?.contactNumber ?: "")
                    }
                } else if (requestCode == REQUEST_CODE_DIGITAL_SEARCH_NUMBER) {
                    if (data != null) {
                        val inputNumberActionType = data.getIntExtra(EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, 0)
                        val orderClientNumber = data.getParcelableExtra<Parcelable>(EXTRA_CALLBACK_CLIENT_NUMBER)
                        handleCallbackSearchNumber(orderClientNumber as TopupBillsFavNumberItem, inputNumberActionType)
                    } else {
                        handleCallbackSearchNumberCancel()
                    }
                } else if (requestCode == REQUEST_CODE_CART_DIGITAL) {
                    if (data.hasExtra(DigitalExtraParam.EXTRA_MESSAGE)) {
                        val message = data.getStringExtra(DigitalExtraParam.EXTRA_MESSAGE)
                        if (!TextUtils.isEmpty(message)) {
                            showErrorCartDigital(message)
                        }
                    }
                } else if (requestCode == REQUEST_CODE_LOGIN) {
                    if (userSession.isLoggedIn) {
                        navigateToCart()
                    }
                }
            }
        }
    }

    fun getPrefixOperatorData() {
        viewModel.getPrefixOperator(GraphqlHelper.loadRawString(resources,
                R.raw.query_prefix_select_telco), getTelcoMenuId())
        viewModel.catalogPrefixSelect.observe(this, Observer {
            when (it) {
                is Success -> onSuccessCustomData()
                is Fail -> onErrorCustomData()
            }
        })
    }

    private fun onSuccessCustomData() {
        this.operatorData = (viewModel.catalogPrefixSelect.value as Success).data
        renderProductFromCustomData()
    }

    private fun onErrorCustomData() {
        val errorData = (viewModel.catalogPrefixSelect.value as Fail).throwable
        view?.run {
            Toaster.make(this, ErrorHandler.getErrorMessage(context, errorData), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    protected fun validatePhoneNumber(operatorData: TelcoCatalogPrefixSelect, clientNumberWidget: DigitalClientNumberWidget) {
        for (validation in operatorData.rechargeCatalogPrefixSelect.validations) {
            val phoneIsValid = Pattern.compile(validation.rule)
                    .matcher(clientNumberWidget.getInputNumber()).matches()
            if (!phoneIsValid) {
                clientNumberWidget.setErrorInputNumber(validation.message)
                break
            }
        }
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        // do nothing
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        super.processMenuDetail(data)

        renderTicker(data.tickers)
        sendOpenScreenTracking()
        initiateMenuTelco(data.recommendations, data.promos)
    }

    private fun initiateMenuTelco(recom: List<TopupBillsRecommendation>, promo: List<TopupBillsPromo>) {
        listMenu.clear()
        var idTab = 1L
        if (promo.isNotEmpty()) {
            viewModel.setPromoTelco(promo)
            listMenu.add(TelcoTabItem(null, TelcoComponentName.PROMO, idTab++))
        }
        if (recom.isNotEmpty()) {
            viewModel.setRecommendationTelco(recom)
            listMenu.add(TelcoTabItem(null, TelcoComponentName.RECENTS, idTab++))
        }

        viewModel.setTitleMenu(listMenu.size < 2)
        renderPromoAndRecommendation()
    }

    fun sendImpressionPromo() {
        viewModel.setPromoImpression()
    }

    fun sendImpressionRecents() {
        viewModel.setRecentsImpression()
    }

    override fun onMenuDetailError(error: Throwable) {
        super.onMenuDetailError(error)
        NetworkErrorHelper.showEmptyState(activity, pageContainer, ErrorHandler.getErrorMessage(context, error)) {
            getMenuDetail(getTelcoMenuId())
        }
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {
        setFavNumbers(data)
    }

    override fun onEnquiryError(error: Throwable) {
        //do nothing
    }

    override fun onCatalogPluginDataError(error: Throwable) {
        //do nothing
    }

    override fun onFavoriteNumbersError(error: Throwable) {
        errorSetFavNumbers()
    }

    override fun onCheckVoucherError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    override fun onExpressCheckoutError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    private fun sendOpenScreenTracking() {
        rechargeAnalytics.eventOpenScreen(
                userSession.userId,
                topupAnalytics.getCategoryName(getTelcoCategoryId()),
                getTelcoCategoryId().toString()
        )
    }

    protected fun setTrackingOnTabMenu(title: String) {
        var action = DigitalTopupEventTracking.Action.CLICK_TAB_PROMO
        if (title == TelcoComponentName.RECENTS) {
            action = DigitalTopupEventTracking.Action.CLICK_TAB_RECENT
        }
        topupAnalytics.eventClickTabMenuTelco(categoryId, userSession.userId, action)
    }

    abstract fun onCollapseAppBar()

    abstract fun onExpandAppBar()

    abstract fun onBackPressed()

    protected abstract fun getTelcoMenuId(): Int

    protected abstract fun getTelcoCategoryId(): Int

    protected abstract fun renderPromoAndRecommendation()

    protected abstract fun renderProductFromCustomData()

    protected abstract fun setupCheckoutData()

    protected abstract fun showErrorCartDigital(message: String)

    protected abstract fun setFavNumbers(data: TopupBillsFavNumber)

    protected abstract fun errorSetFavNumbers()

    protected abstract fun handleCallbackSearchNumber(orderClientNumber: TopupBillsFavNumberItem, inputNumberActionTypeIndex: Int)

    protected abstract fun handleCallbackSearchNumberCancel()

    protected abstract fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation)

    protected abstract fun setInputNumberFromContact(contactNumber: String)

    override fun onDestroy() {
        listMenu.clear()
        super.onDestroy()
    }

    companion object {
        const val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 77
        const val REQUEST_CODE_CONTACT_PICKER = 78
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
    }
}