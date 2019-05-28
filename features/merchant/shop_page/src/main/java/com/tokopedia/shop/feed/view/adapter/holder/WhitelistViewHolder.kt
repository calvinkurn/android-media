package com.tokopedia.shop.feed.view.adapter.holder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.WhitelistViewModel
import kotlinx.android.synthetic.main.item_post_entry.view.*

/**
 * @author by yfsx on 16/05/19.
 */
class WhitelistViewHolder(v: View,
                          private val mainView: FeedShopContract.View)
    : AbstractViewHolder<WhitelistViewModel>(v) {

    private val FORMAT_NAME = "{{name}}"

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_post_entry
    }

    override fun bind(element: WhitelistViewModel) {
        initView(element)
        initViewListener(element)
    }

    private fun initView(model: WhitelistViewModel) {
        itemView.tvCaption.text = MethodChecker.fromHtml(formatWhiteListTitle(
                model.whitelist.title))

        ImageHandler.loadImageCircle2(
                itemView.ivAvatar.context,
                itemView.ivAvatar,
                model.whitelist.image
        )
    }

    private fun formatWhiteListTitle(title: String): String {
        return title
                .replace(FORMAT_NAME, bold(FORMAT_NAME))
                .replace(FORMAT_NAME, if (mainView.getUserSession().hasShop())
                    mainView.getUserSession().getShopName()
                else
                    mainView.getUserSession().getName())
    }

    private fun bold(text: String): String {
        return String.format("<b>%s</b>", text)
    }

    private fun initViewListener(element: WhitelistViewModel) {
        itemView.btnCreatePost.setOnClickListener { view -> mainView.onWhitelistClicked(element) }
    }
}