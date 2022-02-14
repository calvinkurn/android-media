package com.tokopedia.play.broadcaster.setup.product.view.bottomsheet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
open class BaseProductSetupBottomSheet : BottomSheetUnify() {

    protected lateinit var viewModel: PlayBroProductSetupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val parentFragment = requireParentFragment() as ProductSetupFragment
        viewModel = ViewModelProvider(parentFragment, parentFragment.getViewModelFactory())
            .get(PlayBroProductSetupViewModel::class.java)
        super.onCreate(savedInstanceState)
    }
}