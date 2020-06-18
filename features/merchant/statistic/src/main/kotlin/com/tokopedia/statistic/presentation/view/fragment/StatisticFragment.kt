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
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.statistic.R
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.presentation.view.bottomsheet.SelectDateRageBottomSheet
import com.tokopedia.statistic.presentation.view.viewhelper.StatisticLayoutManager
import com.tokopedia.statistic.presentation.view.viewhelper.setOnTabSelectedListener
import com.tokopedia.statistic.presentation.view.viewmodel.StatisticViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_stc_statistic.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(), WidgetListener {

    companion object {
        private const val DEFAULT_START_DAYS = 6L
        private const val TOAST_DURATION = 5000L
        private const val SCREEN_NAME = "statistic_page_fragment"
        private const val TAG_TOOLTIP = "statistic_tooltip"

        fun newInstance(): StatisticFragment {
            return StatisticFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val mViewModel: StatisticViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticViewModel::class.java)
    }
    private val mLayoutManager by lazy { StatisticLayoutManager(context, 2) }
    private val recyclerView by lazy { super.getRecyclerView(view) }
    private val dateRangeBottomSheet by lazy { SelectDateRageBottomSheet(requireContext(), childFragmentManager) }
    private val defaultStartDate = Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_START_DAYS))
    private val defaultEndDate = Date()

    private var tabItems = listOf<Pair<String, String>>()
    private var isFirstLoad = true
    private var isErrorToastShown = false
    private var isUserScrolling = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_stc_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        hideTooltipIfExist()
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

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        DaggerStatisticComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {}

    override fun loadData(page: Int) {}

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        if (!isAdded || context == null) return
        TooltipBottomSheet(requireContext(), tooltip)
                .show(this@StatisticFragment.childFragmentManager, TAG_TOOLTIP)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        showErrorToaster()
    }

    private fun setupView() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(headerStcStatistic)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = activity.getString(R.string.stc_shop_statistic)
        }

        setDefaultRange()
        setupRecyclerView()

        tabLayoutStc.customTabMode = TabLayout.MODE_SCROLLABLE
        swipeRefreshStc.setOnRefreshListener {
            reloadPage()
        }

        globalErrorStc.setActionClickListener {
            reloadPage()
        }
    }

    private fun setDefaultRange() = view?.run {
        val headerSubTitle: String = context.getString(R.string.stc_last_n_days_cc, DEFAULT_START_DAYS)
        val startDateStr: String = DateTimeUtil.format(defaultStartDate.time, "dd")
        val endDateStr: String = DateTimeUtil.format(defaultEndDate.time, "dd MMM yyyy")
        val subTitle = "$headerSubTitle ($startDateStr - $endDateStr)"

        setHeaderSubTitle(subTitle)
    }

    private fun setHeaderSubTitle(subTitle: String) {
        view?.headerStcStatistic?.headerSubTitle = subTitle
    }

    private fun setupRecyclerView() = view?.run {
        with(mLayoutManager) {
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

            setOnScrollVertically {
                selectTabIfSectionWidget(this)
            }
        }
        recyclerView.layoutManager = mLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                isUserScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    requestVisibleWidgetsData()
                }
            }
        })

        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        (bottomSheet as? BottomSheetUnify)?.dismiss()
    }

    private fun getCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        mViewModel.getCardWidgetData(dataKeys)
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
        if (!isAdded) return
        dateRangeBottomSheet
                .setOnApplyChanges {

                }
                .show()
    }

    private fun requestVisibleWidgetsData() {
        val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
        val firstVisible: Int = layoutManager.findFirstVisibleItemPosition()
        val lastVisible: Int = layoutManager.findLastVisibleItemPosition()

        val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        adapter.data.forEachIndexed { index, widget ->
            if (index in firstVisible..lastVisible && !widget.isLoaded) {
                visibleWidgets.add(widget)
            }
        }

        if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
    }

    private fun selectTabIfSectionWidget(layoutManager: GridLayoutManager) {
        val firstVisible: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstVisible == RecyclerView.NO_POSITION) return

        val mostTopVisibleWidget: BaseWidgetUiModel<*> = adapter.data[firstVisible]
        val widgetPair: Pair<String, String> = Pair(mostTopVisibleWidget.title, mostTopVisibleWidget.dataKey)
        val tabPair: Pair<String, String> = tabItems.firstOrNull {
            it.second == widgetPair.second || it.second == widgetPair.first
        } ?: return
        val tabIndex: Int = tabItems.map { it.first }.distinct().indexOfFirst { it == tabPair.first }
        view?.tabLayoutStc?.tabLayout?.getTabAt(tabIndex)?.select()
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        recyclerView.visible()
        view?.globalErrorStc?.gone()

        super.clearAllData()
        super.renderList(widgets)
        setupTabItems()

        if (isFirstLoad) {
            recyclerView.post {
                requestVisibleWidgetsData()
            }
            isFirstLoad = false
        } else {
            requestVisibleWidgetsData()
        }
    }

    private fun setupTabItems() = view?.run {
        tabLayoutStc.tabLayout.removeAllTabs()
        var sectionTitle = ""
        tabItems = adapter.data.map {
            return@map if (it.widgetType == WidgetType.SECTION) {
                tabLayoutStc.addNewTab(it.title)
                sectionTitle = it.title
                Pair(it.title, it.title)
            } else {
                Pair(sectionTitle, it.dataKey)
            }
        }
        tabLayoutStc.tabLayout.setOnTabSelectedListener { tab ->
            scrollToPosition()
        }
        selectTabIfSectionWidget(mLayoutManager)
    }

    private fun scrollToPosition() = view?.run {
        if (isUserScrolling) return@run

        val selectedTabIndex = tabLayoutStc.tabLayout.selectedTabPosition
        val tabTitle: String = try {
            tabItems.map { it.first }.distinct()[selectedTabIndex]
        } catch (e: IndexOutOfBoundsException) {
            ""
        }
        val adapterIndex: Int = adapter.data.indexOfFirst { it.title == tabTitle }
        if (adapterIndex != RecyclerView.NO_POSITION) {
            mLayoutManager.scrollToPositionWithOffset(adapterIndex, 0)
            recyclerView.post {
                requestVisibleWidgetsData()
            }
        }
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets: Map<String, List<BaseWidgetUiModel<*>>> = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.CARD]?.run { getCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { getLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { getProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { getCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { getPostData(this) }
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
     * if any widget that failed when load their data, the action should be load the widget data.
     * else, reload the page like pull refresh
     * */
    private fun reloadPageOrLoadDataOfErrorWidget() {
        val isAnyErrorWidget = adapter.data.any { !it.data?.error.isNullOrBlank() }
        if (!isAnyErrorWidget) {
            reloadPage()
            return
        }

        isErrorToastShown = false

        val errorWidgets: List<BaseWidgetUiModel<*>> = adapter.data.filterIndexed { index, widget ->
            val isWidgetError = !widget.data?.error.isNullOrBlank()
            if (isWidgetError) {
                //set data to null then notify adapter to show the widget shimmer
                widget.data = null
                adapter.notifyItemChanged(index)
            }
            return@filterIndexed isWidgetError
        }

        if (errorWidgets.isNotEmpty()) {
            getWidgetsData(errorWidgets)
        }
    }

    private fun reloadPage() = view?.run {
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
        adapter.data.filter { it.widgetType == widgetType }
                .forEach { widget ->
                    if (widget is W) {
                        val widgetData = D::class.java.newInstance().apply {
                            error = message
                        }
                        widget.data = widgetData
                        notifyWidgetChanged(widget)
                    }
                }

        showErrorToaster()
        recyclerView.post {
            requestVisibleWidgetsData()
        }
    }

    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState() {
        forEach { widgetData ->
            adapter.data.find { it.dataKey == widgetData.dataKey }?.let { widget ->
                if (widget is W) {
                    widget.data = widgetData
                    notifyWidgetChanged(widget)
                }
            }
        }
        recyclerView.post {
            requestVisibleWidgetsData()
        }
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
                is Success -> result.data.setOnSuccessWidgetState()
                is Fail -> result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
            }
        })
    }
}