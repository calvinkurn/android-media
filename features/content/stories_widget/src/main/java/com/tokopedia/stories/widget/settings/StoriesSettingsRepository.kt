package com.tokopedia.stories.widget.settings

/**
 * @author by astidhiyaa on 3/26/24
 */
interface StoriesSettingsRepository {
    suspend fun getOptions(authorId: String, authorType: String) : List<StoriesSettingOpt> //Todo(): author Type, check type
    suspend fun updateOption(authorId: String, authorType: String, option: StoriesSettingOpt) : Boolean//Todo(): author Type, check type
}
