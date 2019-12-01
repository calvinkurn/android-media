package com.tokopedia.gamification.pdp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.presentation.viewHolders.RecommendationVH
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

//class PdpGamificationAdapter(val typeFactory: PdpGamificationAdapterTypeFactory,
//                             visitables: List<Visitable<Any>>) :
//        BaseAdapter<PdpGamificationAdapterTypeFactory>(typeFactory, visitables) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
//        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
//        return typeFactory.createViewHolder(view, viewType)
//
//    }
//
//    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
//        super.onBindViewHolder(holder, position)
//    }
//
//}
class PdpGamificationAdapter(val visitables: List<Visitable<*>>, val recommendationListener: RecommendationListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(RecommendationVH.LAYOUT, parent, false)
        return RecommendationVH(v, recommendationListener)
    }

    override fun getItemCount() = visitables.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecommendationVH){
//            holder.bind(visitables[position] as Visitable<Recommendation>)
        }
    }


}