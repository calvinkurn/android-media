package com.tokopedia.pdpsimulation.common.presentation.adapter

import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.SectionTitleUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SeeMoreOptionsUiModel
import com.tokopedia.pdpsimulation.paylater.domain.model.SupervisorUiModel

interface PayLaterAdapterFactory {

    fun type(detail: Detail): Int

    fun type(seeMoreOptionsUiModel: SeeMoreOptionsUiModel): Int

    fun type(sectionTitleUiModel: SectionTitleUiModel): Int

    fun type(supervisorUiModel: SupervisorUiModel): Int

}