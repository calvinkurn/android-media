package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.data.mapper.StoriesMapperImpl
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.usecase.StoriesDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoriesGroupsUseCase
import com.tokopedia.stories.uimodel.StoryActionType
import com.tokopedia.stories.usecase.ProductMapper
import com.tokopedia.stories.usecase.StoriesProductUseCase
import com.tokopedia.stories.usecase.UpdateStoryUseCase
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storiesGroupsUseCase: StoriesGroupsUseCase,
    private val storiesDetailsUSeCase: StoriesDetailsUseCase,
    private val updateStoryUseCase: UpdateStoryUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val storiesProductUseCase: StoriesProductUseCase,
    private val productMapper: ProductMapper,
    private val userSession: UserSessionInterface,
    private val mapper: StoriesMapperImpl,
) : StoriesRepository {

    override suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesGroupUiModel = withContext(dispatchers.io) {
        val storiesGroupData = storiesGroupsUseCase(data)
        val storiesDetailData = storiesDetailsUSeCase(data)
        return@withContext mapper.mapStoriesInitialData(storiesGroupData, storiesDetailData)
    }

    override suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetailUiModel = withContext(dispatchers.io) {
        val storiesDetailData = storiesDetailsUSeCase(data)
        return@withContext mapper.mapStoriesDetailRequest(storiesDetailData)
    }

    override suspend fun deleteStory(storyId: String) : Boolean = withContext(dispatchers.io) {
        val param = UpdateStoryUseCase.Param(storyId, StoryActionType.Delete)
        val response = updateStoryUseCase(param)
        response.storyId.storyId == storyId
    }

    override suspend fun getStoriesProducts(
        shopId: String,
        storyId: String,
    ): List<ContentTaggedProductUiModel> {
        return withContext(dispatchers.io) {
            val response = storiesProductUseCase(
                storiesProductUseCase.convertToMap(
                    StoriesProductUseCase.Param(
                        id = storyId,
                    )
                )
            )
            productMapper.mapProducts(response.data, shopId)
        }
    }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        shopId: String,
        price: Double
    ): Boolean {
        return withContext(dispatchers.io) {
            val response = addToCartUseCase.apply {
                setParams(
                    AddToCartUseCase.getMinimumParams(
                        productId = productId,
                        shopId = shopId,
                        atcExternalSource = AtcFromExternalSource.ATC_FROM_STORIES,
                        productName = productName,
                        price = price.toString(),
                        userId = userSession.userId,
                    )
                )
            }.executeOnBackground()
            !response.isStatusError()
        }
    }

}
