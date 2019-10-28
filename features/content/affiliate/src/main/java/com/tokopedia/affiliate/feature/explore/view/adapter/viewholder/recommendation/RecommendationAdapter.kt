package com.tokopedia.affiliate.feature.explore.view.adapter.viewholder.recommendation

import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract
import kotlinx.android.synthetic.main.item_af_recommendation_child.view.*

/**
 * @author by milhamj on 14/03/19.
 */
class RecommendationAdapter(private val mainView: ExploreContract.View)
    : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    val list: MutableList<ExploreCardViewModel> = arrayListOf()
    var adapterPosition: Int = 0

    private val displayMetrics: DisplayMetrics? by lazy {
        if (mainView.activity != null) {
            val tempDisplayMetrics = DisplayMetrics()
            mainView.activity.windowManager.defaultDisplay.getMetrics(tempDisplayMetrics)
            tempDisplayMetrics
        } else {
            null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_af_recommendation_child, null))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        val itemView = holder.itemView
        itemView.card.showTitle = false
        itemView.card.bind(element)
        itemView.card.setMainViewClickListener {
            mainView.onProductClicked(element, adapterPosition)
        }

        updateCardWidth(itemView)
    }

    private fun updateCardWidth(itemView: View) {
        displayMetrics?.let {
            val viewWidth = (it.widthPixels / 2.5).toInt()
            if (itemView.card.layoutParams.width != viewWidth) {
                itemView.card.layoutParams.width = viewWidth
                itemView.card.requestLayout()
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}