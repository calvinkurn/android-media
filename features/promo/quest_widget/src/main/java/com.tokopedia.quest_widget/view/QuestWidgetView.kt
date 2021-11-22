package com.tokopedia.quest_widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quest_widget.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.data.WidgetData
import com.tokopedia.quest_widget.di.DaggerQuestComponent
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class QuestWidgetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs), HandleError {

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
    private var questWidgetLogin: ImageUnify
    private var rvError: RecyclerView
    var userSession: UserSessionInterface
    private lateinit var page: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    init {

        View.inflate(context, R.layout.quest_widget_view, this)

        tvLabel = findViewById(R.id.tv_label)
        tvLihat = findViewById(R.id.tv_lihat)
        rvQuestWidget = findViewById(R.id.rv_quest_widget)
        shimmerQuestWidget = findViewById(R.id.quest_widget_shimmer)
        constraintLayoutQuestWidget = findViewById(R.id.constraintLayoutQuestWidget)
        questWidgetLogin = findViewById(R.id.iv_login)

        rvError = findViewById(R.id.rv_error)

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
                    rvQuestWidget.hide()
                    constraintLayoutQuestWidget.show()
                    rvError.show()
                    rvError.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    rvError.adapter = QuestWidgetErrorAdapter(this)
                    tvLihat.text = context.getString(R.string.lihat_semua)
                    tvLabel.text = context.getString(R.string.quest_label)
                }
                LiveDataResult.STATUS.NON_LOGIN -> {
                    shimmerQuestWidget.hide()
                    constraintLayoutQuestWidget.show()
                    rvQuestWidget.hide()
                    questWidgetLogin.show()
                    tvLihat.text = context.getString(R.string.lihat_semua)
                    tvLabel.text = context.getString(R.string.quest_label)
                }
            }
        })
    }

    private fun setData(data: QuestData?) {

        shimmerQuestWidget.hide()
        constraintLayoutQuestWidget.show()

        tvLabel.text = data?.widgetData?.questWidgetList?.pageDetail?.title
        tvLihat.text = data?.widgetData?.questWidgetList?.pageDetail?.cta?.text

        val list = data?.widgetData?.questWidgetList

        list?.let {
            rvQuestWidget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = QuestWidgetAdapter(
                    data.widgetData.questWidgetList.questWidgetList,
                    data.config
                )
            rvQuestWidget.adapter = adapter
        }

        tvLihat.setOnClickListener {
            data?.widgetData?.questWidgetList?.pageDetail?.cta?.url?.let {
                try {
                    RouteManager.route(context, it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        questWidgetLogin.setOnClickListener {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }

    }

    // the only call required to setup this widget
    fun getQuestList(channel: Int = 0, channelSlug: String = "", page: String) {

        this.page = page
        val userSession = UserSession(context)
        viewModel.getWidgetList(channel, channelSlug, page, userSession)
    }

    fun setQuestData(questData: QuestData){
        shimmerQuestWidget.hide()
        constraintLayoutQuestWidget.show()
        setData(questData)
    }

    override fun retry() {
        constraintLayoutQuestWidget.hide()
        shimmerQuestWidget.show()
        getQuestList(0, "", this.page)
    }
}