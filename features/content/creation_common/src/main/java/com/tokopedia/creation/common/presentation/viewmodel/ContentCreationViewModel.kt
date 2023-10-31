package com.tokopedia.creation.common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.creation.common.domain.ContentCreationConfigUseCase
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationEntryPointSource
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.creation.common.presentation.utils.ContentCreationRemoteConfigManager
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 07/09/23
 */
class ContentCreationViewModel @Inject constructor(
    private val contentCreationConfigUseCase: ContentCreationConfigUseCase,
    private val contentCreationConfigManager: ContentCreationRemoteConfigManager
) : ViewModel() {

    private val _selectedCreationType = MutableStateFlow<ContentCreationItemModel?>(null)
    val selectedCreationType = _selectedCreationType.asStateFlow()

    private val _creationConfig = MutableStateFlow<Result<ContentCreationConfigModel>?>(null)
    val creationConfig = _creationConfig.asStateFlow()

    val authorType: ContentCreationAuthorEnum
        get() = selectedCreationType.value?.let {
            it.authorType
        } ?: creationConfig.value?.let {
            if (it is Success) {
                it.data.creationItems.firstOrNull()?.authorType ?: ContentCreationAuthorEnum.NONE
            } else {
                ContentCreationAuthorEnum.NONE
            }
        } ?: ContentCreationAuthorEnum.NONE

    val selectedItemTitle: String
        get() = selectedCreationType.value?.let {
            it.title
        } ?: ""

    fun selectCreationItem(item: ContentCreationItemModel) {
        _selectedCreationType.value = item
    }

    fun fetchConfig(
        widgetSource: ContentCreationEntryPointSource,
        creationConfig: ContentCreationConfigModel = ContentCreationConfigModel.Empty
    ) {
        viewModelScope.launch {
            try {
                val formattedCreationConfig = if (creationConfig.isActive) {
                    formatCreationConfig(creationConfig, widgetSource)
                } else {
                    val response = contentCreationConfigUseCase(Unit)
                    formatCreationConfig(response, widgetSource)
                }

                _creationConfig.value = Success(formattedCreationConfig)
            } catch (t: Throwable) {
                _creationConfig.value = Fail(t)
            }
        }
    }

    fun getPerformanceDashboardApplink(): String =
        if (_creationConfig.value is Success) {
            (_creationConfig.value as Success).data.statisticApplink
        } else {
            ApplinkConstInternalContent.PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK
        }

    private fun formatCreationConfig(
        creationConfig: ContentCreationConfigModel,
        widgetSource: ContentCreationEntryPointSource
    ): ContentCreationConfigModel =
        creationConfig.copy(
            creationItems = creationConfig.creationItems.mapNotNull { item ->
                if (!isStoryEnabled(widgetSource) && item.type == ContentCreationTypeEnum.STORY) {
                    null
                } else {
                    item
                }
            }
        )

    private fun isStoryEnabled(widgetSource: ContentCreationEntryPointSource): Boolean =
        when (widgetSource) {
            ContentCreationEntryPointSource.Shop -> contentCreationConfigManager.isShowingShopEntryPoint()
            ContentCreationEntryPointSource.Feed -> contentCreationConfigManager.isShowingFeedEntryPoint()
            else -> contentCreationConfigManager.isShowingCreation()
        }
}
