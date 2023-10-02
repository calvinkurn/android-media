package com.tokopedia.stories.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.stories.data.mapper.StoriesMapperImpl
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.domain.usecase.StoriesDetailsUseCase
import com.tokopedia.stories.domain.usecase.StoriesGroupsUseCase
import com.tokopedia.stories.domain.usecase.StoriesTrackActivityUseCase
import com.tokopedia.stories.uimodel.StoryActionType
import com.tokopedia.stories.usecase.ProductMapper
import com.tokopedia.stories.usecase.StoriesProductUseCase
import com.tokopedia.stories.usecase.UpdateStoryUseCase
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.state.ProductBottomSheetUiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val storiesGroupsUseCase: StoriesGroupsUseCase,
    private val storiesDetailsUseCase: StoriesDetailsUseCase,
    private val storiesTrackActivityUseCase: StoriesTrackActivityUseCase,
    private val updateStoryUseCase: UpdateStoryUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val storiesProductUseCase: StoriesProductUseCase,
    private val productMapper: ProductMapper,
    private val userSession: UserSessionInterface,
    private val mapper: StoriesMapperImpl,
) : StoriesRepository {

    override suspend fun getStoriesInitialData(data: StoriesRequestModel): StoriesUiModel =
        withContext(dispatchers.io) {
            val groupRequest = async { storiesGroupsUseCase(data) }
            val detailRequest = async { storiesDetailsUseCase(data) }
            val groupResult = groupRequest.await()
            val detailResult = detailRequest.await()
            return@withContext mapper.mapStoriesInitialData(groupResult, detailResult)
        }

    override suspend fun getStoriesDetailData(data: StoriesRequestModel): StoriesDetail =
        withContext(dispatchers.io) {
            val detailRequest = storiesDetailsUseCase(data)
            return@withContext mapper.mapStoriesDetailRequest("", detailRequest)
        }

    override suspend fun setStoriesTrackActivity(data: StoriesTrackActivityRequestModel): Boolean =
        withContext(dispatchers.io) {
            val trackActivityRequest = storiesTrackActivityUseCase(data)
            return@withContext trackActivityRequest.data.isSuccess
        }

    override suspend fun deleteStory(storyId: String) : Boolean = withContext(dispatchers.io) {
        val param = UpdateStoryUseCase.Param(storyId, StoryActionType.Delete)
        val response = updateStoryUseCase(param)
        response.storyId.storyId == storyId
    }

    override suspend fun getStoriesProducts(
        shopId: String,
        storyId: String,
        catName: String,
    ): ProductBottomSheetUiState {
        return withContext(dispatchers.io) {
            val response = storiesProductUseCase(
                storiesProductUseCase.convertToMap(
                    StoriesProductUseCase.Param(
                        id = storyId,
                        catName = catName,
                    )
                )
            )
            ProductBottomSheetUiState(
                products = productMapper.mapProducts(response.data, shopId),
                campaign = productMapper.mapCampaign(response.data.campaign),
                resultState = ResultState.Success,
            )
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
