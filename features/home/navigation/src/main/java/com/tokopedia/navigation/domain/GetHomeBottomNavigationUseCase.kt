package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.navigation.util.BottomNavBarItemCacheManager
import com.tokopedia.navigation_common.model.bottomnav.GetHomeBottomNavigationResponse
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetHomeBottomNavigationUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val cache: BottomNavBarItemCacheManager
) : CoroutineUseCase<GetHomeBottomNavigationUseCase.FromCache, List<BottomNavBarUiModel>>(Dispatchers.IO) {

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
                    }
                }
            }
        """
    }

    override suspend fun execute(params: FromCache): List<BottomNavBarUiModel> {
        return if (params.value) {
            getDataFromCache()
        } else {
            getDataFromNetwork().also { cache.saveBottomNav(it) }
        }
    }

    private suspend fun getDataFromCache(): List<BottomNavBarUiModel> {
        return cache.getBottomNav() ?: defaultBottomNavModel
    }

    private suspend fun getDataFromNetwork(): List<BottomNavBarUiModel> {
        val response: GetHomeBottomNavigationResponse = graphqlRepository.request(graphqlQuery(), Unit)
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
                },
                discoId = DiscoId(data.discoId)
            )
        }
    }

    @JvmInline
    value class FromCache(val value: Boolean)
}

private val defaultBottomNavModel = listOf(
    BottomNavBarUiModel(
        id = 1,
        title = "Home",
        type = BottomNavBarItemType("home"),
        jumper = null,
        assets = mapOf(
            "selected_icon_light_more" to BottomNavBarAsset.Image("https://images.tokopedia.net/img/iEWsxH/2024/4/21/6dad9310-318c-49ca-8a33-e26f5a84979c.png"),
            "unselected_icon_light_more" to BottomNavBarAsset.Image("https://images.tokopedia.net/img/iEWsxH/2024/4/21/9ffc8d54-59a2-4a15-84eb-183e7f9196a3.png"),
            "active_icon_light_mode" to BottomNavBarAsset.Lottie("https://assets.tokopedia.net/file/MqkckN/2024/4/21/4e89b3c0-b07c-4a8e-bfc7-bfdb9b3c6bf2.json"),
            "inactive_icon_light_mode" to BottomNavBarAsset.Lottie("https://assets.tokopedia.net/file/MqkckN/2024/4/21/e88f1fc0-bcbc-43ad-9108-4288a73f50d2.json")
        ),
        discoId = DiscoId.Empty
    ),
    BottomNavBarUiModel(
        id = 2,
        title = "Feed",
        type = BottomNavBarItemType("feed"),
        jumper = null,
        assets = mapOf(
            "selected_icon_light_more" to BottomNavBarAsset.Image("https://images.tokopedia.net/img/iEWsxH/2024/4/21/75768c0e-91af-465e-b202-4ab8d844463f.png"),
            "unselected_icon_light_more" to BottomNavBarAsset.Image("https://images.tokopedia.net/img/iEWsxH/2024/4/21/636aa35b-4ba1-4fab-9ea2-6d44ec2dd021.png"),
            "active_icon_light_mode" to BottomNavBarAsset.Lottie("https://assets.tokopedia.net/file/MqkckN/2024/4/21/41d3677b-231b-479f-81c2-ed54f2afdcfb.json"),
            "inactive_icon_light_mode" to BottomNavBarAsset.Lottie("https://assets.tokopedia.net/file/MqkckN/2024/4/21/3d8084ee-6583-4f89-a24e-ca1148269cb3.json")
        ),
        discoId = DiscoId.Empty
    ),
    BottomNavBarUiModel(
        id = 3,
        title = "Official Store",
        type = BottomNavBarItemType("discopage"),
        jumper = null,
        assets = mapOf(
            "selected_icon_light_more" to BottomNavBarAsset.Image("https://images.tokopedia.net/img/iEWsxH/2024/4/21/d96ff39e-6acf-42c0-afbe-00d5263435ea.png"),
            "unselected_icon_light_more" to BottomNavBarAsset.Image("https://images.tokopedia.net/img/iEWsxH/2024/4/21/96c4167d-0e11-4404-8e70-e302b3545d47.png"),
            "active_icon_light_mode" to BottomNavBarAsset.Lottie("https://assets.tokopedia.net/file/MqkckN/2024/4/21/2bb87f04-4b52-43ee-949a-077c85163ae2.json"),
            "inactive_icon_light_mode" to BottomNavBarAsset.Lottie("https://assets.tokopedia.net/file/MqkckN/2024/4/22/dd18633e-372f-4821-ac52-7a4efbb4bd8d.json")
        ),
        discoId = DiscoId.Empty
    )
)
