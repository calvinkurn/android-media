package com.tokopedia.mvcwidget.quest_widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.mvcwidget.R
import com.tokopedia.unifycomponents.UnifyButton
import kotlin.coroutines.coroutineContext

class QuestWidgetAdapter(val data: ArrayList<QuestWidgetListItem>, val isHiddenCta: Boolean) : RecyclerView.Adapter<QuestWidgetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestWidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quest_widget_card_item, parent, false)
        return QuestWidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestWidgetViewHolder, position: Int) {
        holder.setData(data.get(position))
        if(isHiddenCta){
            holder.btnActionButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class QuestWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val tvBannerTitle: TextView = itemView.findViewById(R.id.tv_banner_title)
    val tvBannerDesc: TextView = itemView.findViewById(R.id.tv_banner_desc)
    val tvLabel: TextView = itemView.findViewById(R.id.tv_label)
    val viewSideBar: View = itemView.findViewById(R.id.view_side_bar)
    val ivBannerIcon: ImageView = itemView.findViewById(R.id.iv_banner_icon)
    val btnActionButton: UnifyButton = itemView.findViewById(R.id.btn_actionButton)

    fun setData(item: QuestWidgetListItem){
        val config = convertStringToConfig(item.config)
        tvLabel.text = item.label?.title
        tvBannerTitle.text = config.banner_title
        tvBannerDesc.text = config.banner_description
        Glide.with(itemView.context)
            .load(config.banner_icon_url)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .centerCrop()
            .into(ivBannerIcon)
        viewSideBar.setBackgroundColor(Color.parseColor(config.banner_background_color))

        when(item.questUser?.status){
            "Idle" -> {
                btnActionButton.text = item.actionButton?.shortText
            }
            "On Progress" ->{
                btnActionButton.text = item.actionButton?.shortText + "${(item.task?.get(0)?.progress?.current?.let {
                    item.task?.get(0)?.progress?.target?.minus(
                        it
                    )
                })}"
            }
            "Completed" ->{
                btnActionButton.text = item.actionButton?.shortText
            }
            "Claimed" ->{
                btnActionButton.text = item.actionButton?.shortText
            }
        }

    }

    private fun convertStringToConfig(configString: String?) : Config{
        val dataClassType = object : TypeToken<Config>() {}.type
        return Gson().fromJson(configString, dataClassType)
    }
}