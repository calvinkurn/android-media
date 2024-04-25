package com.tokopedia.stories.widget.settings.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingConfig
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.stories.widget.settings.presentation.StoriesSettingsEntryPoint
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsPageUiModel
import com.tokopedia.stories.widget.settings.data.usecase.StoriesSettingOptionsUseCase
import com.tokopedia.stories.widget.settings.data.usecase.UpdateStoriesSettingUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/26/24
 */
class StoriesSettingsRepo @Inject constructor(
    private val storiesSettingOptionsUseCase: StoriesSettingOptionsUseCase,
    private val updateStoriesSettingUseCase: UpdateStoriesSettingUseCase,
    private val dispatchers: CoroutineDispatchers,
) : StoriesSettingsRepository {

    private var lastRequestTime: Long = 0L
    private val isAvailable: Boolean
        get() {
            val diff = System.currentTimeMillis() - lastRequestTime
            return diff >= DELAY_MS
        }

    override suspend fun getOptions(entryPoint: StoriesSettingsEntryPoint): StoriesSettingsPageUiModel =
        withContext(dispatchers.io) {
            val response = storiesSettingOptionsUseCase(
                StoriesSettingOptionsUseCase.Param(
                    req = StoriesSettingOptionsUseCase.Param.Author(
                        entryPoint.authorId,
                        entryPoint.authorType
                    )
                )
            )
            return@withContext StoriesSettingsPageUiModel(
                options = response.data.options.map {
                    StoriesSettingOpt(
                        text = it.copy,
                        isSelected = !it.isDisabled,
                        optionType = it.optionType,
                        isDisabled = false,
                    )
                },
                config = StoriesSettingConfig(
                    articleCopy = response.data.config.copy,
                    articleAppLink = response.data.config.appLink,
                    articleWebLink = response.data.config.webLink
                )
            )
        }

    override suspend fun updateOption(
        entryPoint: StoriesSettingsEntryPoint,
        option: StoriesSettingOpt
    ): Boolean =
        withContext(dispatchers.io) {
            return@withContext if (isAvailable) {
                lastRequestTime = System.currentTimeMillis()
                val response =
                    updateStoriesSettingUseCase(
                        UpdateStoriesSettingUseCase.Param(
                            req = UpdateStoriesSettingUseCase.Param.Author(
                                authorId = entryPoint.authorId,
                                authorType = entryPoint.authorType,
                                optionType = option.optionType,
                                isDisabled = option.isSelected
                            )
                        )
                    )
                response.response.success
            } else {
                throw Exception(ERROR_MESSAGE)
            }
        }

    companion object {
        private const val DELAY_MS = 5000L
        private const val ERROR_MESSAGE = "Tunggu 5 detik dulu ya"
    }
}

