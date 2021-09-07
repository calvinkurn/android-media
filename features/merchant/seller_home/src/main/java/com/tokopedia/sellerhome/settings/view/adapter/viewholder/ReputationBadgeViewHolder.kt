package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ReputationBadgeWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify

class ReputationBadgeViewHolder(itemView: View?,
                                private val onReputationBadgeClicked: () -> Unit,
                                private val onErrorClicked: () -> Unit) :
    AbstractViewHolder<ReputationBadgeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_badge
    }

    private val shopBadgeImageView: ImageUnify? =
        itemView?.findViewById(R.id.iv_sah_new_other_shop_badge)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_shop_badge)
    private val shimmerLoading: LoaderTextView? =
        itemView?.findViewById(R.id.shimmer_sah_new_other_shop_badge)

    override fun bind(element: ReputationBadgeWidgetUiModel) {
        when(val state = element.state) {
            is SettingResponseState.SettingSuccess -> showBadge(state.data)
            is SettingResponseState.SettingError -> showError()
            else -> showLoading()
        }
    }

    private fun showBadge(badgeUrl: String) {
        shopBadgeImageView?.run {
            show()
            setImageUrl(badgeUrl)
            setOnClickListener {
                onReputationBadgeClicked()
            }
        }
        errorLayout?.gone()
        shimmerLoading?.gone()
    }

    private fun showError() {
        shopBadgeImageView?.gone()
        errorLayout?.run {
            show()
            setOnClickListener {
                onErrorClicked()
            }
        }
        shimmerLoading?.gone()
    }

    private fun showLoading() {
        shopBadgeImageView?.gone()
        errorLayout?.gone()
        shimmerLoading?.show()
    }
}