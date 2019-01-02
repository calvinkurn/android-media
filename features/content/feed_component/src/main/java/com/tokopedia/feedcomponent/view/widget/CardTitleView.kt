package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateTitle
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.partial_card_title.view.*

/**
 * @author by milhamj on 02/01/19.
 */
class CardTitleView : BaseCustomView {

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
        val view = View.inflate(context, R.layout.partial_card_title, this)
    }

    fun bind(title: Title, template: TemplateTitle) {
        text.shouldShowWithAction(template.text) {
            text.text = title.text
            text.setOnClickListener { onTextClick() }
        }
        badge.shouldShowWithAction(template.textBadge) {
            badge.loadImage(title.textBadge)
            badge.setOnClickListener { onTextClick() }
        }
        cta.shouldShowWithAction(template.ctaLink) {
            cta.text = title.ctaLink.text
            cta.setOnClickListener {
                //TODO milhamj
                Toast.makeText(context, "Title CTA clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onTextClick() {
        //TODO milhamj
        Toast.makeText(context, "Title Text clicked", Toast.LENGTH_LONG).show()
    }
}