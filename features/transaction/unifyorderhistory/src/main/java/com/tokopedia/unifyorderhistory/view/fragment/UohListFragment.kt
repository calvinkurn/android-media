package com.tokopedia.unifyorderhistory.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.Transaction
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_DALAM_PROSES
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_DEALS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_DIGITAL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_EVENTS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_E_TIKET
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_GIFTCARDS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_HOTEL
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_INSURANCE
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_MARKETPLACE
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_MARKETPLACE_DALAM_PROSES
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_MODALTOKO
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_PESAWAT
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_PLUS
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_SEMUA_TRANSAKSI
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_TOKOFOOD
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_TRAIN
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_TRAVEL_ENTERTAINMENT
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_UOH_DELIVERED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_UOH_ONGOING
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_UOH_PROCESSED
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_UOH_SENT
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_UOH_WAITING_CONFIRMATION
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.SOURCE_FILTER
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.HOME_ENABLE_AUTO_REFRESH_UOH
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.helper.ViewHelper
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyorderhistory.R
import com.tokopedia.unifyorderhistory.analytics.UohAnalytics
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceAdd
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceAddRecommendation
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceClick
import com.tokopedia.unifyorderhistory.data.model.CancelOrderQueryParams
import com.tokopedia.unifyorderhistory.data.model.FlightQueryParams
import com.tokopedia.unifyorderhistory.data.model.LsPrintData
import com.tokopedia.unifyorderhistory.data.model.PmsNotification
import com.tokopedia.unifyorderhistory.data.model.TrainQueryParams
import com.tokopedia.unifyorderhistory.data.model.TrainResendEmailParam
import com.tokopedia.unifyorderhistory.data.model.UohEmptyState
import com.tokopedia.unifyorderhistory.data.model.UohFilterBundle
import com.tokopedia.unifyorderhistory.data.model.UohFilterCategory
import com.tokopedia.unifyorderhistory.data.model.UohFinishOrder
import com.tokopedia.unifyorderhistory.data.model.UohFinishOrderParam
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohListParam
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.FragmentUohListBinding
import com.tokopedia.unifyorderhistory.di.DaggerUohListComponent
import com.tokopedia.unifyorderhistory.di.UohListModule
import com.tokopedia.unifyorderhistory.domain.mapper.OrderDataMapper
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohConsts.ACTION_FINISH_ORDER
import com.tokopedia.unifyorderhistory.util.UohConsts.ALL_DATE
import com.tokopedia.unifyorderhistory.util.UohConsts.ALL_PRODUCTS
import com.tokopedia.unifyorderhistory.util.UohConsts.ALL_STATUS
import com.tokopedia.unifyorderhistory.util.UohConsts.ALL_STATUS_TRANSACTION
import com.tokopedia.unifyorderhistory.util.UohConsts.APPLINK_BASE
import com.tokopedia.unifyorderhistory.util.UohConsts.APP_LINK_TYPE
import com.tokopedia.unifyorderhistory.util.UohConsts.CTA_ATC
import com.tokopedia.unifyorderhistory.util.UohConsts.CUSTOMER_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.DALAM_PROSES
import com.tokopedia.unifyorderhistory.util.UohConsts.DATE_FORMAT_YYYYMMDD
import com.tokopedia.unifyorderhistory.util.UohConsts.DIKIRIM
import com.tokopedia.unifyorderhistory.util.UohConsts.DIPROSES
import com.tokopedia.unifyorderhistory.util.UohConsts.EE_PRODUCT_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.EE_PRODUCT_PRICE
import com.tokopedia.unifyorderhistory.util.UohConsts.END_DATE
import com.tokopedia.unifyorderhistory.util.UohConsts.EVENT_LABEL_CART_EXISTING
import com.tokopedia.unifyorderhistory.util.UohConsts.EVENT_LABEL_CART_REDIRECTION
import com.tokopedia.unifyorderhistory.util.UohConsts.E_TIKET
import com.tokopedia.unifyorderhistory.util.UohConsts.FLIGHT_STATUS_OK
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_FINISH_ORDER
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_FLIGHT_EMAIL
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_LS_FINISH
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_LS_LACAK
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_MP_ATC
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_MP_ATC_REDIRECTION
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_MP_CHAT
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_MP_EXTEND
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_MP_FINISH
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_MP_OCC
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_RECHARGE_BATALKAN
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_TRACK
import com.tokopedia.unifyorderhistory.util.UohConsts.GQL_TRAIN_EMAIL
import com.tokopedia.unifyorderhistory.util.UohConsts.MENUNGGU_KONFIRMASI
import com.tokopedia.unifyorderhistory.util.UohConsts.NOTES
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_BOUGHT_DATE
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_HELP_LINK_URL
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_INVOICE
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_INVOICE_URL
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_ORDER_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_SERIALIZABLE_LIST_PRODUCT
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_SHOP_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_SHOP_NAME
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_SOURCE_UOH
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_STATUS_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.PRODUCT_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.PRODUCT_NAME
import com.tokopedia.unifyorderhistory.util.UohConsts.PRODUCT_PRICE
import com.tokopedia.unifyorderhistory.util.UohConsts.QUANTITY
import com.tokopedia.unifyorderhistory.util.UohConsts.QUERY_PARAM_INVOICE
import com.tokopedia.unifyorderhistory.util.UohConsts.QUERY_PARAM_INVOICE_URL
import com.tokopedia.unifyorderhistory.util.UohConsts.REPLACE_ORDER_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.SHOP_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.START_DATE
import com.tokopedia.unifyorderhistory.util.UohConsts.STATUS_DIKIRIM
import com.tokopedia.unifyorderhistory.util.UohConsts.STATUS_DIPROSES
import com.tokopedia.unifyorderhistory.util.UohConsts.STATUS_MENUNGGU_KONFIRMASI
import com.tokopedia.unifyorderhistory.util.UohConsts.STATUS_TIBA_DI_TUJUAN
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_INDEX
import com.tokopedia.unifyorderhistory.util.UohConsts.TIBA_DI_TUJUAN
import com.tokopedia.unifyorderhistory.util.UohConsts.TRANSAKSI_BERLANGSUNG
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_ACTION_BUTTON_LINK
import com.tokopedia.unifyorderhistory.util.UohConsts.TYPE_ACTION_CANCEL_ORDER
import com.tokopedia.unifyorderhistory.util.UohConsts.URL_RESO
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_DEALS
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_EVENTS
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_FLIGHT
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_GIFTCARD
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_HOTEL
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_INSURANCE
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_MODALTOKO
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_PLUS
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_TOKOFOOD
import com.tokopedia.unifyorderhistory.util.UohConsts.VERTICAL_CATEGORY_TRAIN
import com.tokopedia.unifyorderhistory.util.UohConsts.WAREHOUSE_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.WEB_LINK_TYPE
import com.tokopedia.unifyorderhistory.util.UohUtils
import com.tokopedia.unifyorderhistory.view.activity.UohListActivity
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetKebabMenuAdapter
import com.tokopedia.unifyorderhistory.view.adapter.UohBottomSheetOptionAdapter
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohFilterOptionsBottomSheet
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohFinishOrderBottomSheet
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohKebabMenuBottomSheet
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohLsFinishOrderBottomSheet
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohSendEmailBottomSheet
import com.tokopedia.unifyorderhistory.view.viewmodel.UohListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.StringUtils
import timber.log.Timber
import java.io.Serializable
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.net.UnknownHostException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 29/06/20.
 */
@Keep
open class UohListFragment : BaseDaggerFragment(), RefreshHandler.OnRefreshHandlerListener, UohItemAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var uohItemAdapter: UohItemAdapter
    private lateinit var userSession: UserSession
    private lateinit var scrollRecommendationListener: EndlessRecyclerViewScrollListener
    private var refreshHandler: RefreshHandler? = null
    private var paramUohOrder = UohListParam()
    private var orderList: UohListOrder.Data.UohOrders = UohListOrder.Data.UohOrders()
    private var recommendationList: List<RecommendationWidget> = listOf()
    private var tdnBanner: TopAdsImageViewModel = TopAdsImageViewModel()
    private var responseFinishOrder: UohFinishOrder.Data.FinishOrderBuyer = UohFinishOrder.Data.FinishOrderBuyer()
    private var responseLsPrintFinishOrder: LsPrintData.Data.Oiaction = LsPrintData.Data.Oiaction()
    private var currFilterDateKey: String = ""
    private var currFilterStatusKey: String = ""
    private var currFilterCategoryKey: String = ""
    private var currFilterDateLabel: String = ""
    private var currFilterStatusLabel: String = ""
    private var currFilterCategoryLabel: String = ""
    private var currFilterType: Int = -1
    private var tempFilterDateKey: String = ""
    private var tempFilterStatusKey: String = ""
    private var tempFilterCategoryKey: String = ""
    private var tempFilterDateLabel: String = ""
    private var tempFilterStatusLabel: String = ""
    private var tempFilterCategoryLabel: String = ""
    private var tempFilterType: Int = -1
    private var tempStartDate: String = ""
    private var tempEndDate: String = ""
    private var chipDate: SortFilterItem? = null
    private var chipStatus: SortFilterItem? = null
    private var chipCategoryProduct: SortFilterItem? = null
    private var chosenStartDate: GregorianCalendar? = null
    private var chosenEndDate: GregorianCalendar? = null
    private var arrayFilterDate: Array<String>? = arrayOf()
    private var onLoadMore = false
    private var onLoadMoreRecommendation = false
    private var isFetchRecommendation = false
    private var currPage = 1
    private var currRecommendationListPage = 0
    private var isReset = false
    private var filterStatus = ""
    private var orderIdNeedUpdated = ""
    private var currIndexNeedUpdate = -1
    private var isFilterClicked = false
    private var isFirstLoad = true
    private var gson = Gson()
    private var activityOrderHistory = ""
    private var searchQuery = ""
    private var _arrayListStatusFilterBundle = arrayListOf<UohFilterBundle>()
    private var _arrayListCategoryProductFilterBundle = arrayListOf<UohFilterBundle>()
    private var _listParamAtcMulti = arrayListOf<AddToCartMultiParam>()
    private var _atcVerticalCategory = ""
    private var trackingQueue: TrackingQueue? = null
    private var _buttonAction = ""
    private var _atcOccParams: AddToCartOccMultiRequestParams? = null

    private var binding by autoClearedNullable<FragmentUohListBinding>()

    @SuppressLint("SimpleDateFormat")
    private val monthStringDateFormat = SimpleDateFormat("dd MMM yyyy")

    @SuppressLint("SimpleDateFormat")
    private val splitStringDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private val uohListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[UohListViewModel::class.java]
    }

    init {
        activity?.let {
            trackingQueue = TrackingQueue(it)
        }
    }

    companion object {
        const val PARAM_ACTIVITY_ORDER_HISTORY = "activity_order_history"
        const val PARAM_HOME = "home"
        private var CATEGORIES_DIGITAL = ""
        private var CATEGORIES_MP = ""
        private var CATEGORIES_TRAVELENT = ""
        private var CATEGORIES_KEUANGAN = ""
        private var CATEGORIES_TOKOFOOD = ""
        private var CATEGORIES_PLUS = ""

        private val CATEGORY_GROUP_MP = "belanja"
        private val CATEGORY_GROUP_RECHARGE = "recharge"
        private val CATEGORY_GROUP_TRAVEL = "travel"
        private val CATEGORY_GROUP_INVESTMENT = "investment"
        private val CATEGORY_GROUP_TOKONOW = "tokonow"
        private val CATEGORY_GROUP_GOTO_PLUS = "goto_plus"
        private val CATEGORY_GROUP_TOKOFOOD = "tokofood"
        private val CATEGORY_GROUP_OTHER = "other"

        @JvmStatic
        fun newInstance(bundle: Bundle): UohListFragment {
            return UohListFragment().apply {
                arguments = bundle.apply {
                    putString(
                        PARAM_ACTIVITY_ORDER_HISTORY,
                        this.getString(
                            PARAM_ACTIVITY_ORDER_HISTORY
                        )
                    )
                }
            }
        }

        const val URL_IMG_EMPTY_SEARCH_LIST = "https://images.tokopedia.net/img/android/uoh/uoh_empty_search_list.png"
        const val URL_IMG_EMPTY_ORDER_LIST = "https://images.tokopedia.net/img/android/uoh/uoh_empty_order_list.png"
        const val CREATE_REVIEW_APPLINK = "product-review/create/"
        const val CREATE_REVIEW_MESSAGE = "create_review_message"
        const val CREATE_REVIEW_REQUEST_CODE = 200
        const val REQUEST_CODE_LOGIN = 288
        const val MIN_KEYWORD_CHARACTER_COUNT = 3
        const val LABEL_0 = 0
        const val LABEL_1 = 1
        const val LABEL_2 = 2
        const val LABEL_3 = 3
        const val STATUS_600 = 600
        const val STATUS_200 = 200
        const val UOH_CANCEL_ORDER = 300
        const val LABEL_HELP_LINK = "Bantuan"
        const val MINUS_30 = -30
        const val MINUS_90 = -90
        private const val MIN_30_DAYS = -30
        const val INSTANT_CANCEL_BUYER_REQUEST = 100
        const val RESULT_MSG_INSTANT_CANCEL = "result_msg_instant"
        const val RESULT_CODE_INSTANT_CANCEL = "result_code_instant"
        const val RESULT_CODE_SUCCESS = 1
        const val EXTEND_ORDER_REQUEST_CODE = 400
        const val OPEN_ORDER_REQUEST_CODE = 500
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfigImpl? {
        return FirebaseRemoteConfigImpl(context)
    }

    private fun isAutoRefreshEnabled(): Boolean {
        return try {
            return getFirebaseRemoteConfig()?.getBoolean(HOME_ENABLE_AUTO_REFRESH_UOH) ?: false
        } catch (e: Exception) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(context)
        if (arguments?.getString(SOURCE_FILTER) != null) {
            filterStatus = arguments?.getString(SOURCE_FILTER).toString()
        }
        checkLogin()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUohListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    override fun onRefresh(view: View?) {
        refreshUohData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        launchAutoRefresh(isVisibleToUser)
    }

    private fun checkLogin() {
        if (userSession.isLoggedIn) {
            loadFilters()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    private fun launchAutoRefresh(isVisibleToUser: Boolean = true) {
        if (isVisibleToUser && isAutoRefreshEnabled()) {
            binding?.run {
                globalErrorUoh.gone()
                rvOrderList.visible()
                rvOrderList.scrollToPosition(0)
            }
            refreshUohData()
        }
    }

    private fun refreshUohData() {
        onLoadMore = false
        isFetchRecommendation = false
        onLoadMoreRecommendation = false
        currPage = 1
        currRecommendationListPage = 1
        loadOrderHistoryList("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                loadFilters()
            } else {
                activity?.finish()
            }
        } else if ((requestCode == CREATE_REVIEW_REQUEST_CODE)) {
            if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateReview(data?.getStringExtra(CREATE_REVIEW_MESSAGE) ?: getString(R.string.uoh_review_create_success_toaster, userSession.name))
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                onFailCreateReview(data?.getStringExtra(CREATE_REVIEW_MESSAGE) ?: getString(R.string.uoh_review_create_invalid_to_review))
            }
        } else if (requestCode == UOH_CANCEL_ORDER) {
            if (resultCode == INSTANT_CANCEL_BUYER_REQUEST) {
                val resultMsg = data?.getStringExtra(RESULT_MSG_INSTANT_CANCEL)
                val result = data?.getIntExtra(RESULT_CODE_INSTANT_CANCEL, 1)
                if (result == RESULT_CODE_SUCCESS) {
                    if (resultMsg != null) {
                        uohItemAdapter.showLoaderAtIndex(currIndexNeedUpdate)
                        showToaster(resultMsg, Toaster.TYPE_NORMAL)
                        loadOrderHistoryList(orderIdNeedUpdated)
                    }
                }
            } else {
                loadFilters()
            }
        } else if (requestCode == EXTEND_ORDER_REQUEST_CODE) {
            val isOrderExtend = data?.getBooleanExtra(ApplinkConstInternalOrder.OrderExtensionKey.IS_ORDER_EXTENDED, true)
            if (isOrderExtend == true) {
                // order extended
                uohItemAdapter.showLoaderAtIndex(currIndexNeedUpdate)
                loadOrderHistoryList(orderIdNeedUpdated)
            } else {
                // order not extended
                resetFilter()
                currIndexNeedUpdate = -1
                orderIdNeedUpdated = ""
                refreshHandler?.startRefresh()
                scrollRecommendationListener.resetState()
            }
            val toasterMessage = data?.getStringExtra(ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_MESSAGE)
            if (!toasterMessage.isNullOrBlank()) {
                val toasterType = data.getIntExtra(ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_TYPE, Toaster.TYPE_NORMAL)
                showToaster(toasterMessage, toasterType)
            }
        } else if (requestCode == OPEN_ORDER_REQUEST_CODE) {
            if (currIndexNeedUpdate > -1 && orderIdNeedUpdated.isNotEmpty()) {
                val item = uohItemAdapter.listTypeData.getOrNull(currIndexNeedUpdate)
                if (item != null &&
                    item.typeLayout == UohConsts.TYPE_ORDER_LIST &&
                    item.dataObject is UohListOrder.Data.UohOrders.Order &&
                    item.dataObject.orderUUID == orderIdNeedUpdated
                ) {
                    uohItemAdapter.showLoaderAtIndex(currIndexNeedUpdate)
                    loadOrderHistoryList(orderIdNeedUpdated)
                    return
                }
            }
            currIndexNeedUpdate = -1
            orderIdNeedUpdated = ""
        }
    }

    private fun loadFilters() {
        uohListViewModel.loadFilterCategory()
    }

    private fun initialLoadOrderHistoryList() {
        setInitialValue()
        loadOrderHistoryList("")
    }

    private fun setInitialValue() {
        paramUohOrder.page = 1
        arrayFilterDate = activity?.resources?.getStringArray(R.array.filter_date) as? Array<String>
    }

    private fun observingData() {
        observingFinishOrder()
        observingLsFinishOrder()
        observingRechargeSetFail()
        observingFilterCategory()
        observingOrderHistory()
        observingRecommendationList()
        observingAtc()
        observingAtcMulti()
        observingAtcOccMulti()
        observingFlightResendEmail()
        observingTrainResendEmail()
        observeTdnBanner()
        observeUohPmsCounter()
    }

    private fun observeTdnBanner() {
        uohListViewModel.tdnBannerResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    tdnBanner = it.data
                }
                is Fail -> {
                }
            }
        }
    }

    private fun observeUohPmsCounter() {
        uohListViewModel.getUohPmsCounterResult.observe(viewLifecycleOwner) {
            if (it is Success) {
                if (!paramUohOrder.hasActiveFilter()) {
                    val data = UohTypeData(
                        dataObject = it.data,
                        typeLayout = UohConsts.TYPE_PMS_BUTTON
                    )
                    uohItemAdapter.appendPmsButton(data) { position ->
                        binding?.rvOrderList?.smoothScrollToPosition(position)
                    }
                }
            }
        }
    }

    private fun prepareLayout() {
        binding?.run {
            refreshHandler = RefreshHandler(swipeRefreshLayout, this@UohListFragment)
            refreshHandler?.setPullEnabled(true)
            activityOrderHistory = arguments?.getString(PARAM_ACTIVITY_ORDER_HISTORY, "") as String

            statusbar.layoutParams.height = ViewHelper.getStatusBarHeight(activity)
            viewLifecycleOwner.lifecycle.addObserver(uohNavtoolbar)
            uohNavtoolbar.setupSearchbar(
                searchbarType = NavToolbar.Companion.SearchBarType.TYPE_EDITABLE,
                hints = arrayListOf(
                    HintData(getString(R.string.hint_cari_transaksi))
                ),
                editorActionCallback = { query ->
                    searchQuery = query
                    when {
                        searchQuery.isBlank() -> {
                            view?.let { view -> context?.let { context -> UohUtils.hideKeyBoard(context, view) } }
                            triggerSearch()
                        }
                        searchQuery.length in 1 until MIN_KEYWORD_CHARACTER_COUNT -> {
                            showToaster(getString(R.string.error_message_minimum_search_keyword), Toaster.TYPE_ERROR)
                        }
                        else -> {
                            view?.let { context?.let { _ -> uohNavtoolbar.hideKeyboard() } }
                            triggerSearch()
                        }
                    }
                }
            )
            var pageSource = ""
            if (activityOrderHistory != PARAM_HOME) {
                uohNavtoolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
                statusbar.visibility = View.GONE
            } else {
                pageSource = ApplinkConsInternalNavigation.SOURCE_HOME_UOH
            }
            val icons = IconBuilder(
                IconBuilderFlag(pageSource = pageSource)
            )
            icons.apply {
                addIcon(IconList.ID_MESSAGE) {}
                addIcon(IconList.ID_NOTIFICATION) {}
                addIcon(IconList.ID_CART) {}
                addIcon(IconList.ID_NAV_GLOBAL) {}
            }
            uohNavtoolbar.setIcon(icons)
        }

        uohItemAdapter = UohItemAdapter().apply {
            setActionListener(this@UohListFragment)
        }

        addEndlessScrollListener()
    }

    private fun getLimitDate(): GregorianCalendar {
        var returnDate = GregorianCalendar()
        val defDate = orderList.dateLimit
        val splitDefDate = defDate.split("-")
        if (splitDefDate.isNotEmpty() && splitDefDate.size == MIN_KEYWORD_CHARACTER_COUNT) {
            returnDate = stringToCalendar("${splitDefDate[0].toIntOrZero()}-${(splitDefDate[1].toIntOrZero() - 1)}-${splitDefDate[2].toIntOrZero()}")
        }
        return returnDate
    }

    private fun setDefaultDatesForDatePicker() {
        chosenStartDate = getLimitDate()
        chosenEndDate = GregorianCalendar()
    }

    private fun triggerSearch() {
        searchQuery.let { keyword ->
            resetFilter()
            paramUohOrder.searchableText = keyword
            refreshHandler?.startRefresh()
            scrollRecommendationListener.resetState()
            userSession.userId?.let { UohAnalytics.submitSearch(keyword, it) }
        }
    }

    private fun addEndlessScrollListener() {
        val glm = GridLayoutManager(activity, 2)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (uohItemAdapter.getItemViewType(position)) {
                    UohItemAdapter.LAYOUT_EMPTY_STATE -> 2
                    UohItemAdapter.LAYOUT_RECOMMENDATION_LIST -> 1
                    else -> 2
                }
            }
        }

        scrollRecommendationListener = object : EndlessRecyclerViewScrollListener(glm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                currentPage += 1
                if (isFetchRecommendation) {
                    onLoadMoreRecommendation = true
                    loadRecommendationList()
                } else {
                    onLoadMore = true
                    loadOrderHistoryList("")
                }
            }
        }

        binding?.run {
            globalErrorUoh.gone()
            rvOrderList.apply {
                visible()
                layoutManager = glm
                adapter = uohItemAdapter
                addOnScrollListener(scrollRecommendationListener)
            }
        }
    }

    private fun loadOrderHistoryList(uuid: String) {
        if (uuid.isNotEmpty()) {
            paramUohOrder.uUID = uuid
            paramUohOrder.page = 1
        } else {
            paramUohOrder.uUID = ""
            paramUohOrder.page = currPage
        }
        uohListViewModel.loadOrderList(paramUohOrder)
        if (!paramUohOrder.hasActiveFilter()) {
            uohListViewModel.loadPmsCounter(userSession.shopId)
        }
    }

    private fun loadRecommendationList() {
        isFetchRecommendation = true
        uohListViewModel.loadRecommendationList(currRecommendationListPage)
    }

    private fun observingFilterCategory() {
        uohListViewModel.filterCategoryResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    renderChipsFilter(
                        it.data.uohFilterCategoryData.v2Filters,
                        it.data.uohFilterCategoryData.categories
                    )
                    setDefaultDatesForDatePicker()
                    initialLoadOrderHistoryList()
                }
                is Fail -> {
                    showToaster(
                        ErrorHandler.getErrorMessage(context, it.throwable),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observingOrderHistory() {
        if (orderIdNeedUpdated.isEmpty() && !onLoadMore) uohItemAdapter.showLoader()
        uohListViewModel.orderHistoryListResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    orderList = it.data
                    if (orderList.orders.isNotEmpty()) {
                        if (orderIdNeedUpdated.isEmpty()) {
                            currPage += 1
                            renderOrderList()
                        } else {
                            if (currIndexNeedUpdate > -1) {
                                loop@ for (i in orderList.orders.indices) {
                                    if (orderList.orders[i].orderUUID.equals(
                                            orderIdNeedUpdated,
                                            true
                                        )
                                    ) {
                                        uohItemAdapter.updateDataAtIndex(
                                            currIndexNeedUpdate,
                                            orderList.orders[i]
                                        )
                                        orderIdNeedUpdated = ""
                                        break@loop
                                    }
                                }
                            }
                        }
                        UohAnalytics.viewOrderListPage()
                    } else {
                        if (currPage == 1) {
                            uohListViewModel.loadTdnBanner()
                            loadRecommendationList()
                        }
                    }
                }
                is Fail -> {
                    val errorType = when (it.throwable) {
                        is MessageErrorException -> null
                        is SocketTimeoutException, is UnknownHostException -> GlobalError.NO_CONNECTION
                        else -> GlobalError.SERVER_ERROR
                    }
                    if (errorType != null) {
                        binding?.run {
                            rvOrderList.gone()
                            globalErrorUoh.visible()
                            globalErrorUoh.setType(errorType)
                            globalErrorUoh.setActionClickListener {
                                initialLoadOrderHistoryList()
                            }
                        }
                    }

                    showToaster(
                        ErrorHandler.getErrorMessage(context, it.throwable),
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observingRecommendationList() {
        uohListViewModel.recommendationListResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    currRecommendationListPage += 1
                    recommendationList = it.data
                    if (recommendationList.isNotEmpty()) {
                        renderEmptyList()
                    }
                }
                is Fail -> {}
            }
        }
    }

    private fun observingFinishOrder() {
        uohListViewModel.finishOrderResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    responseFinishOrder = it.data
                    if (responseFinishOrder.success == 1) {
                        responseFinishOrder.message.firstOrNull()
                            ?.let { it1 -> showToaster(it1, Toaster.TYPE_NORMAL) }
                        loadOrderHistoryList(orderIdNeedUpdated)
                    } else {
                        if (responseFinishOrder.message.isNotEmpty()) {
                            responseFinishOrder.message.firstOrNull()
                                ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                        } else {
                            context?.getString(R.string.fail_cancellation)
                                ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    responseFinishOrder.message.firstOrNull()
                        ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                }
            }
        }
    }

    private fun observingAtcMulti() {
        uohListViewModel.atcMultiResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val msg = StringUtils.convertListToStringDelimiter(
                        result.data.atcMulti.buyAgainData.message,
                        ","
                    )
                    if (result.data.atcMulti.buyAgainData.success == 1) {
                        val product = result.data.atcMulti.buyAgainData.listProducts.getOrNull(0)
                        showToasterAtc(msg, Toaster.TYPE_NORMAL)
                        if (_buttonAction == GQL_MP_ATC_REDIRECTION) {
                            val intent = RouteManager.getIntent(context, ApplinkConst.CART)
                            product?.cartId?.let { cartId ->
                                intent.putExtra(Transaction.EXTRA_CART_ID, cartId.toString())
                            }
                            startActivity(intent)
                        }

                        // analytics
                        if (_listParamAtcMulti.isNotEmpty() && _atcVerticalCategory.isNotEmpty()) {
                            val arrayListProducts = arrayListOf<ECommerceAdd.Add.Products>()
                            _listParamAtcMulti.forEachIndexed { _, product ->
                                arrayListProducts.add(
                                    ECommerceAdd.Add.Products(
                                        name = product.productName,
                                        id = product.productId.toString(),
                                        price = product.productPrice.toString(),
                                        quantity = product.qty.toString(),
                                        dimension79 = product.shopId.toString()
                                    )
                                )
                            }

                            if (_buttonAction == GQL_MP_ATC) {
                                UohAnalytics.clickBeliLagiOnOrderCardMP(
                                    "",
                                    userSession.userId,
                                    arrayListProducts,
                                    _atcVerticalCategory,
                                    result.data.atcMulti.buyAgainData.listProducts.firstOrNull()?.cartId.toString()
                                )
                                UohAnalytics.sendClickBeliLagiButtonEvent(
                                    EVENT_LABEL_CART_EXISTING,
                                    arrayListProducts,
                                    result.data.atcMulti.buyAgainData.listProducts.firstOrNull()?.cartId.toString(),
                                    userSession.userId,
                                    _atcVerticalCategory
                                )
                            } else if (_buttonAction == GQL_MP_ATC_REDIRECTION) {
                                UohAnalytics.sendClickBeliLagiButtonEvent(
                                    EVENT_LABEL_CART_REDIRECTION,
                                    arrayListProducts,
                                    result.data.atcMulti.buyAgainData.listProducts.firstOrNull()?.cartId.toString(),
                                    userSession.userId,
                                    _atcVerticalCategory
                                )
                            }
                        }
                    } else {
                        showToaster(msg, Toaster.TYPE_ERROR)
                    }
                }
                is Fail -> {
                    context?.getString(R.string.fail_cancellation)
                        ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                    UohAnalytics.sendViewErrorToasterBeliLagiEvent()
                }
            }
        }
    }

    private fun observingAtcOccMulti() {
        uohListViewModel.atcOccMultiResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    RouteManager.route(context, ApplinkConst.OCC)

                    UohAnalytics.sendClickBeliLagiOccButtonEvent(
                        _atcOccParams,
                        userSession.userId,
                        _atcVerticalCategory
                    )
                }
                is Fail -> {
                    context?.getString(R.string.fail_cancellation)
                        ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                    UohAnalytics.sendViewErrorToasterBeliLagiEvent()
                }
            }
        }
    }

    private fun observingLsFinishOrder() {
        uohListViewModel.lsPrintFinishOrderResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    responseLsPrintFinishOrder = it.data.oiaction
                    if (responseLsPrintFinishOrder.status == STATUS_200) {
                        if (responseLsPrintFinishOrder.data.message.isNotEmpty()) {
                            showToaster(
                                responseLsPrintFinishOrder.data.message,
                                Toaster.TYPE_NORMAL
                            )
                        }
                        loadOrderHistoryList(orderIdNeedUpdated)
                    } else {
                        if (responseLsPrintFinishOrder.data.message.isNotEmpty()) {
                            showToaster(responseLsPrintFinishOrder.data.message, Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(R.string.fail_cancellation)
                                ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                        }
                    }
                }
                is Fail -> {
                    showToaster(responseLsPrintFinishOrder.data.message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingFlightResendEmail() {
        uohListViewModel.flightResendEmailResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    val flightEmailResponse = result.data.flightResendEmailV2
                    if (flightEmailResponse == null) {
                        showToaster(
                            getString(R.string.toaster_failed_send_email),
                            Toaster.TYPE_ERROR
                        )
                    } else {
                        if (flightEmailResponse.meta.status.equals(FLIGHT_STATUS_OK, true)) {
                            showToaster(
                                getString(R.string.toaster_succeed_send_email),
                                Toaster.TYPE_NORMAL
                            )
                        }
                    }
                }
                is Fail -> {
                    showToaster(getString(R.string.toaster_failed_send_email), Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingTrainResendEmail() {
        uohListViewModel.trainResendEmailResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val trainEmailResponse = it.data.trainResendBookingEmail
                    if (trainEmailResponse == null) {
                        showToaster(
                            getString(R.string.toaster_failed_send_email),
                            Toaster.TYPE_ERROR
                        )
                    } else {
                        if (trainEmailResponse.success) {
                            showToaster(
                                getString(R.string.toaster_succeed_send_email),
                                Toaster.TYPE_NORMAL
                            )
                        }
                    }
                }
                is Fail -> {
                    showToaster(getString(R.string.toaster_failed_send_email), Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observingRechargeSetFail() {
        uohListViewModel.rechargeSetFailResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val isSuccess = it.data.rechargeSetOrderToFail.attributes.isSuccess
                    if (isSuccess) {
                        loadOrderHistoryList(orderIdNeedUpdated)
                    } else {
                        showToaster(
                            it.data.rechargeSetOrderToFail.attributes.errorMessage,
                            Toaster.TYPE_ERROR
                        )
                    }
                }
                is Fail -> {
                    context?.getString(R.string.fail_cancellation)
                        ?.let { it1 -> showToaster(it1, Toaster.TYPE_ERROR) }
                }
            }
        }
    }

    private fun observingAtc() {
        uohListViewModel.atcResult.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.isStatusError()) {
                        val atcErrorMessage = it.data.getAtcErrorMessage()
                        if (atcErrorMessage != null) {
                            showToaster(atcErrorMessage, Toaster.TYPE_ERROR)
                        } else {
                            context?.getString(R.string.fail_cancellation)?.let { errorDefaultMsg ->
                                showToaster(
                                    errorDefaultMsg,
                                    Toaster.TYPE_ERROR
                                )
                            }
                        }
                    } else {
                        val successMsg =
                            StringUtils.convertListToStringDelimiter(it.data.data.message, ",")
                        showToasterAtc(successMsg, Toaster.TYPE_NORMAL)
                    }
                }
                is Fail -> {
                    context?.also { ctx ->
                        val throwable = it.throwable
                        var errorMessage = if (throwable is ResponseErrorException) {
                            throwable.message ?: ""
                        } else {
                            ErrorHandler.getErrorMessage(
                                ctx,
                                throwable,
                                ErrorHandler.Builder().withErrorCode(false)
                            )
                        }
                        if (errorMessage.isBlank()) {
                            errorMessage = ctx.getString(R.string.fail_cancellation)
                        }
                        showToaster(errorMessage, Toaster.TYPE_ERROR)
                    }
                }
            }
        }
    }

    private fun renderChipsFilter(
        filterDataList: List<UohFilterCategory.Data.UohFilterCategoryData.FilterV2>,
        categoryDataList: List<UohFilterCategory.Data.UohFilterCategoryData.Category>
    ) {
        if (filterStatus.isNotEmpty()) {
            var status = ""
            var statusLabel = ""
            when (filterStatus) {
                PARAM_DALAM_PROSES -> {
                    status = DALAM_PROSES
                    statusLabel = TRANSAKSI_BERLANGSUNG
                    paramUohOrder.createTimeStart = ""
                    paramUohOrder.createTimeEnd = ""
                }
                PARAM_E_TIKET -> {
                    status = E_TIKET
                    statusLabel = status
                    paramUohOrder.createTimeStart = ""
                    paramUohOrder.createTimeEnd = ""
                }
                PARAM_SEMUA_TRANSAKSI -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                }
                PARAM_MARKETPLACE -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                }
                PARAM_MARKETPLACE_DALAM_PROSES -> {
                    status = DALAM_PROSES
                    statusLabel = TRANSAKSI_BERLANGSUNG
                    paramUohOrder.verticalCategory = CATEGORIES_MP
                }
                PARAM_UOH_WAITING_CONFIRMATION -> {
                    status = STATUS_MENUNGGU_KONFIRMASI
                    statusLabel = MENUNGGU_KONFIRMASI
                    paramUohOrder.verticalCategory = CATEGORIES_MP
                }
                PARAM_UOH_PROCESSED -> {
                    status = STATUS_DIPROSES
                    statusLabel = DIPROSES
                    paramUohOrder.verticalCategory = CATEGORIES_MP
                }
                PARAM_UOH_SENT -> {
                    status = STATUS_DIKIRIM
                    statusLabel = DIKIRIM
                    paramUohOrder.verticalCategory = CATEGORIES_MP
                }
                PARAM_UOH_DELIVERED -> {
                    status = STATUS_TIBA_DI_TUJUAN
                    statusLabel = TIBA_DI_TUJUAN
                    paramUohOrder.verticalCategory = CATEGORIES_MP
                }
                PARAM_DIGITAL -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = CATEGORIES_DIGITAL
                }
                PARAM_EVENTS -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_EVENTS
                }
                PARAM_DEALS -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_DEALS
                }
                PARAM_PESAWAT -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_FLIGHT
                }
                PARAM_TRAIN -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_TRAIN
                }
                PARAM_GIFTCARDS -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_GIFTCARD
                }
                PARAM_INSURANCE -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_INSURANCE
                }
                PARAM_MODALTOKO -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_MODALTOKO
                }
                PARAM_HOTEL -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_HOTEL
                }
                PARAM_TRAVEL_ENTERTAINMENT -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = CATEGORIES_TRAVELENT
                }
                PARAM_TOKOFOOD -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_TOKOFOOD
                }
                PARAM_PLUS -> {
                    status = ""
                    statusLabel = ALL_STATUS_TRANSACTION
                    paramUohOrder.verticalCategory = VERTICAL_CATEGORY_PLUS
                }
                PARAM_UOH_ONGOING -> {
                    status = DALAM_PROSES
                    statusLabel = TRANSAKSI_BERLANGSUNG
                    paramUohOrder.verticalCategory = ""
                }
            }
            paramUohOrder.status = status
            currFilterType = UohConsts.TYPE_FILTER_STATUS
            currFilterStatusKey = status
            currFilterStatusLabel = statusLabel
        }

        val chips = arrayListOf<SortFilterItem>()

        renderChipsFilterStatus(chips, filterDataList)
        renderChipsFilterCategoryProducts(chips, categoryDataList)
        renderChipsFilterDate(chips)

        chipDate?.refChipUnify?.setChevronClickListener { onClickFilterDate() }
        chipStatus?.refChipUnify?.setChevronClickListener { onClickFilterStatus() }
        chipCategoryProduct?.refChipUnify?.setChevronClickListener { onClickFilterCategoryProduct() }
    }

    private fun renderChipsFilterStatus(chips: ArrayList<SortFilterItem>, filterDataList: List<UohFilterCategory.Data.UohFilterCategoryData.FilterV2>) {
        _arrayListStatusFilterBundle.clear()
        filterDataList.forEach { v2Filter ->
            val type = if (v2Filter.isPrimary) 0 else 1
            _arrayListStatusFilterBundle.add(UohFilterBundle(key = v2Filter.value, value = v2Filter.label, type = type))
        }

        val typeStatus = if (filterStatus.isEmpty() || filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) ||
            filterStatus.equals(PARAM_MARKETPLACE, true) ||
            filterStatus.equals(PARAM_DIGITAL, true) ||
            filterStatus.equals(PARAM_EVENTS, true) ||
            filterStatus.equals(PARAM_DEALS, true) ||
            filterStatus.equals(PARAM_PESAWAT, true) ||
            filterStatus.equals(PARAM_GIFTCARDS, true) ||
            filterStatus.equals(PARAM_INSURANCE, true) ||
            filterStatus.equals(PARAM_MODALTOKO, true) ||
            filterStatus.equals(PARAM_HOTEL, true) ||
            filterStatus.equals(PARAM_TRAIN, true) ||
            filterStatus.equals(PARAM_TRAVEL_ENTERTAINMENT, true) ||
            filterStatus.equals(PARAM_TOKOFOOD, true) ||
            filterStatus.equals(PARAM_PLUS, true)
        ) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }
        chipStatus = SortFilterItem(ALL_STATUS, typeStatus, ChipsUnify.SIZE_SMALL)
        val chipTitle = if (filterStatus.isNotEmpty() && !isReset && !filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true)) currFilterStatusLabel else ALL_STATUS
        chipStatus?.let { chip ->
            chip.title = chipTitle
            chip.apply {
                listener = { onClickFilterStatus() }
            }
            chips.add(chip)
        }
    }

    private fun renderChipsFilterCategoryProducts(chips: ArrayList<SortFilterItem>, categoryDataList: List<UohFilterCategory.Data.UohFilterCategoryData.Category>) {
        _arrayListCategoryProductFilterBundle.clear()
        _arrayListCategoryProductFilterBundle.add(UohFilterBundle(key = "", value = ALL_PRODUCTS, type = 0))
        // below is applinks which available on uoh
        var titleMP = ""
        var titleDigital = ""
        var titleTravel = ""
        var titleInvestment = ""
        var titleTokofood = ""
        var titlePlus = ""

        categoryDataList.forEach { category ->
            _arrayListCategoryProductFilterBundle.add(UohFilterBundle(key = category.value, value = category.label, desc = category.description, type = 0))

            // update selected categories when one of uoh applink is opened
            when {
                category.categoryGroup.equals(CATEGORY_GROUP_MP, true) -> {
                    CATEGORIES_MP = category.value
                    titleMP = category.label
                }
                category.categoryGroup.equals(CATEGORY_GROUP_RECHARGE, true) -> {
                    CATEGORIES_DIGITAL = category.value
                    titleDigital = category.label
                }
                category.categoryGroup.equals(CATEGORY_GROUP_TRAVEL, true) -> {
                    CATEGORIES_TRAVELENT = category.value
                    titleTravel = category.label
                }
                category.categoryGroup.equals(CATEGORY_GROUP_INVESTMENT, true) -> {
                    CATEGORIES_KEUANGAN = category.value
                    titleInvestment = category.label
                }
                category.categoryGroup.equals(CATEGORY_GROUP_TOKOFOOD, true) -> {
                    CATEGORIES_TOKOFOOD = category.value
                    titleTokofood = category.label
                }
                category.categoryGroup.equals(CATEGORY_GROUP_GOTO_PLUS, true) -> {
                    CATEGORIES_PLUS = category.value
                    titlePlus = category.label
                }
            }
        }

        val typeCategory = if (filterStatus.equals(PARAM_MARKETPLACE_DALAM_PROSES, true) ||
            filterStatus.equals(PARAM_UOH_WAITING_CONFIRMATION, true) ||
            filterStatus.equals(PARAM_UOH_PROCESSED, true) ||
            filterStatus.equals(PARAM_UOH_SENT, true) ||
            filterStatus.equals(PARAM_UOH_DELIVERED, true) ||
            filterStatus.equals(PARAM_DIGITAL, true) ||
            filterStatus.equals(PARAM_EVENTS, true) ||
            filterStatus.equals(PARAM_DEALS, true) ||
            filterStatus.equals(PARAM_PESAWAT, true) ||
            filterStatus.equals(PARAM_GIFTCARDS, true) ||
            filterStatus.equals(PARAM_INSURANCE, true) ||
            filterStatus.equals(PARAM_MODALTOKO, true) ||
            filterStatus.equals(PARAM_TRAIN, true) ||
            filterStatus.equals(PARAM_HOTEL, true) ||
            filterStatus.equals(PARAM_TRAVEL_ENTERTAINMENT, true) ||
            filterStatus.equals(PARAM_TOKOFOOD, true) ||
            filterStatus.equals(PARAM_PLUS, true)
        ) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }

        chipCategoryProduct = SortFilterItem(ALL_PRODUCTS, typeCategory, ChipsUnify.SIZE_SMALL)
        chipCategoryProduct?.listener = {
            onClickFilterCategoryProduct()
        }
        if (filterStatus.equals(PARAM_MARKETPLACE, true) ||
            filterStatus.equals(PARAM_SEMUA_TRANSAKSI, true) ||
            filterStatus.equals(PARAM_UOH_ONGOING, true) && !isReset
        ) {
            chipCategoryProduct?.title = ALL_PRODUCTS
        } else if ((
            filterStatus.equals(PARAM_MARKETPLACE_DALAM_PROSES, true) ||
                filterStatus.equals(PARAM_UOH_WAITING_CONFIRMATION, true) ||
                filterStatus.equals(PARAM_UOH_PROCESSED, true) ||
                filterStatus.equals(PARAM_UOH_SENT, true) ||
                filterStatus.equals(PARAM_UOH_DELIVERED, true)
            ) && !isReset
        ) {
            chipCategoryProduct?.title = titleMP
        } else if (filterStatus.equals(PARAM_DIGITAL, true) && !isReset) {
            chipCategoryProduct?.title = titleDigital
        } else if ((
            filterStatus.equals(PARAM_EVENTS, true) || filterStatus.equals(PARAM_DEALS, true) ||
                filterStatus.equals(PARAM_PESAWAT, true) || filterStatus.equals(PARAM_HOTEL, true) ||
                filterStatus.equals(PARAM_TRAIN, true) ||
                filterStatus.equals(PARAM_TRAVEL_ENTERTAINMENT, true)
            ) && !isReset
        ) {
            chipCategoryProduct?.title = titleTravel
        } else if ((
            filterStatus.equals(PARAM_GIFTCARDS, true) || filterStatus.equals(PARAM_INSURANCE, true) ||
                filterStatus.equals(PARAM_MODALTOKO, true)
            ) && !isReset
        ) {
            chipCategoryProduct?.title = titleInvestment
        } else if ((filterStatus.equals(PARAM_TOKOFOOD, true)) && !isReset) {
            chipCategoryProduct?.title = titleTokofood
        } else if ((filterStatus.equals(PARAM_PLUS, true)) && !isReset) {
            chipCategoryProduct?.title = titlePlus
        }
        chipCategoryProduct?.let { chips.add(it) }
    }

    private fun renderChipsFilterDate(chips: ArrayList<SortFilterItem>) {
        val typeDate = if (isReset || isFirstLoad || filterStatus.equals(PARAM_UOH_ONGOING, true)) {
            ChipsUnify.TYPE_NORMAL
        } else {
            ChipsUnify.TYPE_SELECTED
        }

        chipDate = SortFilterItem(ALL_DATE, typeDate, ChipsUnify.SIZE_SMALL)
        chipDate?.listener = {
            onClickFilterDate()
        }
        chipDate?.let {
            chips.add(it)
        }

        binding?.run {
            uohSortFilter.run {
                addItem(chips)
                sortFilterPrefix.setOnClickListener {
                    try {
                        val limitDate = splitStringDateFormat.parse(orderList.dateLimit)
                        limitDate?.let {
                            val limitDateStr = monthStringDateFormat.format(limitDate)
                            view?.let { view ->
                                context?.let { context -> UohUtils.hideKeyBoard(context, view) }
                            }
                            val resetMsg = activity?.resources?.getString(R.string.uoh_reset_filter_msg)?.replace(
                                UohConsts.DATE_LIMIT,
                                limitDateStr
                            )
                            resetMsg?.let { it1 -> showToaster(it1, Toaster.TYPE_NORMAL) }
                        }
                    } catch (exception: ParseException) {
                        Timber.d(exception)
                    }

                    resetFilter()
                    refreshHandler?.startRefresh()
                    scrollRecommendationListener.resetState()
                    userSession.userId?.let { it1 -> UohAnalytics.clickXChipsToClearFilter(it1) }
                }
            }
        }
    }

    private fun onClickFilterDate() {
        val arrayListDateFilterBundle = arrayListOf<UohFilterBundle>()
        arrayFilterDate?.let {
            if (it.isNotEmpty()) {
                it.forEachIndexed { index, s ->
                    arrayListDateFilterBundle.add(UohFilterBundle(key = index.toString(), value = s, type = 0))
                }
            }
        }

        tempFilterType = UohConsts.TYPE_FILTER_DATE
        if (tempFilterDateLabel.isEmpty()) tempFilterDateLabel = ALL_DATE
        if (tempFilterDateKey.isEmpty()) tempFilterDateKey = "0"

        val filterDateBottomSheet = UohFilterOptionsBottomSheet.newInstance(UohConsts.CHOOSE_DATE, true)
        if (filterDateBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val optionAdapter = UohBottomSheetOptionAdapter()
        optionAdapter.filterBundleList = arrayListDateFilterBundle
        optionAdapter.filterType = UohConsts.TYPE_FILTER_DATE
        optionAdapter.selectedKey = currFilterDateKey
        optionAdapter.isReset = isReset

        filterDateBottomSheet.setAdapter(optionAdapter)
        filterDateBottomSheet.setListener(object : UohFilterOptionsBottomSheet.UohFilterOptionBottomSheetListener {
            override fun onClickApply() {
                filterDateBottomSheet.dismiss()
                isFilterClicked = true
                isReset = false
                currFilterType = tempFilterType
                currFilterDateKey = tempFilterDateKey
                currFilterDateLabel = tempFilterDateLabel

                paramUohOrder.createTimeStart = tempStartDate
                paramUohOrder.createTimeEnd = tempEndDate

                if (tempFilterDateKey != "0") {
                    chipDate?.type = ChipsUnify.TYPE_SELECTED
                } else {
                    chipDate?.type = ChipsUnify.TYPE_NORMAL
                }
                val dateOption: String
                val labelTrackingDate: String
                if (currFilterDateKey.isNotEmpty() && currFilterDateKey == "3") {
                    if (paramUohOrder.createTimeStart.isEmpty()) {
                        paramUohOrder.createTimeStart = orderList.dateLimit
                    }
                    if (paramUohOrder.createTimeEnd.isEmpty()) {
                        paramUohOrder.createTimeEnd = Date().toFormattedString("yyyy-MM-dd")
                    }
                    val splitStartDate = paramUohOrder.createTimeStart.split('-')
                    val splitEndDate = paramUohOrder.createTimeEnd.split('-')
                    if (splitStartDate.isNotEmpty() && splitStartDate.size == MIN_KEYWORD_CHARACTER_COUNT && splitEndDate.isNotEmpty() && splitEndDate.size == MIN_KEYWORD_CHARACTER_COUNT) {
                        dateOption = "${splitStartDate[2]}/${splitStartDate[1]}/${splitStartDate[0]} - ${splitEndDate[2]}/${splitEndDate[1]}/${splitEndDate[0]}"
                        chipDate?.title = dateOption
                    }
                    labelTrackingDate = getString(R.string.filter_custom_date_title)
                } else {
                    dateOption = currFilterDateLabel

                    if (currFilterDateKey == "0") {
                        chipDate?.title = ALL_DATE
                        labelTrackingDate = ALL_DATE
                    } else {
                        chipDate?.title = currFilterDateLabel
                        labelTrackingDate = dateOption
                    }
                }

                userSession.userId?.let { it1 -> UohAnalytics.clickTerapkanOnDateFilterChips(labelTrackingDate, it1) }

                isFirstLoad = false
                refreshHandler?.startRefresh()
                scrollRecommendationListener.resetState()
            }

            override fun showDatePicker(flag: String) {
                context?.let { context ->
                    var minDate = GregorianCalendar()
                    var maxDate = GregorianCalendar()
                    var currDate = GregorianCalendar()

                    if (flag.equals(START_DATE, true)) {
                        chosenEndDate?.let { maxDate = it }
                        chosenStartDate?.let { currDate = it }
                        minDate = getLimitDate()
                    } else if (flag.equals(END_DATE, true)) {
                        chosenStartDate?.let { minDate = it }
                        chosenEndDate?.let { currDate = it }
                    }

                    val datePicker = DateTimePickerUnify(context, minDate, currDate, maxDate, null, DateTimePickerUnify.TYPE_DATEPICKER).apply {
                        datePickerButton.setOnClickListener {
                            val resultDate = getDate()
                            if (flag.equals(START_DATE, true)) {
                                chosenStartDate = resultDate as GregorianCalendar
                                filterDateBottomSheet.setStartDate(resultDate)
                                tempStartDate = UohUtils.calendarToStringFormat(resultDate, DATE_FORMAT_YYYYMMDD).toString()
                            } else {
                                chosenEndDate = resultDate as GregorianCalendar
                                filterDateBottomSheet.setEndDate(resultDate)
                                tempEndDate = UohUtils.calendarToStringFormat(resultDate, DATE_FORMAT_YYYYMMDD).toString()
                            }
                            dismiss()
                        }

                        if (flag.equals(START_DATE, true)) {
                            setTitle(context.getString(R.string.uoh_custom_start_date))
                        } else {
                            setTitle(context.getString(R.string.uoh_custom_end_date))
                        }
                        setCloseClickListener { dismiss() }
                    }
                    datePicker.show(childFragmentManager, "")
                }
            }

            override fun onOptionItemClick(label: String, value: String, filterType: Int) {
                isFilterClicked = true
                tempFilterType = filterType
                tempFilterDateKey = label
                tempFilterDateLabel = value
                if (label.isNotEmpty()) {
                    when {
                        label.toIntOrZero() == LABEL_0 -> {
                            filterDateBottomSheet.hideChooseDate()
                            tempStartDate = ""
                            tempEndDate = ""
                        }
                        label.toIntOrZero() == LABEL_1 -> {
                            filterDateBottomSheet.hideChooseDate()
                            val startDate = getCalculatedFormattedDate("yyyy-MM-dd", MIN_30_DAYS)
                            val endDate = Date().toFormattedString("yyyy-MM-dd")
                            tempStartDate = startDate.toString()
                            tempEndDate = endDate
                        }
                        label.toIntOrZero() == LABEL_2 -> {
                            filterDateBottomSheet.hideChooseDate()
                            val startDate = getCalculatedFormattedDate("yyyy-MM-dd", MINUS_90)
                            val endDate = Date().toFormattedString("yyyy-MM-dd")
                            tempStartDate = startDate.toString()
                            tempEndDate = endDate
                        }
                        label.toIntOrZero() == LABEL_3 -> {
                            var start = GregorianCalendar()
                            var end = GregorianCalendar()
                            chosenStartDate?.let { startDate -> start = startDate }
                            chosenEndDate?.let { endDate -> end = endDate }

                            tempStartDate = UohUtils.calendarToStringFormat(start, DATE_FORMAT_YYYYMMDD).toString()
                            tempEndDate = UohUtils.calendarToStringFormat(end, DATE_FORMAT_YYYYMMDD).toString()
                            filterDateBottomSheet.showChooseDate(start, end)
                        }
                    }
                }
                userSession.userId?.let { UohAnalytics.clickDateFilterChips(it) }
            }
        })
        filterDateBottomSheet.show(childFragmentManager)
    }

    private fun onClickFilterStatus() {
        val filterStatusBottomSheet = UohFilterOptionsBottomSheet.newInstance(
            UohConsts.CHOOSE_STATUS,
            false
        )
        if (filterStatusBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val optionAdapter = UohBottomSheetOptionAdapter()
        optionAdapter.filterBundleList = _arrayListStatusFilterBundle
        optionAdapter.filterType = UohConsts.TYPE_FILTER_STATUS
        optionAdapter.selectedKey = currFilterStatusKey
        optionAdapter.isReset = isReset

        filterStatusBottomSheet.setAdapter(optionAdapter)
        filterStatusBottomSheet.setListener(object : UohFilterOptionsBottomSheet.UohFilterOptionBottomSheetListener {
            override fun onClickApply() {
                filterStatusBottomSheet.dismiss()
                isFilterClicked = true
                isReset = false
                currFilterType = tempFilterType

                val labelTrackingStatus: String
                currFilterStatusKey = tempFilterStatusKey
                currFilterStatusLabel = tempFilterStatusLabel
                if (tempFilterStatusKey.isNotEmpty() && tempFilterStatusKey != ALL_STATUS_TRANSACTION) {
                    chipStatus?.type = ChipsUnify.TYPE_SELECTED
                    chipStatus?.title = currFilterStatusLabel
                    labelTrackingStatus = currFilterStatusLabel
                } else {
                    chipStatus?.type = ChipsUnify.TYPE_NORMAL
                    chipStatus?.title = ALL_STATUS
                    labelTrackingStatus = ALL_STATUS
                }
                userSession.userId?.let { it1 -> UohAnalytics.clickTerapkanOnStatusFilterChips(labelTrackingStatus, it1) }

                isFirstLoad = false
                refreshHandler?.startRefresh()
                scrollRecommendationListener.resetState()
            }

            override fun showDatePicker(flag: String) {}

            override fun onOptionItemClick(label: String, value: String, filterType: Int) {
                isFilterClicked = true
                tempFilterType = filterType
                tempFilterStatusKey = label
                tempFilterStatusLabel = value
                paramUohOrder.status = label
                userSession.userId?.let { UohAnalytics.clickStatusFilterChips(it) }
            }
        })
        filterStatusBottomSheet.show(childFragmentManager)

        tempFilterType = UohConsts.TYPE_FILTER_STATUS
        if (tempFilterStatusLabel.isEmpty()) tempFilterStatusLabel = ALL_STATUS_TRANSACTION
        if (tempFilterStatusKey.isEmpty()) tempFilterStatusKey = ""
    }

    private fun onClickFilterCategoryProduct() {
        val selectedKey: String
        tempFilterType = UohConsts.TYPE_FILTER_PRODUCT
        if (tempFilterCategoryLabel.isEmpty()) tempFilterCategoryLabel = ALL_PRODUCTS
        if (tempFilterCategoryKey.isEmpty()) tempFilterCategoryKey = ""

        if ((
            filterStatus.equals(PARAM_MARKETPLACE_DALAM_PROSES, true) ||
                filterStatus.equals(PARAM_UOH_WAITING_CONFIRMATION, true) ||
                filterStatus.equals(PARAM_UOH_PROCESSED, true) ||
                filterStatus.equals(PARAM_UOH_SENT, true) ||
                filterStatus.equals(PARAM_UOH_DELIVERED, true)
            ) &&
            !isReset
        ) {
            selectedKey = CATEGORIES_MP
        } else if (filterStatus.equals(PARAM_DIGITAL, true) && !isReset) {
            selectedKey = CATEGORIES_DIGITAL
        } else if ((
            filterStatus.equals(PARAM_EVENTS, true) ||
                filterStatus.equals(PARAM_DEALS, true) ||
                filterStatus.equals(PARAM_PESAWAT, true) ||
                filterStatus.equals(PARAM_HOTEL, true) ||
                filterStatus.equals(PARAM_TRAIN, true) ||
                filterStatus.equals(PARAM_TRAVEL_ENTERTAINMENT, true)
            ) &&
            !isReset
        ) {
            selectedKey = CATEGORIES_TRAVELENT
        } else if ((
            filterStatus.equals(PARAM_GIFTCARDS, true) ||
                filterStatus.equals(PARAM_INSURANCE, true) ||
                filterStatus.equals(PARAM_MODALTOKO, true)
            ) && !isReset
        ) {
            selectedKey = CATEGORIES_KEUANGAN
        } else if (filterStatus.equals(PARAM_TOKOFOOD, true) && !isReset) {
            selectedKey = CATEGORIES_TOKOFOOD
        } else if (filterStatus.equals(PARAM_PLUS, true) && !isReset) {
            selectedKey = CATEGORIES_PLUS
        } else {
            selectedKey = currFilterCategoryKey
        }

        val filterProductBottomSheet = UohFilterOptionsBottomSheet.newInstance(
            UohConsts.CHOOSE_PRODUCT,
            false
        )
        if (filterProductBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val optionAdapter = UohBottomSheetOptionAdapter()
        optionAdapter.filterBundleList = _arrayListCategoryProductFilterBundle
        optionAdapter.filterType = UohConsts.TYPE_FILTER_PRODUCT
        optionAdapter.selectedKey = selectedKey
        optionAdapter.isReset = isReset

        filterProductBottomSheet.setAdapter(optionAdapter)
        filterProductBottomSheet.setListener(object : UohFilterOptionsBottomSheet.UohFilterOptionBottomSheetListener {
            override fun onClickApply() {
                filterProductBottomSheet.dismiss()
                isFilterClicked = true
                isReset = false
                currFilterType = tempFilterType
                filterStatus = ""
                val labelTrackingCategory: String
                currFilterCategoryKey = tempFilterCategoryKey
                currFilterCategoryLabel = tempFilterCategoryLabel
                if (tempFilterCategoryKey.isNotEmpty() && tempFilterCategoryKey != ALL_PRODUCTS) {
                    chipCategoryProduct?.type = ChipsUnify.TYPE_SELECTED
                    chipCategoryProduct?.title = currFilterCategoryLabel
                    labelTrackingCategory = currFilterCategoryLabel
                } else {
                    chipCategoryProduct?.type = ChipsUnify.TYPE_NORMAL
                    chipCategoryProduct?.title = ALL_PRODUCTS
                    labelTrackingCategory = ALL_PRODUCTS
                }
                userSession.userId?.let { it1 -> UohAnalytics.clickTerapkanOnCategoryFilterChips(labelTrackingCategory, it1) }

                isFirstLoad = false
                refreshHandler?.startRefresh()
                scrollRecommendationListener.resetState()
            }

            override fun showDatePicker(flag: String) {}

            override fun onOptionItemClick(label: String, value: String, filterType: Int) {
                isFilterClicked = true
                tempFilterType = filterType
                tempFilterCategoryKey = label
                tempFilterCategoryLabel = value
                if (tempFilterCategoryKey == ALL_PRODUCTS) {
                    paramUohOrder.verticalCategory = ""
                } else {
                    paramUohOrder.verticalCategory = label
                }
                userSession.userId?.let { UohAnalytics.clickCategoryFilterChips(it) }
            }
        })
        filterProductBottomSheet.show(childFragmentManager)
    }

    private fun resetFilter() {
        filterStatus = ""

        currFilterDateKey = ""
        currFilterDateLabel = ""
        tempFilterDateKey = ""
        tempFilterDateLabel = ""

        currFilterStatusKey = ""
        currFilterStatusLabel = ""
        tempFilterStatusKey = ""
        tempFilterStatusLabel = ""

        currFilterCategoryKey = ""
        currFilterCategoryLabel = ""
        tempFilterCategoryKey = ""
        tempFilterCategoryLabel = ""

        tempStartDate = ""
        tempEndDate = ""

        isFilterClicked = false
        isReset = true
        binding?.run { uohSortFilter.resetAllFilters() }
        chipDate?.title = ALL_DATE
        chipStatus?.title = ALL_STATUS
        chipCategoryProduct?.title = ALL_PRODUCTS
        paramUohOrder = UohListParam()
        setInitialValue()
        setDefaultDatesForDatePicker()
    }

    private fun renderOrderList() {
        refreshHandler?.finishRefresh()

        val listOrder = arrayListOf<UohTypeData>()

        if (!onLoadMore && orderList.tickers.isNotEmpty()) {
            listOrder.add(UohTypeData(orderList, UohConsts.TYPE_TICKER))
        }

        orderList.orders.forEach {
            listOrder.add(UohTypeData(it, UohConsts.TYPE_ORDER_LIST))
        }

        if (!onLoadMore) {
            uohItemAdapter.addList(listOrder)
            scrollRecommendationListener.resetState()

            if (!paramUohOrder.hasActiveFilter()) {
                uohListViewModel.getUohPmsCounterResult.value?.let {
                    if (it is Success) {
                        val data = UohTypeData(dataObject = it.data, typeLayout = UohConsts.TYPE_PMS_BUTTON)
                        uohItemAdapter.appendPmsButton(data) { position ->
                            binding?.rvOrderList?.smoothScrollToPosition(position)
                        }
                    }
                }
            } else {
                uohItemAdapter.removePmsButton()
            }
        } else {
            uohItemAdapter.appendList(listOrder)
            scrollRecommendationListener.updateStateAfterGetData()
        }
    }

    private fun renderEmptyList() {
        refreshHandler?.finishRefresh()
        val listRecomm = arrayListOf<UohTypeData>()
        if (!onLoadMoreRecommendation) {
            val searchBarIsNotEmpty = searchQuery.isNotEmpty()
            var pmsButtonData: UohTypeData? = null
            val emptyStatus: UohEmptyState?
            when {
                searchBarIsNotEmpty -> {
                    emptyStatus = activity?.resources?.let { resource ->
                        UohEmptyState(
                            URL_IMG_EMPTY_SEARCH_LIST,
                            resource.getString(R.string.uoh_search_empty),
                            resource.getString(R.string.uoh_search_empty_desc),
                            false,
                            ""
                        )
                    }
                }
                paramUohOrder.status.isNotEmpty() || paramUohOrder.verticalCategory.isNotEmpty() -> {
                    emptyStatus = activity?.resources?.let { resource ->
                        UohEmptyState(
                            URL_IMG_EMPTY_ORDER_LIST,
                            resource.getString(R.string.uoh_filter_empty),
                            resource.getString(R.string.uoh_filter_empty_desc),
                            true,
                            resource.getString(R.string.uoh_filter_empty_btn)
                        )
                    }
                }
                else -> {
                    if (!paramUohOrder.hasActiveFilter()) {
                        val uohPmsCounterResult = uohListViewModel.getUohPmsCounterResult.value
                        pmsButtonData =
                            if (uohPmsCounterResult != null && uohPmsCounterResult is Success) {
                                UohTypeData(
                                    dataObject = uohPmsCounterResult.data,
                                    typeLayout = UohConsts.TYPE_PMS_BUTTON
                                )
                            } else {
                                UohTypeData(
                                    dataObject = PmsNotification(),
                                    typeLayout = UohConsts.TYPE_PMS_BUTTON
                                )
                            }
                    }
                    emptyStatus = activity?.resources?.let { resource ->
                        UohEmptyState(
                            URL_IMG_EMPTY_ORDER_LIST,
                            resource.getString(R.string.uoh_no_order),
                            resource.getString(R.string.uoh_no_order_desc),
                            true,
                            resource.getString(R.string.uoh_no_order_btn)
                        )
                    }
                }
            }
            pmsButtonData?.let { listRecomm.add(pmsButtonData) }
            emptyStatus?.let { emptyState -> UohTypeData(emptyState, UohConsts.TYPE_EMPTY) }?.let { uohTypeData -> listRecomm.add(uohTypeData) }
            listRecomm.add(UohTypeData(getString(R.string.uoh_recommendation_title), UohConsts.TYPE_RECOMMENDATION_TITLE))
            recommendationList.firstOrNull()?.recommendationItemList?.forEachIndexed { index, recommendationItem ->
                if (index == TDN_INDEX) listRecomm.add(UohTypeData(tdnBanner, UohConsts.TDN_BANNER))
                listRecomm.add(UohTypeData(recommendationItem, UohConsts.TYPE_RECOMMENDATION_ITEM))
            }
            uohItemAdapter.addList(listRecomm)
        } else {
            recommendationList.firstOrNull()?.recommendationItemList?.forEach {
                listRecomm.add(UohTypeData(it, UohConsts.TYPE_RECOMMENDATION_ITEM))
            }
            uohItemAdapter.appendList(listRecomm)
            scrollRecommendationListener.updateStateAfterGetData()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let { activity ->
            DaggerUohListComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .uohListModule(UohListModule(activity))
                .build()
                .inject(this)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    private fun stringToCalendar(stringParam: CharSequence): GregorianCalendar {
        val split = stringParam.split("-")
        return if (split.isNotEmpty() && split.size == LABEL_3) {
            GregorianCalendar(split[0].toIntOrZero(), split[1].toIntOrZero(), split[2].toIntOrZero())
        } else {
            GregorianCalendar()
        }
    }

    private fun showToaster(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            if (activity !is UohListActivity) {
                try {
                    context?.resources?.displayMetrics?.let {
                        toasterSuccess.toasterCustomBottomHeight = 48.dpToPx(it)
                    }
                } catch (t: Throwable) {
                    // ignore
                    Timber.d(t)
                }
            }
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, "").show()
        }
    }

    private fun showToasterAtc(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(
                v,
                message,
                Toaster.LENGTH_SHORT,
                type,
                CTA_ATC,
                View.OnClickListener {
                    RouteManager.route(context, ApplinkConst.CART)
                    userSession.userId?.let { it1 -> UohAnalytics.clickLihatButtonOnAtcSuccessToaster(it1) }
                }
            ).show()
        }
    }

    override fun onKebabMenuClicked(order: UohListOrder.Data.UohOrders.Order, orderIndex: Int) {
        val kebabMenuBottomSheet = UohKebabMenuBottomSheet.newInstance()
        if (kebabMenuBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        val kebabMenuAdapter = UohBottomSheetKebabMenuAdapter()
        kebabMenuAdapter._orderIndex = orderIndex
        kebabMenuAdapter.addList(order)

        kebabMenuBottomSheet.setAdapter(kebabMenuAdapter)
        kebabMenuBottomSheet.setListener(object : UohKebabMenuBottomSheet.UohKebabMenuBottomSheetListener {
            override fun onKebabItemClick(index: Int, orderData: UohListOrder.Data.UohOrders.Order, orderIndex: Int) {
                kebabMenuBottomSheet.dismiss()
                try {
                    val dotMenu = orderData.metadata.dotMenus[index]
                    if (dotMenu.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
                        if (dotMenu.appURL.contains(APPLINK_BASE)) {
                            RouteManager.route(context, URLDecoder.decode(dotMenu.appURL, UohConsts.UTF_8))
                        } else {
                            val linkUrl = if (dotMenu.appURL.contains(UohConsts.WEBVIEW)) {
                                dotMenu.webURL
                            } else {
                                dotMenu.appURL
                            }
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
                        }
                    } else if (dotMenu.actionType.equals(TYPE_ACTION_CANCEL_ORDER, true)) {
                        if (dotMenu.appURL.contains(APPLINK_BASE)) {
                            var helpLinkUrl = ""
                            currIndexNeedUpdate = index
                            orderIdNeedUpdated = orderData.orderUUID
                            orderData.metadata.dotMenus.forEach {
                                if (it.label.equals(LABEL_HELP_LINK)) {
                                    helpLinkUrl = it.webURL
                                }
                            }

                            val cancelOrderQueryParam = gson.fromJson(orderData.metadata.queryParams, CancelOrderQueryParams::class.java)
                            val intentCancelOrder = RouteManager.getIntent(context, URLDecoder.decode(dotMenu.appURL, UohConsts.UTF_8)).apply {
                                putExtra(PARAM_SHOP_NAME, cancelOrderQueryParam.shopName)
                                putExtra(PARAM_INVOICE, cancelOrderQueryParam.invoice)
                                putExtra(PARAM_SERIALIZABLE_LIST_PRODUCT, orderData.metadata.listProducts as Serializable?)
                                putExtra(PARAM_ORDER_ID, cancelOrderQueryParam.orderId)
                                putExtra(PARAM_SHOP_ID, cancelOrderQueryParam.shopId)
                                putExtra(PARAM_BOUGHT_DATE, orderData.metadata.paymentDateStr)
                                putExtra(PARAM_INVOICE_URL, cancelOrderQueryParam.invoiceUrl)
                                putExtra(PARAM_STATUS_ID, cancelOrderQueryParam.status)
                                putExtra(PARAM_SOURCE_UOH, true)
                                putExtra(PARAM_HELP_LINK_URL, helpLinkUrl)
                            }
                            startActivityForResult(intentCancelOrder, UOH_CANCEL_ORDER)
                        } else {
                            val linkUrl = if (dotMenu.appURL.contains(UohConsts.WEBVIEW)) {
                                dotMenu.webURL
                            } else {
                                dotMenu.appURL
                            }
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
                        }
                    } else {
                        when {
                            dotMenu.actionType.equals(GQL_FLIGHT_EMAIL, true) -> {
                                val sendEmailBottomSheet = UohSendEmailBottomSheet.newInstance()
                                if (sendEmailBottomSheet.isAdded || childFragmentManager.isStateSaved) return

                                sendEmailBottomSheet.setListener(object : UohSendEmailBottomSheet.UohSendEmailBottomSheetListener {
                                    override fun onEmailSent(email: String) {
                                        val flightQueryParam = gson.fromJson(orderData.metadata.queryParams, FlightQueryParams::class.java)
                                        val invoiceId = flightQueryParam.invoiceId
                                        uohListViewModel.doFlightResendEmail(invoiceId, email)
                                        userSession.userId?.let { userId -> UohAnalytics.clickKirimOnBottomSheetSendEmail(userId, orderData.verticalCategory) }
                                    }
                                })
                                sendEmailBottomSheet.show(childFragmentManager)
                            }
                            dotMenu.actionType.equals(GQL_TRAIN_EMAIL, true) -> {
                                val sendEmailBottomSheet = UohSendEmailBottomSheet.newInstance()
                                if (sendEmailBottomSheet.isAdded || childFragmentManager.isStateSaved) return

                                sendEmailBottomSheet.setListener(object : UohSendEmailBottomSheet.UohSendEmailBottomSheetListener {
                                    override fun onEmailSent(email: String) {
                                        val trainQueryParam = gson.fromJson(orderData.metadata.queryParams, TrainQueryParams::class.java)
                                        val invoiceId = trainQueryParam.invoiceId
                                        val param = TrainResendEmailParam(bookCode = invoiceId, email = email)
                                        uohListViewModel.doTrainResendEmail(param)
                                        userSession.userId?.let { userId -> UohAnalytics.clickKirimOnBottomSheetSendEmail(userId, orderData.verticalCategory) }
                                    }
                                })
                                sendEmailBottomSheet.show(childFragmentManager)
                            }
                            dotMenu.actionType.equals(GQL_MP_CHAT, true) -> {
                                doChatSeller(dotMenu.appURL, orderData)
                            }
                            dotMenu.actionType.equals(GQL_MP_ATC, true) -> {
                                atc(orderData)
                            }
                            dotMenu.actionType.equals(GQL_MP_OCC, true) -> {
                                atcOcc(orderData)
                            }
                            dotMenu.actionType.equals(GQL_MP_FINISH, true) -> {
                                orderIdNeedUpdated = orderData.orderUUID
                                doFinishOrder(
                                    orderIndex,
                                    orderData.verticalStatus,
                                    orderData.verticalID
                                )
                            }
                            dotMenu.actionType.equals(GQL_TRACK, true) -> {
                                val applinkTrack = ApplinkConst.ORDER_TRACKING.replace(
                                    REPLACE_ORDER_ID,
                                    orderData.verticalID
                                )
                                RouteManager.route(context, applinkTrack)
                            }
                            dotMenu.actionType.equals(GQL_LS_LACAK, true) -> {
                                val linkUrl = dotMenu.appURL
                                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
                            }
                            dotMenu.actionType.equals(GQL_MP_EXTEND, true) -> {
                                goToOrderExtension(order, index)
                            }
                        }
                    }
                    userSession.userId?.let { UohAnalytics.clickSecondaryOptionOnThreeDotsMenu(orderData.verticalCategory, dotMenu.label, it) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        kebabMenuBottomSheet.show(childFragmentManager)
        userSession.userId?.let { UohAnalytics.clickThreeDotsMenu(order.verticalCategory, it) }
    }

    private fun doFinishOrder(index: Int, status: String, verticalId: String) {
        val finishOrderBottomSheet = UohFinishOrderBottomSheet.newInstance(index, status, verticalId)
        if (finishOrderBottomSheet.isAdded || childFragmentManager.isStateSaved) return

        finishOrderBottomSheet.setListener(object : UohFinishOrderBottomSheet.UohFinishOrderBottomSheetListener {
            override fun onClickFinishOrder(index: Int, status: String, orderId: String) {
                finishOrderBottomSheet.dismiss()
                currIndexNeedUpdate = index
                uohItemAdapter.showLoaderAtIndex(index)

                var actionStatus = ""
                if (status.isNotEmpty() && status.toIntOrZero() < STATUS_600) actionStatus = ACTION_FINISH_ORDER

                val paramFinishOrder = userSession.userId?.let { it1 ->
                    UohFinishOrderParam(orderId = orderId, userId = it1, action = actionStatus)
                }
                if (paramFinishOrder != null) {
                    uohListViewModel.doFinishOrder(paramFinishOrder)
                }

                userSession.userId?.let { it1 -> UohAnalytics.clickSelesaiOnBottomSheetFinishTransaction(it1) }
            }

            override fun onClickAjukanKomplain(orderId: String) {
                finishOrderBottomSheet.dismiss()
                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_RESO.replace(REPLACE_ORDER_ID, orderId)))
                userSession.userId?.let { userId -> UohAnalytics.clickAjukanKomplainOnBottomSheetFinishTransaction(userId) }
            }
        })
        finishOrderBottomSheet.show(childFragmentManager)
    }

    override fun onListItemClicked(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        try {
            val detailUrl = order.metadata.detailURL
            var intent = Intent()
            if (detailUrl.appTypeLink == WEB_LINK_TYPE) {
                intent = RouteManager.getIntent(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(detailUrl.appURL, UohConsts.UTF_8)))
            } else if (detailUrl.appTypeLink == APP_LINK_TYPE) {
                intent = RouteManager.getIntent(context, URLDecoder.decode(detailUrl.appURL, UohConsts.UTF_8))
            }

            currIndexNeedUpdate = index
            orderIdNeedUpdated = order.orderUUID

            // analytics
            var jsonArray = JsonArray()
            if (order.metadata.listProducts.isNotEmpty()) {
                jsonArray = JsonParser().parse(order.metadata.listProducts).asJsonArray
            }
            val arrayListProducts = arrayListOf<ECommerceClick.Products>()
            var i = 0
            order.metadata.products.forEach {
                var eeProductId = ""
                var eeProductPrice = ""
                if (order.metadata.listProducts.isNotEmpty()) {
                    val objProduct = jsonArray.get(i).asJsonObject
                    eeProductId = objProduct.get(EE_PRODUCT_ID).asString
                    eeProductPrice = objProduct.get(EE_PRODUCT_PRICE).asString
                }
                arrayListProducts.add(
                    ECommerceClick.Products(
                        name = it.title,
                        id = eeProductId,
                        price = eeProductPrice,
                        list = "/order list - ${order.verticalCategory}",
                        position = index.toString()
                    )
                )
                i++
            }

            userSession.userId?.let { UohAnalytics.clickOrderCard(order.verticalCategory, it, arrayListProducts) }

            // requested as old flow (from old order list)
            UohAnalytics.orderDetailOpenScreenEvent()

            startActivityForResult(intent, OPEN_ORDER_REQUEST_CODE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActionButtonClicked(
        order: UohListOrder.Data.UohOrders.Order,
        index: Int,
        buttonIndex: Int
    ) {
        try {
            order.metadata.buttons.getOrNull(buttonIndex)?.let { button ->
                _buttonAction = button.actionType
                if (button.actionType.equals(TYPE_ACTION_BUTTON_LINK, true)) {
                    handleRouting(button.appURL)
                } else {
                    when {
                        button.actionType.equals(GQL_FINISH_ORDER, true) -> {
                            orderIdNeedUpdated = order.orderUUID
                            doFinishOrder(index, order.verticalStatus, order.verticalID)
                        }
                        button.actionType.equals(GQL_MP_ATC, true) -> {
                            atc(order)
                        }
                        button.actionType.equals(GQL_MP_ATC_REDIRECTION, true) -> {
                            atc(order)
                        }
                        button.actionType.equals(GQL_MP_OCC, true) -> {
                            atcOcc(order)
                        }
                        button.actionType.equals(GQL_TRACK, true) -> {
                            val applinkTrack = ApplinkConst.ORDER_TRACKING.replace(
                                REPLACE_ORDER_ID,
                                order.verticalID
                            )
                            RouteManager.route(context, applinkTrack)
                        }
                        button.actionType.equals(GQL_LS_FINISH, true) -> {
                            orderIdNeedUpdated = order.orderUUID
                            val lsFinishOrderBottomSheet =
                                UohLsFinishOrderBottomSheet.newInstance(index, order.verticalID)
                            if (lsFinishOrderBottomSheet.isAdded || childFragmentManager.isStateSaved) return

                            lsFinishOrderBottomSheet.setListener(object :
                                    UohLsFinishOrderBottomSheet.UohLsFinishOrderBottomSheetListener {
                                    override fun onClickLsFinishOrder(index: Int, orderId: String) {
                                        currIndexNeedUpdate = index
                                        uohItemAdapter.showLoaderAtIndex(index)
                                        uohListViewModel.doLsPrintFinishOrder(orderId)
                                    }
                                })
                            lsFinishOrderBottomSheet.show(childFragmentManager)
                        }
                        button.actionType.equals(GQL_LS_LACAK, true) -> {
                            val linkUrl = button.appURL
                            RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URLDecoder.decode(linkUrl, UohConsts.UTF_8)))
                        }
                        button.actionType.equals(GQL_RECHARGE_BATALKAN, true) -> {
                            currIndexNeedUpdate = index
                            orderIdNeedUpdated = order.orderUUID
                            if (order.verticalID.isNotEmpty()) {
                                uohListViewModel.doRechargeSetFail(order.verticalID.toIntOrZero())
                            }
                        }
                        button.actionType.equals(GQL_MP_EXTEND, true) -> {
                            goToOrderExtension(order, index)
                        }
                    }
                }
                userSession.userId?.let { UohAnalytics.clickPrimaryButtonOnOrderCard(order.verticalCategory, button.label, it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onEmptyResultResetBtnClicked() {
        resetFilter()
        refreshHandler?.startRefresh()
        scrollRecommendationListener.resetState()
        userSession.userId?.let { UohAnalytics.clickResetFilterOnEmptyFilterResult(it) }
    }

    override fun onTickerDetailInfoClicked(url: String) {
        if (url.contains(APPLINK_BASE)) {
            RouteManager.route(context, url)
        } else {
            if (url.contains(ApplinkConst.WEBVIEW)) {
                RouteManager.route(context, url)
            } else {
                RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
            }
        }
    }

    override fun trackViewOrderCard(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        var jsonArray = JsonArray()
        if (order.metadata.listProducts.isNotEmpty()) {
            jsonArray = JsonParser().parse(order.metadata.listProducts).asJsonArray
        }
        userSession.userId?.let { userId ->
            trackingQueue?.let { UohAnalytics.viewOrderCard(it, order, userId, jsonArray, index.toString()) }
        }
    }

    override fun onMulaiBelanjaBtnClicked() {
        RouteManager.route(context, ApplinkConst.HOME)
        userSession.userId?.let { UohAnalytics.clickMulaiBelanjaOnEmptyOrderList(it) }
    }

    override fun trackProductViewRecommendation(recommendationItem: RecommendationItem, index: Int) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val url = recommendationItem.trackerImageUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        var list = UohConsts.RECOMMENDATION_LIST_TRACK
        if (topAds) {
            list += UohConsts.RECOMMENDATION_LIST_TOPADS_TRACK
            activity?.let { TopAdsUrlHitter(it).hitImpressionUrl(UohListFragment::class.qualifiedName, url, productId, productName, imageUrl) }
        }

        userSession.userId?.let { userId ->
            trackingQueue?.let { UohAnalytics.productViewRecommendation(it, userId, recommendationItem, topAds, index.toString()) }
        }
    }

    override fun trackProductClickRecommendation(recommendationItem: RecommendationItem, index: Int) {
        val productId = recommendationItem.productId.toString()
        val topAds = recommendationItem.isTopAds
        val clickUrl = recommendationItem.clickUrl
        val productName = recommendationItem.name
        val imageUrl = recommendationItem.imageUrl

        userSession.userId?.let {
            UohAnalytics.productClickRecommendation(
                ECommerceClick.Products(
                    name = productName,
                    id = recommendationItem.productId.toString(),
                    price = recommendationItem.priceInt.toString(),
                    category = recommendationItem.categoryBreadcrumbs,
                    position = index.toString()
                ),
                topAds,
                it
            )
        }

        if (topAds) activity?.let { TopAdsUrlHitter(it).hitClickUrl(UohListFragment::class.qualifiedName, clickUrl, productId, productName, imageUrl) }
        onProductClicked(productId)
    }

    private fun onProductClicked(productId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_INFO, productId)
            startActivity(intent)
        }
    }

    override fun atcRecommendationItem(recommendationItem: RecommendationItem) {
        val atcParam = AddToCartRequestParams(
            productId = recommendationItem.productId,
            productName = recommendationItem.name,
            price = recommendationItem.priceInt.toString(),
            quantity = recommendationItem.quantity,
            shopId = recommendationItem.shopId,
            category = recommendationItem.categoryBreadcrumbs,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_RECOMMENDATION
        )
        uohListViewModel.doAtc(atcParam)

        // analytics
        trackAtcRecommendationItem(recommendationItem)
    }

    private fun trackAtcRecommendationItem(recommendationItem: RecommendationItem) {
        val productId = recommendationItem.productId.toString()
        val productName = recommendationItem.name
        val productPrice = recommendationItem.priceInt.toString()
        val productCategory = recommendationItem.categoryBreadcrumbs
        val qty = recommendationItem.quantity.toString()
        val imageUrl = recommendationItem.imageUrl
        val cartId = recommendationItem.cartId
        val isTopAds = recommendationItem.isTopAds
        val url = "${recommendationItem.clickUrl}&click_source=ATC_direct_click"

        val product = ECommerceAddRecommendation.Add.ActionField.Product(
            name = productName,
            id = productId,
            price = productPrice,
            category = productCategory,
            quantity = qty,
            dimension45 = cartId
        )
        val arrayListProduct = arrayListOf<ECommerceAddRecommendation.Add.ActionField.Product>()
        arrayListProduct.add(product)

        userSession.userId?.let { UohAnalytics.productAtcRecommendation(userId = it, listProduct = arrayListProduct, isTopads = isTopAds) }
        if (isTopAds) activity?.let { TopAdsUrlHitter(it).hitClickUrl(UohListFragment::class.qualifiedName, url, productId, productName, imageUrl) }
    }

    override fun onImpressionPmsButton() {
        UohAnalytics.impressionMenungguPembayaran()
    }

    override fun onPmsButtonClicked() {
        activity?.let {
            UohAnalytics.clickMenungguPembayaran()
            RouteManager.route(it, ApplinkConstInternalPayment.PMS_PAYMENT_LIST)
        }
    }

    private fun doChatSeller(appUrl: String, order: UohListOrder.Data.UohOrders.Order) {
        var invoiceCode = ""
        var invoiceUrl = ""
        var status = order.verticalStatus
        if (order.verticalStatus.contains("-")) {
            status = order.verticalStatus.split("-")[0]
        }

        try {
            val parser = JsonParser()
            val queryParams: JsonObject = parser.parse(order.metadata.queryParams).asJsonObject
            invoiceCode = queryParams.get(QUERY_PARAM_INVOICE).asString
            invoiceUrl = queryParams.get(QUERY_PARAM_INVOICE_URL).asString
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val intent = RouteManager.getIntent(context, URLDecoder.decode(appUrl, UohConsts.UTF_8))
        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, order.verticalID)
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, invoiceCode)
        if (order.metadata.products.isNotEmpty()) {
            intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE, order.metadata.products.firstOrNull()?.title)
            intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, order.metadata.products.firstOrNull()?.imageURL)
        }
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, order.metadata.paymentDateStr)
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL, invoiceUrl)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, status)
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, order.metadata.status.label)
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, order.metadata.totalPrice.value)
        startActivity(intent)
    }

    private fun atc(orderData: UohListOrder.Data.UohOrders.Order) {
        if (orderData.metadata.listProducts.isNotEmpty()) {
            _atcVerticalCategory = orderData.verticalCategory
            val jsonArray: JsonArray = JsonParser().parse(orderData.metadata.listProducts).asJsonArray
            for (x in 0 until jsonArray.size()) {
                val objParam = jsonArray.get(x).asJsonObject
                _listParamAtcMulti.add(
                    AddToCartMultiParam(
                        productId = objParam.get(PRODUCT_ID).asLong,
                        productName = objParam.get(PRODUCT_NAME).asString,
                        productPrice = objParam.get(PRODUCT_PRICE).asLong,
                        qty = objParam.get(QUANTITY).asInt,
                        notes = objParam.get(NOTES).asString,
                        shopId = objParam.get(SHOP_ID).asInt,
                        custId = objParam.get(CUSTOMER_ID).asInt,
                        warehouseId = objParam.get(WAREHOUSE_ID).asInt
                    )
                )
            }

            uohListViewModel.doAtcMulti(
                userSession.userId ?: "",
                GraphqlHelper.loadRawString(
                    activity?.resources,
                    com.tokopedia.atc_common.R.raw.mutation_add_to_cart_multi
                ),
                _listParamAtcMulti,
                _atcVerticalCategory
            )
        }
    }

    private fun atcOcc(orderData: UohListOrder.Data.UohOrders.Order) {
        _atcVerticalCategory = orderData.verticalCategory
        if (orderData.metadata.listProducts.isNotEmpty()) {
            _atcOccParams = OrderDataMapper.mapOrderDataToOccParams(orderData)
            _atcOccParams?.let { occParams ->
                uohListViewModel.doAtcOccMulti(occParams)
            }
        }
    }

    private fun handleRouting(applink: String) {
        if (applink.contains(CREATE_REVIEW_APPLINK)) {
            startActivityForResult(
                RouteManager.getIntent(
                    context,
                    URLDecoder.decode(applink, UohConsts.UTF_8)
                ),
                CREATE_REVIEW_REQUEST_CODE
            )
        } else {
            RouteManager.route(context, URLDecoder.decode(applink, UohConsts.UTF_8))
        }
    }

    private fun onSuccessCreateReview(message: String) {
        view?.let { Toaster.build(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(R.string.uoh_review_oke)).show() }
        refreshHandler?.startRefresh()
    }

    private fun onFailCreateReview(errorMessage: String) {
        view?.let { Toaster.build(it, errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.uoh_review_oke)).show() }
    }

    private fun goToOrderExtension(order: UohListOrder.Data.UohOrders.Order, index: Int) {
        val params = mapOf<String, Any>(ApplinkConstInternalOrder.PARAM_ORDER_ID to order.verticalID)
        val appLink = UriUtil.buildUriAppendParams(
            ApplinkConstInternalOrder.MARKETPLACE_INTERNAL_BUYER_ORDER_EXTENSION,
            params
        )
        val intent = RouteManager.getIntentNoFallback(context, appLink)?.apply {
            putExtra(ApplinkConstInternalOrder.OrderExtensionKey.IS_FROM_UOH, true)
        } ?: return
        orderIdNeedUpdated = order.orderUUID
        currIndexNeedUpdate = index
        startActivityForResult(intent, EXTEND_ORDER_REQUEST_CODE)
    }

    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }
}
