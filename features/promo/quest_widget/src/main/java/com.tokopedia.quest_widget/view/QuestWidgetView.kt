package com.tokopedia.quest_widget.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.quest_widget.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.QuestWidgetListItem
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton

class QuestWidgetView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ): CardUnify(context, attrs) {

    private var tvBannerTitle: TextView? = null
    private var tvBannerDesc: TextView? = null
    private var tvLabel: TextView? = null
    private var viewSideBar: View? = null
    private var ivBannerIcon: ImageView? = null
    private var btnActionButton: UnifyButton? = null

    init {

        View.inflate(context, R.layout.quest_widget_card_item, this)

        tvBannerTitle = findViewById(R.id.tv_banner_title)
        tvBannerDesc = findViewById(R.id.tv_banner_desc)
        tvLabel = findViewById(R.id.tv_label)
        viewSideBar = findViewById(R.id.view_side_bar)
        ivBannerIcon = findViewById(R.id.iv_banner_icon)
        btnActionButton = findViewById(R.id.btn_actionButton)
    }

    fun setData(item: QuestWidgetListItem){
        val config = convertStringToConfig(item.config)
        tvLabel?.text = item.label?.title
        tvBannerTitle?.text = config.banner_title
        tvBannerDesc?.text = config.banner_description
     /*   ivBannerIcon?.let {
            Glide.with(context)
                .load(config.banner_icon_url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .into(it)
        }*/
        viewSideBar?.setBackgroundColor(Color.parseColor(config.banner_background_color))

        when(item.questUser?.status){
            "Idle" -> {
                btnActionButton?.text = item.actionButton?.shortText
            }
            "On Progress" ->{
                btnActionButton?.text = item.actionButton?.shortText + "${(item.task?.get(0)?.progress?.current?.let {
                    item.task?.get(0)?.progress?.target?.minus(
                        it
                    )
                })}"
            }
            "Completed" ->{
                btnActionButton?.text = item.actionButton?.shortText
            }
            "Claimed" ->{
                btnActionButton?.text = item.actionButton?.shortText
            }
        }

    }

    private fun convertStringToConfig(configString: String?) : Config {
        val dataClassType = object : TypeToken<Config>() {}.type
        return Gson().fromJson(configString, dataClassType)
    }

}