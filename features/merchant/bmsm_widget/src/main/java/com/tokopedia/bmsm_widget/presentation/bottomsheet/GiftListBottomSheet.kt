package com.tokopedia.bmsm_widget.presentation.bottomsheet

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.bmsm_widget.R
import com.tokopedia.bmsm_widget.databinding.BottomsheetGiftListBinding
import com.tokopedia.bmsm_widget.di.component.DaggerBmsmWidgetComponent
import com.tokopedia.bmsm_widget.domain.entity.MainProduct
import com.tokopedia.bmsm_widget.domain.entity.PageSource
import com.tokopedia.bmsm_widget.domain.entity.TierGift
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.bmsm_widget.presentation.bottomsheet.uimodel.GiftListEvent
import com.tokopedia.bmsm_widget.presentation.bottomsheet.uimodel.GiftListUiState
import com.tokopedia.bmsm_widget.util.constant.BundleConstant
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreview.imagesecure.ImageSecurePreviewActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.IOException
import javax.inject.Inject

class GiftListBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_OFFER_ID = "offer_id"
        private const val BUNDLE_KEY_SHOP_ID = "shop_id"
        private const val BUNDLE_KEY_WAREHOUSE_ID = "warehouse_id"
        private const val BUNDLE_KEY_PAGE_SOURCE = "page_source"
        private const val BUNDLE_KEY_TIER_GIFTS = "tier_gifts"
        private const val BUNDLE_KEY_SELECTED_TIER_ID = "selected_tier_id"
        private const val BUNDLE_KEY_MAIN_PRODUCTS = "main_products"
        private const val TWO_COLUMN_GRID = 2
        private const val MARGIN = 16

        /**
         * @param tierGifts: List of item that listed as gift from a specific tier
         * @param pageSource: An enum that describe who is the caller of this bottom sheet, this param required by backend.
         * @param autoSelectTierChipByTierId: If filled with valid tier id, once open the bottom sheet a matching tier id chips will be auto selected,
         *      otherwise will auto select the first left chip item as the default selected chips.
         */
        @JvmStatic
        fun newInstance(
            offerId: Long,
            shopId: String,
            warehouseId: Long,
            tierGifts: List<TierGifts>,
            mainProducts: List<MainProduct>,
            pageSource: PageSource,
            autoSelectTierChipByTierId: Long = BundleConstant.ID_NO_SELECTED_TIER
        ): GiftListBottomSheet {
            return GiftListBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(BUNDLE_KEY_OFFER_ID, offerId)
                    putString(BUNDLE_KEY_SHOP_ID, shopId)
                    putLong(BUNDLE_KEY_WAREHOUSE_ID, warehouseId)
                    putParcelableArrayList(BUNDLE_KEY_TIER_GIFTS, ArrayList(tierGifts))
                    putParcelableArrayList(BUNDLE_KEY_MAIN_PRODUCTS, ArrayList(mainProducts))
                    putParcelable(BUNDLE_KEY_PAGE_SOURCE, pageSource)
                    putLong(BUNDLE_KEY_SELECTED_TIER_ID, autoSelectTierChipByTierId)
                }
            }
        }
    }

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
        isFullpage = true
    }

    private val giftListAdapter by lazy { GiftListAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[GiftListViewModel::class.java] }

    private var binding by autoClearedNullable<BottomsheetGiftListBinding>()
    private val offerId by lazy { arguments?.getLong(BUNDLE_KEY_OFFER_ID).orZero() }
    private val shopId by lazy { arguments?.getString(BUNDLE_KEY_SHOP_ID).orEmpty() }
    private val warehouseId by lazy { arguments?.getLong(BUNDLE_KEY_WAREHOUSE_ID).orZero() }
    private val tierGifts by lazy { arguments?.getParcelableArrayList<TierGifts>(BUNDLE_KEY_TIER_GIFTS) ?: emptyList() }
    private val mainProducts by lazy { arguments?.getParcelableArrayList<MainProduct>(BUNDLE_KEY_MAIN_PRODUCTS) ?: emptyList() }
    private val source by lazy { arguments?.getParcelable(BUNDLE_KEY_PAGE_SOURCE) as? PageSource ?: PageSource.OFFER_LANDING_PAGE }
    private val selectedTierId by lazy { arguments?.getLong(BUNDLE_KEY_SELECTED_TIER_ID) ?: BundleConstant.ID_NO_SELECTED_TIER }

    private fun setupDependencyInjection() {
        DaggerBmsmWidgetComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        viewModel.processEvent(
            GiftListEvent.OpenScreen(
                offerId = offerId,
                warehouseId = warehouseId,
                giftProducts = tierGifts,
                source = source,
                selectedTierId = selectedTierId,
                userCache = getUserCache(),
                shopId = shopId,
                mainProducts = mainProducts
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        setCloseClickListener {
            viewModel.processEvent(GiftListEvent.TapIconCloseBottomSheet)
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiState()
        viewModel.processEvent(GiftListEvent.GetGiftList)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetGiftListBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.gwp_gift_list))
    }

    private fun setupView() {
        setupList()
        setupGlobalError()
    }

    private fun setupGlobalError() {
        binding?.globalError?.apply {
            setType(GlobalError.SERVER_ERROR)
            setActionClickListener { viewModel.processEvent(GiftListEvent.GetGiftList) }
        }
    }

    private fun setupList() {
        binding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(activity ?: return, TWO_COLUMN_GRID)
            giftListAdapter.setOnGiftClick { index ->
                val product = giftListAdapter.snapshot()[index]
                redirectToImageDetail(product.productImageUrl)
            }
            adapter = giftListAdapter
            val itemDecoration = object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = MARGIN.toPx()
                }
            }
            addItemDecoration(itemDecoration)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun handleUiState(uiState: GiftListUiState) {
        val isError = uiState.error != null
        val isLoading = uiState.isLoading

        when {
            isLoading -> {
                binding?.loader?.visible()
                binding?.globalError?.gone()
                binding?.sortFilter?.gone()
                binding?.recyclerView?.gone()
            }
            isError -> {
                binding?.loader?.gone()
                binding?.globalError?.visible()
                renderErrorState(uiState.error)
                binding?.sortFilter?.gone()
                binding?.recyclerView?.gone()
            }
            else -> {
                binding?.loader?.gone()
                binding?.globalError?.gone()
                binding?.sortFilter?.visible()
                binding?.recyclerView?.visible()
                renderContent(uiState)
            }
        }
    }

    private fun renderContent(uiState: GiftListUiState) {
        renderChips(uiState.tierGift, uiState.selectedTier)
        renderList(uiState.selectedTier)
    }

    private fun renderChips(
        tierGift: List<TierGift>,
        selectedTier: TierGift?
    ) {
        val sortFilterItem = ArrayList<SortFilterItem>()

        tierGift.forEach { tier ->
            val isSelected = selectedTier?.tierId == tier.tierId
            val chipType = if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL

            sortFilterItem.add(
                SortFilterItem(
                    title = tier.tierName,
                    type = chipType,
                    size = ChipsUnify.SIZE_SMALL,
                    listener = {
                        viewModel.processEvent(GiftListEvent.ChangeGiftTier(tier))
                    }
                )
            )
        }

        binding?.sortFilter?.addItem(sortFilterItem)
        binding?.tpgGiftTitle?.text = selectedTier?.tierMessage.orEmpty()
    }

    private fun renderList(selectedTier: TierGift?) {
        val giftProducts = selectedTier?.products.orEmpty()
        giftListAdapter.submit(giftProducts)

        val ribbonText = context?.getString(R.string.gwp_gift_ribbon_text_selected)
        giftListAdapter.setRibbonText(ribbonText.orEmpty())
    }

    private fun renderErrorState(throwable: Throwable?) {
        if (throwable is IOException) {
            binding?.globalError?.setType(GlobalError.NO_CONNECTION)
        } else {
            binding?.globalError?.setType(GlobalError.SERVER_ERROR)
        }
    }

    private fun redirectToImageDetail(imageUrl: String) {
        val intent = ImageSecurePreviewActivity.getCallingIntent(
            context = context ?: return,
            imageUris = arrayListOf(imageUrl)
        )
        startActivity(intent)
    }

    @SuppressLint("PII Data Exposure")
    private fun getUserCache(): LocalCacheModel {
        val context = context ?: return LocalCacheModel()
        return ChooseAddressUtils.getLocalizingAddressData(context)
    }
}
