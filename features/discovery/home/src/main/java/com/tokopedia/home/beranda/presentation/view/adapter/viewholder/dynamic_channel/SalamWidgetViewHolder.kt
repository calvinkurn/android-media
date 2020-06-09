package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.SalamWidgetDataModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.home_recharge_recommendation_item.view.*


class SalamWidgetViewHolder(
        itemView: View,
        private val listener: SalamWidgetListener,
        private val categoryListener: HomeCategoryListener
) : AbstractViewHolder<SalamWidgetDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_recommendation_item
    }

    override fun bind(element: SalamWidgetDataModel) {
        with(itemView) {
            if (element.salamWidget.salamWidget.appLink.isNotEmpty()) {
                home_recharge_recommendation_loading.show()
            } else {
                home_recharge_recommendation_loading.hide()

                val salamWidget = element.salamWidget.salamWidget
                if (salamWidget.backgroundColor.isNotEmpty()) {
                    try {
                        recharge_recommendation_widget_container.setBackgroundColor(Color.parseColor(salamWidget.backgroundColor))
                    } catch (e: Exception) {
                    }
                }
                if (salamWidget.title.isNotEmpty()) {
                    recharge_recommendation_title.text = salamWidget.title
                }
                ic_recharge_recommendation_product.loadImage(salamWidget.iconURL)
                recharge_recommendation_text_main.text = MethodChecker.fromHtml(salamWidget.mainText)
                recharge_recommendation_text_sub.text = MethodChecker.fromHtml(salamWidget.subText)

                if (salamWidget.buttonText.isNotEmpty()) {
                    btn_recharge_recommendation.text = salamWidget.buttonText
                }
                btn_recharge_recommendation.setOnClickListener {
                    categoryListener.getTrackingQueueObj()?.let {

                    }

                    listener.onSalamWidgetClickListener(salamWidget.appLink)


                }

                addOnImpressionListener(element, object : ViewHintListener {
                    override fun onViewHint() {
                        categoryListener.getTrackingQueueObj()?.let {

                        }
                    }
                })

                ic_close_recharge_recommendation.setOnClickListener {

                }
            }
        }
    }

    interface SalamWidgetListener {
        fun onSalamWidgetClickListener(applink: String)
    }
}