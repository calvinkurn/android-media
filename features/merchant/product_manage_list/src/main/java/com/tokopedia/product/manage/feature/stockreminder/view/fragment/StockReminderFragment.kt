package com.tokopedia.product.manage.feature.stockreminder.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.databinding.FragmentStockReminderBinding
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_RESULT_STATUS
import com.tokopedia.product.manage.feature.stockreminder.constant.StockReminderConst.REMINDER_ACTIVE
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderAdapter
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderAdapterFactoryImpl
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderViewHolder
import com.tokopedia.product.manage.feature.stockreminder.view.bottomsheet.SetStockForVariantSelectionReminderBottomSheet
import com.tokopedia.product.manage.feature.stockreminder.view.bottomsheet.StockRemainingInfoBottomSheet
import com.tokopedia.product.manage.feature.stockreminder.view.bottomsheet.VariantSelectionStockReminderBottomSheet
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.data.mapper.ProductStockReminderMapper.mapToGroupVariant
import com.tokopedia.product.manage.feature.stockreminder.view.viewmodel.StockReminderViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class StockReminderFragment : BaseDaggerFragment(),
    ProductStockReminderViewHolder.ProductStockReminderListener {

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_IS_VARIANT = "isVariant"
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"

        fun createInstance(productId: Long, productName: String, isVariant: Boolean): Fragment {
            val fragment = StockReminderFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId.toString())
                putString(ARG_PRODUCT_NAME, productName)
                putBoolean(ARG_IS_VARIANT, isVariant)
            }
            return fragment
        }
    }

    private var productId: String? = null
    private var productName: String? = null
    private var warehouseId: String? = null

    private var isVariant: Boolean = false
    private var adapter: BaseAdapter<ProductStockReminderAdapterFactoryImpl>? = null

    private val updateWarehouseParam = ArrayList<ProductWarehouseParam>()

    private var dataProducts: ArrayList<ProductStockReminderUiModel>? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(StockReminderViewModel::class.java) }

    private var binding by autoClearedNullable<FragmentStockReminderBinding>()

    override fun getScreenName(): String = javaClass.simpleName

    override fun initInjector() {
        activity?.let {
            DaggerStockReminderComponent
                .builder()
                .productManageComponent(ProductManageInstance.getComponent(it.application))
                .build()
                .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(ARG_PRODUCT_ID)?.let { productId = it }
        arguments?.getString(ARG_PRODUCT_NAME)?.let { productName = it }
        arguments?.getBoolean(ARG_IS_VARIANT)?.let { isVariant = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentStockReminderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLogin()
        initView()
        observeState()
    }

    override fun onChangeStockReminder(
        productId: String,
        stock: Int,
        status: Int,
        isValid: Boolean
    ) {
        updateProductWareHouseList(productId) {
            it.copy(threshold = stock.toString(), thresholdStatus = status.toString())
        }
        binding?.btnSaveReminder?.isEnabled = isValid

    }

    private fun checkLogin() {
        if (!userSession.isLoggedIn) {
            RouteManager.route(context, ApplinkConst.LOGIN)
            activity?.finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(context, ApplinkConst.HOME)
            activity?.finish()
        }
    }

    private fun initView() {
        setupAdapterProduct()
        viewModel.getProductLiveData.observe(viewLifecycleOwner, getProduct())
        viewModel.getStockReminderLiveData.observe(viewLifecycleOwner, getStockReminderObserver())
        viewModel.createStockReminderLiveData.observe(
            viewLifecycleOwner,
            createStockReminderObserver()
        )
        getStockReminder()

        binding?.clEditAll?.showWithCondition(isVariant)

        val tickerData = getTicker()
        val tickerAdapter = TickerPagerAdapter(
            requireContext(),
            tickerData
        )
        binding?.tickerStockReminder?.addPagerView(tickerAdapter, tickerData)
        binding?.ivEdit?.setOnClickListener {
            showVariantSelectionBottomSheet()
        }

        binding?.btnSaveReminder?.setOnClickListener {
            createStockReminder()
            val isReminderActive =
                updateWarehouseParam.firstOrNull() { it.thresholdStatus == REMINDER_ACTIVE.toString() }
            if (isReminderActive != null) {
                ProductManageTracking.eventToggleReminderSave(TOGGLE_ACTIVE)
            } else {
                ProductManageTracking.eventToggleReminderSave(TOGGLE_NOT_ACTIVE)
            }
        }

        binding?.clRemainingStock?.setOnClickListener {
            showRemainingStockBottomSheet()
        }

    }

    private fun createAdapter(): BaseAdapter<ProductStockReminderAdapterFactoryImpl> {
        return ProductStockReminderAdapter(ProductStockReminderAdapterFactoryImpl(this))
    }

    private fun setupAdapterProduct() {
        adapter = createAdapter()
        binding?.rvProduct?.run {
            adapter = this@StockReminderFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeState() {
        observe(viewModel.showLoading) { showProgressBar ->
            if (showProgressBar) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        binding?.ivLoadingStockReminder?.let { iv ->
            ImageHandler.loadGif(
                iv,
                com.tokopedia.resources.common.R.drawable.ic_loading_indeterminate,
                com.tokopedia.resources.common.R.drawable.ic_loading_indeterminate
            )
        }
        binding?.loadingStockReminder?.visibility = View.VISIBLE
        binding?.cardSaveBtn?.visibility = View.GONE
    }

    private fun hideLoading() {
        binding?.ivLoadingStockReminder?.let { iv ->
            ImageHandler.clearImage(iv)
        }
        binding?.loadingStockReminder?.visibility = View.GONE
        binding?.cardSaveBtn?.visibility = View.VISIBLE
    }

    private fun doResultIntent() {
        val extraCacheManagerId =
            activity?.intent?.data?.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID)
                .orEmpty()
        if (extraCacheManagerId.isNotBlank()) {
            val cacheManager =
                context?.let { context -> SaveInstanceCacheManager(context, extraCacheManagerId) }
            cacheManager?.let {
                it.put(EXTRA_RESULT_STATUS, Activity.RESULT_OK)
            }
        } else {
            val resultIntent = Intent()
            activity?.setResult(Activity.RESULT_OK, resultIntent)
        }
        activity?.finish()
    }

    private fun getStockReminder() {
        productId?.let { viewModel.getStockReminder(it) }
    }

    private fun createStockReminder() {
        viewModel.createStockReminder(
            userSession.shopId,
            updateWarehouseParam
        )
    }

    private fun getStockReminderObserver() =
        Observer<Result<GetStockReminderResponse>> { stockReminderData ->
            when (stockReminderData) {
                is Success -> {
                    warehouseId =
                        stockReminderData.data.getByProductIds.data.getOrNull(0)?.productsWareHouse?.getOrNull(
                            0
                        )?.wareHouseId
                    viewModel.getProduct(productId.orEmpty(), warehouseId.orEmpty())
                }
                is Fail -> {
                    binding?.cardSaveBtn?.visibility = View.GONE
                    binding?.globalErrorStockReminder?.visibility = View.VISIBLE
                    binding?.geStockReminder?.setType(GlobalError.SERVER_ERROR)
                    binding?.geStockReminder?.setActionClickListener {
                        binding?.globalErrorStockReminder?.visibility = View.GONE
                        binding?.cardSaveBtn?.visibility = View.VISIBLE
                        getStockReminder()
                    }
                    ProductManageListErrorHandler.logExceptionToCrashlytics(stockReminderData.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = stockReminderData.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.GET_STOCK_REMINDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }

    private fun getProduct() = Observer<Result<List<ProductStockReminderUiModel>>> { productData ->
        when (productData) {
            is Success -> {
                val product = productData.data
                dataProducts = ArrayList(product)
                adapter?.addElement(product)
                setListProductWarehouse(product)
            }
            is Fail -> {
                binding?.cardSaveBtn?.visibility = View.GONE
                binding?.globalErrorStockReminder?.visibility = View.VISIBLE
                binding?.geStockReminder?.setType(GlobalError.SERVER_ERROR)
                binding?.geStockReminder?.setActionClickListener {
                    binding?.globalErrorStockReminder?.visibility = View.GONE
                    binding?.cardSaveBtn?.visibility = View.VISIBLE
                    viewModel.getProduct(productId.toString(), warehouseId.toString())
                }
                ProductManageListErrorHandler.logExceptionToCrashlytics(productData.throwable)
                ProductManageListErrorHandler.logExceptionToServer(
                    errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                    throwable = productData.throwable,
                    errorType =
                    ProductManageListErrorHandler.ProductManageMessage.GET_STOCK_REMINDER_ERROR,
                    deviceId = userSession.deviceId.orEmpty()
                )
            }
        }

    }

    private fun setListProductWarehouse(data: List<ProductStockReminderUiModel>) {
        val productWarehouse = data.map {
            ProductWarehouseParam(
                productId = it.id,
                wareHouseId = warehouseId.orEmpty(),
                threshold = it.stockAlertCount.toString(),
                thresholdStatus = it.stockAlertStatus.toString()
            )
        }
        updateWarehouseParam.addAll(productWarehouse)
    }

    private fun createStockReminderObserver() =
        Observer<Result<CreateStockReminderResponse>> { stockReminderData ->
            when (stockReminderData) {
                is Success -> {
                    doResultIntent()
                }
                is Fail -> {
                    binding?.layout?.let { layout ->
                        Toaster.build(
                            layout,
                            getString(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc),
                            Snackbar.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(R.string.product_stock_reminder_toaster_action_text),
                            clickListener = {
                                createStockReminder()
                            }
                        ).show()
                    }
                    ProductManageListErrorHandler.logExceptionToCrashlytics(stockReminderData.throwable)
                    ProductManageListErrorHandler.logExceptionToServer(
                        errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                        throwable = stockReminderData.throwable,
                        errorType =
                        ProductManageListErrorHandler.ProductManageMessage.CREATE_STOCK_REMINDER_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }

    private fun getTicker(): List<TickerData> {
        if (userSession.isMultiLocationShop) {
            return listOf(
                TickerData(
                    description = activity?.getString(R.string.product_stock_reminder_ticker_multi_location_1)
                        .orEmpty(),
                    type = Ticker.TYPE_ANNOUNCEMENT
                ),
                TickerData(
                    description = activity?.getString(R.string.product_stock_reminder_ticker_multi_location_2)
                        .orEmpty(),
                    type = Ticker.TYPE_ANNOUNCEMENT
                )
            )
        } else {
            return listOf(
                TickerData(
                    description = activity?.getString(R.string.product_stock_reminder_ticker)
                        .orEmpty(),
                    type = Ticker.TYPE_ANNOUNCEMENT
                ),
            )
        }
    }

    private fun showVariantSelectionBottomSheet() {
        val groupVariantBottomSheet = VariantSelectionStockReminderBottomSheet(
            childFragmentManager
        )
        groupVariantBottomSheet.setData(dataProducts.orEmpty().mapToGroupVariant())
        groupVariantBottomSheet.setOnNextListener { dataSelection ->
            showSetOnceStockReminderBottomSheet(dataSelection)
        }
        groupVariantBottomSheet.show()

    }

    private fun showSetOnceStockReminderBottomSheet(selection: List<ProductStockReminderUiModel>) {
        val setStockForVariantSelectionReminderBottomSheet =
            SetStockForVariantSelectionReminderBottomSheet(
                childFragmentManager
            )
        setStockForVariantSelectionReminderBottomSheet.setOnApplyListener { stockReminder, reminderStatus ->
            updateFromBulkSetting(stockReminder, reminderStatus, selection)
        }
        setStockForVariantSelectionReminderBottomSheet.show()

    }

    private fun showRemainingStockBottomSheet() {
        val stockRemainingInfoBottomSheet = StockRemainingInfoBottomSheet(
            childFragmentManager
        )
        stockRemainingInfoBottomSheet.show()
    }

    private fun getProductWareHouseList(): List<ProductWarehouseParam> {
        return updateWarehouseParam.toList()
    }

    private fun updateProductWareHouseList(
        productId: String,
        block: (ProductWarehouseParam) -> ProductWarehouseParam
    ) {
        getProductWareHouseList().firstOrNull { it.productId == productId }?.let {
            val index = updateWarehouseParam.indexOf(it)
            updateWarehouseParam[index] = block.invoke(it)
        }
    }

    private fun updateFromBulkSetting(
        stockReminder: Int,
        reminderStatus: Int,
        selection: List<ProductStockReminderUiModel>
    ) {
        val newDataProduct = arrayListOf<ProductStockReminderUiModel>()
        dataProducts?.forEach { existing ->
            val newData = selection.firstOrNull { it.id == existing.id }
            if (newData != null) {
                newData.stockAlertCount = stockReminder
                newData.stockAlertStatus = reminderStatus
                newDataProduct.add(newData)
            } else {
                newDataProduct.add(existing)
            }
        }

        dataProducts = newDataProduct
        adapter?.clearAllElements()
        adapter?.addElement(newDataProduct)
        binding?.btnSaveReminder?.isEnabled = selection.isNotEmpty()

    }

}
