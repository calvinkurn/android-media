package com.tokopedia.home.account.presentation.viewholder

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.MenuGridIconNotificationItemViewModel
import com.tokopedia.iconnotification.IconNotification
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 15/07/20.
 */
class MenuGridIconNotificationItemViewHolder(private val context: Context, itemView: View, private val listener: AccountItemListener?) : RecyclerView.ViewHolder(itemView) {
    private val rlCategoryGrid: ConstraintLayout = itemView.findViewById(R.id.cl_category_grid)
    private val iconNotification: IconNotification = itemView.findViewById(R.id.icon_notification)
    private val iconDesc: Typography = itemView.findViewById(R.id.icon_desc)

    fun bind(menuItem: MenuGridIconNotificationItemViewModel) {
        val param = LinearLayout.LayoutParams(40, 40)
        val backgroundImgView = ImageView(context)
        backgroundImgView.background = ContextCompat.getDrawable(context, R.drawable.ic_uoh_all_transactions)
        backgroundImgView.layoutParams = param
        iconNotification.apply {
                imageDrawable = ContextCompat.getDrawable(context, menuItem.resourceId)
                setSize(40, 40)

                if (menuItem.count > 0) {
                    notificationRef.visible()
                    notificationGravity = Gravity.TOP or Gravity.END
                    (notificationRef.parent as FrameLayout).setBackgroundColor(androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                    notificationRef.setNotification(menuItem.count.toString(), NotificationUnify.COUNTER_TYPE, NotificationUnify.COLOR_TEXT_TYPE)
                } else {
                    notificationRef.gone()
                }
            }
        iconDesc.text = menuItem.description
        if (listener != null) rlCategoryGrid.setOnClickListener { listener.onMenuGridBackgroundItemClicked(menuItem) }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_menu_grid_item_with_bg
    }

}