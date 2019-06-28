package com.tokopedia.topupbills.telco.view.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.design.component.ticker.TickerView
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoCustomViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.TelcoCatalogMenuDetailViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalPromoListWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalRecentTransactionWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
open abstract class DigitalBaseTelcoFragment : BaseDaggerFragment() {

    protected lateinit var mainContainer: NestedScrollView
    protected lateinit var tickerView: TickerView
    protected lateinit var recentNumbersView: DigitalRecentTransactionWidget
    protected lateinit var promoListView: DigitalPromoListWidget
    protected lateinit var checkoutPassData: DigitalCheckoutPassData
    private lateinit var customViewModel: DigitalTelcoCustomViewModel
    private lateinit var catalogMenuDetailViewModel: TelcoCatalogMenuDetailViewModel

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            customViewModel = viewModelProvider.get(DigitalTelcoCustomViewModel::class.java)
            catalogMenuDetailViewModel = viewModelProvider.get(TelcoCatalogMenuDetailViewModel::class.java)
        }
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    fun getInputFilterDataCollections() {
        customViewModel.getCustomData(GraphqlHelper.loadRawString(resources,
                R.raw.query_custom_digital_telco), getMapCustomData(),
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    protected abstract fun onSuccessCustomData(telcoData: TelcoCustomComponentData)

    protected abstract fun onErrorCustomData(error: Throwable)

    protected abstract fun getMapCustomData(): Map<String, kotlin.Any>

    protected abstract fun onLoadingMenuDetail(showLoading: Boolean)

    protected abstract fun getMapCatalogMenuDetail(): Map<String, kotlin.Any>

    protected abstract fun getMapFavNumbers(): Map<String, kotlin.Any>

    fun handleFocusClientNumber() {
        mainContainer.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS)
        mainContainer.setFillViewport(true)
        mainContainer.setFocusable(true)
        mainContainer.setFocusableInTouchMode(true)
        mainContainer.setOnTouchListener { view1, motionEvent ->
            if (view1 is DigitalClientNumberWidget) {
                view1.requestFocusFromTouch()
            } else {
                view1.clearFocus()
            }
            false
        }
    }

    fun getCatalogMenuDetail() {
        catalogMenuDetailViewModel.getCatalogMenuDetail(GraphqlHelper.loadRawString(resources,
                R.raw.query_telco_catalog_menu_detail), getMapCatalogMenuDetail(),
                this::onLoadingMenuDetail, this::onSuccessCatalogMenuDetail, this::onErrorCatalogMenuDetail)
        catalogMenuDetailViewModel.getFavNumbers(GraphqlHelper.loadRawString(resources,
                R.raw.temp_query_fav_number_digital), getMapFavNumbers(),
                this::onSuccessFavNumbers, this::onErrorFavNumbers)
    }

    fun onSuccessCatalogMenuDetail(catalogMenuDetailData: TelcoCatalogMenuDetailData) {
        renderPromoList(catalogMenuDetailData.catalogMenuDetailData.promos)
        renderRecentTransactions(catalogMenuDetailData.catalogMenuDetailData.recommendations)
        renderTicker(catalogMenuDetailData.catalogMenuDetailData.tickers)
    }

    fun onErrorCatalogMenuDetail(error: Throwable) {
        Toast.makeText(activity, "catalog menu detail " + error.message, Toast.LENGTH_SHORT).show()
    }

    fun onSuccessFavNumbers(data: TelcoRechargeFavNumberData) {
        setFavNumbers(data)
    }

    fun onErrorFavNumbers(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    abstract fun setFavNumbers(data: TelcoRechargeFavNumberData)

    fun renderTicker(tickers: List<TelcoTicker>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<String>()
            for (item in tickers) {
                messages.add(item.content)
            }
            tickerView.setListMessage(messages)
            tickerView.buildView()
            tickerView.visibility = View.VISIBLE
        } else {
            tickerView.visibility = View.GONE
        }
    }

    fun navigateContact() {
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

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            NetworkErrorHelper.showSnackbar(activity,
                    getString(R.string.error_message_contact_not_found))
        }
    }

    fun processToCart() {
        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }

    fun navigateToCart() {
        val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
        intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
        startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
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
                        val orderClientNumber = data.getParcelableExtra<Parcelable>(DigitalSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER)
                        handleCallbackSearchNumber(orderClientNumber as TelcoFavNumber)
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
                    if (userSession.isLoggedIn && ::checkoutPassData.isInitialized) {
                        navigateToCart()
                    }
                }
            }
        }
    }

    protected abstract fun showErrorCartDigital(message: String)

    protected abstract fun handleCallbackSearchNumber(orderClientNumber: TelcoFavNumber)

    protected abstract fun handleCallbackSearchNumberCancel()

    fun renderRecentTransactions(recentNumbers: List<TelcoRecommendation>) {
        if (recentNumbers.isNotEmpty()) {
            recentNumbersView.setListener(object : DigitalRecentTransactionWidget.ActionListener {
                override fun onClickRecentNumber(telcoRecommendation: TelcoRecommendation) {
                    onClickItemRecentNumber(telcoRecommendation)
                }
            })
            recentNumbersView.setRecentNumbers(recentNumbers)
            recentNumbersView.visibility = View.VISIBLE
        } else {
            recentNumbersView.visibility = View.GONE
        }
    }

    protected abstract fun onClickItemRecentNumber(telcoRecommendation: TelcoRecommendation)

    fun renderPromoList(promos: List<TelcoPromo>) {
        if (promos.isNotEmpty()) {
            promoListView.visibility = View.VISIBLE
            promoListView.setListener(object : DigitalPromoListWidget.ActionListener {
                override fun onCopiedPromoCode(voucherCode: String) {
                    val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                            CLIP_DATA_VOUCHER_CODE_DIGITAL, voucherCode
                    )
                    clipboard.primaryClip = clip
                    view?.run {
                        Toaster.showGreen(this,
                                getString(R.string.digital_voucher_code_already_copied), Snackbar.LENGTH_LONG)
                    }
                }

                override fun onClickItemPromo(telcoPromo: TelcoPromo) {
                    if (!TextUtils.isEmpty(telcoPromo.urlBannerPromo)) {
                        RouteManager.route(activity, telcoPromo.urlBannerPromo)
                    }
                }
            })
            promoListView.setPromoList(promos)
        } else {
            promoListView.visibility = View.GONE
        }
    }

    abstract fun setInputNumberFromContact(contactNumber: String)

    override fun onDestroy() {
        customViewModel.clear()
        super.onDestroy()
    }

    companion object {
        val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 77
        val REQUEST_CODE_CONTACT_PICKER = 78
        val REQUEST_CODE_LOGIN = 1010
        val REQUEST_CODE_CART_DIGITAL = 1090
        val CLIP_DATA_VOUCHER_CODE_DIGITAL = "digital_telco_clip_data_promo"
    }
}