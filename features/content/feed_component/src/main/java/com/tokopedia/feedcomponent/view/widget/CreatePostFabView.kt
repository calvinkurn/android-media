package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_create_post_fab.view.*

/**
 * @author by milhamj on 2019-09-14.
 */
class CreatePostFabView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var onFabBymeClicked: (() -> Unit)? = null

    var onFabLinkClicked: ((String) -> Unit)? = null

    private var isFabExpanded: Boolean = false

    init {
        View.inflate(context, R.layout.layout_create_post_fab, this)
        hideAllFab(true)
    }

    fun render(whitelist: Whitelist) {
        if (whitelist.authors.isEmpty()) {
            return
        }

        fab_feed.show()
        if (whitelist.authors.size > 1) {
            fab_feed.setOnClickListener(fabClickListener(whitelist))
        } else if (whitelist.authors.size == 1) {
            val author = whitelist.authors.first()
            fab_feed.setOnClickListener { onFabLinkClicked?.invoke(author.link) }
        }
    }

    fun hideAllFab(isInitial: Boolean = false) {
        if (context == null) {
            return
        }

        if (isInitial) {
            fab_feed.hide()
        } else {
            fab_feed.animation = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)
        }

        fab_feed_byme.hide()
        fab_feed_shop.hide()
        text_fab_byme.hide()
        text_fab_shop.hide()
        layout_grey_popup.hide()
        isFabExpanded = false
    }

    private fun fabClickListener(whitelistDomain: Whitelist): OnClickListener {
        return OnClickListener {
            if (isFabExpanded) {
                hideAllFab(false)
            } else {
                if (context != null) {
                    fab_feed.animation = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
                }

                layout_grey_popup.show()
                for (author in whitelistDomain.authors) {
                    if (author.type.equals(Author.TYPE_AFFILIATE, ignoreCase = true)) {
                        fab_feed_byme.show()
                        text_fab_byme.show()
                        text_fab_byme.text = author.title
                        fab_feed_byme.setOnClickListener { onFabBymeClicked?.invoke() }
                    } else {
                        fab_feed_shop.show()
                        text_fab_shop.show()
                        text_fab_shop.text = author.title
                        fab_feed_shop.setOnClickListener { onFabLinkClicked?.invoke(author.link) }
                    }
                }
                layout_grey_popup.setOnClickListener { hideAllFab(false) }
                isFabExpanded = true
            }
        }
    }
}