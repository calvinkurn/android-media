package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.databinding.FragmentMerchantPageLayoutBinding
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.MerchantPageCarouselAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.ProductListAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.CustomOrderDetailBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.MerchantInfoBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.OrderNoteBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet.ProductDetailBottomSheet
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.MerchantCarouseItemViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.ProductCardViewHolder
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_FULL
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MerchantPageFragment : BaseMultiFragment(),
        MerchantCarouseItemViewHolder.OnCarouselItemClickListener,
        ProductCardViewHolder.OnProductCardItemClickListener,
        OrderNoteBottomSheet.OnSaveNoteButtonClickListener,
        ProductDetailBottomSheet.OnProductDetailClickListener, CustomOrderDetailBottomSheet.OnCustomOrderDetailClickListener {

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var binding: FragmentMerchantPageLayoutBinding? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(MerchantPageViewModel::class.java)
    }

    private var merchantInfoBottomSheet: MerchantInfoBottomSheet? = null
    private var orderNoteBottomSheet: OrderNoteBottomSheet? = null

    private var carouselAdapter: MerchantPageCarouselAdapter? = null
    private var productListAdapter: ProductListAdapter? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_merchant_page, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                Toast.makeText(requireContext(), "click on share", Toast.LENGTH_LONG).show()
                true
            }
            R.id.action_open_global_menu -> {
                RouteManager.route(requireContext(), ApplinkConst.HOME_NAVIGATION)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentMerchantPageLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        setupMerchantLogo()
        setupMerchantProfileCarousel()
        setupProductList()
        setupOrderNoteBottomSheet()
        observeLiveData()
        collectFlow()
        initializeMiniCartWidget()
    }

    private fun observeLiveData() {
        viewModel.getMerchantDataResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val merchantData = result.data.tokofoodGetMerchantData
                    // render ticker data if not empty
                    val tickerData = merchantData.ticker
                    if (!viewModel.isTickerDetailEmpty(tickerData)) {
                        renderTicker(tickerData)
                    }
                    // render merchant logo, name, categories, carousel
                    val merchantProfile = merchantData.merchantProfile
                    renderMerchantProfile(merchantProfile)
                    // setup merchant info bottom sheet
                    val name = merchantProfile.name
                    val address = merchantProfile.address
                    val merchantOpsHours = viewModel.mapOpsHourDetailsToMerchantOpsHours(merchantProfile.opsHourDetail)
                    setupMerchantInfoBottomSheet(name, address, merchantOpsHours)
                    // render product list
                    val isShopClosed = merchantProfile.opsHourFmt.isWarning
                    val foodCategories = merchantData.categories
                    val productListItems = viewModel.mapFoodCategoriesToProductListItems(isShopClosed, foodCategories)
                    val finalProductListItems = viewModel.applyProductSelection(productListItems, viewModel.selectedProducts)
                    renderProductList(finalProductListItems)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            activityViewModel?.cartDataValidationFlow?.collect {
                when (it.state) {
                    UiEvent.EVENT_SUCCESS_LOAD_CART -> {
                        activityViewModel?.cartDataFlow?.collect { cartData ->
                            viewModel.selectedProducts = cartData.availableSection.products
                            val dummyMerchantId = "88d9f5a4-7410-46f5-a835-93955b8e3496"
                            val dummyLatLong = "-6.2,106.816666"
                            viewModel.getMerchantData(dummyMerchantId, dummyLatLong, "")
                        }
                    }
                    UiEvent.EVENT_SUCCESS_ADD_TO_CART -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.let { requestParam ->
                                (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                    cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }?.let { cartTokoFood ->
                                        val cardPositions = viewModel.productMap[requestParam.productId]
                                        cardPositions?.run {
                                            productListAdapter?.updateProductUiModel(
                                                    cartTokoFood = cartTokoFood,
                                                    dataSetPosition = viewModel.getDataSetPosition(this),
                                                    adapterPosition = viewModel.getAdapterPosition(this)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_NOTES -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.let { requestParam ->
                                (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                    cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }?.let { cartTokoFood ->
                                        val cardPositions = viewModel.productMap[requestParam.productId]
                                        cardPositions?.run {
                                            productListAdapter?.updateProductUiModel(
                                                    cartTokoFood = cartTokoFood,
                                                    dataSetPosition = viewModel.getDataSetPosition(this),
                                                    adapterPosition = viewModel.getAdapterPosition(this)
                                            )
                                        }
                                    }
                                }
                                view?.let { view ->
                                    Toaster.build(
                                            view = view,
                                            text = "Sip, catatanmu tersimpan.",
                                            duration = Toaster.LENGTH_SHORT,
                                            type = Toaster.TYPE_NORMAL,
                                            actionText = "Oke"
                                    ).show()
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_DELETE_PRODUCT -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.let { requestParam ->
                                val cardPositions = viewModel.productMap[requestParam.productId]
                                cardPositions?.run {
                                    val dataSetPosition = viewModel.getDataSetPosition(this)
                                    val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
                                    if (productUiModel?.isCustomizable == true) {
                                        productListAdapter?.removeCustomOrder(
                                                cartId = requestParam.cartId,
                                                dataSetPosition = viewModel.getDataSetPosition(this)
                                        )
                                    } else {
                                        productListAdapter?.updateOrderQty(
                                                orderQty = Int.ONE,
                                                dataSetPosition = viewModel.getDataSetPosition(this)
                                        )
                                        productListAdapter?.updateAtcStatus(
                                                isAtc = false,
                                                dataSetPosition = viewModel.getDataSetPosition(this),
                                                adapterPosition = viewModel.getAdapterPosition(this)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY -> {
                        (it.data as? Pair<*, *>)?.let { pair ->
                            (pair.first as? UpdateParam)?.productList?.firstOrNull()?.let { requestParam ->
                                (pair.second as? CartTokoFoodData)?.let { cartTokoFoodData ->
                                    cartTokoFoodData.carts.firstOrNull { data -> data.productId == requestParam.productId }?.let { cartTokoFood ->
                                        val cardPositions = viewModel.productMap[requestParam.productId]
                                        cardPositions?.run {
                                            val dataSetPosition = viewModel.getDataSetPosition(this)
                                            val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
                                            if (productUiModel?.isCustomizable == true) {
                                                productListAdapter?.updateCustomOrderQty(
                                                        cartId = cartTokoFood.cartId,
                                                        orderQty = cartTokoFood.quantity,
                                                        dataSetPosition = dataSetPosition
                                                )
                                            } else {
                                                productListAdapter?.updateProductUiModel(
                                                        cartTokoFood = cartTokoFood,
                                                        dataSetPosition = viewModel.getDataSetPosition(this),
                                                        adapterPosition = viewModel.getAdapterPosition(this)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initializeMiniCartWidget() {
        activityViewModel?.let {
            binding?.miniCartWidget?.initialize(it, viewLifecycleOwner.lifecycleScope, "")
        }
    }

    private fun setupMerchantLogo() {
        binding?.iuMerchantLogo?.type = ImageUnify.TYPE_CIRCLE
    }

    private fun setupMerchantProfileCarousel() {
        carouselAdapter = MerchantPageCarouselAdapter(this)
        binding?.rvMerchantInfoCarousel?.let {
            it.adapter = carouselAdapter
            it.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            )
        }
    }

    private fun setupProductList() {
        productListAdapter = ProductListAdapter(this)
        binding?.rvProductList?.let {
            it.adapter = productListAdapter
            it.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            )
        }
    }

    private fun setupOrderNoteBottomSheet() {
        orderNoteBottomSheet = OrderNoteBottomSheet.createInstance(this)
    }

    private fun renderTicker(tickerData: TokoFoodTickerDetail) {
        binding?.tickerMerchantPage?.apply {
            this.tickerType = TYPE_WARNING
            this.tickerShape = SHAPE_FULL
            this.closeButtonVisibility = View.GONE
            this.tickerTitle = tickerTitle
            this.setTextDescription(tickerData.subtitle)
            this.show()
        }
    }

    private fun renderMerchantProfile(merchantProfile: TokoFoodMerchantProfile) {
        val imageUrl = merchantProfile.imageURL ?: ""
        if (imageUrl.isNotBlank()) binding?.iuMerchantLogo?.setImageUrl(imageUrl)
        binding?.tpgMerchantName?.text = merchantProfile.name
        binding?.tpgMerchantCategory?.text = merchantProfile.merchantCategories.joinToString()
        val carouselData = viewModel.mapMerchantProfileToCarouselData(merchantProfile)
        carouselAdapter?.setCarouselData(carouselData)
        // TODO: add close in xxx
    }

    private fun setupMerchantInfoBottomSheet(name: String, address: String, merchantOpsHours: List<MerchantOpsHour>) {
        merchantInfoBottomSheet = MerchantInfoBottomSheet.createInstance(name, address, merchantOpsHours)
    }

    private fun renderProductList(productListItems: List<ProductListItem>) {
        productListAdapter?.setProductListItems(productListItems)
    }

    override fun onCarouselItemClicked() {
        merchantInfoBottomSheet?.show(childFragmentManager)
    }

    override fun onProductCardClicked(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        val productDetailBottomSheet = ProductDetailBottomSheet.createInstance(productUiModel, this)
        productDetailBottomSheet.setSelectedCardPositions(cardPositions)
        productDetailBottomSheet.show(childFragmentManager)
    }

    override fun onAtcButtonClicked(productUiModel: ProductUiModel, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productUiModel.id] = cardPositions
        if (productUiModel.isCustomizable && productUiModel.isAtc) {
            CustomOrderDetailBottomSheet.createInstance(productUiModel = productUiModel, this).show(childFragmentManager)
        } else if (productUiModel.isCustomizable) {
            navigateToOrderCustomizationPage(cartId = "", productUiModel = productUiModel)
        } else {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = userSession.shopId,
                    productUiModel = productUiModel
            )
            activityViewModel?.addToCart(updateParam, "")
        }
    }

    override fun onAddNoteButtonClicked(productId: String, orderNote: String, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productId] = cardPositions
        orderNoteBottomSheet?.setSelectedProductId(productId)
        orderNoteBottomSheet?.renderOrderNote(orderNote)
        orderNoteBottomSheet?.show(childFragmentManager)
    }

    override fun onDeleteButtonClicked(cartId: String, productId: String, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productId] = cardPositions
        activityViewModel?.deleteProduct(productId = productId, cartId = cartId, "")
    }

    override fun onIncreaseQtyButtonClicked(productId: String, quantity: Int, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.orderQty = quantity
        productUiModel?.run {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = userSession.shopId,
                    productUiModel = productUiModel
            )
            activityViewModel?.updateQuantity(updateParam, "")
        }
    }

    override fun onNavigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel) {
        navigateToOrderCustomizationPage(cartId = cartId, productUiModel = productUiModel)
    }

    override fun onDecreaseQtyButtonClicked(productId: String, quantity: Int, cardPositions: Pair<Int, Int>) {
        viewModel.productMap[productId] = cardPositions
        val dataSetPosition = viewModel.getDataSetPosition(cardPositions)
        val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
        productUiModel?.orderQty = quantity
        productUiModel?.run {
            val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                    shopId = userSession.shopId,
                    productUiModel = productUiModel
            )
            activityViewModel?.updateQuantity(updateParam, "")
        }
    }

    override fun onSaveNoteButtonClicked(productId: String, orderNote: String) {
        val cardPositions = viewModel.productMap[productId]
        cardPositions?.run {
            val dataSetPosition = viewModel.getDataSetPosition(this)
            val productUiModel = productListAdapter?.getProductUiModel(dataSetPosition)
            productUiModel?.orderNote = orderNote
            productUiModel?.run {
                val updateParam = viewModel.mapProductUiModelToAtcRequestParam(
                        shopId = userSession.shopId,
                        productUiModel = productUiModel
                )
                activityViewModel?.updateNotes(updateParam, "")
            }
        }
        orderNoteBottomSheet?.dismiss()
    }


    override fun onDeleteCustomOrderButtonClicked(cartId: String, productId: String) {
        activityViewModel?.deleteProduct(productId = productId, cartId = cartId, "")
    }

    override fun onUpdateCustomOrderQtyButtonClicked(productId: String, quantity: Int, customOrderDetail: CustomOrderDetail) {
        customOrderDetail.qty = quantity
        val updateParam = viewModel.mapCustomOrderDetailToAtcRequestParam(
                shopId = userSession.shopId,
                productId = productId,
                customOrderDetail = customOrderDetail
        )
        activityViewModel?.updateQuantity(updateParam, "")
    }

    private fun navigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel) {
        val orderCustomizationFragment = OrderCustomizationFragment.createInstance(
                productUiModel = productUiModel,
                cartId = cartId
        )
        navigateToNewFragment(orderCustomizationFragment)
    }
}