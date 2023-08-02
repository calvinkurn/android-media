package com.tokopedia.play.view.viewcomponent

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getVisiblePercent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.ui.productsheet.itemdecoration.ProductLineItemDecoration
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.ui.productsheet.viewholder.ProductSheetSectionViewHolder
import com.tokopedia.play.view.custom.PlayVoucherView
import com.tokopedia.play.view.custom.RectangleShadowOutlineProvider
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.PlayEmptyBottomSheetInfoUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.util.extension.awaitLayout
import com.tokopedia.play_common.util.extension.getBitmapFromUrl
import com.tokopedia.play_common.view.BottomSheetHeader
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.viewcomponent.BottomSheetViewComponent
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.tokopedia.play_common.R as commonR

/**
 * Created by jegul on 31/07/20
 */
class ProductSheetViewComponent(
    container: ViewGroup,
    private val listener: Listener,
    private val scope: CoroutineScope
) : BottomSheetViewComponent(container, R.id.cl_product_sheet) {

    private val clProductContent: ConstraintLayout = findViewById(R.id.cl_product_content)
    private val clVoucherContent: ConstraintLayout = findViewById(R.id.cl_product_voucher_content)
    private val rvProductList: RecyclerView = findViewById(R.id.rv_product_list)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)

    private val globalError: GlobalError = findViewById(R.id.global_error_product)

    private val clProductEmpty: ConstraintLayout = findViewById(R.id.cl_product_empty)
    private val btnProductEmpty: UnifyButton = findViewById(R.id.btn_action_product_empty)
    private val tvHeaderProductEmpty: TextView = findViewById(R.id.tv_title_product_empty)
    private val tvBodyProductEmpty: TextView = findViewById(R.id.tv_desc_product_empty)
    private val ivProductEmpty: AppCompatImageView = findViewById(R.id.iv_img_illustration)

    private val voucherInfo: PlayVoucherView = findViewById(R.id.voucher_view)

    private val header: BottomSheetHeader = findViewById(R.id.bottom_sheet_header)

    private val impressionSet = mutableSetOf<String>()

    private val productCardListener = object : ProductLineViewHolder.Listener {
        override fun onProductClicked(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section
        ) {
            listener.onProductCardClicked(
                this@ProductSheetViewComponent,
                product,
                section,
                viewHolder.adapterPosition
            )
        }

        override fun onButtonTransactionProduct(
            viewHolder: ProductLineViewHolder,
            product: PlayProductUiModel.Product,
            section: ProductSectionUiModel.Section,
            action: ProductAction
        ) {
            listener.onButtonTransactionClicked(
                this@ProductSheetViewComponent,
                product,
                section,
                action
            )
        }
    }

    private val productAdapter = ProductSheetAdapter(
        sectionListener = object : ProductSheetSectionViewHolder.Listener {
            override fun onReminderClicked(
                holder: ProductSheetSectionViewHolder,
                section: ProductSectionUiModel.Section
            ) {
                listener.onReminderClicked(
                    this@ProductSheetViewComponent,
                    section
                )
            }

            override fun onReminderImpressed(
                holder: ProductSheetSectionViewHolder,
                section: ProductSectionUiModel.Section
            ) {
                listener.onReminderImpressed(
                    this@ProductSheetViewComponent,
                    section
                )
            }

            override fun onInformationClicked(
                holder: ProductSheetSectionViewHolder,
                section: ProductSectionUiModel.Section
            ) {
                listener.onInformationClicked(this@ProductSheetViewComponent)
            }

            override fun onInformationImpressed(
                holder: ProductSheetSectionViewHolder,
                section: ProductSectionUiModel.Section
            ) {
                listener.onInformationImpressed(this@ProductSheetViewComponent)
            }
        },
        productListener = productCardListener,
    )

    private val itemDecoration: ProductLineItemDecoration

    private val voucherListener = object : PlayVoucherView.Listener {
        override fun onVoucherInfoClicked(view: PlayVoucherView) {
            listener.onInfoVoucherClicked(this@ProductSheetViewComponent)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_SETTLING -> recyclerView.stopScroll()
                RecyclerView.SCROLL_STATE_IDLE -> sendImpression()
            }
        }
    }

    private val linearLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(rvProductList.context, RecyclerView.VERTICAL, false)
    }

    init {
        rvProductList.apply {
            adapter = productAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(scrollListener)
            itemDecoration = ProductLineItemDecoration(context, this)
            addItemDecoration(itemDecoration)
            setHasFixedSize(true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clProductContent.setPadding(clProductContent.paddingLeft, clProductContent.paddingTop, clProductContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

        clVoucherContent.outlineProvider = RectangleShadowOutlineProvider()
        clVoucherContent.clipToOutline = true
        voucherInfo.setupListener(voucherListener)

        header.setListener(object : BottomSheetHeader.Listener {
            override fun onCloseClicked(view: BottomSheetHeader) {
                listener.onCloseButtonClicked(this@ProductSheetViewComponent)
            }

            override fun onIconClicked(view: BottomSheetHeader) {
                listener.onCartClicked(this@ProductSheetViewComponent)
            }

            override fun impressIcon(view: BottomSheetHeader) {
                listener.onImpressedCart(this@ProductSheetViewComponent)
            }
        })
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()

        /**
         * Everytime user clicks [View All Icon] hit tracker
         */
        scope.launch {
            rootView.awaitLayout()
            impressionSet.clear()
            sendImpression()
        }
    }

    fun showCart(shouldShow: Boolean) {
        if (shouldShow) {
            header.setIconNotification(IconUnify.CART)
        } else {
            header.setIconNotification(null)
        }
    }

    fun setProductSheet(
        sectionList: List<ProductSectionUiModel>,
        voucherList: List<PlayVoucherUiModel>,
        title: String,
    ) {
        showContent(true)
        header.setTitle(title)

        val sections = sectionList.filterIsInstance<ProductSectionUiModel.Section>()
        val newProductList = buildProductList(sections)

        if (newProductList == productAdapter.getItems()) return

        productAdapter.setItemsAndAnimateChanges(newProductList)

        scope.launch {
            itemDecoration.setGuidelines(getBackgroundGuidelines(sections))
            if (productAdapter.getItems() != newProductList) {
                rvProductList.invalidateItemDecorations()
            }
        }
        val merchantVoucher = voucherList.filterIsInstance<PlayVoucherUiModel.Merchant>()
        voucherInfo.showWithCondition(merchantVoucher.isNotEmpty())
        if (merchantVoucher.isNotEmpty()) {
            voucherInfo.setupView(merchantVoucher.first(), merchantVoucher.size)
            listener.onInfoVoucherImpressed(this, merchantVoucher.first())
        }

        impressionSet.clear()
        sendImpression()
    }

    fun showPlaceholder() {
        showContent(true)
        productAdapter.setItemsAndAnimateChanges(
            List(PLACEHOLDER_COUNT) { ProductSheetAdapter.Item.Loading }
        )
    }

    fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        showContent(false)
        clProductEmpty.hide()

        globalError.setActionClickListener {
            onError()
        }

        globalError.setType(
            if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    fun showEmpty(emptyBottomSheetInfoUi: PlayEmptyBottomSheetInfoUiModel) {
        showContent(false)
        globalError.hide()

        tvHeaderProductEmpty.text = emptyBottomSheetInfoUi.header
        tvBodyProductEmpty.text = emptyBottomSheetInfoUi.body
        btnProductEmpty.text = emptyBottomSheetInfoUi.button
        ivProductEmpty.loadImage(emptyBottomSheetInfoUi.imageUrl)

        btnProductEmpty.setOnClickListener {
            listener.onEmptyButtonClicked(this@ProductSheetViewComponent)
        }
    }

    fun setCartCount(count: Int) {
        header.setIconNotificationText(
            if (count <= 0) ""
            else count.toString()
        )
    }

    private fun showContent(shouldShow: Boolean) {
        if (shouldShow) {
            header.showTitle(true)
            rvProductList.show()

            globalError.hide()
            clProductEmpty.hide()
        } else {
            header.showTitle(false)
            rvProductList.hide()
            voucherInfo.hide()

            globalError.show()
            clProductEmpty.show()
        }
    }

    private fun buildProductList(
        sectionList: List<ProductSectionUiModel.Section>
    ): List<ProductSheetAdapter.Item> {
        return buildList {
            sectionList.forEach { section ->
                if (section.productList.isEmpty()) return@forEach

                add(
                    ProductSheetAdapter.Item.Section(section)
                )

                section.productList.forEach {
                    add(
                        ProductSheetAdapter.Item.Product(it, section)
                    )
                }
            }
        }
    }

    private suspend fun getBackgroundGuidelines(
        sectionList: List<ProductSectionUiModel.Section>
    ): List<ProductLineItemDecoration.BackgroundGuideline> {
        var currentIndex = -1
        return sectionList.map { section ->
            val startIndex = currentIndex + 1
            val endIndex = startIndex + section.productList.size
            val background = when {
                section.productList.isEmpty() -> {
                    ProductLineItemDecoration.Background.Color.Solid(Color.TRANSPARENT)
                }
                section.config.background.imageUrl.isNotBlank() -> {
                    try {
                        ProductLineItemDecoration.Background.Image(
                            getBitmapFromUrl(rootView.context, section.config.background.imageUrl)
                        )
                    } catch (e: IllegalStateException) {
                        ProductLineItemDecoration.Background.Color.Solid(Color.TRANSPARENT)
                    }
                }
                section.config.background.gradients.isNotEmpty() -> {
                    if (section.config.background.gradients.size > 1) {
                        ProductLineItemDecoration.Background.Color.Gradient(
                            LinearGradient(
                                0f,
                                0f,
                                0f,
                                rvProductList.height.toFloat(),
                                Color.parseColor(section.config.background.gradients.first()),
                                Color.parseColor(section.config.background.gradients[1]),
                                Shader.TileMode.CLAMP
                            )
                        )
                    } else {
                        ProductLineItemDecoration.Background.Color.Solid(
                            Color.parseColor(section.config.background.gradients.first())
                        )
                    }
                }
                else -> {
                    ProductLineItemDecoration.Background.Color.Solid(Color.TRANSPARENT)
                }
            }

            currentIndex = endIndex

            ProductLineItemDecoration.BackgroundGuideline(
                startIndex = startIndex,
                endIndex = endIndex,
                background = background
            )
        }
    }

    private fun sendImpression() = synchronized(impressionSet) {
        if (getVisiblePercent(rootView) == -1) return@synchronized

        val products = getVisibleProducts().filterNot {
            impressionSet.contains(it.key.product.id)
        }
        listener.onProductImpressed(this, products)
        products.forEach {
            impressionSet.add(it.key.product.id)
        }
    }

    private fun getVisibleProducts(): Map<ProductSheetAdapter.Item.Product, Int> {
        val products = productAdapter.getItems()
        if (products.isNotEmpty()) {
            val startPosition = linearLayoutManager.findFirstVisibleItemPosition()
            val endPosition = linearLayoutManager.findLastVisibleItemPosition()
            if (startPosition > -1 && endPosition < products.size) {
                return (startPosition..endPosition)
                    .filter { rvProductList.findViewHolderForAdapterPosition(it) is ProductLineViewHolder }
                    .associateBy { products[it] as ProductSheetAdapter.Item.Product }
            }
        }
        return emptyMap()
    }

    /**
     * Lifecycle Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvProductList.removeItemDecoration(itemDecoration)
        rvProductList.removeOnScrollListener(scrollListener)
        itemDecoration.release()
        voucherInfo.setupListener(null)
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 5
    }

    interface Listener {
        fun onCloseButtonClicked(view: ProductSheetViewComponent)
        fun onButtonTransactionClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, action: ProductAction)
        fun onProductCardClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, position: Int)
        fun onEmptyButtonClicked(view: ProductSheetViewComponent)
        fun onProductImpressed(
            view: ProductSheetViewComponent,
            products: Map<ProductSheetAdapter.Item.Product, Int>
        )
        fun onInfoVoucherClicked(view: ProductSheetViewComponent)
        fun onReminderClicked(view: ProductSheetViewComponent, productSectionUiModel: ProductSectionUiModel.Section)
        fun onReminderImpressed(view: ProductSheetViewComponent, section: ProductSectionUiModel.Section)
        fun onInformationClicked(view: ProductSheetViewComponent)
        fun onInformationImpressed(view: ProductSheetViewComponent)
        fun onInfoVoucherImpressed(view: ProductSheetViewComponent, voucher: PlayVoucherUiModel.Merchant)

        fun onCartClicked(view: ProductSheetViewComponent)
        fun onImpressedCart(view: ProductSheetViewComponent)
    }
}
