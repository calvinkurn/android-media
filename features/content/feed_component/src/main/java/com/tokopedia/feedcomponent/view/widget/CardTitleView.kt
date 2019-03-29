package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Action
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.partial_card_title.view.*

/**
 * @author by milhamj on 02/01/19.
 */
class CardTitleView : BaseCustomView {

    companion object {
        const val ACTION_POPUP = "popuptopads"
        const val ACTION_REDIRECT = "redirectpage"
    }

    var listener: CardTitleListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.partial_card_title, this)
    }

    fun bind(title: Title, template: TemplateTitle) {
        titleLayout.shouldShowWithAction(shouldShowTitle(template)) {
            text.shouldShowWithAction(template.text) {
                text.text = title.text
                text.setOnClickListener { onTextClick(title.action) }
            }
            badge.shouldShowWithAction(template.textBadge
                    && !TextUtils.isEmpty(title.textBadge)) {
                badge.loadImage(title.textBadge)
                badge.setOnClickListener { onTextClick(title.action) }
            }
            cta.shouldShowWithAction(template.ctaLink) {
                cta.text = title.ctaLink.text
                cta.setOnClickListener {
                    listener?.onTitleCtaClick(title.ctaLink.appLink)
                }
            }
        }
    }

    private fun shouldShowTitle(template: TemplateTitle): Boolean {
        return template.text || template.textBadge || template.ctaLink
    }

    private fun onTextClick(action: Action) {
        when(action.action.toLowerCase()) {
            ACTION_POPUP -> listener?.onActionPopup()
            ACTION_REDIRECT -> listener?.onActionRedirect(action.appLink)
        }
    }

    interface CardTitleListener {
        fun onActionPopup()

        fun onActionRedirect(redirectUrl: String)

        fun onTitleCtaClick(redirectUrl: String)
    }
}