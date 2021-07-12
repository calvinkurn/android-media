package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
import com.tokopedia.affiliate.feature.dashboard.view.activity.AffiliateCuratedProductActivity
import com.tokopedia.affiliate.feature.dashboard.view.activity.CommissionDetailActivity
import com.tokopedia.affiliate.feature.dashboard.view.adapter.CuratedProductSortAdapter
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.CuratedProductSortTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactory
import com.tokopedia.affiliate.feature.dashboard.view.adapter.factory.DashboardItemTypeFactoryImpl
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract
import com.tokopedia.affiliate.feature.dashboard.view.presenter.AffiliateCuratedProductPresenter
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.CuratedProductSortViewModel
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.EmptyState
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateCuratedProductFragment : BaseListFragment<DashboardItemViewModel, DashboardItemTypeFactory>(), AffiliateCuratedProductContract.View, CuratedProductSortTypeFactoryImpl.OnClickListener {

    interface CuratedProductListener {
        fun shouldShareProfile()
    }

    companion object {
        private const val EXTRA_TYPE = "type"

        fun newInstance(type: Int?): AffiliateCuratedProductFragment {
            val fragment = AffiliateCuratedProductFragment()
            val args = Bundle()
            type?.let { args.putInt(EXTRA_TYPE, type) }
            fragment.arguments = args
            return fragment
        }
    }

    override val ctx: Context?
        get() = context

    private var cursor: String = ""

    var startDate: Date? = null
    var endDate: Date? = null

    private val type: Int? by lazy { if (arguments?.containsKey(EXTRA_TYPE) == true) arguments?.getInt(EXTRA_TYPE) else null }

    private var currentSort: Int? = null

    private lateinit var cvSort: CardView
    private lateinit var tvSort: TextView
    private lateinit var svEmptyState: NestedScrollView
    private lateinit var esShareNow: EmptyState

    private lateinit var sortDialog: CloseableBottomSheetDialog
    private lateinit var sortDialogView: View

    private val sortAdapter: CuratedProductSortAdapter by lazy { CuratedProductSortAdapter(CuratedProductSortTypeFactoryImpl(this), emptyList()) }

    private var listener: CuratedProductListener? = null

    @Inject
    lateinit var presenter: AffiliateCuratedProductPresenter

    override fun getAdapterTypeFactory(): DashboardItemTypeFactory {
        return DashboardItemTypeFactoryImpl(object : DashboardItemTypeFactoryImpl.OnClickListener {
            override fun onDashboardItemClickedListener(item: DashboardItemViewModel) {
                onDashboardItemClicked(item)
            }

            override fun onBuyClick(appLink: String) {
                openApplink(appLink)
            }
        })
    }

    override fun hasInitialSwipeRefresh(): Boolean = activity is AffiliateCuratedProductActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        return inflater.inflate(
                if (hasInitialSwipeRefresh()) R.layout.fragment_af_curated_product_refresh
                else R.layout.fragment_af_curated_product,
                container,
                false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipe_refresh_layout
    }

    private fun initView(view: View) {
        view.run {
            cvSort = findViewById(R.id.cv_sort)
            tvSort = findViewById(R.id.tv_sort)
            svEmptyState = findViewById(R.id.sv_empty_state)
            esShareNow = findViewById(R.id.es_share_now)
        }
    }

    private fun setupView(view: View) {
        tvSort.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_sort_grayscale_24), null, null, null)
        if (type == null) {
            cvSort.visible()
            getRecyclerView(view)?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) cvSort.gone()
                    else cvSort.visible()
                }
            })
        } else cvSort.gone()
        cvSort.setOnClickListener { showSortBottomSheet() }

        esShareNow.setPrimaryCTAClickListener { shouldShareProfile() }
    }

    override fun onItemClicked(model: DashboardItemViewModel?) {

    }

    override fun getScreenName(): String = "Curated Product List"

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun loadData(page: Int) {
        if (view == null || !::presenter.isInitialized) {
            return
        }

        if (page == 1) resetState()
        presenter.loadCuratedProductByType(type, cursor, currentSort, startDate, endDate)
    }

    override fun onErrorGetDashboardItem(error: String) {
        adapter.clearAllElements()
        NetworkErrorHelper.showEmptyState(activity,
                view,
                error
        ) { loadInitialData() }
    }

    override fun onSuccessLoadMoreDashboardItem(itemList: List<DashboardItemViewModel>, cursor: String) {
        if (this.cursor.isEmpty()) clearAllData()
        renderList(itemList, cursor.isNotEmpty())
        this.cursor = if (itemList.isEmpty()) "" else cursor
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
        presenter.detachView()
    }

    override fun onGetSortOptions(sortList: List<CuratedProductSortViewModel>) {
        sortAdapter.setElements(sortList)
        sortDialog.show()
    }

    override fun onSuccessReloadSortOptions(sortList: List<CuratedProductSortViewModel>) {
        sortAdapter.setElements(sortList)
    }

    override fun onSortClicked(sortId: Int?) {
        presenter.reloadSortOptions(sortAdapter.list as List<CuratedProductSortViewModel>, sortId)
    }

    override fun showEmpty() {
        svEmptyState.visible()
        getRecyclerView(view)?.gone()
    }

    fun setListener(listener: CuratedProductListener) {
        this.listener = listener
    }

    private fun openApplink(applink: String) {
        if (RouteManager.isSupportApplink(context, applink)) {
            RouteManager.route(context, applink)
        }
    }

    private fun showSortBottomSheet() {
        if (!::sortDialogView.isInitialized) sortDialogView = initSortDialogView()
        if (!::sortDialog.isInitialized) sortDialog = CloseableBottomSheetDialog.createInstanceRounded(context).apply {
            setCustomContentView(sortDialogView, "", false)
            setCancelable(false)
        }
        if (sortAdapter.itemCount == 0) presenter.loadSortOptions()
        else {
            onSortClicked(currentSort)
            sortDialog.show()
        }
    }

    private fun initSortDialogView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_af_sort_curated, null, false)
        val tvSave = view.findViewById<TextView>(R.id.tv_save)
        tvSave.setOnClickListener { onSaveSortOption() }

        val ivClose = view.findViewById<AppCompatImageView>(R.id.iv_close)
        ivClose.setOnClickListener { sortDialog.dismiss() }

        val rvSort = view.findViewById<RecyclerView>(R.id.rv_sort_option)
        rvSort.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = sortAdapter
        }
        return view
    }

    private fun onSaveSortOption() {
        currentSort = (sortAdapter.list as List<CuratedProductSortViewModel>).firstOrNull { it.isChecked }?.id
                ?: 1
        sortDialog.dismiss()
        loadInitialData()
    }

    private fun resetState() {
        cursor = ""
        adapter.clearAllElements()
        svEmptyState.gone()
        getRecyclerView(view)?.visible()
    }

    private fun shouldShareProfile() {
        listener?.shouldShareProfile()
    }

    private fun onDashboardItemClicked(item: DashboardItemViewModel) {
        context?.let {
            val affPostId = item.id
            if (affPostId != null) {
                startActivity(
                        CommissionDetailActivity.newInstance(it, affPostId)
                )
            }
        }
    }
}