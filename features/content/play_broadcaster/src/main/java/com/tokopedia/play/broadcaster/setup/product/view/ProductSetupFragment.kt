package com.tokopedia.play.broadcaster.setup.product.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductSetupFragment @Inject constructor(
    private val productSetupViewModelFactory: PlayBroProductSetupViewModel.Factory,
) : Fragment() {

    private var mDataSource: DataSource? = null

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productChooserListener = object : ProductChooserBottomSheet.Listener {
        override fun onSetupCancelled(bottomSheet: ProductChooserBottomSheet) {
            if (childFragmentManager.fragments.isEmpty()) {
                //CLOSE BOTTOMSHEET
            }
        }

        override fun onSetupSuccess(bottomSheet: ProductChooserBottomSheet) {
            bottomSheet.dismiss()
            openProductSummary()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openProductChooser()
//        openProductSummary()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductChooserBottomSheet -> childFragment.setListener(productChooserListener)
        }
    }

    fun removeFragment() {
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }

    fun openCampaignAndEtalaseList() {
        EtalaseListBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }

    fun openProductChooser() {
        ProductChooserBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }

    fun openProductSummary() {
        ProductSummaryBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    fun getViewModelFactory(): ViewModelProvider.Factory {
        if (!::viewModelFactory.isInitialized) {
            viewModelFactory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return productSetupViewModelFactory.create(
                        mDataSource?.getProductSectionList().orEmpty()
                    ) as T
                }
            }
        }
        return viewModelFactory
    }

    interface DataSource {

        fun getProductSectionList(): List<ProductTagSectionUiModel>
    }
}