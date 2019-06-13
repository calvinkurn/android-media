package com.tokopedia.mlp.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.merchant_lending_widget.R
import com.tokopedia.mlp.contractModel.BoxesItem

class MLPBoxAdapter(private val boxList: MutableList<BoxesItem>, val context:Context) : RecyclerView.Adapter<MLPBoxAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewContainer: View = view.findViewById(R.id.viewbody_background)
        var textBox: TextView = view.findViewById(R.id.text_body_content)
        var titleBox: TextView = view.findViewById(R.id.text_body_title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MLPBoxAdapter.ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mlp_box_layout, parent, false))

    }

    override fun getItemCount(): Int {
        return boxList.size
    }

    override fun onBindViewHolder(holder: MLPBoxAdapter.ViewHolder, position: Int) {

        holder.textBox.text = boxList[0].text
        holder.titleBox.text = boxList[0].title
        holder.viewContainer.visibility = View.VISIBLE


        val boxcolor: String
        if (boxList[0].boxColor!=null) {
            boxcolor = boxList[0].boxColor!!
            val unwrappedDrawableBody = AppCompatResources.getDrawable( context,R.drawable.bg_body)
            val wrappedDrawableBody = DrawableCompat.wrap(unwrappedDrawableBody!!)
            DrawableCompat.setTint(wrappedDrawableBody, Color.parseColor(boxcolor))

        }
    }

}