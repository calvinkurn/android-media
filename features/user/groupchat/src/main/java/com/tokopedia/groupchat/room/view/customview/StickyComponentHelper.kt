package com.tokopedia.groupchat.room.view.customview

import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.kotlin.extensions.view.hide

/**
 * @author by nisie on 25/02/19.
 */
class StickyComponentHelper {

    companion object {

        fun setView(view: View, stickyComponentViewModel: StickyComponentViewModel) {
            val title = view.findViewById<TextView>(R.id.tv_title)
            val subtitle = view.findViewById<TextView>(R.id.tv_subtitle)
            val image = view.findViewById<ImageView>(R.id.iv_image)
            val byMeIcon = view.findViewById<ImageView>(R.id.iv_byme)

            when (stickyComponentViewModel.componentType.toLowerCase()) {
                StickyComponentViewModel.TYPE_PRODUCT -> setProduct(stickyComponentViewModel,
                        title, subtitle, image, byMeIcon)
            }
        }

        private fun setProduct(stickyComponentViewModel: StickyComponentViewModel, title: TextView, subtitle: TextView, image: ImageView, byMeIcon: ImageView) {
            byMeIcon.hide()

            ImageHandler.LoadImage(image, stickyComponentViewModel.imageUrl)

            title.text = MethodChecker.fromHtml(stickyComponentViewModel.title)
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
            subtitle.setTypeface(null, Typeface.NORMAL)
            title.setTextColor(MethodChecker.getColor(title.context, R.color.black_70))

            subtitle.text = MethodChecker.fromHtml(stickyComponentViewModel.subtitle)
            subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
            subtitle.setTypeface(null, Typeface.BOLD)
            subtitle.setTextColor(MethodChecker.getColor(title.context, R.color.orange_red))

        }
    }
}