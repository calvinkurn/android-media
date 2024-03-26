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
    private val dispatchers: CoroutineDispatchers,
) : StoriesSettingsRepository {
    override suspend fun getOptions(authorId: String, authorType: String): List<StoriesSettingOpt> =
        withContext(dispatchers.io) {
            val response = storiesSettingOptionsUseCase(
                StoriesSettingOptionsUseCase.Param(
                    req = StoriesSettingOptionsUseCase.Param.Author(
                        authorId,
                        authorType
                    )
                )
            )
            return@withContext response.data.options.optionType.split(",").map {
                StoriesSettingOpt(text = it, isSelected = response.data.options.isDisabled)
            }
        }

    override suspend fun updateOption(
        authorId: String,
        authorType: String,
        option: StoriesSettingOpt
    ): Boolean =
        withContext(dispatchers.io) {
            val response =
                updateStoriesSettingUseCase(
                    UpdateStoriesSettingUseCase.Param(
                        req = UpdateStoriesSettingUseCase.Param.Author(
                            authorId = authorId,
                            authorType = authorType,
                            optionType = option.text,
                            isDisabled = option.isSelected
                        )
                    )
                )
            return@withContext response.response.success
        }
}
