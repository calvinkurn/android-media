package com.tokopedia.tokopedianow.home.presentation.viewholder.quest

import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowQuestFinishedBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestFinishedWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.utils.view.binding.viewBinding

class HomeQuestFinishedWidgetViewHolder(
    itemView: View
): AbstractViewHolder<HomeQuestFinishedWidgetUiModel>(itemView) {
    companion object {
        private const val APPLINK_COUPON = "tokopedia://rewards/kupon-saya"
        private const val ASSET_PATH_NEW_FONT = "OpenSauceOneExtraBold.ttf"
        private const val ASSET_PATH_OLD_FONT = "RobotoBold.ttf"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_quest_finished
    }

    private val binding: ItemTokopedianowQuestFinishedBinding? by viewBinding()

    override fun bind(element: HomeQuestFinishedWidgetUiModel) {
        binding?.apply {
            tpTitle.text = element.title
            tpDescription.setDescription(element.contentDescription)
            sivCircleSeeAll.setOnClickListener {
                RouteManager.route(itemView.context, APPLINK_COUPON)
            }
        }
    }

    private fun Typography.setDescription(contentDescription: String) {
        val hardcodedDescription = getString(R.string.tokopedianow_quest_finished_widget)
        val fullDescription = "$hardcodedDescription $contentDescription"
        val spannableText = SpannableString(fullDescription)
        val typeface = getTypeface(context, if (Typography.isFontTypeOpenSauceOne) ASSET_PATH_NEW_FONT else ASSET_PATH_OLD_FONT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && typeface != null) {
            spannableText.setSpan(
                TypefaceSpan(typeface),
                hardcodedDescription.length,
                fullDescription.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableText.setSpan(
                StyleSpan(Typeface.BOLD),
                hardcodedDescription.length,
                fullDescription.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        text = spannableText
    }
}
