package com.tokopedia.quest_widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.applink.RouteManager
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.quest_widget.R
import com.tokopedia.quest_widget.data.QuestData
import com.tokopedia.quest_widget.di.DaggerQuestComponent
import com.tokopedia.quest_widget.listeners.QuestWidgetCallbacks
import com.tokopedia.quest_widget.tracker.QuestSource
import com.tokopedia.quest_widget.tracker.QuestTracker
import com.tokopedia.quest_widget.tracker.QuestTrackerImpl
import com.tokopedia.quest_widget.util.ConnectionLiveData
import com.tokopedia.quest_widget.util.LiveDataResult
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class QuestWidgetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), HandleError {

    private var position: Int = -1
    private val DIRECTION_RIGHT = "right"
    private val DIRECTION_LEFT = "left"
    val questTracker = QuestTracker()

    @QuestSource
    private var source = QuestSource.DEFAULT

    private lateinit var viewModel: QuestWidgetViewModel
    private var tvLabel: Typography
    private var tvLihat: Typography
    private var rvQuestWidget: RecyclerView
    private var shimmerQuestWidget: ConstraintLayout
    private var constraintLayoutQuestWidget: ConstraintLayout
    private var questWidgetLogin: DeferredImageView
    private var rvError: RecyclerView
    var userSession: UserSessionInterface
    private lateinit var page: String
    private lateinit var questWidgetCallbacks: QuestWidgetCallbacks

    private var reload = false

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

        val connectionLiveData = ConnectionLiveData(context)
        connectionLiveData.observe(context as AppCompatActivity, {
            if(!it){
                reload = true
                showErrorUi()
            }
            else{
                if(reload)
                    getQuestList(0, "", page = page, source = source)
            }
        })

        viewModel.questWidgetListLiveData.removeObservers(context as AppCompatActivity)
        viewModel.questWidgetListLiveData.observe(context as AppCompatActivity, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    if (isConnectedToInternet()) {
                        shimmerQuestWidget.show()
                    } else {
                        showErrorUi()
                    }
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    showSuccessUi(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    showErrorUi()
                }
                LiveDataResult.STATUS.NON_LOGIN -> {
                    if (isConnectedToInternet()) {
                        showNonLoginUi()
                    } else {
                        showErrorUi()
                    }
                }
                LiveDataResult.STATUS.EMPTY_DATA ->{
                    hide()

                    val params: ViewGroup.LayoutParams = this.layoutParams
                    params.height = 0
                    params.width = 0
                    this.layoutParams = params
                    questWidgetCallbacks.deleteQuestWidget()
                }
            }
        })
    }

    private fun showNonLoginUi() {

        shimmerQuestWidget.hide()
        constraintLayoutQuestWidget.show()
        rvQuestWidget.hide()
        questWidgetLogin.show()
        tvLihat.text = context.getString(R.string.quest_widget_lihat_semua)
        tvLabel.text = context.getString(R.string.quest_widget_quest_label)
        setupNonLoginClickListeners()
    }

    private fun showSuccessUi(data: QuestData?) {
        questWidgetLogin.hide()
        shimmerQuestWidget.hide()
        rvError.hide()
        rvQuestWidget.show()
        constraintLayoutQuestWidget.show()
        setData(data)
    }

    private fun showErrorUi() {
        questWidgetLogin.hide()
        shimmerQuestWidget.hide()
        rvQuestWidget.hide()
        constraintLayoutQuestWidget.show()
        rvError.show()
        rvError.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvError.adapter = QuestWidgetErrorAdapter(this)
        tvLihat.text = context.getString(R.string.quest_widget_lihat_semua)
        tvLabel.text = context.getString(R.string.quest_widget_quest_label)
    }

    fun isConnectedToInternet(): Boolean {
        context?.let {
            return DeviceConnectionInfo.isConnectCellular(it) ||
                    DeviceConnectionInfo.isConnectWifi(it)
        }
        return false
    }

    fun setupListeners(listener: QuestWidgetCallbacks) {
        questWidgetCallbacks = listener
    }

    private fun setupNonLoginClickListeners() {

        tvLihat.setOnClickListener {
            questWidgetCallbacks.questLogin()
        }

        questWidgetLogin.setOnClickListener {
            questWidgetCallbacks.questLogin()
        }

    }

    private fun setData(data: QuestData?) {

        if(data?.widgetData?.questWidgetList?.pageDetail?.title.isNullOrEmpty()){
            tvLabel.hide()
            tvLihat.hide()
        }

        tvLabel.text = data?.widgetData?.questWidgetList?.pageDetail?.title
        tvLihat.text = data?.widgetData?.questWidgetList?.pageDetail?.cta?.text

        tvLihat.setOnClickListener {

            //tracker event
            questTracker.clickLihatButton(source)
            questWidgetCallbacks.updateQuestWidget(position)

            data?.widgetData?.questWidgetList?.pageDetail?.cta?.applink?.let {
                try {
                    RouteManager.route(context, it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        val list = data?.widgetData?.questWidgetList

        list?.let {
            rvQuestWidget.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = QuestWidgetAdapter(
                data.widgetData.questWidgetList.questWidgetList,
                data.config,
                questTracker,
                source,
                questWidgetCallbacks,
                this.position
            )
            rvQuestWidget.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                private var direction = ""

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            questTracker.slideQuestCard(source, "slide $direction")
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    when {
                        dx > 0 -> {
                            direction = DIRECTION_RIGHT
                        }
                        dx < 0 -> {
                            direction = DIRECTION_LEFT
                        }
                        else -> {

                        }
                    }
                }
            })
            rvQuestWidget.adapter = adapter
        }

    }

    // the only call required to setup this widget
    fun getQuestList(
        channel: Int = 0,
        channelSlug: String = "",
        page: String,
        @QuestSource source: Int = QuestSource.DEFAULT,
        position: Int = -1
    ) {
        this.position = position
        this.source = source
        this.page = page
        val userSession = UserSession(context)

        viewModel.getWidgetList(channel, channelSlug, page, userSession)
    }

    override fun retry() {
        constraintLayoutQuestWidget.hide()
        shimmerQuestWidget.show()
        getQuestList(0, "", this.page, this.source)
    }

    fun setTrackerImpl(questTrackerImpl: QuestTrackerImpl){
        questTracker.trackerImpl = questTrackerImpl
    }
}