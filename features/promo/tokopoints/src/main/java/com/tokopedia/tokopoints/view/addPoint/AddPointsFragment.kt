package com.tokopedia.tokopoints.view.addPoint

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.AddPointGridViewHolder
import com.tokopedia.tokopoints.view.adapter.AddPointsAdapter
import com.tokopedia.tokopoints.view.model.addpointsection.SectionsItem
import com.tokopedia.tokopoints.view.model.addpointsection.SheetHowToGetV2
import kotlinx.android.synthetic.main.tp_add_point_section.*
import kotlinx.android.synthetic.main.tp_add_point_section.view.*
import javax.inject.Inject
import android.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Success
import kotlin.math.roundToInt


class AddPointsFragment : BottomSheetDialogFragment(), TokopointAddPointContract.View, AddPointGridViewHolder.ListenerItemClick {

    @Inject
    lateinit var factory : ViewModelFactory

    private val  viewModel: AddPointViewModel by lazy { ViewModelProviders.of(this,factory).get(AddPointViewModel::class.java) }

    private val addPointsAdapter: AddPointsAdapter by lazy { AddPointsAdapter(ArrayList(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setStyle(STYLE_NORMAL, com.tokopedia.design.R.style.TransparentBottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        val d = dialog as BottomSheetDialog
        val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        bottomSheet?.let {
            BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(it).skipCollapsed = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.tp_add_point_section, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        viewModel.getRewardPoint()
        addObserver()
    }

    private fun addObserver() {
        addSheetObserver()
    }

    private fun addSheetObserver() = viewModel.sheetLiveData.observe(this , Observer {
        it?.let {
            when(it){
                is Loading -> inflateContainerLayout(false)
                is Success -> {
                    inflateContainerLayout(true)
                    inflatePointsData(it.data)
                }
            }
        }
    })

    private fun initView(view: View) {
        view.rv_section.layoutManager = LinearLayoutManager(context)
        view.rv_section.adapter = addPointsAdapter
        view.rv_section.setHasFixedSize(true)
    }

    override fun inflatePointsData(item: SheetHowToGetV2) {

        view?.tvTitleAddPoint?.text = item.title
        view?.tvDescription?.text = item.subTitle

        addPointsAdapter.item.clear()
        addPointsAdapter.item.addAll(item.sections as ArrayList<SectionsItem>)
        addPointsAdapter.notifyDataSetChanged()
        populateAddPointContainer()
    }

    private fun populateAddPointContainer() {
        if (addPointsAdapter.item.isEmpty()) {
            rv_section.visibility = View.GONE
        } else
            rv_section.visibility = View.VISIBLE
    }

    override fun inflateContainerLayout(success: Boolean) {
        if (success) {
            containerData.visibility = View.VISIBLE
            shimmerView.visibility = View.GONE

        } else {
            setItemInGridLayout()
            shimmerView.visibility = View.VISIBLE
            containerData.visibility = View.GONE
        }
    }

    fun initInjector() {
        DaggerTokopointBundleComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .tokopointsQueryModule(TokopointsQueryModule(activity as BaseSimpleActivity))
                .build()
                .inject(this)
    }

    override fun onClickItem(appLink: String?) {
        RouteManager.route(context, appLink)
    }

    fun setItemInGridLayout() {

        var index = 0
        var column = 0
        var row = 0
        val columnCount = 4
        val rowCount = 4
        val total = 16
        val resources = context?.resources
        val metrics = resources?.displayMetrics
        val screenWidth = metrics?.widthPixels
        val shimmerGridItemWidth = screenWidth?.div(rowCount)

        while (index < total) {
            if (column == columnCount) {
                column = 0
                row++
            }
            val gridlayout = view?.findViewById<GridLayout>(R.id.gridlayout)
            val v = View.inflate(context, R.layout.tp_placeholder_addpoint_item, null)
            val param = GridLayout.LayoutParams()
            if (shimmerGridItemWidth != null) {
                param.width = shimmerGridItemWidth
            }
            param.height = ViewGroup.LayoutParams.WRAP_CONTENT
            param.topMargin = dpToPx(20f, metrics)
            param.columnSpec = GridLayout.spec(column)
            param.rowSpec = GridLayout.spec(row)
            v.layoutParams = param
            gridlayout?.addView(v)
            index++
            column++
        }
    }

    fun dpToPx(dp: Float, metrics: DisplayMetrics?): Int {
        metrics?.let {
            val px = dp * (it.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            return px.roundToInt()
        }
        return 0
    }
}
