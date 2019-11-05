package com.tokopedia.tokopoints.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.AddPointsAdapter
import com.tokopedia.tokopoints.view.presenter.AddPointPresenter
import kotlinx.android.synthetic.main.tp_add_point_section.view.*
import java.util.ArrayList
import javax.inject.Inject

class AddPointsFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var addPointPresenter: AddPointPresenter

    val addPointsAdapter: AddPointsAdapter by lazy { AddPointsAdapter(ArrayList(), this) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.tp_add_point_section, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        view.rv_section.layoutManager = GridLayoutManager(context, 4)
        view.rv_section.adapter = addPointsAdapter
    }
}
