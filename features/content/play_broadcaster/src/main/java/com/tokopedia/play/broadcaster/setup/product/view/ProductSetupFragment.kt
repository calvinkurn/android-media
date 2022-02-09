package com.tokopedia.play.broadcaster.setup.product.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductSetupFragment @Inject constructor() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openProductChooser()
//        openProductSummary()
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
}