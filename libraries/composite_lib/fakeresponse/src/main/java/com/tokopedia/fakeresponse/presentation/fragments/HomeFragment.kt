package com.tokopedia.fakeresponse.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.data.diProvider.fragments.GqlFragmentProvider
import com.tokopedia.fakeresponse.presentation.adapters.GqlRvAdapter
import com.tokopedia.fakeresponse.presentation.livedata.Fail
import com.tokopedia.fakeresponse.presentation.livedata.Loading
import com.tokopedia.fakeresponse.presentation.livedata.Success
import com.tokopedia.fakeresponse.presentation.viewmodels.FakeResponseModel


class HomeFragment : BaseFragment() {
    val STATE_EMPTY = 0
    val STATE_DATA = 1

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var rv: RecyclerView
    lateinit var viewFlipper: ViewFlipper

    lateinit var adapter: GqlRvAdapter
    lateinit var dataList: ArrayList<ResponseListData>
    lateinit var mapOfData: MutableMap<Int, ResponseListData>

    lateinit var viewModel: FakeResponseModel

    override fun getLayout() = R.layout.fake_fragment_home

    override fun initVars() {
        dataList = ArrayList()
        adapter = GqlRvAdapter(dataList, itemClickCallback = { data, isChecked ->
            viewModel.toggleGql(data as ResponseListData, isChecked)
        })
    }

    override fun initViews(view: View) {
        viewFlipper = view.findViewById(R.id.viewFlipper)
        rv = view.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(context)

        context?.let { c->
            val itemDecoration = DividerItemDecoration(c, DividerItemDecoration.VERTICAL)
            val dividerLine = ContextCompat.getDrawable(c, R.drawable.fake_divider_line)
            dividerLine?.let {
                itemDecoration.setDrawable(it)
            }
            rv.addItemDecoration(itemDecoration)
        }

        rv.adapter = adapter
        mapOfData = mutableMapOf()
    }

    override fun setupFragment() {
        injectComponents()
        setListeners()
        getData()
    }

    fun setListeners() {
        viewModel.liveData.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is Success -> {
                    if (it.data.isNotEmpty()) {
                        mapOfData.clear()
                        dataList.clear()
                        dataList.addAll(it.data)
                        dataList.forEachIndexed { index, item -> mapOfData[item.id] = item }
                        adapter.notifyDataSetChanged()
                        showDataUi()
                    } else {
                        showEmptyUi()
                    }

                }
                is Fail -> {
                }
                is Loading -> {
                }
            }
        })

        viewModel.toggleLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> toggleGql(it.data.first, it.data.second)
                is Fail -> showErrorToggle()
                else -> {
                    // no-op
                }
            }
        })
    }

    fun toggleGql(id: Int, enable: Boolean) {
        mapOfData[id]?.isChecked = enable
        adapter.notifyDataSetChanged()
    }

    fun showErrorToggle() {
        Toast.makeText(context, "Unable to toggle", Toast.LENGTH_SHORT).show()
    }

    fun showEmptyUi() {
        viewFlipper.displayedChild = STATE_EMPTY
    }

    fun showDataUi() {
        viewFlipper.displayedChild = STATE_DATA
    }

    private fun getData() {
        viewModel.getGql()
    }

    private fun injectComponents() {
        GqlFragmentProvider().inject(this)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectComponents()
    }
}