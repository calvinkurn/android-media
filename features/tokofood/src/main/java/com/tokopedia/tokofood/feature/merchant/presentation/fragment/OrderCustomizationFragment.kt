package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.FragmentOrderCustomizationLayoutBinding
import com.tokopedia.tokofood.feature.merchant.analytics.MerchantPageAnalytics
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.CustomListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderNoteInputViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.model.VariantWrapperUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductAddOnViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.OrderCustomizationViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class OrderCustomizationFragment : BaseMultiFragment(),
        ProductAddOnViewHolder.OnAddOnSelectListener,
        OrderNoteInputViewHolder.OnNoteTextChangeListener {

    companion object {

        const val BUNDLE_KEY_VARIANT_TRACKER = "variant_tracker"
        private const val BUNDLE_KEY_PRODUCT_UI_MODEL = "productUiModel"
        private const val BUNDLE_KEY_CART_ID = "cartId"
        private const val BUNDLE_KEY_MERCHANT_ID = "merchantId"
        private const val BUNDLE_KEY_CACHE_MANAGER_ID = "cache_manager_id"
        private const val SOURCE = "merchant_page"

        @JvmStatic
        fun createInstance(productUiModel: ProductUiModel,
                           cartId: String = "",
                           merchantId: String = "",
                           cacheManagerId: String
        ) = OrderCustomizationFragment().apply {
            this.arguments = Bundle().apply {
                putParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL, productUiModel)
                putString(BUNDLE_KEY_CART_ID, cartId)
                putString(BUNDLE_KEY_MERCHANT_ID, merchantId)
                putString(BUNDLE_KEY_CACHE_MANAGER_ID, cacheManagerId)
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

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        activity?.let {
            DaggerMerchantPageComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        setVariantWrapperFromCacheManager()
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)

        val productUiModel = arguments?.getParcelable<ProductUiModel>(BUNDLE_KEY_PRODUCT_UI_MODEL)
        val cartId = arguments?.getString(BUNDLE_KEY_CART_ID) ?: ""
        val merchantId = arguments?.getString(BUNDLE_KEY_MERCHANT_ID) ?: ""

        context?.run {
            if (cartId.isNotBlank()) binding?.atcButton?.text = getString(com.tokopedia.tokofood.R.string.action_update)
        }

        productUiModel?.run {

            binding?.tpgProductName?.text = productUiModel.name
            binding?.qeuProductQtyEditor?.setValue(orderQty)

            val customListItems = viewModel.getCustomListItems(cartId, productUiModel)
            customListItems.run { setupCustomList(this) }

            viewModel.baseProductPrice = productUiModel.price

            // no selections
            if (!isAtc) {
                binding?.subtotalProductPriceLabel?.text = priceFmt
            } else binding?.subtotalProductPriceLabel?.text = subTotalFmt

            // setup quantity editor
            binding?.qeuProductQtyEditor?.setAddClickListener {
                val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
                val quantity = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE
                val subTotalPrice = viewModel.calculateSubtotalPrice(
                        baseProductPrice = viewModel.baseProductPrice,
                        quantity = quantity,
                        addOnUiModels = addOnUiModels?: listOf()
                )
                binding?.subtotalProductPriceLabel?.text = viewModel.formatSubtotalPrice(subTotalPrice)
            }
            binding?.qeuProductQtyEditor?.setSubstractListener {
                val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
                val quantity = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE
                val subTotalPrice = viewModel.calculateSubtotalPrice(
                        baseProductPrice = viewModel.baseProductPrice,
                        quantity = quantity,
                        addOnUiModels = addOnUiModels?: listOf()
                )
                binding?.subtotalProductPriceLabel?.text = viewModel.formatSubtotalPrice(subTotalPrice)
            }

            // setup atc button click listener
            binding?.atcButton?.setOnClickListener {
                customListAdapter?.getCustomListItems()?.run {

                    //hit trackers
                    merchantPageAnalytics.clickOnOrderVariantPage(
                        variantWrapperUiModel?.productListItem,
                        variantWrapperUiModel?.merchantId.orEmpty(),
                        variantWrapperUiModel?.tokoFoodMerchantProfile,
                        variantWrapperUiModel?.position.orZero()
                    )

                    val validationResult = viewModel.validateCustomOrderInput(this)
                    val isError = validationResult.first
                    if (isError) {
                        customListAdapter?.setCustomListItems(validationResult.second)
                        return@setOnClickListener
                    }
                    // exclude the last custom list item which contain order note information
                    val addOnUiModels = this.filter { it.addOnUiModel != null }.map {
                        it.addOnUiModel ?: AddOnUiModel()
                    }
                    val updateParam = viewModel.generateRequestParam(
                            shopId = merchantId,
                            productUiModel = productUiModel,
                            cartId = cartId,
                            orderNote = this.last().orderNote,
                            orderQty = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE,
                            addOnUiModels = addOnUiModels
                    )
                    if (viewModel.isEditingCustomOrder(cartId)) activityViewModel?.updateCart(updateParam = updateParam, source = SOURCE)
                    else activityViewModel?.addToCart(updateParam = updateParam, source = SOURCE)
                    parentFragmentManager.popBackStack()
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

    private fun setVariantWrapperFromCacheManager() {
        val cacheManager = context?.let {
            SaveInstanceCacheManager(
                it,
                arguments?.getString(BUNDLE_KEY_CACHE_MANAGER_ID)
            )
        }
        val variantWrapperUiModel = cacheManager?.get(
            BUNDLE_KEY_VARIANT_TRACKER,
            VariantWrapperUiModel::class.java) ?: VariantWrapperUiModel()

        this.variantWrapperUiModel = variantWrapperUiModel
    }

    override fun onAddOnSelected(isSelected: Boolean, addOnPrice: Double, addOnPositions: Pair<Int, Int>) {
        customListAdapter?.updateAddOnSelection(isSelected, addOnPositions)
        val addOnUiModels = customListAdapter?.getCustomListItems()?.map { it.addOnUiModel }
        val quantity = binding?.qeuProductQtyEditor?.getValue() ?: Int.ONE
        val subTotalPrice = viewModel.calculateSubtotalPrice(
                baseProductPrice = viewModel.baseProductPrice,
                quantity = quantity,
                addOnUiModels = addOnUiModels?: listOf()
        )
        binding?.subtotalProductPriceLabel?.text = viewModel.formatSubtotalPrice(subTotalPrice)
    }

    override fun onNoteTextChanged(orderNote: String, dataSetPosition: Int) {
        customListAdapter?.updateOrderNote(orderNote, dataSetPosition)
    }
}