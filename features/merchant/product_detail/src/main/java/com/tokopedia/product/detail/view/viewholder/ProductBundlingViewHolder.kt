package com.tokopedia.product.detail.view.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewStub
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference

class ProductBundlingViewHolder(
    private val view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductBundlingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_bundling

        private const val BUNDLE_TYPE_SINGLE = "SINGLE"
        private const val BUNDLE_TYPE_MULTIPLE = "MULTIPLE"
        private const val BUNDLE_ITEM_MINIMUM_COUNT_SINGLE = 1
        private const val BUNDLE_ITEM_MINIMUM_COUNT_MULTIPLE = 2
    }

    private var componentTrackDataModel: ComponentTrackDataModel? = null

    private val bundleViewDelegate = lazy { BundleView(view) }
    private val bundleComponent: BundleView by bundleViewDelegate

    private val multiBundleDelegate = lazy { MultiBundle(view) }
    private val multiBundle: MultiBundle by multiBundleDelegate

    private val singleBundleDelegate = lazy { SingleBundle(view) }
    private val singleBundle: SingleBundle by singleBundleDelegate

    override fun bind(element: ProductBundlingDataModel) {

        val bundle = element.bundleInfo ?: return hideComponent()

        val bundleItems = bundle.bundleItems
        val bundleType = bundle.type
        if (!checkBundleItems(bundleType, bundleItems)) return hideComponent()

        componentTrackDataModel = getComponentTrackData(element)

        val bundleId = bundle.bundleId
        bundleComponent.process(bundle) {
            listener.onClickCheckBundling(bundleId, bundleType, componentTrackDataModel)
        }

        when (bundleType) {
            BUNDLE_TYPE_SINGLE -> showSingleBundle(bundleItems.first())
            BUNDLE_TYPE_MULTIPLE -> showMultiBundle(bundle, bundleId)
        }

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressionProductBundling(bundleId, bundleType, componentTrackDataModel)
        }
    }

    private fun showSingleBundle(item: BundleInfo.BundleItem) {
        if (multiBundleDelegate.isInitialized()) multiBundle.hide()
        singleBundle.process(item)
    }

    private fun showMultiBundle(bundle: BundleInfo, bundleId: String) {
        if (singleBundleDelegate.isInitialized()) singleBundle.hide()
        multiBundle.process(bundle) { productId ->
            listener.onClickProductInBundling(bundleId, productId, componentTrackDataModel)
        }
    }

    private fun checkBundleItems(
        bundleType: String,
        bundleItems: List<BundleInfo.BundleItem>
    ): Boolean {

        val minimumItemCount = when (bundleType) {
            BUNDLE_TYPE_SINGLE -> BUNDLE_ITEM_MINIMUM_COUNT_SINGLE
            BUNDLE_TYPE_MULTIPLE -> BUNDLE_ITEM_MINIMUM_COUNT_MULTIPLE
            else -> return false
        }

        if (bundleItems.size < minimumItemCount) return false

        return true
    }

    private fun hideComponent() {
        if (bundleViewDelegate.isInitialized()) {
            bundleComponent.hide()
        }
    }

    private fun getComponentTrackData(element: ProductBundlingDataModel) = ComponentTrackDataModel(
        componentType = element.type(),
        componentName = element.name(),
        adapterPosition = adapterPosition + 1
    )

    class MultiBundle(parent: View) {

        private val viewStub: ViewStub = parent.findViewById(R.id.product_bundling_stub_multi)
        private val view: View = viewStub.inflate()

        private val weakContext: WeakReference<Context> = WeakReference(view.context)

        private val image1: ImageUnify = view.findViewById(R.id.product_bundling_image_1)
        private val image2: ImageUnify = view.findViewById(R.id.product_bundling_image_2)
        private val image3: ImageUnify = view.findViewById(R.id.product_bundling_image_3)
        private val price1: Typography = view.findViewById(R.id.product_bundling_price_1)
        private val price2: Typography = view.findViewById(R.id.product_bundling_price_2)
        private val price3: Typography = view.findViewById(R.id.product_bundling_price_3)
        private val discount1: Label = view.findViewById(R.id.product_bundling_discount_1)
        private val discount2: Label = view.findViewById(R.id.product_bundling_discount_2)
        private val discount3: Label = view.findViewById(R.id.product_bundling_discount_3)
        private val slash1: Typography = view.findViewById(R.id.product_bundling_slash_1)
        private val slash2: Typography = view.findViewById(R.id.product_bundling_slash_2)
        private val slash3: Typography = view.findViewById(R.id.product_bundling_slash_3)
        private val group1: Group = view.findViewById(R.id.product_bundling_group_1)
        private val group2: Group = view.findViewById(R.id.product_bundling_group_2)
        private val group3: Group = view.findViewById(R.id.product_bundling_group_3)

        private val quantity: Typography = parent.findViewById(R.id.product_bundling_total_quantity)

        private val images = listOf(image1, image2, image3)
        private val prices = listOf(price1, price2, price3)
        private val discounts = listOf(discount1, discount2, discount3)
        private val slashes = listOf(slash1, slash2, slash3)
        private val groups = listOf(group1, group2, group3)

        fun process(
            bundle: BundleInfo,
            setOnClickItem: (productId: String) -> Unit
        ) {
            view.show()

            val items = bundle.bundleItems
            val unusedGroups = groups.toMutableList()

            items.forEachIndexed { index, item ->
                val viewImage = images[index]
                val viewPrice = prices[index]
                val viewDiscount = discounts[index]
                val viewSlash = slashes[index]

                groups[index].apply {
                    unusedGroups -= this
                    show()
                }

                viewImage.urlSrc = item.picURL

                val discount = item.discountPercentage
                if (discount.isBlank()) {
                    viewSlash.hide()
                    viewDiscount.hide()
                    viewPrice.text = item.originalPrice
                } else {
                    viewDiscount.show()
                    viewSlash.show()
                    viewPrice.text = item.bundlePrice
                    viewDiscount.text = discount
                    viewSlash.apply {
                        text = item.originalPrice
                        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    }
                }

                val itemProductId = item.productId
                if (itemProductId != bundle.productId) {
                    setItemClickListener(listOf(viewImage, viewPrice, viewDiscount, viewSlash)) {
                        setOnClickItem(itemProductId)
                    }
                }
            }

            unusedGroups.forEach { group ->
                group.invisible()
            }

            val quantityText = weakContext.get()?.getString(
                R.string.pdp_bundling_quantity,
                items.size.toString()
            ) ?: items.size.toString()
            quantity.text = quantityText
            quantity.show()
        }

        fun hide() {
            view.hide()
            quantity.hide()
        }

        private fun setItemClickListener(views: List<View>, onClick: () -> Unit) {
            views.forEach { it.setOnClickListener { onClick() } }
        }
    }

    class SingleBundle(parent: View) {
        private val viewStub: ViewStub = parent.findViewById(R.id.product_bundling_stub_single)
        private val view: View = viewStub.inflate()

        private val image: ImageUnify = view.findViewById(R.id.product_bundling_single_image)
        private val label: Label = view.findViewById(R.id.product_bundling_single_label)
        private val name: Typography = view.findViewById(R.id.product_bundling_single_name)
        private val price: Typography = view.findViewById(R.id.product_bundling_single_price)
        private val discount: Label = view.findViewById(R.id.product_bundling_single_discount)
        private val slash: Typography = view.findViewById(R.id.product_bundling_single_slash)

        fun process(item: BundleInfo.BundleItem) {
            view.show()

            image.urlSrc = item.picURL
            label.text = item.quantity
            name.text = item.name
            price.text = item.bundlePrice
            discount.text = item.discountPercentage
            slash.apply {
                text = item.originalPrice
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        fun hide() = view.hide()
    }

    class BundleView(parent: View) {
        private val viewStub: ViewStub = parent.findViewById(R.id.product_bundling_stub_component)
        private val view: View = viewStub.inflate()

        private val title: Typography = view.findViewById(R.id.product_bundling_title)
        private val info: Typography = view.findViewById(R.id.product_bundling_info)
        private val slash: Typography = view.findViewById(R.id.product_bundling_total_slash)
        private val saving: Typography = view.findViewById(R.id.product_bundling_total_saving)
        private val price: Typography = view.findViewById(R.id.product_bundling_total_price)
        private val buttonCheck: UnifyButton = view.findViewById(R.id.product_bundling_button_check)

        fun process(bundle: BundleInfo, onClickCheck: () -> Unit) {
            view.show()

            title.text = bundle.titleComponent
            info.text = bundle.preorderString
            slash.apply {
                text = bundle.originalPriceBundling
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            saving.text = bundle.savingPriceBundling
            price.text = bundle.finalPriceBundling
            buttonCheck.setOnClickListener { onClickCheck() }
        }

        fun hide() = view.hide()
    }

}