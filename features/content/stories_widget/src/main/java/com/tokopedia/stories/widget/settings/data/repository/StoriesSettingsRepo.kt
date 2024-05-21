package com.tokopedia.stories.widget.settings.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingConfig
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.stories.widget.settings.presentation.StoriesSettingsEntryPoint
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsPageUiModel
import com.tokopedia.stories.widget.settings.data.usecase.StoriesSettingOptionsUseCase
import com.tokopedia.stories.widget.settings.data.usecase.UpdateStoriesSettingUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/26/24
 */
class StoriesSettingsRepo @Inject constructor(
    private val storiesSettingOptionsUseCase: StoriesSettingOptionsUseCase,
    private val updateStoriesSettingUseCase: UpdateStoriesSettingUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val userSessionInterface: UserSessionInterface
) : StoriesSettingsRepository {

    private val entryPoint: StoriesSettingsEntryPoint
        get() = StoriesSettingsEntryPoint(
            authorType = "shop",
            authorId = userSessionInterface.shopId
        )

    override suspend fun getOptions(): StoriesSettingsPageUiModel =
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
                    )
                },
                config = StoriesSettingConfig(
                    articleCopy = response.data.config.copy,
                    articleAppLink = response.data.config.appLink,
                    articleWebLink = response.data.config.webLink,
                    isEligible = false,
                ),
                state = ResultState.Success
            )
        }

    override suspend fun updateOption(
        option: StoriesSettingOpt
    ): Boolean =
        withContext(dispatchers.io) {
            val response = updateStoriesSettingUseCase(
                UpdateStoriesSettingUseCase.Param(
                    req = UpdateStoriesSettingUseCase.Param.Author(
                        authorId = entryPoint.authorId,
                        authorType = entryPoint.authorType,
                        optionType = option.optionType,
                        isDisabled = option.isSelected
                    )
                )
            )

            return@withContext response.response.success
        }
}

