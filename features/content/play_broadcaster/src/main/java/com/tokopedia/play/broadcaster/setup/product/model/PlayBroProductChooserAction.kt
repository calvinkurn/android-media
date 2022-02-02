package com.tokopedia.play.broadcaster.setup.product.model

import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
sealed class PlayBroProductChooserAction {

    data class SetSort(val sort: SortUiModel) : PlayBroProductChooserAction()
}