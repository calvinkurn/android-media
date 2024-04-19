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
    override suspend fun getOptions(entryPoint: StoriesSettingsEntryPoint): List<StoriesSettingOpt> =
        withContext(dispatchers.io) {
            val response = storiesSettingOptionsUseCase(
                StoriesSettingOptionsUseCase.Param(
                    req = StoriesSettingOptionsUseCase.Param.Author(
                        entryPoint.authorId,
                        entryPoint.authorType
                    )
                )
            )
            //TODO(): no need to split its array
            return@withContext response.data.options.optionType.split(",").map {
                StoriesSettingOpt(text = it, isSelected = response.data.options.isDisabled)
            }
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

