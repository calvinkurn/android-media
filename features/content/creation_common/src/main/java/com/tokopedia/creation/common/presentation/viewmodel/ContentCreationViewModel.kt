package com.tokopedia.creation.common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.creation.common.analytics.ContentCreationAnalytics
import com.tokopedia.creation.common.domain.ContentCreationConfigUseCase
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
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
    private val analytics: ContentCreationAnalytics
) : ViewModel() {

    private val _selectedCreationType = MutableStateFlow<ContentCreationItemModel?>(null)
    val selectedCreationType = _selectedCreationType.asStateFlow()

    private val _creationConfig = MutableStateFlow<Result<ContentCreationConfigModel>?>(null)
    val creationConfig = _creationConfig.asStateFlow()

    private val _isFirstOpen = MutableStateFlow(true)

    fun selectCreationItem(item: ContentCreationItemModel) {
        _selectedCreationType.value = item
    }

    fun fetchConfig(creationConfig: ContentCreationConfigModel = ContentCreationConfigModel.Empty) {
        viewModelScope.launch {
            try {
                if (creationConfig.isActive) {
                    _creationConfig.value = Success(creationConfig)
                    return@launch
                }

                val response = contentCreationConfigUseCase(Unit)
                _creationConfig.value = Success(response)
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

    fun sendClickNextAnalytic() {
        selectedCreationType.value?.let {
            analytics.eventClickNextButton(
                it.authorType,
                it.title
            )
        }
    }

    fun sendClickPerformanceDashboardAnalytic() {
        creationConfig.value?.let {
            if (it is Success && _isFirstOpen.value) {
                it.data.creationItems.firstOrNull()?.let { item ->
                    analytics.eventClickPerformanceDashboard(item.authorType)
                    _isFirstOpen.value = false
                }
            }
        }
    }

    fun sendImpressionCreationBottomSheetAnalytic() {
        creationConfig.value?.let {
            if (it is Success) {
                it.data.creationItems.firstOrNull()?.let { item ->
                    analytics.eventImpressionContentCreationBottomSheet(item.authorType)
                }
            }
        }
    }

    fun sendImpressionContentCreationWidgetAnalytic() {
        creationConfig.value?.let {
            if (it is Success) {
                it.data.creationItems.firstOrNull()?.let { item ->
                    analytics.eventImpressionContentCreationEndpointWidget(item.authorType)
                }
            }
        }
    }

    fun sendClickContentCreationWidgetAnalytic() {
        creationConfig.value?.let {
            if (it is Success) {
                it.data.creationItems.firstOrNull()?.let { item ->
                    analytics.clickContentCreationEndpointWidget(item.authorType)
                }
            }
        }
    }

    fun onDismissBottomSheet() {
        _isFirstOpen.value = true
    }
}
