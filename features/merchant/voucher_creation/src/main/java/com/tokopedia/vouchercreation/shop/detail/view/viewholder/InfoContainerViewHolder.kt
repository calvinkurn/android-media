package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.InfoContainerUiModel
import com.tokopedia.vouchercreation.shop.detail.view.adapter.SubInfoAdapter
import kotlinx.android.synthetic.main.item_mvc_info_container.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class InfoContainerViewHolder(
        itemView: View?,
        private val onCtaClick: (dataKey: String) -> Unit,
        private val onImpression: (dataKey: String) -> Unit
) : AbstractViewHolder<InfoContainerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_info_container
    }

    private val subInfoAdapter by lazy { SubInfoAdapter() }

    override fun bind(element: InfoContainerUiModel) {
        with(itemView) {
            tvMvcInfoTitle.text = context?.getString(element.titleRes).toBlankOrString()
            rvMvcSubInfo.layoutManager = getLinearLayoutManager(context)
            rvMvcSubInfo.adapter = subInfoAdapter
            tvMvcInfoCta.isVisible = element.hasCta
            tvMvcInfoCta.setOnClickListener {
                onCtaClick(element.dataKey)
            }
            addOnImpressionListener(element.impressHolder) {
                onImpression(element.dataKey)
            }
        }
        with(subInfoAdapter) {
            setSubInfoItems(element.informationList)
            setOnPromoCodeClicked {
                element.onPromoCodeCopied()
            }
        }
    }

    private fun getLinearLayoutManager(context: Context): LinearLayoutManager {
        return object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }
    }
}