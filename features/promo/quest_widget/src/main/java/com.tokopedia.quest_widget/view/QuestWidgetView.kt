package com.tokopedia.quest_widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quest_widget.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.unifycomponents.CardUnify
import javax.inject.Inject

class QuestWidgetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardUnify(context, attrs) {

    private lateinit var viewModel: QuestWidgetViewModel
    private var tvLabel: TextView? = null
    private var tvLihat: TextView? = null
    private var rvQuestWidget: RecyclerView? = null
    private var shimmerQuestWidget: RecyclerView? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    init {

        View.inflate(context, R.layout.quest_widget_view, this)

        tvLabel = findViewById(R.id.tv_label)
        tvLihat = findViewById(R.id.tv_lihat)
        rvQuestWidget = findViewById(R.id.rv_quest_widget)
        shimmerQuestWidget = findViewById(R.id.quest_widget_shimmer)

        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context, viewModelFactory)
            viewModel = viewModelProvider[QuestWidgetViewModel::class.java]
        }

        val viewModelProvider =
            ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
        viewModel = viewModelProvider[QuestWidgetViewModel::class.java]
    }

    fun setData(data: WidgetData?) {

        tvLabel?.text = data?.pageDetail?.title
        tvLihat?.text = data?.pageDetail?.text

        val list = data?.questWidgetList

        list?.let {
            rvQuestWidget?.layoutManager = LinearLayoutManager(context)
            val adapter = data.pageDetail?.isHiddenCta?.let { it1 ->
                QuestWidgetAdapter(data.questWidgetList.questWidgetList,
                    it1
                )
            }
            rvQuestWidget?.adapter = adapter
        }
    }

    fun getQuestList(channel: Int, channelSlug: String, page: String) {
        viewModel.getWidgetList(channel, channelSlug, page)
        viewModel.questWidgetListLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    showLoading(true)
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    showLoading(false)
                    setData(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    showLoading(false)
                    handleError(it.error)
                }
            }
        })
    }

    private fun handleError(error: Throwable?) {
        visibility = View.GONE
    }

    fun showLoading(show: Boolean) {
        if (show)
            shimmerQuestWidget?.show()
        else
            shimmerQuestWidget?.hide()

    }

}