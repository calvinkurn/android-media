package com.tokopedia.creation.common.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.creation.common.domain.ContentCreationConfigUseCase
import com.tokopedia.creation.common.presentation.model.ContentCreationAuthorEnum
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.creation.common.presentation.model.ContentCreationItemModel
import com.tokopedia.creation.common.presentation.model.ContentCreationMediaModel
import com.tokopedia.creation.common.presentation.model.ContentCreationTypeEnum
import com.tokopedia.iconunify.IconUnify
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
    private val contentCreationConfigUseCase: ContentCreationConfigUseCase
) : ViewModel() {

    private val _selectedCreationType = MutableStateFlow<ContentCreationItemModel?>(null)
    val selectedCreationType = _selectedCreationType.asStateFlow()

    private val _creationConfig = MutableStateFlow<Result<ContentCreationConfigModel>?>(null)
    val creationConfig = _creationConfig.asStateFlow()

    fun selectCreationItem(item: ContentCreationItemModel) {
        _selectedCreationType.value = item
    }

    fun fetchConfig() {
        viewModelScope.launch {
            try {
                val response = contentCreationConfigUseCase(Unit)
                _creationConfig.value = Success(response)
            } catch (t: Throwable) {
                _creationConfig.value = Fail(t)
            }
        }
    }

    fun getDummyConfig() {
        _creationConfig.value = Success(
            ContentCreationConfigModel(
                isActive = true,
                statisticApplink = "",
                authors = emptyList(),
                creationItems = listOf(
                    ContentCreationItemModel(
                        isActive = true,
                        type = ContentCreationTypeEnum.SHORT,
                        title = "Buat Video",
                        applink = "tokopedia://content/create_post",
                        media = ContentCreationMediaModel(
                            type = "image",
                            id = "",
                            coverUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                            mediaUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.SHORT_VIDEO,
                        authorType = ContentCreationAuthorEnum.USER
                    ),
                    ContentCreationItemModel(
                        isActive = true,
                        type = ContentCreationTypeEnum.LIVE,
                        title = "Buat Live",
                        applink = "tokopedia://play-broadcaster?author_type=content-shop",
                        media = ContentCreationMediaModel(
                            type = "image",
                            id = "",
                            coverUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                            mediaUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.VIDEO,
                        authorType = ContentCreationAuthorEnum.SHOP
                    ),
                    ContentCreationItemModel(
                        isActive = true,
                        type = ContentCreationTypeEnum.POST,
                        title = "Buat Post",
                        applink = "tokopedia://content/create_post",
                        media = ContentCreationMediaModel(
                            type = "image",
                            id = "",
                            coverUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                            mediaUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.IMAGE,
                        authorType = ContentCreationAuthorEnum.USER
                    ),
                    ContentCreationItemModel(
                        isActive = true,
                        type = ContentCreationTypeEnum.STORY,
                        title = "Buat Story",
                        applink = "tokopedia://content/create_story",
                        media = ContentCreationMediaModel(
                            type = "image",
                            id = "",
                            coverUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                            mediaUrl = "https://img.freepik.com/free-vector/gradient-hexagonal-background_23-2148947773.jpg?w=500",
                        ),
                        descriptionApplink = "tokopedia://any",
                        drawableIconId = IconUnify.PHONE,
                        authorType = ContentCreationAuthorEnum.SHOP
                    ),
                )
            )
        )
    }

    fun getPerformanceDashboardApplink(): String =
        if (_creationConfig.value is Success) (_creationConfig.value as Success).data.statisticApplink
        else ApplinkConstInternalContent.PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK

}
