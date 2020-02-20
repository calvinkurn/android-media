package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.di.PreferenceListComponent
import kotlinx.android.synthetic.main.fragment_preference_list.*
import javax.inject.Inject

class PreferenceListFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PreferenceListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PreferenceListViewModel::class.java]
    }

    private val adapter = PreferenceListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preference_list, container, false)
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceListComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        viewModel.preferenceList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initViews() {
        btn_preference_list_action.setOnClickListener {
            if (group_empty_state.visibility == View.GONE) {
                rv_preference_list.visibility = View.GONE
                swipe_refresh_layout.isRefreshing = true
                group_empty_state.visibility = View.VISIBLE
            } else {
//                group_empty_state.visibility = View.GONE
//                rv_preference_list.visibility = View.VISIBLE
                startActivity(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT))
            }
        }
        rv_preference_list.adapter = adapter
        rv_preference_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_preference_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
//                val position = parent.getChildAdapterPosition(view)
//                val lastIndex = (parent.adapter?.itemCount ?: 1) - 1
//                if (position >= lastIndex) {
//                    outRect.bottom =
//                }
                outRect.left = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.right = context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
                outRect.top = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
                outRect.bottom = context?.resources?.getDimension(R.dimen.dp_8)?.toInt() ?: 0
            }
        })
    }
}
