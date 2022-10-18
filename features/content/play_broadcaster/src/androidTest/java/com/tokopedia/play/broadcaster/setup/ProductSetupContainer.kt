package com.tokopedia.play.broadcaster.setup

import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.play.broadcaster.helper.BottomSheetContainer
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
class ProductSetupContainer(
    private val viewModel: (handle: SavedStateHandle) -> PlayBroProductSetupViewModel = {
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
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle,
            ): T {
                return object : PlayBroProductSetupViewModel.Factory {
                    override fun create(
                        productSectionList: List<ProductTagSectionUiModel>,
                        savedStateHandle: SavedStateHandle,
                        isEligibleForPin: Boolean,
                    ): PlayBroProductSetupViewModel {
                        return viewModel(savedStateHandle)
                    }
                }.create(emptyList(), handle, false) as T
            }
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        onAttach(childFragment)
    }
}
