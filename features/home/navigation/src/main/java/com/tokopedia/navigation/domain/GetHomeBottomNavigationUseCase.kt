package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.navigation.domain.adjustor.BottomNavigationAdjustor
import com.tokopedia.navigation.domain.validator.BottomNavigationValidator
import com.tokopedia.navigation.util.BottomNavBarItemCacheManager
import com.tokopedia.navigation.util.CompletableTask
import com.tokopedia.navigation_common.model.bottomnav.GetHomeBottomNavigationResponse
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarJumper
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetHomeBottomNavigationUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val cache: BottomNavBarItemCacheManager,
    private val validator: BottomNavigationValidator,
    private val adjustor: BottomNavigationAdjustor
) : CoroutineUseCase<GetHomeBottomNavigationUseCase.FromCache, CompletableTask<List<BottomNavBarUiModel>>>(Dispatchers.IO) {

    override fun graphqlQuery(): String {
        return """
            query {
                getHomeBottomNavigation() {
                    bottomNavigations {
                        id
                        name
                        type
                        imageList {
                            type
                            imageUrl
                            leftPadding
                            rightPadding
                            imageType
                        }
                        jumper {
                            id
                            name
                            imageList {
                                type
                                imageUrl
                                leftPadding
                                rightPadding
                                imageType
                            }
                        }
                        discoID
                        queryParams
                    }
                }
            }
        """
    }

    override suspend fun execute(params: FromCache): CompletableTask<List<BottomNavBarUiModel>> {
        return if (params.value) {
            val cache = getDataFromCache().orEmpty()
            val adjustedData = adjustor.adjust(cache)
            CompletableTask(adjustedData) {}
        } else {
            val dataFromNetwork = getDataFromNetwork()
            validator.validate(dataFromNetwork)
            val adjustedData = adjustor.adjust(dataFromNetwork)
            CompletableTask(adjustedData) { cache.saveBottomNav(it) }
        }
    }

    private suspend fun getDataFromCache(): List<BottomNavBarUiModel>? {
        return cache.getBottomNav()
    }

    private suspend fun getDataFromNetwork(): List<BottomNavBarUiModel> {
        val response: GetHomeBottomNavigationResponse = graphqlRepository.request(graphqlQuery(), Unit)
        return response.toNavBarModelList()
    }

    private fun GetHomeBottomNavigationResponse.toNavBarModelList(): List<BottomNavBarUiModel> {
        return data.bottomNavigations.map { data ->
            val jumper = data.jumper
            BottomNavBarUiModel(
                id = data.id.toInt(),
                title = data.name,
                type = BottomNavBarItemType(data.type),
                jumper = if (jumper != null) {
                    BottomNavBarJumper(
                        id = jumper.id.toInt(),
                        title = jumper.name,
                        assets = jumper.imageList.toAssetsMap()
                    )
                } else {
                    null
                },
                assets = data.imageList.toAssetsMap(),
                discoId = DiscoId(data.discoId),
                queryParams = data.queryParams
            )
        }
    }

    private fun List<GetHomeBottomNavigationResponse.Image>.toAssetsMap(): Map<BottomNavBarAsset.Id, BottomNavBarAsset.Type> {
        return associate {
            BottomNavBarAsset.Id(it.type) to when (val type = it.imageType) {
                ASSET_TYPE_IMAGE -> BottomNavBarAsset.Type.ImageUrl(it.imageUrl)
                ASSET_TYPE_LOTTIE -> BottomNavBarAsset.Type.LottieUrl(it.imageUrl)
                else -> error("Not supported for type $type")
            }
        }
    }

    companion object {
        private const val ASSET_TYPE_IMAGE = "image"
        private const val ASSET_TYPE_LOTTIE = "lottie"
    }

    @JvmInline
    value class FromCache(val value: Boolean)
}
