package com.tokopedia.shop.home.view.customview.directpurchase

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.customview.ImageTabData
import com.tokopedia.common.customview.ImageTabs
import com.tokopedia.common.customview.MultipleContentSwitcher
import com.tokopedia.common.setRetainTextColor
import com.tokopedia.shop.databinding.LayoutDirectPurchaseWidgetBinding

/***
 * A Vertical LinearLayout that shows below:
 * [Title] ex: Sprint Summer Collection 2023
 * [Title Switcher] ex: Man/Woman/Kids
 * [Etalase Switcher] ex: T-Shirt/Shirts/Pants
 * [Horizontal RecyclerView] to show Product (max 10 items) with plus button on each item
 *
 * figma: https://www.figma.com/file/kOSxQDOOCtQQe8ZJuMlGf8/Reimagine-Shop-Page?node-id=4514%3A308737&mode=dev
 *
 * How to use:
 * directPurchaseWidgetView = inflater.inflate
 *   <com.....DirectPurchaseWidgetView width="match_parent" height="wrap_content"/>
 *
 * to set max etalase shown:
 * (Optional) directPurchaseWidgetView.setMaxProductShown(5)
 *   If not set, default max etalase shown is 5. Above than 5, see more view will be displayed.
 *
 * directPurchaseWidgetView.setListener(
 *    triggerLoadProductDirectPurchase = { etalaseId, timestampLastCaptured ->
 *       // action when user navigate to selected etalase
 *       // viewModel.loadEtalase(etalaseId)
 *       // timestampLastCaptured is the timestamp of the productList of the etalase is captured.
 *       // This timestamp can be used to determine if we want to reload the product List or not
 *       // you can set diffThreshold by setDiffTriggerloadProduct() or handle manually.
 *    }
 *    onProductDirectPurchaseClick = { data ->
 *       // action when user click product
 *       // Usually you want to navigate to product page
 *    }
 *    onAddButtonProductDirectPurchaseClick = { data ->
 *       // action when user click add product
 *       // Example action: open add to cart
 *    }
 *    onSeeMoreClick = { etalaseId ->
 *       // action when user click see more on etalase carousel
 *       // open product list for selected etalaseId
 *    }
 * )
 *
 * (Optional) directPurchaseWidgetView.setWidgetColor(<<WidgetColor>>)
 * (Optional) directPurchaseWidgetView.setTypeTab()
 * (Optional) directPurchaseWidgetView.setSeeAllCardModeType()
 * (Optional) directPurchaseWidgetView.setDiffTriggerloadProduct(30000) // 5 minute cache
 * directPurchaseWidgetView.setData(<<WidgetData>>)
 *   In Widget data there is timestamp.
 *   Everytime the product list of selected etalase is fetched, either it is failed/success,
 *   put the productList with the currentTimeStamp in the model, this is to keep track the cache.
 *
 *   This WidgetData when set into WidgetView,
 *   this view will intelligently diff the previous data and current data,
 *   and will only render the difference.
 */

class DirectPurchaseWidgetView : LinearLayout,
    MultipleContentSwitcher.MultipleContentSwitcherListener, ImageTabs.ImageTabsListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    interface DirectPurchaseWidgetViewListener {
        fun triggerLoadProductDirectPurchase(etalaseId: String, timestampLastCaptured: Long)

        fun onAddButtonProductDirectPurchaseClick(data: ProductCardDirectPurchaseDataModel)

        fun onProductDirectPurchaseClick(data: ProductCardDirectPurchaseDataModel)

        fun onSeeMoreClick(etalaseId: String)

    }

    companion object {
        const val MAX_PRODUCT_SHOWN = 5
        const val DEFAULT_THRESHOLD_RECAPTURE_DATA = 30000 // in ms
    }

    private var listener: DirectPurchaseWidgetViewListener? = null
    private var diffTriggerLoadProduct = DEFAULT_THRESHOLD_RECAPTURE_DATA

    fun setListener(listener: DirectPurchaseWidgetViewListener?) {
        this@DirectPurchaseWidgetView.listener = listener
    }

    private val binding = LayoutDirectPurchaseWidgetBinding.inflate(
        LayoutInflater.from(context), this
    )

    private var widgetData: WidgetData? = null
    private var colorPallete: ColorPallete? = null

    //wrapper to unifyTabs that support images
    private var imageTabsWrapper =
        ImageTabs(binding.tabs).apply { setListener(this@DirectPurchaseWidgetView) }

    private var frameBannerWrapperView = FrameBannerWrapperView(binding.frameBanner, binding.switcher,this)

    private val productAdapter: ProductCarouselDirectPurchaseAdapter by lazyThreadSafetyNone {
        ProductCarouselDirectPurchaseAdapter(object :
            ProductDirectPurchaseViewHolder.ProductDirectPurchaseContentVHListener {
            override fun onAddButtonProductDirectPurchaseClick(data: ProductCardDirectPurchaseDataModel) {
                listener?.onAddButtonProductDirectPurchaseClick(data)
            }

            override fun onProductDirectPurchaseClick(data: ProductCardDirectPurchaseDataModel) {
                listener?.onProductDirectPurchaseClick(data)
            }
        }, object :
            ProductDirectPurchaseViewHolder.ProductDirectPurchaseErrorVHListener {
            override fun onRetryClick() {
                val currentEtalase = getSelectedEtalase() ?: return
                listener?.triggerLoadProductDirectPurchase(etalaseId = currentEtalase.etalaseId, 0)
            }
        }, object : ProductDirectPurchaseViewHolder.ProductDirectPurchaseSeeMoreVHListener {
            override fun onSeeMoreClick() {
                val currentEtalase = getSelectedEtalase() ?: return
                listener?.onSeeMoreClick(etalaseId = currentEtalase.etalaseId)
            }

        }, MAX_PRODUCT_SHOWN)
    }

    private fun <T> lazyThreadSafetyNone(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

    init {
        widgetData = null
        orientation = VERTICAL
        setUpProductRecyclerView()
    }

    fun setColor(colorPalleteInput: ColorPallete) {
        this.colorPallete = colorPalleteInput
        frameBannerWrapperView.setColor(colorPalleteInput)
        imageTabsWrapper.setColor(colorPalleteInput)
        productAdapter.setWidgetColor(colorPalleteInput)
    }

    /**
     * TYPE_RECT = 0
     * TYPE_CIRCLE = 1
     */
    fun setTypeTab(typeTab: Int) {
        imageTabsWrapper.setTypeTab(typeTab)
    }

    /**
     * main function to set the data into the view.
     * Please put timestamp = System.currentTimeMillis() whenever the productlist is fetched
     * in each Etalase object.
     */
    fun setData(widgetDataInput: WidgetData) {
        this@DirectPurchaseWidgetView.widgetData = widgetDataInput
        setUpTitleText(widgetDataInput.widgetTitle)
        frameBannerWrapperView.setData(widgetDataInput.titleList)
        val etalaseList = getCurrentTitle()?.etalaseList
        setUpEtalaseList(etalaseList)
        submitProductList(etalaseList?.get(imageTabsWrapper.selectedIndex))
        this.visibility = View.VISIBLE
    }

    fun clearData() {
        this@DirectPurchaseWidgetView.widgetData = null
        this.visibility = View.GONE
    }

    /**
     * set max product shown in product carousel List
     * If not set, default max etalase shown is 5. Above than 5, see more view will be displayed.
     */
    fun setMaxProductShown(maxProductShown: Int) {
        productAdapter.setMaxProductShown(maxProductShown)
    }

    /**
     * MODE_INVERT(8) or MODE_NORMAL(6)
     */
    fun setSeeAllCardModeType(type: Int) {
        productAdapter.setSeeAllCardModeType(type)
    }

    /**
     * set how much time load product will be triggered since the last time fetched.
     * Time is in milliseconds. 30000 = 5 minutes.
     * If 5 minutes is set, the loadProduct will be triggered again
     * if user click etalase after above 5 minutes, even though the product list is already set
     * You can set this to 0, and handle the diff manually in function triggerLoadProductDirectPurchase
     */
    fun setDiffTriggerloadProduct(diffTriggerLoadProductParam: Int) {
        diffTriggerLoadProduct = diffTriggerLoadProductParam
    }

    private fun setUpProductRecyclerView() {
        binding.rvProduct.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvProduct.adapter = productAdapter
        binding.rvProduct.addItemDecoration(ProductDirectPurchaseItemDecoration(context))
        productAdapter.setLoading()
    }

    private fun setUpTitleText(titleText: String) {
        if (titleText.isNotEmpty()) {
            binding.tpTitle.text = titleText
            binding.tpTitle.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
            binding.tpTitle.visibility = View.VISIBLE
        } else {
            binding.tpTitle.visibility = View.GONE
        }
    }

    private fun setUpEtalaseList(etalaseList: List<Etalase>?) {
        imageTabsWrapper.setData(etalaseList?.map {
            ImageTabData(
                it.etalaseId,
                it.name,
                it.imageUrl
            )
        })
    }

    /**
     * Trigger when multiple content switcher item is clicked
     * Example, click "Men" "Woman", or "Kids", this will trigger:
     * 1. image url change
     * 2. etalase content change
     * 3. product etalase change (see onImageTabSelected)
     */
    override fun onMultipleSwitcherSelected(selectedIndex: Int, selectedItem: String) {
        val selectedTitle = widgetData?.titleList?.get(selectedIndex)
        binding.ivTitle.setImageUrl(selectedTitle?.imageUrl ?: "")
        setUpEtalaseList(selectedTitle?.etalaseList)
    }

    override fun onImageTabSelected(index: Int, imageTabData: ImageTabData?) {
        val prevEtalaseId = getSelectedEtalase()?.etalaseId
        val currentEtalase = getSelectedEtalase()
        submitProductList(currentEtalase)
        // trigger scroll to first position only if the etalase is changed.
        if (prevEtalaseId != currentEtalase?.etalaseId) {
            binding.rvProduct.post {
                binding.rvProduct.layoutManager?.scrollToPosition(0)
            }
        }
    }

    private fun submitProductList(data: Etalase?) {
        data?.apply {
            val productList = productList.toMutableList()
            if (productList.isEmpty()) {
                if (errorMessageIfFailedFetch?.isNotEmpty() == true) {
                    productAdapter.setError(errorMessageIfFailedFetch.toString())
                } else if (lastTimeStampProductListCaptured == 0L) {
                    productAdapter.setLoading()
                } else {
                    productAdapter.setEmpty()
                }
            } else {
                productAdapter.setProductData(productList.map {
                    ProductCarouselDirectPurchaseAdapter.Model.Content(
                        it
                    )
                })
            }
            if (System.currentTimeMillis() - lastTimeStampProductListCaptured > diffTriggerLoadProduct) {
                listener?.triggerLoadProductDirectPurchase(
                    etalaseId,
                    lastTimeStampProductListCaptured
                )
            }
        }
    }

    private fun getSelectedEtalase() =
        getCurrentTitle()?.etalaseList?.get(imageTabsWrapper.selectedIndex)

    private fun getCurrentTitle() =
        this@DirectPurchaseWidgetView.widgetData?.titleList?.get(binding.switcher.getSelectedIndex())

}


