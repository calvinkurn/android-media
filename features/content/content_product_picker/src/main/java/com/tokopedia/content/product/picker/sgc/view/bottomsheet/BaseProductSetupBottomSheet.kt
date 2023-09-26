package com.tokopedia.content.product.picker.sgc.view.bottomsheet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.content.product.picker.sgc.view.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.content.product.picker.sgc.view.viewmodel.ViewModelFactoryProvider
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
open class BaseProductSetupBottomSheet : BottomSheetUnify() {

    protected lateinit var viewModel: PlayBroProductSetupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val parentFragment = requireParentFragment()
        viewModel = ViewModelProvider(
            parentFragment, (parentFragment as ViewModelFactoryProvider).getFactory())
            .get(PlayBroProductSetupViewModel::class.java)
        super.onCreate(savedInstanceState)
    }
}
