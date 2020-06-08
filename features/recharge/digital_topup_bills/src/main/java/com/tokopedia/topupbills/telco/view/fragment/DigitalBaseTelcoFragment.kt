package com.tokopedia.topupbills.telco.view.fragment

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.common.topupbills.widget.TopupBillsRecentTransactionWidget
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.telco.data.RechargeCatalogPrefixSelect
import com.tokopedia.topupbills.telco.data.TelcoCatalogPrefixSelect
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoOperatorViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
abstract class DigitalBaseTelcoFragment : BaseTopupBillsFragment() {

    protected lateinit var mainContainer: NestedScrollView
    protected lateinit var tickerView: Ticker
    protected lateinit var recentNumbersWidget: TopupBillsRecentTransactionWidget
    protected lateinit var promoListWidget: TopupBillsPromoListWidget
    private lateinit var operatorViewModel: DigitalTelcoOperatorViewModel

    protected var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect())
    override var menuId = 0

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

    override fun initInjector() {
        getComponent(DigitalTopupComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            operatorViewModel = viewModelProvider.get(DigitalTelcoOperatorViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            //TODO
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
                        val contact = contactURI.covertContactUriToContactData(it.contentResolver)
                        setInputNumberFromContact(contact.contactNumber)
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
        operatorViewModel.getPrefixOperator(GraphqlHelper.loadRawString(resources,
                R.raw.query_prefix_select_telco), menuId)
        operatorViewModel.catalogPrefixSelect.observe(this, Observer {
            when (it) {
                is Success -> onSuccessCustomData()
                is Fail -> onErrorCustomData()
            }
        })
    }

    private fun onSuccessCustomData() {
        this.operatorData = (operatorViewModel.catalogPrefixSelect.value as Success).data
        renderProductFromCustomData()
    }

    private fun onErrorCustomData() {
        val errorData = (operatorViewModel.catalogPrefixSelect.value as Fail).throwable
        view?.run {
            Toaster.make(this, ErrorHandler.getErrorMessage(context, errorData), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun renderRecentTransactions(recentNumbers: List<TopupBillsRecommendation>) {
        if (recentNumbers.isNotEmpty()) {
            recentNumbersWidget.setListener(object : TopupBillsRecentTransactionWidget.ActionListener {
                override fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, categoryId: Int,
                                                 position: Int) {
                    topupBillsRecommendation.position = position
                    onClickItemRecentNumber(topupBillsRecommendation)
                }

                override fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>) {
                    topupAnalytics.impressionEnhanceCommerceRecentTransaction(topupBillsTrackRecentList)
                }
            })
            recentNumbersWidget.setRecentNumbers(recentNumbers)
            //TODO
            recentNumbersWidget.visibility = View.VISIBLE
        } else {
            recentNumbersWidget.visibility = View.GONE
        }
    }

    protected fun handleFocusClientNumber() {
        mainContainer.setOnTouchListener { view1, motionEvent ->
            view1.clearFocus()
            false
        }
    }

    private fun renderPromoList(promos: List<TopupBillsPromo>) {
        if (promos.isNotEmpty()) {
            //TODO
            promoListWidget.visibility = View.VISIBLE
            promoListWidget.setListener(object : TopupBillsPromoListWidget.ActionListener {
                override fun onCopiedPromoCode(promoId: Int, voucherCode: String) {
                    clickCopyOnPromoCode(promoId)
                    topupAnalytics.eventClickCopyPromoCode(voucherCode, promos.indexOfFirst { it.promoCode == voucherCode })

                    activity?.let {
                        val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(
                                CLIP_DATA_VOUCHER_CODE_DIGITAL, voucherCode
                        )
                        clipboard.primaryClip = clip

                        view?.run {
                            Toaster.make(this,
                                    getString(com.tokopedia.common.topupbills.R.string.common_topup_voucher_code_already_copied), Snackbar.LENGTH_LONG)
                        }
                    }
                }

                override fun onTrackImpressionPromoList(topupBillsTrackPromoList: List<TopupBillsTrackPromo>) {
                    topupAnalytics.impressionEnhanceCommercePromoList(topupBillsTrackPromoList)
                }

                override fun onClickItemPromo(topupBillsPromo: TopupBillsPromo, position: Int) {
                    topupAnalytics.clickEnhanceCommercePromo(topupBillsPromo, position)
                    if (!TextUtils.isEmpty(topupBillsPromo.urlBannerPromo)) {
                        RouteManager.route(activity, topupBillsPromo.urlBannerPromo)
                    }
                }
            })
            promoListWidget.setPromoList(promos)
        } else {
            //TODO
            promoListWidget.visibility = View.GONE
        }
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        // do nothing
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        super.processMenuDetail(data)

        renderPromoList(data.promos)
        renderRecentTransactions(data.recommendations)
        renderTicker(data.tickers)
    }

    override fun processFavoriteNumbers(data: TopupBillsFavNumber) {
        setFavNumbers(data)
    }

    override fun onEnquiryError(error: Throwable) {
        //do nothing
    }

    override fun onMenuDetailError(error: Throwable) {
        super.onMenuDetailError(error)
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onCatalogPluginDataError(error: Throwable) {
        //do nothing
    }

    override fun onFavoriteNumbersError(error: Throwable) {
        errorSetFavNumbers()
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onCheckVoucherError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    override fun onExpressCheckoutError(error: Throwable) {
        NetworkErrorHelper.showRedSnackbar(activity, error.message)
    }

    protected abstract fun renderProductFromCustomData()

    protected abstract fun setupCheckoutData()

    protected abstract fun showErrorCartDigital(message: String)

    protected abstract fun setFavNumbers(data: TopupBillsFavNumber)

    protected abstract fun errorSetFavNumbers()

    protected abstract fun handleCallbackSearchNumber(orderClientNumber: TopupBillsFavNumberItem, inputNumberActionTypeIndex: Int)

    protected abstract fun handleCallbackSearchNumberCancel()

    protected abstract fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation)

    protected abstract fun clickCopyOnPromoCode(promoId: Int)

    protected abstract fun setInputNumberFromContact(contactNumber: String)

    protected abstract fun onBackPressed()

    override fun onDestroy() {
        operatorViewModel.flush()
        super.onDestroy()
    }

    companion object {
        const val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 77
        const val REQUEST_CODE_CONTACT_PICKER = 78
        const val REQUEST_CODE_LOGIN = 1010
        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val CLIP_DATA_VOUCHER_CODE_DIGITAL = "digital_telco_clip_data_promo"
    }
}