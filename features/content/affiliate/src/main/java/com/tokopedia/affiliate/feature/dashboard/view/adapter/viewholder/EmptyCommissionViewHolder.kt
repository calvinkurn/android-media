package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewholder

import android.content.Context
import android.os.Build
import androidx.annotation.LayoutRes
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.EmptyCommissionViewModel
import kotlinx.android.synthetic.main.item_af_commission_empty.view.*

/**
 * @author by yoasfs on 2019-08-15
 */
class EmptyCommissionViewHolder (v: View) : AbstractViewHolder<EmptyCommissionViewModel>(v) {

    companion object {
        val VALUE_CARD_SIZE = 0.60
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_af_commission_empty
    }
    override fun bind(element: EmptyCommissionViewModel) {
        itemView.container.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = itemView.container.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        val displayMetrics = DisplayMetrics()
                        (itemView.context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.let {
                            it.defaultDisplay.getMetrics(displayMetrics)
                            itemView.container.layoutParams.height = (displayMetrics.heightPixels * VALUE_CARD_SIZE).toInt()
                            itemView.container.requestLayout()
                        }
                    }
                }
        )
        ImageHandler.LoadImage(itemView.iv_empty, element.iconRes)
    }
}