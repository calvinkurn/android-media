package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestWidgetAllClaimedBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeQuestAllClaimedWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestAllClaimedWidgetViewHolder(
    itemView: View,
    private val listener: HomeQuestSequenceWidgetViewHolder.HomeQuestSequenceWidgetListener? = null
): AbstractViewHolder<HomeQuestAllClaimedWidgetUiModel>(itemView) {

    companion object {
        private const val APPLINK_COUPON = "tokopedia://rewards/kupon-saya"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_widget_all_claimed
    }

    private var binding: ItemTokopedianowQuestWidgetAllClaimedBinding? by viewBinding()

    override fun bind(element: HomeQuestAllClaimedWidgetUiModel) {
        setupUi(element)
    }

    private fun setupUi(element: HomeQuestAllClaimedWidgetUiModel) {
        setIcon()
        setCloseBtn(element.id)
        setText(element.currentQuestFinished, element.totalQuestTarget)
    }

    private fun setIcon() {
        binding?.apply {
            iuGift.setImage(newIconId = IconUnify.GIFT)
            iuGift.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White), BlendModeCompat.SRC_ATOP)
        }
    }

    private fun setCloseBtn(id: String) {
        binding?.apply {
            btnClose.setImage(newIconId = IconUnify.CLOSE)
            btnClose.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900), BlendModeCompat.SRC_ATOP)
            btnClose.setOnClickListener {
                listener?.onCloseQuestAllClaimedBtnClicked(id)
            }
        }
    }

    private fun setText(currentFinished: Int, totalQuestTarget: Int) {
        binding?.apply {
            tpCounter.text = String.format("$currentFinished/$totalQuestTarget")
            tpTitle.text = MethodChecker.fromHtml(getString(R.string.tokopedianow_quest_widget_all_claimed_title))
            tpDesc.text = getString(R.string.tokopedianow_quest_widget_all_claimed_desc)
            tpDesc.setOnClickListener {
                listener?.onClickCheckReward()
                RouteManager.route(itemView.context, APPLINK_COUPON)
            }
        }
    }

}