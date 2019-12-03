package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.homepage.data.UmrahCategories
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.salam.umrah.search.presentation.activity.UmrahSearchActivity
import kotlinx.android.synthetic.main.widget_umrah_homepage_package.view.*

/**
 * @author by firman on 22/10/19
 */

class UmrahHomepageChoosePacketAdapter(val onBindListener: onItemBindListener): RecyclerView.Adapter<UmrahHomepageChoosePacketAdapter.UmrahHomepageChoosePacketViewHolder>(){
    private var listCategories = emptyList<UmrahCategories>()

    inner class UmrahHomepageChoosePacketViewHolder (view: View): RecyclerView.ViewHolder(view){

        fun bind(categories:UmrahCategories) {
            with(itemView) {
                container_umrah_homepage_package_shimmering.visibility = View.GONE
                tg_umrah_package_name.text = categories.title
                tg_umrah_price.text = getRupiahFormat(categories.startingPrice.toInt())
                container_umrah_homepage_package.visibility = View.VISIBLE
                container_umrah_homepage_package.setOnClickListener {
                    onBindListener.onClickCategory(categories, position)
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