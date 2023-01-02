package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodBottomSheet
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodExt
import com.tokopedia.tokofood.common.util.TokofoodExt.copyParcelable
import com.tokopedia.tokofood.common.util.TokofoodExt.setupEditText
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentOrderCustomizationLayoutBinding
import com.tokopedia.tokofood.feature.merchant.analytics.MerchantPageAnalytics
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.CustomListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.PhoneNumberVerificationBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.VariantWrapperUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderNoteInputViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductAddOnViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.OrderCustomizationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class OrderCustomizationFragment : BaseMultiFragment(),
    ProductAddOnViewHolder.OnAddOnSelectListener,
    OrderNoteInputViewHolder.OnNoteTextChangeListener,
    PhoneNumberVerificationBottomSheet.OnButtonCtaClickListener {

    companion object {

        const val BUNDLE_KEY_VARIANT_TRACKER = "variant_tracker"
        private const val BUNDLE_KEY_PRODUCT_UI_MODEL = "productUiModel"
        private const val BUNDLE_KEY_CART_ID = "cartId"
        private const val BUNDLE_KEY_MERCHANT_ID = "merchantId"
        private const val BUNDLE_KEY_CACHE_MANAGER_ID = "cache_manager_id"
        private const val SOURCE = "merchant_page"
        private const val BUNDLE_KEY_SOURCE = "source"
        private const val BUNDLE_KEY_IS_CHANGE_MERCHANT = "is_change_merchant"

        @JvmStatic
        fun createInstance(
            productUiModel: ProductUiModel,
            cartId: String = "",
            merchantId: String = "",
            source: String = "",
            cacheManagerId: String? = null,
            isChangeMerchant: Boolean = false
        ) = OrderCustomizationFragment().apply {
            this.arguments = Bundle().apply {
                putParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL, productUiModel)
                putString(BUNDLE_KEY_CART_ID, cartId)
                putString(BUNDLE_KEY_MERCHANT_ID, merchantId)
                putString(BUNDLE_KEY_SOURCE, source)
                putBoolean(BUNDLE_KEY_IS_CHANGE_MERCHANT, isChangeMerchant)
                if (cacheManagerId != null) {
                    putString(BUNDLE_KEY_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var merchantPageAnalytics: MerchantPageAnalytics

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(OrderCustomizationViewModel::class.java)
    }

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var binding: FragmentOrderCustomizationLayoutBinding? = null

    private var customListAdapter: CustomListAdapter? = null

    private var variantWrapperUiModel: VariantWrapperUiModel? = null

    private var productUiModel: ProductUiModel? = null
    private var source: String = String.EMPTY

    override fun getFragmentToolbar(): Toolbar? = binding?.toolbarOrderCustomization

    override fun getFragmentTitle(): String? {
        return productUiModel?.name
    }

    override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
        return BaseMultiFragmentLaunchMode.SINGLE_TASK
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun initInjector() {
        activity?.let {
            DaggerMerchantPageComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentOrderCustomizationLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackgroundColor()
        setDataFromCacheManagerOrArguments()
        observeUpdateCart()

        val cartId = arguments?.getString(BUNDLE_KEY_CART_ID) ?: ""
        val merchantId = arguments?.getString(BUNDLE_KEY_MERCHANT_ID) ?: ""
        val isChangeMerchant = arguments?.getBoolean(BUNDLE_KEY_IS_CHANGE_MERCHANT, false) ?: false

        source = arguments?.getString(BUNDLE_KEY_SOURCE) ?: SOURCE

        context?.run {
            if (cartId.isNotBlank()) binding?.atcButton?.text = getString(com.tokopedia.tokofood.R.string.action_update)
        }

        productUiModel?.apply {
            setupHeaderToolbar(name)
            // set the product quantity based on custom order qty
            customOrderDetails.firstOrNull { it.cartId == cartId }?.let { customOrderDetail ->
                binding?.qeuProductQtyEditor?.setValue(customOrderDetail.qty)
            }

            val customListItems = viewModel.getCustomListItems(cartId, this)
            customListItems.run { setupCustomList(this) }

            viewModel.baseProductPrice = price

            // no selections
            if (!isAtc) {
                binding?.subtotalProductPriceLabel?.text = priceFmt
            } else {
                customOrderDetails.firstOrNull { it.cartId == cartId }?.let { customOrderDetail ->
                    val subTotalPrice = viewModel.calculateSubtotalPrice(
                            baseProductPrice = viewModel.baseProductPrice,
                            quantity = customOrderDetail.qty,
                            addOnUiModels = customOrderDetail.customListItems.map { it.addOnUiModel }
                    )
                    binding?.subtotalProductPriceLabel?.text = viewModel.formatSubtotalPrice(subTotalPrice)
                }
            }

            // setup quantity editor
            binding?.qeuProductQtyEditor?.setupEditText()
            binding?.qeuProductQtyEditor?.maxValue = TokofoodExt.MAXIMUM_QUANTITY
            binding?.qeuProductQtyEditor?.setAddClickListener {
                val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
                val quantity = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE
                updateSubtotalPriceLabel(addOnUiModels, quantity)
            }
            binding?.qeuProductQtyEditor?.setSubstractListener {
                val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
                val quantity = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE
                updateSubtotalPriceLabel(addOnUiModels, quantity)
            }
            binding?.qeuProductQtyEditor?.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
                    val quantity = binding?.qeuProductQtyEditor?.getValue().orZero()
                    updateSubtotalPriceLabel(addOnUiModels, quantity)
                }
                override fun afterTextChanged(p0: Editable?) {}
            })

            // setup atc button click listener
            binding?.atcButton?.setOnClickListener {
                customListAdapter?.getCustomListItems()?.run {

                    val validationResult = viewModel.validateCustomOrderInput(this)
                    val isError = validationResult.first
                    if (isError) {
                        customListAdapter?.setCustomListItems(validationResult.second)
                        showErrorMessage()
                        return@setOnClickListener
                    }
                    // exclude the last custom list item which contain order note information
                    val addOnUiModels = this.filter { it.addOnUiModel != null }.map {
                        it.addOnUiModel ?: AddOnUiModel()
                    }
                    val updateParam = viewModel.generateRequestParam(
                        shopId = merchantId,
                        productUiModel = this@apply,
                        cartId = cartId,
                        orderNote = this.lastOrNull()?.orderNote.orEmpty(),
                        orderQty = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE,
                        addOnUiModels = addOnUiModels
                    )
                    binding?.atcButton?.isLoading = true
                    if (viewModel.isEditingCustomOrder(cartId)) {
                        activityViewModel?.updateCart(
                            updateParam = updateParam,
                            source = source
                        )
                    } else {
                        if (isChangeMerchant) {
                            activityViewModel?.deleteAllAtcAndAddProduct(
                                updateParam = updateParam,
                                source = source
                            )
                        } else {
                            activityViewModel?.addToCart(
                                updateParam = updateParam,
                                source = source
                            )
                        }
                        //hit trackers
                        merchantPageAnalytics.clickOnOrderVariantPage(
                            variantWrapperUiModel?.productListItem,
                            variantWrapperUiModel?.merchantId.orEmpty(),
                            variantWrapperUiModel?.merchantName.orEmpty(),
                            variantWrapperUiModel?.position.orZero()
                        )
                    }
                }
            }
        }
    }

    private fun setupBackgroundColor() {
        activity?.let {
            it.window.decorView.setBackgroundColor(
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background)
            )
        }
    }

    private fun setupHeaderToolbar(foodName: String) {
        activity?.let {
            (it as? AppCompatActivity)?.apply {
                binding?.toolbarOrderCustomization?.run {
                    title = foodName
                    subtitle = it.getString(com.tokopedia.tokofood.R.string.text_header_order_custom)
                    setNavigationOnClickListener {
                        onBackPressed()
                    }
                }
            }
        }
    }

    private fun observeUpdateCart() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect {
                when (it.state) {
                    UiEvent.EVENT_HIDE_LOADING_ADD_TO_CART, UiEvent.EVENT_HIDE_LOADING_UPDATE_TO_CART -> {
                        binding?.atcButton?.isLoading = false
                        hideKeyboard()
                        parentFragmentManager.popBackStack()
                    }
                    UiEvent.EVENT_PHONE_VERIFICATION -> {
                        binding?.atcButton?.isLoading = false
                        val bottomSheetData = it.data as? CartTokoFoodBottomSheet
                        bottomSheetData?.run {
                            if (isShowBottomSheet) {
                                val bottomSheet = PhoneNumberVerificationBottomSheet.createInstance(bottomSheetData = this)
                                bottomSheet.setClickListener(this@OrderCustomizationFragment)
                                bottomSheet.show(childFragmentManager)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupCustomList(customListItems: List<CustomListItem>) {
        customListAdapter = CustomListAdapter(this, this)
        binding?.rvCustomList?.let {
            it.adapter = customListAdapter
            it.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        customListAdapter?.setCustomListItems(customListItems = customListItems)
    }

    private fun setDataFromCacheManagerOrArguments() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(BUNDLE_KEY_CACHE_MANAGER_ID)
            )
        }
        val variantWrapperUiModel = cacheManager?.get(
            BUNDLE_KEY_VARIANT_TRACKER,
            VariantWrapperUiModel::class.java
        ) ?: VariantWrapperUiModel()

        this.variantWrapperUiModel = variantWrapperUiModel

        val productUiParcelable = arguments?.getParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL) ?: ProductUiModel()
        this.productUiModel = productUiParcelable.copyParcelable()
    }

    private fun showErrorMessage() {
        view?.let { view ->
            Toaster.build(
                view = view,
                text = getString(com.tokopedia.tokofood.R.string.text_error_product_custom_selection),
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL
            ).show()
        }
    }

    private fun hideKeyboard() {
        try {
            KeyboardHandler.hideSoftKeyboard(activity)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun updateSubtotalPriceLabel(addOnUiModels: List<AddOnUiModel?>?, quantity: Int) {
        val subTotalPrice = viewModel.calculateSubtotalPrice(
                baseProductPrice = viewModel.baseProductPrice,
                quantity = quantity,
                addOnUiModels = addOnUiModels ?: listOf()
        )
        binding?.subtotalProductPriceLabel?.text = viewModel.formatSubtotalPrice(subTotalPrice)
    }

    override fun onAddOnSelected(
        isSelected: Boolean,
        addOnPrice: Double,
        addOnPositions: Pair<Int, Int>
    ) {
        customListAdapter?.updateAddOnSelection(isSelected, addOnPositions)
        val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
        val quantity = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE
        val subTotalPrice = viewModel.calculateSubtotalPrice(
            baseProductPrice = viewModel.baseProductPrice,
            quantity = quantity,
            addOnUiModels = addOnUiModels ?: listOf()
        )
        binding?.subtotalProductPriceLabel?.text = viewModel.formatSubtotalPrice(subTotalPrice)
    }

    override fun onNoteTextChanged(orderNote: String, dataSetPosition: Int) {
        customListAdapter?.updateOrderNote(orderNote, dataSetPosition)
    }

    override fun onButtonCtaClickListener(appLink: String) {
        var applicationLink = ApplinkConstInternalUserPlatform.ADD_PHONE
        if (appLink.isNotEmpty()) applicationLink = appLink
        context?.run {
            TokofoodRouteManager.routePrioritizeInternal(this, applicationLink)
        }
    }
}
