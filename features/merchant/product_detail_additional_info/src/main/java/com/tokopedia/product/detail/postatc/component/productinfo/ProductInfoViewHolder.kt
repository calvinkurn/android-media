package com.tokopedia.product.detail.postatc.component.productinfo

import android.view.View.OnClickListener
import androidx.core.content.ContextCompat.startActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.detail.databinding.ItemProductInfoBinding
import com.tokopedia.product.detail.postatc.base.PostAtcListener
import com.tokopedia.product.detail.postatc.base.PostAtcViewHolder

class ProductInfoViewHolder(
    private val binding: ItemProductInfoBinding,
    private val listener: PostAtcListener
) : PostAtcViewHolder<ProductInfoUiModel>(binding.root) {
    override fun bind(element: ProductInfoUiModel) {
        binding.apply {
            postAtcProductInfoImage.setImageUrl(element.imageLink)
            postAtcProductInfoTitle.text = element.title
            postAtcProductInfoSubtitle.text = element.subtitle
            postAtcProductInfoButton.text = element.buttonText

            postAtcProductInfoButton.setOnClickListener(buttonClickListener)
        }
    }

    private val buttonClickListener = OnClickListener {
        listener.goToCart("123")
    }
}
