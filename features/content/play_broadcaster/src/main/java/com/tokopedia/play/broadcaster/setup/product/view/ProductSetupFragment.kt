package com.tokopedia.play.broadcaster.setup.product.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.product.viewmodel.ViewModelFactoryProvider
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.view.bottomsheet.ProductPickerUGCBottomSheet
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductSetupFragment @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val productSetupViewModelFactory: PlayBroProductSetupViewModel.Factory,
) : Fragment(), ViewModelFactoryProvider {

    private var mDataSource: DataSource? = null

    private lateinit var productSetupViewModelProviderFactory: ViewModelProvider.Factory

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private var chooserSource = ChooserSource.Preparation

    private val productChooserListener = object : ProductChooserBottomSheet.Listener {
        override fun onSetupCancelled(bottomSheet: ProductChooserBottomSheet) {
            bottomSheet.dismiss()

            when (chooserSource) {
                ChooserSource.Preparation -> removeFragment()
                ChooserSource.Summary -> openProductSummary()
            }
        }

        override fun onSetupSuccess(bottomSheet: ProductChooserBottomSheet) {
            bottomSheet.dismiss()
            openProductSummary()
        }

        override fun openCampaignAndEtalaseList(bottomSheet: ProductChooserBottomSheet) {
            openCampaignAndEtalaseList()
        }
    }

    private val productSummaryListener = object : ProductSummaryBottomSheet.Listener {
        override fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>) {
            parentViewModel.submitAction(
                PlayBroadcastAction.SetProduct(productTagSectionList)
            )
        }

        override fun onShouldAddProduct(bottomSheet: ProductSummaryBottomSheet) {
            bottomSheet.dismiss()
            openProductChooser(ChooserSource.Summary)
        }

        override fun onFinish(bottomSheet: ProductSummaryBottomSheet) {
            bottomSheet.dismiss()
            removeFragment()
        }
    }

    private val productPickerUGCListener = object : ProductPickerUGCBottomSheet.Listener {
        override fun onCancelled(bottomSheet: ProductPickerUGCBottomSheet) {
            bottomSheet.dismiss()

            when (chooserSource) {
                ChooserSource.Preparation -> removeFragment()
                ChooserSource.Summary -> openProductSummary()
            }
        }

        override fun onFinished(bottomSheet: ProductPickerUGCBottomSheet) {
            bottomSheet.dismiss()
            openProductSummary()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity())
        )[PlayBroadcastViewModel::class.java]

        if (savedInstanceState != null) return

        if (parentViewModel.productSectionList.isEmpty()) openProductChooser(ChooserSource.Preparation)
        else openProductSummary()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductChooserBottomSheet -> childFragment.setListener(productChooserListener)
            is ProductSummaryBottomSheet -> childFragment.setListener(productSummaryListener)
            is ProductPickerUGCBottomSheet -> childFragment.setListener(productPickerUGCListener)
        }
    }

    private fun removeFragment() {
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    private fun openCampaignAndEtalaseList() {
        EtalaseListBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }

    private fun openShopProductChooser() {
        ProductChooserBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }

    private fun openProductSummary() {
        ProductSummaryBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }

    private fun openUGCProductChooser() {
        ProductPickerUGCBottomSheet.getOrCreate(
            childFragmentManager,
            requireActivity().classLoader,
        ).showNow(childFragmentManager)
    }

    private fun openProductChooser(chooserSource: ChooserSource) {
        this.chooserSource = chooserSource

        val selectedAccount = parentViewModel.uiState.value.selectedContentAccount
        if (selectedAccount.isShop) {
            openShopProductChooser()
        } else {
            openUGCProductChooser()
        }
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    override fun getFactory(): ViewModelProvider.Factory {
        if (!::productSetupViewModelProviderFactory.isInitialized) {
            productSetupViewModelProviderFactory = object : AbstractSavedStateViewModelFactory(
                this,
                arguments
            ) {
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return productSetupViewModelFactory.create(
                        mDataSource?.getProductSectionList().orEmpty(),
                        handle,
                        mDataSource?.isEligibleForPin() ?: true,
                    ) as T
                }
            }
        }
        return productSetupViewModelProviderFactory
    }

    private enum class ChooserSource {
        Preparation,
        Summary
    }

    interface DataSource {

        fun getProductSectionList(): List<ProductTagSectionUiModel>
        fun isEligibleForPin(): Boolean
    }
}
