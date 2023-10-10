package com.tokopedia.sellerorder.detail.domain.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.detail.data.model.SomDetailIncomeDetailResponse
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFee
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeUiModelWrapper
import javax.inject.Inject

class GetFeeTransparencyFeeMapper @Inject constructor() {

    companion object {
        private const val LABEL_WORD_DIVIDER = " "
        private const val ATTRIBUTE_TYPE_ICON = "SOMIncomeDetailAttributeIconData"
        private const val ATTRIBUTE_TYPE_LABEL = "SOMIncomeDetailAttributeLabelData"
        private const val SECTION_TYPE_ITEM = "item"
        private const val SECTION_TYPE_SUB_ITEM = "sub-item"
    }

    fun map(
        incomeDetail: SomDetailIncomeDetailResponse.GetSomIncomeDetail
    ): TransparencyFeeUiModelWrapper {
        return TransparencyFeeUiModelWrapper(
            bottomSheetTitle = incomeDetail.title.orEmpty(),
            transparencyFeeList = mapTransparencyFeeList(
                sections = incomeDetail.sections
            ),
            summary = mapSummary(incomeDetail.summary)
        )
    }

    private fun mapTransparencyFeeList(
        sections: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section?>?
    ): List<BaseTransparencyFee> {
        return mutableListOf<BaseTransparencyFee>().apply {
            includeSections(sections)
        }.toList()
    }

    private fun MutableList<BaseTransparencyFee>.includeSections(
        sections: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section?>?
    ) {
        sections?.forEachIndexed { index, section ->
            includeSectionHeader(
                section = section,
                isFirstHeader = index == Int.ZERO,
                hasComponents = !section?.components.isNullOrEmpty()
            )
            includeSectionItems(section?.components)
        }
    }

    private fun MutableList<BaseTransparencyFee>.includeSectionHeader(
        section: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section?,
        isFirstHeader: Boolean,
        hasComponents: Boolean
    ) {
        add(
            TransparencyFeeHeaderUiModel(
                value = section?.value.orEmpty(),
                attributes = mapHeaderAttributes(
                    headerLabel = "${section?.label.orEmpty()} ${section?.subLabel.orEmpty()}",
                    attributes = section?.attributes
                ),
                isFirstHeader = isFirstHeader,
                hasComponents = hasComponents
            )
        )
    }

    private fun mapHeaderAttributes(
        headerLabel: String?,
        attributes: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute?>?
    ): List<BaseTransparencyFeeAttributes> {
        return mutableListOf<BaseTransparencyFeeAttributes>().apply {
            includeHeaderLabelAttribute(headerLabel)
            addAll(mapAttributes(attributes))
        }
    }

    private fun MutableList<BaseTransparencyFeeAttributes>.includeHeaderLabelAttribute(
        text: String?
    ) {
        if (text == null) return
        text.split(LABEL_WORD_DIVIDER).forEach { word ->
            add(TransparencyFeeHeaderLabelUiModel(word))
            add(TransparencyFeeHeaderLabelUiModel(LABEL_WORD_DIVIDER))
        }
    }

    private fun mapComponentAttributes(
        componentLabel: String?,
        attributes: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute?>?
    ): List<BaseTransparencyFeeAttributes> {
        return mutableListOf<BaseTransparencyFeeAttributes>().apply {
            includeComponentLabelAttribute(componentLabel)
            addAll(mapAttributes(attributes))
        }
    }

    private fun MutableList<BaseTransparencyFeeAttributes>.includeComponentLabelAttribute(
        text: String?
    ) {
        if (text == null) return
        text.split(LABEL_WORD_DIVIDER).forEach { word ->
            add(TransparencyFeeComponentLabelUiModel(word))
            add(TransparencyFeeComponentLabelUiModel(LABEL_WORD_DIVIDER))
        }
    }

    private fun mapSubComponentAttributes(
        subComponentLabel: String?,
        attributes: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute?>?
    ): List<BaseTransparencyFeeAttributes> {
        return mutableListOf<BaseTransparencyFeeAttributes>().apply {
            includeSubComponentLabelAttribute(subComponentLabel)
            addAll(mapAttributes(attributes))
        }
    }

    private fun MutableList<BaseTransparencyFeeAttributes>.includeSubComponentLabelAttribute(
        text: String?
    ) {
        if (text == null) return
        text.split(LABEL_WORD_DIVIDER).forEach { word ->
            add(TransparencyFeeSubComponentLabelUiModel(word))
            add(TransparencyFeeSubComponentLabelUiModel(LABEL_WORD_DIVIDER))
        }
    }

    private fun mapAttributes(
        attributes: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute?>?
    ): List<BaseTransparencyFeeAttributes> {
        return attributes?.filterNotNull()?.map { attribute ->
            when (attribute.data?.type) {
                ATTRIBUTE_TYPE_ICON -> mapIconAttribute(attribute)
                ATTRIBUTE_TYPE_LABEL -> mapLabelAttribute(attribute)
                else -> null
            }
        }.orEmpty().filterNotNull()
    }

    private fun mapIconAttribute(
        attribute: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute
    ): TransparencyFeeIconUiModel {
        return TransparencyFeeIconUiModel(
            iconUrl = attribute.data?.iconUrl.orEmpty(),
            darkIconUrl = attribute.data?.iconUrlDark.orEmpty(),
            transparencyFeeInfo = mapIconAttributeInfo(attribute.tooltip)
        )
    }

    private fun mapIconAttributeInfo(
        tooltip: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute.Tooltip?
    ): TransparencyFeeIconUiModel.TransparencyFeeInfo {
        return TransparencyFeeIconUiModel.TransparencyFeeInfo(
            title = tooltip?.title.orEmpty(),
            desc = tooltip?.value.orEmpty()
        )
    }

    private fun mapLabelAttribute(
        attribute: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute
    ): TransparencyFeeLabelUiModel {
        return TransparencyFeeLabelUiModel(
            label = attribute.data?.label.orEmpty(),
            labelType = attribute.data?.level.orEmpty()
        )
    }

    private fun MutableList<BaseTransparencyFee>.includeSectionItems(
        components: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section.Component?>?
    ) {
        val filteredComponents = components?.filterNotNull().orEmpty()
        filteredComponents.forEachIndexed { index, component ->
            when (component.type) {
                SECTION_TYPE_ITEM -> add(mapSectionItem(index, filteredComponents.size, component))
                SECTION_TYPE_SUB_ITEM -> add(mapSectionSubItem(index, filteredComponents.size, component))
            }
        }
    }

    private fun mapSectionItem(
        componentIndex: Int,
        componentCount: Int,
        component: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section.Component
    ): TransparencyFeeComponentUiModel {
        return TransparencyFeeComponentUiModel(
            value = component.value.orEmpty(),
            attributes = mapComponentAttributes(
                componentLabel = "${component.label.orEmpty()} ${component.subLabel.orEmpty()}",
                attributes = component.attributes
            ),
            isFirstIndex = componentIndex == Int.ZERO,
            isLastIndex = componentIndex == componentCount.dec()
        )
    }

    private fun mapSectionSubItem(
        componentIndex: Int,
        componentCount: Int,
        component: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section.Component
    ): TransparencyFeeSubComponentUiModel {
        return TransparencyFeeSubComponentUiModel(
            value = component.value.orEmpty(),
            attributes = mapSubComponentAttributes(
                subComponentLabel = "${component.label.orEmpty()} ${component.subLabel.orEmpty()}",
                attributes = component.attributes
            ),
            isFirstIndex = componentIndex == Int.ZERO,
            isLastIndex = componentIndex == componentCount.dec()
        )
    }

    private fun mapSummary(
        summary: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Summary?
    ): TransparencyFeeSummaryUiModel {
        return TransparencyFeeSummaryUiModel(
            value = summary?.value.orEmpty(),
            attributes = mapSummaryAttributes(
                summaryLabel = "${summary?.label.orEmpty()} ${summary?.subLabel.orEmpty()}",
                attributes = summary?.attributes
            ),
            note = summary?.note.orEmpty(),
            state = summary?.state.orEmpty()
        )
    }

    private fun mapSummaryAttributes(
        summaryLabel: String?,
        attributes: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute?>?
    ): List<BaseTransparencyFeeAttributes> {
        return mutableListOf<BaseTransparencyFeeAttributes>().apply {
            includeSummaryLabelAttribute(summaryLabel)
            addAll(mapAttributes(attributes))
        }
    }

    private fun MutableList<BaseTransparencyFeeAttributes>.includeSummaryLabelAttribute(
        text: String?
    ) {
        if (text == null) return
        text.split(LABEL_WORD_DIVIDER).forEach { word ->
            add(TransparencyFeeSummaryLabelUiModel(word))
            add(TransparencyFeeSummaryLabelUiModel(LABEL_WORD_DIVIDER))
        }
    }
}
