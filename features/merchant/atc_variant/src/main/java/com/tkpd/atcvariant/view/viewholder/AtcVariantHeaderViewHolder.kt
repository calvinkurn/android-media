package com.tkpd.atcvariant.view.viewholder

import android.view.View
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.ProductHeaderData
import com.tkpd.atcvariant.data.uidata.VariantHeaderDataModel
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_IMAGE_ONLY
import com.tkpd.atcvariant.util.PAYLOAD_UPDATE_PRICE_ONLY
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheetListener
import com.tkpd.atcvariant.view.widget.AtcVariantNormalPriceWidget
import com.tkpd.atcvariant.view.widget.AtcVariantPromoPriceWidget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.product.detail.common.R as productdetailcommonR

/**
 * Created by Yehezkiel on 07/05/21
 */

class AtcVariantHeaderViewHolder(
    private val view: View,
    private val listener: AtcVariantListener,
    private val atcVarBottomSheetListener: AtcVariantBottomSheetListener
) : AbstractViewHolder<VariantHeaderDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_header_viewholder
    }

    private val productImage = view.findViewById<ImageUnify>(R.id.img_header_main)
    private val productStock = view.findViewById<Typography>(R.id.txt_header_stock)
    private val iconEnlarge = view.findViewById<IconUnify>(R.id.ic_enlarge_img_header)
    private val widgetNormalPrice =
        view.findViewById<AtcVariantNormalPriceWidget>(R.id.widget_header_normal_price)
    private val widgetPromoPrice =
        view.findViewById<AtcVariantPromoPriceWidget>(R.id.widget_header_promo_price)

    private val labelVar1 = view.findViewById<Label>(R.id.lbl_variant_name_1)
    private val labelVar2 = view.findViewById<Label>(R.id.lbl_variant_name_2)
    private val txtTokoCabang = view.findViewById<Typography>(R.id.txt_var_warehouse)

    override fun bind(element: VariantHeaderDataModel) {
        loadImage(element.productImage)
        renderPriceSection(element.headerData, element.cashBackPercentage)
        renderVariantName(element.listOfVariantTitle)
        renderTokoCabang(element.isTokoCabang, element.uspImageUrl)
        renderStock(element.headerData)
    }

    override fun bind(element: VariantHeaderDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isEmpty()) {
            return
        }

        renderVariantName(element.listOfVariantTitle)
        renderTokoCabang(element.isTokoCabang, element.uspImageUrl)
        when (payloads[0] as Int) {
            PAYLOAD_UPDATE_IMAGE_ONLY -> {
                loadImage(element.productImage)
            }

            PAYLOAD_UPDATE_PRICE_ONLY -> {
                renderStock(element.headerData)
                renderPriceSection(element.headerData, element.cashBackPercentage)
            }
        }
    }

    private fun renderPriceSection(
        headerData: ProductHeaderData,
        cashbackPercentage: Int
    ) {
        if (headerData.promoPrice != null) {
            renderPromoPriceWidget(headerData.promoPrice, headerData.productMainPrice)
        } else {
            renderNormalPriceWidget(headerData, cashbackPercentage)
        }
    }

    private fun renderPromoPriceWidget(promoPriceUiModel: PromoPriceUiModel,
                                       originalPriceFmt: String
    ) {
        widgetNormalPrice.hide()
        widgetPromoPrice.run {
            show()
            renderView(
                promoPriceData = promoPriceUiModel,
                originalPriceFmt = originalPriceFmt
            )
        }
    }

    private fun renderNormalPriceWidget(headerData: ProductHeaderData, cashbackPercentage: Int) {
        widgetPromoPrice.hide()
        widgetNormalPrice.run {
            show()
            renderView(
                headerData = headerData,
                cashbackPercentage = cashbackPercentage
            )
        }
    }

    private fun renderTokoCabang(isTokoCabang: Boolean, uspImageUrl: String) = with(view) {
        txtTokoCabang?.run {
            shouldShowWithAction(isTokoCabang) {
                setOnClickListener {
                    atcVarBottomSheetListener.onTokoCabangClicked(uspImageUrl)
                }
            }
        }
    }

    private fun renderVariantName(listName: List<String>) = with(view) {
        labelVar1?.shouldShowWithAction(listName.getOrNull(0)?.isNotEmpty() == true) {
            labelVar1.text = listName.getOrNull(0) ?: ""
        }

        labelVar2?.shouldShowWithAction(listName.getOrNull(1)?.isNotEmpty() == true) {
            labelVar2.text = listName.getOrNull(1) ?: ""
        }
    }

    private fun renderStock(headerData: ProductHeaderData) = with(view) {
        productStock.shouldShowWithAction(
            headerData.productStockFmt.isNotEmpty()
                    && headerData.productStockFmt != "0"
        ) {
            productStock.text = headerData.productStockFmt
        }
    }

    private fun loadImage(imgUrl: String) {
        iconEnlarge.setBackgroundResource(productdetailcommonR.drawable.bg_circle_grey)

        productImage?.run {
            setImageUrl(imgUrl)
            setOnClickListener {
                listener.onVariantImageClicked(imgUrl)
            }
        }
    }
}
