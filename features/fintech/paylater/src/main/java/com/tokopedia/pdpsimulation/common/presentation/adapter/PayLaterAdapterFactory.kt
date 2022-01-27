package com.tokopedia.pdpsimulation.common.presentation.adapter

import com.tokopedia.pdpsimulation.paylater.domain.model.*

interface PayLaterAdapterFactory {

    fun type(detail: Detail): Int

    fun type(seeMoreOptionsUiModel: SeeMoreOptionsUiModel): Int

    fun type(sectionTitleUiModel: SectionTitleUiModel): Int

    fun type(supervisorUiModel: SupervisorUiModel): Int

    fun type(content: Content): Int
}