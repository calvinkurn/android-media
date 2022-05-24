package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcMixLeftBinding
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselViewAllCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselViewAllCardViewHolder.Companion.DEFAULT_VIEW_ALL_ID
import com.tokopedia.home_component.util.*
import com.tokopedia.home_component.viewholders.adapter.MixLeftAdapter
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * @author by yoasfs on 2020-03-05
 */

@SuppressLint("SyntheticAccessor")
class MixLeftComponentViewHolder (itemView: View,
                                  val mixLeftComponentListener: MixLeftComponentListener?,
                                  val homeComponentListener: HomeComponentListener?,
                                  private val parentRecycledViewPool: RecyclerView.RecycledViewPool? = null,
                                  private val cardInteraction: Boolean = false
) : AbstractViewHolder<MixLeftDataModel>(itemView), CoroutineScope, CommonProductCardCarouselListener {

    private lateinit var adapter: MixLeftAdapter

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var recyclerView: RecyclerView
    private lateinit var image: ImageView
    private lateinit var loadingBackground: ImageView
    private lateinit var parallaxBackground: View
    private lateinit var containerMixLeft: ConstraintLayout

    private lateinit var layoutManager: LinearLayoutManager

    private var isCacheData = false

    private var binding: GlobalDcMixLeftBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_mix_left
        val RECYCLER_VIEW_ID = R.id.rv_product
        private const val FPM_MIX_LEFT = "home_mix_left"
    }

    override fun bind(element: MixLeftDataModel) {
        isCacheData = element.isCache
        initVar()
        setupBackground(element.channelModel)
        setupList(element.channelModel)
        setSnapEffect()
        setHeaderComponent(element)
        setChannelDivider(element)

        itemView.addOnImpressionListener(element.channelModel)  {
            if (!isCacheData)
                mixLeftComponentListener?.onMixLeftImpressed(element.channelModel, adapterPosition)
        }
    }

    override fun bind(element: MixLeftDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        if (!isCacheData)
            mixLeftComponentListener?.onProductCardImpressed(channelModel, channelGrid, adapterPosition, position)
    }

    override fun onProductCardClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        mixLeftComponentListener?.onProductCardClicked(channelModel, channelGrid, adapterPosition, position, applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        mixLeftComponentListener?.onSeeMoreCardClicked(channel, applink)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        mixLeftComponentListener?.onEmptyCardClicked(channel, applink, parentPos)
    }

    private fun setChannelDivider(element: MixLeftDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun initVar() {
        recyclerView = itemView.findViewById(R.id.rv_product)
        image = itemView.findViewById(R.id.parallax_image)
        loadingBackground = itemView.findViewById(R.id.background_loader)
        parallaxBackground = itemView.findViewById(R.id.parallax_background)
        containerMixLeft = itemView.findViewById(R.id.container_mixleft)
    }

    private fun setupBackground(channel: ChannelModel) {
        if (channel.channelBanner.imageUrl.isNotEmpty()) {
            loadingBackground.show()
            image.invisible()

            //reset layout to 0,0,0,0. There is possibility where view is being reused, makes image
            //becomes stretched.
            //https://github.com/bumptech/glide/issues/1591
            image.layout(0,0,0,0)

            //reset image state
            image.translationX = 0f
            image.alpha = 1f

            image.addOnImpressionListener(channel){
                if (!isCacheData)
                    mixLeftComponentListener?.onImageBannerImpressed(channel, adapterPosition)
            }
            parallaxBackground.setBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.transparent)
            )
            image.loadImageWithoutPlaceholder(channel.channelBanner.imageUrl, FPM_MIX_LEFT, object : ImageHandler.ImageLoaderStateListener{
                override fun successLoad() {
                    parallaxBackground.setGradientBackground(channel.channelBanner.gradientColor)
                    loadingBackground.hide()
                    image.show()
                }

                override fun failedLoad() {
                    parallaxBackground.setGradientBackground(channel.channelBanner.gradientColor)
                    loadingBackground.hide()
                    image.show()
                }
            })
        } else {
            loadingBackground.hide()
        }
    }

    private fun setupList(channel: ChannelModel) {
        image.alpha = 1f
        recyclerView.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel, cardInteraction)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(CarouselEmptyCardDataModel(channel, adapterPosition, this, channel.channelBanner.applink))
        val productDataList = convertDataToProductData(channel)
        listData.addAll(productDataList)

        launch {
            try {
                recyclerView.setHeightBasedOnProductCardMaxHeight(productDataList.map {it.productModel})
            }
            catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }

        if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty()) {
            if(channel.channelViewAllCard.id != DEFAULT_VIEW_ALL_ID && channel.channelViewAllCard.contentType.isNotBlank() && channel.channelViewAllCard.contentType != CarouselViewAllCardViewHolder.CONTENT_DEFAULT) {
                listData.add(
                    CarouselViewAllCardDataModel(
                        channel.channelHeader.applink,
                        channel.channelViewAllCard,
                        this,
                        channel.channelBanner.imageUrl,
                        channel.channelBanner.gradientColor,
                        channel.layout
                    )
                )
            }
            else {
                listData.add(CarouselSeeMorePdpDataModel(channel.channelHeader.applink, channel.channelHeader.backImage, this))
            }
        }
        adapter = MixLeftAdapter(listData,typeFactoryImpl)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(getParallaxEffect())
    }

    private fun setSnapEffect() {
        val snapHelper: SnapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(recyclerView)
    }

    private fun getParallaxEffect(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() == 0 && dx != 0) {
                    val firstView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * 0.2f
                        if (translateX <= 0) {
                            image.translationX = translateX
                            if (distanceFromLeft <= 0) {
                                val itemSize = it.width.toFloat()
                                val alpha = (abs(distanceFromLeft).toFloat() / itemSize * 0.80f)
                                image.alpha = 1 - alpha
                            }
                        } else {
                            image.translationX = 0f
                            image.alpha = 1f
                        }
                    }
                }
            }
        }
    }

    private fun convertDataToProductData(channel: ChannelModel): List<CarouselProductCardDataModel> {
        val list :MutableList<CarouselProductCardDataModel> = mutableListOf()
        for (element in channel.channelGrids) {
            list.add(CarouselProductCardDataModel(
                    ChannelModelMapper.mapToProductCardModel(element, cardInteraction),
                    blankSpaceConfig = BlankSpaceConfig(),
                    grid = element,
                    applink = element.applink,
                    listener = this,
                    componentName = FPM_MIX_LEFT
            ))
        }
        return list
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
        return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

    private fun setHeaderComponent(element: MixLeftDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                mixLeftComponentListener?.onSeeAllBannerClicked(element.channelModel, element.channelModel.channelHeader.applink)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}
