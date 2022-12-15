package com.tokopedia.product.manage.feature.list.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.common.interfaces.ProductCampaignInfoListener
import com.tokopedia.product.manage.databinding.ItemManageProductListBinding
import com.tokopedia.product.manage.databinding.ItemManageProductListTobaccoBinding
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_STOCK_AVAILABLE
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.utils.view.binding.viewBinding
import kotlin.math.abs

class TobaccoViewHolder(
    view: View
) : AbstractViewHolder<ProductUiModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_manage_product_list_tobacco
    }

    private val binding by viewBinding<ItemManageProductListTobaccoBinding>()

    override fun bind(product: ProductUiModel) {

    }
}
