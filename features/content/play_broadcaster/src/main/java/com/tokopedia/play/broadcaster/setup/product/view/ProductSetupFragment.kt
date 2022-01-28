package com.tokopedia.play.broadcaster.setup.product.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class ProductSetupFragment @Inject constructor() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        PlayBroEtalaseListBottomSheet.getFragment(
//            childFragmentManager,
//            requireActivity().classLoader,
//        ).show(childFragmentManager)

        ProductChooserBottomSheet.getFragment(
            childFragmentManager,
            requireActivity().classLoader,
        ).show(childFragmentManager)
    }
}