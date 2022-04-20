package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.startActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.activity.TokomemberDashCreateCardActivity
import com.tokopedia.tokomember_seller_dashboard.view.adapter.TokomemberIntroAdapterListener
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroButtonItem
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroTextItem
import kotlinx.android.synthetic.main.tm_dash_intro.view.*
import kotlinx.android.synthetic.main.tm_dash_intro_button_item.*
import kotlinx.android.synthetic.main.tm_dash_intro_button_item.view.*
import kotlinx.android.synthetic.main.tm_dash_intro_text_item.view.*

class TokomemberIntroButtonVh(val listener: TokomemberIntroAdapterListener, view: View)
    : AbstractViewHolder<TokomemberIntroButtonItem>(view) {

    private val tvSectionText = itemView.btnContinue

    override fun bind(element: TokomemberIntroButtonItem?) {
        element?.apply {
            tvSectionText.text = element.text
        }
        tvSectionText.setOnClickListener {
            itemView.context.startActivity(Intent(itemView.context, TokomemberDashCreateCardActivity::class.java))
        }
        if (element != null) {
            listener.onItemDisplayed(element,adapterPosition)
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.tm_dash_intro_button_item
    }

}
