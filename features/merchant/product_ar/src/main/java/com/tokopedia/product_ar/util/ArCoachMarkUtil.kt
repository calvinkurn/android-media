package com.tokopedia.product_ar.util

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.product_ar.R

object ArCoachMarkUtil {

    fun buildCoachMark(context: Context,
                       rvVariant: RecyclerView?,
                       animatedTxtIcon2: AnimatedTextIcon?,
                       icCompareAr: ImageView?): ArrayList<CoachMark2Item> {
        val coachMarkList = arrayListOf<CoachMark2Item>()

        rvVariant?.apply {
            val title1 = context.getString(R.string.coachmark_1_title_ar)
            val desc1 = context.getString(R.string.coachmark_1_desc_ar)
            coachMarkList.add(CoachMark2Item(this,
                    title1, position = CoachMark2.POSITION_TOP, description = desc1))
        }

        animatedTxtIcon2?.getIconInstanceView()?.run {
            val title2 = context.getString(R.string.coachmark_2_title_ar)
            val desc2 = context.getString(R.string.coachmark_2_desc_ar)
            coachMarkList.add(CoachMark2Item(this,
                    title2, position = CoachMark2.POSITION_TOP, description = desc2))
        }

        icCompareAr?.run {
            val title3 = context.getString(R.string.coachmark_3_title_ar)
            val desc3 = context.getString(R.string.coachmark_3_desc_ar)
            coachMarkList.add(CoachMark2Item(this,
                    title3, position = CoachMark2.POSITION_TOP, description = desc3))
        }

        return coachMarkList
    }
}