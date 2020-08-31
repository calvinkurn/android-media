package com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.*
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.activity.ShopShowcaseAddActivity
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment.ShopShowcaseAddFragment
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem
import com.tokopedia.shop_showcase.shop_showcase_management.di.DaggerShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementModule
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity.ShopShowcaseListActivity.Companion.REQUEST_CODE_ADD_ETALASE
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter.ShopShowcaseListAdapter
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcaseListViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class ShopShowcaseListFragment : BaseDaggerFragment(), ShopShowcaseManagementListener,
        HasComponent<ShopShowcaseManagementComponent> {

    companion object {
        const val SHOP_SHOWCASE_TRACE = "mp_shop_showcase"

        @JvmStatic
        fun createInstance(shopType: String, shopId: String?, selectedEtalaseId: String?,
                           isShowDefault: Boolean? = false, isShowZeroProduct: Boolean? = false,
                           isMyShop: Boolean? = false, isNeedToGoToAddShowcase: Boolean? = false
        ): ShopShowcaseListFragment {
            val fragment = ShopShowcaseListFragment()
            val bundle = Bundle()
            bundle.putString(ShopShowcaseListParam.EXTRA_SHOP_ID, shopId)
            bundle.putString(ShopShowcaseListParam.EXTRA_SHOP_TYPE, shopType)
            bundle.putString(ShopShowcaseListParam.EXTRA_ETALASE_ID, selectedEtalaseId)
            bundle.putBoolean(ShopShowcaseListParam.EXTRA_IS_SHOW_DEFAULT, isShowDefault ?: false)
            bundle.putBoolean(ShopShowcaseListParam.EXTRA_IS_SHOW_ZERO_PRODUCT, isShowZeroProduct
                    ?: false)
            bundle.putBoolean(ShopShowcaseListParam.EXTRA_IS_MY_SHOP, isMyShop ?: false)
            bundle.putBoolean(ShopShowcaseListParam.EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE, isNeedToGoToAddShowcase
                    ?: false)
            fragment.arguments = bundle
            return fragment
        }

        const val REQUEST_EDIT_SHOWCASE_CODE = 1
    }

    @Inject
    lateinit var viewModel: ShopShowcaseListViewModel
    lateinit var shopShowcaseFragmentNavigation: ShopShowcaseFragmentNavigation
    private lateinit var btnAddEtalase: UnifyButton
    private lateinit var searchbar: SearchBarUnify
    private lateinit var loading: LoaderUnify
    private lateinit var bottomSheet: BottomSheetUnify
    private lateinit var headerUnify: HeaderUnify
    private lateinit var headerLayout: CardView
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateContainer: LinearLayout
    private lateinit var imgEmptyState: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var globalError: GlobalError
    private var layoutManager: LinearLayoutManager? = null
    private var shopShowcaseListAdapter: ShopShowcaseListAdapter? = null
    private var showcaseList: List<ShowcaseItem> = listOf()
    private var tracking: ShopShowcaseTracking? = null
    private var performanceMonitoring: PerformanceMonitoring? = null

    private var shopId: String = "0"
    private var selectedEtalaseId: String? = null
    private var isShowDefault: Boolean? = null
    private var isShowZeroProduct: Boolean? = null
    private var isMyShop: Boolean = false
    private var shopType = ""
    private var isNeedToGoToAddShowcase = false
    private var isReorderList = false


    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): ShopShowcaseManagementComponent? {
        return activity?.run {
            DaggerShopShowcaseManagementComponent
                    .builder()
                    .shopShowcaseManagementModule(ShopShowcaseManagementModule(this))
                    .shopShowcaseComponent(ShopShowcaseInstance.getComponent(application))
                    .build()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        shopShowcaseFragmentNavigation = context as ShopShowcaseFragmentNavigation
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_ADD_ETALASE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (isNeedToGoToAddShowcase) {
                        isNeedToGoToAddShowcase = false
                        activity?.let {
                            val intent = Intent()
                            it.setResult(Activity.RESULT_OK, intent)
                            it.finish()
                        }
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    activity?.let {
                        if (isNeedToGoToAddShowcase) {
                            isNeedToGoToAddShowcase = false
                            val intent = Intent()
                            it.setResult(Activity.RESULT_CANCELED, intent)
                            it.finish()
                        }
                    }
                }
            }
            REQUEST_EDIT_SHOWCASE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val isSuccessEditShowcase = data?.extras?.getInt(ShopShowcaseListParam.EXTRA_EDIT_SHOWCASE_RESULT)
                    if (isSuccessEditShowcase == ShopShowcaseAddFragment.SUCCESS_EDIT_SHOWCASE)
                        onSuccessUpdateShowcase()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { tracking = ShopShowcaseTracking(it) }
        arguments?.let {
            shopId = it.getString(ShopShowcaseListParam.EXTRA_SHOP_ID, "")
            shopType = it.getString(ShopShowcaseListParam.EXTRA_SHOP_TYPE, "")
            selectedEtalaseId = it.getString(ShopShowcaseListParam.EXTRA_ETALASE_ID)
            isShowDefault = it.getBoolean(ShopShowcaseListParam.EXTRA_IS_SHOW_DEFAULT)
            isShowZeroProduct = it.getBoolean(ShopShowcaseListParam.EXTRA_IS_SHOW_ZERO_PRODUCT)
            isMyShop = it.getBoolean(ShopShowcaseListParam.EXTRA_IS_MY_SHOP)
            isNeedToGoToAddShowcase = it.getBoolean(ShopShowcaseListParam.EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_showcase_list, container, false)
        headerUnify = view.findViewById(R.id.showcase_list_toolbar)
        headerLayout = view.findViewById(R.id.header_layout)
        globalError = view.findViewById(R.id.globalError)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        btnAddEtalase = view.findViewById(R.id.btn_add_etalase)
        loading = view.findViewById(R.id.loading)
        searchbar = view.findViewById(R.id.searchbar)
        emptyStateContainer = view.findViewById(R.id.empty_state)
        imgEmptyState = view.findViewById(R.id.img_empty_state)
        recyclerView = view.findViewById(R.id.rv_list_etalase)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        shopShowcaseListAdapter = ShopShowcaseListAdapter(this, isMyShop)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = shopShowcaseListAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(SHOP_SHOWCASE_TRACE)
        showLoading(true)
        initHeaderUnify()
        initSearchbar()
        initSwipeRefresh()
        initRecyclerView()
        setupBuyerSellerView()

        observeShopShowcaseSellerData()
        observeShopShowcaseBuyerData()
        observeDeleteShopShowcase()
        observeTotalProduct()

        btnAddEtalase.setOnClickListener {
//            isNeedToGoToAddShowcase = true
            tracking?.clickTambahEtalase(shopId, shopType)
            checkTotalProduct()
        }
    }

    private fun initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        showLoadingSwipeToRefresh(true)
        loadData()
    }

    private fun initSearchbar() {
        searchbar.searchBarTextField.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    tracking?.clickSearchBar(shopId, shopType)
                }
            }
        })

        searchbar.searchBarTextField.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                showLoading(true)

                val shopShowcaseViewModelList = ArrayList<ShowcaseItem>()
                if (showcaseList != null && showcaseList.size > 0) {
                    val lowercaseKeyword = s.toString().toLowerCase()
                    for (showcase in showcaseList) {
                        if (showcase.name.toLowerCase().contains(lowercaseKeyword)) {
                            shopShowcaseViewModelList.add(showcase)
                        }
                    }
                }

                if (shopShowcaseViewModelList.size > 0) {
                    isSearchShowcaseFound(true)
                    shopShowcaseListAdapter?.updateDataShowcaseList(shopShowcaseViewModelList)
                } else {
                    isSearchShowcaseFound(false)
                }
            }
        })
    }

    private fun initRecyclerView() {
        var currentScrollPosition = 0

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScrollPosition += dy
                val HAS_ELEVATION = 16.0f
                val NO_ELEVATION = 0f

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (currentScrollPosition == 0) {
                        headerLayout?.cardElevation = NO_ELEVATION
                    } else {
                        headerLayout?.cardElevation = HAS_ELEVATION
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroy() {
        viewModel.reoderShopShowcaseResponse.removeObservers(this)
        viewModel.getListBuyerShopShowcaseResponse.removeObservers(this)
        viewModel.getListSellerShopShowcaseResponse.removeObservers(this)
        viewModel.deleteShopShowcaseResponse.removeObservers(this)
        viewModel.getShopProductResponse.removeObservers(this)
        super.onDestroy()
    }

    override fun sendClickShowcaseMenuMore(dataShowcase: ShowcaseItem, position: Int) {
        tracking?.clickDots(shopId, shopType, dataShowcase.name)
        handleEditShowcase(dataShowcase, position)
    }

    override fun sendClickShowcase(dataShowcase: ShowcaseItem, position: Int) {
        tracking?.clickEtalase(shopId, shopType, dataShowcase.name)
        val showcaseId = if (dataShowcase.type == ShowcaseType.GENERATED) dataShowcase.alias else dataShowcase.id
        activity?.run{
            val intent = Intent()
//            intent.putExtra(ShopShowcaseListParam.EXTRA_ETALASE_ID, showcaseId)
//            intent.putExtra(ShopShowcaseListParam.EXTRA_ETALASE_NAME, dataShowcase.name)
//            intent.putExtra(ShopShowcaseListParam.EXTRA_ETALASE_BADGE, dataShowcase.badge)
//            intent.putExtra(ShopShowcaseListParam.EXTRA_IS_NEED_TO_RELOAD_DATA, true)
//            intent.putExtra(ShopShowcaseListParam.EXTRA_ETALASE_TYPE, dataShowcase.type)

            intent.putExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID, showcaseId)
            intent.putExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME, dataShowcase.name)
            intent.putExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_BADGE, dataShowcase.badge)
            intent.putExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, true)
            intent.putExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_TYPE, dataShowcase.type)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onSuccessUpdateShowcase() {
        showToaster(resources.getString(R.string.info_success_edit_showcase), Toaster.TYPE_NORMAL)
    }

    private fun observeShopShowcaseBuyerData() {
        viewModel.getListBuyerShopShowcaseResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    stopPerformanceMonitoring()
                    showLoading(false)
                    showLoadingSwipeToRefresh(false)
                    val errorMessage = it.data.shopShowcasesByShopID.error.message
                    if (errorMessage.isNotEmpty()) {
                        showErrorResponse(errorMessage)
                    } else {
                        hideGlobalError()
                        showcaseList = it.data.shopShowcasesByShopID.result
                        shopShowcaseListAdapter?.updateDataShowcaseList(showcaseList)
                    }
                }
                is Fail -> {
                    showLoading(false)
                    showErrorMessage(it.throwable)
                    showGlobalError(GlobalError.SERVER_ERROR)
                }
            }
        })
    }

    private fun observeShopShowcaseSellerData() {
        viewModel.getListSellerShopShowcaseResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    stopPerformanceMonitoring()
                    showLoading(false)
                    showLoadingSwipeToRefresh(false)
                    val errorMessage = it.data.shopShowcases.error.message
                    if (errorMessage.isNotEmpty()) {
                        showErrorResponse(errorMessage)
                    } else {
                        hideGlobalError()
                        showcaseList = it.data.shopShowcases.result
                        shopShowcaseListAdapter?.updateDataShowcaseList(showcaseList)
                    }
                }
                is Fail -> {
                    showLoading(false)
                    showErrorMessage(it.throwable)
                    showGlobalError(GlobalError.SERVER_ERROR)
                }
            }
        })
    }

    private fun observeTotalProduct() {
        viewModel.getShopProductResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val error = it.data.getShopProduct.errors
                    val totalProduct = it.data.getShopProduct.totalData

                    if (error.isNotEmpty()) {
                        showErrorResponse(error)
                    } else {
                        if (totalProduct < 1) {
                            showErrorResponse(getString(R.string.error_product_less_than_one))
                        } else if (totalProduct > 0) {
                            goToAddShowcase()
                        }
                    }
                }
                is Fail -> {
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun observeDeleteShopShowcase() {
        viewModel.deleteShopShowcaseResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    val isSuccess = it.data.deleteShopShowcase.success
                    val message = it.data.deleteShopShowcase.message
                    if (isSuccess) {
                        showLoading(true)
                        showToaster(getString(R.string.info_success_delete_showcase), Toaster.TYPE_NORMAL)
                        loadData()
                    } else {
                        showErrorResponse(message)
                    }
                }
                is Fail -> {
                    showErrorMessage(it.throwable)
                }
            }
        })
    }

    private fun goToAddShowcase() {
        val addShowcaseIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_ADD)
        startActivityForResult(addShowcaseIntent, REQUEST_CODE_ADD_ETALASE)
    }

    private fun loadData() {
        if (isNeedToGoToAddShowcase && isMyShop) {
            checkTotalProduct()
            Handler().postDelayed({
                viewModel.getShopShowcaseListAsSeller()
            }, 500)
        } else {
            if (!isMyShop) {
                viewModel.getShopShowcaseListAsBuyer(shopId, false)
            } else {
                viewModel.getShopShowcaseListAsSeller()
            }
        }
    }

    private fun checkTotalProduct() {
        val page = 1
        val perPage = 1
        val fkeyword = ""
        val sort = 0
        val fmenu = "etalase"
        viewModel.getTotalProduct(shopId, page, perPage, sort, fmenu, fkeyword)
    }

    private fun handleEditShowcase(dataShowcase: ShowcaseItem, position: Int) {
        bottomSheet = BottomSheetUnify()
        bottomSheet.setTitle(getString(R.string.setting_showcase))
        bottomSheet.setCloseClickListener {
            onDismissBottomSheet()
        }
        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.setting_item_menu, null)
        val btnEditBottomSheet: LinearLayout = bottomSheetView.findViewById(R.id.ubah_container)
        val btnDeleteBottomSheet: LinearLayout = bottomSheetView.findViewById(R.id.hapus_container)
        val showcaseId = dataShowcase.id
        val showcaseName = dataShowcase.name
        bottomSheetView.apply {
            btnEditBottomSheet.setOnClickListener {
                onDismissBottomSheet()
                tracking?.clickEditMenu(shopId, shopType)
                val isActionEdit = true
                val intent = ShopShowcaseAddActivity.createIntent(context, isActionEdit, showcaseId, showcaseName)
                startActivityForResult(intent, REQUEST_EDIT_SHOWCASE_CODE)
            }
            btnDeleteBottomSheet.setOnClickListener {
                onDismissBottomSheet()
                tracking?.clickDeleteMenu(shopId, shopType)
                if (showcaseId.isNotEmpty()) {
                    showDeleteDialog(showcaseId)
                } else {
                    showErrorResponse(context.getString(R.string.error_happens))
                }
            }
        }
        bottomSheet.setChild(bottomSheetView)
        fragmentManager?.let {
            bottomSheet.show(it, "Bottomsheet edit showcase show")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.GONE
        }
    }

    private fun onDismissBottomSheet() {
        try {
            if (bottomSheet != null) {
                bottomSheet.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    private fun showToaster(message: String, type: Int) {
        view?.run {
            Toaster.make(this, message, Snackbar.LENGTH_SHORT, type)
        }
    }

    private fun isSearchShowcaseFound(isFound: Boolean) {
        if (isFound) {
            loading.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            emptyStateContainer.visibility = View.GONE
            ImageHandler.loadImage(context, imgEmptyState, ImageAssets.SEARCH_SHOWCASE_NOT_FOUND, null)
        } else {
            loading.visibility = View.GONE
            recyclerView.visibility = View.GONE
            emptyStateContainer.visibility = View.VISIBLE
            ImageHandler.loadImage(context, imgEmptyState, ImageAssets.SEARCH_SHOWCASE_NOT_FOUND, null)
        }
    }

    private fun setupBuyerSellerView() {
        if (isMyShop) {
            setupSellerView()
        } else {
            setupBuyerView()
        }
    }

    private fun setupBuyerView() {
        btnAddEtalase.visibility = View.GONE
        headerUnify.actionTextView?.visibility = View.GONE
    }

    private fun setupSellerView() {
        btnAddEtalase.visibility = View.VISIBLE
        headerUnify.actionTextView?.visibility = View.VISIBLE
    }

    private fun stopPerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
    }

    private fun showErrorMessage(t: Throwable) {
        view?.let {
            Toaster.showError(it,
                    ErrorHandler.getErrorMessage(context, t),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun showErrorResponse(message: String) {
        view?.let {
            Toaster.showError(it, message, Snackbar.LENGTH_LONG)
        }
    }

    private fun showSuccessMessage(message: String) {
        view?.let {
            Toaster.showNormal(it, message, Snackbar.LENGTH_LONG)
        }
    }

    private fun hideGlobalError() {
        globalError.visibility = View.GONE
        headerLayout.visibility = View.VISIBLE
        swipeRefreshLayout.visibility = View.VISIBLE
    }

    private fun showGlobalError(errorType: Int) {
        globalError.setType(errorType)
        globalError.visibility = View.VISIBLE
        headerLayout.visibility = View.GONE
        swipeRefreshLayout.visibility = View.GONE
        loading.visibility = View.GONE
        emptyStateContainer.visibility = View.GONE
        globalError.setActionClickListener {
            refreshData()
        }
    }

    private fun showLoadingSwipeToRefresh(isLoading: Boolean){
        if (isLoading) {
            swipeRefreshLayout.isRefreshing = true
        } else {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initHeaderUnify() {
        headerUnify.apply {
            setNavigationOnClickListener {
                tracking?.clickBackButton(shopId, shopType)
                activity?.finish()
            }
        }
        headerUnify.actionTextView?.setOnClickListener {
            val shopShowcaseViewModelList = ArrayList<ShowcaseItem>()
            for (shopShowcaseViewModel in showcaseList) {
                shopShowcaseViewModelList.add(shopShowcaseViewModel)
            }
            tracking?.clickSusun(shopId, shopType)

            shopShowcaseFragmentNavigation.navigateToPage(
                    PageNameConstant.SHOWCASE_LIST_REORDER_PAGE,
                    null, shopShowcaseViewModelList)
        }
    }

    private fun showDeleteDialog(showcaseId: String) {
        activity?.also {
            var deleteDialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            deleteDialog.apply {
                setTitle(TextConstant.TEXT_TITLE_DIALOG_DELETE)
                setDescription(TextConstant.TEXT_DESCRIPTION_DIALOG_DELETE)
                setPrimaryCTAText(getString(R.string.text_cancel_button))
                setPrimaryCTAClickListener {
                    this.dismiss()
                    tracking?.clickBatalDialog(shopId, shopType)
                }
                setSecondaryCTAText(getString(R.string.text_delete_button))
                setSecondaryCTAClickListener {
                    this.dismiss()
                    tracking?.clickHapusDialog(shopId, shopType)
                    viewModel.removeSingleShopShowcase(showcaseId)
                }
                show()
            }
        }
    }

}

