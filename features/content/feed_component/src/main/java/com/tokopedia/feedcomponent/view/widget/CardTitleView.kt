package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Action
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by milhamj on 02/01/19.
 */
class CardTitleView : BaseCustomView {

    companion object {
        const val ACTION_POPUP = "popuptopads"
        const val ACTION_REDIRECT = "redirectpage"
    }

    var listener: CardTitleListener? = null

    private val titleLayout: LinearLayout = findViewById(R.id.titleLayout)
    private val text: Typography = findViewById(R.id.text)
    private val badge: ImageView = findViewById(R.id.badge)
    private val cta: Typography = findViewById(R.id.cta)

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

    fun bind(title: Title, template: TemplateTitle, adapterPosition: Int) {
        titleLayout.shouldShowWithAction(shouldShowTitle(title, template)) {
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
                    listener?.onTitleCtaClick(title.ctaLink.appLink, adapterPosition)
                }
            }
        }
    }

    private fun shouldShowTitle(title: Title, template: TemplateTitle): Boolean {
        return (template.text || template.textBadge || template.ctaLink) && (title.text.isNotEmpty())
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

        fun onTitleCtaClick(redirectUrl: String, adapterPosition: Int)
    }
}
