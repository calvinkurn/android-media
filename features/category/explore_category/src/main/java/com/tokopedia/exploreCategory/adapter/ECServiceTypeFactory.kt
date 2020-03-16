package com.tokopedia.exploreCategory.adapter

import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECAccordionVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECImageIconVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECShimmerVHViewModel

interface ECServiceTypeFactory {
    fun type(viewModel: ECShimmerVHViewModel): Int

    fun type(viewModel: ECAccordionVHViewModel): Int

    fun type(viewModel: ECImageIconVHViewModel): Int
}