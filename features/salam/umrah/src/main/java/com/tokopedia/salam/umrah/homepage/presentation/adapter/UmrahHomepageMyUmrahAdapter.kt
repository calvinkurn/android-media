package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.widget_umrah_my_umrah.view.*

class UmrahHomepageMyUmrahAdapter(val onItemBindListener: onItemBindListener) : RecyclerView.Adapter<UmrahHomepageMyUmrahAdapter.UmrahHomepageUmrahSayaViewHolder>() {
    private var listCategories = emptyList<MyUmrahEntity>()


    inner class UmrahHomepageUmrahSayaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(element: MyUmrahEntity) {
            with(itemView) {
                iv_my_umrah_loading.visibility = View.GONE
                tg_umrah_package_loading.visibility = View.GONE
                tg_umrah_departure_loading.visibility = View.GONE
                tg_umrah_next_label_loading.visibility = View.GONE
                tg_umrah_next_loading.visibility = View.GONE
                btn_my_umrah_detail_loading.visibility = View.GONE

                tg_umrah_package.text = element.subHeader
                tg_umrah_departure.text = element.header
                tg_umrah_next.text = element.nextActionText
                btn_my_umrah_detail.text = element.mainButton.text
                container_umrah_widget_my_umrah.setOnClickListener {
                    onItemBindListener.onClickUmrahMyUmrah(element.nextActionText, element, position)
                    RouteManager.route(context, element.mainButton.link)
                }
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahHomepageUmrahSayaViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepageUmrahSayaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.widget_umrah_my_umrah, parent, false)
        if (listCategories.size == 1) {
            itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return UmrahHomepageUmrahSayaViewHolder(itemView)
    }

    fun setList(list: List<MyUmrahEntity>) {
        listCategories = list
        notifyDataSetChanged()
    }

}