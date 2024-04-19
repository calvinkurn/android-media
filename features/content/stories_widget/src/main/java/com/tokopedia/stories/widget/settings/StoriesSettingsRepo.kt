package com.tokopedia.stories.widget.settings

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/26/24
 */
class StoriesSettingsRepo @Inject constructor(
    private val storiesSettingOptionsUseCase: StoriesSettingOptionsUseCase,
    private val updateStoriesSettingUseCase: UpdateStoriesSettingUseCase,
    private val checkEligibilityUseCase: StoriesEligibilityUseCase,
    private val dispatchers: CoroutineDispatchers,
) : StoriesSettingsRepository {
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
                        isSelected = it.isDisabled,
                        optionType = it.optionType
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
            val response =
                updateStoriesSettingUseCase(
                    UpdateStoriesSettingUseCase.Param(
                        req = UpdateStoriesSettingUseCase.Param.Author(
                            authorId = entryPoint.authorId,
                            authorType = entryPoint.authorType,
                            optionType = option.text,
                            isDisabled = option.isSelected
                        )
                    )
                )
            return@withContext response.response.success
        }

    override suspend fun checkEligibility(entryPoint: StoriesSettingsEntryPoint): Boolean =
        withContext(dispatchers.io) {
            val response = checkEligibilityUseCase(
                StoriesEligibilityUseCase.Param(
                    req = StoriesEligibilityUseCase.Param.Author(
                        authorId = entryPoint.authorId,
                        authorType = entryPoint.authorType
                    )
                )
            )
            return@withContext response.data.isEligibleForAuto || response.data.isEligibleForManual
        }
}

