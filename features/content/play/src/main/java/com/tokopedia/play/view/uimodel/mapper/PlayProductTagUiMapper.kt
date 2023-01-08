package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.Product
import com.tokopedia.play.data.Section
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
@PlayScope
class PlayProductTagUiMapper @Inject constructor() {

    fun mapSection(
        input: Section,
        controlTime: Date,
    ) = ProductSectionUiModel.Section(
        productList = input.listOfProducts.map {
            mapProduct(it, ProductSectionType.getSectionValue(sectionType = input.sectionType))
        },
        config = mapConfig(input, controlTime),
        id = input.id,
    )

    private fun mapConfig(
        input: Section,
        controlTime: Date,
    ) = ProductSectionUiModel.Section.ConfigUiModel(
        title = input.sectionTitle,
        type = ProductSectionType.getSectionValue(sectionType = input.sectionType),
        controlTime = controlTime,
        serverTime = input.serverTime.toDate(format = DateUtil.YYYY_MM_DD_T_HH_MM_SS),
        startTime = input.timerStartTime.toDate(format = DateUtil.YYYY_MM_DD_T_HH_MM_SS),
        endTime = input.timerEndTime.toDate(format = DateUtil.YYYY_MM_DD_T_HH_MM_SS),
        timerInfo = input.countdown.countdownInfo,
        background = ProductSectionUiModel.Section.BackgroundUiModel(
            gradients = input.background.gradientList ?: emptyList(),
            imageUrl = input.background.imageUrl
        ),
        reminder = mapReminder(hasReminder = input.id.toLongOrZero() != 0L && (ProductSectionType.getSectionValue(sectionType = input.sectionType) == ProductSectionType.Upcoming))
    )

    private fun mapReminder(hasReminder: Boolean) : PlayUpcomingBellStatus = if(hasReminder) PlayUpcomingBellStatus.Off else PlayUpcomingBellStatus.Unknown

    private fun mapProduct(
        input: Product,
        sectionType: ProductSectionType = ProductSectionType.Unknown,
    ): PlayProductUiModel.Product {
        return PlayProductUiModel.Product(
            id = input.id,
            shopId = input.shopId,
            imageUrl = input.image,
            title = input.name,
            price = if (input.discount != 0L) {
                DiscountedPrice(
                    originalPrice = input.originalPriceFormatted,
                    discountedPrice = input.priceFormatted,
                    discountedPriceNumber = input.price,
                    discountPercent = input.discount
                )
            } else {
                OriginalPrice(
                    price = input.originalPriceFormatted,
                    priceNumber = input.originalPrice
                )
            },
            isVariantAvailable = input.isVariant,
            stock = if (sectionType == ProductSectionType.Upcoming) ComingSoon else
                if (input.quantity > 0 && input.isAvailable) StockAvailable(input.quantity) else OutOfStock,
            minQty = input.minimumQuantity,
            isFreeShipping = input.isFreeShipping,
            applink = input.appLink,
            isTokoNow = input.isTokoNow,
            isPinned = input.isPinned,
            isRilisanSpesial = sectionType == ProductSectionType.Active || sectionType == ProductSectionType.Upcoming,
            buttons = mapButtons(input.buttons),
        )
    }

    private fun mapButtons(buttons: List< Product.ProductButton>) : List<ProductButtonUiModel> {
        return buttons.map { btn ->
                ProductButtonUiModel(text = btn.text, color = ProductButtonColor.getByValue(btn.color), type = ProductButtonType.getByValue(btn.buttonType))
            }
    }
}
