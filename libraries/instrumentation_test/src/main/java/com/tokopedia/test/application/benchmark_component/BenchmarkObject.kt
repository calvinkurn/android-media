package com.tokopedia.test.application.benchmark_component

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView

object BenchmarkObject {
    fun simpleViewFromLayout(layoutRes: Int, activity: Activity): View {
        val viewGroup = FrameLayout(activity)
        return LayoutInflater.from(viewGroup.context)
                .inflate(layoutRes, viewGroup, false)
    }

    fun simpleAdapter(layoutRes: Int, onCreateView: (itemView: View) -> RecyclerView.ViewHolder): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(layoutRes, parent, false)
                return onCreateView.invoke(itemView)
            }

            override fun getItemCount(): Int {
                return 1
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            }
        }
    }
}