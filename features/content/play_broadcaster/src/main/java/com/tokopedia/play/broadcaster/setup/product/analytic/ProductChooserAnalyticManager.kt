package com.tokopedia.play.broadcaster.setup.product.analytic

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalytic
import com.tokopedia.play.broadcaster.setup.product.model.ProductChooserUiState
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.model.ProductListPaging
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.EtalaseChipsViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.ProductListViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SaveButtonViewComponent
import com.tokopedia.play.broadcaster.setup.product.view.viewcomponent.SortChipsViewComponent
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 17/02/22
 */
class ProductChooserAnalyticManager @Inject constructor(
    private val analytic: PlayBroSetupProductAnalytic,
    private val dispatchers: CoroutineDispatchers,
) {

    fun observe(
        scope: CoroutineScope,
        event: EventBus<Any>,
        state: StateFlow<ProductChooserUiState>
    ) {
        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
                when (it) {
                    SaveButtonViewComponent.Event.OnClicked -> {
                        //TODO("Ask why need productId")
                        analytic.clickSaveButtonOnProductSetup("")
                    }
                    is ProductListViewComponent.Event.OnSelected -> {
                        if (it.product.stock <= 0) return@collect
                        analytic.clickSelectProductOnProductSetup(it.product.id)
                    }
                    EtalaseChipsViewComponent.Event.OnClicked -> {
                        analytic.clickCampaignAndEtalaseFilter()
                    }
                    SortChipsViewComponent.Event.OnClicked -> {
                        analytic.clickProductSorting()
                    }
                    ProductChooserBottomSheet.Event.ExitDialogConfirm -> {
                        analytic.clickConfirmCloseOnProductChooser()
                    }
                    ProductChooserBottomSheet.Event.ExitDialogCancel -> {
                        analytic.clickCancelCloseOnProductChooser()
                    }
                    ProductChooserBottomSheet.Event.CloseClicked -> {
                        analytic.clickCloseOnProductChooser(isProductSelected = false)
                    }
                    is ProductChooserBottomSheet.Event.SortChosen -> {
                        //TODO("Ask whether this is every sort clicked or chosen")
                        analytic.clickProductSortingType(it.sort.text)
                    }
                }
            }
        }

        scope.launch(dispatchers.computation) {
            state.withCache().collectLatest { (prevState, state) ->
                handleSearchAnalytic(prevState?.loadParam, state.loadParam)
            }
        }
    }

    private fun handleSearchAnalytic(
        prev: ProductListPaging.Param?,
        state: ProductListPaging.Param
    ) {
        if (prev?.keyword != null && prev.keyword != state.keyword) {
            analytic.clickSearchBarOnProductSetup(state.keyword)
        }
    }
}