package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal sealed interface FeedCategoryInspirationAction {

    object Init : FeedCategoryInspirationAction
    data class LoadData(val menu: WidgetMenuModel) : FeedCategoryInspirationAction

    object LoadMoreData : FeedCategoryInspirationAction
    data class SelectMenu(val menu: WidgetMenuModel) : FeedCategoryInspirationAction
}
