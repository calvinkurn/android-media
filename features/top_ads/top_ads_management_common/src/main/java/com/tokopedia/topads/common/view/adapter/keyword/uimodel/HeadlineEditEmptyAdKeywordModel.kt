package com.tokopedia.topads.common.view.adapter.keyword.viewmodel

import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeadlineEditEmptyAdKeywordModel(@StringRes val titleText: Int,
                                           @StringRes var subTitleText: Int,
                                           @StringRes var ctaBtnText: Int) : KeywordUiModel
