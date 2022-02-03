package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.interactor.repository.HomePopularKeywordRepository
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import javax.inject.Inject

class HomePopularKeywordUseCase @Inject constructor(
        private val popularKeywordRepository: HomePopularKeywordRepository
        ) {
    suspend fun onPopularKeywordRefresh(refreshCount: Int, currentPopularKeywordListDataModel: PopularKeywordListDataModel): PopularKeywordListDataModel {
        try {
            popularKeywordRepository.setParams(page = refreshCount)
            val results = popularKeywordRepository.executeOnBackground()
            return if (results.data.keywords.isNotEmpty()) {
                val resultList = convertPopularKeywordDataList(results.data)
                val newPopularKeyword = currentPopularKeywordListDataModel.copy(
                        title = results.data.title,
                        subTitle = results.data.subTitle,
                        popularKeywordList = resultList,
                        isErrorLoad = false
                )
                newPopularKeyword
            } else {
                return currentPopularKeywordListDataModel.copy(isErrorLoad = true)
            }
        } catch (e: Exception) {
            return currentPopularKeywordListDataModel.copy(isErrorLoad = true)
        }
    }

    private fun convertPopularKeywordDataList(popularKeywordList: HomeWidget.PopularKeywordList): MutableList<PopularKeywordDataModel> {
        val keywordList = popularKeywordList.keywords
        val dataList: MutableList<PopularKeywordDataModel> = mutableListOf()
        for (pojo in keywordList) {
            dataList.add(
                    PopularKeywordDataModel(
                            recommendationType = popularKeywordList.recommendationType,
                            applink = pojo.url,
                            imageUrl = pojo.imageUrl,
                            title = pojo.keyword,
                            productCount = pojo.productCount)
            )
        }
        return dataList
    }
}