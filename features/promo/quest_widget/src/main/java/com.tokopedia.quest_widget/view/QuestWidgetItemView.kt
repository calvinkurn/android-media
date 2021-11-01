package com.tokopedia.quest_widget.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.quest_widget.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.quest_widget.data.Config
import com.tokopedia.quest_widget.data.Progress
import com.tokopedia.quest_widget.data.QuestWidgetListItem
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

const val LAGITEXT = "x lagi"
class QuestWidgetItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
): CardUnify(context, attrs) {

    private val TAG = QuestWidgetItemView::class.java.simpleName
    private var tvBannerTitle: Typography
    private var tvBannerDesc: Typography
    private var ivBannerIcon: ImageUnify
    private var progressBar: QuestProgressBar
    private var progressBarContainer : FrameLayout
    private var iconContainer : ImageUnify

    init {

        View.inflate(context, R.layout.quest_widget_item_view, this)

        tvBannerTitle = findViewById(R.id.tv_banner_title)
        tvBannerDesc = findViewById(R.id.tv_banner_desc)
        ivBannerIcon = findViewById(R.id.iv_banner_icon)
        progressBar = findViewById(R.id.progressBar)
        progressBarContainer = findViewById(R.id.progressBarContainer)
        iconContainer = findViewById(R.id.iconContainer)
    }

    @SuppressLint("SetTextI18n")
    fun setData(item: QuestWidgetListItem, config: Config) {
        tvBannerTitle.text = config.banner_title
        ivBannerIcon.loadImage(config.banner_icon_url)
        val progress = calculateProgress((item.task?.get(0)?.progress))
        if (progress != 0F) {
            iconContainer.hide()
            progressBar.show()
            progressBar.apply {
                setProgress(calculateProgress(item.task?.get(0)?.progress))
                setProgressColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                setRounded(true)
                setProgressWidth(8F)
                setProgressBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N75
                    )
                )
            }
        }
        else{
            progressBar.hide()
            iconContainer.show()
        }

        val desc = item.actionButton?.shortText + " " + (item.task?.get(0)?.progress?.current?.let {
            item.task[0]?.progress?.target?.minus(
                it
            )
        })

        tvBannerDesc.text =
            desc + LAGITEXT + " " +context.resources.getString(R.string.str_dot) + " " + item.label?.title
    }

    private fun calculateProgress(progress:Progress?):Float{
        val start:Float = progress?.current?.toFloat()?:0F
        val target:Float = progress?.target?.toFloat()?:1F

       return 100 - (((target-start) / target) * 100)
    }

    companion object {
        const val PROGRESS = 0
        const val CONTAINER = 1
    }
}

