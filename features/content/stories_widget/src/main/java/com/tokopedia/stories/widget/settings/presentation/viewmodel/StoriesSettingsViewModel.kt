package com.tokopedia.stories.widget.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stories.widget.settings.StoriesSettingsChecker
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepository
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsPageUiModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/22/24
 */
class StoriesSettingsViewModel @Inject constructor(
    private val repository: StoriesSettingsRepository,
    private val storiesChecker: StoriesSettingsChecker
) : ViewModel() {

    private val _pageInfo = MutableStateFlow(StoriesSettingsPageUiModel.Empty)
    val pageInfo: Flow<StoriesSettingsPageUiModel>
        get() = _pageInfo

    private val _event = MutableSharedFlow<StoriesSettingEvent>()

    val uiEvent: Flow<StoriesSettingEvent>
        get() = _event

    fun onEvent(action: StoriesSettingsAction) {
        when (action) {
            StoriesSettingsAction.FetchPageInfo -> getList()
            is StoriesSettingsAction.SelectOption -> updateOption(action.option)
            is StoriesSettingsAction.Navigate -> viewModelScope.launch {
                _event.emit(StoriesSettingEvent.Navigate(action.appLink))
            }

            is StoriesSettingsAction.ShowCoolingDown -> viewModelScope.launch {
                _event.emit(StoriesSettingEvent.ShowErrorToaster(action.throwable, {}))
            }

            else -> {}
        }
    }

    private val onEligibleError = CoroutineExceptionHandler { _, _ ->
        _pageInfo.update { data ->
            data.copy(
                state = ResultState.Success,
                config = data.config.copy(isEligible = false)
            )
        }
    }

    private fun getList() {
        viewModelScope.launchCatchError(block = {
            val eligibleJob = viewModelScope.launch(onEligibleError) {
                val value = storiesChecker.isEligible()
                _pageInfo.update { data -> data.copy(config = data.config.copy(isEligible = value)) }
            }

            eligibleJob.join()

            if (eligibleJob.isCancelled) return@launchCatchError

            viewModelScope.launchCatchError(block = {
                val data = repository.getOptions()
                _pageInfo.update { result ->
                    result.copy(
                        state = ResultState.Success,
                        options = data.options,
                        config = result.config.copy(
                            articleWebLink = data.config.articleWebLink,
                            articleAppLink = data.config.articleAppLink,
                            articleCopy = data.config.articleCopy
                        )
                    )
                }
            }) {
                _pageInfo.update { data -> data.copy(state = ResultState.Fail(it)) }
            }
        }) {}
    }

    private var lastRequestTime: Long = 0L
    private val isAvailableForClick: Boolean
        get() {
            val diff = System.currentTimeMillis() - lastRequestTime
            return diff >= 5000L
        }

    private fun updateOption(option: StoriesSettingOpt) {
        if (!isAvailableForClick) {
            updatePageOption(option, false)
            onEvent(
                StoriesSettingsAction.ShowCoolingDown(
                    MessageErrorException("Tunggu 5 detik dulu ya")
                )
            )
            return
        }

        viewModelScope.launchCatchError(block = {
            val response = repository.updateOption(option)
            if (response) {
                updatePageOption(option.copy(isSelected = !option.isSelected), true)
                lastRequestTime = System.currentTimeMillis()
                _event.emit(StoriesSettingEvent.ClickTrack(option.copy(isSelected = !option.isSelected)))
            } else {
                throw Exception()
            }
        }) {
            updatePageOption(option, false)
            _event.emit(
                StoriesSettingEvent.ShowErrorToaster(it) {
                    updateOption(option)
                }
            )
        }
    }

    private fun updatePageOption(option: StoriesSettingOpt, isSuccess: Boolean) {
        fun updateSwitch(isSwitchOn: Boolean) {
            _pageInfo.update { pageInfo ->
                pageInfo.copy(
                    options = pageInfo.options.map {
                        it.copy(
                            isSelected = isSwitchOn,
                            isUpdateSuccess = isSuccess
                        )
                    }
                )
            }
        }

        fun updateCheckbox(option: StoriesSettingOpt) {
            _pageInfo.update { pageInfo ->
                pageInfo.copy(
                    options = pageInfo.options.map { opt ->
                        when (opt.optionType) {
                            option.optionType -> opt.copy(
                                isSelected = option.isSelected,
                                isUpdateSuccess = isSuccess
                            )

                            "all" -> opt.copy(
                                isSelected = _pageInfo.value.options.any { it.isSelected },
                                isUpdateSuccess = isSuccess
                            )

                            else -> opt.copy(isUpdateSuccess = isSuccess)
                        }
                    }
                )
            }
        }

        if (option.optionType == "all") {
            updateSwitch(option.isSelected)
        } else {
            updateCheckbox(option)
        }
    }
}
