package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CampaignModular
import com.tokopedia.product.detail.common.data.model.pdplayout.LabelIcons
import com.tokopedia.product.detail.common.extensions.parseAsHtmlLink
import com.tokopedia.product.detail.data.model.datamodel.ProductContentMainData
import com.tokopedia.product.detail.databinding.ItemProductContentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.campaign.ui.widget.CampaignRibbon
import com.tokopedia.product.detail.view.widget.CenteredImageSpan
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.product.detail.common.R as productdetailcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Yehezkiel on 25/05/20
 */
class PartialContentView(
    private val view: View,
    private val listener: DynamicProductDetailListener
) {

    companion object {
        private val KVI_ICON_HEIGHT = 16.toPx()
    }

    private val context = view.context
    private val binding = ItemProductContentBinding.bind(view)
    private val glideApp by lazyThreadSafetyNone {
        GlideApp.with(context).asDrawable()
    }

    fun renderData(
        data: ProductContentMainData,
        isUpcomingNplType: Boolean,
        freeOngkirImgUrl: String
    ) = with(binding) {
        txtMainPrice.contentDescription =
            context.getString(R.string.content_desc_txt_main_price, data.price.value)
        productName.contentDescription = context.getString(
            R.string.content_desc_product_name,
            MethodChecker.fromHtml(data.productName)
        )

        setProductName(labelIcons = data.labelIcons, data.productName)

        renderFreeOngkir(freeOngkirImgUrl)

        textCashbackGreen.shouldShowWithAction(data.cashbackPercentage > 0) {
            textCashbackGreen.text = context.getString(
                productdetailcommonR.string.template_cashback,
                data.cashbackPercentage.toString()
            )
        }

        when {
            isUpcomingNplType -> {
                if (data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN || data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN) {
                    renderCampaignInactiveNpl(data.price.priceFmt)
                } else {
                    setTextCampaignActive(data.campaign)
                }
            }
            // no campaign
            data.campaign.campaignIdentifier == CampaignRibbon.NO_CAMPAIGN -> {
                renderCampaignInactive(data.price.priceFmt)
            }
            // thematic only
            data.campaign.campaignIdentifier == CampaignRibbon.THEMATIC_CAMPAIGN -> {
                renderCampaignInactive(data.price.priceFmt)
            }
            else -> {
                setTextCampaignActive(data.campaign)
            }
        }

        renderStockAvailable(data.campaign, data.isVariant, data.stockWording, data.isProductActive)
    }

    fun updateWishlist(wishlisted: Boolean, shouldShowWishlist: Boolean) = with(binding.fabDetailPdp) {
        showWithCondition(shouldShowWishlist)
        if (shouldShowWishlist && activeState != wishlisted) {
            activeState = wishlisted
        }
    }

    fun renderFreeOngkir(freeOngkirUrl: String) = with(binding) {
        imgFreeOngkir.shouldShowWithAction(freeOngkirUrl.isNotEmpty()) {
            imgFreeOngkir.loadImageWithoutPlaceholder(freeOngkirUrl)
        }
    }

    fun updateUniversalShareWidget(shouldShow: Boolean) = with(binding.universalShareWidget) {
        if (shouldShow) {
            listener.onUniversalShareWidget(this)
            setColorShareIcon(unifyprinciplesR.color.Unify_NN700)
            show()
        } else {
            hide()
        }
    }

    private fun renderCampaignInactive(price: String) = with(binding) {
        txtMainPrice.text = price
        textSlashPrice.gone()
        textDiscountRed.gone()
    }

    private fun renderCampaignInactiveNpl(price: String) = with(binding) {
        txtMainPrice.text = price
        textSlashPrice.gone()
        textDiscountRed.gone()
    }

    private fun setTextCampaignActive(campaign: CampaignModular) = with(binding) {
        txtMainPrice.run {
            text = campaign.priceFmt
            show()
        }

        textSlashPrice.run {
            text = campaign.slashPriceFmt
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            show()
        }

        textDiscountRed.run {
            text = campaign.discPercentageFmt
            show()
        }
        hideGimmick(campaign)
    }

    //region product name text & label icons
    private fun setProductName(labelIcons: List<LabelIcons>, title: String) {
        if (labelIcons.isEmpty()) {
            setProductNameText(title = title)
        } else {
            setProductNameWithIcon(labelIcons = labelIcons, title = title)
        }
    }

    private fun setProductNameText(title: String) {
        binding.productName.text = title.parseAsHtmlLink(context, false)
    }

    private fun setProductNameWithIcon(labelIcons: List<LabelIcons>, title: String) {
        val textView = binding.productName
        val stringBuilder = SpannableStringBuilder().apply {
            append(title.parseAsHtmlLink(context, false))
        }
        textView.text = stringBuilder

        processLabelIcons(stringBuilder = stringBuilder, labelIcons = labelIcons)
    }

    private fun processLabelIcons(
        stringBuilder: SpannableStringBuilder,
        labelIcons: List<LabelIcons>
    ) {
        val mapIcons: MutableMap<String, Drawable?> = labelIcons
            .associate { it.iconURL to null }
            .toMutableMap()

        prepareLoadIcons(mapIcons = mapIcons, stringBuilder)
    }

    private fun prepareLoadIcons(
        mapIcons: MutableMap<String, Drawable?>,
        stringBuilder: SpannableStringBuilder
    ) {
        mapIcons.keys.forEach { url ->
            loadIcon(url = url) {
                mapIcons[url] = this
                renderLabelIcons(stringBuilder = stringBuilder, mapIcons = mapIcons)
            }
        }
    }

    private fun renderLabelIcons(
        stringBuilder: SpannableStringBuilder,
        mapIcons: MutableMap<String, Drawable?>
    ) {
        // Exit if all icons have not finished load
        if (!allIconsLoaded(mapIcons)) return

        val textView = binding.productName
        val textViewLineSpacing = textView.lineSpacingExtra
        mapIcons.getIcons().forEach { resource ->
            resource.resizeLabelIconSpec()
            stringBuilder.setLabelIconSpan(drawable = resource, lineSpacing = textViewLineSpacing)
            textView.text = stringBuilder
        }
    }

    private fun MutableMap<String, Drawable?>.getIcons() = values
        // reversed this, because each logo put from index 0 always
        .reversed()
        .asSequence()
        .filterNotNull()

    private fun allIconsLoaded(mapIcons: MutableMap<String, Drawable?>): Boolean {
        // ensure all icons has loaded
        return mapIcons.values.all { it != null }
    }

    private fun Drawable.resizeLabelIconSpec() {
        val ratio = intrinsicWidth.toFloat() / intrinsicHeight.toFloat()
        val bottom = if (intrinsicHeight > KVI_ICON_HEIGHT) KVI_ICON_HEIGHT else intrinsicHeight
        val right = bottom * ratio
        setBounds(Int.ZERO, Int.ZERO, right.toInt(), bottom)
    }

    private fun SpannableStringBuilder.setLabelIconSpan(drawable: Drawable, lineSpacing: Float) {
        val imageSpan = CenteredImageSpan(drawable, lineSpacing)

        insert(Int.ZERO, "  ")
        setSpan(imageSpan, Int.ZERO, Int.ONE, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private inline fun loadIcon(url: String, crossinline onLoaded: Drawable?.() -> Unit) {
        glideApp.load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    onLoaded(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    onLoaded(null)
                }
            })
    }
    // endregion product name & label icons

    private fun renderStockAvailable(
        campaign: CampaignModular,
        isVariant: Boolean,
        stockWording: String,
        isProductActive: Boolean
    ) = with(binding) {
        textStockAvailable.text = MethodChecker.fromHtml(stockWording)
        textStockAvailable.showWithCondition(!campaign.activeAndHasId && !isVariant && stockWording.isNotEmpty() && isProductActive)
    }

    private fun hideGimmick(campaign: CampaignModular) = with(binding) {
        if (campaign.hideGimmick) {
            textSlashPrice.visibility = View.GONE
            textDiscountRed.visibility = View.GONE
        } else {
            textSlashPrice.visibility = View.VISIBLE
            textDiscountRed.visibility = View.VISIBLE
        }
    }

    fun onViewRecycled() {
    }
}
