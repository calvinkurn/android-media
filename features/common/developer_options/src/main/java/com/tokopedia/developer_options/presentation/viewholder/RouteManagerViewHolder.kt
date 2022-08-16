package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.applink.presentation.activity.AppLinkListActivity
import com.tokopedia.developer_options.presentation.model.RouteManagerUiModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class RouteManagerViewHolder(
    itemView: View
): AbstractViewHolder<RouteManagerUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_route_manager
    }

    override fun bind(element: RouteManagerUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.route_manager_btn)
        val tf = itemView.findViewById<TextFieldUnify>(R.id.route_manager_tf)
        val appLinkListBtn = itemView.findViewById<UnifyButton>(R.id.view_applink_list_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                val routeManagerString = tf.textFieldInput.text.toString()
                if (routeManagerString.isBlank()) {
                    val appLink = ApplinkConstInternalTokopediaNow.RECIPE_DETAIL
                    RouteManager.route(this, appLink, "1")
                } else {
                    RouteManager.route(this, routeManagerString)
                }
            }
        }
        appLinkListBtn.setOnClickListener {
            itemView.context.startActivity(AppLinkListActivity.newInstance(it.context))
        }
    }
}