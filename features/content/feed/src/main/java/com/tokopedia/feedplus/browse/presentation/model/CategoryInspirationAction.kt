package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal sealed interface CategoryInspirationAction {

    object Init : CategoryInspirationAction
    data class LoadData(val menu: WidgetMenuModel) : CategoryInspirationAction

    object LoadMoreData : CategoryInspirationAction
    data class SelectMenu(val menu: WidgetMenuModel) : CategoryInspirationAction
}
