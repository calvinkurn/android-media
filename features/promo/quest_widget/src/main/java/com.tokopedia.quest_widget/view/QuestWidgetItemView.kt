package com.tokopedia.quest_widget.view

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.quest_widget.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.QuestWidgetListItem
import com.tokopedia.unifycomponents.CardUnify

class QuestWidgetItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ): CardUnify(context, attrs) {

    private var tvBannerTitle: TextView
    private var tvBannerDesc: TextView
    private var ivBannerIcon: ImageView
    val lagi = "x Lagi"

    init {

        View.inflate(context, R.layout.quest_widget_item_view, this)

        tvBannerTitle = findViewById(R.id.tv_banner_title)
        tvBannerDesc = findViewById(R.id.tv_banner_desc)
        ivBannerIcon = findViewById(R.id.iv_banner_icon)
    }

    fun setData(item: QuestWidgetListItem, config: Config){
        tvBannerTitle.text = config.banner_title
        ivBannerIcon.loadImage(config.banner_icon_url)

        val desc = item.actionButton?.shortText + (item.task?.get(0)?.progress?.current?.let {
            item.task[0]?.progress?.target?.minus(
                it
            )
        })


        // desc = Belanja 10x Lagi

        tvBannerDesc.text = desc + lagi + context.resources.getString(R.string.str_sisa) + item.label?.title
    }
}