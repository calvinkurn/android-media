package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeAllCategoryUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel

object HomeLayoutMapper {

    fun mapToHomeUiModel(response: List<HomeLayoutResponse>): List<Visitable<*>> {
        // Temp Dummy Data
        return response.run {
            listOf(
                    HomeChooseAddressWidgetUiModel("0", "Choose Address Widget"),
                    HomeAllCategoryUiModel("1", "Kategori"),
                    HomeSectionUiModel("2", "Another section"),
                    BannerDataModel(ChannelModel(
                            "1221",
                            "12",
                            channelGrids = listOf(
                                    ChannelGrid(id = "0", imageUrl = "http://www.helpmykidlearn.ie/images/uploads/Big_and_small_329.jpg"),
                                    ChannelGrid(id = "1", imageUrl = "http://www.helpmykidlearn.ie/images/uploads/Big_and_small_329.jpg")
                            )
                    ))
            )
        }
    }
}
