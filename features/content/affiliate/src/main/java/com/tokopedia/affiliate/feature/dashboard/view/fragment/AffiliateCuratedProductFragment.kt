package com.tokopedia.affiliate.feature.dashboard.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.feature.dashboard.di.DaggerDashboardComponent
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
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateCuratedProductFragment : BaseListFragment<DashboardItemViewModel, DashboardItemTypeFactory>(), AffiliateCuratedProductContract.View, CuratedProductSortTypeFactoryImpl.OnClickListener {

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

    private val type: Int? by lazy { if (arguments?.containsKey(EXTRA_TYPE) == true) arguments?.getInt(EXTRA_TYPE) else null }

    private var currentSort: Int = 1

    private lateinit var cvSort: CardView

    private lateinit var sortDialog: CloseableBottomSheetDialog
    private lateinit var sortDialogView: View

    private val sortAdapter: CuratedProductSortAdapter by lazy { CuratedProductSortAdapter(CuratedProductSortTypeFactoryImpl(this), emptyList()) }

    @Inject
    lateinit var presenter: AffiliateCuratedProductPresenter

    override fun getAdapterTypeFactory(): DashboardItemTypeFactory {
        return DashboardItemTypeFactoryImpl(object : DashboardItemTypeFactoryImpl.OnClickListener {
            override fun onDashboardItemClickedListener(item: DashboardItemViewModel) {
                onItemClicked(item)
            }

            override fun onBuyClick(appLink: String) {
                openApplink(appLink)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        return inflater.inflate(R.layout.fragment_af_curated_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    private fun initView(view: View) {
        view.run {
            cvSort = findViewById(R.id.cv_sort)
        }
    }

    private fun setupView(view: View) {
        if (type == null) cvSort.visible() else cvSort.gone()
        cvSort.setOnClickListener { showSortBottomSheet() }
    }

    override fun onItemClicked(t: DashboardItemViewModel?) {

    }

    override fun getScreenName(): String = "Product Bought"

    override fun initInjector() {
        val affiliateComponent = DaggerAffiliateComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication).baseAppComponent).build() as DaggerAffiliateComponent

        DaggerDashboardComponent.builder()
                .affiliateComponent(affiliateComponent)
                .build().inject(this)
    }

    override fun loadData(page: Int) {
        if (page == 0) resetState()
        presenter.loadProductBoughtByType(type, cursor, currentSort)
    }

    override fun onErrorGetDashboardItem(error: String) {

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
        presenter.detachView()
    }

    override fun onGetSortOptions(sortList: List<CuratedProductSortViewModel>) {
        sortAdapter.setElements(sortList)
        sortDialog.show()
    }

    override fun onSuccessReloadSortOptions(sortList: List<CuratedProductSortViewModel>) {
        sortAdapter.setElements(sortList)
    }

    override fun onSortClicked(sortId: Int) {
        presenter.reloadSortOptions(sortAdapter.list as List<CuratedProductSortViewModel>, sortId)
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
        else sortDialog.show()
    }

    private fun initSortDialogView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_af_sort_curated, null, false)
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
        currentSort = (sortAdapter.list as List<CuratedProductSortViewModel>).firstOrNull { it.isChecked }?.id ?: 1
        sortDialog.dismiss()
        loadData(0)
    }

    private fun resetState() {
        cursor = ""
        adapter.clearAllElements()
    }
}