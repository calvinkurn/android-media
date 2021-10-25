package com.tokopedia.product.manage.feature.stockreminder.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant.EXTRA_PRODUCT_NAME
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.databinding.FragmentStockReminderBinding
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_RESULT_STATUS
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.EXTRA_THRESHOLD
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.CreateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.createupdateresponse.UpdateStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse.GetStockReminderResponse
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.viewmodel.StockReminderViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class StockReminderFragment: BaseDaggerFragment() {

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_STOCK = "stock"
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"
        private const val MINIMUM_STOCK = 1
        private const val EMPTY_INPUT_STOCK = 0

        fun createInstance(productId: Long, productName: String, stock: Int): Fragment {
            val fragment = StockReminderFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_PRODUCT_ID, productId.toString())
                putString(ARG_PRODUCT_NAME, productName)
                putInt(ARG_STOCK, stock)
            }
            return fragment
        }
    }

    private var firstStateChecked: Boolean = false
    private var toggleStateChecked: Boolean? = null
    private var productId: String? = null
    private var productName: String? = null
    private var warehouseId: String? = null
    private var threshold: Int? = null
    private var stock: Int = 0

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
        arguments?.getString(ARG_PRODUCT_ID)?.let{ productId = it }
        arguments?.getString(ARG_PRODUCT_NAME)?.let { productName = it }
        arguments?.getInt(ARG_STOCK)?.let { stock = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentStockReminderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLogin()
        initView()
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
        viewModel.getStockReminderLiveData.observe(viewLifecycleOwner, getStockReminderObserver())
        viewModel.createStockReminderLiveData.observe(viewLifecycleOwner, createStockReminderObserver())
        viewModel.updateStockReminderLiveData.observe(viewLifecycleOwner, updateStockReminderObserver())

        getStockReminder()

        when(stock) {
            0 -> binding?.qeStock?.maxValue = 1
            else -> binding?.qeStock?.maxValue = stock
        }

        binding?.qeStock?.run {
            editText.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFocus()
                    KeyboardHandler.DropKeyboard(activity, this)
                }
                true
            }
        }

        addStockEditorTextChangedListener()
        setAddButtonClickListener()
        setSubtractButtonClickListener()

        binding?.swStockReminder?.setOnCheckedChangeListener { _, isChecked ->
            toggleStateChecked = isChecked
            toggleStateChecked?.let { state ->
                binding?.containerStockReminder?.showWithCondition(state)
            }

            if(firstStateChecked) {
                if(isChecked) {
                    ProductManageTracking.eventToggleReminder(TOGGLE_ACTIVE)
                } else {
                    ProductManageTracking.eventToggleReminder(TOGGLE_NOT_ACTIVE)
                }
            }
            firstStateChecked = true
        }

        binding?.btnSaveReminder?.setOnClickListener {
            binding?.qeStock?.clearFocus()
            if (binding?.swStockReminder?.isChecked == true) {
                if (threshold == 0) {
                    threshold = binding?.qeStock?.getValue()
                    createStockReminder()
                } else {
                    threshold = binding?.qeStock?.getValue()
                    updateStockReminder()
                }
            } else {
                threshold = 0
                updateStockReminder()
            }

            toggleStateChecked?.let { state ->
                if (state) {
                    ProductManageTracking.eventToggleReminderSave(TOGGLE_ACTIVE)
                } else {
                    ProductManageTracking.eventToggleReminderSave(TOGGLE_NOT_ACTIVE)
                }
            }
        }
    }

    private fun showLoading() {
        binding?.ivLoadingStockReminder?.let { iv ->
            ImageHandler.loadGif(
                iv,
                com.tokopedia.resources.common.R.drawable.ic_loading_indeterminate,
                com.tokopedia.resources.common.R.drawable.ic_loading_indeterminate)
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
        val extraCacheManagerId = activity?.intent?.data?.getQueryParameter(ApplinkConstInternalMarketplace.ARGS_CACHE_MANAGER_ID).orEmpty()
        if (extraCacheManagerId.isNotBlank()) {
            val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, extraCacheManagerId) }
            cacheManager?.let {
                it.put(EXTRA_PRODUCT_NAME, productName)
                it.put(EXTRA_THRESHOLD, threshold)
                it.put(EXTRA_RESULT_STATUS, Activity.RESULT_OK)
            }
        } else {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_PRODUCT_NAME, productName)
            resultIntent.putExtra(EXTRA_THRESHOLD, threshold)
            activity?.setResult(Activity.RESULT_OK, resultIntent)
        }
        activity?.finish()
    }

    private fun getStockReminder() {
        showLoading()
        productId?.let { viewModel.getStockReminder(it) }
    }

    private fun createStockReminder() {
        showLoading()
        productId?.let { productId ->
            warehouseId?.let { warehouseId ->
                viewModel.createStockReminder(userSession.shopId, productId, warehouseId, threshold.toString())
            }
        }
    }

    private fun updateStockReminder() {
        showLoading()
        productId?.let { productId ->
            warehouseId?.let { warehouseId ->
                viewModel.updateStockReminder(userSession.shopId, productId, warehouseId, threshold.toString())
            }
        }
    }

    private fun getStockReminderObserver() = Observer<Result<GetStockReminderResponse>> { stockReminderData ->
        hideLoading()
        when(stockReminderData) {
            is Success -> {
                warehouseId = stockReminderData.data.getByProductIds.data.getOrNull(0)?.productsWareHouse?.getOrNull(0)?.wareHouseId
                threshold = stockReminderData.data.getByProductIds.data.getOrNull(0)?.productsWareHouse?.getOrNull(0)?.threshold

                threshold?.let {
                    binding?.qeStock?.setValue(it)
                }
                binding?.swStockReminder?.isChecked = threshold != 0
                if(binding?.swStockReminder?.isChecked == false) {
                    firstStateChecked = true
                }
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
            }
        }
    }

    private fun createStockReminderObserver()= Observer<Result<CreateStockReminderResponse>> { stockReminderData ->
        hideLoading()
        when(stockReminderData) {
            is Success -> { doResultIntent() }
            is Fail -> {
                binding?.layout?.let { layout ->
                    Toaster.build(
                        layout,
                        getString(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR,
                        getString(R.string.product_stock_reminder_toaster_action_text)
                    ).show()
                }
                Toaster.onCTAClick = View.OnClickListener { createStockReminder() }
                ProductManageListErrorHandler.logExceptionToCrashlytics(stockReminderData.throwable)
            }
        }
    }

    private fun updateStockReminderObserver() = Observer<Result<UpdateStockReminderResponse>> { stockReminderData ->
        hideLoading()
        when(stockReminderData) {
            is Success -> { doResultIntent() }
            is Fail -> {
                binding?.layout?.let { layout ->
                    Toaster.build(
                        layout,
                        getString(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR,
                        getString(R.string.product_stock_reminder_toaster_action_text)
                    ).show()
                    Toaster.onCTAClick = View.OnClickListener { updateStockReminder() }
                    ProductManageListErrorHandler.logExceptionToCrashlytics(stockReminderData.throwable)
                }
            }
        }
    }

    private fun addStockEditorTextChangedListener() {
        binding?.qeStock?.run {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editor: Editable?) {
                    val input = editor.toString()
                    val stock = if(input.isNotEmpty()) {
                        input.toInt()
                    } else {
                        EMPTY_INPUT_STOCK
                    }
                    toggleQuantityEditorBtn(stock)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    private fun setAddButtonClickListener() {
        binding?.qeStock?.run {
            addButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }

                stock++

                if(stock <= binding?.qeStock?.maxValue.orZero()) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun setSubtractButtonClickListener() {
        binding?.qeStock?.run {
            subtractButton.setOnClickListener {
                val input = editText.text.toString()

                var stock = if(input.isNotEmpty()) {
                    input.toInt()
                } else {
                    EMPTY_INPUT_STOCK
                }

                stock--

                if(stock >= MINIMUM_STOCK) {
                    editText.setText(stock.getNumberFormatted())
                }
            }
        }
    }

    private fun toggleQuantityEditorBtn(stock: Int) {
        val enableAddBtn = stock < binding?.qeStock?.maxValue.orZero()
        val enableSubtractBtn = stock > MINIMUM_STOCK

        binding?.qeStock?.run {
            addButton.isEnabled = enableAddBtn
            subtractButton.isEnabled = enableSubtractBtn
        }
    }

    private fun String.toInt(): Int {
        return replace(".", "").toIntOrZero()
    }
}
