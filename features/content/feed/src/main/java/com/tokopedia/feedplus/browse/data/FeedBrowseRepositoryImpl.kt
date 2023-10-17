package com.tokopedia.feedplus.browse.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedXHeaderRequestFields
import com.tokopedia.content.common.usecase.FeedXHeaderUseCase
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationModel
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseRepositoryImpl @Inject constructor(
    private val feedXHeaderUseCase: FeedXHeaderUseCase,
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val playWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val mapper: FeedBrowseMapper,
    private val connectionUtil: PlayWidgetConnectionUtil,
    private val dispatchers: CoroutineDispatchers
) : FeedBrowseRepository {

    override suspend fun getTitle(): String {
        return withContext(dispatchers.io) {
            try {
                feedXHeaderUseCase.setRequestParams(
                    FeedXHeaderUseCase.createParam(
                        listOf(
                            FeedXHeaderRequestFields.BROWSE.value
                        )
                    )
                )
                val response = feedXHeaderUseCase.executeOnBackground()
                mapper.mapTitle(response)
            } catch (_: Throwable) {
                ""
            }
        }
    }

    override suspend fun getSlots(): List<FeedBrowseModel> {
        return withContext(dispatchers.io) {
            val response = feedXHomeUseCase(
                feedXHomeUseCase.createParams(source = FeedXHomeUseCase.SOURCE_BROWSE)
            )
            listOf(
                FeedBrowseModel.InspirationBanner(
                    slotId = "item.id",
                    title = "Ini Banner",
                    identifier = "Identifier Banner",
                    bannerList = emptyList(),
                )
            ) + mapper.mapSlotsResponse(response).ifEmpty {
                throw IllegalStateException("no slots available")
            }
        }
    }

    override suspend fun getWidgetContentSlot(
        extraParam: WidgetRequestModel
    ): ContentSlotModel = withContext(dispatchers.io) {
        val isWifi = connectionUtil.isEligibleForHeavyDataUsage()
        val response = playWidgetSlotUseCase(
            GetPlayWidgetSlotUseCase.Param(
                req = GetPlayWidgetSlotUseCase.Param.Request(
                    group = extraParam.group,
                    cursor = "",
                    sourceId = extraParam.sourceId,
                    sourceType = extraParam.sourceType,
                    isWifi = isWifi
                )
            )
        )
        mapper.mapWidgetResponse(response)
    }

    override suspend fun getWidget(extraParam: WidgetRequestModel): FeedBrowseItemUiModel {
        val isWifi = connectionUtil.isEligibleForHeavyDataUsage()
        return withContext(dispatchers.io) {
            try {
                val response = playWidgetSlotUseCase(
                    GetPlayWidgetSlotUseCase.Param(
                        req = GetPlayWidgetSlotUseCase.Param.Request(
                            group = extraParam.group,
                            cursor = "",
                            sourceId = extraParam.sourceId,
                            sourceType = extraParam.sourceType,
                            isWifi = isWifi
                        )
                    )
                )
                mapper.mapWidget(response)
            } catch (err: Throwable) {
                ChannelUiState.Error(err, extraParam = extraParam)
            }
        }
    }

    override suspend fun getWidgetRecommendation(
        identifier: String
    ): WidgetRecommendationModel = withContext(dispatchers.io) {
        WidgetRecommendationModel.Banners(
            listOf(
                BannerWidgetModel(
                    title = "Buku",
                    imageUrl = "https://t4.ftcdn.net/jpg/01/77/47/67/360_F_177476718_VWfYMWCzK32bfPI308wZljGHvAUYSJcn.jpg",
                    appLink = ""
                ),
                BannerWidgetModel(
                    title = "Dapur",
                    imageUrl = "https://t3.ftcdn.net/jpg/00/89/98/90/360_F_89989031_L8rIrutZm7gnGsIkkyDEAO9s43BYipqt.jpg",
                    appLink = ""
                ),
                BannerWidgetModel(
                    title = "Elektronik",
                    imageUrl = "https://t4.ftcdn.net/jpg/00/86/56/65/240_F_86566504_1yU5DuXgM97XOXkZwcsSAvjP9EXtB4v2.jpg",
                    appLink = ""
                ),
                BannerWidgetModel(
                    title = "Fashion Wanita",
                    imageUrl = "https://t3.ftcdn.net/jpg/03/67/86/38/360_F_367863869_tYibIxEubHFxMl33Ow38JAHYakAZGsNz.jpg",
                    appLink = ""
                ),
                BannerWidgetModel(
                    title = "Fashion Anak",
                    imageUrl = "https://t3.ftcdn.net/jpg/02/47/59/52/360_F_247595262_LGff0kg2mdraH9DOqPGlftfsZCACc6Fn.jpg",
                    appLink = ""
                )
            )
        )
    }

    override suspend fun getCategoryInspiration(): List<FeedCategoryInspirationModel> {
        val isWifi = connectionUtil.isEligibleForHeavyDataUsage()
//        return withContext(dispatchers.io) {
//            try {
//                val response = playWidgetSlotUseCase.executeOnBackground(
//                    group = extraParam.group,
//                    cursor = "",
//                    sourceId = extraParam.sourceId,
//                    sourceType = extraParam.sourceType,
//                    isWifi = isWifi
//                )
//                mapper.mapWidget(response)
//            } catch (err: Throwable) {
//                ChannelUiState.Error(err, extraParam = extraParam)
//            }
//        }
        return withContext(dispatchers.io) {
            buildList {
                add(
                    FeedCategoryInspirationModel.Chips(
                        id = "chips_mock",
                        chipList = List(6) {
                            WidgetMenuModel(
                                id = it.toString(),
                                label = "Chips $it",
                                group = "Group $it",
                                sourceType = "Source Type $it",
                                sourceId = "Source Id $it",
                            )
                        }
                    )
                )

                addAll(
                    List(7) {
                        FeedCategoryInspirationModel.Card(
                            id = it.toString(),
                            imageUrl = "",
                            partnerName = "Partner $it",
                            avatarUrl = "",
                            badgeUrl = "",
                            title = "Card $it"
                        )
                    }
                )
            }
        }
    }
}
