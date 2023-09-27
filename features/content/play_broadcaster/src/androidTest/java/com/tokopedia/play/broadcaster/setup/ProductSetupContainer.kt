package com.tokopedia.play.broadcaster.setup

import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.helper.BottomSheetContainer
import com.tokopedia.content.product.picker.sgc.view.viewmodel.ContentProductPickerSGCViewModel
import com.tokopedia.content.product.picker.sgc.view.viewmodel.ViewModelFactoryProvider
import com.tokopedia.content.product.picker.sgc.model.campaign.ProductTagSectionUiModel

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
class ProductSetupContainer(
    private val viewModel: (handle: SavedStateHandle) -> ContentProductPickerSGCViewModel = {
        productSetupViewModel(handle = it)
    },
    private val onAttach: (child: Fragment) -> Unit = {},
    creator: (className: String) -> Fragment,
) : BottomSheetContainer(creator), ViewModelFactoryProvider {

    override fun getFactory(): ViewModelProvider.Factory {
        return object : AbstractSavedStateViewModelFactory(
            this,
            arguments
        ) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle,
            ): T {
                return object : ContentProductPickerSGCViewModel.Factory {
                    override fun create(
                        creationId: String,
                        maxProduct: Int,
                        productSectionList: List<ProductTagSectionUiModel>,
                        savedStateHandle: SavedStateHandle,
                        isNumerationShown: Boolean,
                        isEligibleForPin: Boolean,
                        fetchCommissionProduct: Boolean
                    ): ContentProductPickerSGCViewModel {
                        return viewModel(savedStateHandle)
                    }
                }.create("123", 30, emptyList(), handle, isNumerationShown = true,
                    isEligibleForPin = false,
                    fetchCommissionProduct = false
                ) as T
            }
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        onAttach(childFragment)
    }
}
