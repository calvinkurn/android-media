package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.adapter.ProductCardAdapter
import com.tokopedia.shop_widget.common.adapter.ProductCardDiffer
import com.tokopedia.shop_widget.common.customview.DynamicHeaderCustomView
import com.tokopedia.shop_widget.common.customview.DynamicHeaderCustomView.HeaderCustomViewListener
import com.tokopedia.shop_widget.common.typefactory.ProductCardTypeFactoryImpl
import com.tokopedia.shop_widget.common.uimodel.ProductCardSeeAllUiModel
import com.tokopedia.shop_widget.common.uimodel.ProductCardSpaceUiModel
import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.common.util.ColorUtil.getBackGroundColor
import com.tokopedia.shop_widget.common.viewholder.ProductCardSeeAllViewHolder
import com.tokopedia.shop_widget.common.viewholder.ProductCardViewHolder
import com.tokopedia.shop_widget.databinding.ItemThematicWidgetBinding
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

class ThematicWidgetViewHolder (
    itemView: View,
    private val listener: ThematicWidgetListener
) : AbstractViewHolder<ThematicWidgetUiModel>(itemView), CoroutineScope, HeaderCustomViewListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_thematic_widget

        private const val MAX_TOTAL_PRODUCT = 10
    }

    private var binding: ItemThematicWidgetBinding? by viewBinding()

    private val masterJob = SupervisorJob()

    private var ivParallaxImage: AppCompatImageView? = null
    private var rvProduct: RecyclerView? = null
    private var viewParallaxBackground: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dynamicHeaderCustomView: DynamicHeaderCustomView? = null
    private var uiModel: ThematicWidgetUiModel? = null

    private val adapter by lazy {
        ProductCardAdapter(
            baseListAdapterTypeFactory = ProductCardTypeFactoryImpl(
                productCardListener = productCardListenerImpl(),
                productCardSeeAllListener = productCardSeeAllListenerImpl()
            ),
            differ = ProductCardDiffer()
        )
    }

    init {
        binding?.let {
            rvProduct = it.rvProduct
            dynamicHeaderCustomView = it.dynamicHeaderCustomView
            ivParallaxImage = it.parallaxImage
            viewParallaxBackground = it.parallaxBackground
        }
        rvProduct?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                saveInstanceStateToLayoutManager(recyclerView)
                calculateParallaxImage(dx)
            }
        })
    }

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: ThematicWidgetUiModel) {
        uiModel = element
        dynamicHeaderCustomView?.setModel(
            model = element.header,
            listener = this
        )
        setupRecyclerView(
            element = element
        )
        setupImage(
            imageBanner = element.imageBanner
        )
        setupBackgroundColor(
            startBackGroundColor = element.firstBackgroundColor,
            endBackGroundColor = element.secondBackgroundColor
        )
        listener.onThematicWidgetImpressListener(element, adapterPosition)
    }

    override fun onSeeAllClick(appLink: String) {
        listener.onSeeAllThematicWidgetClickListener(appLink, uiModel?.campaignId.orEmpty(), uiModel?.name.orEmpty())
    }

    private fun setupRecyclerView(element: ThematicWidgetUiModel) {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        rvProduct?.layoutManager = layoutManager
        restoreInstanceStateToLayoutManager()
        setHeightRecyclerView()
        rvProduct?.adapter = adapter
        submitList(element)
    }

    private fun submitList(element: ThematicWidgetUiModel) {
        val productList : List<ProductCardUiModel>
        val newList = mutableListOf<Visitable<*>>()
        newList.add(ProductCardSpaceUiModel())
        if (element.header.totalProduct <= MAX_TOTAL_PRODUCT) {
            productList = element.productList
            newList.addAll(productList)
        } else {
            productList = element.productList.take(MAX_TOTAL_PRODUCT)
            newList.addAll(productList)
            newList.add(ProductCardSeeAllUiModel(element.header.ctaTextLink))
        }
        adapter.submitList(newList)
        listener.onProductCardThematicWidgetImpressListener(productList, adapterPosition, uiModel?.campaignId.orEmpty(), uiModel?.name.orEmpty())
    }

    private fun calculateParallaxImage(dx: Int) {
        launch {
            layoutManager?.apply {
                if (findFirstVisibleItemPosition() == 0 && dx != 0) {
                    findViewByPosition(findFirstVisibleItemPosition())?.apply {
                        val distanceFromLeft = left
                        val translateX = distanceFromLeft * 0.2f
                        if (translateX <= 0) {
                            ivParallaxImage?.translationX = translateX
                            if (distanceFromLeft <= 0) {
                                val itemSize = width.toFloat()
                                val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                                ivParallaxImage?.alpha = 1 - alpha
                            }
                        } else {
                            ivParallaxImage?.translationX = 0f
                            ivParallaxImage?.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun saveInstanceStateToLayoutManager(recyclerView: RecyclerView) {
        launch {
            uiModel?.productList?.firstOrNull()?.let {
                it.rvState = recyclerView.layoutManager?.onSaveInstanceState()
            }
        }
    }

    private fun restoreInstanceStateToLayoutManager() {
        launch {
            val rvState =  uiModel?.productList?.firstOrNull()?.rvState
            if (null != rvState) {
                rvProduct?.layoutManager?.onRestoreInstanceState(rvState)
            }
        }
    }

    private fun setHeightRecyclerView() {
        val carouselLayoutParams = rvProduct?.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        rvProduct?.layoutParams = carouselLayoutParams
    }

    private fun setupImage(imageBanner: String) {
        ivParallaxImage?.show()
        ivParallaxImage?.layout(0,0,0,0)
        ivParallaxImage?.translationX = 0f
        ivParallaxImage?.alpha = 1f
        ivParallaxImage?.loadImage(imageBanner)
    }

    private fun setupBackgroundColor(startBackGroundColor: String?, endBackGroundColor: String?) {
        val colors = intArrayOf(
            getBackGroundColor(itemView.context, startBackGroundColor, com.tokopedia.unifyprinciples.R.color.Unify_N75),
            getBackGroundColor(itemView.context, endBackGroundColor, com.tokopedia.unifyprinciples.R.color.Unify_N75)
        )
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
        viewParallaxBackground?.background = gradientDrawable
    }

    private fun productCardListenerImpl(): ProductCardViewHolder.ProductCardListener = object : ProductCardViewHolder.ProductCardListener {
        override fun onProductCardClickListener(product: ProductCardUiModel) {
            listener.onProductCardThematicWidgetClickListener(
                product = product,
                campaignId = uiModel?.campaignId.orEmpty(),
                campaignName = uiModel?.name.orEmpty(),
                position = adapterPosition
            )
        }
    }

    private fun productCardSeeAllListenerImpl(): ProductCardSeeAllViewHolder.ProductCardSeeAllListener = object : ProductCardSeeAllViewHolder.ProductCardSeeAllListener {
        override fun onProductCardSeeAllClickListener(appLink: String) {
            listener.onProductCardSeeAllThematicWidgetClickListener(
                appLink = appLink,
                campaignId = uiModel?.campaignId.orEmpty(),
                campaignName = uiModel?.name.orEmpty()
            )
        }
    }

    interface ThematicWidgetListener {
        fun onThematicWidgetImpressListener(model: ThematicWidgetUiModel, position: Int)
        fun onProductCardThematicWidgetImpressListener(products: List<ProductCardUiModel>, position: Int, campaignId: String, campaignName: String)
        fun onProductCardThematicWidgetClickListener(product: ProductCardUiModel, campaignId: String, campaignName: String, position: Int)
        fun onProductCardSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String)
        fun onSeeAllThematicWidgetClickListener(appLink: String, campaignId: String, campaignName: String)
    }
}
