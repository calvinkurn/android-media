package com.tokopedia.statistic.presentation.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.statistic.R
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.presentation.view.viewmodel.StatisticViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_stc_statistic.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(), WidgetListener {

    companion object {
        private const val TOAST_DURATION = 5000L
        private const val DELAY_FETCH_VISIBLE_WIDGET_DATA = 500L

        fun newInstance(): StatisticFragment {
            return StatisticFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: StatisticViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticViewModel::class.java)
    }
    private val recyclerView by lazy { super.getRecyclerView(view) }

    private var widgetHasMap = hashMapOf<String, MutableList<BaseWidgetUiModel<*>>>()
    private var isFirstLoad = true
    private var isErrorToastShown = false
    private var hasLoadCardData = false
    private var hasLoadLineGraphData = false
    private var hasLoadProgressData = false
    private var hasLoadPostData = false
    private var hasLoadCarouselData = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_stc_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()

        observeWidgetLayoutLiveData()
        observeWidgetData(mViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(mViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(mViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(mViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(mViewModel.carouselWidgetData, WidgetType.CAROUSEL)
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad) {
            reloadPage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_stc_action_calendar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
            R.id.actionStcSelectDate -> selectDateRange()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvStcWidgets

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl {
        return WidgetAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        DaggerStatisticComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {}

    override fun loadData(page: Int) {}

    override fun onTooltipClicked(tooltip: TooltipUiModel) {

    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
            widgetHasMap[widget.widgetType]?.remove(widget)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        showErrorToaster()
    }

    override fun getCardData() {
        if (hasLoadCardData) return
        hasLoadCardData = true
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(adapter.data)
        mViewModel.getCardWidgetData(dataKeys)
    }

    override fun getLineGraphData() {
        if (hasLoadLineGraphData) return
        hasLoadLineGraphData = true
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(adapter.data)
        mViewModel.getLineGraphWidgetData(dataKeys)
    }

    override fun getProgressData() {
        if (hasLoadProgressData) return
        hasLoadProgressData = true
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(adapter.data)
        mViewModel.getProgressWidgetData(dataKeys)
    }

    override fun getPostData() {
        if (hasLoadPostData) return
        hasLoadPostData = true
        val dataKeys = Utils.getWidgetDataKeys<PostListWidgetUiModel>(adapter.data)
        mViewModel.getPostWidgetData(dataKeys)
    }

    override fun getCarouselData() {
        if (hasLoadCarouselData) return
        hasLoadCarouselData = true
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(adapter.data)
        mViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun getCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        mViewModel.getCardWidgetData(dataKeys)
    }

    private fun setupView() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(headerStcStatistic)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = activity.getString(R.string.stc_shop_statistic)
        }

        setupRecyclerView()

        swipeRefreshStc.setOnRefreshListener {
            reloadPage()
        }

        globalErrorStc.setActionClickListener {
            reloadPage()
        }
    }

    private fun setupRecyclerView() = view?.run {
        val gridLayoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return try {
                        val isCardWidget = adapter.data[position].widgetType == WidgetType.CARD
                        if (isCardWidget) 1 else spanCount
                    } catch (e: IndexOutOfBoundsException) {
                        spanCount
                    }
                }
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    requestVisibleWidgetsData()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun getLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        mViewModel.getLineGraphWidgetData(dataKeys)
    }

    private fun getProgressData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        mViewModel.getProgressWidgetData(dataKeys)
    }

    private fun getPostData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<PostListWidgetUiModel>(widgets)
        mViewModel.getPostWidgetData(dataKeys)
    }

    private fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        mViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun selectDateRange() {

    }

    private fun requestVisibleWidgetsData() {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val firstVisible: Int = layoutManager.findFirstVisibleItemPosition()
        val lastVisible: Int = layoutManager.findLastVisibleItemPosition()

        val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        widgetHasMap.entries.forEach { pair ->
            if (pair.key == WidgetType.CARD) {
                visibleWidgets.addAll(pair.value.filter { !it.isLoaded })
            } else {
                pair.value.forEach { widget ->
                    val widgetIndexInRecyclerView = adapter.data.indexOf(widget)
                    if (widgetIndexInRecyclerView in firstVisible..lastVisible && !widget.isLoaded) {
                        visibleWidgets.add(widget)
                    }
                }
            }
        }

        if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        recyclerView.visible()
        view?.globalErrorStc?.gone()

        super.clearAllData()
        widgetHasMap.clear()

        super.renderList(widgets)
        adapter.notifyDataSetChanged()

        widgets.forEach {
            if (widgetHasMap[it.widgetType].isNullOrEmpty()) {
                widgetHasMap[it.widgetType] = mutableListOf(it)
                return@forEach
            }
            widgetHasMap[it.widgetType]?.add(it)
        }

        if (isFirstLoad) {
            recyclerView.post {
                requestVisibleWidgetsData()
            }
            isFirstLoad = false
        } else {
            requestVisibleWidgetsData()
        }
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets: Map<String, List<BaseWidgetUiModel<*>>> = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.CARD]?.run { getCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { getLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { getProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { getCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { getPostData(this) }
        groupedWidgets[WidgetType.SECTION]?.run {
            forEach {
                if (!it.isLoaded) {
                    it.isLoaded = true
                    notifyWidgetChanged(it)
                }
            }
        }
    }

    private fun setOnErrorGetLayout(throwable: Throwable) = view?.run {
        if (adapter.data.isEmpty()) {
            globalErrorStc.visible()
            recyclerView.gone()
        } else {
            showErrorToaster()
            globalErrorStc.gone()
        }
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.make(this, context.getString(R.string.stc_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.stc_reload),
                View.OnClickListener {
                    reloadPageOrLoadDataOfErrorWidget()
                }
        )

        Handler().postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }

    /**
     * if any widget that failed when load their data, the action should be load the widget data
     * else, reload the page like pull refresh
     * */
    private fun reloadPageOrLoadDataOfErrorWidget() {
        val isAnyErrorWidget = adapter.data.any { !it.data?.error.isNullOrBlank() }
        if (!isAnyErrorWidget) {
            reloadPage()
            return
        }

        isErrorToastShown = false
        adapter.data.forEachIndexed { index, widget ->
            if (!widget.data?.error.isNullOrBlank()) {
                when (widget.widgetType) {
                    WidgetType.CARD -> {
                        hasLoadCardData = false
                        getCardData()
                    }
                    WidgetType.LINE_GRAPH -> {
                        hasLoadLineGraphData = false
                        getLineGraphData()
                    }
                    WidgetType.PROGRESS -> {
                        hasLoadProgressData = false
                        getProgressData()
                    }
                    WidgetType.CAROUSEL -> {
                        hasLoadCarouselData = false
                        getCarouselData()
                    }
                    WidgetType.POST_LIST -> {
                        hasLoadPostData = false
                        getPostData()
                    }
                }
                widget.data = null
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun reloadPage() = view?.run {
        hasLoadCardData = false
        hasLoadLineGraphData = false
        hasLoadProgressData = false
        hasLoadPostData = false
        hasLoadCarouselData = false

        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshStc.isRefreshing = isAdapterNotEmpty

        globalErrorStc.gone()
        mViewModel.getWidgetLayout()
    }

    private fun setProgressBarVisibility(isShown: Boolean) = view?.run {
        if (isShown) {
            globalErrorStc.gone()
            progressBarStc.visible()
        } else {
            swipeRefreshStc.isRefreshing = false
            progressBarStc.gone()
        }
    }

    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(widgetType: String) {
        val message = this.message.orEmpty()
        widgetHasMap[widgetType]?.forEach { widget ->
            if (widget is W) {
                widget.data = D::class.java.newInstance().apply {
                    error = message
                }
                notifyWidgetChanged(widget)
            }
        }
        showErrorToaster()
        view?.postDelayed({
            requestVisibleWidgetsData()
        }, DELAY_FETCH_VISIBLE_WIDGET_DATA)
    }

    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState(widgetType: String) {
        forEach { widgetData ->
            widgetHasMap[widgetType]?.find { it.dataKey == widgetData.dataKey }?.let { widget ->
                if (widget is W) {
                    widget.data = widgetData
                    notifyWidgetChanged(widget)
                }
            }
        }
        view?.postDelayed({
            requestVisibleWidgetsData()
        }, DELAY_FETCH_VISIBLE_WIDGET_DATA)
    }

    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        val widgetPosition = adapter.data.indexOf(widget)
        if (widgetPosition > -1) {
            adapter.notifyItemChanged(widgetPosition)
            view?.swipeRefreshStc?.isRefreshing = false
        }
    }

    private fun observeWidgetLayoutLiveData() {
        mViewModel.widgetLayout.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> setOnSuccessGetLayout(result.data)
                is Fail -> setOnErrorGetLayout(result.throwable)
            }
            setProgressBarVisibility(false)
        })

        setProgressBarVisibility(true)
        mViewModel.getWidgetLayout()
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(liveData: LiveData<Result<List<D>>>, type: String) {
        liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> {
                    result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
                }
            }
        })
    }
}