package com.tokopedia.content.product.picker.seller.view.bottomsheet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSellerViewModel
import com.tokopedia.content.product.picker.seller.view.viewmodel.ViewModelFactoryProvider
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
open class BaseProductSetupBottomSheet : BottomSheetUnify() {

    protected lateinit var viewModel: ContentProductPickerSellerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val parentFragment = requireParentFragment()
        viewModel = ViewModelProvider(
            parentFragment, (parentFragment as ViewModelFactoryProvider).getFactory())
            .get(ContentProductPickerSellerViewModel::class.java)
        super.onCreate(savedInstanceState)
    }
}
