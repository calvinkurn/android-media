package com.tokopedia.play.broadcaster.setup

import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.helper.BottomSheetContainer
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource

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
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle,
            ): T {
                return object : PlayBroProductSetupViewModel.Factory {
                    override fun create(
                        creationId: String,
                        maxProduct: Int,
                        productSectionList: List<ProductTagSectionUiModel>,
                        savedStateHandle: SavedStateHandle,
                        source: PlayBroPageSource,
                        isEligibleForPin: Boolean,
                        fetchCommissionProduct: Boolean
                    ): PlayBroProductSetupViewModel {
                        return viewModel(savedStateHandle)
                    }
                }.create("123", 30, emptyList(), handle, source = PlayBroPageSource.Live,
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
