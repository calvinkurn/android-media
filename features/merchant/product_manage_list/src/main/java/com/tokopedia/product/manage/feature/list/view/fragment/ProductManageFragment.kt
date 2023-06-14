package com.tokopedia.product.manage.feature.list.view.fragment

import android.accounts.NetworkErrorException
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.centralizedpromo.CentralizedPromoApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_PRODUCT_ID
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATED_STATUS
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATED_STOCK
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATE_IS_STATUS_CHANGED
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATE_IS_STOCK_CHANGED
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_UPDATE_MESSAGE
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.REQUEST_CODE_CAMPAIGN_STOCK
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.product.manage.common.feature.uploadstatus.constant.UploadStatusType
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.ui.QuickEditVariantStockBottomSheet
import com.tokopedia.product.manage.common.session.ProductManageSession
import com.tokopedia.product.manage.common.util.ProductManageConfig
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.common.view.adapter.base.BaseProductManageAdapter
import com.tokopedia.product.manage.common.view.ongoingpromotion.bottomsheet.OngoingPromotionBottomSheet
import com.tokopedia.product.manage.databinding.FragmentProductManageSellerBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.activity.CampaignStockActivity
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.list.constant.ProductManageAnalytics.MP_PRODUCT_MANAGE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.BROADCAST_CHAT_CREATE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_IS_NEED_TO_RELOAD_DATA_SHOP_PRODUCT_LIST
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.PRODUCT_ID
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_ADD_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_EDIT_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_ETALASE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_PICK_ETALASE
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.REQUEST_CODE_STOCK_REMINDER
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.URL_TIPS_TRICK
import com.tokopedia.product.manage.feature.list.constant.ProductManageUrl
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.view.adapter.ProductManageListDiffutilAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.decoration.ProductListItemDecoration
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductManageMoreMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.listener.ProductManageListListener
import com.tokopedia.product.manage.feature.list.view.model.DeleteProductDialogType.MultipleProduct
import com.tokopedia.product.manage.feature.list.view.model.DeleteProductDialogType.SingleProduct
import com.tokopedia.product.manage.feature.list.view.model.FilterTabUiModel
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.ShowFilterTab
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.ProductManageEmptyModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.CreateBroadcastChat
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.CreateProductCoupon
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.Delete
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.Duplicate
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.Preview
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.RemoveFeaturedProduct
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.SeeTopAds
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.SetFeaturedProduct
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.SetTopAds
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel.StockReminder
import com.tokopedia.product.manage.feature.list.view.model.ProductMoreMenuModel
import com.tokopedia.product.manage.feature.list.view.model.ViewState.HideLoadingDialog
import com.tokopedia.product.manage.feature.list.view.model.ViewState.HideProgressDialog
import com.tokopedia.product.manage.feature.list.view.model.ViewState.ShowLoadingDialog
import com.tokopedia.product.manage.feature.list.view.model.ViewState.ShowProgressDialog
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.*
import com.tokopedia.product.manage.feature.list.view.ui.tab.ProductManageFilterTab
import com.tokopedia.product.manage.feature.list.view.viewmodel.ProductManageViewModel
import com.tokopedia.product.manage.feature.multiedit.ui.bottomsheet.ProductMultiEditBottomSheet
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getRetryMessage
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiEditToastMessage.getSuccessMessage
import com.tokopedia.product.manage.feature.multiedit.ui.toast.MultiVariantToastMessage
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment.ProductManageQuickEditPriceFragment
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui.QuickEditVariantPriceBottomSheet
import com.tokopedia.product.manage.feature.suspend.view.bottomsheet.SuspendReasonBottomSheet
import com.tokopedia.product.manage.feature.violation.view.bottomsheet.ViolationReasonBottomSheet
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.DELETED
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.INACTIVE
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.MODERATED
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.valueOf
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByCondition
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByKeyword
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByPage
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterByStatus
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption.FilterId.NOTIFY_ME_ONLY
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException
import javax.inject.Inject

open class ProductManageFragment :
    BaseListFragment<Visitable<*>, ProductManageAdapterFactoryImpl>(),
    ProductViewHolder.ProductViewHolderView,
    ProductMenuViewHolder.ProductMenuListener,
    ProductMultiEditBottomSheet.MultiEditListener,
    ProductManageFilterFragment.OnFinishedListener,
    ProductManageQuickEditPriceFragment.OnFinishedListener,
    ProductManageQuickEditStockFragment.OnFinishedListener,
    ProductManageMoreMenuViewHolder.ProductManageMoreMenuListener,
    ProductManageListListener,
    ProductManageAddEditMenuBottomSheet.AddEditMenuClickListener,
    ProductCampaignInfoListener,
    SellerHomeFragmentListener,
    ViolationReasonBottomSheet.Listener,
    SuspendReasonBottomSheet.Listener {

    private val defaultItemAnimator by lazy { DefaultItemAnimator() }

    @Inject
    lateinit var viewModel: ProductManageViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var productManageSession: ProductManageSession

    protected var binding by autoClearedNullable<FragmentProductManageSellerBinding>()

    private var shopDomain: String = ""
    private var goldMerchant: Boolean = false
    private var isOfficialStore: Boolean = false
    private var productListFeaturedOnlySize: Int = 0
    private var dialogFeaturedProduct: DialogUnify? = null
    private var productManageBottomSheet: ProductManageBottomSheet? = null
    private var filterProductBottomSheet: ProductManageFilterFragment? = null
    private var productManageMoreMenuBottomSheet: ProductManageMoreMenuBottomSheet? = null
    private var multiEditBottomSheet: ProductMultiEditBottomSheet? = null
    private var currentPositionStockReminderCoachMark: Int = -1
    private var navigationHomeMenuView: View? = null
    private var haveSetReminder = -1

    private val stockInfoBottomSheet by lazy { StockInformationBottomSheet(childFragmentManager) }

    private val productManageAddEditMenuBottomSheet by lazy {
        ProductManageAddEditMenuBottomSheet(
            sellerFeatureCarouselClickListener,
            this,
            childFragmentManager
        )
    }

    private val productManageListAdapter by lazy { adapter as BaseProductManageAdapter }
    private var defaultFilterOptions: List<FilterOption> = emptyList()
    private var itemsChecked: MutableList<ProductUiModel> = mutableListOf()
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var filterTab: ProductManageFilterTab? = null

    private var shouldEnableMultiEdit: Boolean = false
    private var shouldAddAsFeatured: Boolean = false
    private var extraCacheManagerId: String = ""
    private var sellerMigrationFeatureName: String = ""
    private var sellerFeatureCarouselClickListener: SellerFeatureCarousel.SellerFeatureClickListener =
        object : SellerFeatureCarousel.SellerFeatureClickListener {
            override fun onSellerFeatureClicked(item: SellerFeatureUiModel) {
                when (item) {
                    is SellerFeatureUiModel.MultiEditFeatureWithDataUiModel -> goToSellerAppProductManageMultiEdit()
                    is SellerFeatureUiModel.TopAdsFeatureWithDataUiModel -> goToSellerAppTopAds()
                    is SellerFeatureUiModel.FeaturedProductFeatureWithDataUiModel -> goToSellerAppProductManageThenAddAsFeatured(
                        item.data as ProductUiModel
                    )

                    is SellerFeatureUiModel.StockReminderFeatureWithDataUiModel -> goToSellerAppSetStockReminder(
                        item.data as ProductUiModel
                    )

                    is SellerFeatureUiModel.ProductManageSetVariantFeatureWithDataUiModel -> goToSellerAppAddProduct()
                    is SellerFeatureUiModel.BroadcastChatProductManageUiModel -> {
                        val product = item.data as ProductUiModel
                        goToCreateBroadCastChat(product)
                        ProductManageTracking.eventClickBroadcastChat(
                            userId = userSession.userId,
                            productId = product.id,
                            isCarousel = true
                        )
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }

    private val recyclerViewScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onCheckCoachMarkLabelGuarantee()
                onCheckCoachMarkStockReminder()
                onCheckCoachMarkMoreOption()
            }

            fun onCheckCoachMarkStockReminder() {
                if (coachMarkStockReminder?.isShowing.orFalse()) {
                    productManageLayoutManager?.let { layoutManager ->
                        val currentProductStockReminder =
                            layoutManager.findViewByPosition(currentPositionStockReminderCoachMark)
                                ?.findViewById<IconUnify>(R.id.imageStockReminder)
                        onDismissCoachMarkWhenScroll(
                            layoutManager,
                            coachMarkStockReminder,
                            currentProductStockReminder,
                            currentPositionStockReminderCoachMark
                        )
                    }
                } else {
                    productManageLayoutManager?.let {
                        val viewPosition =
                            getProductWithStockReminder(adapter.data.filterIsInstance<ProductUiModel>())

                        if (viewPosition != -1) {
                            val view = it.findViewByPosition(viewPosition)
                                ?.findViewById<IconUnify>(R.id.imageStockReminder)
                            view?.let { it1 ->
                                onShowCoachMarkStockWhenScroll(
                                    it,
                                    viewPosition,
                                    view = it1
                                )
                            }
                        }
                    }
                }
            }

            fun onCheckCoachMarkLabelGuarantee() {
                if (coachMarkLabelGuarantee?.isShowing.orFalse()) {
                    productManageLayoutManager?.let {
                        val currentMoreOptionButton =
                            it.findViewByPosition(Int.ZERO)
                                ?.findViewById<ImageUnify>(R.id.ivLabelGuaranteed)
                        onDismissCoachMarkWhenScroll(
                            it,
                            coachMarkLabelGuarantee,
                            currentMoreOptionButton,
                            Int.ZERO
                        )
                    }
                } else {
                    productManageLayoutManager?.let {
                        val viewPosition =
                            getProductWithStockAvailable(adapter.data.filterIsInstance<ProductUiModel>())
                        if (viewPosition == Int.ZERO) {
                            val view = it.findViewByPosition(Int.ZERO)
                                ?.findViewById<ImageUnify>(R.id.ivLabelGuaranteed)
                            view?.let { it1 ->
                                onShowCoachMarkStockWhenScroll(
                                    it,
                                    viewPosition,
                                    view = it1
                                )
                            }
                        }
                    }
                }
            }

            fun onCheckCoachMarkMoreOption() {
                if (coachMarkMoreOption?.isShowing.orFalse()) {
                    val layoutManager = productManageLayoutManager
                    layoutManager?.let {
                        val currentStockAvailable =
                            layoutManager.findViewByPosition(Int.ZERO)
                                ?.findViewById<IconUnify>(R.id.btnMoreOptions)
                        onDismissCoachMarkWhenScroll(
                            it,
                            coachMarkMoreOption,
                            currentStockAvailable,
                            Int.ZERO
                        )
                    }
                } else {
                    productManageLayoutManager?.let {
                        val viewPositionStockReminder =
                            getProductWithStockReminder(adapter.data.filterIsInstance<ProductUiModel>())
                        if (viewPositionStockReminder == -1) {
                            val view = it.findViewByPosition(Int.ZERO)
                                ?.findViewById<IconUnify>(R.id.btnMoreOptions)
                            view?.let { it1 ->
                                onShowCoachMarkStockWhenScroll(
                                    it,
                                    Int.ZERO,
                                    view = it1
                                )
                            }
                        }
                    }
                }
            }

            fun onDismissCoachMarkWhenScroll(
                layoutManager: LinearLayoutManager,
                coachMark: CoachMark2?,
                view: View?,
                positionCoachMark: Int
            ) {
                val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
                if (!coachMark?.isDismissed.orFalse() && (
                    positionCoachMark !in
                        firstVisibleIndex..lastVisibleIndex || (
                        view != null && getVisiblePercent(
                                view
                            ) == -1
                        )
                    )
                ) {
                    coachMark?.dismissCoachMark()
                }
            }

            fun onShowCoachMarkStockWhenScroll(
                layoutManager: LinearLayoutManager,
                viewPosition: Int,
                view: View
            ) {
                val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()

                if (viewPosition in firstVisibleIndex..lastVisibleIndex) {
                    view.let {
                        manageShowCoachMark(view)
                    }
                }
            }
        }
    }

    // these variables only use from seller migration (entry point broadcast chat)
    private var productId = ""
    private var productStock = 0
    private var isProductVariant = false
    private var isProductActive = false

    private var tickerIsReady = false
    private var shouldScrollToTop = false
    private var hasTickerClosed = false

    private var progressDialog: ProgressDialog? = null
    private var optionsMenu: Menu? = null

    private val ticker: Ticker?
        get() = binding?.layoutFragmentProductManage?.ticker?.root
    private val mainContainer: CoordinatorLayout?
        get() = binding?.layoutFragmentProductManage?.mainContainer
    private val errorPage: GlobalError?
        get() = binding?.layoutFragmentProductManage?.errorPage
    private val noAccessPage: GlobalError?
        get() = binding?.layoutFragmentProductManage?.noAccessPage
    private val swipeRefreshLayout: SwipeToRefresh?
        get() = binding?.layoutFragmentProductManage?.swipeRefreshLayout
    private val constraintLayout: ConstraintLayout?
        get() = binding?.layoutFragmentProductManage?.constraintLayoutProductManage
    private val tabSortFilter: SortFilter?
        get() = binding?.layoutFragmentProductManage?.tabSortFilter
    private val shimmerSortFilter: ConstraintLayout?
        get() = binding?.layoutFragmentProductManage?.shimmerSortFilter?.root
    private val textProductCount: Typography?
        get() = binding?.layoutFragmentProductManage?.textProductCount
    private val textMultipleSelect: Typography?
        get() = binding?.layoutFragmentProductManage?.textMultipleSelect
    private val multiSelectContainer: LinearLayout?
        get() = binding?.layoutFragmentProductManage?.multiSelectContainer
    private val checkBoxSelectAll: CheckboxUnify?
        get() = binding?.layoutFragmentProductManage?.checkBoxSelectAll
    private val btnMultiEdit: CardView?
        get() = binding?.layoutFragmentProductManage?.btnMultiEdit
    private val recyclerView: RecyclerView?
        get() = binding?.layoutFragmentProductManage?.recyclerView
    private val progressBar: LoaderUnify?
        get() = binding?.layoutFragmentProductManage?.progressBar
    private val interceptor: FrameLayout?
        get() = binding?.layoutFragmentProductManage?.interceptor
    private val searchBar: SearchBarUnify?
        get() = binding?.layoutFragmentProductManage?.searchBarProductManage

    private val productManageLayoutManager by lazy { recyclerView?.layoutManager as? LinearLayoutManager }

    private val coachMarkMoreOption: CoachMark2? by lazy {
        context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else {
                null
            }
        }
    }

    private val coachMarkStockReminder: CoachMark2? by lazy {
        context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else {
                null
            }
        }
    }

    private val coachMarkMoreMenuReminder: CoachMark2? by lazy {
        context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else {
                null
            }
        }
    }

    private val coachMarkLabelGuarantee: CoachMark2? by lazy {
        context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else {
                null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductManageSellerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        activity?.intent?.data?.run {
            shouldEnableMultiEdit = this.getBooleanQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_ENABLE_MULTI_EDIT,
                false
            )
            shouldAddAsFeatured = this.getBooleanQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_ADD_AS_FEATURED,
                false
            )
            extraCacheManagerId =
                this.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID)
                    .orEmpty()
            sellerMigrationFeatureName =
                this.getQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME)
                    .orEmpty()
            productId =
                this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ID).orEmpty()
            productStock =
                this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_STOCK)
                    .toIntOrZero()
            isProductVariant =
                this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_VARIANT)
                    .toBoolean()
            isProductActive =
                this.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ACTIVE)
                    .toBoolean()
        }
    }

    fun setNavigationHomeMenuView(view: View?) {
        this.navigationHomeMenuView = view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            val bottomSheet = childFragmentManager.findFragmentByTag(BOTTOM_SHEET_TAG)
            when (bottomSheet) {
                is ProductManageFilterFragment -> bottomSheet.setOnFinishedListener(this@ProductManageFragment)
                is ProductManageQuickEditStockFragment -> bottomSheet.setOnFinishedListener(this@ProductManageFragment)
                is ProductManageQuickEditPriceFragment -> bottomSheet.setOnFinishedListener(this@ProductManageFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun clearAllData() {
        isLoadingInitialData = true
        recyclerView?.post {
            super.clearAllData()
        }
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? = swipeRefreshLayout

    override fun getRecyclerView(view: View?): RecyclerView? = recyclerView

    private fun initView() {
        setupInterceptor()
        setupSearchBar()
        setupProductList()
        setupProgressDialogVariant()
        setupFiltersTab()
        setupMultiSelect()
        setupSelectAll()
        setupErrorPage()
        setupNoAccessPage()
        renderCheckedView()

        observeShopInfo()
        observeDeleteProduct()
        observeProductListFeaturedOnly()
        observeProductList()
        observeFilterTabs()
        observeMultiSelect()
        observeProductVariantBroadcast()
        observeUploadStatus()

        observeEditPrice()
        observeEditStock()
        observeMultiEdit()
        observeGetPopUpInfo()

        observeSetFeaturedProduct()
        observeViewState()
        observeFilter()

        observeEditVariantPrice()
        observeEditVariantStock()
        observeProductManageAccess()
        observeDeleteProductDialog()
        observeOptionsMenu()

        observeShopStatus()

        getProductManageAccess()
        setupDialogFeaturedProduct()

        context?.let { UpdateShopActiveWorker.execute(it) }
    }

    private fun setupProgressDialogVariant() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog?.setMessage(getString(R.string.message_loading_progress_dialog))
    }

    private fun showProgressDialogVariant() {
        progressDialog?.show()
    }

    private fun hideProgressDialogVariant() {
        progressDialog?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        optionsMenu = menu

        val menuViewId = R.menu.menu_product_manage

        menu.clear()
        inflater.inflate(menuViewId, menu)

        for (i in 0 until menu.size()) {
            menu.getItem(i)?.let { menuItem ->
                menuItem.actionView?.setOnClickListener {
                    onOptionsItemSelected(menuItem)
                }
            }
        }
        showHideOptionsMenu()
        disableOrEnableOptionMenuAddProduct(
            viewModel.shopStatus.value?.isOnModerationMode().orFalse()
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_product_menu) {
            if (ProductManageConfig.IS_SELLER_APP) {
                val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_ADD)
                startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
                ProductManageTracking.eventAddProduct()
            } else {
                showAddEditMenuBottomSheet()
            }
        } else if (item.itemId == R.id.action_more_menu) {
            showMoreMenuBottomSheet()
            ProductManageTracking.eventClickMoreMenuEllipses()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMoreMenuClicked(menu: ProductMoreMenuModel) {
        if (menu.title == getString(R.string.product_manage_shop_showcase_more_menu_text)) {
            // goto showcase list
            val showcaseListIntent = RouteManager.getIntent(
                context,
                ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST
            )
            val showcaseListBundle = Bundle().apply {
                putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, userSession.shopId)
                putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
                putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
                putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
            }
            showcaseListIntent.putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, showcaseListBundle)
            startActivityForResult(showcaseListIntent, REQUEST_CODE_ETALASE)
            productManageMoreMenuBottomSheet?.dismiss()
            ProductManageTracking.eventClickMoreMenuShopShowcase()
        }
    }

    private fun onClickMoreFilter() {
        showFilterBottomSheet()

        val tabName = getString(R.string.product_manage_filter)
        ProductManageTracking.eventInventory(tabName)
    }

    private fun onClickFilterTab(filter: FilterTabUiModel) {
        resetMultiSelect()
        disableMultiSelect()
        renderCheckedView()
        getFiltersTab(withDelay = true)
        isLoadingInitialData = true
        shouldScrollToTop = true
        endlessRecyclerViewScrollListener.resetState()
        getProductList(isRefreshFromSortFilter = true)

        val tabName = getString(filter.titleId, filter.count)
        ProductManageTracking.eventInventory(tabName)
    }

    private fun animateProductTicker(isEnter: Boolean) {
        Handler().postDelayed({
            val shouldAnimateTicker =
                (isEnter && tickerIsReady && (ticker?.visibility == View.INVISIBLE || ticker?.visibility == View.GONE)) || !isEnter
            if (adapter.data.isNotEmpty() && shouldAnimateTicker) {
                val enterValue: Float
                val exitValue: Float
                if (isEnter) {
                    enterValue = 0f
                    exitValue = 1f
                } else {
                    enterValue = 1f
                    exitValue = 0f
                }
                ticker?.run {
                    val marginTop = TICKER_MARGIN_TOP.toPx()
                    val height = height.toFloat().orZero() + marginTop
                    translationY = enterValue * height
                    show()

                    val animator = ValueAnimator.ofFloat(enterValue, exitValue).apply {
                        duration = TICKER_ENTER_LEAVE_ANIMATION_DURATION
                        addUpdateListener { valueAnimator ->
                            context?.let {
                                val animValue = (valueAnimator.animatedValue as? Float).orZero()
                                val translation = animValue * height
                                translationY = translation
                                alpha = animValue
                                translateTickerConstrainedLayout(translation)
                            }
                        }
                        addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(p0: Animator) {}

                            override fun onAnimationEnd(p0: Animator) {
                                tickerIsReady = false
                                if (!isEnter) {
                                    this@run.invisible()
                                }
                            }

                            override fun onAnimationCancel(p0: Animator) {}

                            override fun onAnimationRepeat(p0: Animator) {}
                        })
                    }

                    animator.start()
                }
            }
        }, TICKER_ENTER_LEAVE_ANIMATION_DELAY)
    }

    private fun translateTickerConstrainedLayout(translation: Float) {
        mainContainer?.translationY = translation
        errorPage?.translationY = translation
        noAccessPage?.translationY = translation
        val params = (swipeToRefresh?.layoutParams as? ViewGroup.MarginLayoutParams)
        params?.bottomMargin = translation.toInt()
        swipeRefreshLayout?.layoutParams = params
    }

    override fun editMultipleProductsEtalase() {
        goToEtalasePicker()
        ProductManageTracking.eventBulkSettingsMoveEtalase()
    }

    override fun editMultipleProductsInActive() {
        val totalDTProductSelected = itemsChecked.filter {
            it.isDTInbound && !it.isCampaign
        }.size
        val totalProductSelected = itemsChecked.size
        val isAllDT = (totalDTProductSelected == totalProductSelected)
        if (isAllDT) {
            showEditDTProductsInActiveConfirmationDialog()
        } else {
            showEditProductsInActiveConfirmationDialog()
        }
        ProductManageTracking.eventBulkSettingsDeactive()
    }

    override fun deleteMultipleProducts() {
        viewModel.onDeleteMultipleProducts()
        ProductManageTracking.eventBulkSettingsDeleteBulk()
    }

    override fun onFinish(selectedData: FilterOptionWrapper) {
        val isNotifyMe = selectedData.filterOptions.find { it.id == NOTIFY_ME_ONLY } != null
        if (isNotifyMe) {
            ProductManageTracking.eventClickFilterNotifyMe()
        }
        viewModel.setFilterOptionWrapper(selectedData)
    }

    override fun onFinishEditPrice(product: ProductUiModel) {
        product.title?.let {
            product.minPrice?.price?.let { price ->
                viewModel.editPrice(
                    product.id,
                    price,
                    it
                )
            }
        }
    }

    override fun onFinishEditStock(
        productId: String,
        productName: String,
        stock: Int?,
        status: ProductStatus?
    ) {
        viewModel.editStock(productId, productName, stock, status)
    }

    override fun getEmptyDataViewModel(): EmptyModel {
        val id = System.currentTimeMillis().toString()
        return ProductManageEmptyModel(id).apply {
            if (showProductEmptyState()) {
                contentRes = R.string.product_manage_list_empty_product
                urlRes = ProductManageUrl.PRODUCT_MANAGE_LIST_EMPTY_STATE
            } else {
                contentRes = R.string.product_manage_list_empty_search
                urlRes = ProductManageUrl.PRODUCT_MANAGE_SEARCH_EMPTY_STATE
            }
        }
    }

    override fun onAddProductWithNoVariantClicked() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_ADD)
        startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        productManageAddEditMenuBottomSheet.dismiss()
    }

    override fun onViolationError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        binding?.root?.let {
            Toaster.build(
                it,
                type = Toaster.TYPE_ERROR,
                text = errorMessage,
                duration = Toaster.LENGTH_LONG
            ).show()
        }
    }

    override fun onSuspendError(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        binding?.root?.let {
            Toaster.build(
                it,
                type = Toaster.TYPE_ERROR,
                text = errorMessage,
                duration = Toaster.LENGTH_LONG
            ).show()
        }
    }

    private fun observeProductVariantBroadcast() {
        viewLifecycleOwner.observe(viewModel.getProductVariantsResult) {
            when (it) {
                is Success -> setVariantGoToBroadcastChat(it.data)
                is Fail -> {
                    val message =
                        activity?.resources?.getString(R.string.broadcast_chat_error_state_message_empty_variant)
                            .orEmpty()
                    val action =
                        activity?.resources?.getString(R.string.broadcast_chat_error_state_action_retry)
                            .orEmpty()
                    errorStateBroadcastChat(message, action, isRetry = true)
                }
            }
        }
    }

    private fun observeUploadStatus() {
        viewLifecycleOwner.observe(viewModel.uploadStatus) {
            if (it.status == UploadStatusType.STATUS_DONE.name) {
                viewModel.getPopupsInfo(it.productId)
                getFiltersTab(withDelay = true)
                getProductList(withDelay = true, isRefresh = true)
                viewModel.clearUploadStatus()
            }
        }
    }

    private fun setVariantGoToBroadcastChat(data: GetVariantResult) {
        if (!data.isEmptyPrimaryVariant()) {
            val mainProduct = data.getMainVariant()
            redirectToBroadcastChat(mainProduct?.id.orEmpty())
        } else if (!data.isEmptyOtherVariant()) {
            val mainProduct = data.getOtherVariant()
            redirectToBroadcastChat(mainProduct?.id.orEmpty())
        } else {
            showErrorStateEmptyProductBroadcastChat()
        }
    }

    private fun goToCreateBroadCastChat(product: ProductUiModel?) {
        if (isSellerMigrationEnabled(context)) {
            goToSellerAppProductManageBroadcastChat(product)
        } else {
            if (product?.stock.isZero() || product?.isActive() != true) {
                showErrorStateEmptyProductBroadcastChat()
            } else {
                // request variant
                if (product.isVariant()) {
                    viewModel.getProductVariants(product.id)
                } else {
                    redirectToBroadcastChat(product.id)
                }
            }
        }
    }

    private fun goToCreateProductCoupon(product: ProductUiModel?) {
        val firstTimeLink =
            if (checkProductCouponFirstTime()) {
                Uri.parse(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_FIRST_TIME)
                    .buildUpon()
                    .appendQueryParameter(
                        CentralizedPromoApplinkConst.PROMO_TYPE,
                        CentralizedPromoApplinkConst.TYPE_VOUCHER_PRODUCT
                    )
                    .appendQueryParameter(
                        CentralizedPromoApplinkConst.PRODUCT_ID,
                        product?.id.orEmpty()
                    )
                    .build().toString()
            } else {
                ApplinkConstInternalSellerapp.SELLER_MVC_CREATE_PRODUCT_VOUCHER
            }
        context?.let {
            RouteManager.route(it, firstTimeLink)
        }
    }

    private fun checkProductCouponFirstTime(): Boolean {
        return context?.getSharedPreferences(VOUCHER_CREATION_PREF, Context.MODE_PRIVATE)
            ?.getBoolean(IS_PRODUCT_COUPON_FIRST_TIME, true)
            .orTrue()
    }

    private fun goToCreateBroadcastFromSellerMigration(
        stock: Int,
        isActive: Boolean,
        isVariant: Boolean,
        productId: String
    ) {
        if (stock.isZero() || !isActive) {
            showErrorStateEmptyProductBroadcastChat()
        } else {
            // request variant
            if (isVariant) {
                viewModel.getProductVariants(productId)
            } else {
                redirectToBroadcastChat(productId)
            }
        }
    }

    private fun showErrorStateEmptyProductBroadcastChat() {
        val message =
            activity?.resources?.getString(R.string.broadcast_chat_error_state_message_empty_stock)
                .orEmpty()
        val action =
            activity?.resources?.getString(R.string.broadcast_chat_error_state_action_oke).orEmpty()
        errorStateBroadcastChat(message, action)
    }

    private fun redirectToBroadcastChat(productId: String) {
        val chatBlastUrl = BROADCAST_CHAT_CREATE
        val url =
            Uri.parse(chatBlastUrl).buildUpon().appendQueryParameter(PRODUCT_ID, productId).build()
                .toString()
        RouteManager.route(requireContext(), ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun errorStateBroadcastChat(
        message: String,
        action: String,
        isRetry: Boolean = false,
        product: ProductUiModel? = null
    ) {
        constraintLayout?.let {
            Toaster.build(
                it,
                type = Toaster.TYPE_ERROR,
                text = message,
                actionText = action,
                duration = Toaster.LENGTH_LONG,
                clickListener = View.OnClickListener {
                    if (isRetry) {
                        goToCreateBroadCastChat(product)
                    } else {
                        return@OnClickListener
                    }
                }
            ).show()
        }
    }

    private fun goToSellerAppProductManageBroadcastChat(product: ProductUiModel?) {
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
            .buildUpon()
            .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ID, product?.id)
            .appendQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_STOCK,
                product?.stock?.toString()
            )
            .appendQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_ACTIVE,
                product?.isActive()?.toString()
            )
            .appendQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_PRODUCT_VARIANT,
                product?.isVariant()?.toString()
            )
            .build()
            .toString()

        val chatBlastUrl = Uri.parse(BROADCAST_CHAT_CREATE).buildUpon()
            .appendQueryParameter(PRODUCT_ID, product?.id).build().toString()
        val secondAppLink = UriUtil.buildUri(ApplinkConstInternalGlobal.WEBVIEW, chatBlastUrl)

        goToSellerMigrationPage(
            SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT,
            arrayListOf(firstAppLink, secondAppLink)
        )
    }

    private fun goToSellerAppProductManageMultiEdit() {
        val appLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
            .buildUpon()
            .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_ENABLE_MULTI_EDIT, "true")
            .build()
            .toString()

        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_MULTI_EDIT, arrayListOf(appLink))
    }

    private fun goToSellerAppTopAds() {
        val firstAppLink = ApplinkConst.PRODUCT_MANAGE
        val secondAppLink = ApplinkConst.SellerApp.TOPADS_DASHBOARD

        goToSellerMigrationPage(
            SellerMigrationFeatureName.FEATURE_TOPADS,
            arrayListOf(firstAppLink, secondAppLink)
        )
    }

    private fun goToSellerAppProductManageThenAddAsFeatured(product: ProductUiModel) {
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
            .buildUpon()
            .appendQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_ADD_AS_FEATURED, "true")
            .appendQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_IS_PRODUCT_FEATURED,
                product.isFeatured.toString()
            )
            .appendQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_SELECTED_PRODUCT_ID,
                product.id
            )
            .build()
            .toString()

        goToSellerMigrationPage(
            SellerMigrationFeatureName.FEATURE_FEATURED_PRODUCT,
            arrayListOf(firstAppLink)
        )
    }

    private fun goToSellerAppSetStockReminder(product: ProductUiModel) {
        val cacheManagerId = UUID.randomUUID().toString()
        val firstAppLink = Uri.parse(ApplinkConst.PRODUCT_MANAGE)
            .buildUpon()
            .appendQueryParameter(
                ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID,
                cacheManagerId
            )
            .build()
            .toString()
        val secondAppLink = UriUtil.buildUri(
            ApplinkConstInternalMarketplace.STOCK_REMINDER,
            product.id,
            product.title,
            product.isVariant.toString()
        )
            .plus("?${ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID}=$cacheManagerId")
        goToSellerMigrationPage(
            SellerMigrationFeatureName.FEATURE_STOCK_REMINDER,
            arrayListOf(firstAppLink, secondAppLink)
        )
    }

    private fun goToSellerAppAddProduct() {
        val secondAppLink = ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW
        goToSellerMigrationPage(
            SellerMigrationFeatureName.FEATURE_SET_VARIANT,
            arrayListOf(ApplinkConst.PRODUCT_MANAGE, secondAppLink)
        )
    }

    private fun goToSellerMigrationPage(
        @SellerMigrationFeatureName featureName: String,
        appLinks: ArrayList<String>
    ) {
        context?.run {
            val intent =
                SellerMigrationActivity.createIntent(this, featureName, screenName, appLinks)
            startActivity(intent)
        }
    }

    // set filter options if filterOptions is not null or empty
    private fun setDefaultFilterOption() {
        if (defaultFilterOptions.isNotEmpty()) {
            val filterOptionsWrapper = FilterOptionWrapper(
                sortOption = null,
                filterOptions = defaultFilterOptions,
                filterShownState = listOf(true, true, false, true),
                selectedFilterCount = ProductManageFilterMapper.countSelectedFilter(
                    defaultFilterOptions
                )
            )
            viewModel.setFilterOptionWrapper(filterOptionsWrapper)

            defaultFilterOptions = emptyList()
        }
    }

    fun setDefaultFilterOptions(filterOptions: List<FilterOption>) {
        defaultFilterOptions = filterOptions
    }

    fun setSearchKeywordOptions(keyword: String) {
        isLoadingInitialData = true
        tabSortFilter?.show()
        searchBar?.show()
        searchBar?.searchBarTextField?.setText(keyword)
        showLoadingProgress()
        getProductList()
        searchBar?.clearFocus()
    }

    private fun showProductEmptyState(): Boolean {
        val selectedFilters = viewModel.selectedFilterAndSort.value
        val searchKeyword = searchBar?.searchBarTextField?.text?.toString().orEmpty()
        return searchKeyword.isEmpty() && selectedFilters?.selectedFilterCount.orZero() == 0 && filterTab?.isFilterActive() == false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupInterceptor() {
        interceptor?.setOnTouchListener { v, event ->
            searchBar?.clearFocus()
            if (event?.action == MotionEvent.ACTION_UP) {
                v?.performClick()
            }
            false
        }
    }

    private fun setupSearchBar() {
        searchBar?.run {
            clearFocus()

            // Set fitsSystemWindows to false to avoid removed padding after changing systemUiVisibility
            (searchBarTextField.parent as? View)?.fitsSystemWindows = false

            searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    recyclerView?.itemAnimator = null
                    recyclerView?.removeOnScrollListener(recyclerViewScrollListener)
                    showLoadingProgress()
                    isLoadingInitialData = true
                    dismissAllCoachMark()
                    getProductList()
                    clearFocus()
                    true
                } else {
                    false
                }
            }
            clearListener = {
                clearSearchBarInput()
                loadInitialData()
            }
        }
    }

    private fun setupBottomSheet(access: ProductManageAccess) {
        productManageBottomSheet = ProductManageBottomSheet.createInstance(access).apply {
            init(this@ProductManageFragment, sellerFeatureCarouselClickListener)
        }
        multiEditBottomSheet = ProductMultiEditBottomSheet(this, childFragmentManager)
        productManageMoreMenuBottomSheet =
            ProductManageMoreMenuBottomSheet(context, this, childFragmentManager)
    }

    private fun setupMultiSelect() {
        textMultipleSelect?.setOnClickListener {
            val isNotAllTobacco = adapter.data.filterIsInstance<ProductUiModel>().filter {
                !it.isTobacco
            }.isNotEmpty()

            if (textMultipleSelect?.text.toString() == getString(R.string.product_manage_multiple_select)) {
                if (isNotAllTobacco) {
                    viewModel.toggleMultiSelect()
                } else {
                    showErrorToast(getString(R.string.product_tobacco_message_not_allow_bulk_edit_all))
                }
            } else {
                viewModel.toggleMultiSelect()
            }
            ProductManageTracking.eventMultipleSelect()
        }

        btnMultiEdit?.setOnClickListener {
            multiEditBottomSheet?.show(viewModel.shopStatus.value?.isOnModerationMode().orFalse())
            ProductManageTracking.eventBulkSettings()
        }
    }

    private fun startPerformanceMonitoring() {
        performanceMonitoring = PerformanceMonitoring.start(MP_PRODUCT_MANAGE)
    }

    private fun setupDialogFeaturedProduct() {
        context?.let {
            dialogFeaturedProduct =
                DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        }
    }

    private fun setupSelectAll() {
        checkBoxSelectAll?.setOnClickListener {
            val isChecked = checkBoxSelectAll?.isChecked == true
            recyclerView?.post {
                if (isChecked) {
                    productManageListAdapter.checkAllProducts(itemsChecked) {
                        itemsChecked = it.filter { !it.isTobacco }.toMutableList()
                        if (itemsChecked.isEmpty()) {
                            viewModel.toggleMultiSelect()
                            showErrorToast(getString(R.string.product_tobacco_message_not_allow_bulk_edit_all))
                        }
                    }
                } else {
                    productManageListAdapter.unCheckMultipleProducts(null, itemsChecked) {
                        itemsChecked = it.filter { !it.isTobacco }.toMutableList()
                    }
                }

                renderSelectAllCheckBox()
                renderCheckedView()
            }
        }
    }

    private fun setupErrorPage() {
        errorPage?.run {
            errorAction.text = context?.getString(R.string.product_manage_refresh_page)

            setActionClickListener {
                onSwipeRefresh()
            }
        }
    }

    private fun setupNoAccessPage() {
        noAccessPage?.run {
            ImageHandler.loadImageAndCache(
                errorIllustration,
                ProductManageUrl.ILLUSTRATION_NO_ACCESS
            )
            errorTitle.text = context?.getString(R.string.product_manage_no_access_title)
            errorDescription.text =
                context?.getString(R.string.product_manage_no_access_description)
            errorAction.text = context?.getString(R.string.product_manage_back_to_home)
            setButtonFull(true)

            setActionClickListener {
                RouteManager.route(context, ApplinkConst.SellerApp.SELLER_APP_HOME)
            }
        }
    }

    private fun showFilterBottomSheet() {
        filterProductBottomSheet = context?.let {
            ProductManageFilterFragment.createInstance(viewModel.selectedFilterAndSort.value, this)
        }
        this.childFragmentManager.let { filterProductBottomSheet?.show(it, BOTTOM_SHEET_TAG) }
    }

    private fun showMoreMenuBottomSheet() {
        productManageMoreMenuBottomSheet?.show()
    }

    private fun setActiveFilterCount(filter: FilterOptionWrapper) {
        filterTab?.setActiveFilterCount(filter.selectedFilterCount)
    }

    private fun renderCheckedView() {
        val multiSelectEnabled = viewModel.toggleMultiSelect.value == true
        if (multiSelectEnabled) {
            val textSelectedProduct = getString(
                R.string.product_manage_bulk_count,
                itemsChecked.size.toString()
            )
            textProductCount?.text = textSelectedProduct
            textProductCount?.show()
        } else {
            btnMultiEdit?.hide()
            renderProductCount()
        }

        btnMultiEdit?.showWithCondition(itemsChecked.isNotEmpty())
    }

    private fun renderSelectAllCheckBox() {
        when {
            itemsChecked.isEmpty() -> {
                resetSelectAllCheckBox()
            }

            itemsChecked.size == adapter.data.size -> {
                checkBoxSelectAll?.isChecked = true
                checkBoxSelectAll?.setIndeterminate(false)
            }

            checkBoxSelectAll?.getIndeterminate() == false -> {
                checkBoxSelectAll?.isChecked = true
                checkBoxSelectAll?.setIndeterminate(true)
            }
        }
    }

    private fun setupProductList() {
        recyclerView?.run {
            itemAnimator = defaultItemAnimator
            addItemDecoration(ProductListItemDecoration())
        }
    }

    private fun setupFiltersTab() {
        filterTab = tabSortFilter?.let { sortFilter ->
            ProductManageFilterTab(sortFilter, {
                onClickMoreFilter()
            }, {
                onClickFilterTab(it)
            })
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ProductManageAdapterFactoryImpl> {
        return ProductManageListDiffutilAdapter(adapterTypeFactory, userSession.deviceId.orEmpty())
    }

    override fun getAdapterTypeFactory(): ProductManageAdapterFactoryImpl {
        return ProductManageAdapterFactoryImpl(this, this)
    }

    override fun getScreenName(): String = "/product list page"

    override fun initInjector() {
        getComponent(ProductManageListComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        isLoadingInitialData = false
        getProductList(page)
    }

    private fun renderProductList(list: List<ProductUiModel>, hasNextPage: Boolean) {
        recyclerView?.post {
            if (isLoadingInitialData && list.isEmpty()) {
                productManageListAdapter.updateEmptyState(emptyDataViewModel)
            } else {
                if (isLoadingInitialData) {
                    productManageListAdapter.updateProduct(list)
                } else {
                    productManageListAdapter.removeEmptyAndUpdateLayout(list)
                }
            }
            updateScrollListenerState(hasNextPage)
            if (shouldScrollToTop) {
                shouldScrollToTop = false
                recyclerView?.addOneTimeGlobalLayoutListener {
                    recyclerView?.smoothScrollToPosition(RV_TOP_POSITION)
                }
            }
        }
        recyclerView?.post {
            recyclerView?.addOnScrollListener(recyclerViewScrollListener)
        }
        renderCheckedView()
        showAddAsFeaturedProduct()
        if (extraCacheManagerId.isNotBlank()) {
            val cacheManager =
                context?.let { context -> SaveInstanceCacheManager(context, extraCacheManagerId) }
            val resultStatus =
                cacheManager?.get(ProductManageListConstant.EXTRA_RESULT_STATUS, Int::class.java)
                    ?: 0
            if (resultStatus == Activity.RESULT_OK && sellerMigrationFeatureName == SellerMigrationFeatureName.FEATURE_STOCK_REMINDER) {
                onSetStockReminderResult()
            }
        }
        if (sellerMigrationFeatureName == SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT) {
            goToCreateBroadcastFromSellerMigration(
                productStock,
                isProductActive,
                isProductVariant,
                productId
            )
        }
    }

    private fun showAddAsFeaturedProduct() {
        if (shouldAddAsFeatured) {
            shouldAddAsFeatured = false
            val isFeatured = activity?.intent?.data?.getBooleanQueryParameter(
                DeepLinkMapperProductManage.QUERY_PARAM_IS_PRODUCT_FEATURED,
                false
            )
                ?: false
            val productId =
                activity?.intent?.data?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_SELECTED_PRODUCT_ID)
                    .orEmpty()
            onSetFeaturedProductClicked(isFeatured, productId)
        }
    }

    private fun getProductList(
        page: Int = 1,
        isRefresh: Boolean = false,
        withDelay: Boolean = false,
        isRefreshFromSortFilter: Boolean = false
    ) {
        val keyword = searchBar?.searchBarTextField?.text?.toString().orEmpty()
        val selectedFilter = viewModel.selectedFilterAndSort.value
        val filterOptions = createFilterOptions(page, keyword)
        val sortOption = selectedFilter?.sortOption

        filterTab?.getSelectedFilter()?.let {
            filterOptions.add(FilterByStatus(it))
        }
        if (isRefreshFromSortFilter) {
            tabSortFilter?.show()
            shimmerSortFilter?.hide()
            swipeToRefresh?.isRefreshing = true
        } else if (isLoadingInitialData) {
            tabSortFilter?.hide()
            shimmerSortFilter?.show()
        }

        viewModel.getProductList(
            userSession.shopId,
            filterOptions,
            sortOption,
            isRefresh,
            withDelay
        )
    }

    private fun getProductListFeaturedOnlySize() {
        viewModel.getFeaturedProductCount(userSession.shopId)
    }

    private fun createFilterOptions(page: Int, keyword: String?): MutableList<FilterOption> {
        val selectedFilter = viewModel.selectedFilterAndSort.value
        val filterOptions = selectedFilter?.filterOptions.orEmpty().toMutableList()

        filterOptions.addKeywordFilter(keyword)
        filterOptions.add(FilterByPage(page))
        return filterOptions
    }

    private fun MutableList<FilterOption>.addKeywordFilter(keyword: String?) {
        if (!keyword.isNullOrEmpty()) add(FilterByKeyword(keyword))
    }

    private fun showProductList(productList: List<ProductUiModel>) {
        val hasNextPage = productList.isNotEmpty()
        renderProductList(productList, hasNextPage)
        renderSelectAllCheckBox()
        setDefaultFilterOption()
    }

    private fun renderMultiSelectProduct() {
        val productNotEmpty = adapter.data
            .filterIsInstance<ProductUiModel>()
            .isNotEmpty()

        val productManageAccess =
            viewModel.productManageAccess.value as? Success<ProductManageAccess>
        val hasMultiSelectAccess = productManageAccess?.data?.multiSelect == true
        val shouldShow =
            productNotEmpty && ProductManageConfig.IS_SELLER_APP && hasMultiSelectAccess

        multiSelectContainer?.showWithCondition(productNotEmpty)
        textMultipleSelect?.showWithCondition(shouldShow)

        if (shouldEnableMultiEdit && hasMultiSelectAccess) {
            shouldEnableMultiEdit = false
            textMultipleSelect?.performClick()
        }

        if (hasMultiSelectAccess) {
            enableMultiSelect()
        }
    }

    private fun onErrorEditPrice(editPriceResult: EditPriceResult) {
        with(editPriceResult) {
            val message = if (error is NetworkErrorException) {
                getString(error.message.toIntOrZero())
            } else {
                error?.message
            }
            message?.let {
                val retryMessage =
                    getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
                showErrorToast(it, retryMessage) {
                    viewModel.editPrice(productId, price, productName)
                }
            }
        }
    }

    private fun onErrorEditStock(editStockResult: EditStockResult) {
        with(editStockResult) {
            val message = if (error is NetworkErrorException) {
                getString(error?.message.toIntOrZero())
            } else {
                error?.message
            }
            message?.let {
                val retryMessage =
                    getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
                showErrorToast(it, retryMessage) {
                    viewModel.editStock(productId, productName, stock, status)
                }
            }
        }
    }

    private fun onSuccessEditPrice(productId: String, price: String, productName: String) {
        showToaster(getString(R.string.product_manage_quick_edit_price_success, productName))
        recyclerView?.post {
            productManageListAdapter.updatePrice(productId, price)
        }
    }

    private fun onSuccessEditStock(
        productId: String,
        productName: String,
        stock: Int?,
        status: ProductStatus?
    ) {
        when {
            status != null -> {
                if (status == INACTIVE) {
                    showMessageToast(
                        getString(
                            com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_status_inactive_success,
                            productName
                        )
                    )
                } else {
                    showMessageToast(
                        getString(
                            com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_status_active_success,
                            productName
                        )
                    )
                }
            }

            stock != null -> {
                showToaster(
                    getString(
                        com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_stock_success,
                        productName
                    )
                )
            }
        }

        recyclerView?.post {
            productManageListAdapter.updateStock(productId, stock, status)
        }

        filterTab?.getSelectedFilter()?.let {
            filterProductListByStatus(it)
            renderMultiSelectProduct()
        }

        getFiltersTab(withDelay = true)
    }

    private fun onErrorDeleteProduct(deleteProductResult: DeleteProductResult) {
        val message = if (deleteProductResult.error is NetworkErrorException) {
            getString(deleteProductResult.error.message.toIntOrZero())
        } else {
            deleteProductResult.error?.message
        }
        message?.let {
            if (deleteProductResult.error is NetworkErrorException) {
                val retryMessage =
                    getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_retry)
                showErrorToast(it, retryMessage) {
                    viewModel.deleteSingleProduct(
                        deleteProductResult.productName,
                        deleteProductResult.productId
                    )
                }
            } else {
                val okeMessage =
                    getString(R.string.label_oke)
                showErrorToast(it, okeMessage)
            }
        }
    }

    private fun onSuccessDeleteProduct(productName: String, productId: String) {
        showToaster(getString(R.string.product_manage_delete_product_success, productName))
        recyclerView?.post {
            productManageListAdapter.deleteProduct(productId)
        }
        renderMultiSelectProduct()
        getFiltersTab(withDelay = true)
    }

    private fun showMessageToast(message: String) {
        view?.let {
            val actionLabel = getString(R.string.label_oke)
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionLabel)
                .show()
        }
    }

    private fun showMessageToastWithoutAction(message: String) {
        view?.let {
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showErrorToast(
        message: String = getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_fail),
        actionLabel: String = getString(com.tokopedia.abstraction.R.string.close),
        listener: () -> Unit = {}
    ) {
        view?.let {
            val onClickActionLabel = View.OnClickListener { listener.invoke() }
            Toaster.build(
                it,
                message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                actionLabel,
                onClickActionLabel
            ).show()
        }
    }

    private fun showRetryToast() {
        view?.let {
            val onClickActionLabel = View.OnClickListener {
                if (isLoadingInitialData) {
                    onSwipeRefresh()
                } else {
                    endlessRecyclerViewScrollListener.loadMoreNextPage()
                }
            }
            Toaster.build(
                it,
                getString(com.tokopedia.product.manage.common.R.string.product_manage_snack_bar_fail),
                Snackbar.LENGTH_INDEFINITE,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.retry_label),
                onClickActionLabel
            ).show()
        }
    }

    private fun showLoadingProgress() {
        progressBar?.show()
    }

    private fun hideLoadingProgress() {
        progressBar?.hide()
    }

    private fun onSuccessGetPopUp(isShowPopup: Boolean, productId: String) {
        if (isShowPopup) {
            initPopUpDialog(productId).show()
        }
    }

    private fun initPopUpDialog(productId: String): DialogUnify {
        context?.let { context ->
            return DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val backgroundColor = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                val spanText = SpannableString(getString(R.string.popup_tips_trick_clickable))
                val textLinkLength = TEXT_LINK_LENGTH_END
                val textLinkStart = TEXT_LINK_LENGTH_START
                spanText.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(v: View) {
                            RouteManager.route(
                                context,
                                String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TIPS_TRICK)
                            )
                            activity?.finish()
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = backgroundColor
                            ds.isUnderlineText = false
                        }
                    },
                    spanText.length - textLinkLength,
                    spanText.length - textLinkStart,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spanText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    spanText.length - textLinkLength,
                    spanText.length - textLinkStart,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                setTitle(getString(R.string.popup_label_static))
                setDescription(spanText)
                setPrimaryCTAText(getString(R.string.courier_option))
                setPrimaryCTAClickListener {
                    RouteManager.route(context, ApplinkConst.SELLER_SHIPPING_EDITOR)
                    activity?.finish()
                }
                setSecondaryCTAText(getString(R.string.product_option))
                setSecondaryCTAClickListener {
                    goToPDP(productId)
                    dismiss()
                }
            }
        }
        return DialogUnify(requireContext(), DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
    }

    private fun onErrorGetPopUp() {
        onSuccessGetPopUp(false, "")
    }

    private fun onSuccessMultiEditProducts(result: MultiEditResult) {
        when (result) {
            is EditByStatus -> {
                val haveDTProducts = itemsChecked.filter { it.isDTInbound }
                if (result.status == INACTIVE) {
                    if (haveDTProducts.isNotEmpty()) {
                        showEditDTProductsInActiveConfirmationDialog()
                        showMultiEditToast(result, true)
                    } else {
                        showMultiEditToast(result)
                        updateEditProductList(result)
                    }
                } else {
                    showMultiEditToast(result)
                }
            }

            is EditByMenu -> {
                updateEditProductList(result)
                showMultiEditToast(result)
            }
        }
    }

    private fun onSuccessMultiEditDTProducts(result: MultiEditResult) {
        if (result is EditByStatus) {
            if (result.failedDT.isNotEmpty() && result.status == DELETED) {
                showInfoNotAllowedToDeleteProductDT(
                    getString(
                        com.tokopedia.product.manage.common.R.string.product_manage_deletion_product_dt_bulkedit_title,
                        result.failedDT.size.toString()
                    ),
                    getString(com.tokopedia.product.manage.common.R.string.product_manage_deletion_product_dt_desc)
                )
            }
        }
        showMultiEditToast(result)
        updateEditProductList(result)
    }

    private fun showMultiEditToast(result: MultiEditResult, isHaveNextProcess: Boolean = false) {
        context?.let { context ->
            if (result.failedDT.isNotEmpty()) {
                showInfoNotAllowedToDeleteProductDT(
                    getString(
                        com.tokopedia.product.manage.common.R.string.product_manage_deletion_product_dt_bulkedit_title,
                        result.failedDT.size.toString()
                    ),
                    getString(com.tokopedia.product.manage.common.R.string.product_manage_deletion_product_dt_desc)
                )
            }
            if (result.failed.isNotEmpty()) {
                val okeLabel =
                    getString(R.string.label_oke)
                val retryMessage = getRetryMessage(context, result)

                showErrorToast(retryMessage, okeLabel)
            } else if (result.success.isNotEmpty()) {
                val itemCheckedNonDT = itemsChecked.filter {
                    !it.isDTInbound
                }.toMutableList()

                val message = getSuccessMessage(context, result, itemCheckedNonDT)
                showMessageToast(message)
                if (!isHaveNextProcess) {
                    clearSelectedProduct()
                    renderCheckedView()
                }
            }
        }
    }

    private fun retryMultiEditProducts(result: MultiEditResult) {
        val productIds = result.failed.map { it.productID }

        when (result) {
            is EditByStatus -> viewModel.editProductsByStatus(productIds, result.status)
            is EditByMenu -> viewModel.editProductsEtalase(
                productIds,
                result.menuId,
                result.menuName
            )
        }
    }

    private fun updateEditProductList(result: MultiEditResult) {
        val productIds = result.success.map { it.productID }

        when (result) {
            is EditByStatus -> {
                updateProductListStatus(productIds, result.status)

                filterTab?.getSelectedFilter()?.let {
                    filterProductListByStatus(it)
                    renderMultiSelectProduct()
                }

                getFiltersTab(withDelay = true)
            }

            else -> {
                // no op
            }
        }

        if (result.failed.isEmpty()) {
            viewModel.toggleMultiSelect()
        } else {
            unCheckMultipleProducts(productIds)
        }
    }

    private fun unCheckMultipleProducts(productIds: List<String>) {
        recyclerView?.post {
            productManageListAdapter.unCheckMultipleProducts(productIds, itemsChecked) {
                itemsChecked = it.filter { !it.isTobacco }.toMutableList()
            }

            renderSelectAllCheckBox()
            renderCheckedView()
        }
    }

    private fun updateProductListStatus(productIds: List<String>, status: ProductStatus) {
        recyclerView?.post {
            when (status) {
                DELETED -> productManageListAdapter.deleteProducts(productIds)
                INACTIVE -> productManageListAdapter.setProductsStatuses(productIds, status)
                else -> {
                } // do nothing
            }
        }
    }

    private fun filterProductListByStatus(productStatus: ProductStatus?) {
        recyclerView?.post {
            productManageListAdapter.filterProductList { it.status == productStatus }
        }
    }

    private fun filterProductListByFeatured() {
        recyclerView?.post {
            productManageListAdapter.filterProductList { it.isFeatured == true }
        }
    }

    override fun onSwipeRefresh() {
        swipeToRefresh.isRefreshing = true
        refreshCoachMark()
        loadInitialData()
        clearFilterAndKeywordIfEmpty()

        hideSnackBarRetry()
        resetProductList()
        disableMultiSelect()

        hideNoAccessPage()
        hideErrorPage()

        getProductManageAccess()
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        shouldScrollToTop = true
        endlessRecyclerViewScrollListener.resetState()
        getProductList(isRefresh = true)
    }

    private fun clearFilterAndKeywordIfEmpty() {
        val productList = adapter.data
            .filterIsInstance<ProductUiModel>()

        if (productList.isEmpty()) {
            resetSelectedFilter()
            clearSearchBarInput()
        }
    }

    private fun resetSelectedFilter() {
        removeObservers(viewModel.selectedFilterAndSort)
        viewModel.resetSelectedFilter()
        filterTab?.resetFilters()
        observeFilter()
    }

    private fun clearSearchBarInput() {
        searchBar?.searchBarTextField?.text?.clear()
    }

    private fun onSuccessChangeFeaturedProduct(productId: String, status: Int) {
        // Default feature product action is to remove the product from featured products.
        // The value will change depends on the status code. 0 is remove, 1 is add
        var successMessage: String =
            getString(R.string.product_manage_success_remove_featured_product)
        var isFeaturedProduct = false
        // If the action is to add featured product, invert the attributes value also
        if (status == ProductManageListConstant.FEATURED_PRODUCT_ADD_STATUS) {
            successMessage = getString(R.string.product_manage_success_add_featured_product)
            isFeaturedProduct = true
        }
        recyclerView?.post {
            productManageListAdapter.updateFeaturedProduct(productId, isFeaturedProduct)
        }
        val filterOptions = viewModel.selectedFilterAndSort.value?.filterOptions
        if (filterOptions == listOf(FilterByCondition.FeaturedOnly)) {
            filterProductListByFeatured()
            renderMultiSelectProduct()
        }
        hideLoadingProgress()
        showMessageToastWithoutAction(successMessage)
    }

    private fun onFailedChangeFeaturedProduct(e: Throwable) {
        val toasterError = ""
        hideLoadingProgress()
        showErrorToast(getChangeFeaturedErrorMessage(e), toasterError) {}
    }

    private fun getChangeFeaturedErrorMessage(throwable: Throwable): String =
        when (throwable) {
            is UnknownHostException -> getString(R.string.product_manage_failed_no_internet)
            is TimeoutException -> getString(R.string.product_manage_failed_set_featured_product)
            is MessageErrorException ->
                throwable.message
                    ?: getString(R.string.product_manage_failed_set_featured_product)

            else -> ErrorHandler.getErrorMessage(context, throwable)
        }

    override fun onClickProductCheckBox(isChecked: Boolean, position: Int) {
        adapter.data.getOrNull(position)?.let { product ->
            if (product is ProductUiModel) {
                val checkedData = itemsChecked.firstOrNull { it.id.contains(product.id) }
                adapter.data[position] = product.copy(isChecked = isChecked)

                if (isChecked && checkedData == null) {
                    itemsChecked.add(product)
                } else if (!isChecked) {
                    itemsChecked.remove(checkedData)
                }
            }

            renderSelectAllCheckBox()
            renderCheckedView()
        }
    }

    override fun onClickStockInformation() {
        stockInfoBottomSheet.show()
    }

    override fun onClickNotifyMeBuyerInformation(product: ProductUiModel) {
        val notifyMeInfoBottomSheet =
            NotifyMeBuyerInformationBottomSheet.createInstance(product.notifyMeOOSWording)
        notifyMeInfoBottomSheet.setOnClickEditProductStock {
            if (product.isVariant()) {
                ProductManageTracking.eventClickAturStockNotifyMe(productId, productId)
                onClickEditVariantStockButton(product)
            } else {
                ProductManageTracking.eventClickAturStockNotifyMe(productId)
                onClickEditStockButton(product)
            }
        }
        notifyMeInfoBottomSheet.show(childFragmentManager)

        if (product.isVariant()) {
            ProductManageTracking.eventClickNotifyMeIcon(productId, productId)
        } else {
            ProductManageTracking.eventClickNotifyMeIcon(productId)
        }
    }

    override fun onClickStockReminderInformation(stockAlertCount: Int, stockAlertActive: Boolean) {
        val stockReminderInfoBottomSheet = StockReminderInformationBottomSheet(
            childFragmentManager,
            stockAlertCount,
            stockAlertActive
        )
        stockReminderInfoBottomSheet.show()
    }

    override fun onClickLabelGuarantee() {
        val stockGuaranteeBottomSheet = StockAvailableBottomSheet(
            childFragmentManager
        )
        stockGuaranteeBottomSheet.show()
    }

    override fun onClickDTIdentifier() {
        val infoDilayaniTokopediaBottomSheet = InfoDilayaniTokopediaBottomSheet(
            childFragmentManager
        )
        infoDilayaniTokopediaBottomSheet.show()
    }

    override fun onClickEditStockButton(product: ProductUiModel) {
        if (product.hasStockReserved) {
            context?.run {
                startActivityForResult(
                    CampaignStockActivity.createIntent(
                        this,
                        userSession.shopId,
                        arrayOf(product.id),
                        product.isProductBundling
                    ),
                    REQUEST_CODE_CAMPAIGN_STOCK
                )
            }
        } else {
            val editStockBottomSheet = context?.let {
                ProductManageQuickEditStockFragment.createInstance(
                    it,
                    product,
                    this,
                    this
                )
            }
            editStockBottomSheet?.show(childFragmentManager, BOTTOM_SHEET_TAG)
        }
        ProductManageTracking.eventEditStock(product.id)
    }

    override fun onClickEditVariantPriceButton(product: ProductUiModel) {
        val editVariantBottomSheet =
            QuickEditVariantPriceBottomSheet.createInstance(
                product.id,
                product.isProductBundling,
                userSession.isMultiLocationShop
            ) { result ->
                viewModel.editVariantsPrice(result)
            }
        editVariantBottomSheet.show(childFragmentManager, QuickEditVariantPriceBottomSheet.TAG)
        ProductManageTracking.eventClickEditPriceVariant()
    }

    override fun onClickEditVariantStockButton(product: ProductUiModel) {
        if (product.hasStockReserved) {
            context?.run {
                startActivityForResult(
                    CampaignStockActivity.createIntent(
                        this,
                        userSession.shopId,
                        arrayOf(product.id)
                    ),
                    REQUEST_CODE_CAMPAIGN_STOCK
                )
            }
        } else {
            try {
                val editVariantStockBottomSheet = QuickEditVariantStockBottomSheet.createInstance(
                    product.id,
                    product.isProductBundling,
                    ::onClickCampaignInfo,
                    ::onClickDTIdentifier
                ) { result ->
                    viewModel.editVariantsStock(result)
                }
                editVariantStockBottomSheet.show(
                    childFragmentManager,
                    QuickEditVariantStockBottomSheet.TAG
                )
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
        ProductManageTracking.eventClickEditStockVariant()
    }

    override fun onClickContactCsButton(product: ProductUiModel) {
        when {
            product.isViolation() -> {
                goToProductViolationHelpPage()
            }

            product.isPending() -> {
                showViolationReasonBottomSheet(product.id)
            }

            product.isSuspend() -> {
                showSuspendReasonBottomSheet(product.id)
            }
        }
        ProductManageTracking.eventContactCs(product.id)
    }

    private fun showCoachMarkLabelGuarantee(view: View) {
        if (getVisiblePercent(view) == 0) {
            coachMarkLabelGuarantee?.showCoachMark(
                step = getCoachMarkLabelGuarantee(view)
            )
            coachMarkLabelGuarantee?.setOnDismissListener {
                CoachMarkPreference.setShown(
                    view.context,
                    SHARED_PREF_PRODUCT_MANAGE_SHOW_LABEL_GUARANTEE_COACH_MARK,
                    true
                )
                val stockReminderPosition =
                    getProductWithStockReminder(adapter.data.filterIsInstance<ProductUiModel>())
                val item = productManageLayoutManager?.findViewByPosition(stockReminderPosition)
                    ?.findViewById<IconUnify>(R.id.imageStockReminder)
                item?.let {
                    manageShowCoachMark(view = it)
                }
            }
        }
    }

    override fun onFinishBindMenuReminder(view: View) {
        productManageBottomSheet?.menuList?.post {
            showCoachMenuReminder(view)
        }
    }

    override fun onClickCampaignInfo(campaignTypeList: List<ProductCampaignType>) {
        if (!isAdded) return
        showOngoingPromotionBottomSheet(campaignTypeList)
    }

    override fun onClickMoreOptionsButton(product: ProductUiModel) {
        hideSoftKeyboard()

        if (product.status == MODERATED) {
            val errorMessage =
                getString(R.string.product_manage_desc_product_on_supervision, product.title)
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        } else {
            val isPowerMerchantOrOfficialStore = viewModel.isPowerMerchant() || isOfficialStore
            productManageBottomSheet?.show(
                childFragmentManager,
                product,
                isPowerMerchantOrOfficialStore
            )
        }
    }

    override fun onClickEditPriceButton(product: ProductUiModel) {
        val editPriceBottomSheet = context?.let {
            ProductManageQuickEditPriceFragment.createInstance(
                it,
                product,
                userSession.isMultiLocationShop,
                this
            )
        }
        editPriceBottomSheet?.show(childFragmentManager, BOTTOM_SHEET_TAG)
        ProductManageTracking.eventEditPrice(product.id)
    }

    override fun onClickOptionMenu(menu: ProductMenuUiModel) {
        val product = menu.product
        val productId = product.id
        val productName = product.title ?: ""

        when (menu) {
            is Preview -> {
                goToPDP(productId)
                ProductManageTracking.eventSettingsPreview(productId)
            }

            is Duplicate -> {
                clickDuplicateProduct(productId)
                ProductManageTracking.eventSettingsDuplicate(productId)
            }

            is StockReminder -> {
                onSetStockReminderClicked(product)
                ProductManageTracking.eventSettingsReminder(productId)
            }

            is Delete -> {
                if (product.isDTInbound && !product.isCampaign) {
                    showInfoNotAllowedToDeleteProductDT(
                        getString(com.tokopedia.product.manage.common.R.string.product_manage_deletion_product_dt_title),
                        getString(com.tokopedia.product.manage.common.R.string.product_manage_deletion_product_dt_desc)
                    )
                } else {
                    clickDeleteProductMenu(productName, productId)
                }
                ProductManageTracking.eventSettingsDelete(productId)
            }

            is SetTopAds -> {
                onPromoTopAdsClicked(product.id)
                ProductManageTracking.eventSettingsTopads(productId)
            }

            is SeeTopAds -> {
                onSeeTopAdsClicked(product.id)
                ProductManageTracking.eventSettingsTopadsDetail(productId)
            }

            is SetFeaturedProduct -> {
                onSetFeaturedProductClicked(product.isFeatured ?: false, product.id)
                ProductManageTracking.eventSettingsFeatured(productId)
            }

            is RemoveFeaturedProduct -> {
                onRemoveFeaturedProductClicked(product)
                ProductManageTracking.eventSettingsFeatured(productId)
            }

            is CreateBroadcastChat -> {
                goToCreateBroadCastChat(product)
                ProductManageTracking.eventClickBroadcastChat(
                    userId = userSession.userId,
                    productId = productId,
                    isCarousel = false
                )
            }

            is CreateProductCoupon -> {
                goToCreateProductCoupon(product)
                ProductManageTracking.eventClickCreateProductCoupon(userSession.shopId)
            }
        }
        productManageBottomSheet?.dismiss(childFragmentManager)
    }

    private fun goToProductViolationHelpPage() {
        RouteManager.route(
            activity,
            "${ApplinkConst.WEBVIEW}?url=${ProductManageUrl.PRODUCT_VIOLATION_HELP_URL}"
        )
    }

    private fun clickDuplicateProduct(productId: String) {
        goToDuplicateProduct(productId)
    }

    private fun clickDeleteProductMenu(productName: String, productId: String) {
        viewModel.onDeleteSingleProduct(productName, productId)
    }

    private fun hideSoftKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun onSetFeaturedProductClicked(isFeatured: Boolean, productId: String) {
        context?.let { context ->
            if ((viewModel.isPowerMerchant() || isOfficialStore) && !isFeatured) {
                when (productListFeaturedOnlySize) {
                    MAX_FEATURED_PRODUCT -> {
                        setDialogFeaturedProduct(
                            ProductManageUrl.ILLUSTRATION_MAX_FEATURED_PRODUCT_DOMAIN,
                            getString(R.string.product_featured_max_dialog_title),
                            getString(R.string.product_featured_max_dialog_desc),
                            getString(R.string.product_featured_max_dialog_primary_cta),
                            getString(R.string.product_featured_max_dialog_secondary_cta)
                        )
                        dialogFeaturedProduct?.setPrimaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                        dialogFeaturedProduct?.setSecondaryCTAClickListener {
                            dialogFeaturedProduct?.dismiss()
                            viewModel.setSelectedFilter(listOf(FilterByCondition.FeaturedOnly))
                        }
                        dialogFeaturedProduct?.show()
                    }

                    MIN_FEATURED_PRODUCT -> {
                        setDialogFeaturedProduct(
                            ProductManageUrl.ILLUSTRATION_ADD_FEATURED_PRODUCT_DOMAIN,
                            getString(R.string.product_featured_add_dialog_title),
                            getString(R.string.product_featured_add_dialog_desc),
                            getString(R.string.product_featured_add_dialog_primary_cta),
                            getString(R.string.product_featured_add_dialog_secondary_cta)
                        )
                        dialogFeaturedProduct?.setPrimaryCTAClickListener {
                            addFeaturedProduct(productId)
                            dialogFeaturedProduct?.dismiss()
                            ProductManageTracking.eventFeaturedProductPopUpSave()
                        }
                        dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                        dialogFeaturedProduct?.show()
                    }

                    else -> {
                        addFeaturedProduct(productId)
                    }
                }
            } else {
                setDialogFeaturedProduct(
                    ProductManageUrl.ILLUSTRATION_SPECIAL_FEATURED_PRODUCT_DOMAIN,
                    getString(R.string.product_featured_special_dialog_title),
                    getString(R.string.product_featured_special_dialog_desc),
                    getString(R.string.product_featured_special_dialog_primary_cta),
                    getString(R.string.product_featured_special_dialog_secondary_cta)
                )
                dialogFeaturedProduct?.setPrimaryCTAClickListener {
                    dialogFeaturedProduct?.dismiss()
                    RouteManager.route(
                        context,
                        ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
                    )
                    ProductManageTracking.eventFeaturedProductPopUpMore()
                }
                dialogFeaturedProduct?.setSecondaryCTAClickListener { dialogFeaturedProduct?.dismiss() }
                dialogFeaturedProduct?.show()
            }
        }
    }

    private fun onRemoveFeaturedProductClicked(productManageUiModel: ProductUiModel) {
        productListFeaturedOnlySize -= 1
        showLoadingProgress()
        setFeaturedProduct(
            productManageUiModel.id,
            ProductManageListConstant.FEATURED_PRODUCT_REMOVE_STATUS
        )
    }

    private fun onSetStockReminderClicked(productManageUiModel: ProductUiModel) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.STOCK_REMINDER,
            productManageUiModel.id,
            productManageUiModel.isVariant.toString()
        )
        startActivityForResult(intent, REQUEST_CODE_STOCK_REMINDER)
    }

    private fun onPromoTopAdsClicked(productId: String) {
        RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_MP_ADS_CREATION, productId)
    }

    private fun onSeeTopAdsClicked(productId: String) {
        RouteManager.route(
            context,
            ApplinkConstInternalTopAds.TOPADS_SEE_ADS_PERFORMANCE,
            productId,
            TOPADS_PERFORMANCE_CURRENT_SITE
        )
    }

    private fun goToDuplicateProduct(productId: String) {
        activity?.let {
            val uri = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, productId)
                .appendQueryParameter(
                    ApplinkConstInternalMechant.QUERY_PARAM_MODE,
                    ApplinkConstInternalMechant.MODE_DUPLICATE_PRODUCT
                )
                .build()
                .toString()
            val intent = RouteManager.getIntent(requireContext(), uri)
            startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        }
    }

    private fun goToEditProduct(productId: String) {
        context?.let {
            if (!viewModel.shopStatus.value?.isOnModerationMode().orFalse()) {
                val intent =
                    RouteManager.getIntent(requireContext(), ApplinkConst.PRODUCT_EDIT, productId)
                startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT)
            }
        }
    }

    private fun addFeaturedProduct(productId: String) {
        productListFeaturedOnlySize += 1
        showLoadingProgress()
        setFeaturedProduct(productId, ProductManageListConstant.FEATURED_PRODUCT_ADD_STATUS)
    }

    private fun setFeaturedProduct(id: String, type: Int) {
        viewModel.setFeaturedProduct(id, type)
    }

    private fun showDialogDeleteProduct(data: SingleProduct) {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title = if (data.isMultiLocationShop) {
                    getString(R.string.product_manage_multi_location_delete_product_title)
                } else {
                    getString(R.string.product_manage_delete_product_title)
                }

                val description = if (data.isMultiLocationShop) {
                    getString(R.string.product_manage_multi_location_delete_product_description)
                } else {
                    getString(R.string.product_manage_delete_product_description)
                }

                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(it.resources.getString(R.string.product_manage_delete_product_delete_button))
                setSecondaryCTAText(it.resources.getString(R.string.product_manage_delete_product_cancel_button))
                setPrimaryCTAClickListener {
                    viewModel.deleteSingleProduct(data.productName, data.productId)
                    ProductManageTracking.eventDeleteProduct(data.productId)
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    override fun onClickProductItem(product: ProductUiModel) {
        goToEditProduct(product.id)
        ProductManageTracking.eventOnProduct(product.id)
    }

    override fun clearAndGetProductList() {
        clearAllData()
        resetMultiSelect()
        disableMultiSelect()
        getProductList()
    }

    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private fun goToPDP(
        productId: String?,
        showTopAdsSheet: Boolean = false
    ) {
        if (productId != null) {
            val uri = Uri.parse(ApplinkConstInternalMarketplace.PRODUCT_DETAIL).buildUpon()

            if (showTopAdsSheet) {
                uri.appendQueryParameter(
                    SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME,
                    SellerMigrationFeatureName.FEATURE_ADS
                )
            }

            val uriString = uri.build().toString()
            val intent = RouteManager.getIntent(context, uriString, productId)

            startActivity(intent)
        }
    }

    private fun goToEtalasePicker() {
        context?.let {
            val intent =
                RouteManager.getIntent(it, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                    .apply {
                        val bundle = Bundle().apply {
                            putString(
                                ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_SHOWCASE_PICKER,
                                ShowcasePickerType.RADIO
                            )
                        }
                        putExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE, bundle)
                    }

            startActivityForResult(intent, REQUEST_CODE_PICK_ETALASE)
        }
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // no op
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            dismissAllCoachMark()
        }
    }

    override fun onPause() {
        super.onPause()
        if (productManageAddEditMenuBottomSheet.isVisible) {
            productManageAddEditMenuBottomSheet.dismiss()
        }
        if (productManageBottomSheet?.isVisible == true) {
            productManageBottomSheet?.dismiss(childFragmentManager)
        }
    }

    override fun onResume() {
        super.onResume()
        hasTickerClosed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
        removeObservers()
    }

    private fun removeObservers() {
        removeObservers(viewModel.viewState)
        removeObservers(viewModel.productListResult)
        removeObservers(viewModel.getProductVariantsResult)
        removeObservers(viewModel.shopInfoResult)
        removeObservers(viewModel.multiEditProductResult)
        removeObservers(viewModel.deleteProductResult)
        removeObservers(viewModel.editPriceResult)
        removeObservers(viewModel.getPopUpResult)
        removeObservers(viewModel.setFeaturedProductResult)
        removeObservers(viewModel.toggleMultiSelect)
        removeObservers(viewModel.productFiltersTab)
    }

    override fun callInitialLoadAutomatically(): Boolean = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (intent != null) {
            when (requestCode) {
                REQUEST_CODE_PICK_ETALASE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val productIds = itemsChecked.map { product -> product.id }
                        val selectedShowcase: ShowcaseItemPicker = intent.getParcelableExtra(
                            ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE
                        ) ?: ShowcaseItemPicker()

                        viewModel.editProductsEtalase(
                            productIds,
                            selectedShowcase.showcaseId,
                            selectedShowcase.showcaseName
                        )
                    }
                }

                REQUEST_CODE_ETALASE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        val shopId = userSession.shopId
                        val showcaseId =
                            intent.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                        val isNeedToReloadData = intent.getBooleanExtra(
                            ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA,
                            false
                        )
                        val shopShowcaseIntent = RouteManager.getIntent(
                            context,
                            ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST,
                            shopId,
                            showcaseId
                        )
                        shopShowcaseIntent.putExtra(
                            EXTRA_IS_NEED_TO_RELOAD_DATA_SHOP_PRODUCT_LIST,
                            isNeedToReloadData
                        )
                        startActivity(shopShowcaseIntent)
                    }
                }

                REQUEST_CODE_STOCK_REMINDER -> {
                    if (resultCode == Activity.RESULT_OK) {
                        onSetStockReminderResult()
                    }
                }

                REQUEST_CODE_CAMPAIGN_STOCK -> {
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val productId = intent.getStringExtra(EXTRA_PRODUCT_ID).orEmpty()
                            val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME)
                            val stock = intent.getIntExtra(EXTRA_UPDATED_STOCK, 0)
                            val status =
                                valueOf(intent.getStringExtra(EXTRA_UPDATED_STATUS).orEmpty())
                            val isStockChanged =
                                intent.getBooleanExtra(EXTRA_UPDATE_IS_STOCK_CHANGED, false)
                            val isStatusChanged =
                                intent.getBooleanExtra(EXTRA_UPDATE_IS_STATUS_CHANGED, false)

                            recyclerView?.post {
                                productManageListAdapter.updateStock(productId, stock, status)
                            }

                            filterTab?.getSelectedFilter()?.let { productStatus ->
                                filterProductListByStatus(productStatus)
                                renderMultiSelectProduct()
                            }

                            getFiltersTab(withDelay = true)

                            val successMessageRes =
                                when {
                                    isStockChanged && isStatusChanged -> com.tokopedia.product.manage.common.R.string.product_manage_campaign_stock_status_success_toast
                                    isStatusChanged -> com.tokopedia.product.manage.common.R.string.product_manage_campaign_status_success_toast
                                    else -> com.tokopedia.product.manage.common.R.string.product_manage_campaign_stock_success_toast
                                }

                            val successMessage = getString(successMessageRes, productName)
                            constraintLayout?.let { view ->
                                Toaster.build(
                                    view,
                                    successMessage,
                                    Snackbar.LENGTH_SHORT,
                                    Toaster.TYPE_NORMAL
                                )
                                    .show()
                            }
                        }

                        Activity.RESULT_CANCELED -> {
                            val errorMessage = intent.getStringExtra(EXTRA_UPDATE_MESSAGE)
                                ?: getString(com.tokopedia.product.manage.common.R.string.product_manage_campaign_stock_error_toast)
                            constraintLayout?.let { view ->
                                showErrorToast(errorMessage, getString(R.string.label_oke))
                            }
                        }

                        else -> {
                        }
                    }
                }

                else -> {
                    super.onActivityResult(requestCode, resultCode, intent)
                }
            }
        }
    }

    override fun onScrollToTop() {
        recyclerView?.post {
            recyclerView?.smoothScrollToPosition(RV_TOP_POSITION)
        }
    }

    private fun onSetStockReminderResult() {
        val toasterMessage = getString(R.string.product_stock_reminder_toaster_success_desc)
        constraintLayout?.let {
            Toaster.build(it, toasterMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
        onSwipeRefresh()
    }

    override fun showGetListError(throwable: Throwable?) {
        if (isLoadingInitialData) {
            showErrorPage()
        } else {
            updateStateScrollListener()
            showRetryToast()
        }
        viewModel.hideTicker()
        hideLoading()
    }

    private fun getGoldMerchantStatus() {
        viewModel.getGoldMerchantStatus()
    }

    private fun getTopAdsInfo() {
        viewModel.getTopAdsInfo()
    }

    private fun getProductManageAccess() {
        viewModel.getProductManageAccess()
    }

    private fun getTickerData() {
        viewModel.getTickerData()
    }

    private fun getFiltersTab(withDelay: Boolean = false) {
        viewModel.getFiltersTab(withDelay)
    }

    private fun setDialogFeaturedProduct(
        imageUrl: String,
        title: String,
        desc: String,
        primaryCta: String,
        secondaryCta: String
    ) {
        dialogFeaturedProduct?.setImageUrl(imageUrl)
        dialogFeaturedProduct?.setTitle(title)
        dialogFeaturedProduct?.setDescription(desc)
        dialogFeaturedProduct?.setPrimaryCTAText(primaryCta)
        dialogFeaturedProduct?.setSecondaryCTAText(secondaryCta)
    }

    // region observers
    private fun observeSetFeaturedProduct() {
        viewLifecycleOwner.observe(viewModel.setFeaturedProductResult) {
            when (it) {
                is Success -> onSuccessChangeFeaturedProduct(it.data.productId, it.data.status)
                is Fail -> {
                    onFailedChangeFeaturedProduct(it.throwable)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.SET_FEATURED_PRODUCT_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeGetPopUpInfo() {
        viewLifecycleOwner.observe(viewModel.getPopUpResult) {
            when (it) {
                is Success -> onSuccessGetPopUp(it.data.isSuccess, it.data.productId)
                is Fail -> {
                    onErrorGetPopUp()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.GET_POP_UP_INFO_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeEditPrice() {
        viewLifecycleOwner.observe(viewModel.editPriceResult) {
            when (it) {
                is Success -> onSuccessEditPrice(
                    it.data.productId,
                    it.data.price,
                    it.data.productName
                )

                is Fail -> {
                    onErrorEditPrice(it.throwable as EditPriceResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.EDIT_PRICE_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeEditStock() {
        viewLifecycleOwner.observe(viewModel.editStockResult) {
            when (it) {
                is Success -> {
                    with(it.data) {
                        onSuccessEditStock(productId, productName, stock, status)
                    }
                }

                is Fail -> {
                    onErrorEditStock(it.throwable as EditStockResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.EDIT_STOCK_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeMultiEdit() {
        viewLifecycleOwner.observe(viewModel.multiEditProductResult) {
            when (it) {
                is Success -> onSuccessMultiEditProducts(it.data)
                is Fail -> {
                    showErrorToast()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.MULTI_EDIT_PRODUCT_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }

        viewLifecycleOwner.observe(viewModel.multiEditDTProductResult) {
            when (it) {
                is Success -> onSuccessMultiEditDTProducts(it.data)
                is Fail -> {
                    showErrorToast()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.MULTI_EDIT_PRODUCT_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun showDeleteProductsConfirmationDialog(data: MultipleProduct) {
        context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                val title = if (data.isMultiLocationShop) {
                    getString(R.string.product_manage_multi_location_delete_product_title)
                } else {
                    getString(
                        R.string.product_manage_dialog_delete_products_title,
                        itemsChecked.count()
                    )
                }

                val description = if (data.isMultiLocationShop) {
                    getString(R.string.product_manage_multi_location_delete_product_description)
                } else {
                    getString(R.string.product_manage_delete_product_description)
                }

                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(getString(R.string.product_manage_delete_product_delete_button))
                setSecondaryCTAText(getString(R.string.product_manage_delete_product_cancel_button))
                setPrimaryCTAClickListener {
                    val productIds = itemsChecked.map { item -> item.id }
                    viewModel.editProductsByStatus(productIds, DELETED)
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    private fun showEditProductsInActiveConfirmationDialog() {
        context?.let { it ->
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(
                    getString(
                        R.string.product_manage_dialog_edit_products_inactive_title,
                        itemsChecked.count()
                    )
                )
                setDescription(getString(R.string.product_manage_edit_products_inactive_description))
                setPrimaryCTAText(getString(R.string.product_manage_edit_products_inactive_button))
                setSecondaryCTAText(getString(R.string.product_manage_delete_product_cancel_button))
                setPrimaryCTAClickListener {
                    val productIds =
                        itemsChecked.filter { !it.isDTInbound }.map { item -> item.id }
                    viewModel.editProductsByStatus(productIds, INACTIVE)
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    private fun showEditDTProductsInActiveConfirmationDialog() {
        context?.let { it ->
            val htmlText = HtmlLinkHelper(
                it,
                getString(R.string.product_manage_confirm_inactive_dt_product_desc)
            )
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(
                    getString(
                        R.string.product_manage_confirm_inactive_dt_product_title
                    )
                )
                setDescription(htmlText.spannedString ?: String.EMPTY)
                setPrimaryCTAText(getString(R.string.product_manage_confirm_inactive_dt_product_positive_button))
                setSecondaryCTAText(getString(R.string.product_manage_confirm_dt_product_cancel_button))
                setPrimaryCTAClickListener {
                    val productIds = itemsChecked.filter { it.isDTInbound }.map { item -> item.id }
                    viewModel.editProductsByStatus(productIds, INACTIVE, true)
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    private fun observeProductList() {
        viewLifecycleOwner.observe(viewModel.productListResult) {
            when (it) {
                is Success -> {
                    initHeaderView()
                    showProductList(it.data)
                    recyclerView?.post {
                        renderMultiSelectProduct()
                    }
                }

                is Fail -> {
                    showGetListError(it.throwable)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.PRODUCT_LIST_RESULT_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
            hidePageLoading()
            stopPerformanceMonitoring()
        }
    }

    private fun stopPerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
    }

    private fun observeFilterTabs() {
        viewLifecycleOwner.observe(viewModel.productFiltersTab) {
            when (it) {
                is Success -> {
                    val data = it.data

                    if (data is ShowFilterTab) {
                        val tabData = getTabData(data)
                        filterTab?.show(tabData)
                    } else {
                        filterTab?.update(data, this)
                    }
                    renderCheckedView()
                }

                else -> {
                    // no op
                }
            }
        }
    }

    private fun getTabData(data: GetFilterTabResult): GetFilterTabResult {
        val tabName = arguments?.getString(
            ProductManageSellerFragment.PRODUCT_MANAGE_TAB,
            String.EMPTY
        )
        val tabs = data.tabs.map { tab ->
            return@map if (tabName.equals(tab.status?.name, true)
            ) {
                tab.isSelected = true
                tab
            } else {
                tab
            }
        }
        return ShowFilterTab(tabs)
    }

    private fun observeProductListFeaturedOnly() {
        viewLifecycleOwner.observe(viewModel.productListFeaturedOnlyResult) {
            when (it) {
                is Success -> productListFeaturedOnlySize = it.data
                else -> {
                    // no op
                }
            }
        }
    }

    private fun observeMultiSelect() {
        viewLifecycleOwner.observe(viewModel.toggleMultiSelect) { multiSelectEnabled ->
            if (multiSelectEnabled) {
                showMultiSelectView()
            } else {
                hideMultiSelectView()
                resetMultiSelect()
            }

            renderCheckedView()
            showHideProductCheckBox(multiSelectEnabled)
        }
    }

    private fun showHideProductCheckBox(multiSelectEnabled: Boolean) {
        recyclerView?.post {
            productManageListAdapter.setMultiSelectEnabled(multiSelectEnabled)
        }
    }

    private fun showMultiSelectView() {
        val cancelMultiSelectText = getString(R.string.product_manage_cancel_multiple_select)
        textMultipleSelect?.text = cancelMultiSelectText
        checkBoxSelectAll?.show()
    }

    private fun hideMultiSelectView() {
        val multiSelectText = getString(R.string.product_manage_multiple_select)
        textMultipleSelect?.text = multiSelectText
        checkBoxSelectAll?.hide()
        btnMultiEdit?.hide()
    }

    private fun resetProductList() {
        resetMultiSelect()
        renderCheckedView()
    }

    private fun resetMultiSelect() {
        resetSelectAllCheckBox()
        clearSelectedProduct()
    }

    private fun enableMultiSelect() {
        setupMultiSelect()
        checkBoxSelectAll?.isEnabled = true
        textMultipleSelect?.isEnabled = true
    }

    private fun disableMultiSelect() {
        checkBoxSelectAll?.isEnabled = false
        textMultipleSelect?.isEnabled = false
    }

    private fun observeDeleteProduct() {
        viewLifecycleOwner.observe(viewModel.deleteProductResult) {
            when (it) {
                is Success -> onSuccessDeleteProduct(it.data.productName, it.data.productId)
                is Fail -> {
                    onErrorDeleteProduct(it.throwable as DeleteProductResult)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.DELETE_PRODUCT_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeFilter() {
        viewLifecycleOwner.observe(viewModel.selectedFilterAndSort) {
            clearAllData()
            resetMultiSelect()
            getProductList()
            setActiveFilterCount(it)
        }
    }

    private fun observeShopInfo() {
        viewLifecycleOwner.observe(viewModel.shopInfoResult) {
            if (it is Success) {
                goldMerchant = it.data.isGoldMerchant
                isOfficialStore = it.data.isOfficialStore
                shopDomain = it.data.shopDomain
            }
        }
    }

    private fun observeViewState() {
        viewLifecycleOwner.observe(viewModel.viewState) {
            when (it) {
                is ShowProgressDialog -> showLoadingProgress()
                is HideProgressDialog -> hideLoadingProgress()
                is ShowLoadingDialog -> showProgressDialogVariant()
                is HideLoadingDialog -> hideProgressDialogVariant()
            }
        }
        viewLifecycleOwner.observe(viewModel.tickerData) { tickerData ->
            var tickerPagerAdapter = TickerPagerAdapter(context, tickerData)
            tickerPagerAdapter = tickerPagerAdapter.apply {
                setPagerDescriptionClickEvent(object : TickerPagerCallback {
                    override fun onPageDescriptionViewClick(
                        linkUrl: CharSequence,
                        itemData: Any?
                    ) {
                        context?.let { RouteManager.route(it, linkUrl.toString()) }
                    }
                })
                onDismissListener = {
                    viewModel.hideTicker()
                    hasTickerClosed = true
                }
            }

            ticker?.let { tickerView ->
                val visibility = tickerView.visibility
                tickerView.addPagerView(tickerPagerAdapter, tickerData)
                tickerView.post {
                    tickerView.visibility = visibility
                    viewModel.showTicker()
                }
            }
        }
        viewLifecycleOwner.observe(viewModel.showTicker) { shouldShow ->
            if (shouldShow) {
                tickerIsReady = true
            }
            if (!hasTickerClosed) {
                animateProductTicker(shouldShow)
            }
        }
        viewLifecycleOwner.observe(viewModel.refreshList) { shouldRefresh ->
            if (shouldRefresh) {
                resetProductList()
            }
        }
    }

    private fun observeEditVariantPrice() {
        viewLifecycleOwner.observe(viewModel.editVariantPriceResult) {
            when (it) {
                is Success -> {
                    recyclerView?.post {
                        productManageListAdapter.updatePrice(it.data)
                    }
                    val message = context?.getString(
                        R.string.product_manage_quick_edit_price_success,
                        it.data.productName
                    ).orEmpty()
                    showMessageToast(message)
                }

                is Fail -> {
                    showErrorMessageToast(it)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.EDIT_VARIANT_PRICE_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeEditVariantStock() {
        viewLifecycleOwner.observe(viewModel.editVariantStockResult) {
            when (it) {
                is Success -> {
                    val message = MultiVariantToastMessage.getSuccessMessage(context, it.data)
                    updateVariantStock(it.data)
                    showMessageToast(message)
                }

                is Fail -> {
                    showErrorMessageToast(it)
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = it.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.EDIT_VARIANT_STOCK_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeProductManageAccess() {
        viewLifecycleOwner.observe(viewModel.productManageAccess) {
            when (it) {
                is Success -> {
                    val access = it.data
                    if (access.productList) {
                        loadInitialData()

                        getTickerData()
                        getFiltersTab()
                        getProductListFeaturedOnlySize()
                        getGoldMerchantStatus()
                        getTopAdsInfo()

                        setupBottomSheet(access)
                        showHideOptionsMenu()

                        renderStockLocationBottomSheet()
                        hideNoAccessPage()
                        hideErrorPage()
                    } else {
                        showNoAccessPage()
                    }
                }

                is Fail -> showErrorPage()
            }
        }
    }

    private fun observeDeleteProductDialog() {
        viewLifecycleOwner.observe(viewModel.deleteProductDialog) {
            when (it) {
                is SingleProduct -> showDialogDeleteProduct(it)
                is MultipleProduct -> {
                    showDeleteProductsConfirmationDialog(it)
                }
            }
        }
    }

    private fun observeOptionsMenu() {
        viewLifecycleOwner.observe(viewModel.showAddProductOptionsMenu) {
            optionsMenu?.findItem(R.id.add_product_menu)?.isVisible = it
        }
        viewLifecycleOwner.observe(viewModel.showEtalaseOptionsMenu) {
            optionsMenu?.findItem(R.id.action_more_menu)?.isVisible = it
        }
    }

    private fun observeShopStatus() {
        viewLifecycleOwner.observe(viewModel.shopStatus) { statusInfo ->
            disableOrEnableOptionMenuAddProduct(statusInfo.isOnModerationMode())
        }
    }

    private fun renderStockLocationBottomSheet() {
        val multiLocationShop = userSession.isMultiLocationShop
        val showStockLocationBottomSheet = productManageSession.getShowStockLocationBottomSheet()

        if (multiLocationShop && showStockLocationBottomSheet) {
            ProductManageStockLocationBottomSheet.newInstance().show(childFragmentManager)
            productManageSession.setShowStockLocationBottomSheet(false)
        }
    }

    private fun showNoAccessPage() {
        noAccessPage?.show()
    }

    private fun hideNoAccessPage() {
        noAccessPage?.hide()
    }

    private fun showErrorPage() {
        errorPage?.show()
    }

    private fun hideErrorPage() {
        errorPage?.hide()
    }

    private fun updateVariantStock(data: EditVariantResult) {
        val stock = data.countVariantStock()
        val status = data.getVariantStatus()

        recyclerView?.post {
            productManageListAdapter.updateStock(data.productId, stock, status)
        }

        filterTab?.getSelectedFilter()?.let {
            filterProductListByStatus(it)
            renderMultiSelectProduct()
        }

        getFiltersTab(withDelay = true)
    }

    private fun showErrorMessageToast(result: Fail) {
        val error = result.throwable
        if (error is MessageErrorException) {
            val message = error.message.orEmpty()
            showErrorToast(message)
        } else {
            showErrorToast()
        }
    }

    private fun hidePageLoading() {
        swipeToRefresh.isRefreshing = false
    }

    private fun initHeaderView() {
        tabSortFilter?.show()
        shimmerSortFilter?.hide()
    }

    private fun clearSelectedProduct() {
        itemsChecked.clear()
    }

    private fun resetSelectAllCheckBox() {
        if (checkBoxSelectAll?.isChecked == true) {
            checkBoxSelectAll?.isChecked = false
        }
        checkBoxSelectAll?.setIndeterminate(false)
    }

    private fun renderProductCount() {
        val productCount = viewModel.getTotalProductCount()
        textProductCount?.text = getString(R.string.product_manage_count_format, productCount)
    }

    private fun showHideOptionsMenu() {
        viewModel.showHideOptionsMenu()
    }

    private fun showAddEditMenuBottomSheet() {
        productManageAddEditMenuBottomSheet.show()
    }

    private fun showOngoingPromotionBottomSheet(campaignTypeList: List<ProductCampaignType>) {
        context?.let {
            OngoingPromotionBottomSheet.createInstance(it, ArrayList(campaignTypeList))
                .show(childFragmentManager)
        }
    }

    private fun showViolationReasonBottomSheet(productId: String) {
        ViolationReasonBottomSheet.createInstance(productId, this).show(childFragmentManager)
    }

    open fun showSuspendReasonBottomSheet(productId: String) {
        SuspendReasonBottomSheet.createInstance(productId, this).show(childFragmentManager)
    }

    private fun showToaster(message: String) {
        constraintLayout?.let {
            Toaster.build(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun manageShowCoachMark(view: View) {
        context?.let {
            val conditionNotShowCoachmarkAvailable = getConditionNotShowCoachMarkStockAvailable(it)
            val conditionNotShowCoachmarkReminder =
                getConditionNotShowCoachMarkStockReminder(it, conditionNotShowCoachmarkAvailable)
            val conditionNotShowMoreMenu = getConditionNotShowCoachMarkOnMoreOption(
                it,
                conditionNotShowCoachmarkAvailable,
                conditionNotShowCoachmarkReminder
            )

            when {
                view.id == R.id.ivLabelGuaranteed && !conditionNotShowCoachmarkAvailable -> {
                    showCoachMarkLabelGuarantee(view)
                }

                view.id == R.id.imageStockReminder && !conditionNotShowCoachmarkReminder -> {
                    showCoachProductWithStockReminder(view)
                }

                view.id == R.id.btnMoreOptions && !conditionNotShowMoreMenu -> {
                    if (ProductManageConfig.IS_SELLER_APP) {
                        showCoachMoreOptionMenu(
                            view,
                            conditionNotShowCoachmarkAvailable,
                            conditionNotShowCoachmarkReminder
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun getCoachMarkMoreMenu(btnMoreOption: View): ArrayList<CoachMark2Item> {
        return arrayListOf(
            CoachMark2Item(
                btnMoreOption,
                activity?.resources?.getString(R.string.content_coach_mark_more_option_title)
                    .orEmpty(),
                activity?.resources?.getString(R.string.content_coach_mark_more_option_desc)
                    .orEmpty(),
                CoachMark2.POSITION_BOTTOM

            )
        )
    }

    private fun getCoachMarkFlagStockReminder(imageStockReminder: View): ArrayList<CoachMark2Item> {
        val coachMarkItems = arrayListOf<CoachMark2Item>()
        coachMarkItems.add(
            CoachMark2Item(
                imageStockReminder,
                activity?.resources?.getString(R.string.content_coach_mark_flag_stock_reminder_title)
                    .orEmpty(),
                activity?.resources?.getString(R.string.content_coach_mark_flag_stock_reminder_desc)
                    .orEmpty()
            )
        )

        navigationHomeMenuView?.let {
            coachMarkItems.add(
                CoachMark2Item(
                    it,
                    activity?.resources?.getString(R.string.content_coach_mark_nav_home_stock_reminder_title)
                        .orEmpty(),
                    activity?.resources?.getString(R.string.content_coach_mark_nav_home_stock_reminder_desc)
                        .orEmpty(),
                    CoachMark2.POSITION_TOP
                )
            )
        }

        return coachMarkItems
    }

    private fun getCoachMarkReminderMenu(item: View): ArrayList<CoachMark2Item> {
        return arrayListOf(
            CoachMark2Item(
                item,
                activity?.resources?.getString(R.string.content_coach_mark_stock_reminder_menu_title)
                    .orEmpty(),
                activity?.resources?.getString(R.string.content_coach_mark_stock_reminder_menu_desc)
                    .orEmpty()
            )
        )
    }

    private fun showCoachMoreOptionMenu(
        view: View,
        notShowCoachMarkLabelGuarantee: Boolean,
        notShowCoachMarkStockReminder: Boolean
    ) {
        if (notShowCoachMarkStockReminder && notShowCoachMarkLabelGuarantee) {
            CoachMarkPreference.setShown(
                view.context,
                SHARED_PREF_PRODUCT_MANAGE_MENU_OPTIONS_COACH_MARK,
                true
            )
            coachMarkMoreOption?.showCoachMark(
                step = getCoachMarkMoreMenu(view)
            )
        }
    }

    private fun showCoachProductWithStockReminder(view: View) {
        val stockReminderPosition =
            getProductWithStockReminder(adapter.data.filterIsInstance<ProductUiModel>())

        haveSetReminder = stockReminderPosition

        if (getVisiblePercent(view) == 0) {
            currentPositionStockReminderCoachMark = stockReminderPosition
            coachMarkStockReminder?.stepButtonTextLastChild =
                activity?.resources?.getString(com.tokopedia.abstraction.R.string.label_done)
                    .orEmpty()

            val itemCoachMark = getCoachMarkFlagStockReminder(view)
            coachMarkStockReminder?.showCoachMark(
                step = itemCoachMark,
                index = 0
            )
            coachMarkStockReminder?.setOnDismissListener {
                CoachMarkPreference.setShown(
                    requireContext(),
                    SHARED_PREF_STOCK_REMINDER_FLAG_COACH_MARK,
                    true
                )
            }
        }
    }

    private fun showCoachMenuReminder(view: View) {
        if (haveSetReminder == -1) {
            if (getVisiblePercent(view) == 0 && !CoachMarkPreference.hasShown(
                    view.context,
                    SHARED_PREF_STOCK_REMINDER_MENU_COACH_MARK
                )
            ) {
                CoachMarkPreference.setShown(
                    view.context,
                    SHARED_PREF_STOCK_REMINDER_MENU_COACH_MARK,
                    true
                )

                coachMarkMoreMenuReminder?.showCoachMark(
                    step = getCoachMarkReminderMenu(view)
                )
            }
        }
    }

    private fun refreshCoachMark() {
        haveSetReminder = -1
        recyclerView?.removeOnScrollListener(recyclerViewScrollListener)
        dismissAllCoachMark()
    }

    private fun getCoachMarkLabelGuarantee(iconGuarantee: View): ArrayList<CoachMark2Item> {
        return arrayListOf(
            CoachMark2Item(
                iconGuarantee,
                activity?.resources?.getString(R.string.content_coach_mark_label_guarantee_title)
                    .orEmpty(),
                activity?.resources?.getString(R.string.content_coach_mark_label_guarantee_desc)
                    .orEmpty(),
                CoachMark2.POSITION_BOTTOM

            )
        )
    }

    private fun getProductWithStockReminder(data: List<ProductUiModel>): Int {
        return data.indexOfFirst {
            it.hasStockAlert && !it.stockAlertActive && !it.haveNotifyMeOOS && !it.isEmptyStock &&
                !it.isVariant()
        }
    }

    private fun getProductWithStockAvailable(data: List<ProductUiModel>): Int {
        return data.indexOfFirst {
            it.isStockGuaranteed
        }
    }

    private fun dismissAllCoachMark() {
        if (coachMarkLabelGuarantee?.isDismissed == false) {
            coachMarkLabelGuarantee?.dismissCoachMark()
            coachMarkLabelGuarantee?.isDismissed = false
        }
        if (coachMarkMoreOption?.isDismissed == false) {
            coachMarkMoreOption?.dismissCoachMark()
            coachMarkMoreOption?.isDismissed = false
        }
        if (coachMarkStockReminder?.isDismissed == false) {
            coachMarkStockReminder?.dismissCoachMark()
            coachMarkStockReminder?.isDismissed = false
        }
    }

    private fun disableOrEnableOptionMenuAddProduct(isEnable: Boolean) {
        if (optionsMenu != null) {
            if (isEnable) {
                val layoutMenuAddProduct =
                    optionsMenu?.findItem(R.id.add_product_menu)?.actionView as? LinearLayout
                val iconMenuAddProduct =
                    layoutMenuAddProduct?.findViewById<IconUnify>(R.id.ivAddProduct)
                iconMenuAddProduct?.isEnabled = false
                context?.let { context ->
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifycomponents.R.color.Unify_NN300
                    )
                }?.let { color ->
                    iconMenuAddProduct?.setColorFilter(
                        color,
                        PorterDuff.Mode.SRC_ATOP
                    )
                }

                optionsMenu?.findItem(R.id.add_product_menu)?.isEnabled = false
                optionsMenu?.findItem(R.id.add_product_menu)?.actionView?.setOnClickListener(null)
            } else {
                optionsMenu?.findItem(R.id.add_product_menu)?.isEnabled = true
                optionsMenu?.findItem(R.id.add_product_menu)?.let { menuItem ->
                    menuItem.actionView?.setOnClickListener {
                        onOptionsItemSelected(menuItem)
                    }
                }
            }
        }
    }

    private fun showInfoNotAllowedToDeleteProductDT(title: String, desc: String) {
        context?.let { it ->
            DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(
                    title
                )
                setDescription(desc)
                setPrimaryCTAText(getString(com.tokopedia.product.manage.common.R.string.oke))
                setPrimaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    private fun getConditionNotShowCoachMarkStockAvailable(context: Context): Boolean {
        val hasShowCoachMarkLabelGuarantee = CoachMarkPreference.hasShown(
            context,
            SHARED_PREF_PRODUCT_MANAGE_SHOW_LABEL_GUARANTEE_COACH_MARK
        )

        val hasStockAvailable =
            getProductWithStockAvailable(adapter.data.filterIsInstance<ProductUiModel>()) == 0

        return (hasStockAvailable && hasShowCoachMarkLabelGuarantee) ||
            (!hasStockAvailable && hasShowCoachMarkLabelGuarantee) ||
            (!hasStockAvailable && !hasShowCoachMarkLabelGuarantee)
    }

    private fun getConditionNotShowCoachMarkStockReminder(
        context: Context,
        conditionNotShowCoachmarkAvailable: Boolean
    ): Boolean {
        val hasShowCoachMarkStockReminder = CoachMarkPreference.hasShown(
            context,
            SHARED_PREF_STOCK_REMINDER_FLAG_COACH_MARK
        )

        haveSetReminder =
            getProductWithStockReminder(adapter.data.filterIsInstance<ProductUiModel>())

        return (
            (haveSetReminder == -1 && hasShowCoachMarkStockReminder && !conditionNotShowCoachmarkAvailable) ||
                (haveSetReminder == -1 && !hasShowCoachMarkStockReminder && !conditionNotShowCoachmarkAvailable) ||
                (haveSetReminder == -1 && hasShowCoachMarkStockReminder && conditionNotShowCoachmarkAvailable) ||
                (haveSetReminder != -1 && !hasShowCoachMarkStockReminder && !conditionNotShowCoachmarkAvailable) ||
                (haveSetReminder != -1 && hasShowCoachMarkStockReminder && !conditionNotShowCoachmarkAvailable) ||
                (haveSetReminder != -1 && hasShowCoachMarkStockReminder && conditionNotShowCoachmarkAvailable)
            )
    }

    private fun getConditionNotShowCoachMarkOnMoreOption(
        context: Context,
        notShowCoachMarkStockAvailable: Boolean,
        notShowCoachMarkStockReminder: Boolean
    ): Boolean {
        val hasShowCoachMarkOptionMenu = CoachMarkPreference.hasShown(
            context,
            SHARED_PREF_PRODUCT_MANAGE_MENU_OPTIONS_COACH_MARK
        )

        return (!notShowCoachMarkStockAvailable && !notShowCoachMarkStockReminder && hasShowCoachMarkOptionMenu) ||
            (notShowCoachMarkStockAvailable && notShowCoachMarkStockReminder && hasShowCoachMarkOptionMenu)
    }

    companion object {
        private const val BOTTOM_SHEET_TAG = "BottomSheetTag"

        private const val VOUCHER_CREATION_PREF = "voucher_creation"
        private const val IS_PRODUCT_COUPON_FIRST_TIME = "is_product_coupon_first_time"

        private const val MIN_FEATURED_PRODUCT = 0
        private const val MAX_FEATURED_PRODUCT = 5

        private const val TICKER_ENTER_LEAVE_ANIMATION_DURATION = 300L
        private const val TICKER_ENTER_LEAVE_ANIMATION_DELAY = 10L

        private const val RV_TOP_POSITION = 0
        private const val TICKER_MARGIN_TOP = 8
        private const val TEXT_LINK_LENGTH_START = 0
        private const val TEXT_LINK_LENGTH_END = 5

        private const val TOPADS_PERFORMANCE_CURRENT_SITE = "tokopediaseller"

        const val SHARED_PREF_PRODUCT_MANAGE_MENU_OPTIONS_COACH_MARK = "productMoreMenu"
        const val SHARED_PREF_STOCK_REMINDER_FLAG_COACH_MARK = "flagStockAlert"
        const val SHARED_PREF_STOCK_REMINDER_MENU_COACH_MARK = "menuStockReminder"
        const val SHARED_PREF_PRODUCT_MANAGE_SHOW_LABEL_GUARANTEE_COACH_MARK = "showLabelGuarantee"
    }
}
