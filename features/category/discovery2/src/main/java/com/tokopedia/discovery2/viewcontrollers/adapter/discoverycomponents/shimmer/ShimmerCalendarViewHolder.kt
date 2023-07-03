package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.CardUnify
import kotlin.math.roundToInt

class ShimmerCalendarViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private var shimmerCalendarViewModel: ShimmerViewModel? = null
    private var parentLayout: CardUnify = itemView.findViewById(R.id.parentLayout)
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        shimmerCalendarViewModel = discoveryBaseViewModel as ShimmerViewModel
        setProductShimmerView()
    }

    private fun setProductShimmerView() {
        val layoutParams: ViewGroup.LayoutParams = parentLayout.layoutParams
        val width = Resources.getSystem().displayMetrics.widthPixels
        when (shimmerCalendarViewModel?.getCalendarLayout()) {
            Constant.Calendar.SINGLE -> {
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar).gone()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar_single).show()
            }
            Constant.Calendar.CAROUSEL -> {
                layoutParams.width = (width / 2.5).roundToInt()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar).show()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar_single).gone()
            }
            Constant.Calendar.DOUBLE, Constant.Calendar.GRID -> {
                layoutParams.width =
                    ((width) / 2)
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar).show()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar_single).gone()
            }
            Constant.Calendar.TRIPLE -> {
                layoutParams.width = ((width) / 3)
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar).show()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar_single).gone()
            }
            else -> {
                layoutParams.width = (width / 2.5).roundToInt()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar).show()
                itemView.findViewById<ConstraintLayout>(R.id.constraint_layout_calendar_single).gone()
            }
        }
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        parentLayout.layoutParams = layoutParams
    }
}
