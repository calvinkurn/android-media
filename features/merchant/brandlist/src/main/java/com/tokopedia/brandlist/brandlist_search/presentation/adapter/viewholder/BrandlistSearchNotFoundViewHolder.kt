package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchNotFoundUiModel
import com.tokopedia.brandlist.common.ImageAssets
import com.tokopedia.brandlist.databinding.ViewBrandNotFoundBinding
import com.tokopedia.utils.view.binding.viewBinding

class BrandlistSearchNotFoundViewHolder(
        view: View,
        private val listener: Listener
): AbstractViewHolder<BrandlistSearchNotFoundUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_brand_not_found
    }
    private var binding: ViewBrandNotFoundBinding? by viewBinding()

    override fun bind(element: BrandlistSearchNotFoundUiModel?) {
        binding?.brandNotFoundLayout?.visibility = View.VISIBLE
        ImageHandler.loadImage(itemView.context, binding?.imgBrandNotFound,
                ImageAssets.BRAND_NOT_FOUND, null)
        binding?.btnCheckBalance?.setOnClickListener {
            val inputManager = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            listener.focusSearchView()
        }
    }

    interface Listener {
        fun focusSearchView()
    }
}
