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
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.databinding.FragmentStockReminderBinding
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_RESULT_STATUS
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderAdapter
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderAdapterFactoryImpl
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderViewHolder
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel
import com.tokopedia.product.manage.feature.stockreminder.view.viewmodel.StockReminderViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class StockReminderFragment : BaseDaggerFragment(),
    ProductStockReminderViewHolder.ProductStockReminder {

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_IS_VARIANT = "isVariant"

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
    private var threshold: Int? = null

    private var isVariant: Boolean = false
    private var adapter: BaseAdapter<ProductStockReminderAdapterFactoryImpl>? = null
    private val listProductWarehouse = ArrayList<ProductWarehouseParam>()

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
        viewModel.updateStockReminderLiveData.observe(
            viewLifecycleOwner,
            updateStockReminderObserver()
        )
        getStockReminder()
//


//        binding?.swStockReminder?.setOnCheckedChangeListener { _, isChecked ->
//            toggleStateChecked = isChecked
//            toggleStateChecked?.let { state ->
//                binding?.containerStockReminder?.showWithCondition(state)
//            }
//
//            if (firstStateChecked) {
//                if (isChecked) {
//                    ProductManageTracking.eventToggleReminder(TOGGLE_ACTIVE)
//                } else {
//                    ProductManageTracking.eventToggleReminder(TOGGLE_NOT_ACTIVE)
//                }
//            }
//            firstStateChecked = true
//        }

        binding?.btnSaveReminder?.setOnClickListener {
//            binding?.qeStock?.clearFocus()
//            if (binding?.swStockReminder?.isChecked == true) {
//                if (threshold == 0) {
//                    threshold = binding?.qeStock?.getValue()
            createStockReminder()
//                } else {
//                    threshold = binding?.qeStock?.getValue()
//                    updateStockReminder()
//                }
//            } else {
//                threshold = 0
//                updateStockReminder()
//            }
//
//            toggleStateChecked?.let { state ->
//                if (state) {
//                    ProductManageTracking.eventToggleReminderSave(TOGGLE_ACTIVE)
//                } else {
//                    ProductManageTracking.eventToggleReminderSave(TOGGLE_NOT_ACTIVE)
//                }
//            }
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
            listProductWarehouse
        )
    }

    private fun updateStockReminder() {
        productId?.let { productId ->
            warehouseId?.let { warehouseId ->
                viewModel.updateStockReminder(
                    userSession.shopId,
                    productId,
                    warehouseId,
                    threshold.toString()
                )
            }
        }
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
        listProductWarehouse.addAll(productWarehouse)
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

    private fun updateStockReminderObserver() =
        Observer<Result<UpdateStockReminderResponse>> { stockReminderData ->
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
                                updateStockReminder()
                            }
                        ).show()
                        ProductManageListErrorHandler.logExceptionToCrashlytics(stockReminderData.throwable)
                        ProductManageListErrorHandler.logExceptionToServer(
                            errorTag = ProductManageListErrorHandler.PRODUCT_MANAGE_TAG,
                            throwable = stockReminderData.throwable,
                            errorType =
                            ProductManageListErrorHandler.ProductManageMessage.UPDATE_STOCK_REMINDER_ERROR,
                            deviceId = userSession.deviceId.orEmpty()
                        )
                    }
                }
            }
        }

    private fun getProductWareHouseList(): List<ProductWarehouseParam> {
        return listProductWarehouse.toList()
    }

    private fun updateProductWareHouseList(
        productId: String,
        block: (ProductWarehouseParam) -> ProductWarehouseParam
    ) {
        getProductWareHouseList().firstOrNull { it.productId == productId }?.let {
            val index = listProductWarehouse.indexOf(it)
            listProductWarehouse[index] = block.invoke(it)
        }
    }

    override fun onChangeStockReminder(productId: String, stock: Int, status: Int) {
        updateProductWareHouseList(productId) {
            it.copy(threshold = stock.toString(), thresholdStatus = status.toString())
        }

    }
}
