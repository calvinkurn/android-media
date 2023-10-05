package com.tokopedia.recharge_component.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.GravitySnapHelper
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.util.loadImageFitCenter
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.viewholders.adapter.MixLeftAdapter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.HomeRechargeBuWidgetMixLeftBinding
import com.tokopedia.recharge_component.listener.RechargeBUWidgetListener
import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.RechargeBUWidgetProductCardModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.recharge_component.presentation.adapter.RechargeBUWidgetProductCardTypeFactoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@SuppressLint("SyntheticAccessor")
class RechargeBUWidgetMixLeftViewHolder(
    itemView: View,
    val listener: RechargeBUWidgetListener
) : AbstractViewHolder<RechargeBUWidgetDataModel>(itemView),
    CoroutineScope,
    CommonProductCardCarouselListener {

    private val binding = HomeRechargeBuWidgetMixLeftBinding.bind(itemView)

    lateinit var dataModel: RechargeBUWidgetDataModel

    private lateinit var adapter: MixLeftAdapter

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var recyclerView: RecyclerView
    private lateinit var image: ImageView
    private lateinit var parallaxBackground: View
    private lateinit var parallaxView: View

    private lateinit var layoutManager: LinearLayoutManager

    private var isCacheData = false

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_mix_left
        const val BU_WIDGET_TYPE_LEFT = "mix-left"
        const val RESET_IMAGE_LAYOUT_VALUE: Int = 0

        private const val EXPIRED_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZZZZ"
        private const val SECOND_IN_MILIS = 1000
        private const val FIRST_INDEX = 0
        private const val MULTIPLIER_DISTANCE_LEFT = 0.2f
        private const val MULTIPLIER_ITEM_SIZE_ALPHA = 0.80f
        private const val SUBTRACTOR_ALPHA = 1
    }

    override fun bind(element: RechargeBUWidgetDataModel) {
        dataModel = element
        if (element.data.items.isNotEmpty()) {
            binding.rechargeBuContentShimmering.root.hide()
            isCacheData = element.isDataCache
            initVar()
            setupBackground(element)
            setupList(element)
            setSnapEffect()
            setHeaderComponent(element)
            setChannelDivider(element)

            if (!isCacheData) {
                itemView.addOnImpressionListener(element) {
                    listener.onRechargeBUWidgetImpression(element)
                }
            }
        } else {
            binding.rechargeBuContentShimmering.root.show()
            val source = element.channel.widgetParam.removePrefix("?section=")
            listener.getRechargeBUWidget(WidgetSource.findSourceByString(source))
        }
    }

    override fun bind(element: RechargeBUWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onProductCardImpressed(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int
    ) {
        if (!isCacheData) {
            // Decrement position to account for empty product card
            listener.onRechargeBUWidgetProductCardImpression(dataModel, position - 1)
        }
    }

    override fun onProductCardClicked(
        channelModel: ChannelModel,
        channelGrid: ChannelGrid,
        position: Int,
        applink: String
    ) {
        // Decrement position to account for empty product card
        listener.onRechargeBUWidgetItemClick(dataModel, position - 1)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        listener.onRechargeBUWidgetClickSeeAllCard(dataModel)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        listener.onRechargeBUWidgetClickBanner(dataModel)
    }

    private fun setChannelDivider(element: RechargeBUWidgetDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channel,
            dividerTop = binding.homeComponentDividerHeader,
            dividerBottom = binding.homeComponentDividerFooter
        )
    }

    private fun initVar() {
        recyclerView = itemView.findViewById(R.id.rv_recharge_bu_product)
        image = itemView.findViewById(R.id.recharge_bu_parallax_image)
        parallaxBackground = itemView.findViewById(R.id.recharge_bu_parallax_background)
        parallaxView = itemView.findViewById(R.id.recharge_bu_parallax_view)
    }

    private fun setupBackground(element: RechargeBUWidgetDataModel) {
        val imageUrl = element.data.mediaUrl
        if (imageUrl.isNotEmpty()) {
            val gradientColor = element.data.option2
            image.addOnImpressionListener(element.channel) {
                if (!isCacheData) {
                    listener.onRechargeBUWidgetBannerImpression(dataModel)
                }
            }
            image.layout(
                RESET_IMAGE_LAYOUT_VALUE,
                RESET_IMAGE_LAYOUT_VALUE,
                RESET_IMAGE_LAYOUT_VALUE,
                RESET_IMAGE_LAYOUT_VALUE
            )
            image.loadImageFitCenter(imageUrl)
            if (gradientColor.isNotEmpty()) {
                parallaxBackground.setGradientBackground(arrayListOf(gradientColor))
            }
        }
    }

    private fun setupList(element: RechargeBUWidgetDataModel) {
        val rechargePerso = element.data
        val channel = element.channel.copy(rechargePerso.title, rechargePerso.title)
        image.alpha = 1f
        recyclerView.resetLayout()
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val typeFactoryImpl = RechargeBUWidgetProductCardTypeFactoryImpl(channel)
        val listData = mutableListOf<Visitable<*>>()
        listData.add(
            CarouselEmptyCardDataModel(
                channel,
                adapterPosition,
                this,
                rechargePerso.applink
            )
        )
        val productDataList = convertDataToProductData(rechargePerso)
        listData.addAll(productDataList)
        if (rechargePerso.textlink.isNotEmpty()) {
            listData.add(CarouselSeeMorePdpDataModel(rechargePerso.applink, listener = this))
        }
        adapter = MixLeftAdapter(listData, typeFactoryImpl)
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
                if (layoutManager.findFirstVisibleItemPosition() == FIRST_INDEX) {
                    val firstView =
                        layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition())
                    firstView?.let {
                        val distanceFromLeft = it.left
                        val translateX = distanceFromLeft * MULTIPLIER_DISTANCE_LEFT
                        parallaxView.translationX = translateX

                        if (distanceFromLeft <= 0) {
                            val itemSize = it.width.toFloat()
                            val alpha = (abs(distanceFromLeft).toFloat() / itemSize * MULTIPLIER_ITEM_SIZE_ALPHA)
                            image.alpha = SUBTRACTOR_ALPHA - alpha
                        }
                    }
                }
            }
        }
    }

    private fun convertDataToProductData(data: RechargePerso): List<RechargeBUWidgetProductCardModel> {
        val list: MutableList<RechargeBUWidgetProductCardModel> = mutableListOf()
        for (element in data.items) {
            list.add(
                RechargeBUWidgetProductCardModel(
                    element.mediaUrl,
                    element.backgroundColor,
                    element.mediaUrlType,
                    element.title.toUpperCase(Locale.getDefault()),
                    data.option2,
                    element.subtitle,
                    element.label2,
                    element.label1,
                    element.label3,
                    element.applink,
                    this,
                    element.soldPercentageValue,
                    element.soldPercentageLabel,
                    element.soldPercentageLabelColor,
                    element.showSoldPercentage
                )
            )
        }
        return list
    }

    private fun RecyclerView.resetLayout() {
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = RecyclerView.LayoutParams.WRAP_CONTENT
        this.layoutParams = carouselLayoutParams
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
        val productCardWidth =
            itemView.context.resources.getDimensionPixelSize(com.tokopedia.productcard.R.dimen.product_card_flashsale_width)
        return productCardModelList.getMaxHeightForGridView(
            itemView.context,
            Dispatchers.Default,
            productCardWidth
        )
    }

    private fun setHeaderComponent(element: RechargeBUWidgetDataModel) {
        val headerView =
            itemView.findViewById<DynamicChannelHeaderView>(R.id.recharge_bu_widget_header_view)
        val currentDate = Calendar.getInstance().time
        val currentTimeInSeconds = currentDate.time / SECOND_IN_MILIS

        val parser = SimpleDateFormat(EXPIRED_DATE_PATTERN)
        val expiredTime = if (element.data.endTime.isNotEmpty()) {
            parser.format(Date(element.data.endTime.toLong() * SECOND_IN_MILIS))
        } else {
            ""
        }

        val channel = element.channel.copy(
            channelHeader = element.channel.channelHeader.copy(
                name = element.data.title,
                applink = if (element.data.textlink.isNotEmpty()) element.data.applink else "",
                subtitle = element.data.subtitle,
                expiredTime = expiredTime,
                serverTimeUnix = currentTimeInSeconds
            ),
            channelConfig = element.channel.channelConfig.copy(
                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(
                    currentTimeInSeconds
                )
            )
        )
        headerView.setChannel(
            channel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    listener.onRechargeBUWidgetClickSeeAllButton(element)
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    binding.rechargeBuWidgetHeaderView.hide()
                    binding.homeRechargeContainer.hide()
                    binding.rechargeBuContentShimmering.root.show()
                    val source = element.channel.widgetParam.removePrefix("?section=")
                    listener.getRechargeBUWidget(WidgetSource.findSourceByString(source))
                }
            }
        )
    }
}
