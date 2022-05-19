package com.tokopedia.tokopedianow.home.presentation.viewholder

import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselUiModel
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderCustomView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselBinding
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselProductCardAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselProductCardTypeFactoryImpl
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeLeftCarouselProductCardDiffer
import com.tokopedia.tokopedianow.home.presentation.view.listener.HomeLeftCarouselCallback
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeLeftCarouselViewHolder (
    itemView: View,
    private val homeLeftCarouselListener: HomeLeftCarouselCallback? = null
) : AbstractViewHolder<HomeLeftCarouselUiModel>(itemView), CoroutineScope,
    TokoNowDynamicHeaderCustomView.HeaderCustomViewListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel
    }

    private var binding: ItemTokopedianowHomeLeftCarouselBinding? by viewBinding()

    private val masterJob = SupervisorJob()

    private var ivParallaxImage: AppCompatImageView? = null
    private var rvProduct: RecyclerView? = null
    private var viewParallaxBackground: View? = null
    private var layoutManager: LinearLayoutManager? = null
    private var dynamicHeaderCustomView: TokoNowDynamicHeaderCustomView? = null
    private var uiModel: HomeLeftCarouselUiModel? = null

    private val adapter by lazy {
        HomeLeftCarouselProductCardAdapter(
            baseListAdapterTypeFactory = HomeLeftCarouselProductCardTypeFactoryImpl(
                productCardListener = homeLeftCarouselListener,
                productCardSeeMoreListener = homeLeftCarouselListener,
                productCardSpaceListener = homeLeftCarouselListener
            ),
            differ = HomeLeftCarouselProductCardDiffer()
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

    override fun bind(element: HomeLeftCarouselUiModel) {
        uiModel = element
        dynamicHeaderCustomView?.setModel(
            model = element.header,
            listener = this
        )
        setupRecyclerView(
            element = element
        )
        setupImage(
            element = element
        )
        setupBackgroundColor(
            backgroundColorArray = element.backgroundColorArray
        )
        onLeftCarouselImpressed(
            element = element
        )
    }

    override fun onSeeAllClicked(appLink: String) {
        homeLeftCarouselListener?.onSeeMoreClicked(
            appLink = appLink,
            channelId = uiModel?.id.orEmpty(),
            headerName = uiModel?.header?.title.orEmpty()
        )
    }

    private fun onLeftCarouselImpressed(element: HomeLeftCarouselUiModel) {
        if (!element.isInvoke) {
            homeLeftCarouselListener?.onLeftCarouselImpressed(
                channelId = element.id,
                headerName = element.header.title
            )
            element.invoke()
        }
    }

    private fun setupRecyclerView(element: HomeLeftCarouselUiModel) {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        rvProduct?.layoutManager = layoutManager
        restoreInstanceStateToLayoutManager()
        setHeightRecyclerView()
        rvProduct?.adapter = adapter
        submitList(element)
    }

    private fun submitList(element: HomeLeftCarouselUiModel) {
        adapter.submitList(element.productList)
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
//            uiModel?.productList?.firstOrNull()?.let {
//                it.rvState = recyclerView.layoutManager?.onSaveInstanceState()
//            }
        }
    }

    private fun restoreInstanceStateToLayoutManager() {
        launch {
//            val rvState =  uiModel?.productList?.firstOrNull()?.rvState
//            if (null != rvState) {
//                rvProduct?.layoutManager?.onRestoreInstanceState(rvState)
//            }
        }
    }

    private fun setHeightRecyclerView() {
        val carouselLayoutParams = rvProduct?.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        rvProduct?.layoutParams = carouselLayoutParams
    }

    private fun setupImage(element: HomeLeftCarouselUiModel) {
        ivParallaxImage?.show()
        ivParallaxImage?.layout(0,0,0,0)
        ivParallaxImage?.translationX = 0f
        ivParallaxImage?.alpha = 1f
        ivParallaxImage?.loadImage(element.imageBanner)
        ivParallaxImage?.setOnClickListener {
            homeLeftCarouselListener?.onLeftCarouselLeftImageClicked(
                appLink = element.imageBannerAppLink,
                channelId = element.id,
                headerName = element.header.title
            )
        }
    }

    private fun setupBackgroundColor(backgroundColorArray: ArrayList<String>) {
        viewParallaxBackground?.setGradientBackground(backgroundColorArray)
    }

    interface HomeLeftCarouselListener {
        fun onSeeMoreClicked(appLink: String, channelId: String, headerName: String)
        fun onLeftCarouselImpressed(channelId: String, headerName: String)
        fun onLeftCarouselLeftImageClicked(appLink: String, channelId: String, headerName: String)
    }
}