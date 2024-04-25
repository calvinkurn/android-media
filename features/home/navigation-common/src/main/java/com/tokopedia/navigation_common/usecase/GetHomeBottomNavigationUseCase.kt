package com.tokopedia.navigation_common.usecase

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.navigation_common.model.bottomnav.GetHomeBottomNavigationResponse
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetHomeBottomNavigationUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
) : CoroutineUseCase<Unit, List<BottomNavBarUiModel>>(Dispatchers.IO) {

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
                    }
                }
            }
        """
    }

    override suspend fun execute(params: Unit): List<BottomNavBarUiModel> {
        val response: GetHomeBottomNavigationResponse = graphqlRepository.request(graphqlQuery(), params)
        return response.data.bottomNavigations.map { data ->
            BottomNavBarUiModel(
                id = data.id.toInt(),
                title = data.name,
                type = BottomNavBarItemType(data.type),
                jumper = null,
                assets = data.imageList.associate {
                    it.type to when (val type = it.imageType) {
                        "image" -> BottomNavBarAsset.Image(it.imageUrl)
                        "lottie" -> BottomNavBarAsset.Lottie(it.imageUrl)
                        else -> error("Not supported for type $type")
                    }
                }
            )
        }
    }
}
