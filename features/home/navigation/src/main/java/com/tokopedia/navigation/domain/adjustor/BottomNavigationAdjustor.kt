package com.tokopedia.navigation.domain.adjustor

import com.tokopedia.navigation.presentation.model.BottomNavHomeType
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import javax.inject.Inject

class BottomNavigationAdjustor @Inject constructor() {

    fun adjust(models: List<BottomNavBarUiModel>): List<BottomNavBarUiModel> {
        return models
            .defaultDataIfEmpty()
            .homeToFirstIndex()
    }

    private fun List<BottomNavBarUiModel>.defaultDataIfEmpty(): List<BottomNavBarUiModel> {
        return ifEmpty { defaultBottomNavModel }
    }

    private fun List<BottomNavBarUiModel>.homeToFirstIndex(): List<BottomNavBarUiModel> {
        val firstModel = firstOrNull() ?: return this
        if (firstModel.type == BottomNavHomeType) return this
        val homeModel = firstOrNull { it.type == BottomNavHomeType } ?: return this
        return listOf(homeModel) + filterNot { it == homeModel }
    }
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
