package com.tokopedia.topupbills.telco.view.fragment

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.*
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import com.google.android.material.snackbar.Snackbar
import androidx.core.widget.NestedScrollView
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.*
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackPromo
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoCustomViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.TelcoCatalogMenuDetailViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsPromoListWidget
import com.tokopedia.common.topupbills.widget.TopupBillsRecentTransactionWidget
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
open abstract class DigitalBaseTelcoFragment : BaseDaggerFragment() {

    protected lateinit var mainContainer: NestedScrollView
    protected lateinit var tickerView: Ticker
    protected lateinit var recentNumbersWidget: TopupBillsRecentTransactionWidget
    protected lateinit var promoListWidget: TopupBillsPromoListWidget
    protected lateinit var checkoutPassData: DigitalCheckoutPassData
    protected lateinit var customViewModel: DigitalTelcoCustomViewModel
    protected lateinit var catalogMenuDetailViewModel: TelcoCatalogMenuDetailViewModel

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

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

    protected abstract fun onSuccessCustomData(telcoData: TelcoCustomComponentData)

    protected abstract fun onErrorCustomData(error: Throwable)

    open fun onSuccessCatalogMenuDetail(catalogMenuDetailData: TelcoCatalogMenuDetailData) {
        renderPromoList(catalogMenuDetailData.catalogMenuDetailData.promos)
        renderRecentTransactions(catalogMenuDetailData.catalogMenuDetailData.recommendations)
        renderTicker(catalogMenuDetailData.catalogMenuDetailData.tickers)
    }

    fun onErrorCatalogMenuDetail(error: Throwable) {
        Toast.makeText(activity, "catalog menu detail " + error.message, Toast.LENGTH_SHORT).show()
    }

    fun onSuccessFavNumbers(data: TopupBillsFavNumberData) {
        setFavNumbers(data)
    }

    fun onErrorFavNumbers(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    abstract fun setFavNumbers(data: TopupBillsFavNumberData)

    fun renderTicker(tickers: List<TopupBillsTicker>) {
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

    fun navigateContact() {
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

    fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    fun processToCart() {
        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            navigateToLoginPage()
        }
    }

    private fun navigateToCart() {
        if (::checkoutPassData.isInitialized) {
            checkoutPassData.idemPotencyKey = userSession.userId.generateRechargeCheckoutToken()
            val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CART_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, checkoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
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

    protected abstract fun showErrorCartDigital(message: String)

    protected abstract fun handleCallbackSearchNumber(orderClientNumber: TopupBillsFavNumberItem, inputNumberActionTypeIndex: Int)

    protected abstract fun handleCallbackSearchNumberCancel()

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
            recentNumbersWidget.visibility = View.VISIBLE
        } else {
            recentNumbersWidget.visibility = View.GONE
        }
    }

    fun handleFocusClientNumber() {
        mainContainer.setOnTouchListener { view1, motionEvent ->
            view1.clearFocus()
            false
        }
    }

    protected abstract fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation)

    private fun renderPromoList(promos: List<TopupBillsPromo>) {
        if (promos.isNotEmpty()) {
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
                                    getString(com.tokopedia.common.topupbills.R.string.digital_voucher_code_already_copied), Snackbar.LENGTH_LONG)
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
            promoListWidget.visibility = View.GONE
        }
    }

    abstract fun clickCopyOnPromoCode(promoId: Int)

    abstract fun setInputNumberFromContact(contactNumber: String)

    abstract fun onBackPressed()

    override fun onDestroy() {
        customViewModel.clear()
        catalogMenuDetailViewModel.clear()
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