package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.PurchaseInfo
import com.tokopedia.thankyou_native.domain.model.Section
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.DiscountUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading1UiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading2UiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.Heading3UiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.NormalTextUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.ProductUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.SeparatorUiModel
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.ShippingUiModel
import com.tokopedia.unifycomponents.toPx

class PurchaseInfoMapper {
    companion object {
        private const val TYPE_HEADING_1 = "heading_1"
        private const val TYPE_HEADING_2 = "heading_2"
        private const val TYPE_HEADING_3 = "heading_3"
        private const val TYPE_NORMAL_TEXT = "normal_text"
        private const val TYPE_PRODUCT = "product"
        private const val TYPE_GROUP_PRODUCT = "group_product"
        private const val TYPE_DISCOUNT = "discount"
        private const val TYPE_SHIPPING = "shipping"
        private const val TYPE_SEPARATOR = "separator"
        private const val DIVIDER_HEIGHT_8 = 8

        fun createVisitable(purchaseInfo: PurchaseInfo): ArrayList<Visitable<*>> {
            val visitableList = mutableListOf<Visitable<*>>()

            purchaseInfo.summarySection.forEach {
                processSection(visitableList, it)
            }

            visitableList.add(SeparatorUiModel(DIVIDER_HEIGHT_8, false))

            purchaseInfo.orderSection.forEach {
                processSection(visitableList, it)
            }

            return ArrayList(visitableList)
        }

        private fun processSection(visitableList: MutableList<Visitable<*>>, section: Section) {
            when(section.type) {
                TYPE_HEADING_1 -> visitableList.add(Heading1UiModel.create(section))
                TYPE_HEADING_2 -> visitableList.add(Heading2UiModel.create(section))
                TYPE_HEADING_3 -> visitableList.add(Heading3UiModel.create(section))
                TYPE_NORMAL_TEXT -> visitableList.add(NormalTextUiModel.create(section))
                TYPE_PRODUCT -> visitableList.add(ProductUiModel.create(section))
                TYPE_GROUP_PRODUCT -> visitableList.addAll(ProductUiModel.createGroupProduct(section))
                TYPE_DISCOUNT -> visitableList.add(DiscountUiModel.create(section))
                TYPE_SHIPPING -> visitableList.add(ShippingUiModel.create(section))
                TYPE_SEPARATOR -> visitableList.add(SeparatorUiModel())
            }
        }
    }
}
