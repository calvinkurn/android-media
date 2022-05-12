package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodNoPinPoinBinding
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeNoPinPoinUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeNoPinPoinViewHolder (
    itemView: View
): AbstractViewHolder<TokoFoodHomeNoPinPoinUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_no_pin_poin

        const val IMG_STATIC_URI = "https://images.tokopedia.net/img/ic-tokofood_home_no_pin_poin.png"
    }

    private var binding: ItemTokofoodNoPinPoinBinding? by viewBinding()
    private var imgNoPinPoin: ImageView? = null
    private var btnNoPinPoin: UnifyButton? = null

    override fun bind(element: TokoFoodHomeNoPinPoinUiModel) {
        setupNoPinPoin()
        bindNoPinPoin()
    }

    private fun setupNoPinPoin() {
        imgNoPinPoin = binding?.imgNoPinPoin
        btnNoPinPoin = binding?.btnNoPinPoinLocation
    }

    private fun bindNoPinPoin() {
        imgNoPinPoin?.loadImage(IMG_STATIC_URI)
        btnNoPinPoin?.setOnClickListener {

        }
    }
}