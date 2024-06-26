package com.tokopedia.shop_widget.buy_more_save_more.presentation.fragment

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.utils.getMaxHeightForListView
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.buy_more_save_more.di.component.DaggerBmsmWidgetComponent
import com.tokopedia.shop_widget.buy_more_save_more.di.module.BmsmWidgetModule
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoByShopIdUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.BmsmWidgetUiState
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.shop_widget.buy_more_save_more.presentation.adapter.BmsmWidgetProductListAdapter
import com.tokopedia.shop_widget.buy_more_save_more.presentation.adapter.decoration.ProductListItemDecoration
import com.tokopedia.shop_widget.buy_more_save_more.presentation.customview.SlidingTextSwitcher
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetItemEventListener
import com.tokopedia.shop_widget.buy_more_save_more.presentation.viewmodel.BmsmWidgetTabViewModel
import com.tokopedia.shop_widget.buy_more_save_more.util.BmsmWidgetColorThemeConfig
import com.tokopedia.shop_widget.buy_more_save_more.util.ColorType
import com.tokopedia.shop_widget.buy_more_save_more.util.NonFatalIssueLogger
import com.tokopedia.shop_widget.buy_more_save_more.util.Status
import com.tokopedia.shop_widget.databinding.FragmentBmsmWidgetBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.R.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BmsmWidgetTabFragment :
    BaseDaggerFragment(),
    BmsmWidgetItemEventListener,
    HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        private const val BUNDLE_KEY_OFFER_DATA = "offer_data"
        private const val BUNDLE_KEY_SHOP_PAGE_COLOR_THEME_CONFIG = "color_theme_config"
        private const val BUNDLE_KEY_SHOP_PAGE_PATTERN_COLOR_TYPE = "color_type"
        private const val BUNDLE_KEY_OFFER_TYPE_ID = "offer_type_id"
        private const val BUNDLE_KEY_OVERRIDE_THEME = "override_theme"
        private const val EXT_PARAM_OFFER_ID = "offer_id"
        private const val EXT_PARAM_WAREHOUSE_ID = "offer_whid"
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        const val PAGE_SIZE = 11
        const val FIRST_PAGE = 1
        private const val TWO_PRODUCT_ITEM_SIZE = 2
        private const val REQUEST_CODE_USER_LOGIN = 101
        private const val saturation = 0.2f
        private const val imageAlphaValue = 128
        private const val OFFER_TYPE_GWP = 2
        private const val OFFER_TYPE_PD = 1
        private const val PD_WIDGET_TITLE_LOADER_LEFT_MARGIN = 64

        @JvmStatic
        fun newInstance(
            data: OfferingInfoByShopIdUiModel,
            offerTypeId: Int,
            colorThemeConfiguration: BmsmWidgetColorThemeConfig,
            patternColorType: ColorType
        ): BmsmWidgetTabFragment {
            return BmsmWidgetTabFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_OFFER_DATA, data)
                    putInt(BUNDLE_KEY_OFFER_TYPE_ID, offerTypeId)
                    putSerializable(
                        BUNDLE_KEY_SHOP_PAGE_COLOR_THEME_CONFIG,
                        colorThemeConfiguration
                    )
                    putSerializable(BUNDLE_KEY_SHOP_PAGE_PATTERN_COLOR_TYPE, patternColorType)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: BmsmWidgetTabViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BmsmWidgetTabViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentBmsmWidgetBinding>()

    private val defaultOfferingData by lazy {
        arguments?.getParcelable(BUNDLE_KEY_OFFER_DATA) as? OfferingInfoByShopIdUiModel
            ?: OfferingInfoByShopIdUiModel()
    }

    private val offerTypeId by lazy {
        arguments?.getInt(BUNDLE_KEY_OFFER_TYPE_ID)
    }

    private val colorThemeConfiguration by lazy {
        arguments?.getSerializable(BUNDLE_KEY_SHOP_PAGE_COLOR_THEME_CONFIG) as? BmsmWidgetColorThemeConfig
            ?: BmsmWidgetColorThemeConfig.DEFAULT
    }

    private val patternColorType by lazy {
        arguments?.getSerializable(BUNDLE_KEY_SHOP_PAGE_PATTERN_COLOR_TYPE) as? ColorType
            ?: ColorType.LIGHT
    }

    private val productListAdapter by lazy {
        BmsmWidgetProductListAdapter(
            this@BmsmWidgetTabFragment,
            colorThemeConfiguration == BmsmWidgetColorThemeConfig.REIMAGINE
        )
    }

    private var onSuccessAtc: (String, String, AddToCartDataModel) -> Unit = { _, _, _ -> }
    private var onErrorAtc: (String) -> Unit = {}
    private var onNavigateToOlp: (String, String, String) -> Unit = { _, _, _ -> }
    private var onProductCardClicked: (String, String, Product) -> Unit = { _, _, _ -> }
    private var onWidgetVisible: (String) -> Unit = {}
    private val isLogin: Boolean
        get() = viewModel.isLogin
    private val currentState: BmsmWidgetUiState
        get() = viewModel.currentState

    private val localCacheModel by lazy {
        context?.let {
            ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    override fun getScreenName(): String = BmsmWidgetTabFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        activity?.run {
            DaggerBmsmWidgetComponent
                .builder()
                .bmsmWidgetModule(BmsmWidgetModule())
                .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this@BmsmWidgetTabFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmsmWidgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialUiState()
        setupCardLayout()
        setupErrorSection()
        setupProductList()
        setupObserver()
        onWidgetVisible.invoke(currentState.offeringInfo.offerings.firstOrNull()?.id.toString())
    }

    override fun onResume() {
        super.onResume()
        getOfferingData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        context?.let {
            AtcVariantHelper.onActivityResultAtcVariant(it, requestCode, data) {
                if (atcMessage.isNotEmpty()) viewModel.getMinicartV3()
            }
        }
    }

    override fun onAtcClicked(product: Product) {
        if (isLogin) {
            binding?.apply {
                pdUpsellingLoader.showWithCondition(pdUpsellingWrapper.isVisible)
                pdUpsellingWrapper.gone()
            }
            if (product.isVbs) {
                openAtcVariant(product)
            } else {
                viewModel.addToCart(product)
            }
        } else {
            redirectToLoginPage(REQUEST_CODE_USER_LOGIN)
        }
    }

    override fun onProductCardClicked(product: Product) {
        onProductCardClicked.invoke(
            currentState.offerIds.firstOrNull().toString(),
            offerTypeId.toString(),
            product
        )
    }

    override fun onNavigateToOlp() {
        onNavigateToOlp.invoke(
            currentState.offerIds.firstOrNull().toString(),
            offerTypeId.toString(),
            currentState.offeringInfo.offerings.firstOrNull()?.olpAppLink.orEmpty()
        )
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                if (state.isShowLoading) {
                    setViewState(VIEW_LOADING)
                } else {
                    setupHeader(state)
                }
            }
        }
        viewModel.productList.observe(viewLifecycleOwner) { productList ->
            if (productList.size.isMoreThanZero()) {
                setProductListData(productList)
                setViewState(VIEW_CONTENT)
            } else {
                setViewState(VIEW_ERROR, Status.OOS)
            }
        }

        viewModel.miniCartAdd.observe(viewLifecycleOwner) { atc ->
            getOfferingData()
            viewModel.getMinicartV3()
            when (atc) {
                is Success -> {
                    if (atc.data.isDataError()) {
                        onErrorAtc.invoke(atc.data.getAtcErrorMessage().orEmpty())
                    } else {
                        onSuccessAtc.invoke(
                            currentState.offerIds.firstOrNull().toString(),
                            offerTypeId.toString(),
                            atc.data
                        )
                    }
                }

                is Fail -> {
                    sendLogger(atc.throwable)
                    onErrorAtc.invoke(atc.throwable.localizedMessage.orEmpty())
                }
            }
        }

        viewModel.miniCartSimplifiedData.observe(viewLifecycleOwner) { minicartSimplifiedData ->
            binding?.apply {
                val offerMessage = minicartSimplifiedData.bmgmData.offerMessage
                when (offerTypeId) {
                    OFFER_TYPE_PD -> tpgPdUpsellingWording.setUpsellingPd(offerMessage)
                    OFFER_TYPE_GWP -> tpgSubTitleWidget.setUpsellingGwp(offerMessage)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { throwable ->
            sendLogger(throwable)
            setViewState(
                VIEW_ERROR,
                getErrorCodeFromThrowable(throwable.localizedMessage.toIntOrZero())
            )
        }
    }

    private fun setInitialUiState() {
        viewModel.setInitialUiState(
            offerIds = listOf(defaultOfferingData.offerId),
            shopId = defaultOfferingData.shopId,
            defaultOfferingData = defaultOfferingData,
            localCacheModel = localCacheModel
        )
    }

    private fun getOfferingData() {
        viewModel.getOfferingData()
    }

    private fun setupCardLayout() {
        binding?.apply {
            cardContent.setCardBackgroundColor(getCardBackgroundColor())
            pdIllustration.apply {
                setGreyScaledTransparentIllustration(
                    TokopediaImageUrl.BMSM_PD_WIDGET_ILLUSTRATION
                )
                showWithCondition(offerTypeId == OFFER_TYPE_PD)
            }
            gwpIllustration.apply {
                setGreyScaledTransparentIllustration(
                    TokopediaImageUrl.BMSM_GWP_WIDGET_ILLUSTRATION
                )
                showWithCondition(offerTypeId == OFFER_TYPE_GWP)
            }
        }
    }

    private fun setupHeader(state: BmsmWidgetUiState) {
        binding?.apply {
            val offering = state.offeringInfo.offerings.firstOrNull()
            val upsellWording = offering?.upsellWording.orEmpty()
            val miniCartData = state.miniCartData.bmgmData
            val offerMessage = miniCartData.offerMessage
            val defaultOfferMessage = offering?.tierList?.firstOrNull()?.tierWording.orEmpty()

            val productBenefitImageFromOfferingInfo = offering?.tierList
                ?.firstOrNull()
                ?.benefits?.firstOrNull()
                ?.products?.map {
                    it.image
                }

            val productBenefitImageFromMinicart = miniCartData.tiersApplied
                .firstOrNull()
                ?.benefitProducts?.map {
                    it.productImage
                }

            val productGiftImages = if (productBenefitImageFromMinicart?.isNotEmpty() == true) {
                productBenefitImageFromMinicart
            } else {
                productBenefitImageFromOfferingInfo
            }

            if (offerMessage.isEmpty() && !currentState.isWidgetOnInitialState) {
                setViewState(VIEW_ERROR, Status.GIFT_OOS)
            } else {
                tpgTitleWidget.setTitle(offerMessage, upsellWording, defaultOfferMessage)
                when (offerTypeId) {
                    OFFER_TYPE_PD -> setupPdHeader(offerMessage)
                    OFFER_TYPE_GWP -> setupGwpHeader(productGiftImages)
                }
            }
            setupOlpNavigation()
        }
    }

    private fun setupOlpNavigation() {
        binding?.apply {
            iconChevron.setOnClickListener {
                onNavigateToOlp.invoke(
                    currentState.offerIds.firstOrNull().toString(),
                    offerTypeId.toString(),
                    currentState.offeringInfo.offerings.firstOrNull()?.olpAppLink.orEmpty()
                )
            }
        }
    }

    private fun setupProductList() {
        binding?.apply {
            rvProduct.apply {
                val linearLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                layoutManager = linearLayoutManager
                adapter = productListAdapter
            }
        }
    }

    private fun setProductListData(productList: List<Product>) {
        binding?.rvProduct?.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                if (productList.size > Int.ONE) setHeightBasedOnProductCardMaxHeight(productList.mapToProductCardModel())
            }
            if (productList.size == TWO_PRODUCT_ITEM_SIZE) {
                layoutManager =
                    GridLayoutManager(
                        context,
                        TWO_PRODUCT_ITEM_SIZE
                    )
                addItemDecoration(ProductListItemDecoration())
            }
        }
        productListAdapter.apply {
            if (itemCount.isZero()) addProductList(productList)
        }
    }

    private fun setViewState(viewState: Int, status: Status = Status.SUCCESS) {
        binding?.apply {
            when (viewState) {
                VIEW_LOADING -> {
                    loadingState.apply {
                        root.visible()
                        val isGwpWidget = offerTypeId == OFFER_TYPE_GWP
                        clGiftImageLoaderWrapper.showWithCondition(isGwpWidget)
                        if (!isGwpWidget) {
                            val params = loaderTitle.layoutParams as MarginLayoutParams
                            params.marginStart = PD_WIDGET_TITLE_LOADER_LEFT_MARGIN
                            loaderTitle.layoutParams = params
                        }
                    }
                    cardErrorState.gone()
                    flContentWrapper.gone()
                }

                VIEW_ERROR -> {
                    setErrorState(
                        title = getString(R.string.bmsm_widget_oos_product_title),
                        description = getString(R.string.bmsm_widget_oos_product_description),
                        status = status
                    )
                }

                VIEW_CONTENT -> {
                    loadingState.root.gone()
                    cardGroup.visible()
                    flContentWrapper.visible()
                    cardErrorState.gone()
                }
            }
        }
    }

    private fun setErrorState(
        title: String,
        description: String,
        status: Status
    ) {
        binding?.apply {
            loadingState.root.gone()
            cardGroup.gone()
            flContentWrapper.gone()
            cardErrorState.gone()
            when (status) {
                Status.GIFT_OOS -> {
                    loadingState.root.gone()
                    cardGroup.visible()
                    flContentWrapper.visible()
                    cardErrorState.gone()
                    groupStackedImg.gone()
                    imgGiftItems.gone()
                    imgGiftWhiteFrameBroder.gone()
                    tpgSubTitleWidget.gone()
                    tpgTitleWidget.apply {
                        setWeight(Typography.REGULAR)
                        text = context.getString(R.string.bmsm_widget_gift_oos_description)
                    }
                }

                Status.OOS -> {
                    emptyPageLarge.apply {
                        visible()
                        setImageUrl(TokopediaImageUrl.ILLUSTRATION_GENERAL_EMPTY_BASKET)
                        setTitle(title)
                        setDescription(description)
                        emptyStateTitleID.setEmptyStateTextColor()
                        emptyStateDescriptionID.setEmptyStateTextColor()
                    }
                    cardErrorState.gone()
                }

                else -> {
                    emptyPageLarge.gone()
                    cardErrorState.visible()
                }
            }
        }
    }

    private fun openAtcVariant(product: Product) {
        val stringOfferIds = currentState.offerIds.joinToString(",")
        val stringWarehouseIds = currentState.offeringInfo.nearestWarehouseIds.joinToString(",")
        val shopId = currentState.shopId
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                context = it,
                productId = product.productId.toString(),
                pageSource = VariantPageSource.BUY_MORE_GET_MORE,
                shopId = shopId.toString(),
                saveAfterClose = true,
                extParams = AtcVariantHelper.generateExtParams(
                    mapOf(
                        EXT_PARAM_OFFER_ID to stringOfferIds,
                        EXT_PARAM_WAREHOUSE_ID to stringWarehouseIds
                    )
                ),
                startActivitResult = this::startActivityForResult
            )
        }
    }

    private fun setupPdHeader(offerMessage: List<String>) {
        binding?.apply {
            // gift image section
            imgGiftItems.gone()
            imgGiftWhiteFrameBroder.gone()
            groupStackedImg.gone()

            // upselling wording section
            tpgSubTitleWidget.gone()
            pdUpsellingWrapper.apply {
                visibleWithCondition(offerMessage.isNotEmpty())
                setBackgroundResource(getPdUpsellingWrapperBackground())
            }
            rvProduct.apply {
                if (pdUpsellingWrapper.isVisible) {
                    val params = layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(0, context.dpToPx(82).toInt(), 0, context.dpToPx(16).toInt())
                    layoutParams = params
                    invalidate()
                } else {
                    val params = layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(0, context.dpToPx(64).toInt(), 0, context.dpToPx(16).toInt())
                    layoutParams = params
                    invalidate()
                }
            }
        }
    }

    private fun setupGwpHeader(productGiftImages: List<String>?) {
        val shownProductImage =
            productGiftImages?.firstOrNull() ?: defaultOfferingData.thumbnails.firstOrNull()

        binding?.apply {
            // gift image section
            if (productGiftImages.isNullOrEmpty()) {
                imgGiftItems.gone()
                imgGiftWhiteFrameBroder.gone()
                groupStackedImg.gone()
            } else {
                imgGiftItems.apply {
                    setImageUrl(shownProductImage.orEmpty())
                    visible()
                }
                imgGiftWhiteFrameBroder.visible()
                groupStackedImg.showWithCondition(productGiftImages.size > Int.ONE)
            }
            // upselling wording section
            pdUpsellingWrapper.gone()
        }
    }

    private fun setupErrorSection() {
        binding?.apply {
            btnReload.setOnClickListener {
                getOfferingData()
            }
        }
    }

    private fun Typography.setTitle(
        offerMessages: List<String>,
        upsellWording: String,
        defaultOfferMessage: String
    ) {
        val textColor = MethodChecker.getColor(
            context,
            R.color.dms_static_white
        )
        text = if (offerMessages.isNotEmpty()) {
            MethodChecker.fromHtml(upsellWording)
        } else {
            MethodChecker.fromHtml(defaultOfferMessage)
        }
        setTextColor(textColor)
        visible()
    }

    private fun SlidingTextSwitcher.setUpsellingGwp(messages: List<String>) {
        val textColor = MethodChecker.getColor(
            context,
            R.color.dms_static_white
        )
        if (childCount.isZero()) {
            setFactory {
                Typography(context).apply {
                    setType(Typography.SMALL)
                    setTextColor(textColor)
                }
            }
        }
        setMessages(messages = messages, textColor = textColor)
    }

    private fun SlidingTextSwitcher.setUpsellingPd(
        offerMessages: List<String>
    ) {
        binding?.pdUpsellingLoader?.gone()
        val textColor = when (colorThemeConfiguration) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> ContextCompat.getColor(
                context,
                R.color.dms_static_white
            )

            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (patternColorType == ColorType.LIGHT) {
                    ContextCompat.getColor(context, R.color.dms_pd_sub_title_text_color)
                } else {
                    ContextCompat.getColor(context, R.color.dms_static_white)
                }
            }

            BmsmWidgetColorThemeConfig.DEFAULT -> ContextCompat.getColor(
                context,
                R.color.dms_pd_sub_title_text_color
            )
        }
        if (childCount.isZero()) {
            setFactory {
                Typography(context).apply {
                    setType(Typography.SMALL)
                    setTextColor(textColor)
                }
            }
        }
        setMessages(
            messages = offerMessages,
            textColor = textColor
        )
        showWithCondition(offerMessages.isNotEmpty())
    }

    private fun Typography.setEmptyStateTextColor() {
        val textColor = when (colorThemeConfiguration) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> ContextCompat.getColor(
                context,
                R.color.dms_static_white
            )

            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (patternColorType == ColorType.LIGHT) {
                    ContextCompat.getColor(context, R.color.dms_static_black)
                } else {
                    ContextCompat.getColor(context, R.color.dms_static_white)
                }
            }

            BmsmWidgetColorThemeConfig.DEFAULT -> ContextCompat.getColor(
                context,
                color.Unify_NN1000
            )
        }
        setTextColor(textColor)
    }

    fun setOnSuccessAtcListener(onSuccessAtc: (String, String, AddToCartDataModel) -> Unit) {
        this.onSuccessAtc = onSuccessAtc
    }

    fun setOnErrorAtcListener(onErrorAtc: (String) -> Unit) {
        this.onErrorAtc = onErrorAtc
    }

    fun setOnNavigateToOlpListener(onNavigateToOlp: (String, String, String) -> Unit) {
        this.onNavigateToOlp = onNavigateToOlp
    }

    fun setOnProductCardClicked(onProductCardClicked: (String, String, Product) -> Unit) {
        this.onProductCardClicked = onProductCardClicked
    }

    fun setOnWidgetVisible(onWidgetVisible: (String) -> Unit) {
        this.onWidgetVisible = onWidgetVisible
    }

    private fun redirectToLoginPage(requestCode: Int = REQUEST_CODE_USER_LOGIN) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
        productCardModelList: List<ProductCardModel>
    ) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth = if (productCardModelList.size > TWO_PRODUCT_ITEM_SIZE) {
            context?.resources?.getDimensionPixelSize(R.dimen.dp_145).orZero()
        } else {
            context?.resources?.getDimensionPixelSize(R.dimen.dp_200).orZero()
        }
        return if (productCardModelList.size > Int.ONE) {
            productCardModelList.getMaxHeightForGridView(
                context,
                Dispatchers.Default,
                productCardWidth
            )
        } else {
            productCardModelList.getMaxHeightForListView(
                context,
                Dispatchers.Default
            )
        }
    }

    private fun List<Product>.mapToProductCardModel(): List<ProductCardModel> {
        return this.map {
            ProductCardModel(
                productImageUrl = it.imageUrl,
                productName = it.name,
                discountPercentage = if (it.campaign.discountedPercentage != Int.ZERO) "${it.campaign.discountedPercentage}%" else "",
                slashedPrice = it.campaign.originalPrice,
                formattedPrice = it.campaign.discountedPrice.ifEmpty { it.price },
                countSoldRating = it.rating,
                hasAddToCartButton = true,
                labelGroupList = it.labelGroup.toLabelGroup()
            )
        }
    }

    private fun List<Product.LabelGroup>.toLabelGroup(): List<ProductCardModel.LabelGroup> {
        return map {
            ProductCardModel.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                imageUrl = it.url
            )
        }
    }

    private fun ImageUnify.setGreyScaledTransparentIllustration(imageUrl: String) {
        this.apply {
            setImageUrl(imageUrl)
            val matrix = ColorMatrix()
            matrix.setSaturation(saturation)
            val grayScaledColorFilter = ColorMatrixColorFilter(matrix)
            imageAlpha = imageAlphaValue
            colorFilter = grayScaledColorFilter
        }
    }

    private fun getErrorCodeFromThrowable(errorCode: Int): Status {
        return Status.values().firstOrNull { value ->
            value.code == errorCode.toLong()
        } ?: Status.INVALID_OFFER_ID
    }

    private fun getCardBackgroundColor(): Int {
        val bgColor = when (colorThemeConfiguration) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> {
                MethodChecker.getColor(
                    context,
                    R.color.dms_gwp_card_transparent_bg_color
                )
            }

            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (patternColorType == ColorType.DARK) {
                    MethodChecker.getColor(
                        context,
                        R.color.dms_gwp_card_transparent_bg_color
                    )
                } else {
                    MethodChecker.getColor(
                        context,
                        R.color.dms_gwp_card_bg_color
                    )
                }
            }

            else -> {
                MethodChecker.getColor(
                    context,
                    R.color.dms_gwp_card_bg_color
                )
            }
        }
        return bgColor
    }

    private fun getPdUpsellingWrapperBackground(): Int {
        val background = when (colorThemeConfiguration) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> {
                R.drawable.bmsm_pd_upselling_wording_transparent_background
            }

            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (patternColorType == ColorType.DARK) {
                    R.drawable.bmsm_pd_upselling_wording_transparent_background
                } else {
                    R.drawable.bmsm_pd_upselling_wording_background
                }
            }

            else -> {
                R.drawable.bmsm_pd_upselling_wording_background
            }
        }
        return background
    }

    private fun sendLogger(t: Throwable) {
        NonFatalIssueLogger.logToCrashlytics(t, this::class.java.canonicalName)
    }
}
