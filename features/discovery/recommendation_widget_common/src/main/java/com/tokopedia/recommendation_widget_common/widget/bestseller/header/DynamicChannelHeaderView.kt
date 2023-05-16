package com.tokopedia.recommendation_widget_common.widget.bestseller.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.recommendation_widget_common.R

internal class DynamicChannelHeaderView : FrameLayout {
    private val itemView: View

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.home_dynamic_channel_header_revamp, this)
        this.itemView = view
    }

    private val channelTitle: Typography by lazy { itemView.findViewById(R.id.channel_title) }
    private val channelSubtitle: Typography by lazy { itemView.findViewById(R.id.channel_subtitle) }
    private val ctaButtonRevampContainer: LinearLayout by lazy { itemView.findViewById(R.id.cta_border_revamp) }
    private val ctaButtonRevamp: IconUnify by lazy { itemView.findViewById(R.id.cta_button_revamp) }
    private val channelHeaderContainer: ConstraintLayout by lazy { itemView.findViewById(R.id.channel_header_container) }

    fun setHeader(
        title: String?,
        subtitle: String?,
        seeAllApplink: String?,
        onSeeAllClick: () -> Unit
    ) {
        setTitle(title)
        setSubtitle(subtitle)
        setCta(subtitle, seeAllApplink, onSeeAllClick)
    }
    
    private fun setTitle(title: String?) {
        if (title?.isNotEmpty() == true) {
            channelTitle.text = title
            channelTitle.show()
        } else {
            channelHeaderContainer.hide()
        }
    }

    private fun setSubtitle(subtitle: String?) {
        if (subtitle?.isNotEmpty() == true) {
            channelSubtitle.text = subtitle
            channelSubtitle.show()
        } else {
            channelSubtitle.hide()
        }
    }
    
    private fun setCta(
        subtitle: String?,
        seeAllApplink: String?,
        onSeeAllClick: () -> Unit
    ) {
        if(seeAllApplink?.isNotEmpty() == true) {
            ctaButtonRevamp.setImage(
                newIconId = IconUnify.CHEVRON_RIGHT,
                newLightEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900),
                newDarkEnable = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            )
            val bottomAnchor = if (subtitle?.isEmpty() == true) {
                R.id.channel_title
            } else {
                R.id.channel_subtitle
            }
            val constraintSet = ConstraintSet()
            constraintSet.clone(channelHeaderContainer)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.TOP, R.id.channel_title, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.see_all_button, ConstraintSet.BOTTOM, bottomAnchor, ConstraintSet.BOTTOM, 0)
            constraintSet.applyTo(channelHeaderContainer)

            ctaButtonRevampContainer.show()
            ctaButtonRevamp.setOnClickListener {
                onSeeAllClick.invoke()
            }   
        } else {
            ctaButtonRevampContainer.hide()
        }
    }
}

