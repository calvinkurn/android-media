package com.tokopedia.statistic.presentation.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.statistic.R
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.presentation.view.viewmodel.StatisticViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_stc_statistic.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(), WidgetListener {

    companion object {
        private const val TOAST_DURATION = 5000L

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

    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>) {

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
                        val isCardWidget = adapter.data[position].widgetType != WidgetType.CARD
                        if (isCardWidget) spanCount else 1
                    } catch (e: IndexOutOfBoundsException) {
                        spanCount
                    }
                }
            }
        }
        recyclerView.layoutManager = gridLayoutManager
    }

    private fun selectDateRange() {

    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        recyclerView.visible()
        view?.globalErrorStc?.gone()

        adapter.data.clear()
        widgetHasMap.clear()

        adapter.data.addAll(widgets)
        widgets.forEach {
            if (widgetHasMap[it.widgetType].isNullOrEmpty()) {
                widgetHasMap[it.widgetType] = mutableListOf(it)
                return@forEach
            }
            widgetHasMap[it.widgetType]?.add(it)
        }

        renderWidgetOrGetWidgetDataFirst(widgets)
    }

    /**
     * If first load then directly render widget so it show widget shimmer
     * Else it should get all widgets data then render the widget
     * */
    private fun renderWidgetOrGetWidgetDataFirst(widgets: List<BaseWidgetUiModel<*>>) {
        if (isFirstLoad)
            renderList(widgets)
        else
            getWidgetsData(widgets)

        isFirstLoad = false
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEachIndexed { i, widget ->
            when (widget.widgetType) {
                WidgetType.CARD -> getCardData()
                WidgetType.LINE_GRAPH -> getLineGraphData()
                WidgetType.PROGRESS -> getProgressData()
                WidgetType.CAROUSEL -> getCarouselData()
                WidgetType.POST_LIST -> getPostData()
                else -> adapter.notifyItemChanged(i)
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
}