package com.tokopedia.quest_widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quest_widget.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.di.DaggerQuestComponent
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class QuestWidgetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs) {

    companion object{
        const val LOADING = 0
        const val DATA = 1
        const val ERROR = 2
        const val LOGIN = 3
    }

    private lateinit var viewModel: QuestWidgetViewModel
    private var tvLabel: Typography
    private var tvLihat: Typography
    private var rvQuestWidget: RecyclerView
    private var shimmerQuestWidget: ConstraintLayout
    private var constraintLayoutQuestWidget: ConstraintLayout
    private var questWidgetError: LinearLayout
    private var questWidgetLogin: LinearLayout
    var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    init {

        View.inflate(context, R.layout.quest_widget_view, this)

        tvLabel = findViewById(R.id.tv_label)
        tvLihat = findViewById(R.id.tv_lihat)
        rvQuestWidget = findViewById(R.id.rv_quest_widget)
        shimmerQuestWidget = findViewById(R.id.quest_widget_shimmer)
        constraintLayoutQuestWidget = findViewById(R.id.constraintLayoutQuestWidget)
        questWidgetError = findViewById(R.id.quest_widget_error)
        questWidgetLogin = findViewById(R.id.quest_widget_login)

        DaggerQuestComponent.builder()
            .build().inject(this)
        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProvider(context, viewModelFactory)
            viewModel = viewModelProvider[QuestWidgetViewModel::class.java]
        }
        userSession = UserSession(context)

        viewModel.questWidgetListLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    shimmerQuestWidget.show()
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    shimmerQuestWidget.hide()
                    constraintLayoutQuestWidget.show()
                    setData(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    shimmerQuestWidget.hide()
                    questWidgetError.show()
                }
            }
        })
    }

    private fun setData(data: QuestData?) {

        tvLabel.text = data?.widgetData?.pageDetail?.title
        tvLihat.text = data?.widgetData?.pageDetail?.text

        val list = data?.widgetData?.questWidgetList

        list?.let {
            rvQuestWidget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = QuestWidgetAdapter(
                    data.widgetData.questWidgetList.questWidgetList,
                    data.config
                )
            rvQuestWidget.adapter = adapter
        }
    }

    // the only call required to setup this widget
    fun getQuestList(channel: Int, channelSlug: String, page: String) {
        if(userSession.isLoggedIn) {
            viewModel.getWidgetList(channel, channelSlug, page)
        }
        else{
            questWidgetLogin.show()
            shimmerQuestWidget.hide()
        }
    }
}