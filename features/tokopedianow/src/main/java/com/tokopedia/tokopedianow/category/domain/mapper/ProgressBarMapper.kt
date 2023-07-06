package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel

object ProgressBarMapper {
    fun createProgressBar() = TokoNowProgressBarUiModel(CategoryLayoutType.MORE_PROGRESS_BAR.name)
}
