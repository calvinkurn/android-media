package com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.Section
import com.tokopedia.thankyou_native.presentation.adapter.factory.PurchaseDetailTypeFactory

data class Heading1UiModel(
    val name: String = ""
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): Heading1UiModel {
            return Heading1UiModel(section.name)
        }
    }
}

data class Heading2UiModel(
    val name: String = "",
    val totalPriceStr: String = ""
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): Heading2UiModel {
            return Heading2UiModel(section.name, section.totalPriceStr)
        }
    }
}

data class Heading3UiModel(
    val name: String = "",
    val totalPriceStr: String = "",
    val detail: DetailUiModel = DetailUiModel()
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): Heading3UiModel {
            return Heading3UiModel(
                section.name,
                section.totalPriceStr,
                DetailUiModel(
                    nameSubtitle = section.details.nameSubtitle
                )
            )
        }
    }
}

data class NormalTextUiModel(
    val name: String = "",
    val totalPriceStr: String = "",
    val detail: DetailUiModel = DetailUiModel(),
    val showTooltip: Boolean = false,
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): NormalTextUiModel {
            return NormalTextUiModel(
                section.name,
                section.totalPriceStr,
                DetailUiModel(
                    slashedPrice = section.details.slashedPrice,
                    tooltipSubtitle = section.details.tooltipDescription,
                    tooltipTitle = section.details.tooltipTitle
                ),
                section.details.tooltipTitle.isNotEmpty() || section.details.tooltipDescription.isNotEmpty()
            )
        }
    }
}

data class ProductUiModel(
    val name: String = "",
    val subName: String = "",
    val priceStr: String = "",
    val totalPriceStr: String = "",
    val quantity: Long = 0,
    val iconUrl: String = "",
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): ProductUiModel {
            return ProductUiModel(
                name = section.name,
                totalPriceStr = section.totalPriceStr,
                priceStr = section.priceStr,
                quantity = section.quantity,
                iconUrl = section.iconURL
            )
        }

        fun createGroupProduct(section: Section): List<Visitable<*>> {
            val visitableList = mutableListOf<Visitable<*>>()
            visitableList.add(ProductUiModel(
                name = section.name,
                totalPriceStr = section.totalPriceStr,
                iconUrl = section.iconURL
            ))

            section.details.items.forEach {
                visitableList.add(ProductUiModel(
                    subName = it.name,
                    quantity = it.quantity,
                    priceStr = it.priceStr
                ))
            }
            return visitableList
        }
    }
}

data class DiscountUiModel(
    val name: String = "",
    val totalPriceStr: String = ""
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): DiscountUiModel {
            return DiscountUiModel(section.name, section.totalPriceStr)
        }
    }
}

data class ShippingUiModel(
    val name: String = "",
    val totalPriceStr: String = "",
    val detail: DetailUiModel = DetailUiModel()
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun create(section: Section): ShippingUiModel {
            return ShippingUiModel(
                section.name,
                section.totalPriceStr,
                DetailUiModel(
                    shippingName = section.details.shippingName,
                    shippingEta = section.details.shippingEta
                )
            )
        }
    }
}

data class SeparatorUiModel(
    val height: Int = 1,
    val applyMargin: Boolean = true
): Visitable<PurchaseDetailTypeFactory> {

    override fun type(typeFactory: PurchaseDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class DetailUiModel(
    val nameSubtitle: String = "",
    val tooltipTitle: String = "",
    val tooltipSubtitle: String = "",
    val slashedPrice: String = "",
    val totalPriceSubtitle: String = "",
    val customNotes: String = "",
    val items: List<ItemUiModel> = listOf(),
    val shippingName: String = "",
    val shippingEta: String = ""
)

data class ItemUiModel(
    val name: String = "",
    val priceStr: String = "",
    val totalPriceStr: String = "",
    val quantity: Long = 0
)
