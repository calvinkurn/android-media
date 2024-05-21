package com.tokopedia.content.product.picker

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.orUnknown
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductPickerUserBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSellerViewModel
import com.tokopedia.content.product.picker.seller.view.viewmodel.ViewModelFactoryProvider
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
@Suppress("LateinitUsage")
class ProductSetupFragment @Inject constructor(
    private val productSetupViewModelFactory: ContentProductPickerSellerViewModel.Factory,
    private val productSetupProductAnalytic: ContentProductPickerSellerAnalytic,
) : Fragment(), ViewModelFactoryProvider {

    private var mDataSource: DataSource? = null

    private var mListener: Listener? = null

    private lateinit var productSetupViewModelProviderFactory: ViewModelProvider.Factory

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
            mListener?.onProductChanged(productTagSectionList)
        }

        override fun onShouldAddProduct(bottomSheet: ProductSummaryBottomSheet) {
            bottomSheet.dismiss()
            openProductChooser(ChooserSource.Summary)
        }

        override fun onFinish(bottomSheet: ProductSummaryBottomSheet) {
            mListener?.onProductSetupDismissed()
            bottomSheet.dismiss()
            removeFragment()
        }

        override fun onProductSummaryCommissionShown() {
            mListener?.onProductSummaryCommissionShown()
        }
    }

    private val productPickerUGCListener = object : ProductPickerUserBottomSheet.Listener {
        override fun onCancelled(bottomSheet: ProductPickerUserBottomSheet) {
            bottomSheet.dismiss()

            when (chooserSource) {
                ChooserSource.Preparation -> removeFragment()
                ChooserSource.Summary -> openProductSummary()
            }
        }

        override fun onFinished(bottomSheet: ProductPickerUserBottomSheet) {
            bottomSheet.dismiss()
            openProductSummary()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        productSetupProductAnalytic.setSelectedAccount(mDataSource?.getSelectedAccount().orUnknown())

        if (mDataSource?.getProductSectionList()?.isEmpty() == true) {
            openProductChooser(ChooserSource.Preparation)
        } else {
            openProductSummary()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductChooserBottomSheet -> {
                childFragment.setListener(productChooserListener)
            }
            is ProductSummaryBottomSheet -> {
                childFragment.setListener(productSummaryListener)
            }
            is ProductPickerUserBottomSheet -> {
                childFragment.setListener(productPickerUGCListener)
                childFragment.setDataSource(object : ProductPickerUserBottomSheet.DataSource {
                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return mDataSource?.getSelectedAccount().orUnknown()
                    }

                    override fun getShopBadgeIfAny(): String {
                        return mDataSource?.getSelectedAccount()?.badge.orEmpty()
                    }
                })
            }
            is EtalaseListBottomSheet -> {
                childFragment.setDataSource(object : EtalaseListBottomSheet.DataSource {
                    override fun getSelectedAccount(): ContentAccountUiModel {
                        return mDataSource?.getSelectedAccount().orUnknown()
                    }
                })
            }
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
            requireActivity().classLoader
        ).show(childFragmentManager)
    }

    private fun openShopProductChooser() {
        ProductChooserBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader
        ).show(childFragmentManager)
    }

    private fun openProductSummary() {
        ProductSummaryBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader
        ).show(childFragmentManager)
    }

    private fun openUGCProductChooser() {
        ProductPickerUserBottomSheet.getOrCreate(
            childFragmentManager,
            requireActivity().classLoader
        ).showNow(childFragmentManager)
    }

    private fun openProductChooser(chooserSource: ChooserSource) {
        this.chooserSource = chooserSource

        val selectedAccount = mDataSource?.getSelectedAccount()
        if (selectedAccount?.isShop == true) {
            openShopProductChooser()
        } else {
            openUGCProductChooser()
        }
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    override fun getFactory(): ViewModelProvider.Factory {
        if (!::productSetupViewModelProviderFactory.isInitialized) {
            productSetupViewModelProviderFactory = object : AbstractSavedStateViewModelFactory(
                this,
                arguments
            ) {
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return productSetupViewModelFactory.create(
                        mDataSource?.creationId().orEmpty(),
                        mDataSource?.maxProduct().orZero(),
                        mDataSource?.getProductSectionList().orEmpty(),
                        handle,
                        mDataSource?.isNumerationShown().orFalse(),
                        mDataSource?.isEligibleForPin().orTrue(),
                        mDataSource?.fetchCommissionProduct().orFalse(),
                        mDataSource?.getSelectedAccount().orUnknown(),
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
        fun getSelectedAccount(): ContentAccountUiModel
        fun creationId(): String
        fun maxProduct(): Int
        fun isNumerationShown(): Boolean
        fun fetchCommissionProduct(): Boolean
    }

    interface Listener {
        fun onProductChanged(productTagSectionList: List<ProductTagSectionUiModel>)
        fun onProductSetupDismissed() {}

        fun onProductSummaryCommissionShown() {}
    }
}
