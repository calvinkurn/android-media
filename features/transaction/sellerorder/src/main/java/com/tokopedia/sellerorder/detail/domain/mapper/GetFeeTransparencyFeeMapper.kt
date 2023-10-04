package com.tokopedia.sellerorder.detail.domain.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.detail.data.model.SomDetailIncomeDetailResponse
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFee
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFeeAttributes
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeHeaderUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeIconUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeLabelUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSubComponentUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeSummaryUiModel
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeUiModelWrapper
import javax.inject.Inject

class GetFeeTransparencyFeeMapper @Inject constructor() {

    fun map(
        incomeDetail: SomDetailIncomeDetailResponse.GetSomIncomeDetail
    ): TransparencyFeeUiModelWrapper {
        return TransparencyFeeUiModelWrapper(
            bottomSheetTitle = incomeDetail.title.orEmpty(),
            transparencyFeeList = mapTransparencyFeeList(
                sections = incomeDetail.sections,
                summary = incomeDetail.summary
            )
        )
    }

    private fun mapTransparencyFeeList(
        sections: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section?>?,
        summary: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Summary?
    ): List<BaseTransparencyFee> {
        return mutableListOf<BaseTransparencyFee>().apply {
            includeSections(sections)
            includeSummary(summary)
        }.toList()
    }

    private fun MutableList<BaseTransparencyFee>.includeSections(
        sections: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section?>?
    ) {
        sections?.forEach { section ->
            includeSectionHeader(section)
            includeSectionItems(section?.components)
        }
    }

    private fun MutableList<BaseTransparencyFee>.includeSectionHeader(
        section: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section?
    ) {
        add(
            TransparencyFeeHeaderUiModel(
                label = section?.label.orEmpty(),
                value = section?.value.orEmpty(),
                attributes = mapAttributes(section?.attributes)
            )
        )
    }

    private fun mapAttributes(
        attributes: List<SomDetailIncomeDetailResponse.GetSomIncomeDetail.Attribute?>?
    ): List<BaseTransparencyFeeAttributes> {
        return attributes?.filterNotNull()?.map { attribute ->
            when (attribute.data?.type) {
                "SOMIncomeDetailAttributeIconData" -> mapIconAttribute(attribute)
                "SOMIncomeDetailAttributeLabelData" -> mapLabelAttribute(attribute)
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
                "item" -> add(mapSectionItem(index, filteredComponents.size, component))
                "sub-item" -> add(mapSectionSubItem(index, filteredComponents.size, component))
            }
        }
    }

    private fun mapSectionItem(
        componentIndex: Int,
        componentCount: Int,
        component: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Section.Component
    ): TransparencyFeeComponentUiModel {
        return TransparencyFeeComponentUiModel(
            label = component.label.orEmpty(),
            value = component.value.orEmpty(),
            attributes = mapAttributes(component.attributes),
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
            label = component.label.orEmpty(),
            value = component.value.orEmpty(),
            attributes = mapAttributes(component.attributes),
            isFirstIndex = componentIndex == Int.ZERO,
            isLastIndex = componentIndex == componentCount.dec()
        )
    }

    private fun MutableList<BaseTransparencyFee>.includeSummary(
        summary: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Summary?
    ) {
        add(mapSummary(summary))
    }

    private fun mapSummary(
        summary: SomDetailIncomeDetailResponse.GetSomIncomeDetail.Summary?
    ): TransparencyFeeSummaryUiModel {
        return TransparencyFeeSummaryUiModel(
            label = summary?.label.orEmpty(),
            value = summary?.value.orEmpty(),
            attributes = mapAttributes(summary?.attributes),
            note = summary?.note.orEmpty()
        )
    }
}
