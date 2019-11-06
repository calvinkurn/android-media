package com.tokopedia.tokopoints.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokoPointComponent
import com.tokopedia.tokopoints.view.adapter.AddPointAdapterVH
import com.tokopedia.tokopoints.view.adapter.AddPointsAdapter
import com.tokopedia.tokopoints.view.adapter.AddPointsItemDecoration
import com.tokopedia.tokopoints.view.contract.TokopointAddPointContract
import com.tokopedia.tokopoints.view.model.addpointsection.CategoriesItem
import com.tokopedia.tokopoints.view.presenter.AddPointPresenter
import kotlinx.android.synthetic.main.tp_add_point_section.*
import kotlinx.android.synthetic.main.tp_add_point_section.view.*
import java.util.*
import javax.inject.Inject

class AddPointsFragment : BottomSheetDialogFragment(), TokopointAddPointContract.View, AddPointAdapterVH.ListenerItemClick {

    override fun onClickItem(appLink: String?) {
        RouteManager.route(context, appLink)
    }

    @Inject
    lateinit var addPointPresenter: AddPointPresenter

    private val addPointsAdapter: AddPointsAdapter by lazy { AddPointsAdapter(ArrayList(), this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        addPointPresenter.attachView(this)
        setStyle(STYLE_NORMAL, com.tokopedia.design.R.style.TransparentBottomSheetDialogTheme)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.tp_add_point_section, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        addPointPresenter.getRewardPoint(resources)
    }

    private fun initView(view: View) {
        view.rv_section.layoutManager = GridLayoutManager(context, 4)
        view.rv_section.addItemDecoration(AddPointsItemDecoration())
        view.rv_section.adapter = addPointsAdapter
    }

    override fun inflatePointsData(item: ArrayList<CategoriesItem>) {
        addPointsAdapter.item.clear()
        addPointsAdapter.item.addAll(item)
        addPointsAdapter.notifyDataSetChanged()
        populateAddPointContainer()
    }

    private fun populateAddPointContainer() {
        if (addPointsAdapter.item.isEmpty()) {
            rv_section.visibility = View.GONE
        } else
            rv_section.visibility = View.VISIBLE
    }

    fun initInjector() {
        DaggerTokoPointComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

}
