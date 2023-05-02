package com.tokopedia.topads.view.adapter.adgrouplist.viewholder

import android.graphics.Typeface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.constants.SpanConstant
import com.tokopedia.topads.create.R
import com.tokopedia.topads.utils.Span
import com.tokopedia.topads.utils.SpannableUtils
import com.tokopedia.topads.utils.SpannedString
import com.tokopedia.topads.view.adapter.adgrouplist.model.ReloadInfiniteUiModel
import com.tokopedia.unifyprinciples.Typography

class ReloadInfiniteViewHolder(itemView:View,private val listener:ReloadInfiniteScrollListener?) : AbstractViewHolder<ReloadInfiniteUiModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.ad_group_infinite_load_viewholder_layout
        private const val RELOAD_TEXT = "Muat Ulang"
    }

    private var reloadTv:Typography? = null

    init {
      reloadTv = itemView.findViewById(R.id.infinite_reload_text)
        setupReloadText()
    }

    private fun setupReloadText(){
        val reloadString = itemView.context.resources.getString(R.string.mp_ad_creation_grp_list_infinite_reload_text)
        val ctaColor = ResourcesCompat.getColor(itemView.context.resources,com.tokopedia.unifyprinciples.R.color.Unify_GN500,null)
        reloadTv?.movementMethod = LinkMovementMethod.getInstance()
        reloadTv?.text = SpannableUtils.applySpannable(
            reloadString,
            SpannedString(RELOAD_TEXT, listOf(
                Span(SpanConstant.COLOR_SPAN,ctaColor),
                Span(SpanConstant.TYPEFACE_SPAN,Typeface.BOLD),
                Span(SpanConstant.CLICK_SPAN,{listener?.onReload()})
            ))
        )
    }

    override fun bind(element: ReloadInfiniteUiModel?) {}

    interface ReloadInfiniteScrollListener{
        fun onReload()
    }
}
