package com.tokopedia.content.product.picker.sgc.analytic.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.content.product.picker.sgc.analytic.ContentProductPickerSGCAnalytic
import com.tokopedia.content.product.picker.sgc.model.uimodel.ProductChooserUiState
import com.tokopedia.content.product.picker.sgc.model.ProductListPaging
import com.tokopedia.content.product.picker.sgc.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.content.product.picker.sgc.view.viewcomponent.EtalaseChipsViewComponent
import com.tokopedia.content.product.picker.sgc.view.viewcomponent.ProductListViewComponent
import com.tokopedia.content.product.picker.sgc.view.viewcomponent.SaveButtonViewComponent
import com.tokopedia.content.product.picker.sgc.view.viewcomponent.SearchBarViewComponent
import com.tokopedia.content.product.picker.sgc.view.viewcomponent.SortChipsViewComponent
import com.tokopedia.play_common.util.extension.withCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 17/02/22
 */
class ProductChooserAnalyticManager @Inject constructor(
    private val analytic: ContentProductPickerSGCAnalytic,
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
                    is ProductChooserBottomSheet.Event.ViewBottomSheet -> {
                        analytic.viewProductChooser()
                    }
                    SaveButtonViewComponent.Event.OnClicked -> {
                        analytic.clickSaveButtonOnProductSetup()
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
                        analytic.clickProductSortingType(it.sort.text)
                    }
                    SearchBarViewComponent.Event.OnSearchBarClicked -> {
                        analytic.clickSearchBarOnProductSetup()
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
            analytic.clickSearchWhenParamChanged(state.keyword)
        }
    }
}
