package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_umrah_homepage_package.view.*

/**
 * @author by firman on 22/10/19
 */

class UmrahHomepageChoosePacketAdapter(val trackingUmrahUtil: TrackingUmrahUtil): RecyclerView.Adapter<UmrahHomepageChoosePacketAdapter.UmrahHomepageChoosePacketViewHolder>(){
    private var listCategories = emptyList<UmrahCategories>()

    inner class UmrahHomepageChoosePacketViewHolder (view: View): RecyclerView.ViewHolder(view){

        fun bind(categories:UmrahCategories) {
            with(itemView) {
                container_umrah_homepage_package_shimmering.visibility = View.GONE
                tg_umrah_package_name.text = categories.title
                tg_umrah_price.text = "Rp${categories.startingPrice}"
                container_umrah_homepage_package.visibility = View.VISIBLE
                container_umrah_homepage_package.setOnClickListener {
                    trackingUmrahUtil.umrahClickCategoryTracker(categories, position)
                    context?.let {
                        context.startActivity(
                                UmrahSearchActivity.createIntent(it,
                                        categories.title,
                                        categories.slugName
                                       ))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahHomepageChoosePacketViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepageChoosePacketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.widget_umrah_homepage_package, parent, false)
        return UmrahHomepageChoosePacketViewHolder(itemView)
    }

    fun setList(list: List<UmrahCategories>){
        listCategories = list
        notifyDataSetChanged()
    }

}