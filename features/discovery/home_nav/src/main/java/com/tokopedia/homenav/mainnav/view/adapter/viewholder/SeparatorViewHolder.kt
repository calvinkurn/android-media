package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderSeparatorBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.SeparatorDataModel
import com.tokopedia.utils.view.binding.viewBinding

class SeparatorViewHolder(itemView: View,
                          mainNavListener: MainNavListener
): AbstractViewHolder<SeparatorDataModel>(itemView) {

    private var binding: HolderSeparatorBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_separator
    }

    @SuppressLint("ResourcePackage")
    override fun bind(element: SeparatorDataModel) {
        val context = itemView.context
        binding?.dividerUnify?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN200))
    }
}

