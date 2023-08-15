package com.tokopedia.topupbills.telco.common.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberPageConfig
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.utils.covertContactUriToContactData
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.common.analytics.DigitalTopupEventTracking
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.viewmodel.SharedTelcoViewModel
import com.tokopedia.topupbills.telco.common.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.Job
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by nabillasabbaha on 23/05/19.
 */
abstract class DigitalBaseTelcoFragment : BaseTopupBillsFragment() {

    protected lateinit var pageContainer: RelativeLayout
    protected lateinit var tickerView: Ticker
    protected lateinit var appBarLayout: AppBarLayout
    protected lateinit var bannerImage: ImageView
    private lateinit var localCacheHandler: LocalCacheHandler
    private lateinit var viewModel: SharedTelcoViewModel
    protected var listMenu = mutableListOf<TelcoTabItem>()
    protected var operatorData: TelcoCatalogPrefixSelect =
        TelcoCatalogPrefixSelect(RechargeCatalogPrefixSelect())
    override var categoryId: Int = 0
        set(value) {
            field = value
            categoryName = topupAnalytics.getCategoryName(value)
        }

    protected var favNumberList = listOf<TopupBillsSearchNumberDataModel>()
    protected var seamlessFavNumberList = listOf<TopupBillsSeamlessFavNumberItem>()

    protected var actionTypeTrackingJob: Job? = null

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

    protected var loyaltyStatus = ""

    override fun initInjector() {
        getComponent(DigitalTelcoComponent::class.java).inject(this)
    }

    private fun subscribeUi() {
        viewModel.selectedRecentNumber.observe(
            viewLifecycleOwner,
            Observer {
                onClickItemRecentNumber(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            viewModel = viewModelProvider.get(SharedTelcoViewModel::class.java)
            localCacheHandler = LocalCacheHandler(context, TELCO_BASE_PREFERENCE_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAnimationAppBarLayout()
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
                messages.add(
                    TickerData(
                        item.name,
                        item.content,
                        when (item.type) {
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }
                    )
                )
            }
            context?.run {
                tickerView.addPagerView(TickerPagerAdapter(this, messages), messages)
            }
            tickerView.visibility = View.VISIBLE
        } else {
            tickerView.visibility = View.GONE
        }
    }

    protected fun navigateContact(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        categoryName: String,
        isSwitchChecked: Boolean
    ) {
        topupAnalytics.eventClickOnContactPickerHomepage()
        val isDeniedOnce = localCacheHandler.getBoolean(TELCO_PERMISSION_CHECKER_IS_DENIED, false)
        if (!isDeniedOnce && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermission(
                this,
                PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        navigateSavedNumber(clientNumber, dgCategoryIds, categoryName)
                        localCacheHandler.run {
                            putBoolean(TELCO_PERMISSION_CHECKER_IS_DENIED, true)
                            applyEditor()
                        }
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        permissionCheckerHelper.onNeverAskAgain(requireContext(), permissionText)
                    }

                    override fun onPermissionGranted() {
                        navigateSavedNumber(
                            clientNumber,
                            dgCategoryIds,
                            categoryName,
                            isSwitchChecked
                        )
                    }
                }
            )
        } else {
            navigateSavedNumber(
                clientNumber,
                dgCategoryIds,
                categoryName,
                isSwitchChecked
            )
        }
    }

    private fun navigateSavedNumber(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        categoryName: String,
        isSwitchChecked: Boolean = false
    ) {
        context?.let {
            val intent = TopupBillsPersoSavedNumberActivity.createInstance(
                it,
                clientNumber,
                dgCategoryIds,
                arrayListOf(),
                categoryName,
                isSwitchChecked,
                loyaltyStatus,
                FavoriteNumberPageConfig.TELCO
            )
            startActivityForResult(intent, REQUEST_CODE_DIGITAL_SAVED_NUMBER)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.run {
            permissionCheckerHelper.onRequestPermissionsResult(
                this,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                if (data != null) {
                    activity?.let {
                        val contactURI = data.data
                        val contact = contactURI?.covertContactUriToContactData(it.contentResolver)
                        setInputNumberFromContact(contact?.contactNumber ?: "")
                        setContactNameFromContact(contact?.givenName ?: "")
                    }
                }
            } else if (requestCode == REQUEST_CODE_DIGITAL_SEARCH_NUMBER) {
                if (data != null) {
                    val inputNumberActionType =
                        data.getIntExtra(EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, 0)
                    val orderClientNumber =
                        data.getParcelableExtra<Parcelable>(EXTRA_CALLBACK_CLIENT_NUMBER) as TopupBillsSearchNumberDataModel
                    handleCallbackAnySearchNumber(
                        "",
                        orderClientNumber.clientNumber,
                        orderClientNumber.productId,
                        orderClientNumber.categoryId,
                        inputNumberActionType
                    )
                } else {
                    handleCallbackAnySearchNumberCancel()
                }
            } else if (requestCode == REQUEST_CODE_DIGITAL_SAVED_NUMBER) {
                /* next improvement: only re-fetch favorite number if any favnum updated */
                if (data != null) {
                    val orderClientNumber =
                        data.getParcelableExtra<Parcelable>(EXTRA_CALLBACK_CLIENT_NUMBER) as TopupBillsSavedNumber

                    handleCallbackAnySearchNumber(
                        orderClientNumber.clientName,
                        orderClientNumber.clientNumber,
                        orderClientNumber.productId,
                        orderClientNumber.categoryId,
                        orderClientNumber.inputNumberActionTypeIndex
                    )
                } else {
                    handleCallbackAnySearchNumberCancel()
                }
                reloadSortFilterChip()
            } else if (requestCode == REQUEST_CODE_CART_DIGITAL) {
                if (data != null) {
                    if (data.hasExtra(DigitalExtraParam.EXTRA_MESSAGE)) {
                        val throwable = data.getSerializableExtra(DigitalExtraParam.EXTRA_MESSAGE) as Throwable
                        val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                            requireContext(),
                            throwable,
                            ErrorHandler.Builder()
                                .className(this::class.java.simpleName)
                                .build()
                        )
                        if (!TextUtils.isEmpty(errorMessage)) {
                            showErrorCartDigital(errorMessage.orEmpty())
                        }
                    }
                }
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                if (data != null) {
                    if (userSession.isLoggedIn) {
                        addToCart()
                    }
                }
            }
        }
    }

    fun getPrefixOperatorData() {
        viewModel.catalogPrefixSelect.observe(
            this,
            Observer {
                when (it) {
                    is Success -> onSuccessCustomData()
                    is Fail -> onErrorCustomData()
                }
            }
        )
        viewModel.getPrefixOperator(CommonTopupBillsGqlQuery.prefixSelectTelco, getTelcoMenuId())
    }

    private fun onSuccessCustomData() {
        this.operatorData = (viewModel.catalogPrefixSelect.value as Success).data
        renderProductFromCustomData()
    }

    private fun onErrorCustomData() {
        val errorData = (viewModel.catalogPrefixSelect.value as Fail).throwable
        val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
            requireContext(),
            errorData,
            ErrorHandler.Builder()
                .className(this::class.java.simpleName)
                .build()
        )

        view?.run {
            Toaster.build(
                this,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    protected fun validatePhoneNumber(
        operatorData: TelcoCatalogPrefixSelect,
        clientNumberWidget: DigitalClientNumberWidget
    ): Boolean {
        var isValid = true
        for (validation in operatorData.rechargeCatalogPrefixSelect.validations) {
            val phoneIsValid = Pattern.compile(validation.rule)
                .matcher(clientNumberWidget.getInputNumber()).matches()
            if (!phoneIsValid) {
                isValid = false
                clientNumberWidget.setErrorInputNumber(validation.message)
                break
            }
        }
        return isValid
    }

    override fun processEnquiry(data: TopupBillsEnquiryData) {
        // do nothing
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        super.processMenuDetail(data)
        (activity as? BaseSimpleActivity)?.updateTitle(data.menuLabel)
        renderTicker(data.tickers)
        sendOpenScreenTracking()
        initiateMenuTelco(data.recommendations, data.promos)
        loyaltyStatus = data.userPerso.loyaltyStatus
    }

    private fun initiateMenuTelco(
        recom: List<TopupBillsRecommendation>,
        promo: List<TopupBillsPromo>
    ) {
        listMenu.clear()
        var idTab = 1L
        if (recom.isNotEmpty()) {
            viewModel.setRecommendationTelco(recom)
            listMenu.add(TelcoTabItem(TelcoComponentName.RECENTS, idTab++))
        }
        if (promo.isNotEmpty()) {
            viewModel.setPromoTelco(promo)
            listMenu.add(TelcoTabItem(TelcoComponentName.PROMO, idTab++))
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
        val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(activity, error, ErrorHandler.Builder().build())

        NetworkErrorHelper.showEmptyState(
            activity,
            pageContainer,
            errMsg,
            "${getString(com.tokopedia.abstraction.R.string.msg_network_error_2)}. Kode Error: ($errCode)",
            null,
            DEFAULT_ICON_RES
        ) {
            getMenuDetail(getTelcoMenuId())
        }
    }

    override fun processFavoriteNumbers(data: List<TopupBillsSearchNumberDataModel>) {
        favNumberList = data
    }

    override fun processSeamlessFavoriteNumbers(
        data: TopupBillsSeamlessFavNumber,
        shouldRefreshInputNumber: Boolean
    ) {
        if (data.favoriteNumbers.isNotEmpty()) {
            getClientInputNumber().run {
                setInputType(InputType.TYPE_CLASS_TEXT)
            }
        }
        setSeamlessFavNumbers(data, shouldRefreshInputNumber)
    }

    override fun onEnquiryError(error: Throwable) {
        // do nothing
    }

    override fun onCatalogPluginDataError(error: Throwable) {
        // do nothing
    }

    override fun onFavoriteNumbersError(error: Throwable) {
        errorSetFavNumbers()
    }

    override fun onSeamlessFavoriteNumbersError(error: Throwable) {
        errorSetFavNumbers()
    }

    fun getFavoriteNumber(
        categoryIds: List<String>,
        shouldRefreshInputNumber: Boolean = true
    ) {
        getSeamlessFavoriteNumbers(categoryIds, shouldRefreshInputNumber)
    }

    override fun onCheckVoucherError(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(
                v,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.resources.common.R.string.general_label_ok)
            ).show()
        }
    }

    override fun onExpressCheckoutError(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(
                v,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.resources.common.R.string.general_label_ok)
            ).show()
        }
    }

    override fun showErrorMessage(error: Throwable) {
        view?.let { v ->
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            Toaster.build(v, errorMessage.orEmpty(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
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

    private fun setAnimationAppBarLayout() {
        val fadeIn = AlphaAnimation(0f, 1.0f)
        fadeIn.duration = FADE_IN_DURATION
        fadeIn.fillAfter = true

        val fadeOut = AlphaAnimation(1.0f, 0f)
        fadeOut.duration = FADE_OUT_DURATION
        fadeOut.fillAfter = true

        // initial appBar state is expanded
        (activity as? BaseTelcoActivity)?.setupAppBar()

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var lastOffset = -1
            var lastIsCollapsed = false

            override fun onOffsetChanged(p0: AppBarLayout?, verticalOffSet: Int) {
                if (lastOffset == verticalOffSet) return

                lastOffset = verticalOffSet
                if (abs(verticalOffSet) >= appBarLayout.totalScrollRange && !lastIsCollapsed) {
                    if (!getClientInputNumber().isErrorMessageShown()) {
                        // Collapsed
                        lastIsCollapsed = true
                    }
                } else if (verticalOffSet == 0 && lastIsCollapsed) {
                    // Expanded
                    lastIsCollapsed = false
                }
            }
        })
    }

    abstract fun onBackPressed()

    protected abstract fun getClientInputNumber(): DigitalClientNumberWidget

    protected abstract fun getTelcoMenuId(): Int

    protected abstract fun getTelcoCategoryId(): Int

    protected abstract fun renderPromoAndRecommendation()

    protected abstract fun renderProductFromCustomData(isDelayed: Boolean = false)

    protected abstract fun setupCheckoutData()

    protected abstract fun showErrorCartDigital(message: String)

    protected abstract fun setSeamlessFavNumbers(
        data: TopupBillsSeamlessFavNumber,
        shouldRefreshInputNumber: Boolean
    )

    protected abstract fun errorSetFavNumbers()

    protected abstract fun handleCallbackAnySearchNumber(
        clientName: String,
        clientNumber: String,
        productId: String,
        categoryId: String,
        inputNumberActionTypeIndex: Int
    )

    protected abstract fun handleCallbackAnySearchNumberCancel()

    protected abstract fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation)

    protected abstract fun setInputNumberFromContact(contactNumber: String)

    protected abstract fun setContactNameFromContact(contactName: String)

    protected abstract fun reloadSortFilterChip()

    override fun onDestroy() {
        listMenu.clear()
        super.onDestroy()
    }

    companion object {
        const val REQUEST_CODE_DIGITAL_SEARCH_NUMBER = 76
        const val REQUEST_CODE_DIGITAL_SAVED_NUMBER = 77
        const val REQUEST_CODE_CONTACT_PICKER = 78
        const val REQUEST_CODE_LOGIN = 1010

        const val REQUEST_CODE_CART_DIGITAL = 1090
        const val DEFAULT_ICON_RES = 0
        const val FADE_IN_DURATION: Long = 300
        const val FADE_OUT_DURATION: Long = 300

        const val TELCO_BASE_PREFERENCE_NAME = "telco_base_preferences"
        const val TELCO_PERMISSION_CHECKER_IS_DENIED = "telco_permission_checker_is_denied"
    }
}
