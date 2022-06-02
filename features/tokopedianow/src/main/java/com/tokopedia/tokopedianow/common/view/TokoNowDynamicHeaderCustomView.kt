package com.tokopedia.tokopedianow.common.view

import android.view.LayoutInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TokoNowDynamicHeaderCustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var listener: HeaderCustomViewListener? = null
    private var itemView: View?
    private var headerContainer: ConstraintLayout? = null
    private var tpSeeAll: Typography? = null
    private var tpTitle: Typography? = null
    private var tpSubtitle: Typography? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_tokopedianow_dynamic_header_custom_view, this)
        this.itemView = view
    }

    fun setModel(model: TokoNowDynamicHeaderUiModel, listener: HeaderCustomViewListener? = null) {
        this.listener = listener
        handleHeaderComponent(model)
    }

    private fun handleHeaderComponent(model: TokoNowDynamicHeaderUiModel) {
        setupUi()
        handleTitle(model.title)
        handleSubtitle(model.subTitle)
        handleSeeAllAppLink(model.ctaText, model.ctaTextLink)
    }

    private fun setupUi() {
        headerContainer = itemView?.findViewById(R.id.header_container)
        tpTitle = itemView?.findViewById(R.id.tp_title)
        tpSubtitle = itemView?.findViewById(R.id.tp_subtitle)
        tpSeeAll =  itemView?.findViewById(R.id.tp_see_all)
    }

    private fun handleTitle(title: String) {
        if (title.isNotBlank()) {
            headerContainer?.show()
            tpTitle?.text = title
            tpTitle?.show()
        } else {
            headerContainer?.gone()
        }
    }

    private fun handleSubtitle(subtitle: String) {
        if (subtitle.isNotBlank()) {
            tpSubtitle?.text = subtitle
        } else {
            tpSubtitle?.gone()
        }
    }

    private fun handleSeeAllAppLink(ctaText: String, ctaTextLink: String) {
        if (ctaTextLink.isNotBlank()) {
            tpSeeAll?.text = if (ctaText.isNotBlank()) {
                ctaText
            } else {
                itemView?.context?.getString(R.string.tokopedianow_mix_left_carousel_widget_see_all)
            }
            tpSeeAll?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            tpSeeAll?.setOnClickListener {
                listener?.onSeeAllClicked(ctaTextLink)
            }
            tpSeeAll?.show()
        } else {
            tpSeeAll?.hide()
        }
    }

    interface HeaderCustomViewListener {
        fun onSeeAllClicked(appLink: String)
    }
}