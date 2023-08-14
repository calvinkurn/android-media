package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopCredibilityDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE
import com.tokopedia.product.detail.databinding.ItemShopCredibilityBinding
import com.tokopedia.product.detail.databinding.ViewCredibilityTickerBinding
import com.tokopedia.product.detail.databinding.ViewShopCredibilityBinding
import com.tokopedia.product.detail.databinding.ViewShopCredibilityShimmeringBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.inflateWithBinding
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stories.common.StoriesAvatarManager
import com.tokopedia.stories.common.StoriesKey
import com.tokopedia.stories.common.storiesManager
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference

/**
 * Created by Yehezkiel on 15/06/20
 */
class ProductShopCredibilityViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener,
) : AbstractViewHolder<ProductShopCredibilityDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_credibility
    }

    private val itemBinding = ItemShopCredibilityBinding.bind(view)
    private var mainBinding: ViewShopCredibilityBinding? = null
    private var shimmeringBinding: ViewShopCredibilityShimmeringBinding? = null
    private var tickerBinding: ViewCredibilityTickerBinding? = null

    private val context = view.context

    private val Fragment.storiesAvatarManager by storiesManager(StoriesKey.ProductDetail)

    init {
        itemBinding.shopCredibilityMain.setOnInflateListener { _, view ->
            mainBinding = ViewShopCredibilityBinding.bind(view)
        }
        itemBinding.shopCredibilityShimmering.setOnInflateListener { _, view ->
            shimmeringBinding = ViewShopCredibilityShimmeringBinding.bind(view)
        }

        itemBinding.shopCredibilityShimmering.inflate()
    }

    override fun bind(element: ProductShopCredibilityDataModel) {
        val shopName = element.shopName
        if (shopName.isEmpty()) return

        shimmeringBinding?.root?.hide()
        if (mainBinding == null) {
            itemBinding.shopCredibilityMain.inflate()
        }

        val binding = mainBinding ?: return
        with(binding) {
            val componentTracker = getComponentTrackData(element)
            setupShopLocation(
                element.shopLocation,
                element.shopName,
                element.shopWarehouseCount,
                element.shopWarehouseApplink,
                componentTracker,
                binding
            )

            setupLastActive(element.shopLastActive, this)
            setupBadgeAndImage(element, this)
            setupCredibilityPartner(parentLabel = element.partnerLabel)
            setupInfoRegion(element, this)
            setupFollow(element.isFavorite, componentTracker, this)

            root.setOnClickListener {
                listener.gotoShopDetail(componentTracker)
            }

            setupTicker(
                tickerType = element.getTickerType(),
                tickerDataResponse = element.tickerDataResponse,
                componentTrackDataModel = componentTracker,
                binding = this
            )

            view.addOnImpressionListener(element.impressHolder) {
                listener.onShopCredibilityImpressed(element.shopWarehouseCount, componentTracker)
            }
        }
    }

    private fun setupShopLocation(
        shopLocation: String,
        shopName: String,
        shopWarehouseCount: String,
        shopWarehouseApplink: String,
        componentTracker: ComponentTrackDataModel,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        val shopLocationBuilder = if (shopWarehouseCount.isNotEmpty()) {
            icShopCredibilityLocation.show()
            icShopCredibilityLocation.setOnClickListener {
                listener.onShopMultilocClicked(componentTracker)
                listener.goToApplink(shopWarehouseApplink)
            }
            shopCredibilityLocation.setOnClickListener {
                listener.onShopMultilocClicked(componentTracker)
                listener.goToApplink(shopWarehouseApplink)
            }
            "$shopLocation $shopWarehouseCount"
        } else {
            shopCredibilityLocation.setOnClickListener { }
            icShopCredibilityLocation.hide()
            shopLocation
        }

        shopCredibilityName.text = HtmlLinkHelper(context, shopName).spannedString
        shopCredibilityLocation.shouldShowWithAction(shopLocationBuilder.isNotEmpty()) {
            shopCredibilityLocation.text = shopLocationBuilder
        }
    }

    private fun setupTicker(
        tickerType: String,
        tickerDataResponse: List<ShopInfo.TickerDataResponse>,
        componentTrackDataModel: ComponentTrackDataModel,
        binding: ViewShopCredibilityBinding
    ) {
        if (tickerDataResponse.isEmpty()) {
            binding.shopCredibilityTickerStub.hide()
            return
        }

        binding.shopCredibilityTickerStub.inflateWithBinding {
            tickerBinding = ViewCredibilityTickerBinding.bind(it)
        }

        if (tickerType == ProductShopCredibilityDataModel.TIPS_TYPE) {
            renderTips(tickerDataResponse, componentTrackDataModel)
        } else {
            renderTicker(tickerDataResponse, componentTrackDataModel)
        }
    }

    private fun renderTips(
        tickerDataResponse: List<ShopInfo.TickerDataResponse>,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        tickerBinding?.shopCredibilityInfoTicker?.hide()
        tickerBinding?.shopCredibilityInfoTips?.run {
            show()
            val data = tickerDataResponse.first()

            titleView.showIfWithBlock(data.title.isNotEmpty()) {
                text = data.title
            }

            descriptionView.showIfWithBlock(data.message.isNotEmpty()) {
                val htmlString = HtmlLinkHelper(context, generateHtml(data.message, data.link))
                text = htmlString.spannedString
                setOnClickListener {
                    listener.onShopTickerClicked(data, componentTrackDataModel)
                }
            }
        }
    }

    private fun renderTicker(
        tickerDataResponse: List<ShopInfo.TickerDataResponse>,
        componentTrackDataModel: ComponentTrackDataModel
    ) {
        tickerBinding?.shopCredibilityInfoTips?.hide()
        tickerBinding?.shopCredibilityInfoTicker?.run {
            show()
            val data = tickerDataResponse.first()

            listener.onShopTickerImpressed(data, componentTrackDataModel)

            setHtmlDescription(generateHtml(data.message, data.link))
            tickerTitle = data.title
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.onShopTickerClicked(data, componentTrackDataModel)
                }

                override fun onDismiss() {
                    // no op
                }

            })
        }
    }

    override fun bind(element: ProductShopCredibilityDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) return
        when (payloads[0] as Int) {
            PAYLOAD_TOOGLE_FAVORITE -> enableButton() //Will only invoke if fail follow shop
            else -> renderFollow(element.isFavorite)
        }
    }

    private fun setupFollow(
        isFavorite: Boolean,
        componentTrackDataModel: ComponentTrackDataModel,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        if (listener.isOwner()) {
            shopCredibilityButtonFollow.hide()
        } else {
            shopCredibilityButtonFollow.show()
        }

        renderFollow(isFavorite)

        shopCredibilityButtonFollow.setOnClickListener {
            shopCredibilityButtonFollow.isClickable = false
            listener.onShopInfoClicked(it.id, componentTrackDataModel)
        }
    }

    private fun renderFollow(isFavorite: Boolean) = mainBinding?.apply {
        if (isFavorite) {
            shopCredibilityButtonFollow.text = getString(R.string.label_favorited)
            shopCredibilityButtonFollow.buttonType = UnifyButton.Type.ALTERNATE
        } else {
            shopCredibilityButtonFollow.text = getString(R.string.label_follow)
            shopCredibilityButtonFollow.buttonType = UnifyButton.Type.MAIN
        }
        shopCredibilityButtonFollow.isClickable = true
    }

    private fun ViewShopCredibilityBinding.setupCredibilityPartner(
        parentLabel: String
    ) {
        shopCredibilityPartner.shouldShowWithAction(parentLabel.isNotBlank()) {
            shopCredibilityPartner.text = parentLabel
        }
    }

    private fun setupLastActive(
        shopLastActiveData: String,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        val context = root.context
        shopCredibilityLastActive.text = HtmlLinkHelper(context, shopLastActiveData).spannedString
        if (shopLastActiveData == context.getString(R.string.shop_online)) {
            shopCredibilityLastActive.setWeight(Typography.BOLD)
            shopCredibilityLastActive.setTextColor(
                context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            )
        } else {
            shopCredibilityLastActive.setType(Typography.DISPLAY_3)
            shopCredibilityLastActive.setTextColor(
                context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
            )
        }
    }

    private fun setupInfoRegion(
        element: ProductShopCredibilityDataModel,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        val infoShopData = element.infoShopData

        val data1 = infoShopData.getOrNull(0)
        val title1 = data1?.value
        if (title1?.isEmpty() == true) {
            shopCredibilityIcon1.hide()
            shopCredibilityInfoTitle1.hide()
            shopCredibilityInfoDesc1.hide()
        } else {
            shopCredibilityInfoTitle1.show()
            shopCredibilityInfoTitle1.text = title1.orEmpty()

            shopCredibilityInfoDesc1.show()
            shopCredibilityInfoDesc1.text = data1?.desc.orEmpty()

            if (data1?.iconIsNotEmpty() == true) {
                shopCredibilityIcon1.show()
                shopCredibilityIcon1.setImage(data1.icon)
            } else shopCredibilityIcon1.hide()
        }

        val data2 = infoShopData.getOrNull(1)
        val title2 = data2?.value
        if (title2?.isEmpty() == true) {
            shopCredibilityIcon2.hide()
            shopCredibilityInfoTitle2.hide()
            shopCredibilityInfoDesc2.hide()
        } else {
            shopCredibilityInfoTitle2.show()
            shopCredibilityInfoTitle2.text = title2

            shopCredibilityInfoDesc2.show()
            shopCredibilityInfoDesc2.text = data2?.desc.orEmpty()

            if (data2?.iconIsNotEmpty() == true) {
                shopCredibilityIcon2.show()
                shopCredibilityIcon2.setImage(data2.icon)
            } else shopCredibilityIcon2.hide()
        }
    }

    private fun setupBadgeAndImage(
        element: ProductShopCredibilityDataModel,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        if (isNewShopBadgeEnabled()) {
            showNewBadge(element.shopTierBadgeUrl, binding)
        } else {
            showOldBadge(element.isOs, element.isPm, binding)
        }

        shopCredibilityAva.loadImageCircle(element.shopAva)
        listener.getStoriesAvatarManager().manage(storiesBorder, element.shopId)
    }

    private fun isNewShopBadgeEnabled() = true

    private fun showNewBadge(
        shopTierBadgeUrl: String,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        shopCredibilityBadge.shouldShowWithAction(shopTierBadgeUrl.isNotEmpty()) {
            shopCredibilityBadge.scaleType = ImageView.ScaleType.FIT_XY
            shopCredibilityBadge.loadImage(shopTierBadgeUrl)
        }
    }

    private fun showOldBadge(
        isOs: Boolean,
        isPm: Boolean,
        binding: ViewShopCredibilityBinding
    ) = with(binding) {
        val drawable = when {
            isOs -> MethodChecker.getDrawable(
                view.context,
                com.tokopedia.gm.common.R.drawable.ic_official_store_product
            )
            isPm -> MethodChecker.getDrawable(
                view.context,
                com.tokopedia.gm.common.R.drawable.ic_power_merchant
            )
            else -> null
        }
        shopCredibilityBadge.shouldShowWithAction(drawable != null) {
            shopCredibilityBadge.loadImage(drawable)
        }
    }

    private fun enableButton() = mainBinding?.apply {
        shopCredibilityButtonFollow.isClickable = true
    }

    private fun generateHtml(message: String, link: String): String = with(itemView) {
        return message.replace("{link}", context.getString(R.string.ticker_href_builder, link))
    }

    private fun getComponentTrackData(
        element: ProductShopCredibilityDataModel?
    ) = ComponentTrackDataModel(
        componentType = element?.type.orEmpty(),
        componentName = element?.name.orEmpty(),
        adapterPosition = bindingAdapterPosition + Int.ONE
    )
}
